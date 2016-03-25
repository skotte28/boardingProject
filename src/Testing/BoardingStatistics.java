package Testing;

import MVCFramework.BoardingModel;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * This class has been created to test the different blocking mechanisms.
 *
 */
public class BoardingStatistics {

    public static void main(String[] args) {

        String[] aircraft = {"B737", "A320", "CRJ900"};
        String[] boardingMethod = {"Back-to-front", "Random", "Outside-in", "Even-odd"};
        int[] capacity = {100, 75, 50, 25};
        for(String air : aircraft) {
            for (String boarding : boardingMethod) {
                for (int cap : capacity) {
                    ArrayList<Integer> iterations = new ArrayList<>();
                    for (int i = 0; i < 100; i++) {
                        BoardingModel testModel = new BoardingModel();
                        testModel.setAircraftType(air);
                        testModel.setBoardingMethod(boarding);
                        testModel.setCapacity(cap);
                        testModel.runSimulation();
                        testModel.timer.setDelay(0);
                        testModel.timer.start();
                        while (testModel.timer.isRunning()) {
                        }
                        iterations.add(testModel.getModelIteration());
                    }

                    try {
                        FileWriter fileWriter = new FileWriter(new File("Analysis/V1-" + air + "-" + boarding + "-" + cap + ".csv"));
                        for (Integer integer : iterations) {
                            fileWriter.append(integer.toString());
                            fileWriter.append("\n");
                        }
                        fileWriter.flush();
                        fileWriter.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
