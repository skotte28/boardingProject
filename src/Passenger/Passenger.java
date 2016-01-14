package Passenger;

import Aircraft.Position;
import Simulation.Direction;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by Oscar on 2015-11-25.
 */
@XmlRootElement(name="Passenger")
public class Passenger {

    /* Attributes which don't change */
    int row;
    String position;
    int handluggage;

    Direction nextMove;

    /* When moving to allow other passengers in */
    String tempPosition;
    int tempRow = -1;       //Initialized to be inactive, as indicated by -1 value

    /* When blocked by someone else */
    BlockPair blockPair;

    boolean visited;
    boolean seated;

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

    public int getHandluggage() {
        return handluggage;
    }

    public void setHandluggage(int handluggage) {
        this.handluggage = handluggage;
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

    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    public boolean isSeated() {
        return seated;
    }

    public void setSeated(boolean seated) {
        this.seated = seated;
    }

    @Override
    public String toString(){
        return row+position;
    }

}
