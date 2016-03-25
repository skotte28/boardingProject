package Exceptions;

import javax.swing.*;

/**
 * Created by Oscar on 2016-02-04.
 */
public class NoSelectionException extends Exception {
    public NoSelectionException(String missing) {
        JOptionPane.showMessageDialog(null, "There is no " + missing +" selected","No "+missing,
                JOptionPane.ERROR_MESSAGE);
    }
}
