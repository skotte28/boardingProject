package Exceptions;

import javax.swing.*;

/**
 *
 * This exception is thrown if there are no aircraft in the contents directory.
 *
 * @author Oscar Schafer
 *
 */
public class MissingAircraftException extends Exception {
    public MissingAircraftException() {
        JOptionPane.showMessageDialog(null, "There are no aircraft folders in the content folder","No aircraft",
                JOptionPane.ERROR_MESSAGE);
    }
}
