package Methods;

import Aircraft.AircraftType;
import Passenger.Passenger;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Created by Oscar on 2015-11-25.
 */
public class BackToFront {

    String name = "BackToFront";

    public static List<Passenger> mixOrder(List<Passenger> list) {

        int aircraftLength = 30;    //Should really be taken from the aircraftType

        //Instantiate temporary list
        LinkedList<Passenger> firstThird = new LinkedList<Passenger>();
        LinkedList<Passenger> secondThird = new LinkedList<Passenger>();
        LinkedList<Passenger> thirdThird = new LinkedList<Passenger>();

        //Iterate through and sort odd and even
        for(Passenger pax : list){
            if(pax.getRow() < (aircraftLength/3)){
                firstThird.add(pax);
            } else if (pax.getRow() <= (aircraftLength/3)*2){
                secondThird.add(pax);
            } else { thirdThird.add(pax); }
        }

        //Merge the three lists
        firstThird.addAll(secondThird);
        firstThird.addAll(thirdThird);
        return firstThird;
    }
}
