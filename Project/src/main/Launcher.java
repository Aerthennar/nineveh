package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Point2D;
import java.lang.reflect.Constructor;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import plug.creatures.CreaturePluginFactory;
import plug.creatures.PluginMenuItemBuilder;
import creatures.Herbivore;
import creatures.ICreature;
import creatures.IEnvironment;
import creatures.Plant;
import creatures.visual.ColorCube;
import creatures.visual.CreatureInspector;
import creatures.visual.CreatureSimulator;
import creatures.visual.CreatureVisualizer;
import main.GUI2;

/**
 * Just a simple test of the simulator.
 *
 */
@SuppressWarnings("serial")
public class Launcher extends JFrame {

	private final CreaturePluginFactory factory;

	private final CreatureInspector inspector;
	private final CreatureVisualizer visualizer;
	private final CreatureSimulator simulator;
	private GUI2 gui = new GUI2();

	private PluginMenuItemBuilder menuBuilder;
	private JMenuBar mb = new JMenuBar();        
	private Constructor<? extends ICreature> currentConstructor = null;

	public JButton restart = new JButton("(Re-)start simulation");

	ActionListener restartActionListener;

	public Launcher() {
		factory = CreaturePluginFactory.getInstance();

		setName("Creature Simulator Plugin Version");
		setLayout(new BorderLayout());

		JPanel buttons = new JPanel();
		JButton loader = new JButton("Load plugins");
		loader.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				factory.load();
				buildPluginMenus();
			}
		});
		buttons.add(loader);

		JButton reloader = new JButton("Reload plugins");
		reloader.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				factory.reload();
				buildPluginMenus();
			}
		});
		buttons.add(reloader);

		buttons.add(restart);

		defaultStartSimulationButtonListener();

		add(buttons, BorderLayout.SOUTH);

		simulator = new CreatureSimulator(new Dimension(640, 480));                
		inspector = new CreatureInspector();
		inspector.setFocusableWindowState(false);
		visualizer = new CreatureVisualizer(simulator);
		visualizer.setDebug(false);
		visualizer.setPreferredSize(simulator.getSize());

		add(visualizer, BorderLayout.CENTER);
		buildPluginMenus();

		pack();

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent evt) {
				exit(evt);
			}
		});

	}

	private void exit(WindowEvent evt) {
		System.exit(0);
	}

	public void buildPluginMenus() {        
		mb.removeAll();
		ActionListener listener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// the name of the plugin is in the ActionCommand
				currentConstructor = factory.getConstructorMap().get(((JMenuItem) e.getSource()).getActionCommand());

				// put the name of our plugin rather than "creatures.SmartCreature"
				if(currentConstructor.getName() == "creatures.Plant" || currentConstructor.getName() == "creatures.Herbivore"){
					add(gui, BorderLayout.EAST);

					plantHerbiStartSimulationButtonListener();
				} else {
					remove(gui);
					defaultStartSimulationButtonListener();
				}
				repaint();
				revalidate();
				pack();
			}
		};
		menuBuilder = new PluginMenuItemBuilder(factory.getConstructorMap(),listener);
		menuBuilder.setMenuTitle("Creatures");
		menuBuilder.buildMenu();
		mb.add(menuBuilder.getMenu());
		setJMenuBar(mb);
	}


	public static void main(String args[]) {
		Logger.getLogger("plug").setLevel(Level.INFO);
		double myMaxSpeed = 5;
		CreaturePluginFactory.init(myMaxSpeed);
		Launcher launcher = new Launcher();
		launcher.setVisible(true);
	}

	/**
	 * 	remove the old actionListener
	 * 	and set up the default one
	 */
	private void defaultStartSimulationButtonListener(){
		restart.removeActionListener(restartActionListener);
		restartActionListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (currentConstructor != null) {
					synchronized(simulator) {
						if (simulator.isRunning()) {
							simulator.stop();
						}
					}
					simulator.clearCreatures();

					Collection<? extends ICreature> creatures = factory.createCreatures(simulator, 10, new ColorCube(50),currentConstructor);
					simulator.addAllCreatures(creatures);
					simulator.start();
				}
			}
		};
		restart.addActionListener(restartActionListener);
	}

	private void plantHerbiStartSimulationButtonListener(){
		restart.removeActionListener(restartActionListener);
		restartActionListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (currentConstructor != null) {
					synchronized(simulator) {
						if (simulator.isRunning()) {
							simulator.stop();
						}
					}
					simulator.clearCreatures();	
					
					int minLifePlant = Integer.parseInt(gui.minLifePlant.getValue().toString());
					int maxLifePlant = Integer.parseInt(gui.maxLifePlant.getValue().toString());
					Plant.setMinSeed(minLifePlant);
					Plant.setMaxSeed(maxLifePlant);
					Plant.setLifeDegenSpeed(gui.damageOverTimeRate.getValue());
					
					int minLifeHerbi = Integer.parseInt(gui.minLifeHerbivore.getValue().toString());
					int maxLifeHerbi = Integer.parseInt(gui.maxLifeHerbivore.getValue().toString());
					Herbivore.setMinLife(minLifeHerbi);
					Herbivore.setMaxLife(maxLifeHerbi);
					
					Herbivore.setBirthRate(gui.herbivoreBirthRate.getValue());
					Herbivore.setEscapeRate(gui.herbivoreEscape.getValue());
					Herbivore.setLifeDegenSpeed(gui.herbivoreDamageOverTimeRate.getValue());
					Herbivore.setSeedGatheringRate(gui.seedGatheringRate.getValue());
					
					Constructor<? extends ICreature> plantConstructor = factory.getConstructorMap().get("creatures.Plant");
					Constructor<? extends ICreature> herbivoreConstructor = factory.getConstructorMap().get("creatures.Herbivore");
					String plantNumberStr = gui.plantNb.getText().toString();
					String herbivoreNumberStr = gui.herbivoreNb.getText().toString();
					int plantNumber;
					int herbivoreNumber;
					if(plantNumberStr.length() == 0){
						plantNumber = 0;
					} else {
						plantNumberStr = plantNumberStr.replaceAll("\\W", "");
						plantNumber = Integer.parseInt(plantNumberStr);
					}
					if(herbivoreNumberStr.length() == 0){
						herbivoreNumber = 0;
					} else {
						herbivoreNumberStr = herbivoreNumberStr.replaceAll("\\W", "");
						herbivoreNumber = Integer.parseInt(herbivoreNumberStr);
					}
					Collection<? extends ICreature> plants = factory.createCreatures(simulator, plantNumber, new ColorCube(50),plantConstructor);
					Collection<? extends ICreature> herbivores = factory.createCreatures(simulator, herbivoreNumber, new ColorCube(50), herbivoreConstructor);

					simulator.addAllCreatures(plants);
					simulator.addAllCreatures(herbivores);
					simulator.start();
				}
			}
		};
		restart.addActionListener(restartActionListener);
	}

}


