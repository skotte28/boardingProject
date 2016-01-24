package XMLParsing;

import Aircraft.AircraftType;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;

/**
 * Created by Oscar on 2015-12-19.
 */
public class JAXBHandlerLayout {

    public static AircraftType unmarshal(File file){

        try{
            AircraftType aircraftType;
            JAXBContext jaxbContext = JAXBContext.newInstance(AircraftType.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

            aircraftType = (AircraftType) jaxbUnmarshaller.unmarshal(file);

            return aircraftType;

        } catch (JAXBException je){
            System.out.println(/*"There was a problem getting the aircraft layout from XML."+*/je);
            return null;
        }
    }
}
