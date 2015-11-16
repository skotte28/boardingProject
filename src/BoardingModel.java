import Aircraft.AircraftType;

public class BoardingModel {

    private String boardingMethod;

   private String aircraftType;

    BoardingModel(){
        String aircraftType;
    }

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
}