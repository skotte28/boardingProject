package Exceptions;

import javax.swing.*;

/**
 * Created by Oscar on 2016-02-04.
 */
public class NoSelectedException extends Exception {
    public NoSelectedException(String missing) {
        JOptionPane.showMessageDialog(null, "There is no " + missing +" selected","No "+missing,
                JOptionPane.ERROR_MESSAGE);
    }
}
