package Passenger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Oscar on 2015-11-26.
 */
@XmlRootElement(name="Passengers")
@XmlAccessorType(XmlAccessType.FIELD)
public class Passengers {

    @XmlElement(name="Passenger", type=Passenger.class)
    private List<Passenger> passengers = new ArrayList<Passenger>();

    public Passengers(){};

    public Passengers(List<Passenger> passengers){
        this.passengers = passengers;
    }

    public List<Passenger> getPassengers(){
        return passengers;
    }

    public void setPassengers(List<Passenger> passengers){
        this.passengers = passengers;
    }

    public void passengerUnmarshaller(){

        File file = new File("src/A320");
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(Passengers.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

            Passengers passengers = (Passengers) jaxbUnmarshaller.unmarshal(file);

            for(Passenger pax : getPassengers()){
                System.out.println(pax.getSeatName());
                System.out.println(pax.getSeatNumber());
            }

        }
        catch (JAXBException je){
            System.out.println(/*"There was a problem getting the passenger XML list."+*/je);
        }
    }

}
