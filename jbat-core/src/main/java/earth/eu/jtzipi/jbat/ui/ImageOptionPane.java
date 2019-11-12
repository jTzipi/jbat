package earth.eu.jtzipi.jbat.ui;

import earth.eu.jtzipi.jbat.image.ScaleMethod;
import earth.eu.jtzipi.jbat.image.Unit;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Spinner;
import javafx.scene.layout.Pane;
import org.controlsfx.control.PropertySheet;


public class ImageOptionPane extends Pane {

    PropertySheet imgPropertySheet;
    Spinner widthSpin;
    Spinner heightSpin;
    //    Spinner offSetXSpin;
//    Spinner offSetYSpin;
    ComboBox<ScaleMethod> scaleMethodComboB;
    ComboBox<Unit> scaleUnitComboB;
    CheckBox preserveARCB;


    ImageOptionPane() {

        createImageOptionPane();
    }

    private void createImageOptionPane() {
        imgPropertySheet = new PropertySheet();


        widthSpin = new Spinner();
        heightSpin = new Spinner();

        ObservableList<Unit> unitOL = FXCollections.observableArrayList( Unit.values() );
        ObservableList<ScaleMethod> scalemOL = FXCollections.observableArrayList( ScaleMethod.values() );


        scaleMethodComboB = new ComboBox<>( scalemOL );
        scaleUnitComboB = new ComboBox<>( unitOL );
    }
}
