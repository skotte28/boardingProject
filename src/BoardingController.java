import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BoardingController {

    private BoardingView theView;
    private BoardingModel theModel;

    public BoardingController(BoardingView theView, BoardingModel theModel) {
        this.theView = theView;
        this.theModel = theModel;

        /*this.theView.addBoardingListener(new BoardingListener());*/
    }

    class BoardingListener implements ActionListener{

        public void actionPerformed(ActionEvent e) {

        }

    }

}