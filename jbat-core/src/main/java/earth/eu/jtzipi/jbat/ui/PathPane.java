package earth.eu.jtzipi.jbat.ui;

import earth.eu.jtzipi.jbat.JBatGlobal;
import earth.eu.jtzipi.jbat.ui.node.PathNodeFX;
import earth.eu.jtzipi.jbat.ui.table.PathNodeLengthTableCell;
import earth.eu.jtzipi.jbat.ui.table.PathNodeTableCell;
import earth.eu.jtzipi.jbat.ui.table.PathNodeTimeTableCell;
import earth.eu.jtzipi.jbat.ui.table.TableHeader;
import earth.eu.jtzipi.modules.node.path.IPathNode;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.attribute.FileTime;


/**
 * Path Pane.
 *
 * @author jtzipi
 */
public class PathPane extends BorderPane {


    private static final Logger LOG = LoggerFactory.getLogger( "Pathpane" );

    private TableView<PathNodeFX> dirTabV;
//    private Button startSearchB;
//    private Button cancelSearchB;
//    private Button chooseDirB;


    // private BooleanProperty searchRunningProp = new SimpleBooleanProperty(this, ""


    PathPane(final IPathNode pn ) {


        createPathPane( pn );
        JBatGlobal.FX_CURRENT_DIR_PATH.addListener( ( obs, op, np )  -> onPathChanged(op, np) );
        // TODO:
        // autosize();
    }

    private void createPathPane(IPathNode node) {

        dirTabV = createDirTableView( node );
        dirTabV.getSelectionModel().selectedItemProperty().addListener( (obs, oldPfx, newPfx ) -> onTablePathChanged( oldPfx, newPfx ) );

        setCenter( dirTabV );

    }

    private void onTablePathChanged( PathNodeFX oldPfx, PathNodeFX newPfx ) {
        if( null != newPfx && oldPfx != newPfx ) {

            LOG.warn( "Path wird " + newPfx.getPathNodeProp().getValue().getValue() );
            JBatGlobal.FX_PATH_PROP.setValue( newPfx.getPathNodeProp().getValue() );
        }
    }

//    private Node createOptionPane() {
//
//        HBox optBox = new HBox(  );
//        optBox.setSpacing( 4D );
//        optBox.setPadding( new Insets( 2D ) );
//        Label searchLabel = new Label("search...");
//        searchLabel.setPrefWidth( 100 );
//
//
//
//        ProgressIndicator pi = new ProgressIndicator();
//
//        chooseDirB = new Button("Dir...");
//        startSearchB = new Button( "Start..." );
//        cancelSearchB = new Button( "Cancel" );
//        cancelSearchB.disableProperty().bind( searchRunningProp.not() );
//        startSearchB.disableProperty().bind( searchRunningProp );
//        chooseDirB.disableProperty().bind( searchRunningProp );
//
//        startSearchB.setOnAction( actionEvent -> startSearch( searchCombB.getValue() ) );
//        chooseDirB.setOnAction( ae -> showDirChooser() );
//
//        optBox.getChildren().add( searchLabel );
//        optBox.getChildren().add( searchCombB );
//
//        optBox.getChildren().add( chooseDirB );
//        optBox.getChildren().add( startSearchB );
//        optBox.getChildren().add( cancelSearchB );
//
//
//return optBox;
//    }


    private Rectangle createGradient() {
        Color COLOR_BG_TOP = Color.rgb(
                60, 60, 60 );
        Color COLOR_BG_TOP_DOWN = Color.rgb(
                45, 45, 45 );
        Color COLOR_BOTTOM_UP = Color.rgb(
                30, 30, 30 );
        Color COLOR_BG_BOTTOM = Color.rgb(
                10, 10, 10 );

        Stop bgTop = new Stop( 0D, COLOR_BG_TOP );

        Stop bgTopD = new Stop( 0.21D, COLOR_BG_TOP_DOWN );

        Stop bgBottomUp = new Stop( 0.75D, COLOR_BOTTOM_UP );

        Stop bgBottom = new Stop( 1D, COLOR_BG_BOTTOM );

        Rectangle header = new Rectangle( 500, 77 );
        header.setFill( new LinearGradient( 0, 0, 0, 1, true, CycleMethod.NO_CYCLE, bgTop, bgTopD, bgBottomUp, bgBottom ) );


        return header;
    }

    private void showDirChooser() {

    }


    private void startSearch( IPathNode dirNode ) {



        // get all sub dir of dirNode
//        try {
//            st = SearchTask.of( dirNode.getValue() );
//            searchRunningProp.setValue( true );
//        } catch ( IOException ioE ) {
//
//        }
//
//
//        es.execute( st );
//
//        st.setOnCancelled( eh -> es.shutdown() );
//        st.setOnFailed( eh -> es.shutdown() );
//        st.setOnSucceeded( eh -> es.shutdown() );



    }

    private TableView<PathNodeFX> createDirTableView( final IPathNode path ) {

        TableView<PathNodeFX> tv = new TableView<>();
        ObservableList<PathNodeFX> list = FXCollections.observableList( PathNodeFX.createPathNodeFXList( path ) );

        tv.getItems().setAll( list );

        Rectangle header = new Rectangle( 500, 50D );
        header.setFill( Color.rgb( 77, 77, 254 ) );
        // Path name
        TableColumn<PathNodeFX, IPathNode> nameTC = new TableColumn<>("File");
        nameTC.setCellValueFactory( cb-> cb.getValue().getPathNodeProp() );
        nameTC.setCellFactory( cb -> new PathNodeTableCell() );
        nameTC.setPrefWidth( 500D );
        nameTC.setGraphic( new TableHeader( "File", 500D, 46D ) );
        // Path size
        TableColumn<PathNodeFX, Long> lengthTC = new TableColumn<>("Length");
        lengthTC.setCellValueFactory( new PropertyValueFactory<>( "length" ) );
        lengthTC.setCellFactory( cb -> new PathNodeLengthTableCell() );
        lengthTC.setPrefWidth( 100D );

        // Path last Access
        TableColumn<PathNodeFX, FileTime> typeTC = new TableColumn<>("L.A.");
        typeTC.setCellValueFactory( new PropertyValueFactory<>( "type" ) );
        typeTC.setPrefWidth( 100D );

        // Path created
        TableColumn<PathNodeFX, FileTime> createdTC = new TableColumn<>("Created");
        createdTC.setCellValueFactory( new PropertyValueFactory<>( "created" ) );
        createdTC.setCellFactory( callback -> new PathNodeTimeTableCell() );
        createdTC.setPrefWidth( 100D );

        // Path extension
        TableColumn<PathNodeFX, String> pathExtTC =  new TableColumn<>("Ext");
        pathExtTC.setCellValueFactory( new PropertyValueFactory<>( "extension" ) );

        tv.getColumns().add(nameTC);
        tv.getColumns().add(pathExtTC);
        tv.getColumns().add( lengthTC );
        tv.getColumns().add( createdTC );
        return tv;
    }

    private void onPathChanged( IPathNode oldPath, IPathNode newPath ) {

        if( null == newPath || newPath == oldPath ) {

            return;
        }

        dirTabV.getItems().setAll( PathNodeFX.createPathNodeFXList( newPath ) );
    }

    enum DisplayType {

        DETAILED, SIMPLE,
        THUMB;

    }

//    final ObjectProperty<IPathNode> getFxPathNodeProp() {
//        return this.fxPathNodeProp;
//    }

}
