package MVCFramework;

import Aircraft.AircraftType;
import Exceptions.DeadlockException;
import Exceptions.NoSelectedException;
import Methods.Method;
import Passenger.Passenger;
import Passenger.BlockPair;
import Simulation.Direction;
import XMLParsing.JAXBHandlerLayout;
import XMLParsing.JAXBHandlerPassenger;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class BoardingModel extends Observable{
    boolean haltFront = false;

    public Timer timer;
    private int delay;
    private boolean completed;
    private boolean inProcess;
    private boolean textOutput;

    private int isSeatCount;
    private int totalPax;

    private String boardingMethod;
    private AircraftType aircraftType;
    private int capacity;
    protected Passenger[][] theGrid;
    protected List<Passenger> passengers;
    protected Map seats = new HashMap();
    public BoardingModel(){}
    private int modelIteration = 0;
    private boolean nothingChanged;
    private int stallCount = 0;

    public AircraftType getAircraftType() {
        return aircraftType;
    }
    public void setAircraftType(String aircraftString) {
        AircraftType aircraftType;
        File fileLayout = new File("content/"+aircraftString+"/"+"Layout.xml");
        aircraftType = JAXBHandlerLayout.unmarshal(fileLayout);
        this.aircraftType = aircraftType;
        //System.out.println("Set airplane to:"+this.aircraftType); - Enable for testing
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

    private void populateSeats() {
        //System.out.println("Populating airplane"+aircraftType); - Enable for testing
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader("content/" + aircraftType + "/" + "Seating.txt"));
            String line = bufferedReader.readLine();
            while(line != null){
                String[] output = line.split(":");
                seats.put(output[0], Integer.valueOf(output[1]));
                if(!output[2].equals("N")){
                    switch (output[2]) {
                        case "W":
                            aircraftType.addWindowSeat(output[0]);
                            break;
                        case "M":
                            aircraftType.addMiddleSeat(output[0]);
                            break;
                        case "A":
                            aircraftType.addAisleSeat(output[0]);
                            break;
                    }
                }
                line = bufferedReader.readLine();
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        //System.out.println(seats);
    }

    public int getSeatValue(String seat){
        return (Integer) seats.get(seat);
    }

    public List<Passenger> capacityLimiter(List<Passenger> allPassengers){

        Collections.shuffle(allPassengers);
        double xDouble = allPassengers.size()*(getCapacity()/100.0);
        //TODO: Determine if it's necessary to round and cast to integer
        int x = (int) Math.round(xDouble);
        totalPax = x;

        List<Passenger> selectedPassengers = new ArrayList<Passenger>();

        for(int i=0; i<x; i++){
            selectedPassengers.add(allPassengers.get(i));
        }
        return selectedPassengers;
    }

    public void runSimulation(){
        delay = getDelay();
        completed = false;

        //Get passengers
        //System.out.println("Airplane:"+aircraftType); - Enable for testing
        File filePassengers = new File("content/"+aircraftType+"/"+"Passengers.xml");
        passengers = JAXBHandlerPassenger.unmarshal(filePassengers);

        //(Assign parameters)

        //Capacity
        passengers = capacityLimiter(passengers);

        //Order - if Random do nothing as passengers have been shuffled inside the capacityLimiter
        String method = getBoardingMethod();
        //System.out.println("Method: "+method); - Enable for testing
        if(method.equalsIgnoreCase("Back-to-front")){
            passengers = Method.backToFront(passengers, aircraftType);
        }
        else if(method.equalsIgnoreCase("Outside-in")){
            passengers = Method.outsideIn(passengers, aircraftType);
        }
        else if(method.equalsIgnoreCase("Even-odd")) {
            passengers = Method.innovative(passengers);
        }

        setPassengers(passengers);
        //System.out.println("Original passengers:"+passengers); - Enable for testing
        try{
            PrintWriter writer = new PrintWriter("./screenshots/pax.txt");
            writer.println(passengers);
            writer.close();
        } catch (FileNotFoundException e){
            e.printStackTrace();
        }
        theGrid = new Passenger[aircraftType.getWidth() + aircraftType.getAisle()][aircraftType.getRows()+aircraftType.getBuffer()];

        ActionListener taskPerformer = new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                if(!completed){
                    gridBoarder(aircraftType.getWidth(), aircraftType.getAisle(), getPassengers());
                }
            }
        };

        inProcess = true;
        timer = new Timer(delay, taskPerformer);
    }

    //BELOW IS FROM GRIDBOARDER

    public void gridBoarder(int width, int aisle, List<Passenger> passengers) {
        modelIteration++;
        this.passengers = passengers;

        //Check if all passengers are in the correct seat
        while ((!passengers.isEmpty()) || !allSeated()) {
            nothingChanged = true;
            //System.out.println("The Grid:"+theGrid); - Enable for testing
            for (int p = theGrid.length - 1; p >= 0; p--) {             //Start from position 'A'
                for (int r = theGrid[p].length - 1; r >= 0; r--) {      //Start from last row

                    //Is there a passenger in the cell?
                    if (theGrid[p][r] != null) {

                        if(theGrid[p][r].getIteration() < modelIteration){
                            theGrid[p][r].setIteration(modelIteration);

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
                                        if (blockChecker(theGrid[p][r], p, r, width)) {
                                            blockSeater(p, r, aisle, width);
                                            proceed = false;
                                        }
                                    }
                                    if (proceed) {
                                        nextMoveSetter(theGrid[p][r], r, p);

                                        Direction nextMove = theGrid[p][r].getNextMove();
                                        Passenger currentPax = theGrid[p][r];

                                        //Check if possible & execute
                                        if (nextMove != null) {
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
                                                case HOLD:
                                                    break;
                                            }
                                        } else {
                                            //System.out.println(currentPax + " did not have move instructions"); - Enable for testing
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            if(nothingChanged){
                //System.out.println("Stall!"); - Enable for testing
                stallCount++;
                if(stallCount > 10){
                    modelIteration = -1;
                    timer.stop();
                    new DeadlockException();
                    break;
                }
                /*int reserveAisle = getSeatValue("AISLE");
                for(int i = aircraftType.getRows(); i>=0; i--){
                    if(theGrid[reserveAisle][i] != null){
                        theGrid[reserveAisle][i].setTempRow(i+1);
                        theGrid[reserveAisle][i].setTempPosition("AISLE");
                        break;
                    }
                }*/
            } else {
                stallCount = 0;
            }

            //Check if there are still passengers waiting
            if (!passengers.isEmpty()) {
                //If there is space for a new passenger bring them onboard
                if (theGrid[(width + 1) / 2][0] == null) {
                    if(!haltFront) {
                        theGrid[(width + 1) / 2][0] = passengers.remove(0);
                    } else{
                        //TODO: Tidy this up
                        boolean allFree = true;
                        for(int i = 1; i <= width/2; i++){
                            if(!isFree((width + 1) / 2,i)){
                                allFree = false;
                            }
                        }
                        if(allFree) {
                            haltFront = false;
                        }
                    }
                }
            }
            setChanged();
            notifyObservers();
            return;
        }
        //System.out.println("Stopped looping"); - Enable for testing
        completed = true;
        //STOP EVERYTHING
        this.passengers = null;
        timer.stop();
        if(textOutput){
            resultsOutput();
        }
        inProcess = false;
        setChanged();
        notifyObservers(completed);
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
        if(nothingChanged){
            nothingChanged = false;
        }

        theGrid[pNew][rNew] = theGrid[pOld][rOld];
        theGrid[pOld][rOld] = null;

        if(getSeatValue(theGrid[pNew][rNew].getPosition()) == pNew && theGrid[pNew][rNew].getRow() == rNew){
            theGrid[pNew][rNew].setSeated(true);
            isSeatCount++;
        }

        if(theGrid[pNew][rNew].getTempRow() != -1){
            if(getSeatValue(theGrid[pNew][rNew].getTempPosition()) == pNew && theGrid[pNew][rNew].getTempRow() == rNew) {
                if(theGrid[pNew][rNew-1] != null) {
                    theGrid[pNew][rNew].setTempRow(-1);    //Used to be set to null
                    //System.out.println(theGrid[pNew][rNew] + "has reached its TempPosition"); - Enable for testing
                }
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
                    isSeatCount++;
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
                    if(theGrid[p][r-1] != null) {
                        currentPax.setTempRow(-1);     //Used to be set to null
                    }else{
                        boolean override = true;
                        for(int i = currentPax.getTempRow()-1; i>=currentPax.getRow(); i--){
                            if(!isFree(getSeatValue(currentPax.getTempPosition()), i)){
                                override = false;
                            }
                        }
                        if(override){
                            currentPax.setTempRow(-1);
                        }
                        //System.out.println("The spot which blocked -1 for "+currentPax+" contained "+theGrid[p][r-1]); - Enable for testing
                    }
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

    private boolean blockChecker(Passenger passenger, int p, int r, int width){
        if(getSeatValue(passenger.getPosition()) < getSeatValue("AISLE")-1){
            for(int i= getSeatValue("AISLE")-1; i>=0; i--){
                if(theGrid[i][r+1]!= null){
                    if(getSeatValue(theGrid[i][r+1].getPosition()) > getSeatValue(passenger.getPosition())){
                        return true;
                    }
                }
            }
        }
        else if(getSeatValue(passenger.getPosition()) > getSeatValue("AISLE")+1){
            for(int i= getSeatValue("AISLE")+1; i<width; i++){
                if(theGrid[i][r+1]!= null){
                    if(getSeatValue(theGrid[i][r+1].getPosition()) < getSeatValue(passenger.getPosition())){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private void blockSeater(int p, int r, int aisle, int width){
        int movedistance = 2;
        boolean first = true;
        //New Position Assigner
        //if(isFree(p,r+1)) {
        if (getSeatValue(theGrid[p][r].getPosition()) > getSeatValue("AISLE")) {
            for (int i = getSeatValue(theGrid[p][r].getPosition()); i > getSeatValue("AISLE"); i--) {
                if (theGrid[i][r + 1] != null) {
                    movedistance++;
                }
            }
            for (int i = getSeatValue(theGrid[p][r].getPosition()); i > getSeatValue("AISLE"); i--) {
                if (theGrid[i][r + 1] != null) {
                    if (theGrid[i][r + 1].getTempRow() == -1) {
                        theGrid[i][r + 1].setTempPosition("AISLE");
                        theGrid[i][r + 1].setTempRow(r + (movedistance - (i - getSeatValue("AISLE"))));
                        theGrid[i][r + 1].setSeated(false);
                        isSeatCount--;
                        //System.out.println("TempPostion for " + theGrid[i][r + 1] + " is " + theGrid[i][r + 1].getTempRow() + theGrid[i][r + 1].getTempPosition()); - Enable for testing
                        if (first) {
                            if (r > 0) {
                                for (int j = 0; j < movedistance; j++) {
                                    //System.out.println("The problem is: " + (r - 1 - j)); - Enable for testing
                                    if (r - 1 - j >= 0) {
                                        if (theGrid[p][r - 1 - j] != null) {
                                            theGrid[p][r - 1 - j].setBlockPair(new BlockPair(getSeatValue(theGrid[i][r + 1].getPosition()), theGrid[i][r + 1].getRow()));
                                            //System.out.println("The block pair for " + theGrid[p][r - 1] + " is [" + i + "," + (r + 1) + "] " + "(" + theGrid[i][r + 1] + ")"); - Enable for testing
                                            break;
                                        }
                                    }
                                }
                            } else {
                                haltFront = true;
                            }
                        }
                    }
                }
            }
        } else if (getSeatValue(theGrid[p][r].getPosition()) < getSeatValue("AISLE")) {
            for (int i = getSeatValue(theGrid[p][r].getPosition()); i < getSeatValue("AISLE"); i++) {
                if (theGrid[i][r + 1] != null) {
                    movedistance++;
                }
            }
            for (int i = getSeatValue(theGrid[p][r].getPosition()); i < getSeatValue("AISLE"); i++) {
                if (theGrid[i][r + 1] != null) {
                    if (theGrid[i][r + 1].getTempRow() == -1) {
                        theGrid[i][r + 1].setTempPosition("AISLE");
                        theGrid[i][r + 1].setTempRow(r + (movedistance - (getSeatValue("AISLE") - i)));
                        theGrid[i][r + 1].setSeated(false);
                        isSeatCount--;
                        //System.out.println("TempPostion for " + theGrid[i][r + 1] + " is " + theGrid[i][r + 1].getTempRow() + theGrid[i][r + 1].getTempPosition()); - Enable for testing
                        if (first) {
                            //System.out.println("R minus one:" + (r - 1)); - Enable for testing
                            if (r > 0) {
                                for (int j = 0; j < movedistance; j++) {
                                    //System.out.println("The problem is: " + (r - 1 - j)); - Enable for testing
                                    if (r - 1 - j >= 0) {
                                        if (theGrid[p][r - 1 - j] != null) {
                                            theGrid[p][r - 1 - j].setBlockPair(new BlockPair(getSeatValue(theGrid[i][r + 1].getPosition()), theGrid[i][r + 1].getRow()));
                                            //System.out.println("The block pair for " + theGrid[p][r - 1] + " is [" + i + "," + (r + 1) + "] " + "(" + theGrid[i][r + 1] + ")"); - Enable for testing
                                            break;
                                        }
                                    }
                                }
                            } else {
                                haltFront = true;
                            }
                        }
                    }
                }
            }
        }

        //move each piece
        //invalid temps
        //continue as usual?
        //}
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

    public void clear(){
        this.setCompleted(false);
        this.setBoardingMethod(null);
        this.setCapacity(-1);
        this.setDelay(2);
        this.setIsSeatCount(0);
        this.setCompleted(false);
        this.setTotalPax(0);
        this.setModelIteration(0);
        this.setInProcess(false);
        stallCount = 0;
        this.theGrid = null;
        this.timer = null;
        setChanged();
        notifyObservers();
    }

    // BELOW IS FOR TIMER

    public int getDelay() {
        return delay;
    }

    public void setDelay(int value) {
        int[] delayValues = {1000, 500, 250, 50, 12};
        this.delay = delayValues[value];
    }

    public boolean isInProcess() {
        return inProcess;
    }

    public void setInProcess(boolean inProcess) {
        this.inProcess = inProcess;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public int getIsSeatCount() {
        return isSeatCount;
    }

    public void setIsSeatCount(int isSeatCount) {
        this.isSeatCount = isSeatCount;
    }

    public int getTotalPax() {
        return totalPax;
    }

    public void setTotalPax(int totalPax) {
        this.totalPax = totalPax;
    }

    public int getModelIteration() {
        return modelIteration;
    }

    public void setModelIteration(int modelIteration) {
        this.modelIteration = modelIteration;
    }

    private void resultsOutput(){
        try{
            Date date = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
            File file = new File("results/result"+dateFormat.format(date)+".txt");
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(String.format("Boarding of %s"+(completed ? " did" : " did not")+" complete successfully.%n",getAircraftType().toString()));
            if(completed){
                fileWriter.write(String.format("The boarding method was: %s%n",getBoardingMethod().toString()));
                fileWriter.write(String.format("Capacity was: %d%% (%d pax)%n", capacity, getTotalPax()));
                fileWriter.write(String.format("Iterations to complete: %d%n",getModelIteration()));
            }
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public void setTextOutput(boolean textOutput) {
        this.textOutput = textOutput;
    }
}