import Aircraft.AircraftType;
import Methods.*;
import Passenger.*;
import Simulation.AircraftGrid;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EventListener;
import java.util.List;

public class BoardingController implements EventListener{

    private BoardingView theView = new BoardingView();
    private BoardingModel theModel = new BoardingModel();

    public BoardingController(BoardingView theView, BoardingModel theModel) {
        this.theView = theView;
        this.theModel = theModel;

        theView.settingsPanel.startSimulation.addActionListener(new ButtonListener());

        theView.settingsPanel.pauseSimulation.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Paused");

                    //Pause the simulation
                    if(theView.settingsPanel.pauseSimulation.getText().equals("Pause")){
                        theView.settingsPanel.pauseSimulation.setText("Resume");
                        //TODO: Actually pause simulation
                    } else {

                    //Resume the simulations
                        theView.settingsPanel.pauseSimulation.setText("Pause");
                        //TODO: Actually resume simulation
                    }
            }
        });
    }

    private void modelLoader(){
        System.out.println("modelLoader started");
        theModel.setAircraftType(theView.settingsPanel.getSelectedAircraft());
        theModel.setBoardingMethod(theView.settingsPanel.getSelectedBoardingMethod());
        theModel.setCapacity(theView.settingsPanel.getSelectedCapacity());

        /* TODO: Add the doors used options*/
    }

    private void runSimulation(){
        //Get passengers
        File file = new File("src/"+theModel.getAircraftType());
        List<Passenger> passengers = JAXBHandler.unmarshall(file);


        //Check so passengers are in the array
        System.out.println(passengers.toString());

        //(Assign parameters)

        //Capacity
        passengers = capacityLimiter(passengers);

        //Order
        String method = theModel.getBoardingMethod();
        if(method.equalsIgnoreCase("Back-to-front")){
            //TODO: Back to front method call
        }
        else if(method.equalsIgnoreCase("Outside-in")){
            //TODO: Outside-in method call
        }
        else if(method.equalsIgnoreCase("Random")){
            //TODO: Random method
            //Do nothing as already ordered randomly
        }
        else if(method.equalsIgnoreCase("Even-odd")) {
            //TODO: Even-odd/Innovative method call
        }

        //Make into queue

        //Animation of each passenger
        AircraftGrid aircraftGrid = new AircraftGrid(theModel.getAircraftType(), passengers);


    }

    public List<Passenger> capacityLimiter(List<Passenger> allPassengers){

        Collections.shuffle(allPassengers);
        int x = allPassengers.size()*(theModel.getCapacity()/100);
        System.out.println("Capacity: "+x);

        List<Passenger> selectedPassengers = new ArrayList<Passenger>();

        for(int i=0; i<x; i++){
            selectedPassengers.add(allPassengers.get(i));
        }
        System.out.println("Returning the selected passengers");
        return selectedPassengers;
    }

    public class ButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e){
            //Message for testing purposes
            System.out.println("Pressed the button");

            //set all model variables
            modelLoader();

            //should run method in controller
            runSimulation();

        }

    }

    public void passengerLoader(List<Passenger> passengers){
        for(Passenger pax : passengers){
            //theView.animationPanel.passengers.add(pax);     //TODO: Needs to add the correct type - point instead of passenger
            System.out.println("Passenger added");
        }
    }


/*
    An attempt to get the listeners from the BoardingController class


    ActionListener getActionListener(final JComboBox source) {
        System.out.println("Calls this");
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Somethings changed again - BoardingController");
                //animationPanel.setLayoutCells(settingsPanel.getSelectedAircraft());
            }
        };
    }

    ActionListener getButtonListener(final JButton source){
        System.out.println("Button");
        return new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                System.out.println("Button2");
            }
        };
    }
*/
}
