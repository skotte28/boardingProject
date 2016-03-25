package Exceptions;

import javax.swing.*;

/**
 *
 * This exception is thrown when the naming of a folder containing aircraft specification files is incorrect, or inconsistent with the contents of the files.
 *
 * @author Oscar Schafer
 *
 * @see Aircraft.AircraftType
 *
 */
public class IncorrectAircraftException extends Exception {
    public IncorrectAircraftException(String errorObject) {
        JOptionPane.showMessageDialog(null, "One of the aircraft in the content folder could not be read. Check so that the naming and Layout is consistent. See readme file for further information. Error related to:"+errorObject,"Aircraft Error",
                JOptionPane.ERROR_MESSAGE);
    }
}
