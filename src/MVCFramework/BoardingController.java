package MVCFramework;

import Exceptions.NoSelectedException;

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
                int value = theView.settingsPanel.simulationRate.getValue();
                System.out.println(value);
                //TODO: Make sure this timer value is the same as the other one
                theModel.timer.stop();
                theModel.setDelay(value);
                theModel.timer.setDelay(theModel.getDelay());
                theModel.timer.start();
            }
        });
    }


    private void modelLoader(){
        theModel.clear();
        /* Had to be disabled to allowed aircraft preview... Seems strange
        theModel.setAircraftType(theView.settingsPanel.getSelectedAircraft()); */
        theModel.setIsSeatCount(0);
        theModel.setBoardingMethod(theView.settingsPanel.getSelectedBoardingMethod());
        theModel.setCapacity(theView.settingsPanel.getSelectedCapacity());
        theModel.setDelay(theView.settingsPanel.simulationRate.getValue());
        System.out.println("This is the simulation rate"+theView.settingsPanel.simulationRate.getValue());
        /* TODO: Add the doors used options*/
    }

    private class RunButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e){
            if(!theModel.isInProcess()) {
                if(theModel.getAircraftType() != null) {
                    if(theView.settingsPanel.getSelectedBoardingMethod() != null){
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
            //TODO: Print statement for testing purposes, remove when complete
            System.out.println("Paused");

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
            //TODO: Print statement for testing purposes, remove when complete
            System.out.println("Reset");
            theModel.clear();
            theModel.setAircraftType(theView.settingsPanel.aircraftTypeList.getSelectedItem().toString());
            theView.settingsPanel.renable();
            theModel.timer = null;
            theModel.setInProcess(false);
            theModel.setCompleted(false);
            theView.settingsPanel.status.setText("");
            theView.settingsPanel.reset.setEnabled(false);
            theView.settingsPanel.progressBar.setValue(0);
            theView.settingsPanel.progressBar.setString(" ");
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
            System.out.println("I was notified!");
            if (arg.equals(true)) {
                System.out.println("I'm changing the button!");
                theView.settingsPanel.startSimulation.setEnabled(true);
            }
        }
    }

}
