package Exceptions;

import javax.swing.*;

/**
 *
 * This exception is raised when the user has not made a selection required for the simulation to run.
 * Normally this will be either the aircraft type or the boarding method.
 *
 * @author Oscar Schafer
 *
 */
public class NoSelectionException extends Exception {
    public NoSelectionException(String missing) {
        JOptionPane.showMessageDialog(null, "There is no " + missing +" selected","No "+missing,
                JOptionPane.ERROR_MESSAGE);
    }
}
