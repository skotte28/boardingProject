package Aircraft;
import Aircraft.Layout;

import java.awt.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.List;


/**
 * Created by Oscar on 2015-10-15.
 */
public class AircraftType {
    //Long id;
    //private ArrayList capacity;
    private String name;
    private int doors;
    //private Layout layout;

    private int rows;
    private int width;
    private int aisle;

    public AircraftType(String name){
       this.name = name;
       width = 4;
       aisle = 1;
       rows = 8;
    }

    public int getDoors() {
        return doors;
    }

    /* Remove static after tests */

    static public java.util.List<Point> getLayout(AircraftType aircraftType){
        List<Point> fixedInterior = new ArrayList<>();
        String line = null;
        String name = aircraftType.toString();
        try {
        /* Initialize the fixedInterior array */

        /* Import the file containing the points */
            FileReader fr = new FileReader("src/"+name+".txt");
            BufferedReader layoutReader = new BufferedReader(fr);
            while ((line = layoutReader.readLine()) != null) {
                String[] input = line.split(",");
                if(input.length != 2){
                    throw new IOException();
                }
                fixedInterior.add(new Point(Integer.parseInt(input[0]), Integer.parseInt(input[1])));
            }

            System.out.println(fixedInterior);
            layoutReader.close();
        }

        catch(FileNotFoundException ex){
            System.out.println("File could not be found!");
        }

        catch(IOException ex){
            System.out.println("There was a problem reading the file!");
        }

        /* Add the points to the fixedInterior */

        /* Return the fixedInterior */
        return fixedInterior;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getAisle() {
        return aisle;
    }

    public void setAisle(int aisle) {
        this.aisle = aisle;
    }

    @Override
    public String toString(){
        return name;
    }
}