package Methods;

import Passenger.Passenger;

import java.util.Queue;

/**
 * Created by Oscar on 2015-11-25.
 */
public abstract class Method {

    String name;

    public abstract Queue<Passenger> mixOrder(Queue<Passenger> queue);

}
