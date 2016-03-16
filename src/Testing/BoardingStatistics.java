package Testing;

import MVCFramework.BoardingModel;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Oscar on 2016-03-15.
 */
public class BoardingStatistics {

    public static void main(String[] args) {

        String aircraft = "CRJ900";
        String boardingMethod = "Odd-even";
        int capacity = 25;

        ArrayList<Integer> iterations = new ArrayList<>();
        for(int i=0; i<100; i++) {
            BoardingModel testModel = new BoardingModel();
            testModel.setAircraftType(aircraft);
            testModel.setBoardingMethod(boardingMethod);
            testModel.setCapacity(capacity);
            testModel.runSimulation();
            testModel.timer.setDelay(0);
            testModel.timer.start();
            while (testModel.timer.isRunning()) {}
            iterations.add(testModel.getModelIteration());
        }

        try {
            FileWriter fileWriter = new FileWriter(new File("Analysis/"+aircraft+"-"+boardingMethod+"-"+capacity+".csv"));
            for(Integer integer : iterations) {
                fileWriter.append(integer.toString());
                fileWriter.append("\n");
            }
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e){
            e.printStackTrace();
        }
    }


}
