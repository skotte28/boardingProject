package Aircraft;
import Aircraft.Layout;
import java.util.ArrayList;

/**
 * Created by Oscar on 2015-10-15.
 */
public class AircraftType {
    Long id;
    private ArrayList capacity;
    private String name;
    private int doors;
    private Layout layout;

    public int getDoors(){
        return doors;
    }
}
