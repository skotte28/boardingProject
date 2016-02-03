package MVCFramework;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
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
                //TODO: Make sure this timervalue is the same as the otherone
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
        theModel.setBoardingMethod(theView.settingsPanel.getSelectedBoardingMethod());
        theModel.setCapacity(theView.settingsPanel.getSelectedCapacity());
        theModel.setDelay(theView.settingsPanel.simulationRate.getValue());
        System.out.println("This is the simulation rate"+theView.settingsPanel.simulationRate.getValue());
        /* TODO: Add the doors used options*/
    }

    public class RunButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e){
            if(!theModel.isInProcess()) {
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
                theModel.timer.start();
                theView.settingsPanel.pauseSimulation.setEnabled(true);
                theView.settingsPanel.reset.setEnabled(false);
            }
        }

    }

    public class PauseButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            //TODO: Print statement for testing purposes, remove when complete
            System.out.println("Paused");

            //Pause the simulation
            if(theModel.timer.isRunning()){
                theModel.timer.stop();
                theView.settingsPanel.pauseSimulation.setEnabled(false);
                theView.settingsPanel.reset.setEnabled(true);
                theView.settingsPanel.status.setText("Paused");
            }
        }
    }

    public class ResetButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            //TODO: Print statement for testing purposes, remove when complete
            System.out.println("Reset");
            theModel.clear();
            theModel.setAircraftType(theView.settingsPanel.aircraftTypeList.getSelectedItem().toString());
            theView.settingsPanel.renable();
            theModel.timer = null;
            theModel.setInProcess(false);
            theView.settingsPanel.reset.setEnabled(false);
        }
    }

    public class ButtonRunnable implements Runnable {

        public void run() {
            while (theModel.timer != null) {
                while (theModel.timer.isRunning()) {
                    theView.settingsPanel.startSimulation.setEnabled(false);
                    theView.settingsPanel.status.setText("Running");
                }
                theView.settingsPanel.startSimulation.setEnabled(true);
            }
        }
    }

    public class BoardingListener implements ChangeListener{
        @Override
        public void stateChanged(ChangeEvent e){
            theView.queuePanel.repaint();
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
