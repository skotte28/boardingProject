package Exceptions;

import javax.swing.*;

/**
 * Created by Oscar on 2016-02-04.
 */
public class NotIntegerException extends Exception {
    public NotIntegerException(String message) {
        super(message);
        JOptionPane.showMessageDialog(null, "Please make sure that the capacity is an integer between 0 and 100","Capacity Problem",
                JOptionPane.WARNING_MESSAGE);
    }
}
