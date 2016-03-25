package MVCFramework;

import Exceptions.NoSelectionException;
import Exceptions.NotIntegerException;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

/**
 * The class controls the flow of information between the view and the model.
 * It implements the interfaces EventListener and Observer
 *
 * @see EventListener
 * @see Observer
 * @see BoardingModel
 * @see BoardingView
 */

public class BoardingController implements EventListener, Observer {
    private BoardingModel theModel = new BoardingModel();
    private BoardingView theView = new BoardingView(theModel);

    /**
     * Class constructor
     * @param theView the instance of BoardingView used
     * @param theModel the instance of BoardingModel used
     *
     * @see BoardingView
     * @see BoardingModel
     */
    public BoardingController(BoardingView theView, BoardingModel theModel) {
        this.theView = theView;
        this.theModel = theModel;

        theModel.addObserver(this);

        theView.settingsPanel.startSimulation.addActionListener(new RunButtonListener());

        theView.settingsPanel.pauseSimulation.addActionListener(new PauseButtonListener());

        theView.settingsPanel.reset.addActionListener(new ResetButtonListener());

        theView.settingsPanel.simulationRate.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if(theModel.timer != null) {
                    int value = theView.settingsPanel.simulationRate.getValue();
                    //TODO: Make sure this timer value is the same as the other one
                    theModel.setDelay(value);
                    theModel.timer.setDelay(theModel.getDelay());
                }
            }
        });
    }

    /**
     * This method loads the information inputted by the user in the view
     * into the model.
     */
    private void modelLoader(){
        theModel.clear();
        theModel.setBoardingMethod(theView.settingsPanel.getSelectedBoardingMethod());
        theModel.setCapacity(theView.settingsPanel.getSelectedCapacity());
        theModel.setDelay(theView.settingsPanel.simulationRate.getValue());
        theModel.setTextOutput(theView.settingsPanel.outputChk.isSelected());
    }

    /**
     * A class which holds the Run button and associated action. Implements the interface ActionListener
     *
     * @see ActionListener
     */
    private class RunButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e){
            if(!theModel.isInProcess()) {
                if(theModel.getAircraftType() != null) {
                    if(theView.settingsPanel.validationStrings.contains(theView.settingsPanel.getSelectedBoardingMethod())){
                        if (theView.settingsPanel.getCapacityList().getEditor().getItem() != null) {
                            if (theView.settingsPanel.getCapacityList().getEditor().getItem() instanceof Integer && (theView.settingsPanel.getSelectedCapacity() <= 100 && theView.settingsPanel.getSelectedCapacity()>0)){
                                theView.settingsPanel.startSimulation.setEnabled(false);
                                theView.settingsPanel.pauseSimulation.setEnabled(true);
                                theView.settingsPanel.runningDisable();
                                //Set all model variables
                                modelLoader();
                                //Should run method in controller
                                theModel.runSimulation();
                                theModel.timer.start();
                                ButtonRunnable buttonRunnable = new ButtonRunnable();
                                Thread thread = new Thread(buttonRunnable);
                                thread.start();
                            } else {
                                new NotIntegerException("Problem");
                            }
                        }
                    } else {
                        new NoSelectionException("boarding method");
                    }
                } else {
                    new NoSelectionException("aircraft");
                }
            } else {
                theModel.timer.start();
                theView.settingsPanel.pauseSimulation.setEnabled(true);
                theView.settingsPanel.reset.setEnabled(false);
            }
        }

    }

    /**
     * A class which holds the Pause button and associated action. Implements the interface ActionListener
     *
     * @see ActionListener
     */
    private class PauseButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            //Pause the simulation
            if(theModel.timer.isRunning()){
                theModel.timer.stop();
                theView.settingsPanel.pauseSimulation.setEnabled(false);
                theView.settingsPanel.reset.setEnabled(true);
                theView.settingsPanel.status.setForeground(Color.gray);
                theView.settingsPanel.status.setText("Paused");
            }
        }
    }

    /**
     * A class which holds the Reset button and associated action. Implements the interface ActionListener
     *
     * @see ActionListener
     */
    private class ResetButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            theModel.clear();
            theModel.setAircraftType(theView.settingsPanel.aircraftTypeList.getSelectedItem().toString());
            theView.settingsPanel.status.setText("");
            theView.settingsPanel.reset.setEnabled(false);
            theView.settingsPanel.progressBar.setValue(0);
            theView.settingsPanel.progressBar.setString(" ");
            theView.settingsPanel.renable();
            theView.queuePanel.clear();
        }
    }

    /**
     * A class used for the separate thread determining if the simulation is running.
     * Implements the interface Runnable
     *
     * @see Runnable
     */
    private class ButtonRunnable implements Runnable {

        public void run() {
            while (theModel.timer != null) {
                while (theModel.timer.isRunning()) {
                    if(theModel.isCompleted()){
                        theView.settingsPanel.status.setText("Completed");
                    } else {
                        theView.settingsPanel.status.setText("Running");
                    }
                    theView.settingsPanel.startSimulation.setEnabled(false);
                    theView.settingsPanel.status.setForeground(Color.green);
                    //theView.settingsPanel.status.setText("Running");
                }
                theView.settingsPanel.startSimulation.setEnabled(true);
                theView.settingsPanel.reset.setEnabled(true);
                theView.settingsPanel.pauseSimulation.setEnabled(false);
            }
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        if (arg != null) {
            if (arg.equals(true)) {
                theView.settingsPanel.startSimulation.setEnabled(true);
            }
        }
    }

}
