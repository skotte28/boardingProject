package XMLParsing;

import Passenger.*;

import javax.swing.*;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.List;

/**
 * This class unmarshalls the XML passenger list file.
 */
public class JAXBHandlerPassenger {

    public static List<Passenger> unmarshal(File file) {

        try {
            Passengers passengers = new Passengers();
            JAXBContext jaxbContext = JAXBContext.newInstance(Passengers.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

            passengers = (Passengers) jaxbUnmarshaller.unmarshal(file);

            //System.out.println(passengers.toString()); - enable for testing purposes

            return passengers.getPassengers();

        } catch (JAXBException je) {
            JOptionPane.showMessageDialog(null, "There was a problem getting the passenger XML list, please make sure it is correct and restart the application.","JAXB Passenger Error",
                    JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }
}
