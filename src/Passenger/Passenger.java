package Passenger;

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
    char position;

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
    public void setPosition(char position) {
        this.position = position;
    }

    public char getPosition() {
        return position;
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
