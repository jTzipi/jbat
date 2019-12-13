package earth.eu.jtzipi.jbat.ui;

import earth.eu.jtzipi.jbat.JBatGlobal;
import earth.eu.jtzipi.modules.fx.RoundLabel;
import javafx.geometry.Insets;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import org.controlsfx.control.textfield.CustomTextField;

public class SearchPanel extends Pane {

    CustomTextField searchCTF;
    ChoiceBox<String> typeChoiceB;
    CheckBox regExCB;
    CheckBox casiCB;
    CheckBox wordBCB;

    RoundLabel updateRL;

    SearchPanel() {

        createSearchPanel();

    }

    private void createSearchPanel() {


        GridPane mainPane = new GridPane();

        Label aearchL = new Label( "search..." );
        Label typesL = new Label( "typ" );


        searchCTF = new CustomTextField();
        searchCTF.setPrefWidth( 500D );
        searchCTF.setEditable( true );
        searchCTF.textProperty().addListener( event -> JBatGlobal.PATH_FILTER.setFilterText( searchCTF.getText() ) );
        searchCTF.setPromptText( "...search" );
        typeChoiceB = new ChoiceBox<>();
        typeChoiceB.getItems().addAll( "File", "Ext", "Size" );


        regExCB = new CheckBox( "regExp" );
        regExCB.selectedProperty().addListener( ( obs, oldP, newP ) -> JBatGlobal.PATH_FILTER.setReg( newP ) );
        casiCB = new CheckBox( "case insensitive" );
        casiCB.selectedProperty().addListener( ( obs, oldP, newP ) -> JBatGlobal.PATH_FILTER.setCaseInsensitive( newP ) );
        wordBCB = new CheckBox( "word" );
        wordBCB.selectedProperty().addListener( ( obs, oldP, newP ) -> JBatGlobal.PATH_FILTER.setWordBound( newP ) );


        updateRL = new RoundLabel( "Update" );
        mainPane.add( aearchL, 0, 0, 1, 1 );
        mainPane.add( typesL, 0, 1 );
        mainPane.add( searchCTF, 1, 0, 5, 1 );
        mainPane.add( typeChoiceB, 1, 1 );
        mainPane.add( regExCB, 2, 1 );
        mainPane.add( casiCB, 3, 1 );
        mainPane.add( wordBCB, 4, 1 );
        mainPane.add( updateRL, 6, 0, 2, 2 );

        mainPane.setHgap( 5D );
        mainPane.setVgap( 2D );

        mainPane.setPadding( new Insets( 5D ) );
        GridPane.setHgrow( searchCTF, Priority.ALWAYS );

        getChildren().add( mainPane );


        setBackground( new Background( new BackgroundFill( Painter.COLOR_GRAY_47, CornerRadii.EMPTY, Insets.EMPTY ) ) );
    }


}
