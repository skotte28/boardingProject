package XMLParsing;

import Aircraft.AircraftType;
import Aircraft.Position;
import Passenger.*;

import javax.xml.bind.JAXB;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.List;

/**
 * Created by Oscar on 2015-11-25.
 */
public class JAXBHandlerPassenger {

    public static List<Passenger> unmarshal(File file) {

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
}
