import Aircraft.AircraftType;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
    protected AnimationPanel animationPanel;
    protected AircraftType aircraftType;
    protected QueuePanel queuePanel;

    GridBagConstraints gbc = new GridBagConstraints();

    public BoardingView(){

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(STANDARD_WINDOW_WIDTH, STANDARD_WINDOW_HEIGHT);

        settingsPanel = new SettingsPanel();
        animationPanel = new AnimationPanel();
        queuePanel = new QueuePanel();

        /* BoxLayout
        panelFramer.setLayout(new BoxLayout(panelFramer, BoxLayout.X_AXIS));

        panelFramer.add(settingsPanel, BorderLayout.WEST);
        panelFramer.add(animationPanel, BorderLayout.CENTER);
        panelFramer.add(queuePanel, BorderLayout.EAST);
        */


        panelFramer.setLayout(new GridBagLayout());
        gbc.weighty = 1;
        gbc.insets = new Insets(5,5,5,5);

        gbc.gridx = 0;
        gbc.gridy = 0;

        gbc.gridwidth = 1;
        gbc.gridheight = 2;

        gbc.fill = GridBagConstraints.VERTICAL;
        panelFramer.add(settingsPanel,gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 8;

        gbc.gridheight = 1;

        gbc.fill = GridBagConstraints.BOTH;
        panelFramer.add(animationPanel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        panelFramer.add(queuePanel, gbc);

        this.setContentPane(panelFramer);

        // this.pack();
        //settingsPanel.aircraftTypeList.addActionListener(boardingController.getActionListener(settingsPanel.aircraftTypeList));

        settingsPanel.aircraftTypeList.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Somethings changed again");
                settingsPanel.setSelectedAircraft();
                animationPanel.setLayoutCells(aircraftType.getLayout(settingsPanel.getSelectedAircraft()));
                animationPanel.repaint();
            }
        });
        //animationPanel.setLayoutCells();
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