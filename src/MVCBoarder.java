/**
 * Created by Oscar on 2015-10-15.
 */
public class MVCBoarder {

    public static void main(String[] args) {

        System.out.println("Houston we have lift-off!");

        BoardingView theView = new BoardingView();

        BoardingModel theModel = new BoardingModel();

        BoardingController theController = new BoardingController(theView, theModel);

        theView.setVisible(true);

        System.out.println("What is going on?");
    }
}
