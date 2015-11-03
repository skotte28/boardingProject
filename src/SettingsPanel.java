import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;


/**
 * Created by Oscar on 2015-10-19.
 */
public class SettingsPanel extends JPanel{

    GridLayout experimentLayout = new GridLayout(0,2);

    private String[] aircraftTypeStrings = {"B737", "A320"};
    private JComboBox aircraftTypeList = new JComboBox(aircraftTypeStrings);

    private String[] boardingMethodStrings = {"Back-to-front", "Outside-in", "Random", "Even-odd"};
    private JComboBox boardingMethodList = new JComboBox(boardingMethodStrings);

    /*Additional options*/
    private String[] doorsUsedStrings = {"Front only", "Front & Rear"};
    private JComboBox doorsUsedList = new JComboBox(doorsUsedStrings);


    private JButton startSimulation = new JButton("Run"); //Make this grey out when running

    /* Simulation Rate Slider */
    static final int SIM_RATE_MIN = 1;
    static final int SIM_RATE_MAX = 14;
    static final int SIM_RATE_INIT = 7;

    JSlider simulationRate = new JSlider(JSlider.HORIZONTAL, SIM_RATE_MIN, SIM_RATE_MAX, SIM_RATE_INIT);

    SettingsPanel() {

        this.setLayout(experimentLayout);

        this.add(aircraftTypeList);
        this.add(boardingMethodList);
        this.add(startSimulation);

        /*Additional options*/
        this.add(doorsUsedList);

        this.add(simulationRate);
    }

    void addRunListener(ActionListener listenForRunButton){
        startSimulation.addActionListener(listenForRunButton);
    }


}
