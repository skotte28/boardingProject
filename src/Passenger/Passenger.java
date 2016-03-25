package Passenger;

import Simulation.Direction;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.awt.*;

/**
 * This class contains the passenger related information, including information such
 * as seat and a potential BlockPair.
 *
 * @see BlockPair
 */
@XmlRootElement(name="Passenger")
public class Passenger {

    private Point blockPass;

    /* Attributes which don't change */
    private int row;
    private String position;
    private int handLuggage;

    private Direction nextMove;

    /* When moving to allow other passengers in */
    private String tempPosition;
    private int tempRow = -1;       //Initialized to be inactive, as indicated by -1 value

    /* When blocked by someone else */
    private BlockPair blockPair;
    
    private boolean seated;
    private int iteration = 0;

    @XmlElement (name="row")
    public void setRow(int row) {
        this.row = row;
    }

    public int getRow() {
        return row;
    }

    @XmlElement (name="position")
    public void setPosition(String position) {
        this.position = position;
    }

    public String getPosition() {
        return position;
    }

    public int getHandLuggage() {
        return handLuggage;
    }

    public void setHandLuggage(int handLuggage) {
        this.handLuggage = handLuggage;
    }

    public void setNextMove(Direction nextMove){
        this.nextMove = nextMove;
    }

    public Direction getNextMove(){
        return nextMove;
    }

    public String getTempPosition() {
        return tempPosition;
    }

    public void setTempPosition(String tempPosition) {
        this.tempPosition = tempPosition;
    }

    public int getTempRow() {
        return tempRow;
    }

    public void setTempRow(int tempRow) {
        this.tempRow = tempRow;
    }

    public BlockPair getBlockPair() {
        return blockPair;
    }

    public void setBlockPair(BlockPair blockPair) {
        this.blockPair = blockPair;
    }

    public boolean isSeated() {
        return seated;
    }

    public void setSeated(boolean seated) {
        this.seated = seated;
    }

    public int getIteration() {
        return iteration;
    }

    public void setIteration(int iteration) {
        this.iteration = iteration;
    }

    public Point getBlockPass() {
        return blockPass;
    }

    public void setBlockPass(Point blockPass) {
        this.blockPass = blockPass;
    }

    @Override
    public String toString(){
        return row+position;
    }

}
