package Passenger;

/**
 * This class is used to set blocks to prevent deadlocks
 */
public class BlockPair{

    int pos;
    int row;

    /**
     * Class constructor
     * @param pos the index of the letter component of the seat
     * @param row the numerical component of the seat
     */
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
