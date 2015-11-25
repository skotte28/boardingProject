import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EventListener;
import java.util.Set;

public class BoardingController implements EventListener{

    private BoardingView theView = new BoardingView();
    private BoardingModel theModel = new BoardingModel();

    public BoardingController(BoardingView theView, BoardingModel theModel) {
        this.theView = theView;
        this.theModel = theModel;

        theView.settingsPanel.startSimulation.addActionListener(new ButtonListener());
    }

    public void manager(){
        ActionListener actionListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("Looks like something's changed");
                //boardingModel.setAircraftType(aircraftTypeList.getSelectedItem().toString());
            }
        };
            /* Changing the aircraft model selected is intended to show in the layout */
    }

    public void modelLoader(){
        System.out.println("modelLoader started");
        theModel.setAircraftType(theView.settingsPanel.getSelectedAircraft());
        theModel.setBoardingMethod(theView.settingsPanel.getSelectedBoardingMethod());
        theModel.setCapacity(theView.settingsPanel.getSelectedCapacity());

        /* TODO: Add the doors used options*/
    }

    public class ButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e){
            System.out.println("Pressed the button");
            //set all model variables
            modelLoader();
            //should run method in controller
        }

    }


/*
    An attempt to get the listeners from the BoardingController class


    ActionListener getActionListener(final JComboBox source) {
        System.out.println("Calls this");
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Somethings changed again - BoardingController");
                //testPane.setLayoutCells(settingsPanel.getSelectedAircraft());
            }
        };
    }

    ActionListener getButtonListener(final JButton source){
        System.out.println("Button");
        return new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                System.out.println("Button2");
            }
        };
    }
*/
}
