import java.awt.*;

import javax.swing.*;

public class BoardingView extends JFrame/* implements ChangeListener*/{

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

    /*
    simulationRate.addChangeListener(this);

    simulationRate.setMajorTickSpacing(10);
    simulationRate.setMinorTickSpacing(1);
    simulationRate.setPaintTicks(true);
    simulationRate.setPaintLabels(true);
    simulationRate.setBorder(
            BorderFactory.createEmptyBorder(0,0,10,0));
            )
    */
    BoardingView(){

        JPanel settingsPanel = new JPanel();
        JPanel windowPaint = new WindowPaint();
        settingsPanel.add(aircraftTypeList);
        settingsPanel.add(boardingMethodList);
        settingsPanel.add(startSimulation);

        /*Additional options*/
        settingsPanel.add(doorsUsedList);

        settingsPanel.add(simulationRate);


        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1500, 800);
        settingsPanel.setLayout(new BoxLayout(settingsPanel, BoxLayout.Y_AXIS));

        this.add(settingsPanel, BorderLayout.WEST);
        this.add(windowPaint);


    }

    /*Slide listen, from here: http://da2i.univ-lille1.fr/doc/tutorial-java/uiswing/components/examples/SliderDemo.java*/
/*    boolean frozen;
    int delay;
    public void stateChanged(ChangeEvent e){
        JSlider source = (JSlider)e.getSource();
        if (!source.getValueIsAdjusting()){
            int simRate = (int)source.getValue();
            if (simRate == 0) {
                if (!frozen) stopAnimaiton();
            } else {
                delay = 1000 / simRate;
                timer.setDelay(delay);
                timer.setInitialDelay(delay * 10);
                if (frozen) startAnimation();
            }
        }

    }*/
}