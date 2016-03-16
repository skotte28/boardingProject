package Exceptions;

import javax.swing.*;

/**
 * Created by Oscar on 2016-02-04.
 */
public class MissingAircraftException extends Exception {
    public MissingAircraftException() {
        JOptionPane.showMessageDialog(null, "There are no aircraft folders in the content folder","No aircraft",
                JOptionPane.ERROR_MESSAGE);
    }
}
