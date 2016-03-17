import MVCFramework.BoardingController;
import MVCFramework.BoardingModel;
import MVCFramework.BoardingView;
import Recycling.BoardingModelBackup;

/**
 * Created by Oscar on 2015-10-15.
 */
public class MVCBoarder {

    public static void main(String[] args) {

        System.out.println("Houston we have lift-off!");

        BoardingModel theModel = new BoardingModel();

        BoardingView theView = new BoardingView(theModel);

        BoardingController theController = new BoardingController(theView, theModel);

        theView.setVisible(true);

        System.out.println("What is going on?");
    }
}