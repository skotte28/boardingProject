package Methods;

import Aircraft.AircraftType;
import Aircraft.Position;
import Passenger.Passenger;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Created by Oscar on 2015-11-25.
 */
public class Method {

    public static List<Passenger> backToFront(List<Passenger> list, AircraftType aircraftType) {

        int aircraftLength = aircraftType.getRows();

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

    public static List<Passenger> innovative(List<Passenger> list) {

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

    public static List<Passenger> outsideIn(List<Passenger> list) {

        /*
        Problem with this approach is that it's hard coding which seats are the window seats,
        middle seats aisle seats. This means that smaller/larger aircrafts can't be added easily.
        This information really needs to come from enum Position, which needs to load it from the
        aircraft XML file.
         */

        //Instantiate temporary list
        List<Passenger> window = new LinkedList<Passenger>();
        List<Passenger> middle = new LinkedList<Passenger>();
        List<Passenger> aisle = new LinkedList<Passenger>();

        //To be used to ensure that more aircrafts can be added in the future - could be done without if switch case is used below
        LinkedList<Position> windowSeats = new LinkedList<Position>();
        windowSeats.add(Position.A);
        //windowSeats.add(Position.F);
        LinkedList<Position> middleSeats = new LinkedList<Position>();
        windowSeats.add(Position.B);
        //windowSeats.add(Position.E);
        LinkedList<Position> aisleSeats = new LinkedList<Position>();
        windowSeats.add(Position.C);
        //windowSeats.add(Position.D);

        //Iterate through and sort odd and even - could be done with switch case (see above)
        for(Passenger pax : list){
            Position pos = pax.getPosition();
            if(windowSeats.contains(pos)){
                window.add(pax);
            } else if (middleSeats.contains(pos)){
                middle.add(pax);
            } else if (aisleSeats.contains(pos)){
                aisle.add(pax);
            }
        }

        //Merge the three lists
        window.addAll(middle);
        window.addAll(aisle);

        return window;
    }

    public static List<Passenger> random(List<Passenger> list) {

        Collections.shuffle(list);

        return list;
    }

}
