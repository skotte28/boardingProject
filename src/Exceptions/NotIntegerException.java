package Exceptions;

import javax.swing.*;

/**
 *
 * This exception is thrown when the user has entered an invalid input as occupancy.
 *
 * @author Oscar Schafer
 *
 */
public class NotIntegerException extends Exception {
    public NotIntegerException(String message) {
        super(message);
        JOptionPane.showMessageDialog(null, "Please make sure that the capacity is in the range 1-100.","Capacity Problem",
                JOptionPane.WARNING_MESSAGE);
    }
}
