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
 * The class containing the list of passengers as it is imported from the XML file
 */
@XmlRootElement(name="Passengers")
@XmlAccessorType(XmlAccessType.FIELD)
public class Passengers {

    @XmlElement(name="Passenger", type=Passenger.class)
    private List<Passenger> passengers = new ArrayList<>();

    public Passengers(){}

    public Passengers(List<Passenger> passengers){
        this.passengers = passengers;
    }

    public List<Passenger> getPassengers(){
        return passengers;
    }

    public void setPassengers(List<Passenger> passengers){
        this.passengers = passengers;
    }

}