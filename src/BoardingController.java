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

        AircraftType aircraft = theModel.getAircraftType();

        //Get passengers
        File filePassengers = new File("src/"+aircraft);
        //TODO: Should throw/catch "JAXBException"
        List<Passenger> passengers = JAXBHandler.unmarshallPassenger(filePassengers);

        //Check so passengers are in the array
        System.out.println(passengers.toString());

        //Get aircraft
        //TODO: Should throw/catch "JAXBException"
        File fileLayout = new File("src/"+aircraft+"Layout.xml");
        AircraftType aircraftType = JAXBHandler.unmarshallLayout(fileLayout);

        //Set the model to the new aircraft with all parameters... This is quite inefficient as we load it twice...
        theModel.setAircraftType(aircraftType);

        //Check so aircraft is loaded
        System.out.println(aircraftType);

        //(Assign parameters)

        //Capacity
        passengers = capacityLimiter(passengers);

        //Order
        String method = theModel.getBoardingMethod();
        if(method.equalsIgnoreCase("Back-to-front")){
            passengers = Method.backToFront(passengers, aircraft);
        }
        else if(method.equalsIgnoreCase("Outside-in")){
            passengers = Method.outsideIn(passengers);
        }
        else if(method.equalsIgnoreCase("Random")){
            passengers = Method.random(passengers);
            //TODO: Does this really need to be here as it is already ordered randomly
        }
        else if(method.equalsIgnoreCase("Even-odd")) {
            passengers = Method.innovative(passengers);
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
