package Exceptions;

import javax.swing.*;

/**
 * Created by Oscar on 2016-02-04.
 */
public class DeadlockException extends Exception {
    public DeadlockException() {
        JOptionPane.showMessageDialog(null, "We're in an unsolvable deadlock - the boarding will have to be restarted.",
                "Logic Error",
                JOptionPane.ERROR_MESSAGE);
    }
}