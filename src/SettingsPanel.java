import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.BorderLayout;


/**
 * Created by Oscar on 2015-10-19.
 */
public class SettingsPanel extends JPanel {

    GridLayout experimentLayout = new GridLayout(0, 2);

    private String[] aircraftTypeStrings = {"B737", "A320"};
    private JComboBox aircraftTypeList = new JComboBox(aircraftTypeStrings);

    private String[] boardingMethodStrings = {"Back-to-front", "Outside-in", "Random", "Even-odd"};
    private JComboBox boardingMethodList = new JComboBox(boardingMethodStrings);

    /*Additional options*/
    private String[] doorsUsedStrings = {"Front only", "Front & Rear"};
    private JComboBox doorsUsedList = new JComboBox(doorsUsedStrings);


    private JButton startSimulation = new JButton("Run"); //Make this grey out when running
    private JButton pauseSimulation = new JButton("Pause"); //Make this grey out when running

    /* Simulation Rate Slider */
    static final int SIM_RATE_MIN = 1;
    static final int SIM_RATE_MAX = 15;
    static final int SIM_RATE_INIT = 1;
    JSlider simulationRate = new JSlider(JSlider.HORIZONTAL, SIM_RATE_MIN, SIM_RATE_MAX, SIM_RATE_INIT);

    SettingsPanel() {

        simulationRate.setMajorTickSpacing(2);
        simulationRate.setMinorTickSpacing(1);
        simulationRate.setPaintTicks(true);
        simulationRate.setPaintLabels(true);
        simulationRate.setBorder(
                BorderFactory.createEmptyBorder(5, 5, 5, 5));

        simulationRate.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int value = simulationRate.getValue();
                System.out.println(value);
            }
        });

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        this.add(aircraftTypeList);
        this.add(boardingMethodList);

        /*Additional options*/
        this.add(doorsUsedList);

        /*Simulation Control*/
        this.add(startSimulation);
        this.add(pauseSimulation);
        this.add(simulationRate);

        startSimulation.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Running");
                String selectedAircraft = aircraftTypeList.getSelectedItem().toString();
                String selectedBoardingMethod = boardingMethodList.getSelectedItem().toString();
                String selectedDoorsUsed = doorsUsedList.getSelectedItem().toString();
                System.out.println(selectedAircraft+" "+selectedBoardingMethod+" "+selectedDoorsUsed);

                //Should run something based on this...
            }
        });

        pauseSimulation.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Paused");
                if(pauseSimulation.getText().equals("Pause")){
                    pauseSimulation.setText("Resume");
                } else {
                    pauseSimulation.setText("Pause");
                }
            }
        });

//    void addRunListener(ActionListener listenForRunButton){
//        startSimulation.addActionListener(listenForRunButton);
//    }


    }

}
