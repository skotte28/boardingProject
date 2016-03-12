package Methods;

import Aircraft.AircraftType;
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
        thirdThird.addAll(secondThird);
        thirdThird.addAll(firstThird);
        return thirdThird;
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

        //Instantiate temporary list
        List<Passenger> window = new LinkedList<>();
        List<Passenger> middle = new LinkedList<>();
        List<Passenger> aisle = new LinkedList<>();

        //Get the window, middle, and aisle seats from the aircraft type
        ArrayList<String> windowSeats = aircraftType.getWindowSeats();
        ArrayList<String> middleSeats = aircraftType.getMiddleSeats();
        ArrayList<String> aisleSeats = aircraftType.getAisleSeats();

        //Iterate through and sort odd and even - could be done with switch case (see above)
        for(Passenger pax : list){
            String pos = pax.getPosition();
            System.out.println(pos);
            if (windowSeats.contains(pos)){
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

}
