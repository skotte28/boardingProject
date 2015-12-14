package Methods;

import Aircraft.Position;
import Passenger.Passenger;

import java.util.LinkedList;
import java.util.List;


/**
 * Created by Oscar on 2015-11-25.
 */
public class OutsideIn {

    String name = "OutsideIn";

    public List<Passenger> mixOrder(List<Passenger> list) {
        

        //TODO: Change to this adapts according to aircraft type

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
}
