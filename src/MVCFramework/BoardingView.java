package MVCFramework;

import Aircraft.AircraftType;
import Panels.AnimationPanel;
import Panels.QueuePanel;
import Panels.SettingsPanel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.*;
import javax.swing.border.TitledBorder;

public class BoardingView extends JFrame {

    protected SettingsPanel settingsPanel;

    public BoardingView(BoardingModel theModel){

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        int STANDARD_WINDOW_WIDTH = 1500;
        int STANDARD_WINDOW_HEIGHT = 800;
        setSize(STANDARD_WINDOW_WIDTH, STANDARD_WINDOW_HEIGHT);

        settingsPanel = new SettingsPanel(theModel);
        settingsPanel.setToolTipText("Settings Panel");
        AnimationPanel animationPanel = new AnimationPanel(theModel);
        animationPanel.setToolTipText("Animation Panel");
        QueuePanel queuePanel = new QueuePanel(theModel);
        queuePanel.setToolTipText("Queue Panel");
        JMenuBar menuBar = new JMenuBar();
        JMenu menuHelp = new JMenu("Help");

        JMenuItem readmeItem = new JMenuItem("Readme");
        menuHelp.add(readmeItem);
        /* Menu options */

        menuBar.add(menuHelp);

        this.setJMenuBar(menuBar);

        TitledBorder queueTitle = BorderFactory.createTitledBorder("Remaining Passengers:");
        queuePanel.setBorder(queueTitle);

        TitledBorder animationTitle = BorderFactory.createTitledBorder("Boarding Process:");
        animationPanel.setBorder(animationTitle);

        /* Having the addObservers here means the panels can be protected */
        theModel.addObserver(animationPanel);
        theModel.addObserver(queuePanel);
        theModel.addObserver(settingsPanel);

        JPanel panelFramer = new JPanel();
        panelFramer.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.weighty = 1;
        gbc.insets = new Insets(5,5,5,5);

        gbc.gridx = 0;
        gbc.gridy = 0;

        gbc.gridwidth = 1;
        gbc.gridheight = 2;

        gbc.fill = GridBagConstraints.VERTICAL;
        panelFramer.add(settingsPanel, gbc);

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

        readmeItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    java.awt.Desktop.getDesktop().open(new File("./resources/readme.pdf"));
                } catch (IOException ioe){
                    //TODO: Try to get the error messages working
                    JOptionPane.showMessageDialog(null, "There was a problem finding the readme file.","Readme Not Found",
                            JOptionPane.ERROR_MESSAGE);
                    System.err.println("There was a problem finding the readme file.");
                }
            }
        });
    }

}