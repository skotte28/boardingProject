package Methods;

import Aircraft.AircraftType;
import Aircraft.Position;
import Passenger.Passenger;

import java.util.*;

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

    public static List<Passenger> outsideIn(List<Passenger> list, AircraftType aircraftType) {


        //TODO: Change to this adapts according to aircraft type

        /*
        Problem with this approach is that it's hard coding which seats are the window seats,
        middle seats aisle seats. This means that smaller/larger aircrafts can't be added easily.
        This information really needs to come from enum Position, which needs to load it from the
        aircraft XML file.
         */

        //Instantiate temporary list
        List<Passenger> window = new LinkedList<>();
        List<Passenger> middle = new LinkedList<>();
        List<Passenger> aisle = new LinkedList<>();

        //To be used to ensure that more aircrafts can be added in the future - could be done without if switch case is used below
        LinkedList<Position> windowSeats = new LinkedList<>();
        System.out.println(Arrays.deepToString(aircraftType.getWindowSeats()));
        for(Position pos : aircraftType.getWindowSeats()){
            System.out.println("Raw Position:"+pos);
            windowSeats.add(pos);
        }
        LinkedList<Position> middleSeats = new LinkedList<>();
        for(Position pos : aircraftType.getMiddleSeats()){
            middleSeats.add(pos);
        }
        LinkedList<Position> aisleSeats = new LinkedList<>();
        for(Position pos : aircraftType.getAisleSeats()){
            aisleSeats.add(pos);
        }

        //Iterate through and sort odd and even - could be done with switch case (see above)
        for(Passenger pax : list){
            Position pos = pax.getPosition();
            System.out.println(pos);
            if(windowSeats.contains(pos)){
                window.add(pax);
            } else if (middleSeats.contains(pos)){
                middle.add(pax);
            } else if (aisleSeats.contains(pos)){
                aisle.add(pax);
            }
        }

        //Merge the three lists
        System.out.println(window.toString());
        window.addAll(middle);
        window.addAll(aisle);

        return window;
    }

    public static List<Passenger> random(List<Passenger> list) {

        Collections.shuffle(list);

        return list;
    }

}
