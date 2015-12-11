package Methods;

import Passenger.Passenger;

import java.util.*;

/**
 * Created by Oscar on 2015-11-25.
 */
public class Random {

    String name = "Random";

    public static List<Passenger> mixOrder(List<Passenger> list) {

        Collections.shuffle(list);

        return list;
    }
}
