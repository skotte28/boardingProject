package Panels;

import MVCFramework.BoardingModel;
import Passenger.Passenger;
import Simulation.AircraftGrid;

import javax.swing.*;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;

/**
 * Created by Oscar on 2015-11-26.
 */
public class QueuePanel extends JPanel implements Observer {

    BoardingModel boardingModel;
    private int remaining;

    public QueuePanel(BoardingModel boardingModel) {
        this.boardingModel = boardingModel;
        this.setBackground(Color.LIGHT_GRAY);
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        int windowW = getWidth();
        int windowH = getHeight();

        System.out.println(windowW);
        int circleWH = (int) Math.round((5/360.0)*windowW);
        int circleSpacing = (int) Math.round((1/360.0)*windowW);
        System.out.println(circleWH);

        int count = 0;
        if(boardingModel.getPassengers() != null) {
            for (int positionY = circleSpacing; positionY < windowH; positionY = positionY + circleWH + circleSpacing) {
                for (int positionX = circleSpacing; positionX < windowW && count < remaining; positionX = positionX + circleWH + circleSpacing) {
                    g.setColor(Color.BLUE);
                    g.fillOval(positionX, positionY, circleWH, circleWH);
                    g.setColor(Color.WHITE);
                    g.drawString(boardingModel.getPassengers().get(count).toString(), positionX + (circleWH / 8), positionY+(4*(circleWH/5)));
                    count++;
                }
            }
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        if(boardingModel.getPassengers() != null) {
            remaining = boardingModel.getPassengers().size();
            System.out.println("Remaining passengers: " + remaining);
            repaint();
        }
    }
}
