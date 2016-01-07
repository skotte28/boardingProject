package MVCFramework;

import Aircraft.AircraftType;
import Aircraft.Position;
import Methods.Method;
import Passenger.Passenger;
import Passenger.BlockPair;
import Simulation.AircraftGrid;
import Simulation.Direction;
import XMLParsing.JAXBHandlerLayout;
import XMLParsing.JAXBHandlerPassenger;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Observable;

public class BoardingModel extends Observable{

    private String boardingMethod;
    private AircraftType aircraftType;
    private int capacity;
    protected Passenger[][] theGrid;
    protected List<Passenger> passengers;
    public BoardingModel(){}

    public AircraftType getAircraftType() {
        return aircraftType;
    }
    public void setAircraftType(String aircraftString) {
        //TODO: Should throw/catch "JAXBException"
        AircraftType aircraftType;
        File fileLayout = new File("content/"+aircraftString+"/"+aircraftString+"Layout.xml");
        aircraftType = JAXBHandlerLayout.unmarshal(fileLayout);
        this.aircraftType = aircraftType;
    }

    public String getBoardingMethod() {
        return boardingMethod;
    }
    public void setBoardingMethod(String boardingMethod) {
        this.boardingMethod = boardingMethod;
    }

    public int getCapacity() {
        return capacity;
    }
    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public List<Passenger> capacityLimiter(List<Passenger> allPassengers){

        Collections.shuffle(allPassengers);
        double xDouble = allPassengers.size()*(getCapacity()/100.0);
        //TODO: Determine if it's necessary to round and cast to integer
        int x = (int) Math.round(xDouble);
        System.out.println("Capacity: "+x);

        List<Passenger> selectedPassengers = new ArrayList<Passenger>();

        for(int i=0; i<x; i++){
            System.out.println(i);
            selectedPassengers.add(allPassengers.get(i));
        }
        return selectedPassengers;
    }

    protected void runSimulation(){

        aircraftType.populateSeats();
        //Get passengers
        File filePassengers = new File("content/"+aircraftType+"/"+aircraftType+"Passengers.xml");
        //TODO: Should throw/catch "JAXBException"
        List<Passenger> passengers = JAXBHandlerPassenger.unmarshal(filePassengers);

        //Check so passengers are in the array
        //System.out.println(passengers.toString());

        //(Assign parameters)

        //Capacity
        passengers = capacityLimiter(passengers);

        //Order
        String method = getBoardingMethod();
        if(method.equalsIgnoreCase("Back-to-front")){
            passengers = Method.backToFront(passengers, aircraftType);
        }
        else if(method.equalsIgnoreCase("Outside-in")){
            passengers = Method.outsideIn(passengers, aircraftType);
        }
        else if(method.equalsIgnoreCase("Random")){
            passengers = Method.random(passengers);
            //TODO: Does this really need to be here as it is already ordered randomly
        }
        else if(method.equalsIgnoreCase("Even-odd")) {
            passengers = Method.innovative(passengers);
        }

        System.out.println("After mixing: "+passengers);

        //Make into queue

        //Animation of each passenger
        /*Removed for testing: AircraftGrid aircraftGrid = new AircraftGrid(getAircraftType(), passengers);*/
        gridBoarder(aircraftType.getWidth(), aircraftType.getAisle(), aircraftType.getBuffer(), passengers);
    }

    ///BELOW IS FROM GRIDBOARDER

    public void gridBoarder(int width, int aisle, int buffer, List<Passenger> passengers) {
        this.passengers = passengers;
        theGrid = new Passenger[width + aisle][aircraftType.getRows()+buffer];

        //Check if all passengers are in the correct seat
        while ((!passengers.isEmpty()) || !allSeated()) {

            //TODO: Remove print statement - for testing purposes
            System.out.println("Passengers: " + passengers.toString());

            for (int p = theGrid.length - 1; p >= 0; p--) {             //Start from position 'A'
                for (int r = theGrid[p].length - 1; r >= 0; r--) {      //Start from last row


                    //Is there a passenger in the cell?
                    if (theGrid[p][r] != null) {

                        if (!theGrid[p][r].isVisited()) {
                            theGrid[p][r].setVisited(true);

                            //Is the passenger seated?
                            if (!theGrid[p][r].isSeated()) {

                                boolean proceed = false;

                                if (theGrid[p][r].getBlockPair() == null) {

                                    proceed = true;

                                } else {

                                    if (returnedToSeat(theGrid[p][r].getBlockPair().getPos(), theGrid[p][r].getBlockPair().getRow())) {
                                        theGrid[p][r].setBlockPair(null);
                                        proceed = true;
                                    }

                                }

                                if (proceed) {
                                    if (theGrid[p][r].getRow() == r + 1) {
                                        if (blockChecker(theGrid[p][r], p, r)) {
                                            blockSeater(p, r, aisle, width);
                                            proceed = false;
                                        }
                                    }
                                    if (proceed) {
                                        nextMoveSetter(theGrid[p][r], r, p);

                                        Direction nextMove = theGrid[p][r].getNextMove();
                                        Passenger currentPax = theGrid[p][r];

                                        //TODO: Remove print statement - for testing purposes
                                        //System.out.println(currentPax);

                                        //Check if possible & execute
                                        switch (nextMove) {
                                            case FRONTWARDS:
                                                if (isFree(p, r - 1)) {
                                                    move(p, r, p, r - 1);
                                                }
                                                break;
                                            case REARWARDS:
                                                if (isFree(p, r + 1)) {
                                                    move(p, r, p, r + 1);
                                                }
                                                break;
                                            case STARBOARD:
                                                if (isFree(p - 1, r)) {
                                                    move(p, r, p - 1, r);
                                                }
                                                break;
                                            case PORT:
                                                if (isFree(p + 1, r)) {
                                                    move(p, r, p + 1, r);
                                                }
                                                break;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            //Check if there are still passengers waiting
            if (!passengers.isEmpty()) {
                //If there is space for a new passenger bring them onboard
                if (theGrid[(width + 1) / 2][0] == null) {
                    theGrid[(width + 1) / 2][0] = passengers.remove(0);
                }
            }

            //TODO: Discuss with Bahar how much this affects run time - should it be done differently?

            //Reset visited
            for (int p = theGrid.length - 1; p >= 0; p--) {             //Start from position 'A'
                for (int r = theGrid[p].length - 1; r >= 0; r--) {      //Start from last row
                    if(theGrid[p][r] != null){
                        theGrid[p][r].setVisited(false);
                    }
                }
            }
            //TODO: Remove print statement - for testing purposes
            //System.out.println("Post-grid:" + Arrays.deepToString(theGrid));
            /*try{
                Thread.sleep(1000);*/
                setChanged();
                notifyObservers();
            /*}
            catch (InterruptedException ie){
                ie.getStackTrace();
            }*/
        }

        //TODO: Remove print statement - for testing purposes
        System.out.println("Stopped looping");
    }

    private boolean isFree(int p, int r) {
        return theGrid[p][r] == null;
    }

    private boolean allSeated(){
        for(Passenger[] position : theGrid){
            for(Passenger pax : position){
                if(pax != null) {
                    if (!pax.isSeated()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private void move(int pOld, int rOld, int pNew, int rNew){
        theGrid[pNew][rNew] = theGrid[pOld][rOld];
        theGrid[pOld][rOld] = null;

        if(theGrid[pNew][rNew].getPosition().getPositionValue() == pNew && theGrid[pNew][rNew].getRow() == rNew){
            theGrid[pNew][rNew].setSeated(true);
        }

        if(theGrid[pNew][rNew].getTempPosition() != null){
            if(theGrid[pNew][rNew].getTempPosition().getPositionValue() == pNew && theGrid[pNew][rNew].getTempRow() == rNew){
                theGrid[pNew][rNew].setTempPosition(null);
            }
        }

        theGrid[pNew][rNew].setNextMove(null);
    }

    private void nextMoveSetter(Passenger currentPax, int r, int p){

        Position movePosition;
        int moveRow;
        boolean temp;

        if(currentPax.getTempPosition() != null) {
            movePosition = currentPax.getTempPosition();
            moveRow = currentPax.getTempRow();
            temp = true;
        } else {
            movePosition = currentPax.getPosition();
            moveRow = currentPax.getRow();
            temp = false;
        }




        /* Needs to check if there is are temporary position/row, and if met, then these need to be discarded */

        //Correct row
        //Normal
        if(!temp) {
            if (moveRow == r) {
                if (movePosition.getPositionValue() == p) {
                    currentPax.setSeated(true);
                    currentPax.setNextMove(Direction.SEATED);
                }
                //Too far port side
                else if (movePosition.getPositionValue() > p) {
                    currentPax.setNextMove(Direction.PORT);
                }
                //Too far starboard side
                else if (movePosition.getPositionValue() < p) {
                    currentPax.setNextMove(Direction.STARBOARD);
                }
            }
            //Too far forward
            else if (moveRow > r) {
                currentPax.setNextMove(Direction.REARWARDS);
            }
            //Too far back
            else if (moveRow < r) {
                currentPax.setNextMove(Direction.FRONTWARDS);
            }
        }

        //Temporary
        else if (temp){
            if(movePosition.getPositionValue() == p){
                if(moveRow == r){
                    currentPax.setTempPosition(null);
                } else if(moveRow > r){
                    currentPax.setNextMove(Direction.REARWARDS);
                } else if(moveRow < r){
                    currentPax.setNextMove(Direction.FRONTWARDS);
                }
            } else if(movePosition.getPositionValue() > p){
                currentPax.setNextMove(Direction.PORT);
            } else if(movePosition.getPositionValue() < p){
                currentPax.setNextMove(Direction.STARBOARD);
            }
        }
    }

    private boolean blockChecker(Passenger passenger, int p, int r){

        if(passenger.getPosition() == Position.A){
            if(!isFree(Position.B.getPositionValue(),r+1)){
                return true;
            }
        }
        else if(passenger.getPosition() == Position.D){
            if(!isFree(Position.C.getPositionValue(),r+1)){
                return true;
            }
        }
        return false;
    }

    private void blockSeater(int p, int r, int aisle, int width){
        int movedistance = 2;
        boolean first = true;
        //New Position Assigner

        if(theGrid[p][r].getPosition().getPositionValue() > Position.AISLE.getPositionValue()) {
            for (int i = width; i > Position.AISLE.getPositionValue(); i--) {
                if (theGrid[i][r+1] != null) {
                    movedistance++;
                }
            }
            for (int i = width; i > Position.AISLE.getPositionValue(); i--) {
                if (theGrid[i][r+1] != null) {
                    theGrid[i][r+1].setTempPosition(Position.AISLE);
                    theGrid[i][r+1].setTempRow(r + movedistance);
                    theGrid[i][r+1].setSeated(false);
                    if (first) {
                        if (theGrid[p][r - 1] != null) {
                            theGrid[p][r - 1].setBlockPair(new BlockPair(i, r+1));
                        }
                    }
                }

            }
        }

        else if(theGrid[p][r].getPosition().getPositionValue() < Position.AISLE.getPositionValue()){
            for(int i = 0; i < Position.AISLE.getPositionValue(); i++){
                if(theGrid[i][r] != null){
                    movedistance++;
                }
            }
            for(int i = 0; i < Position.AISLE.getPositionValue(); i++){
                if(theGrid[i][r+1] != null){
                    theGrid[i][r+1].setTempPosition(Position.AISLE);
                    theGrid[i][r+1].setTempRow(r+movedistance);
                    theGrid[i][r+1].setSeated(false);
                    if(first){
                        if(theGrid[p][r-1] != null){
                            theGrid[p][r-1].setBlockPair(new BlockPair(i,r+1));
                        }
                    }
                }
            }
        }

        //move each piece
        //invalid temps
        //continue as usual?
    }

    private boolean returnedToSeat(int pos, int row) {

        if(theGrid[pos][row] != null) {
            return theGrid[pos][row].isSeated();
        }

        return false;
    }

    public Passenger[][] getTheGrid() {
        return theGrid;
    }

    public List<Passenger> getPassengers() {
        return passengers;
    }
}