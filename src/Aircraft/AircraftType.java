package Aircraft;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;


@XmlRootElement(name="AircraftType")
public class AircraftType {

    private String name;

    private int doors;

    private int rows;
    private int width;
    private int aisle;
    private int buffer;

    private ArrayList<String> windowSeats = new ArrayList<>();
    private ArrayList<String> middleSeats = new ArrayList<>();
    private ArrayList<String> aisleSeats = new ArrayList<>();

    public ArrayList<String> getWindowSeats() {
        return windowSeats;
    }

    public ArrayList<String> getMiddleSeats() {
        return middleSeats;
    }

    public ArrayList<String> getAisleSeats() {
        return aisleSeats;
    }

    public AircraftType(){}

    public AircraftType(String name){
       this.name = name;
    }

    public String getName() {
        return name;
    }

    @XmlElement(name="name")
    public void setName(String name) {
        this.name = name;
    }

    public int getWidth() {
        return width;
    }

    @XmlElement(name="width")
    public void setWidth(int width) {
        this.width = width;
    }

    public int getBuffer() {
        return buffer;
    }

    @XmlElement(name="buffer")
    public void setBuffer(int buffer) {
        this.buffer = buffer;
    }

    public int getDoors() {
        return doors;
    }

    public int getRows() {
        return rows;
    }

    @XmlElement(name="rows")
    public void setRows(int rows) {
        this.rows = rows;
    }

    public int getAisle() {
        return aisle;
    }

    @XmlElement(name="aisle")
    public void setAisle(int aisle) {
        this.aisle = aisle;
    }

    @Override
    public String toString(){
        return name;
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

    public void addWindowSeat(String position){
        windowSeats.add(position);
    }

    public void addMiddleSeat(String position){
        middleSeats.add(position);
    }

    public void addAisleSeat(String position){
        aisleSeats.add(position);
    }

}

