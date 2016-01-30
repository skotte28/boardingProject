package Panels;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.io.File;


/**
 * Created by Oscar on 2015-10-19.
 */
public class SettingsPanel extends JPanel {

    GridLayout experimentLayout = new GridLayout(0, 2);

    private java.util.List<Point> layoutCells;

    public JComboBox aircraftTypeList = new JComboBox();

    private String[] boardingMethodStrings = {"Back-to-front", "Outside-in", "Random", "Even-odd"};
    private JComboBox boardingMethodList = new JComboBox(boardingMethodStrings);

    private Integer[] capacityStrings = {100, 75, 50, 25};
    private JComboBox<Integer> capacityList = new JComboBox<Integer>(capacityStrings);

    /*Additional options*/
    private String[] doorsUsedStrings = {"Front only", "Front & Rear"};
    private JComboBox doorsUsedList = new JComboBox(doorsUsedStrings);

    public JButton startSimulation = new JButton("Run"); //Make this grey out when running
    public JButton pauseSimulation = new JButton("Pause"); //Make this grey out when running

    /* Simulation Rate Slider */
    static final int SIM_RATE_MIN = 0;
    static final int SIM_RATE_MAX = 4;
    static final int SIM_RATE_INIT = 2;
    public JSlider simulationRate = new JSlider(JSlider.HORIZONTAL, SIM_RATE_MIN, SIM_RATE_MAX, SIM_RATE_INIT);

    /* Capacity Slider */
    static final int CAP_MIN = 0;
    static final int CAP_MAX = 100;
    static final int CAP_INIT = 100;

    public JSlider capacitySlider = new JSlider(JSlider.HORIZONTAL, CAP_MIN, CAP_MAX, CAP_INIT);

    private String selectedAircraft;

    public void setSelectedAircraft(){
        selectedAircraft = aircraftTypeList.getSelectedItem().toString();
    }

    public String getSelectedAircraft() {
        System.out.println(selectedAircraft);
        return selectedAircraft;
    }

    public String getSelectedBoardingMethod() {
        System.out.println(boardingMethodList.getSelectedItem().toString());
        return boardingMethodList.getSelectedItem().toString();
    }

    public int getSelectedCapacity() {
        System.out.println(Integer.parseInt(capacityList.getSelectedItem().toString()));
        return Integer.parseInt(capacityList.getSelectedItem().toString());
    }

    public int getSelectedSimulationRate(){
        return simulationRate.getValue();
    }
    public String getDoorsUsed() {
        System.out.println(doorsUsedList.getSelectedItem().toString());
        return doorsUsedList.getSelectedItem().toString();
    }

    public SettingsPanel() {

        //Loads the aircraft JComboBox based on the directories which are located in "content/"
        try {
            File file = new File("content/");
            File[] files;
            files = file.listFiles();
            for(File f : files){
                String[] name = f.toString().split("\\\\");
                aircraftTypeList.addItem(name[1]);
            }
        }catch (SecurityException se){
            se.printStackTrace();
        }
        System.out.println("AircraftTypeList:"+aircraftTypeList.toString());

        //Tool tips settings
        startSimulation.setToolTipText("Start the simulation");
        pauseSimulation.setToolTipText("Pause the simulation");
        aircraftTypeList.setToolTipText("Select the type of aircraft to be used in the simulation");
        boardingMethodList.setToolTipText("Select the boarding method to be used in the simulation");
        capacityList.setToolTipText("Occupancy rate used in the simulation");
        doorsUsedList.setToolTipText("Select if one or two doors are to be used in the simulation");
        capacitySlider.setToolTipText(Integer.toString(capacitySlider.getValue()));
        //setBackground(Color.YELLOW);

        //simulationRate.setMajorTickSpacing(2);
        simulationRate.setMinorTickSpacing(1);
        simulationRate.setPaintTicks(true);
        simulationRate.setPaintLabels(true);
        simulationRate.setBorder(
                BorderFactory.createEmptyBorder(5, 5, 5, 5));
        simulationRate.setValue(SIM_RATE_INIT);

        capacitySlider.setMajorTickSpacing(10);
        capacitySlider.setMinorTickSpacing(2);
        capacitySlider.setPaintTicks(true);
        capacitySlider.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        capacitySlider.setValue(CAP_INIT);
        //this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        GridBagConstraints gbc = new GridBagConstraints();
        this.setLayout(new GridBagLayout());
        gbc.insets = new Insets(2, 1, 2, 1);
        gbc.gridwidth = 2;

        gbc.weightx = 1.0;
        gbc.weighty = 0;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        this.add(new JLabel("Aircraft Type:"), gbc);

        gbc.gridy++;
        this.add(aircraftTypeList, gbc);

        gbc.gridy++;
        this.add(new JLabel("Boarding Method:"), gbc);

        gbc.gridy++;
        this.add(boardingMethodList, gbc);

        gbc.gridy++;
        this.add(new JLabel("Occupancy (%):"), gbc);

        gbc.gridy++;
        //this.add(capacitySlider, gbc);
        this.add(capacityList, gbc);

        /*Additional options*/
        gbc.gridy++;
        this.add(new JLabel("Doors used:"), gbc);

        gbc.gridy++;
        this.add(doorsUsedList, gbc);

        /*Simulation Control*/
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.gridy++;

        this.add(startSimulation, gbc);
        gbc.gridx = 1;
        this.add(pauseSimulation, gbc);

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 2;
        gbc.gridy++;
        gbc.gridx = 0;
        this.add(new JLabel("Simulation rate:"), gbc);
        gbc.gridy++;
        this.add(simulationRate, gbc);

        //TODO: Default selection option
/*
        aircraftTypeList.setSelectedIndex(1);
        boardingMethodList.setSelectedIndex(0);
        capacityList.setSelectedIndex(0);
*/
/*        startSimulation.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Running");

                *//* A lot of printing *//*
                String selectedAircraft = aircraftTypeList.getSelectedItem().toString();
                    //MVCFramework.BoardingModel.setAircraftType();
                String selectedBoardingMethod = boardingMethodList.getSelectedItem().toString();
                String selectedDoorsUsed = doorsUsedList.getSelectedItem().toString();
                System.out.println(selectedAircraft + " " + selectedBoardingMethod + " " + selectedDoorsUsed);
                AircraftType.getLayout(selectedAircraft);
                //Should run something based on this...
            }
        });*/
    }
/*
    private class ButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e){
            System.out.println("I pressed this button");
            layoutCells = AircraftType.getLayout(aircraftTypeList.getSelectedItem().toString());
        }
    }*/
}
