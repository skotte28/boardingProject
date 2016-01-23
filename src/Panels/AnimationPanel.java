package Panels;

import Aircraft.AircraftType;
import Aircraft.Position;
import MVCFramework.BoardingModel;
import Passenger.Passenger;
import Simulation.AircraftGrid;

import java.awt.*;
import java.util.*;
import java.util.List;
import javax.swing.JPanel;

public class AnimationPanel extends JPanel implements Observer {

    private int CELL_DIMENSION = 25; //Each square is 25cm
    private int columnCount; //= 140;  //Total length 28m
    private int rowCount; //= 18;      //Total width 3.5m
    private int bufferCount;

    private List<Rectangle> cells;
    private List<Point> selectedCell;
    private List<Point> layoutCells;
    private List<Point> seatCells;
    protected List<PointPair> passengers;
    protected List<PointPair> seatedPax;

    //TODO: Should there really need to be two instances of theGrid
    protected Passenger[][] animationGrid;

    private BoardingModel boardingModel;

    public AnimationPanel(BoardingModel boardingModel) {

        AircraftType aircraftType = boardingModel.getAircraftType();
        newValues(aircraftType);
        this.boardingModel = boardingModel;

        cells = new ArrayList<>(columnCount * rowCount);
        selectedCell = new ArrayList<Point>();
        layoutCells = new ArrayList<Point>();
        seatCells = new ArrayList<Point>();
        passengers = new ArrayList<PointPair>();
        seatedPax = new ArrayList<PointPair>();

        /*if(AircraftGrid.theGrid != null) {
            for (Passenger[] position : AircraftGrid.theGrid) {
                for (Passenger pax : position) {
                    if (pax != null) {
                        passengers.add(new Point(boardingModel.getSeatValue(pax.getPosition()), pax.getRow()));
                    }
                }
            }
        }*/

        //TODO: Remove; color for testing purposes
        this.setBackground(Color.ORANGE);
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
                g2d.fillOval(10,10,10,10);
                //TODO: Fix the row labelling

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

        if (!seatCells.isEmpty()) {
            int rowLabeler = 0;
            for(Point sc : seatCells) {
                int index = sc.x + (sc.y * columnCount);
                Rectangle cell = cells.get(index);
                g2d.setColor(Color.LIGHT_GRAY);
                g2d.fill(cell);
            }

            /*Font font = new Font("Serif", Font.PLAIN, 96);
            g2d.setFont(font);
            g2d.setColor(Color.BLACK);
            g2d.fillOval(10,10,10,10);
            columnLabeler++;
            if(columnLabeler <= rowCount){
                g2d.drawString("hello",10,10);
            }*/
        }

        if (!passengers.isEmpty()) {
            System.out.println("Passenger wasn't empty");
            for(PointPair px : passengers) {
                int index = px.getPoint().x + (px.getPoint().y * columnCount);
                Rectangle cell = cells.get(index);
                g2d.setColor(Color.YELLOW);
                g2d.fill(cell);
                g2d.setColor(Color.BLACK);
                g2d.drawString(px.getLabel(), (int) cell.getX()+(CELL_DIMENSION/4), (int) cell.getY()+(CELL_DIMENSION/2));
                //TODO: Remove, for testing purposes only
                //System.out.println(px.getLabel());
            }
        } else {
            System.out.println("Passenger was empty");
        }

        if (!seatedPax.isEmpty()) {
            System.out.println(seatedPax);
            for(PointPair sc : seatedPax) {
                int index = sc.getPoint().x + (sc.getPoint().y * columnCount);
                Rectangle cell = cells.get(index);
                g2d.setColor(Color.GREEN);
                g2d.fill(cell);
                g2d.setColor(Color.BLACK);
                g2d.drawString(sc.getLabel(), (int) cell.getX()+(CELL_DIMENSION/4), (int) cell.getY()+(CELL_DIMENSION/2));
            }
        }

        int columnLabeler = 0;
        int rowLabeler = 0;
        int aisleException = -1;
        if(boardingModel.getAircraftType() != null) {
            aisleException = boardingModel.getSeatValue("AISLE");
        }
        char[] alphabet = "ABCDEFGHIJKLMNOPQRSTUVWYZ".toCharArray();
        for (Rectangle cell : cells) {
            g2d.setColor(Color.GRAY);
            g2d.draw(cell);
            g2d.setColor(Color.BLACK);
            if(columnLabeler > 0 && columnLabeler <= columnCount-bufferCount) {
                g2d.drawString(Integer.toString(columnLabeler), (int) cell.getX() + (CELL_DIMENSION/4), (int) cell.getY());
            }
            if(rowLabeler < rowCount && (columnLabeler%columnCount==0)){
                if(rowLabeler != aisleException) {
                    g2d.drawString(Character.toString(alphabet[rowLabeler]), (int) cell.getX()-(CELL_DIMENSION/2), (int) cell.getY()+(CELL_DIMENSION));
                    System.out.println("Row laballed!");
                }
                rowLabeler++;
            }
            columnLabeler++;
        }
        g2d.dispose();
    }

    public void newValues(AircraftType aircraftType) {
        /*Author: OS */
        if (aircraftType != null) {
            columnCount = aircraftType.getRows() + aircraftType.getBuffer();
            rowCount = aircraftType.getWidth() + aircraftType.getAisle();
            bufferCount = aircraftType.getBuffer();
        } else {
            columnCount = 0;
            rowCount = 0;
            bufferCount = 0;
        }
        System.out.println(rowCount + "and" + columnCount);

        if (aircraftType != null){
            for (int row = 1; row <= aircraftType.getRows(); row++) {
                for (int pos = 0; pos < aircraftType.getWidth() + aircraftType.getAisle(); pos++) {
                    if (pos != Position.AISLE.getPositionValue()) {
                        seatCells.add(new Point(row, pos));
                    }
                }

            }
        }
        System.out.println(seatCells);
    }

    public void paxUpdate(Passenger[][] animationGrid, AircraftType aircraftType){
        for(int pos = 0; pos < aircraftType.getWidth()+aircraftType.getAisle(); pos++){
            for(int row = 1; row <= aircraftType.getRows(); row++){
                if(animationGrid[pos][row] != null){
                    if(animationGrid[pos][row].isSeated()){
                        seatedPax.add(new PointPair(new Point(row, pos), animationGrid[pos][row].toString()));
                    } else {
                        passengers.add(new PointPair(new Point(row, pos), animationGrid[pos][row].toString()));
                    }
                }
            }
        }
    }

    @Override
    public void update(Observable o, Object data){
        invalidate();
        newValues(boardingModel.getAircraftType());
        animationGrid = boardingModel.getTheGrid();
        paxUpdate(animationGrid, boardingModel.getAircraftType());
        repaint();
        //TODO: Print statement for testing purposes, remove once complete
        //System.out.println("From the update in animation panel:"+ Arrays.deepToString(animationGrid));
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
}