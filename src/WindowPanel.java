import javax.swing.*;
import java.awt.*;

/**
 * Created by Oscar on 2015-10-15.
 */
public class WindowPanel extends JPanel{

    private static final int CELL_PIXEL = 5;
    private static final int PUZZLE_SIZE = 200;
   // private static final int SUBSQUARE = 5;
    private static final int BOARD_PIXELS = CELL_PIXEL * PUZZLE_SIZE;
    private static final int TEXT_OFFSET = 15;

    WindowPanel() {
        this.setSize(300, 600);
        this.setBackground(Color.blue);
        this.setOpaque(true);
    }

    @Override public void paintComponent(Graphics g){
        g.setColor(getBackground());
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setColor(Color.BLACK);

        drawGridLines(g);
    }

    private void drawGridLines(Graphics g){
        for(int i = 0; i <= PUZZLE_SIZE; i++){
            int acrossOrDown = i * CELL_PIXEL;
            g.drawLine(acrossOrDown, 0, acrossOrDown, BOARD_PIXELS);
            g.drawLine(0, acrossOrDown, BOARD_PIXELS, acrossOrDown);

           /* if(i % SUBSQUARE == 0){
                acrossOrDown++;
                g.drawLine(acrossOrDown, 0, acrossOrDown, BOARD_PIXELS);
                g.drawLine(0, acrossOrDown, BOARD_PIXELS, acrossOrDown);
            }*/
        }
    }

    public void setMeRed(){
        this.setBackground(Color.red);
    }

}
