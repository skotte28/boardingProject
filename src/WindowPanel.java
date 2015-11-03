import javax.swing.*;
import java.awt.*;

/**
 * Created by Oscar on 2015-10-15.
 */
public class WindowPanel extends JPanel{

    WindowPanel() {
        this.setSize(300, 600);
        this.setBackground(Color.blue);
        this.setOpaque(true);
    }

    public void setMeRed(){
        this.setBackground(Color.red);
    }

}
