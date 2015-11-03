import java.awt.*;

import javax.swing.*;

public class BoardingView extends JFrame/* implements ChangeListener*/{

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

        /*JPanel settingsPanel = new SettingsPanel();
        JPanel windowPaint = new WindowPanel();
        JPanel queuePanel = new QueuePanel();

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1500, 800);
        settingsPanel.setLayout(new BoxLayout(settingsPanel, BoxLayout.Y_AXIS));

        this.add(settingsPanel, BorderLayout.WEST);
        this.add(windowPaint, BorderLayout.CENTER);
        this.add(queuePanel, BorderLayout.SOUTH);*/


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