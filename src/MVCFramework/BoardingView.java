package MVCFramework;

import Aircraft.AircraftType;
import Panels.AnimationPanel;
import Panels.QueuePanel;
import Panels.SettingsPanel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.*;
import javax.swing.border.TitledBorder;

public class BoardingView extends JFrame {

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

        settingsPanel = new SettingsPanel(theModel);
        settingsPanel.setToolTipText("Settings Panel");
        animationPanel = new AnimationPanel(theModel);
        animationPanel.setToolTipText("Animation Panel");
        queuePanel = new QueuePanel(theModel);
        queuePanel.setToolTipText("Queue Panel");
        JMenuBar menuBar = new JMenuBar();
        JMenu menuFile  = new JMenu("File");
        JMenu menuHelp = new JMenu("Help");

        menuFile.getAccessibleContext().setAccessibleDescription(
                "This is the file menu");

        JMenuItem menuItem = new JMenuItem("Readme");

        menuHelp.add(menuItem);

        /* Menu options */

        menuBar.add(menuFile);
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

        menuItem.addActionListener(new ActionListener() {
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