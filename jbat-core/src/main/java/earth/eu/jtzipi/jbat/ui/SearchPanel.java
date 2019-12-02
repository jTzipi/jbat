package earth.eu.jtzipi.jbat.ui;

import earth.eu.jtzipi.modules.fx.RoundLabel;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;

public class SearchPanel extends Pane {

    ComboBox<String> searchCombB;
    ChoiceBox<String> typeChoiceB;
    CheckBox regExCB;
    CheckBox casiCB;
    RoundLabel updateRL;

    SearchPanel() {

        createSearchPanel();
    }

    private void createSearchPanel() {

        GridPane mainPane = new GridPane();

        Label aearchL = new Label( "search..." );
        Label typesL = new Label( "typ" );
        searchCombB = new ComboBox<>();
        searchCombB.setPrefWidth( 500D );

        typeChoiceB = new ChoiceBox<>();
        typeChoiceB.getItems().addAll( "File", "Ext", "Size" );

        regExCB = new CheckBox( "regExp" );
        casiCB = new CheckBox( "case insensitive" );
        updateRL = new RoundLabel();
        mainPane.add( aearchL, 0, 0, 1, 1 );
        mainPane.add( typesL, 0, 1 );
        mainPane.add( searchCombB, 1, 0, 3, 1 );
        mainPane.add( typeChoiceB, 1, 1 );
        mainPane.add( regExCB, 2, 1 );
        mainPane.add( casiCB, 3, 1 );
        mainPane.add( updateRL, 4, 0, 2, 2 );
        mainPane.setHgap( 5D );
        mainPane.setVgap( 2D );


        GridPane.setHgrow( searchCombB, Priority.ALWAYS );


        getChildren().add( mainPane );
    }


}
