import Aircraft.AircraftType;

public class BoardingModel {

    private String boardingMethod;

    private String aircraftType;

    private int capacity;

    BoardingModel(){
        String aircraftType;
    }


    //Testing purposes only
    public static void printAMessage(){
        System.out.println("Just a test message");
    }


    public String getAircraftType() {
        return aircraftType;
    }

    public void setAircraftType(String string) {
        this.aircraftType = aircraftType;
    }

    public String getBoardingMethod() {
        return boardingMethod;
    }

    public void setBoardingMethod(String boardingMethod) {
        this.boardingMethod = boardingMethod;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }
}