package main;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.MaskFormatter;

import creatures.Herbivore;
import creatures.Plant;

@SuppressWarnings("serial")
public class GUI2 extends JPanel {
	public JPanel paneConfig = new JPanel();
	public JPanel paneStrategy = new JPanel();
	public JPanel panePlant = new JPanel();
	public JPanel paneHerbivore = new JPanel();
	public JPanel paneAddPlant = new JPanel();
	public JPanel paneSimulationResults = new JPanel();

	public JFormattedTextField minLifePlant = new JFormattedTextField(
			createFormatter("###"));
	public JFormattedTextField maxLifePlant = new JFormattedTextField(
			createFormatter("###"));
	public JFormattedTextField plantNb = new JFormattedTextField(createFormatter("####"));
	String[] strat = { "Map %", "Raw Number", "Random" };
	public JComboBox<String> strategyPlant = new JComboBox<String>(strat);
	public JSlider damageOverTimeRate = new JSlider();

	public JFormattedTextField minLifeHerbivore = new JFormattedTextField(
			createFormatter("###"));
	public JFormattedTextField maxLifeHerbivore = new JFormattedTextField(
			createFormatter("###"));
	public JFormattedTextField herbivoreNb = new JFormattedTextField(
			createFormatter("####"));
	public JSlider herbivoreDamageOverTimeRate = new JSlider();
	public JSlider herbivoreBirthRate = new JSlider();
	public JSlider seedGatheringRate = new JSlider(JSlider.HORIZONTAL, 0, 100, 75);
	public JSlider herbivoreEscape = new JSlider(JSlider.HORIZONTAL, 0, 100, 50);
	public JRadioButton buttonAddPlants = new JRadioButton(
			"Add Plant");

	public JTextArea textResults = new JTextArea();

	public GUI2() {
		super();
		paneConfig.setLayout(new GridLayout(3, 1));
		paneConfig.add(paneStrategy()); // with plants parameters and herbivores
										// parameters panes
		paneConfig.add(paneAddPlant());
		paneConfig.add(paneSimulationResults());

		add(paneConfig);
		validate();
	}

	public JPanel paneStrategy() {
		paneStrategy.setLayout(new GridLayout(1, 2));
		GridBagLayout plantGBL = new GridBagLayout();
		GridBagLayout herbivoreGBL = new GridBagLayout();

		GridBagConstraints plantGBC = new GridBagConstraints();
		GridBagConstraints herbivoreGBC = new GridBagConstraints();

		// Plant
		panePlant.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1,
				Color.BLACK));
		panePlant.setLayout(plantGBL);
		plantGBC.gridx = plantGBC.gridy = 0;
		// plantGBC.gridx++;
		plantGBC.insets = new Insets(30, 110, 0, 0);
		panePlant.add(new JLabel("Plants Parameters"), plantGBC);

		plantGBC.insets = new Insets(25, -70, 0, 0);

		plantGBC.gridx = 0;
		plantGBC.gridy++;
		plantGBC.gridy++;
		panePlant.add(new JLabel("Plant Number"), plantGBC);

		plantGBC.gridy++;
		plantGBC.gridy++;
		panePlant.add(new JLabel("Life Degeneration Rate"), plantGBC);
		
		plantGBC.gridy = 1;
		//plantGBC.insets = new Insets(0,0,0,0);
		panePlant.add(new JLabel("min life"), plantGBC);
		
		//plantGBC.gridx++;
		//plantGBC.insets = new Insets(0,0,20,0);
		minLifePlant.setColumns(4);
		minLifePlant.setText("003");
		panePlant.add(minLifePlant, plantGBC);
		
		plantGBC.gridx++;
		panePlant.add(new JLabel("max life"), plantGBC);
		
		//plantGBC.gridx++;
		//plantGBC.insets = new Insets(0, 2, 0, 0);
		maxLifePlant.setColumns(4);
		maxLifePlant.setText("010");
		panePlant.add(maxLifePlant, plantGBC);
		
		plantGBC.gridx = 1;
		plantGBC.gridy = 2;
		strategyPlant.setBackground(Color.WHITE);
		strategyPlant.setEnabled(true);
		panePlant.add(strategyPlant, plantGBC);

		strategyPlant.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {

				JComboBox<?> comboBox = (JComboBox<?>) event.getSource();

				Object selected = comboBox.getSelectedItem();
				if (selected.toString().equals("Raw Number")) {
					plantNb.setEnabled(true);
				} else if (selected.toString().equals("Map %")){
					plantNb.setEnabled(true);
				} else {
					plantNb.setEnabled(false);
				}

			}
		});

		plantGBC.gridy++;
		plantNb.setColumns(5);
		plantNb.setEnabled(false);
		panePlant.add(plantNb, plantGBC);

		plantGBC.gridy++;
		damageOverTimeRate.setValue(5);
		damageOverTimeRate.setMajorTickSpacing(25);
		damageOverTimeRate.setMinorTickSpacing(5);
		damageOverTimeRate.setPaintTicks(true);
		damageOverTimeRate.setPaintLabels(true);
		damageOverTimeRate.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				Plant.setLifeDegenSpeed(damageOverTimeRate.getValue());
			}
		});
		panePlant.add(damageOverTimeRate, plantGBC);

		// Herbivore
		paneHerbivore.setLayout(herbivoreGBL);
		herbivoreGBC.gridx = herbivoreGBC.gridy = 0;
		herbivoreGBC.insets = new Insets(5, 120, 15, 0);
		paneHerbivore.add(new JLabel("Herbivores Parameters"), herbivoreGBC);
		
		herbivoreGBC.gridy = 1;
		herbivoreGBC.insets = new Insets(0, -120, 0, 0);
		paneHerbivore.add(new JLabel("min life"), herbivoreGBC);
		
		//herbivoreGBC.insets = new Insets(0, -120, 0, 0);
		//herbivoreGBC.gridx++;
		minLifeHerbivore.setColumns(4);
		paneHerbivore.add(minLifeHerbivore, herbivoreGBC);
		
		herbivoreGBC.gridx++;
		paneHerbivore.add(new JLabel("max life"), herbivoreGBC);
		
		//herbivoreGBC.gridx++;
		maxLifeHerbivore.setColumns(4);
		paneHerbivore.add(maxLifeHerbivore, herbivoreGBC);

		herbivoreGBC.gridy++;
		herbivoreGBC.gridx = 0;
		paneHerbivore.add(new JLabel("Herbivore Number"), herbivoreGBC);

		herbivoreGBC.gridy++;
		paneHerbivore.add(new JLabel("Life Degeneration Rate"), herbivoreGBC);

		herbivoreGBC.gridy++;
		paneHerbivore.add(new JLabel("Birth Rate"), herbivoreGBC);

		herbivoreGBC.gridy++;
		paneHerbivore.add(new JLabel("Seed Gathering Rate"), herbivoreGBC);

		herbivoreGBC.gridy++;
		paneHerbivore.add(new JLabel("Escape Rate"), herbivoreGBC);

		herbivoreGBC.gridx++;
		herbivoreGBC.gridy = 2;
		herbivoreNb.setColumns(5);
		paneHerbivore.add(herbivoreNb, herbivoreGBC);
		
		herbivoreGBC.gridy++;
		herbivoreDamageOverTimeRate.setValue(5);
		herbivoreDamageOverTimeRate.setMajorTickSpacing(25);
		herbivoreDamageOverTimeRate.setMinorTickSpacing(5);
		herbivoreDamageOverTimeRate.setPaintTicks(true);
		herbivoreDamageOverTimeRate.setPaintLabels(true);
		herbivoreDamageOverTimeRate.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				Herbivore.setLifeDegenSpeed(herbivoreDamageOverTimeRate.getValue());
			}
		});
		paneHerbivore.add(herbivoreDamageOverTimeRate, herbivoreGBC);

		herbivoreGBC.gridy++;
		herbivoreBirthRate.setValue(10);
		herbivoreBirthRate.setMajorTickSpacing(25);
		herbivoreBirthRate.setMinorTickSpacing(5);
		herbivoreBirthRate.setPaintTicks(true);
		herbivoreBirthRate.setPaintLabels(true);
		herbivoreBirthRate.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				Herbivore.setBirthRate(herbivoreBirthRate.getValue());
			}
		});
		paneHerbivore.add(herbivoreBirthRate, herbivoreGBC);

		herbivoreGBC.gridy++;
		seedGatheringRate.setMajorTickSpacing(25);
		seedGatheringRate.setMinorTickSpacing(5);
		seedGatheringRate.setPaintTicks(true);
		seedGatheringRate.setPaintLabels(true);
		seedGatheringRate.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent arg0) {
				Herbivore.setSeedGatheringRate(seedGatheringRate.getValue());
			}
		});
		paneHerbivore.add(seedGatheringRate, herbivoreGBC);

		herbivoreGBC.gridy++;
		herbivoreEscape.setMajorTickSpacing(25);
		herbivoreEscape.setMinorTickSpacing(5);
		herbivoreEscape.setPaintTicks(true);
		herbivoreEscape.setPaintLabels(true);
		herbivoreEscape.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				Herbivore.setEscapeRate(herbivoreEscape.getValue());				
			}
		});
		paneHerbivore.add(herbivoreEscape, herbivoreGBC);

		paneStrategy.add(panePlant);
		paneStrategy.add(paneHerbivore);
		return paneStrategy;
	}

	public JPanel paneAddPlant() {
		paneAddPlant.setLayout(new GridBagLayout());
		GridBagConstraints addPlantGBC = new GridBagConstraints();
		paneAddPlant.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0,
				Color.BLACK));

		addPlantGBC.gridx = addPlantGBC.gridy = 0;
		paneAddPlant.add(new JLabel("Left Click Action : "), addPlantGBC);

		addPlantGBC.gridy++;
		paneAddPlant.add(buttonAddPlants, addPlantGBC);
		return paneAddPlant;
	}

	public JPanel paneSimulationResults() {
		paneSimulationResults.setLayout(new GridBagLayout());
		GridBagConstraints simulationResultsGBC = new GridBagConstraints();
		paneSimulationResults.setBorder(BorderFactory.createMatteBorder(1, 0,
				0, 0, Color.BLACK));

		simulationResultsGBC.insets = new Insets(7, 0, 0, 0);
		simulationResultsGBC.gridx = simulationResultsGBC.gridy = 0;
		paneSimulationResults.add(new JLabel("Simulation results"),
				simulationResultsGBC);

		simulationResultsGBC.gridy++;
		textResults.setText("results");
		textResults.setEditable(false);
		paneSimulationResults.add(textResults, simulationResultsGBC);
		return paneSimulationResults;
	}
	
	protected MaskFormatter createFormatter(String s) {
        MaskFormatter formatter = null;
        try {
            formatter = new MaskFormatter(s);
        } catch (java.text.ParseException exc) {
            System.err.println("formatter is bad: " + exc.getMessage());
            System.exit(-1);
        }
        return formatter;
    }
}


