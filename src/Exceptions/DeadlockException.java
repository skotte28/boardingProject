package Exceptions;

import javax.swing.*;

/**
 *
 * This exception is thrown when the simulation engine has identified a deadlock.
 *
 * @author Oscar Schafer
 *
 */
public class DeadlockException extends Exception {
    public DeadlockException() {
        JOptionPane.showMessageDialog(null, "We're in an unsolvable deadlock - the boarding will have to be restarted.",
                "Logic Error",
                JOptionPane.ERROR_MESSAGE);
    }
}