import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.List;

/**
 * Created by Oscar on 2015-11-30.
 */
public class AnimationPanelOld extends JPanel {

    private int CELL_DIMENSIONS = 3;    //Specify the scale used
    private int columns  = 140;         //Specify total length
    private int rows = 18;              //Specify total width

    private List<Rectangle> cells;
    private List<Point> layoutCells;
    private List<Point> selectedCell;

    public void setLayoutCells(List<Point> layoutCells) { this.layoutCells = layoutCells; }

    public AnimationPanelOld(){

        cells = new ArrayList<>(columns*rows);
        selectedCell = new ArrayList<Point>();
        layoutCells = new ArrayList<Point>();

        //For testing purposes
        this.setBackground(Color.orange);



        //Following code is from:
        MouseAdapter mouseHandler;
        mouseHandler = new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                Point point = e.getPoint();

                int width = getWidth();
                int height = getHeight();

                int cellWidth = CELL_DIMENSIONS;
                int cellHeight = CELL_DIMENSIONS;

                int xOffset = (width - (columns * cellWidth)) / 2;
                int yOffset = (height - (rows * cellHeight)) / 2;

                int column = (e.getX() - xOffset) / cellWidth;
                int row = (e.getY() - yOffset) / cellHeight;

                //selectedCell = null;
                if (e.getX() >= xOffset && e.getY() >= yOffset) {

                    column = (e.getX() - xOffset) / cellWidth;
                    row = (e.getY() - yOffset) / cellHeight;

                    if (column >= 0 && row >= 0 && column < columns && row < rows) {
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

            }
        };
        addMouseMotionListener(mouseHandler);

    }

    @Override
    public void repaint(){
        System.out.println("Repainted");
        super.repaint();
    }

    @Override
    public Dimension getPreferredSize(){ return new Dimension(200, 200);}

    @Override
    public void invalidate(){
        cells.clear();
        super.invalidate();
    }

    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();

        int width = getWidth();
        int height = getHeight();

        int cellWidth = CELL_DIMENSIONS;
        int cellHeight = CELL_DIMENSIONS;

        int xOffset = (width - (columns * cellWidth)) / 2;
        int yOffset = (height - (rows * cellHeight)) / 2;

        if (cells.isEmpty()) {
            for (int row = 0; row < rows; row++){
                for (int col = 0; col < columns; col++){
                    Rectangle cell = new Rectangle(
                            xOffset + (col * cellWidth),
                            yOffset + (col * cellHeight),
                            cellWidth,
                            cellHeight
                    );
                    cell.add(cell);

                }
            }
        }

        if (!layoutCells.isEmpty()) {
            for( Point lc : layoutCells) {
                int index = lc.x + (lc.y * columns);
                Rectangle cell = cells.get(index);
                g2d.setColor(Color.BLACK);
                g2d.fill(cell);
            }
        }

        if (!selectedCell.isEmpty()){
            for (Point sc : selectedCell){
                int index = sc.x + (sc.y * columns);
                Rectangle cell = cells.get(index);
                g2d.setColor(Color.BLUE);
                g2d.fill(cell);
            }
        }
        g2d.setColor(Color.GRAY);
        for (Rectangle cell : cells) {
            g2d.draw(cell);
        }

        g2d.dispose();
    }
}
