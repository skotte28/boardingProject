package MVCFramework;

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

/**
 * This class holds the JPanels which make up the user interface.
 * The class is a subclass of JFrame.
 *
 * @see JFrame
 *
 */
public class BoardingView extends JFrame {

    protected SettingsPanel settingsPanel;
    protected QueuePanel queuePanel;

    /**
     * Class constructor
     * @param theModel instance of BoardingModel which is to be used for the BoardingView
     * @see BoardingModel
     */
    public BoardingView(BoardingModel theModel){

        this.setTitle("Aircraft Boarding Visualizer");

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        int STANDARD_WINDOW_WIDTH = 1500;
        int STANDARD_WINDOW_HEIGHT = 800;
        setSize(STANDARD_WINDOW_WIDTH, STANDARD_WINDOW_HEIGHT);

        settingsPanel = new SettingsPanel(theModel);
        settingsPanel.setToolTipText("Settings Panel");
        AnimationPanel animationPanel = new AnimationPanel(theModel);
        animationPanel.setToolTipText("Animation Panel");
        queuePanel = new QueuePanel(theModel);
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

        /* The layout of the JPanels in JFrame */
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

        /* Action to set the aircraft type */
        settingsPanel.aircraftTypeList.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //System.out.println(settingsPanel.aircraftTypeList.getSelectedItem().toString()); - Enable for testing
                theModel.setAircraftType(settingsPanel.aircraftTypeList.getSelectedItem().toString());
            }
        });

        /* Action to open readme file */
        readmeItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    java.awt.Desktop.getDesktop().open(new File("./resources/readme.pdf"));
                } catch (IOException ioe){
                    JOptionPane.showMessageDialog(null, "There was a problem finding the readme file.","Readme Not Found",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

}