import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Set;

import javax.swing.*;

public class BoardingView extends JFrame/* implements ChangeListener*/{

    final int STANDARD_WINDOW_WIDTH = 1500;
    final int STANDARD_WINDOW_HEIGHT = 800;

    private SettingsPanel settingsPanel;
    private TestPane testPane;
    private BoardingModel boardingModel;

    public BoardingView(){

        addWindowListener(new WindowCloser());

        settingsPanel = new SettingsPanel(testPane);
        testPane = new TestPane(boardingModel);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);        //Could replace the WindowCloser?
        this.setSize(STANDARD_WINDOW_WIDTH, STANDARD_WINDOW_HEIGHT);

        this.add(settingsPanel, BorderLayout.WEST);
        //this.add(windowPanel, BorderLayout.CENTER);
        this.add(testPane, BorderLayout.CENTER);

    }

    /* Exits the application upon window closure - currently from TradeViewer */
    private class WindowCloser extends WindowAdapter{
        public void windowClosing(WindowEvent event){
            System.exit(0);
        }
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