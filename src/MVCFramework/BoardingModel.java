package MVCFramework;

import Aircraft.AircraftType;
import Methods.Method;
import Passenger.Passenger;
import Passenger.BlockPair;
import Simulation.Direction;
import XMLParsing.JAXBHandlerLayout;
import XMLParsing.JAXBHandlerPassenger;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;
import java.io.*;
import java.util.*;
import java.util.List;

public class BoardingModel extends Observable{

    ArrayList<Point> blocks = new ArrayList<>();

    boolean haltFront = false;

    private int isSeatCount;
    private int totalPax;
    public Timer timer;
    private int delay;
    private boolean completed;
    private boolean inProcess;

    private String boardingMethod;
    private AircraftType aircraftType;
    private int capacity;
    protected Passenger[][] theGrid;
    protected List<Passenger> passengers;
    protected Map seats = new HashMap();
    public BoardingModel(){}
    private int modelInteration = 0;

    public AircraftType getAircraftType() {
        return aircraftType;
    }
    public void setAircraftType(String aircraftString) {
        //TODO: Should throw/catch "JAXBException"
        AircraftType aircraftType;
        File fileLayout = new File("content/"+aircraftString+"/"+"Layout.xml");
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
            BufferedReader bufferedReader = new BufferedReader(new FileReader("content/" + aircraftType + "/" + "Seating.txt"));
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
        System.out.println("Capacity: "+x);

        List<Passenger> selectedPassengers = new ArrayList<Passenger>();

        for(int i=0; i<x; i++){
            //System.out.println(i);
            selectedPassengers.add(allPassengers.get(i));
        }
        return selectedPassengers;
    }

    protected void runSimulation(){
        delay = getDelay();
        completed = false;

        //TODO: Seat population is done in the setAircraftType, so it might not have to be done again
        populateSeats();
        //Get passengers
        System.out.println("Airplane:"+aircraftType);
        File filePassengers = new File("content/"+aircraftType+"/"+"Passengers.xml");
        //TODO: Should throw/catch "JAXBException"
        passengers = JAXBHandlerPassenger.unmarshal(filePassengers);

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

        setPassengers(passengers);
        System.out.println("Original passengers:"+passengers);
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
                    //System.out.println("Delay:"+delay);
                    gridBoarder(aircraftType.getWidth(), aircraftType.getAisle(), getPassengers());
                }
                else{
                    //TODO: Should there be something here, else remove
                    /* Should probably clear model data and the animation panel information so that if aircraft is changed
                    the old passengers are not featured on there. It could also allow the run button to be enabled again.
                     */
                }
            }
        };

        inProcess = true;
        timer = new Timer(delay, taskPerformer);
    }

    //BELOW IS FROM GRIDBOARDER

    public void gridBoarder(int width, int aisle, List<Passenger> passengers) {
        modelInteration++;
        this.passengers = passengers;

        //Check if all passengers are in the correct seat
        while ((!passengers.isEmpty()) || !allSeated()) {
            System.out.println("The Grid:"+theGrid);
            for (int p = theGrid.length - 1; p >= 0; p--) {             //Start from position 'A'
                for (int r = theGrid[p].length - 1; r >= 0; r--) {      //Start from last row

                    Point pt = new Point(p,r);
                    //System.out.println(pt);
                    if(blocks.contains(pt)){
                        boolean release = false;
                        for(int i = r-1; i<r+(width/2) && i<=aircraftType.getRows(); i++){
                            if(theGrid[p][i] != null){
                                System.out.println(theGrid[p][i].toString());
                                if(theGrid[p][i].getRow() == r){
                                    break;
                                }
                            }
                            release = true;
                        }
                        System.out.println(release);
                        if(release){
                            System.out.println("Removed!");
                            //System.out.println(blocks.indexOf(new Point(p,r)));
                            blocks.remove(blocks.indexOf(new Point(p,r)));
                        }
                    }

                    //Is there a passenger in the cell?
                    if (theGrid[p][r] != null) {

                        System.out.println("CB:"+blocks.toString());

                        if(theGrid[p][r].getIteration() < modelInteration){
                            theGrid[p][r].setIteration(modelInteration);
                            //Is the passenger seated?
                            if (!theGrid[p][r].isSeated()) {

                                boolean proceed = true;

                                if (proceed) {
                                    if (theGrid[p][r].getRow() == r + 1) {
                                        System.out.println(theGrid[p][r].toString()+" is here: "+p+" "+r);
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
                                                    if (isFree(p, r - 1) && squareBlockChecker(p,r-1,currentPax.getRow())) {
                                                        move(p, r, p, r - 1);
                                                    }
                                                    break;
                                                case REARWARDS:
                                                    if (isFree(p, r + 1) && squareBlockChecker(p,r+1,currentPax.getRow())) {
                                                        move(p, r, p, r + 1);
                                                    }
                                                    break;
                                                case STARBOARD:
                                                    if (isFree(p - 1, r) && squareBlockChecker(p-1,r,currentPax.getRow())) {
                                                        move(p, r, p - 1, r);
                                                    }
                                                    break;
                                                case PORT:
                                                    if (isFree(p + 1, r) && squareBlockChecker(p+1,r,currentPax.getRow())) {
                                                        move(p, r, p + 1, r);
                                                    }
                                                    break;
                                                case HOLD:
                                                    break;
                                            }
                                        } else {
                                            System.out.println(currentPax + " did not have move instructions");
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
            //TODO: Remove print statement - for testing purposes
            //System.out.println("Post-grid:" + Arrays.deepToString(theGrid));
                setChanged();
                notifyObservers();
                return;
        }
        System.out.println(Arrays.deepToString(theGrid));
        //TODO: Remove print statement - for testing purposes
        completed = true;
        System.out.println("Stopped looping");
        //STOP EVERYTHING
        this.passengers = null;
        timer.stop();
        boolean finished = true;
        notifyObservers(finished);
        inProcess = false;
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
            isSeatCount++;
        }

        if(theGrid[pNew][rNew].getTempRow() != -1){
            if(getSeatValue(theGrid[pNew][rNew].getTempPosition()) == pNew && theGrid[pNew][rNew].getTempRow() == rNew) {
                if(theGrid[pNew][rNew-1] != null) {
                    theGrid[pNew][rNew].setTempRow(-1);    //Used to be set to null
                    System.out.println(theGrid[pNew][rNew] + "has reached its TempPosition");
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
                        System.out.println("The spot which blocked -1 for "+currentPax+" contained "+theGrid[p][r-1]);
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

    private boolean squareBlockChecker(int p, int r, int paxRow){
        if(r == paxRow){
            System.out.println("Row: "+paxRow);
            return true;
        }
        Point pt = new Point(p,r);
        if (blocks.size() > 0) {
            if (blocks.contains(pt)) {
                return false;
            }
            for(int i = r; i<r+(aircraftType.getWidth()/2) && i<=aircraftType.getRows(); i++){
                if(theGrid[p][i] != null) {
                    System.out.println(theGrid[p][i].toString());
                    if (theGrid[p][i].getRow() == r) {
                        return false;
                    }
                }
            }
        }
        if(theGrid[p][r] != null){
            System.out.println(theGrid[p][r].toString()+" was fine 2");
        }
        return true;
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
                        System.out.println("R:" + r);
                        if (theGrid[i][r + 1].getTempRow() == -1) {
                            theGrid[i][r + 1].setTempPosition("AISLE");
                            theGrid[i][r + 1].setTempRow(r + (movedistance - (i - getSeatValue("AISLE"))));
                            theGrid[i][r + 1].setSeated(false);
                            isSeatCount--;
                            System.out.println("TempPostion for " + theGrid[i][r + 1] + " is " + theGrid[i][r + 1].getTempRow() + theGrid[i][r + 1].getTempPosition());
                            if (first) {
                                if (r > 0) {
                                    Point newpt = new Point(getSeatValue("AISLE"), r+1);
                                    if(!blocks.contains(newpt)) {
                                        blocks.add(newpt);
                                        System.out.println("New block: " + getSeatValue("AISLE") + " " + (r + 1));
                                        System.out.println("The block pair for " + theGrid[p][r - 1] + " is [" + i + "," + (r + 1) + "] " + "(" + theGrid[i][r + 1] + ")");
                                        break;
                                    } else {
                                        System.out.println("Other");
                                        //TODO:Same row block
                                    }
                                }
                            } else {
                                haltFront = true;
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
                        System.out.println("R:"+r);
                        if (theGrid[i][r + 1].getTempRow() == -1) {
                            theGrid[i][r + 1].setTempPosition("AISLE");
                            theGrid[i][r + 1].setTempRow(r + (movedistance - (getSeatValue("AISLE") - i)));
                            theGrid[i][r + 1].setSeated(false);
                            isSeatCount--;
                            System.out.println("TempPostion for " + theGrid[i][r + 1] + " is " + theGrid[i][r + 1].getTempRow() + theGrid[i][r + 1].getTempPosition());
                            if (first) {
                                if (r > 0) {
                                    Point newpt = new Point(getSeatValue("AISLE"), r+1);
                                    if(!blocks.contains(newpt)) {
                                        blocks.add(newpt);
                                        System.out.println("New block: " + getSeatValue("AISLE") + " " + (r + 1));
                                        System.out.println("The block pair for " + theGrid[p][r - 1] + " is [" + i + "," + (r + 1) + "] " + "(" + theGrid[i][r + 1] + ")");
                                        break;
                                    } else {
                                        System.out.println("Other");
                                        //TODO:Same row block
                                    }
                                }
                            } else {
                                haltFront = true;
                            }
                                } else {
                                    haltFront = true;
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
        this.setBoardingMethod(null);
        this.setCapacity(-1);
        this.setDelay(2);
        this.theGrid = null;
        modelInteration = 0;
        setChanged();
        notifyObservers();
    }

    // BELOW IS FOR TIMER

    public int getDelay() {
        return delay;
    }

    public void setDelay(int value) {
        int[] delayValues = {2000, 1000, 12, 250, 12};
        this.delay = delayValues[value];
    }

    public boolean isInProcess() {
        return inProcess;
    }

    public void setInProcess(boolean inProcess) {
        this.inProcess = inProcess;
    }

    public int getModelInteration() {
        return modelInteration;
    }

    public void setIsSeatCount(int isSeatCount) {
        this.isSeatCount = isSeatCount;
    }

    public int getIsSeatCount() {
        return isSeatCount;
    }

    public int getTotalPax() {
        return totalPax;
    }
}