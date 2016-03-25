package Methods;

import Aircraft.AircraftType;
import Passenger.Passenger;

import java.util.*;

/**
 *
 * This class holds the static sorting methods used to order the Passenger list.
 *
 * @author Oscar Schafer
 *
 * @see Passenger
 *
 */
public class Method {

    /**
     *
     * Orders the passengers according to the back-to-front boarding method.
     * The aircraft is divided into three parts, which within the passenger
     * order is random.
     *
     * @param list the list of passengers which is to be sorted
     * @param aircraftType the aircraft type which the simulation is being run on
     * @return the passenger list ordered back-to-front
     * @see AircraftType
     */
    public static List<Passenger> backToFront(List<Passenger> list, AircraftType aircraftType) {

        int aircraftLength = aircraftType.getRows();

        //Instantiate temporary list
        LinkedList<Passenger> firstThird = new LinkedList<>();
        LinkedList<Passenger> secondThird = new LinkedList<>();
        LinkedList<Passenger> thirdThird = new LinkedList<>();

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

    /**
     * Orders the passengers according to the even-odd boarding method.
     * The passengers seated in even rows are put into one list,
     * while passengers seated in odd rows are put into another list.
     *
     * @param list the list of passengers which is to be sorted
     * @return the passenger list ordered even-odd
     */
    public static List<Passenger> innovative(List<Passenger> list) {

        //Instantiate temporary list
        List<Passenger> even = new LinkedList<>();
        List<Passenger> odd = new LinkedList<>();

        //Iterate through and sort odd and even
        for(Passenger pax : list){
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

    /**
     * Orders the passengers according to the outside in boarding method.
     * The passengers seated in window seats are put first, then passengers
     * seated in middle seats, and finally passengers in aisle seats.
     *
     * @param list the list of passengers which is to be sorted
     * @param aircraftType the aircraft type which the simulation is being run on
     * @return the passenger list ordered outside-in
     */
    public static List<Passenger> outsideIn(List<Passenger> list, AircraftType aircraftType) {

        //Instantiate temporary list
        List<Passenger> window = new LinkedList<>();
        List<Passenger> middle = new LinkedList<>();
        List<Passenger> aisle = new LinkedList<>();

        //Get the window, middle, and aisle seats from the aircraft type
        ArrayList<String> windowSeats = aircraftType.getWindowSeats();
        ArrayList<String> middleSeats = aircraftType.getMiddleSeats();
        ArrayList<String> aisleSeats = aircraftType.getAisleSeats();

        //Iterate through and sort odd and even
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
