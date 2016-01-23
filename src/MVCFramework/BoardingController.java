package MVCFramework;

import Aircraft.AircraftType;
import Methods.*;
import Passenger.*;
import Simulation.AircraftGrid;
import XMLParsing.JAXBHandlerPassenger;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.*;

public class BoardingController implements EventListener{
    private BoardingModel theModel = new BoardingModel();
    private BoardingView theView = new BoardingView(theModel);


    public BoardingController(BoardingView theView, BoardingModel theModel) {
        this.theView = theView;
        this.theModel = theModel;

        theView.settingsPanel.startSimulation.addActionListener(new ButtonListener());

        theView.settingsPanel.pauseSimulation.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //TODO: Print statement for testing purposes, remove when complete
                System.out.println("Paused");

                    //Pause the simulation
                    if(theView.settingsPanel.pauseSimulation.getText().equals("Pause")){
                        theView.settingsPanel.pauseSimulation.setText("Resume");
                        theModel.timer.stop();

                    } else {

                    //Resume the simulations
                        theView.settingsPanel.pauseSimulation.setText("Pause");
                        theModel.timer.start();
                    }
            }
        });

        theView.settingsPanel.simulationRate.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int value = theView.settingsPanel.simulationRate.getValue();
                System.out.println(value);
                //TODO: Make sure this timervalue is the same as the otherone
                theModel.timer.setDelay(3/(value)*1000);
            }
        });
    }

    private void modelLoader(){
        theModel.setAircraftType(theView.settingsPanel.getSelectedAircraft());
        theModel.setBoardingMethod(theView.settingsPanel.getSelectedBoardingMethod());
        theModel.setCapacity(theView.settingsPanel.getSelectedCapacity());
        theModel.setDelay(theView.settingsPanel.simulationRate.getValue());
        System.out.println("This is the simulation rate"+theView.settingsPanel.simulationRate.getValue());
        /* TODO: Add the doors used options*/
    }

    public class ButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e){
            //Set all model variables
            modelLoader();

            //Should run method in controller
            theModel.runSimulation();

        }

    }

    public class BoardingListener implements ChangeListener{
        @Override
        public void stateChanged(ChangeEvent e){
            theView.queuePanel.repaint();
        }
    }

    /*
    //TODO: Remove as never used.
    public void passengerLoader(List<Passenger> passengers){
        for(Passenger pax : passengers){
            //theView.animationPanel.passengers.add(pax);     //TODO: Needs to add the correct type - point instead of passenger
            System.out.println("Passenger added");
        }
    }*/
}
