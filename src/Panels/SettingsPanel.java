package Panels;

import Exceptions.NotIntegerException;
import MVCFramework.BoardingModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Observable;
import java.util.Observer;


/**
 * Created by Oscar on 2015-10-19.
 */
public class SettingsPanel extends JPanel implements Observer {

    BoardingModel boardingModel;
    int iterationInt;
    private JLabel iterationLabel = new JLabel();

    GridLayout experimentLayout = new GridLayout(0, 2);

    private java.util.List<Point> layoutCells;

    private DefaultComboBoxModel defaultComboBoxModel = new DefaultComboBoxModel<String>() {
        private static final long serialVersionUID = 1L;
        boolean selectionAllowed = true;

        @Override
        public void setSelectedItem(Object anObject) {
            if (!NOT_SELECTABLE_OPTION.equals(anObject)) {
                super.setSelectedItem(anObject);
            } else if (selectionAllowed) {
                // Allow this just once
                selectionAllowed = false;
                super.setSelectedItem(anObject);
            }
        }
    };
    private static final String NOT_SELECTABLE_OPTION = " - Select - ";

    public JComboBox aircraftTypeList = new JComboBox();

    private String[] boardingMethodStrings = {"Back-to-front", "Outside-in", "Random", "Even-odd"};
    private JComboBox boardingMethodList = new JComboBox();

    private JTextArea boardingMethodInfo = new JTextArea();

    private Integer[] capacityStrings = {100, 75, 50, 25};
    private JComboBox<Integer> capacityList = new JComboBox<Integer>(capacityStrings);

    /* Additional options
    private String[] doorsUsedStrings = {"Front only", "Front & Rear"};
    private JComboBox doorsUsedList = new JComboBox(doorsUsedStrings); */

    /* Buttons */
    public JButton startSimulation = new JButton("Run");
    public JButton pauseSimulation = new JButton("Pause");
    public JButton reset = new JButton("Reset");

    /* Status */
    public JLabel status = new JLabel();
    public JLabel paxRemaining = new JLabel();

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

    public JProgressBar progressBar = new JProgressBar();

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
        if(capacityList.getEditor().getItem() != null){
            try{
                if (capacityList.getEditor().getItem() instanceof Integer) {
                    return Integer.parseInt(capacityList.getEditor().getItem().toString());
                } else {
                    throw new NotIntegerException("Problem");
                }
            } catch (NotIntegerException e){
                //TODO: Make sure that this doesn't continue to run
                //TODO: Make a "Just set capacity to 100%" option
            }
        }
        System.out.println(Integer.parseInt(capacityList.getSelectedItem().toString()));
        return Integer.parseInt(capacityList.getSelectedItem().toString());
    }

    public int getSelectedSimulationRate(){
        return simulationRate.getValue();
    }

    /*
    public String getDoorsUsed() {
        System.out.println(doorsUsedList.getSelectedItem().toString());
        return doorsUsedList.getSelectedItem().toString();
    }*/

    public SettingsPanel(BoardingModel boardingModel) {

        this.boardingModel = boardingModel;

        aircraftTypeList.setModel(defaultComboBoxModel);

        //Loads the aircraft JComboBox based on the directories which are located in "content/"
        try {
            /*From here http://stackoverflow.com/questions/16665688/display-a-non-selectable-default-value-for-jcombobox */
            aircraftTypeList.addItem(NOT_SELECTABLE_OPTION);
            /* */
            File file = new File("content/");
            File[] files;
            files = file.listFiles();
            for (File f : files) {
                String[] name = f.toString().split("\\\\");
                aircraftTypeList.addItem(name[1]);
            }


        } catch (SecurityException se) {
            se.printStackTrace();
        }
        System.out.println("AircraftTypeList:" + aircraftTypeList.toString());

        boardingMethodList.setModel(new DefaultComboBoxModel<String>() {
        private static final long serialVersionUID = 2L;
        boolean selectionAllowed = true;

        @Override
        public void setSelectedItem(Object anObject) {
            if (!NOT_SELECTABLE_OPTION.equals(anObject)) {
                super.setSelectedItem(anObject);
            } else if (selectionAllowed) {
                // Allow this just once
                selectionAllowed = false;
                super.setSelectedItem(anObject);
            }
        }
        });
        boardingMethodList.addItem(NOT_SELECTABLE_OPTION);
        for(String str : boardingMethodStrings){
            boardingMethodList.addItem(str);
        }

        //Tool tips settings
        startSimulation.setToolTipText("Start the simulation");
        pauseSimulation.setToolTipText("Pause the simulation");
        aircraftTypeList.setToolTipText("Select the type of aircraft to be used in the simulation");
        boardingMethodList.setToolTipText("Select the boarding method to be used in the simulation");
        capacityList.setToolTipText("Occupancy rate used in the simulation");
        capacityList.setEditable(true);
        /*doorsUsedList.setToolTipText("Select if one or two doors are to be used in the simulation");*/
        /*capacitySlider.setToolTipText(Integer.toString(capacitySlider.getValue()));*/

        simulationRate.setMinorTickSpacing(1);
        simulationRate.setPaintTicks(true);
        simulationRate.setPaintLabels(true);
        simulationRate.setBorder(
                BorderFactory.createEmptyBorder(5, 5, 5, 5));
        simulationRate.setValue(SIM_RATE_INIT);

        /*capacitySlider.setMajorTickSpacing(10);
        capacitySlider.setMinorTickSpacing(2);
        capacitySlider.setPaintTicks(true);
        capacitySlider.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        capacitySlider.setValue(CAP_INIT);*/

        GridBagConstraints gbc = new GridBagConstraints();
        this.setLayout(new GridBagLayout());
        gbc.insets = new Insets(2, 1, 2, 1);
        gbc.gridwidth = 3;

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
        this.add(new JLabel("Info:"), gbc);

        gbc.gridy++;
        this.add(boardingMethodInfo, gbc);
        boardingMethodInfo.setPreferredSize(new Dimension(200,75));
        boardingMethodInfo.setLineWrap(true);
        boardingMethodInfo.setWrapStyleWord(true);
        boardingMethodInfo.setEditable(false);
        boardingMethodInfo.setSelectionEnd(2);
        boardingMethodInfo.setBackground(Color.white);

        gbc.gridy++;
        this.add(new JLabel("Occupancy (%):"), gbc);

        gbc.gridy++;
        //this.add(capacitySlider, gbc);
        this.add(capacityList, gbc);

        /*Additional options
        gbc.gridy++;
        this.add(new JLabel("Doors used:"), gbc);

        gbc.gridy++;
        this.add(doorsUsedList, gbc);*/

        gbc.gridy++;
        this.add(new JSeparator(),gbc);

        /*Simulation Control*/
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.gridy++;

        this.add(startSimulation, gbc);
        gbc.gridx = 1;
        this.add(pauseSimulation, gbc);
        gbc.gridx = 2;
        this.add(reset,gbc);

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 3;
        gbc.gridy++;
        gbc.gridx = 0;
        this.add(new JLabel("Simulation rate:"), gbc);
        gbc.gridy++;
        this.add(simulationRate, gbc);

        gbc.gridy++;
        this.add(new JSeparator(), gbc);

        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        this.add(new JLabel("Status:"), gbc);
        gbc.gridx++;
        gbc.gridx++;
        this.add(status,gbc);

        /*gbc.gridy++;
        gbc.gridx = 0;
        this.add(new JLabel("Remaining passengers:"), gbc);
        gbc.gridx++;
        this.add(paxRemaining, gbc);*/
        gbc.gridy++;
        gbc.gridx = 0;
        this.add(new JLabel("Iterations:"), gbc);
        gbc.gridx++;
        gbc.gridx++;
        this.add(iterationLabel, gbc);

        gbc.gridy++;
        gbc.gridwidth = 3;
        gbc.gridx = 0;
        JLabel paxProgressLabel = new JLabel("Passengers seated");
        this.add(paxProgressLabel, gbc);
        paxProgressLabel.setHorizontalAlignment(SwingConstants.CENTER);

        gbc.gridy++;
        progressBar.setStringPainted(true);
        progressBar.setString(" ");
        this.add(progressBar, gbc);

        pauseSimulation.setEnabled(false);
        reset.setEnabled(false);

        //TODO: Default selection option

        boardingMethodList.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Is this good practice to have something of type "Object"?
                Object selectedMethod = boardingMethodList.getSelectedItem();
                if(selectedMethod.equals("Back-to-front")){
                    boardingMethodInfo.setText("The passengers seated at the back of the aircraft are welcomed onboard first.");
                } else if(selectedMethod.equals("Outside-in")){
                    boardingMethodInfo.setText("The passengers seated at window seats are welcomed onboard first, followed by middle seat passengers, and finally by aisle seat passengers.");
                } else if(selectedMethod.equals("Random")){
                    boardingMethodInfo.setText("Passengers are welcome to board whenever they wish.");
                } else if(selectedMethod.equals("Even-odd")){
                    boardingMethodInfo.setText("Passengers seated in even numbered rows are welcomed first, followed by passengers seated in odd numbered rows.");
                }
            }
        });
    }

    public void runningDisable(){
        aircraftTypeList.setEnabled(false);
        capacityList.setEnabled(false);
        boardingMethodList.setEnabled(false);
        /*doorsUsedList.setEnabled(false);*/
    }

    public void renable(){
        aircraftTypeList.setEnabled(true);
        capacityList.setEnabled(true);
        boardingMethodList.setEnabled(true);
        /*doorsUsedList.setEnabled(true);*/
    }

    //TODO: Should this go here or somewhere else which isn't layout?

    @Override
    public void update(Observable o, Object data){
        invalidate();
        iterationInt = boardingModel.getModelInteration();
        if(iterationInt > 1){
            iterationLabel.setText(""+iterationInt);
        } else if(iterationInt == 0){
            iterationLabel.setText("");
        }
        if(boardingModel.getTotalPax() != 0){
            System.out.println("Total Pax: "+boardingModel.getTotalPax());
            double progressDouble = ((double) boardingModel.getIsSeatCount() / (double) boardingModel.getTotalPax()) * 100;
            int progress = (int) Math.round(progressDouble);
            System.out.println(progress);
            progressBar.setString(boardingModel.getIsSeatCount()+"/"+boardingModel.getTotalPax());
            progressBar.setValue(progress);
        }
    }
}
