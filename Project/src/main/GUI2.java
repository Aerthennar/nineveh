package main;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.JTextField;


@SuppressWarnings("serial")
public class GUI2 extends JPanel {
        
        public GUI2(){
                super();
                JPanel paneConfig = new JPanel();
                paneConfig.setLayout(new GridLayout(3,1));
                paneConfig.add(paneStrategy()); // with plants parameters and herbivores parameters panes
                paneConfig.add(paneAddPlant());
                paneConfig.add(paneSimulationResults());
                
                add(paneConfig);
                validate();
        }
        
        public JPanel paneStrategy(){
                JPanel paneStrategy = new JPanel();
                paneStrategy.setLayout(new GridLayout(1, 2));
                GridBagLayout plantGBL = new GridBagLayout();
                GridBagLayout herbivoreGBL = new GridBagLayout();
                
                GridBagConstraints plantGBC = new GridBagConstraints();
                GridBagConstraints herbivoreGBC = new GridBagConstraints();
                
                JPanel panePlant = new JPanel();
                JPanel paneHerbivore = new JPanel();
                
                //Plant
                panePlant.setBorder(BorderFactory.createMatteBorder(0,0,0,1,Color.BLACK));
                panePlant.setLayout(plantGBL);
                plantGBC.gridx = plantGBC.gridy = 0;
                plantGBC.gridx++;
                panePlant.add(new JLabel("Plants parameters"), plantGBC);
                
                plantGBC.insets = new Insets(23,2,0,0);
                
                plantGBC.gridx = 0;
                plantGBC.gridy++;
                panePlant.add(new JLabel("plant number"), plantGBC);
                
                plantGBC.gridy++;
                panePlant.add(new JLabel("damage over time rate"), plantGBC);
                
                plantGBC.gridy++;
                panePlant.add(new JLabel("birth rate"), plantGBC);
                
                String[] strat = {"Percent of the map", "User choices", "Random"};
                JComboBox<String> strategyPlant = new JComboBox<String>(strat);
                plantGBC.gridx++;
                plantGBC.gridy = 1;
                panePlant.add(strategyPlant, plantGBC);
                
                plantGBC.gridx++;
                final JTextField plantNb = new JTextField();
                plantNb.setColumns(5);
                plantNb.setEnabled(false);
                panePlant.add(plantNb, plantGBC);
                
                strategyPlant.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {

                JComboBox<?> comboBox = (JComboBox<?>) event.getSource();

                Object selected = comboBox.getSelectedItem();
                if(selected.toString().equals("User choices"))
                        plantNb.setEnabled(true);
                else plantNb.setEnabled(false);

            }
        });
                
                JSlider damageOverTimeRate = new JSlider();
                plantGBC.gridx--;
                plantGBC.gridy++;
                damageOverTimeRate.setMajorTickSpacing(25);
                damageOverTimeRate.setMinorTickSpacing(5);
                damageOverTimeRate.setPaintTicks(true);
                damageOverTimeRate.setPaintLabels(true);
                panePlant.add(damageOverTimeRate, plantGBC);
                
                JSlider plantBirthRate = new JSlider();
                plantGBC.gridy++;
                plantBirthRate.setMajorTickSpacing(25);
                plantBirthRate.setMinorTickSpacing(5);
                plantBirthRate.setPaintTicks(true);
                plantBirthRate.setPaintLabels(true);
                panePlant.add(plantBirthRate, plantGBC);
                
                //Herbivore
                paneHerbivore.setLayout(herbivoreGBL);
                herbivoreGBC.gridx = herbivoreGBC.gridy = 0;
                herbivoreGBC.insets = new Insets(5, 120,5,0);
                paneHerbivore.add(new JLabel("Herbivores parameters"), herbivoreGBC);
                
                herbivoreGBC.insets = new Insets(0,-120,0,0);
                herbivoreGBC.gridy = 1;
                paneHerbivore.add(new JLabel("herbivore number"), herbivoreGBC);
                
                herbivoreGBC.gridy++;
                paneHerbivore.add(new JLabel("damage over time rate"), herbivoreGBC);
                
                herbivoreGBC.gridy++;
                paneHerbivore.add(new JLabel("birth rate"), herbivoreGBC);
                
                herbivoreGBC.gridy++;
                paneHerbivore.add(new JLabel("speed"), herbivoreGBC);
                
                herbivoreGBC.gridy++;
                paneHerbivore.add(new JLabel("escape rate"), herbivoreGBC);

                herbivoreGBC.gridx++;
                herbivoreGBC.gridy = 1;
                JTextField herbivoreNb = new JTextField();
                herbivoreNb.setColumns(5);
                paneHerbivore.add(herbivoreNb, herbivoreGBC);

                JSlider herbivoreDamageOverTimeRate = new JSlider();
                herbivoreGBC.gridy++;
                herbivoreDamageOverTimeRate.setMajorTickSpacing(25);
                herbivoreDamageOverTimeRate.setMinorTickSpacing(5);
                herbivoreDamageOverTimeRate.setPaintTicks(true);
                herbivoreDamageOverTimeRate.setPaintLabels(true);
                paneHerbivore.add(herbivoreDamageOverTimeRate, herbivoreGBC);
                
                JSlider herbivoreBirthRate = new JSlider();
                herbivoreGBC.gridy++;
                herbivoreBirthRate.setMajorTickSpacing(25);
                herbivoreBirthRate.setMinorTickSpacing(5);
                herbivoreBirthRate.setPaintTicks(true);
                herbivoreBirthRate.setPaintLabels(true);
                paneHerbivore.add(herbivoreBirthRate, herbivoreGBC);

                JSlider herbivoreSpeed = new JSlider(JSlider.HORIZONTAL, 0, 12, 6);
                herbivoreGBC.gridy++;
                herbivoreSpeed.setMajorTickSpacing(2);
                herbivoreSpeed.setMinorTickSpacing(1);
                herbivoreSpeed.setPaintTicks(true);
                herbivoreSpeed.setPaintLabels(true);
                paneHerbivore.add(herbivoreSpeed, herbivoreGBC);
                
                JSlider herbivoreEscape = new JSlider(JSlider.HORIZONTAL, 0, 100, 50);
                herbivoreGBC.gridy++;
                herbivoreEscape.setMajorTickSpacing(25);
                herbivoreEscape.setMinorTickSpacing(5);
                herbivoreEscape.setPaintTicks(true);
                herbivoreEscape.setPaintLabels(true);
                paneHerbivore.add(herbivoreEscape, herbivoreGBC);
                
                paneStrategy.add(panePlant);
                paneStrategy.add(paneHerbivore);
                return paneStrategy;
        }
        
        public JPanel paneAddPlant(){
                JPanel paneAddPlant = new JPanel();
                paneAddPlant.setLayout(new GridBagLayout());
                GridBagConstraints addPlantGBC = new GridBagConstraints();
                paneAddPlant.setBorder(BorderFactory.createMatteBorder(1,0,0,0,Color.BLACK));
                
                addPlantGBC.gridx = addPlantGBC.gridy = 0;
                addPlantGBC.insets = new Insets(0, 105,0,0);
                paneAddPlant.add(new JLabel("Add new plant"), addPlantGBC);
                
                addPlantGBC.insets = new Insets(7, -120,0,0);
                addPlantGBC.gridy++;
                paneAddPlant.add(new JLabel("life rate"), addPlantGBC);
                
                addPlantGBC.gridx++;
                JSlider lifeRate = new JSlider();
                lifeRate.setMajorTickSpacing(25);
                lifeRate.setMinorTickSpacing(5);
                lifeRate.setPaintTicks(true);
                lifeRate.setPaintLabels(true);
                paneAddPlant.add(lifeRate, addPlantGBC);
                
                addPlantGBC.gridy++;
                addPlantGBC.insets = new Insets(12,-175,0,0);
                JRadioButton buttonAddPlants = new JRadioButton("Add plant (with a click on the map)");
                paneAddPlant.add(buttonAddPlants, addPlantGBC);
                return paneAddPlant;
        }
        
        
        public JPanel paneSimulationResults(){
                JPanel paneSimulationResults = new JPanel();
                paneSimulationResults.setLayout(new GridBagLayout());
                GridBagConstraints simulationResultsGBC = new GridBagConstraints();
                paneSimulationResults.setBorder(BorderFactory.createMatteBorder(1,0,0,0,Color.BLACK));
                
                simulationResultsGBC.insets = new Insets(7,0,0,0);
                JTextArea textResults = new JTextArea();
                simulationResultsGBC.gridx = simulationResultsGBC.gridy = 0;
                paneSimulationResults.add(new JLabel("Simulation results"), simulationResultsGBC);
                
                simulationResultsGBC.gridy++;
                textResults.setText("results");
                paneSimulationResults.add(textResults, simulationResultsGBC);
                return paneSimulationResults;
        }
}