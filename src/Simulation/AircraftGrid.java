package Simulation;

import Aircraft.AircraftType;
import Aircraft.Position;
import Passenger.Passenger;
import javafx.animation.Animation;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Oscar on 2015-12-01.
 */
public class AircraftGrid {

    public static Passenger[][] theGrid;

    public AircraftGrid(AircraftType aircraftType, List<Passenger> passengers) {

        int width = aircraftType.getWidth();
        int aisle = aircraftType.getAisle();

        //Initiate the underlying aircraft grid
        theGrid = new Passenger[width + aisle][aircraftType.getRows()];

        gridBoarder(passengers, width);
    }

    protected void gridBoarder(List<Passenger> passengers, int width) {

        //Check if all passengers are in the correct seat
        while ((!passengers.isEmpty()) || !allSeated()) {

            //TODO: Remove print statement - for testing purposes
            System.out.println("Passengers: " + passengers.toString());

            for (int p = theGrid.length - 1; p >= 0; p--) {             //Start from position 'A'
                for (int r = theGrid[p].length - 1; r >= 0; r--) {      //Start from last row


                    //Is there a passenger in the cell?
                    if (theGrid[p][r] != null) {

                        //Is the passenger seated?
                        if (!theGrid[p][r].isSeated()) {

                            /*if(theGrid[p][r].getRow() == r){
                                if(blockChecker(theGrid[p][r],p,r)){

                                }
                            }*/

                            nextMoveSetter(theGrid[p][r], r, p);

                            Direction nextMove = theGrid[p][r].getNextMove();
                            Passenger currentPax = theGrid[p][r];

                            //TODO: Remove print statement - for testing purposes
                            System.out.println(currentPax);

                            //Check if possible & execute
                            switch (nextMove) {
                                case FRONTWARDS:
                                    if (isFree(p, r-1)) {
                                        move(p, r, p, r-1);
                                    }
                                    break;
                                case REARWARDS:
                                    if (isFree(p, r+1)) {
                                        move(p, r, p, r+1);
                                    }
                                    break;
                                case PORT:
                                   if (isFree(p-1, r)) {
                                       move(p, r, p-1, r);
                                    }
                                    break;
                                case STARBOARD:
                                    if (isFree(p+1, r)) {
                                        move(p, r, p+1, r);
                                    }
                                    break;
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

            //TODO: Remove print statement - for testing purposes
            System.out.println("Post-grid:" + Arrays.deepToString(theGrid));
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
    }

    private void nextMoveSetter(Passenger currentPax, int r, int p){
        //Correct row
        /*if(currentPax.getRow() == r){
            currentPax.setNextMove(Direction.ONEFRONT);
        }

        else */ if(currentPax.getRow()==r+1){
            if (currentPax.getPosition().getPositionValue() == p){
                currentPax.setSeated(true);
                currentPax.setNextMove(Direction.SEATED);
            }
            //Too far port side
            else if (currentPax.getPosition().getPositionValue() > p) {
                currentPax.setNextMove(Direction.STARBOARD);
            }
            //Too far starboard side
            else if (currentPax.getPosition().getPositionValue() < p) {
                currentPax.setNextMove(Direction.PORT);
            }
        }
        //Too far forward
        else if(currentPax.getRow()>r+1){
            currentPax.setNextMove(Direction.REARWARDS);
        }
        //Too far back
        else if(currentPax.getRow()<r+1){
            currentPax.setNextMove(Direction.FRONTWARDS);
        }
    }

    private boolean blockChecker(Passenger passenger, int p, int r){
        if(passenger.getPosition() == Position.A){
            if(!isFree(Position.B.getPositionValue(),r)){
                return true;
            }
        }
        else if(passenger.getPosition() == Position.D){
            if(!isFree(Position.B.getPositionValue(),r)){
                return true;
            }
        }
        return false;
    }

    private void blockSeater(int p, int r, int aisle, int width){
        int movedistance = 1;
        //New Position Assigner

        if(theGrid[p][r].getPosition().getPositionValue() > aisle){
            for(int i = width-1; i > aisle; i--){
                if(theGrid[i][r] != null){
                    movedistance++;
                }
            }
            for(int i = width-1; i > aisle; i--){
                if(theGrid[i][r] != null){
                    theGrid[i][r].setTempPosition(Position.AISLE);
                    theGrid[i][r].setTempRow(r+movedistance);
                }
            }

        }

        if(theGrid[p][r].getPosition().getPositionValue() < aisle){
            for(int i = 0; i > aisle; i++){
                if(theGrid[i][r] != null){
                    movedistance++;
                }
            }
            for(int i = 0; i > aisle; i++){
                if(theGrid[i][r] != null){
                    theGrid[i][r].setTempPosition(Position.AISLE);
                    theGrid[i][r].setTempRow(r+movedistance);
                }
            }
        }
        //move each piece
        //invalid temps
        //continue as usual?
    }
}