import java.util.ArrayList;

/**
 * Created by Oscar on 2015-10-15.
 */
public class Passenger {
    Long id;
    String name;
    private ArrayList path;
    private int luggage; /*Carry-on luggage stowage time, 0 means no carry-on luggage*/
    private int walk;
    private int sitDown;
    private int seat; /*The numerical value of the seat, count starting from 1A, 1B, 1C... NX*/

    public ArrayList getPath() {
        return path;
    }

    public void setPath(ArrayList path) {
        this.path = path;
    }

    public int getLuggage() {
        return luggage;
    }

    public int getWalk() {
        return walk;
    }

    public void setWalk(int walk) {
        this.walk = walk;
    }

    public int getSitDown() {
        return sitDown;
    }

    public void setSitDown(int sitDown) {
        this.sitDown = sitDown;
    }

    public int getSeat() {
        return seat;
    }

    public void setSeat(int seat) {
        this.seat = seat;
    }
}
