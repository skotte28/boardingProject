package Panels;

import Aircraft.AircraftType;
import MVCFramework.BoardingModel;
import Passenger.Passenger;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.*;

/**
 * This class holds the objects relating the to the drawn animation of the boarding process,
 * and is a subclass of JPanel, as well as implementing the Observer interface.
 *
 * The drawing mechanism has been derived by the work of http://stackoverflow.com/users/992484/madprogrammer
 *
 * @see JPanel
 * @see Observer
 *
 */
public class AnimationPanel extends JPanel implements Observer {

    private int columnCount; //= 140;  //Total length 28m
    private int rowCount; //= 18;      //Total width 3.5m
    private int bufferCount;

    private List<Rectangle> cells;
    private List<Point> layoutCells;
    private List<Point> seatCells;
    private List<PointPair> passengers;
    private List<PointPair> seatedPax;

    private BoardingModel boardingModel;

    private BufferedImage image;

    /**
     * Class constructor
     *
     * @param boardingModel the instance of BoardingModel associated with the animation panel
     * @see BoardingModel
     */
    public AnimationPanel(BoardingModel boardingModel) {

        AircraftType aircraftType = boardingModel.getAircraftType();
        newValues(aircraftType);
        this.boardingModel = boardingModel;

        this.setBackground(Color.WHITE);

        cells = new ArrayList<>(columnCount * rowCount);
        layoutCells = new ArrayList<>();
        seatCells = new ArrayList<>();
        passengers = new ArrayList<>();
        seatedPax = new ArrayList<>();


        try{
            image = ImageIO.read(new File("resources/images/seatBigger.png"));
        } catch (IOException ex){
            System.err.println("Could not find seat image.");
        }

    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(200, 200);
    }

    @Override
    public void invalidate() {
        cells.clear();
        seatCells.clear();
        passengers.clear();
        seatedPax.clear();
        super.invalidate();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();

        Font paxFont = g2d.getFont().deriveFont( 9.0f );
        Font labelFont = g2d.getFont().deriveFont( 12.5f );

        int width = getWidth();
        int height = getHeight();

        int CELL_DIMENSION = 25;
        int xOffset = (width - (columnCount * CELL_DIMENSION)) / 2;
        int yOffset = (height - (rowCount * CELL_DIMENSION)) / 2;

        /* Create the cells */
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


        /* Draw the layouts of the cells */
        if (!layoutCells.isEmpty()) {
            for(Point lc : layoutCells) {
                int index = lc.x + (lc.y * columnCount);
                Rectangle cell = cells.get(index);
                g2d.setColor(Color.BLACK);
                g2d.fill(cell);
                g2d.fillOval(10,10,10,10);
            }

        }

        /* Draw grey under the seat image, in case it can't be found, and insert image on top*/
        if (!seatCells.isEmpty()) {
            for(Point sc : seatCells) {
                int index = sc.x + (sc.y * columnCount);
                Rectangle cell = cells.get(index);
                g2d.setColor(Color.LIGHT_GRAY);
                g2d.fill(cell);
                g2d.drawImage(image, (int) cell.getX(), (int) cell.getY(), null);
            }
        }

        /* Draw the not seated passengers */
        if (!passengers.isEmpty()) {
            g2d.setFont(paxFont);
            for(PointPair px : passengers) {
                int index = px.getPoint().x + (px.getPoint().y * columnCount);
                Rectangle cell = cells.get(index);
                g2d.setColor(Color.YELLOW);
                g2d.fillOval((int) cell.getX(), (int) cell.getY(), CELL_DIMENSION, CELL_DIMENSION);
                g2d.setColor(Color.BLACK);
                g2d.drawString(px.getLabel(), (int) cell.getX()+(CELL_DIMENSION /4), (int) cell.getY()+(2*(CELL_DIMENSION /3)));
            }
        }

        /* Draw the seated passengers */
        if (!seatedPax.isEmpty()) {
            g2d.setFont(paxFont);
            for(PointPair sc : seatedPax) {
                int index = sc.getPoint().x + (sc.getPoint().y * columnCount);
                Rectangle cell = cells.get(index);
                g2d.setColor(Color.GREEN);
                g2d.fillOval((int) cell.getX(), (int) cell.getY(), CELL_DIMENSION, CELL_DIMENSION);
                g2d.setColor(Color.BLACK);
                g2d.drawString(sc.getLabel(), (int) cell.getX()+(CELL_DIMENSION /4), (int) cell.getY()+(2*(CELL_DIMENSION /3)));
            }
        }

        /* Labeler for rows and seat positions */
        int columnLabeler = 0;
        int rowLabeler = 0;
        int aisleException = -1;
        int aisleCount = 0;
        if(boardingModel.getAircraftType() != null) {
            aisleException = boardingModel.getSeatValue("AISLE");
        }
        char[] alphabet = "ABCDEFGHIJKLMNOPQRSTUVWYZ".toCharArray();
        for (Rectangle cell : cells) {
            g2d.setFont(labelFont);
            g2d.setColor(Color.GRAY);
            g2d.draw(cell);
            g2d.setColor(Color.BLACK);
            if(columnLabeler > 0 && columnLabeler <= columnCount-bufferCount) {
                g2d.drawString(Integer.toString(columnLabeler), (int) cell.getX() + (CELL_DIMENSION /4), (int) cell.getY()-(CELL_DIMENSION/8));
            }
            if(rowLabeler < rowCount && (columnLabeler%columnCount==0)){
                if(rowLabeler != aisleException) {
                    g2d.drawString(Character.toString(alphabet[(rowCount-2)-rowLabeler+aisleCount]), (int) cell.getX()-(CELL_DIMENSION /2), (int) cell.getY()+(2*(CELL_DIMENSION /3)));
                } else {
                    aisleCount++;
                }
                rowLabeler++;
            }
            columnLabeler++;
        }
        g2d.dispose();
    }

    /**
     * This method updates the aircraft associated variables needed to draw, when a
     * new aircraft type is selected be the user
     *
     * @param aircraftType the aircraft type used for the simulation
     */
    private void newValues(AircraftType aircraftType) {
        if (aircraftType != null) {
            columnCount = aircraftType.getRows() + aircraftType.getBuffer();
            rowCount = aircraftType.getWidth() + aircraftType.getAisle();
            bufferCount = aircraftType.getBuffer();
        } else {
            columnCount = 0;
            rowCount = 0;
            bufferCount = 0;
        }

        if (aircraftType != null){
            for (int row = 1; row <= aircraftType.getRows(); row++) {
                for (int pos = 0; pos < aircraftType.getWidth() + aircraftType.getAisle(); pos++) {
                    if (pos != boardingModel.getSeatValue("AISLE")) {
                        seatCells.add(new Point(row, pos));
                    }
                }

            }
        }
    }

    /**
     *
     * This method updates the lists on who is seated in the aircraft and who is standing.
     * These lists are used when the passengers are drawn.
     *
     * @param animationGrid the local version of theGrid
     * @param aircraftType the aircraft type used for the simulation
     */
    private void paxUpdate(Passenger[][] animationGrid, AircraftType aircraftType) {
        if (animationGrid != null) {
            for (int pos = 0; pos < aircraftType.getWidth() + aircraftType.getAisle(); pos++) {
                for (int row = 0; row < aircraftType.getRows()+aircraftType.getBuffer(); row++) {
                    if (animationGrid[pos][row] != null) {
                        if (animationGrid[pos][row].isSeated()) {
                            seatedPax.add(new PointPair(new Point(row, pos), animationGrid[pos][row].toString()));
                        } else {
                            passengers.add(new PointPair(new Point(row, pos), animationGrid[pos][row].toString()));
                        }
                    }
                }
            }
        }
    }

    @Override
    public void update(Observable o, Object data){
        invalidate();
        newValues(boardingModel.getAircraftType());
        paxUpdate(boardingModel.getTheGrid(), boardingModel.getAircraftType());
        repaint();
        //takeSnapShot(this); - Enable for testing / debugging purposes
    }

    public class PointPair{
        Point point;
        String label;

        public PointPair(Point point, String label){
            this.point = point;
            this.label = label;
        }

        public String getLabel(){
            return label;
        }

        public Point getPoint(){
            return point;
        }
    }

    /* Enable for testing purposes
    *
    * Produces a screenshot of each iteration, create a folder named screenshots in the top directory to use
    *
    private void takeSnapShot(JPanel panel){
        BufferedImage bufImage = new BufferedImage(panel.getSize().width, panel.getSize().height,BufferedImage.TYPE_INT_RGB);
        panel.paint(bufImage.createGraphics());
        File imageFile = new File("./screenshots/screenshot"+count+".jpeg");
        try{
            imageFile.createNewFile();
            ImageIO.write(bufImage, "jpeg", imageFile);
        }catch(Exception ex){
            System.out.println("Problem with creating image");
        }
        count++;
    } */
}