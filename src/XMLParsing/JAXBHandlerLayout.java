package XMLParsing;

import Aircraft.AircraftType;

import javax.swing.*;
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
            JOptionPane.showMessageDialog(null, "There was a problem getting the layout XML list, please make sure it is correct and restart the application.","JAXB Layout Error",
                    JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }
}
