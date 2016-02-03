package MVCFramework;

import Aircraft.AircraftType;
import MVCFramework.BoardingModel;
import Panels.AnimationPanel;
import Panels.QueuePanel;
import Panels.SettingsPanel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class BoardingView extends JFrame /* implements ChangeListener*/{
/*
    public void addBoardingController(MVCFramework.BoardingController bc){
        boardingController = bc;
    }*/
    private BoardingModel theModel;
    JPanel panelFramer = new JPanel();

    final int STANDARD_WINDOW_WIDTH = 1500;
    final int STANDARD_WINDOW_HEIGHT = 800;

    protected SettingsPanel settingsPanel;
    protected AnimationPanel animationPanel;
    protected AircraftType aircraftType;
    protected QueuePanel queuePanel;

    GridBagConstraints gbc = new GridBagConstraints();

    public BoardingView(BoardingModel theModel){

        this.theModel = theModel;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(STANDARD_WINDOW_WIDTH, STANDARD_WINDOW_HEIGHT);

        settingsPanel = new SettingsPanel();
        settingsPanel.setToolTipText("Settings Panel");
        animationPanel = new AnimationPanel(theModel);
        animationPanel.setToolTipText("Animation Panel");
        queuePanel = new QueuePanel(theModel);
        queuePanel.setToolTipText("Queue Panel");

        TitledBorder queueTitle = BorderFactory.createTitledBorder("Remaining Passengers:");
        queuePanel.setBorder(queueTitle);

        TitledBorder animationTitle = BorderFactory.createTitledBorder("Boarding Process:");
        animationPanel.setBorder(animationTitle);

        /* Having the addObservers here means the panels can be protected */
        theModel.addObserver(animationPanel);
        theModel.addObserver(queuePanel);


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

        settingsPanel.aircraftTypeList.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(settingsPanel.aircraftTypeList.getSelectedItem().toString());
                System.out.println("Somethings changed again");
                //animationPanel.nullPassengers();
                theModel.setAircraftType(settingsPanel.aircraftTypeList.getSelectedItem().toString());
                System.out.println("1: "+settingsPanel.aircraftTypeList.getSelectedItem().toString());

            }
        });

    }
}