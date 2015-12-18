package Passenger;

import Aircraft.AircraftType;

import javax.xml.bind.JAXB;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.List;

/**
 * Created by Oscar on 2015-11-25.
 */
public class JAXBHandler {

    public static List<Passenger> unmarshallPassenger(File file) {

        try {
            Passengers passengers = new Passengers();
            JAXBContext jaxbContext = JAXBContext.newInstance(Passengers.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

            passengers = (Passengers) jaxbUnmarshaller.unmarshal(file);

            System.out.println(passengers.toString());

            return passengers.getPassengers();

        } catch (JAXBException je) {
            System.out.println(/*"There was a problem getting the passenger XML list."+*/je);
            return null;
        }
    }

    public static AircraftType unmarshallLayout(File file){

        //TODO: This shouldn't be in Passenger package

        try{
            AircraftType aircraftType;
            JAXBContext jaxbContext = JAXBContext.newInstance(AircraftType.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

            aircraftType = (AircraftType) jaxbUnmarshaller.unmarshal(file);

            System.out.println(aircraftType.toString());

            return aircraftType;

        } catch (JAXBException je){
            System.out.println(/*"There was a problem getting the aircraft layout from XML."+*/je);
            return null;
        }
    }
}
