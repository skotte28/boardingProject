package MVCFramework;

import Exceptions.NoSelectedException;
import Exceptions.NotIntegerException;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

public class BoardingController implements EventListener, Observer {
    private BoardingModel theModel = new BoardingModel();
    private BoardingView theView = new BoardingView(theModel);

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


    private void modelLoader(){
        theModel.clear();
        theModel.setBoardingMethod(theView.settingsPanel.getSelectedBoardingMethod());
        theModel.setCapacity(theView.settingsPanel.getSelectedCapacity());
        theModel.setDelay(theView.settingsPanel.simulationRate.getValue());
        theModel.setTextOutput(theView.settingsPanel.outputChk.isSelected());
    }

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
                        new NoSelectedException("boarding method");
                    }
                } else {
                    new NoSelectedException("aircraft");
                }
            } else {
                theModel.timer.start();
                theView.settingsPanel.pauseSimulation.setEnabled(true);
                theView.settingsPanel.reset.setEnabled(false);
            }
        }

    }

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
