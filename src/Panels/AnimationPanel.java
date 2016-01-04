package Panels;

import Aircraft.AircraftType;
import MVCFramework.BoardingModel;
import Passenger.Passenger;
import Simulation.AircraftGrid;
import com.sun.org.apache.xpath.internal.SourceTree;
import org.w3c.dom.css.Rect;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class AnimationPanel extends JPanel implements Observer {

    private int CELL_DIMENSION = 8; //Each square is 25cm
    private int columnCount = 140;  //Total length 28m
    private int rowCount = 18;      //Total width 3.5m

    private List<Rectangle> cells;
    private List<Point> selectedCell;
    private List<Point> layoutCells;
    private List<Rectangle> seatCells;
    protected List<Point> passengers;

    //TODO: Should there really need to be two instances of theGrid
    protected Passenger[][] animationGrid;

    public void setLayoutCells(List<Point> layoutCells) {
        this.layoutCells = layoutCells;
    }

    private BoardingModel boardingModel;

    public AnimationPanel(BoardingModel boardingModel) {

        this.boardingModel = boardingModel;
        //this.setSize(400, 400);

        cells = new ArrayList<>(columnCount * rowCount);
        selectedCell = new ArrayList<Point>();
        layoutCells = new ArrayList<Point>();
        seatCells = new ArrayList<Rectangle>();
        passengers = new ArrayList<Point>();

        if(AircraftGrid.theGrid != null) {
            for (Passenger[] position : AircraftGrid.theGrid) {
                //Had to make theGrid static... Could maybe pass the simulation as argument to Panels.AnimationPanel
                for (Passenger pax : position) {
                    if (pax != null) {
                        passengers.add(new Point(pax.getPosition().getPositionValue(), pax.getRow()));
                    }
                }
            }
        }

        //TODO: Remove; color for testing purposes
        this.setBackground(Color.ORANGE);

        //layoutCells = MVCFramework.BoardingModel.getLayout(selectedAircraft);

        MouseAdapter mouseHandler;
        mouseHandler = new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                //Point point = e.getPoint();

                int width = getWidth();
                int height = getHeight();

                int xOffset = (width - (columnCount * CELL_DIMENSION)) / 2;
                int yOffset = (height - (rowCount * CELL_DIMENSION)) / 2;

                //selectedCell = null;
                if (e.getX() >= xOffset && e.getY() >= yOffset) {

                    int column = (e.getX() - xOffset) / CELL_DIMENSION;
                    int row = (e.getY() - yOffset) / CELL_DIMENSION;

                    if (column >= 0 && row >= 0 && column < columnCount && row < rowCount) {
                        System.out.println(column);
                        System.out.println(row);
                        Point addPoint = new Point(column, row);
                        if(!layoutCells.contains(addPoint)) {
                            if (selectedCell.contains(addPoint)) {
                                selectedCell.remove(addPoint);
                            } else {
                                selectedCell.add(addPoint);
                            }
                        }
                        System.out.println(selectedCell);

                    }

                }
                repaint();
                runThrougher(cells);

            }
        };
        //addMouseListener(mouseHandler);
        addMouseMotionListener(mouseHandler);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(200, 200);
    }

    @Override
    public void invalidate() {
        cells.clear();
        //selectedCell = null;
        super.invalidate();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();

        int width = getWidth();
        int height = getHeight();

        int xOffset = (width - (columnCount * CELL_DIMENSION)) / 2;
        int yOffset = (height - (rowCount * CELL_DIMENSION)) / 2;

        if (cells.isEmpty()) {
            for (int row = 0; row < rowCount; row++) {
                for (int col = 0; col < columnCount; col++) {
                    Rectangle cell = new Rectangle(
                            xOffset + (col * CELL_DIMENSION),
                            yOffset + (row * CELL_DIMENSION),
                            CELL_DIMENSION,
                            CELL_DIMENSION);
                    cells.add(cell);
                }
            }
        }

        if (!layoutCells.isEmpty()) {
            for(Point lc : layoutCells) {
                int index = lc.x + (lc.y * columnCount);
                Rectangle cell = cells.get(index);
                g2d.setColor(Color.BLACK);
                g2d.fill(cell);
            }

        }

        if (!selectedCell.isEmpty()) {
            for(Point sc : selectedCell) {
                int index = sc.x + (sc.y * columnCount);
                Rectangle cell = cells.get(index);
                g2d.setColor(Color.BLUE);
                g2d.fill(cell);
            }
        }

        if (!passengers.isEmpty()) {
            System.out.println("Passenger wasn't empty");
            for(Point px : passengers) {
                int index = px.x + (px.y * columnCount);
                Rectangle cell = cells.get(index);
                g2d.setColor(Color.GREEN);
                g2d.fill(cell);
            }
        } else {
            System.out.println("Passenger was empty");
        }

        g2d.setColor(Color.GRAY);
        for (Rectangle cell : cells) {
            g2d.draw(cell);
        }

        g2d.dispose();
    }

    public void runThrougher(List<Rectangle> cells){
        System.out.println("We've started");
        int i = 0;
        System.out.println(cells);
        for (Rectangle r : cells){
            System.out.println("Rectangle");
            System.out.println(i++);
        }
    }

    @Override
    public void update(Observable o, Object data){
        repaint();
        animationGrid = boardingModel.getTheGrid();
        System.out.println("From the update in animation panel:"+ Arrays.deepToString(animationGrid);
    }
}