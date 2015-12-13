package Methods;

import Passenger.Passenger;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Oscar on 2015-11-25.
 */
public class Innovative {

    String name = "Innovative";

    public List<Passenger> mixOrder(List<Passenger> list) {

        //Instantiate temporary list
        List<Passenger> even = new LinkedList<Passenger>();
        List<Passenger> odd = new LinkedList<Passenger>();

        //Iterate through and sort odd and even
        for(Passenger pax : list){          //TODO: Look into the collect call for a list or change it a queue
            if(pax.getRow() % 2 == 0){
                even.add(pax);
            } else {
                odd.add(pax);
            }

        }

        //Merge the two lists
        even.addAll(odd);

        return even;
    }
}
