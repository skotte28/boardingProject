package MVCFramework;

import Aircraft.AircraftType;
import Methods.Method;
import Passenger.Passenger;
import Passenger.BlockPair;
import Simulation.Direction;
import XMLParsing.JAXBHandlerLayout;
import XMLParsing.JAXBHandlerPassenger;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;

public class BoardingModel extends Observable{
    boolean haltFront = false;

    public Timer timer;
    private int delay = 500;
    private boolean completed;

    private String boardingMethod;
    private AircraftType aircraftType;
    private int capacity;
    protected Passenger[][] theGrid;
    protected List<Passenger> passengers;
    protected Map seats = new HashMap();
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
        System.out.println("Set airplane to:"+this.aircraftType);
        populateSeats();
        setChanged();
        notifyObservers();
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

    public void populateSeats() {
        System.out.println("Populating airplane"+aircraftType);
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader("content/" + aircraftType + "/" + aircraftType + "Seating.txt"));
            String line = bufferedReader.readLine();
            while(line != null){
                String[] output = line.split(":");
                seats.put(output[0], Integer.valueOf(output[1]));
                line = bufferedReader.readLine();
            }
        } catch (Exception e){
            //TODO: Better exception handling
            e.printStackTrace();
        }
        System.out.println(seats);
    }

    public int getSeatValue(String seat){
        return (Integer) seats.get(seat);
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
        delay = getDelay(); //milliseconds
        completed = false;

        //TODO: Seat population is done in the setAircraftType, so it might not have to be done again
        populateSeats();
        //Get passengers
        System.out.println("Airplane:"+aircraftType);
        File filePassengers = new File("content/"+aircraftType+"/"+aircraftType+"Passengers.xml");
        //TODO: Should throw/catch "JAXBException"
        passengers = JAXBHandlerPassenger.unmarshal(filePassengers);

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

        setPassengers(passengers);
        theGrid = new Passenger[aircraftType.getWidth() + aircraftType.getAisle()][aircraftType.getRows()+aircraftType.getBuffer()];

        //Make into queue

        //Animation of each passenger
        /*Removed for testing: AircraftGrid aircraftGrid = new AircraftGrid(getAircraftType(), passengers);*/
        //gridBoarder(aircraftType.getWidth(), aircraftType.getAisle(), aircraftType.getBuffer(), passengers);

        ActionListener taskPerformer = new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                if(!completed){
                    System.out.println("Delay:"+delay);
                    gridBoarder(aircraftType.getWidth(), aircraftType.getAisle(), aircraftType.getBuffer(), getPassengers());
                }
                else{
                    //TODO: Should there be something here, else remove
                }
            }
        };

        timer = new Timer(delay, taskPerformer);
        timer.start();
    }

    //BELOW IS FROM GRIDBOARDER

    public void gridBoarder(int width, int aisle, int buffer, List<Passenger> passengers) {
        this.passengers = passengers;

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
                    if(!haltFront) {
                        theGrid[(width + 1) / 2][0] = passengers.remove(0);
                    } else{
                        if(isFree((width + 1) / 2,0) && isFree((width + 1) / 2,1)){
                            haltFront = false;
                        }
                    }
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
                setChanged();
                notifyObservers();
                System.out.println(Arrays.deepToString(theGrid));
                return;
        }
        System.out.println(Arrays.deepToString(theGrid));
        //TODO: Remove print statement - for testing purposes
        completed = true;
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

        if(getSeatValue(theGrid[pNew][rNew].getPosition()) == pNew && theGrid[pNew][rNew].getRow() == rNew){
            theGrid[pNew][rNew].setSeated(true);
        }

        if(theGrid[pNew][rNew].getTempRow() != -1){
            if(getSeatValue(theGrid[pNew][rNew].getTempPosition()) == pNew && theGrid[pNew][rNew].getTempRow() == rNew){
                theGrid[pNew][rNew].setTempRow(-1);    //Used to be set to null
            }
        }

        theGrid[pNew][rNew].setNextMove(null);
    }

    private void nextMoveSetter(Passenger currentPax, int r, int p){

        String movePosition;
        int moveRow;
        boolean temp;

        if(currentPax.getTempRow() != -1) {
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
                if (getSeatValue(movePosition) == p) {
                    currentPax.setSeated(true);
                    currentPax.setNextMove(Direction.SEATED);
                }
                //Too far port side
                else if (getSeatValue(movePosition) > p) {
                    currentPax.setNextMove(Direction.PORT);
                }
                //Too far starboard side
                else if (getSeatValue(movePosition) < p) {
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
            if(getSeatValue(movePosition) == p){
                if(moveRow == r){
                    currentPax.setTempRow(-1);     //Used to be set to null
                } else if(moveRow > r){
                    currentPax.setNextMove(Direction.REARWARDS);
                } else if(moveRow < r){
                    currentPax.setNextMove(Direction.FRONTWARDS);
                }
            } else if(getSeatValue(movePosition) > p){
                currentPax.setNextMove(Direction.PORT);
            } else if(getSeatValue(movePosition) < p){
                currentPax.setNextMove(Direction.STARBOARD);
            }
        }
    }

    private boolean blockChecker(Passenger passenger, int p, int r){
        if(passenger.getPosition().equals("A")){
            if(!isFree(getSeatValue("B"),r+1)){
                return true;
            }
        }
        else if(passenger.getPosition().equals("D")){
            if(!isFree(getSeatValue("C"),r+1)){
                return true;
            }
        }
        return false;
    }

    private void blockSeater(int p, int r, int aisle, int width){
        int movedistance = 1;
        boolean first = true;
        //New Position Assigner

        if(getSeatValue(theGrid[p][r].getPosition()) > getSeatValue("AISLE")) {
            for (int i = width; i > getSeatValue("AISLE"); i--) {
                if (theGrid[i][r+1] != null) {
                    movedistance++;
                }
            }
            for (int i = width; i > getSeatValue("AISLE"); i--) {
                if (theGrid[i][r+1] != null) {
                    theGrid[i][r+1].setTempPosition("AISLE");
                    theGrid[i][r+1].setTempRow(r + movedistance);
                    theGrid[i][r+1].setSeated(false);
                    System.out.println("TempPostion for "+theGrid[i][r+1]+" is "+theGrid[i][r+1].getTempRow()+theGrid[i][r+1].getTempPosition());
                    if (first) {
                        if(r > 0) {
                            if (theGrid[p][r - 1] != null) {
                                theGrid[p][r - 1].setBlockPair(new BlockPair(i, r + 1));
                            }
                        }
                        else {
                            haltFront = true;
                        }
                    }
                }
            }
        }

        else if(getSeatValue(theGrid[p][r].getPosition()) < getSeatValue("AISLE")){
            for(int i = 0; i < getSeatValue("AISLE"); i++){
                if(theGrid[i][r+1] != null){
                    movedistance++;
                }
            }
            for(int i = 0; i < getSeatValue("AISLE"); i++){
                if(theGrid[i][r+1] != null){
                    theGrid[i][r+1].setTempPosition("AISLE");
                    theGrid[i][r+1].setTempRow(r+movedistance);
                    theGrid[i][r+1].setSeated(false);
                    System.out.println("TempPostion for "+theGrid[i][r+1]+" is "+theGrid[i][r+1].getTempRow()+theGrid[i][r+1].getTempPosition());
                    if(first){
                        System.out.println("R minus one:"+(r-1));
                        if(r > 0) {
                            if (theGrid[p][r - 1] != null) {
                                theGrid[p][r - 1].setBlockPair(new BlockPair(i, r + 1));
                            }
                        }
                        else {
                            haltFront = true;
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

    public void setPassengers(List<Passenger> passengers){
        this.passengers = passengers;
    }

    public List<Passenger> getPassengers() {
        return passengers;
    }

    // BELOW IS FOR TIMER


    public int getDelay() {
        return delay;
    }

    /*public void setDelay(int value) {
        this.delay = (3/value)*1000;
        System.out.println("Delay set to:"+delay);
    }*/
}