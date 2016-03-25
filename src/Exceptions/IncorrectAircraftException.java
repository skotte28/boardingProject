package Exceptions;

import javax.swing.*;

/**
 * Created by Oscar on 2016-02-04.
 */
public class IncorrectAircraftException extends Exception {
    public IncorrectAircraftException(String errorObject) {
        JOptionPane.showMessageDialog(null, "One of the aircraft in the content folder could not be read. Check so that the naming and Layout is consistent. See readme file for further information. Error related to:"+errorObject,"Aircraft Error",
                JOptionPane.ERROR_MESSAGE);
    }
}
