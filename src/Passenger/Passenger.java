package Passenger;

import Aircraft.Position;
import Simulation.Direction;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by Oscar on 2015-11-25.
 */
@XmlRootElement(name="Passenger")
public class Passenger {

    int seatNumber;
    String seatName;
    ArrayList route;
    int row;
    Position position;
    Direction nextMove;
    int handluggage;

    Position currentPosition;
    int currentRow;
    boolean seated;

    public int getSeatNumber() {
        return seatNumber;
    }

    @XmlElement (name="seatNumber")
    public void setSeatNumber(int seatNumber) {
        this.seatNumber = seatNumber;
    }

    public String getSeatName() {
        return seatName;
    }

    @XmlElement (name="seatName")
    public void setSeatName(String seatName) {
        this.seatName = seatName;
    }

    public ArrayList getRoute() {
        return route;
    }

    @XmlElement (name="route")
    public void setRoute(ArrayList route) {
        this.route = route;
    }

    @XmlElement (name="row")
    public void setRow(int row) {
        this.row = row;
    }

    public int getRow() {
        return row;
    }

    @XmlElement (name="position")
    public void setPosition(Position position) {
        this.position = position;
    }

    public Position getPosition() {
        return position;
    }

    public void setNextMove(Direction nextMove){
        this.nextMove = nextMove;
    }

    public Direction getNextMove(){
        return nextMove;
    }

    public int getHandluggage() {
        return handluggage;
    }

    public void setHandluggage(int handluggage) {
        this.handluggage = handluggage;
    }

    public Position getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(Position currentPosition) {
        this.currentPosition = currentPosition;
    }

    public int getCurrentRow() {
        return currentRow;
    }

    public void setCurrentRow(int currentRow) {
        this.currentRow = currentRow;
    }

    public boolean isSeated() {
        return seated;
    }

    public void setSeated(boolean seated) {
        this.seated = seated;
    }

    public void passengerUnwrapper(){

        File file = new File("src/A320");
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(Passenger.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

            Passenger passenger = (Passenger) jaxbUnmarshaller.unmarshal(file);

            //for(Passenger pax : passengers.getPassengers()){
              //  System.out.println(pax.getSeatName());
              //  System.out.println(pax.getSeatNumber());
            //}

            System.out.println("Seatname:"+passenger.seatName);

        }
        catch (JAXBException je){
            System.out.println(/*"There was a problem getting the passenger XML list."+*/je);
        }
    }

    @Override
    public String toString(){
        return seatName;
    }

}
