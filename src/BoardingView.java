import Aircraft.AircraftType;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Set;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import javax.swing.*;

public class BoardingView extends JFrame/* implements ChangeListener*/{
/*
    public void addBoardingController(BoardingController bc){
        boardingController = bc;
    }*/

    JPanel panelFramer = new JPanel();

    final int STANDARD_WINDOW_WIDTH = 1500;
    final int STANDARD_WINDOW_HEIGHT = 800;

    protected SettingsPanel settingsPanel;
    protected TestPane testPane;
    protected AircraftType aircraftType;

    public BoardingView(){

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        settingsPanel = new SettingsPanel();
        testPane = new TestPane();

        panelFramer.setLayout(new BoxLayout(panelFramer, BoxLayout.X_AXIS));

        setSize(STANDARD_WINDOW_WIDTH, STANDARD_WINDOW_HEIGHT);

        panelFramer.add(settingsPanel, BorderLayout.WEST);
        //this.add(windowPanel, BorderLayout.CENTER);
        panelFramer.add(testPane, BorderLayout.CENTER);
        this.setContentPane(panelFramer);

        //settingsPanel.aircraftTypeList.addActionListener(boardingController.getActionListener(settingsPanel.aircraftTypeList));

        settingsPanel.aircraftTypeList.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Somethings changed again");
                settingsPanel.setSelectedAircraft();
                testPane.setLayoutCells(aircraftType.getLayout(settingsPanel.getSelectedAircraft()));
                testPane.repaint();
            }
        });
        //testPane.setLayoutCells();
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