package Panels;

import MVCFramework.BoardingModel;

import javax.swing.*;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by Oscar on 2015-11-26.
 */
public class QueuePanel extends JPanel implements Observer {

    private BoardingModel boardingModel;
    private int remaining;

    public QueuePanel(BoardingModel boardingModel) {
        this.boardingModel = boardingModel;
        this.setBackground(Color.WHITE);
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        Font paxFont = g2d.getFont().deriveFont( 9.0f );
        g2d.setFont(paxFont);

        int windowW = getWidth();
        int windowH = getHeight();

        int circleWH = 25;
        int circleSpacing = (int) Math.round((1/360.0)*windowW);


        int count = 0;
        if(boardingModel.getPassengers() != null) {
            for (int positionY = circleSpacing*4; positionY < windowH; positionY = positionY + circleWH + circleSpacing) {  //circleSpacing is *4 to allow space for panel title
                for (int positionX = circleSpacing*2; positionX < windowW-circleWH && count < remaining; positionX = positionX + circleWH + circleSpacing) {
                    g.setColor(Color.ORANGE);
                    g.fillOval(positionX, positionY, circleWH, circleWH);
                    g.setColor(Color.BLACK);
                    g.drawString(boardingModel.getPassengers().get(count).toString(), positionX + (circleWH / 4), positionY+(2*(circleWH/3)));
                    count++;
                }
            }
        }
    }

    public void clear(){
        remaining = 0;
        repaint();
    }

    @Override
    public void update(Observable o, Object arg) {
        if(boardingModel.getPassengers() != null) {
            remaining = boardingModel.getPassengers().size();
            repaint();
        }
    }
}
