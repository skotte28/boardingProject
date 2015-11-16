import Aircraft.AircraftType;
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
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class TestPane extends JPanel {
    final private int CELL_DIMENSION = 9;
    private int columnCount = 140;
    private int rowCount = 16;
    private List<Rectangle> cells;
    private List<Point> selectedCell;
    private List<Point> layoutCells;
    String selectedAircraft;

    public TestPane(BoardingModel boardingModel) {
        cells = new ArrayList<>(columnCount * rowCount);
        selectedCell = new ArrayList<Point>();
        layoutCells = new ArrayList<Point>();
        //layoutCells = BoardingModel.getLayout(selectedAircraft);

        for(int i = 0; i < 140; i++){
            for(int n = 0; n<=0; n++){
                layoutCells.add(new Point(i,n));
            }
        }

        MouseAdapter mouseHandler;
        mouseHandler = new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                Point point = e.getPoint();

                int width = getWidth();
                int height = getHeight();

                int cellWidth = CELL_DIMENSION;
                int cellHeight = CELL_DIMENSION;

                int xOffset = (width - (columnCount * cellWidth)) / 2;
                int yOffset = (height - (rowCount * cellHeight)) / 2;

                int column = (e.getX() - xOffset) / cellWidth;
                int row = (e.getY() - yOffset) / cellHeight;

                //selectedCell = null;
                if (e.getX() >= xOffset && e.getY() >= yOffset) {

                    column = (e.getX() - xOffset) / cellWidth;
                    row = (e.getY() - yOffset) / cellHeight;

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

            }
        };
        //addMouseListener(mouseHandler);
        addMouseMotionListener(mouseHandler);
        runThrougher(cells);
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

        int cellWidth = CELL_DIMENSION;
        int cellHeight = CELL_DIMENSION;

        int xOffset = (width - (columnCount * cellWidth)) / 2;
        int yOffset = (height - (rowCount * cellHeight)) / 2;

        if (cells.isEmpty()) {
            for (int row = 0; row < rowCount; row++) {
                for (int col = 0; col < columnCount; col++) {
                    Rectangle cell = new Rectangle(
                            xOffset + (col * cellWidth),
                            yOffset + (row * cellHeight),
                            cellWidth,
                            cellHeight);
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

        g2d.setColor(Color.GRAY);
        for (Rectangle cell : cells) {
            g2d.draw(cell);
        }

        g2d.dispose();
    }

    public void runThrougher(List<Rectangle> cells){
        System.out.println("We've started");
        int i = 0;
        for (Rectangle r : cells){
            System.out.println(i++);
        }
    }
}