package Aircraft;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.*;

/**
 * This class consists of the aircraft properties.
 *
 * @author Oscar Schafer
 *
 */

@XmlRootElement(name="AircraftType")
public class AircraftType {

    private String name;

    private int rows;
    private int width;
    private int aisle;
    private int buffer;

    private ArrayList<String> windowSeats = new ArrayList<>();
    private ArrayList<String> middleSeats = new ArrayList<>();
    private ArrayList<String> aisleSeats = new ArrayList<>();

    /**
     * Class constructor
     */
    public AircraftType(){}

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

    public void addWindowSeat(String position){
        windowSeats.add(position);
    }

    public void addMiddleSeat(String position){
        middleSeats.add(position);
    }

    public void addAisleSeat(String position){
        aisleSeats.add(position);
    }

    public ArrayList<String> getWindowSeats() {
        return windowSeats;
    }

    public ArrayList<String> getMiddleSeats() {
        return middleSeats;
    }

    public ArrayList<String> getAisleSeats() {
        return aisleSeats;
    }

}

