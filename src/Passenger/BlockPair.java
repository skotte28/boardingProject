package Passenger;

import jdk.nashorn.internal.ir.Block;

/**
 * Created by Oscar on 2015-12-15.
 */
public class BlockPair{

    int pos;
    int row;

    public BlockPair(int pos, int row){
        this.pos = pos;
        this.row = row;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    @Override
    public String toString(){
        return "("+getPos()+","+getRow()+")";
    }
}
