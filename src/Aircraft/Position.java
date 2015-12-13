package Aircraft;

/**
 * Created by Oscar on 2015-12-11.
 */
public enum Position {
    /*A (0),
    B (1),
    C (2),
    D (4),
    E (5),
    F (6);*/

    A (0),
    B (1),
    C (3),
    D (4);

    private final int positionValue;

    Position(int positionValue){
        this.positionValue = positionValue;
    }

    public int getPositionValue(){
        return positionValue;
    }
}
