package Simulation;

import Aircraft.AircraftType;
import Passenger.Passenger;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Oscar on 2015-12-01.
 */
public class AircraftGrid {

    public Passenger[][] theGrid;

    public AircraftGrid(AircraftType aircraftType, List<Passenger> passengers) {
        System.out.println("Entered aircraftgrid");
        int width = aircraftType.getWidth();
        //Initiate the underlying aircraft grid
        theGrid = new Passenger[width + aircraftType.getAisle()][aircraftType.getRows()];

        gridBoarder(theGrid, passengers, width);
    }

    protected void gridBoarder(Passenger[][] theGrid, List<Passenger> passengers, int width) {
        while ((!passengers.isEmpty()) || !aisleFree(theGrid, (width + 1)/2)) {  //Do we still need to continue?
            System.out.println("Passengers: " + passengers.toString());
            for (int p = theGrid.length - 2; p >= 0; p--) {             //Start from back
                for (int r = theGrid[p].length - 1; r >= 0; r--) {

                    /*Section Outline:
                        getNextMove
                        check if possible
                        execute
                        set next move
                     */

                    //Get the next move
                    if (theGrid[p][r] != null) {
                        if(theGrid[p][r].getPosition().getPositionValue() != p && theGrid[p][r].getRow() != r+1){
                        Direction nextMove = theGrid[p][r].getNextMove();
                        Passenger currentPax = theGrid[p][r];
                        System.out.println(currentPax);
                        //Check if possible & execute
                        switch (nextMove) {
                            case FRONTWARDS:
                                if (isFree(theGrid, p, r - 1)) {
                                    moveTowardsFront(theGrid, p, r);
                                }
                                break;
                            case REARWARDS:
                                if (isFree(theGrid, p, r + 1)) {
                                    moveTowardsBack(theGrid, p, r);
                                }
                                break;
                            case PORT:
                                if (isFree(theGrid, p - 1, r)) {
                                    movePort(theGrid, p, r);
                                }
                                break;
                            case STARBOARD:
                                if (isFree(theGrid, p + 1, r)) {
                                    moveStarboard(theGrid, p, r);
                                }
                                break;
                        }
                        //Set the next move

                        //On the correct row
                        if (currentPax.getNextMove() != Direction.SEATED) {
                            if (!seatedCheck(currentPax, r, p)) {
                                nextMoveSetter(currentPax, r, p);
                            }
                        }
                    }
                }
                }
            }
            //Check if there are still passengers waiting
            if (!passengers.isEmpty()) {

                //If there is space for a new passenger bring them onboard
                if (theGrid[(width + 1)/ 2][0] == null) {
                    System.out.println("Free space!");
                    theGrid[(width + 1)/ 2][0] = passengers.remove(0);
                    nextMoveSetter(theGrid[(width + 1)/ 2][0], 0, ((width + 1)/ 2));
                } else {
                    System.out.println("No free space!");
                }
            }
            System.out.println("Post-grid:" + Arrays.deepToString(theGrid));
        }
        System.out.println("Stopped looping");

    }


    private boolean isFree(Passenger[][] theGrid, int p, int r) {
        return theGrid[p][r] == null;
    }

    private boolean aisleFree(Passenger[][] theGrid, int aisle) {
        for (int i = 0; i < theGrid.length; i++) {
            System.out.println("Aisle position: "+aisle);
            System.out.println("Row:"+i);
            System.out.println(theGrid[aisle][i]);
            if (theGrid[aisle][i] != null) {
                //Aisle is busy
                return false;
            }
        }
        //Aisle is free
        return true;
    }

    private void moveTowardsBack(Passenger[][] theGrid, int p, int r) {
        theGrid[p][r + 1] = theGrid[p][r];
        theGrid[p][r] = null;
    }

    private void moveTowardsFront(Passenger[][] theGrid, int p, int r) {
        theGrid[p][r + 1] = theGrid[p][r];
        theGrid[p][r] = null;
    }

    private void moveStarboard(Passenger[][] theGrid, int p, int r) {
        theGrid[p + 1][r] = theGrid[p][r];
        theGrid[p][r] = null;
    }

    private void movePort(Passenger[][] theGrid, int p, int r) {
        theGrid[p - 1][r] = theGrid[p][r];
        theGrid[p][r] = null;
    }

    private boolean seatedCheck(Passenger currentPax, int r, int p){
        if (currentPax.getPosition().getPositionValue() == p-1) {
            currentPax.setNextMove(Direction.SEATED);
            return true;
        } else if (currentPax.getPosition().getPositionValue() == p+1){
            currentPax.setNextMove(Direction.SEATED);
            return true;
        }
        return false;
    }

    private void nextMoveSetter(Passenger currentPax, int r, int p){
    if(currentPax.getRow()==r+1){
        //Too far port side
        if (currentPax.getPosition().getPositionValue() > p) {
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

    private void oneInfront(){
        //current position is one row in-front of the correct row
        //initiate the row check protocol

    }

    private static void update(){

        //Takes all the updatable items
        //Shifts them simultaneously?

    }



}