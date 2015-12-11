package Simulation;

import Aircraft.AircraftType;
import Passenger.Passenger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Oscar on 2015-12-01.
 */
public class AircraftGrid {

    public Passenger[][] theGrid;

    public AircraftGrid(AircraftType aircraftType, List<Passenger> passengers){
        System.out.println("Entered aircraftgrid");
        int width = aircraftType.getWidth();
        //Initiate the underlying aircraft grid
        theGrid = new Passenger[width+aircraftType.getAisle()][aircraftType.getRows()];
        System.out.println(width+aircraftType.getAisle());
        System.out.println(aircraftType.getRows());

        gridBoarder(theGrid, passengers, width);
    }

    protected void gridBoarder(Passenger[][] theGrid, List<Passenger> passengers, int width){
        while((!passengers.isEmpty()) || aisleFree(theGrid, (width/2)+1)){  //Do we still need to continue?
            System.out.println(theGrid);
            System.out.println("Passengers: " + passengers.toString());
            for(int j = theGrid.length-2; j > 0; j--){             //Start from back
                if(isFree(theGrid, j, (width/2)+1)){                      //Check if square is free,
                    System.out.println("Free one");
                    if(!isFree(theGrid, j-1, (width/2)+1)){               //If so, check if there's something to move
                        moveTowardsBack(theGrid, j, (width/2)+1);}        //If there is, then move it
                    else {
                        System.out.println("Nothing");
                    }
                }
            }
            System.out.println("Post-grid:"+theGrid);
            if(theGrid[width][0] == null) {
                System.out.println("Free space!");
                if (!passengers.isEmpty()) {
                    theGrid[(width / 2) + 1][0] = passengers.remove(0);
                    System.out.println(theGrid[(width / 2) + 1][0]);
                }  //If there is space for a new passenger bring them onboard
            }
            else {
                System.out.println("No free space!");
            }
        }
        System.out.println("Stopped looping");

    }



    private boolean isFree(Passenger[][] theGrid, int i, int r){
        System.out.println("i: "+i);
        System.out.println("r: "+r);
        System.out.println(i+"and"+r+" contains "+theGrid[i][r]);
        return theGrid[i][r] == null;
    }

    private boolean aisleFree(Passenger[][] theGrid, int width){
        for(int j = 0; j < theGrid.length; j++){
            System.out.println(theGrid[j][width]);
            if(theGrid[j][width] != null){
                System.out.println("Aisle is busy!");
                return false;
            }
        }
        System.out.println("Aisle is free!");
        return true;
    }

    private void moveTowardsBack(Passenger[][] theGrid, int i, int r){
        theGrid[i][r+1] = theGrid[i][r];
        System.out.println("r+1: "+theGrid[i][r+1]);
        theGrid[i][r] = null;
        System.out.println("Moved backwards");
    }

    private void moveTowardsFront(Passenger[][] theGrid, int i, int r){
        theGrid[i][r+1] = theGrid[i][r];
        theGrid[i][r] = null;
    }

    private void moveStarbord(Passenger[][] theGrid, int i, int r){
        theGrid[i+1][r] = theGrid[i][r];
        theGrid[i][r] = null;
    }

    private void movePort(Passenger[][] theGrid, int i, int r){
        theGrid[i-1][r] = theGrid[i][r];
        theGrid[i][r] = null;
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