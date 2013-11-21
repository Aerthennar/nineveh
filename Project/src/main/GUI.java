package main;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;


@SuppressWarnings("serial")
public class GUI extends JFrame {
	GridBagConstraints gbc = new GridBagConstraints();
	
	public GUI(){
		super();
		/*
		JPanel pane = new JPanel();
		pane.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		*/
		JTabbedPane tab = new JTabbedPane();
		tab.addTab("Strategy", null, paneStrategy(), "Allow to adjust the parameters before starting a new simulation");
		tab.addTab("Add plant", null, paneAddPlant(), "Allow to add some plants during the simulation");
		tab.addTab("Simulation results", null, paneSimulationResults(), "Dislay the resuts of the simulation");
		add(tab);
		pack();
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public JPanel paneStrategy(){
		JPanel paneStrategy = new JPanel();
		paneStrategy.setLayout(new GridBagLayout());
		gbc.gridx = gbc.gridy = 0;
		paneStrategy.add(new JLabel("plant number"), gbc);
		
		gbc.gridy++;
		paneStrategy.add(new JLabel("damage over time rate"), gbc);
		
		gbc.gridy++;
		paneStrategy.add(new JLabel("birth rate"), gbc);
		
		String[] strat = {"Percent of the map", "User choices", "Random"};
		JComboBox<String> strategyPlant = new JComboBox<String>(strat);
		JTextField plantNb = new JTextField();
		gbc.gridx++;
		gbc.gridy = 0;
		paneStrategy.add(strategyPlant, gbc);
		if(strategyPlant.getSelectedIndex() == 1){
			gbc.gridx++;
			plantNb.setColumns(5);
			paneStrategy.add(plantNb, gbc);
		}
		
		JSlider damageOverTimeRate = new JSlider();
		gbc.gridy++;
		damageOverTimeRate.setMajorTickSpacing(25);
		damageOverTimeRate.setMinorTickSpacing(5);
		damageOverTimeRate.setPaintTicks(true);
		damageOverTimeRate.setPaintLabels(true);
		paneStrategy.add(damageOverTimeRate, gbc);
		
		JSlider birthRate = new JSlider();
		gbc.gridy++;
		birthRate.setMajorTickSpacing(25);
		birthRate.setMinorTickSpacing(5);
		birthRate.setPaintTicks(true);
		birthRate.setPaintLabels(true);
		paneStrategy.add(birthRate, gbc);
		
		gbc.insets = new Insets(0,75,0,0);
		gbc.gridx = 3;
		gbc.gridy = 0;
		paneStrategy.add(new JLabel("herbivore number"), gbc);
		
		gbc.gridy++;
		paneStrategy.add(new JLabel("damage over time rate"), gbc);
		
		gbc.gridy++;
		paneStrategy.add(new JLabel("birth rate"), gbc);
		
		gbc.insets = new Insets(0,0,0,0);
		gbc.gridx++;
		gbc.gridy = 0;
		JTextField herbivoreNb = new JTextField();
		herbivoreNb.setColumns(5);
		paneStrategy.add(herbivoreNb, gbc);

		JSlider herbivoreDamageOverTimeRate = new JSlider();
		gbc.gridy++;
		herbivoreDamageOverTimeRate.setMajorTickSpacing(25);
		herbivoreDamageOverTimeRate.setMinorTickSpacing(5);
		herbivoreDamageOverTimeRate.setPaintTicks(true);
		herbivoreDamageOverTimeRate.setPaintLabels(true);
		paneStrategy.add(herbivoreDamageOverTimeRate, gbc);
		
		JSlider herbivoreBirthRate = new JSlider();
		gbc.gridy++;
		herbivoreBirthRate.setMajorTickSpacing(25);
		herbivoreBirthRate.setMinorTickSpacing(5);
		herbivoreBirthRate.setPaintTicks(true);
		herbivoreBirthRate.setPaintLabels(true);
		paneStrategy.add(herbivoreBirthRate, gbc);

		return paneStrategy;
	}
	
	public JPanel paneAddPlant(){
		JPanel paneAddPlant = new JPanel();
		
		gbc.gridx = gbc.gridy = 0;
		paneAddPlant.add(new JLabel("life rate"), gbc);
		
		gbc.gridx++;
		JSlider lifeRate = new JSlider();
		lifeRate.setMajorTickSpacing(25);
		lifeRate.setMinorTickSpacing(5);
		lifeRate.setPaintTicks(true);
		lifeRate.setPaintLabels(true);
		paneAddPlant.add(lifeRate, gbc);
		
		gbc.gridy = 1;
		gbc.gridx = 0;
		JRadioButton buttonAddPlants = new JRadioButton("Add plant (with a click on the map)");
		paneAddPlant.add(buttonAddPlants, gbc);
		return paneAddPlant;
	}
	
	
	
	public JPanel paneSimulationResults(){
		JPanel paneSimulationResults = new JPanel();
		JTextArea text = new JTextArea();
		paneSimulationResults.add(text);
		return paneSimulationResults;
	}
	
	public static void main(String args[]){
		GUI test = new GUI();
		test.setVisible(true);
	}
}
