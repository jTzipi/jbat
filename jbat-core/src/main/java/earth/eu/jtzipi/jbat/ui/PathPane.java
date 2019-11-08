package earth.eu.jtzipi.jbat.ui;

import earth.eu.jtzipi.jbat.JBatGlobal;
import earth.eu.jtzipi.jbat.ui.node.PathNodeFX;
import earth.eu.jtzipi.jbat.ui.table.PathNodeLengthTableCell;
import earth.eu.jtzipi.jbat.ui.table.PathNodeTableCell;
import earth.eu.jtzipi.jbat.ui.table.PathNodeTimeTableCell;
import earth.eu.jtzipi.jbat.ui.task.SearchTask;
import earth.eu.jtzipi.modules.io.IOUtils;
import earth.eu.jtzipi.modules.node.path.IPathNode;
import earth.eu.jtzipi.modules.node.path.RegularPathNode;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.DirectoryChooser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.attribute.FileTime;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutionException;


/**
 * Path Pane.
 *
 * @author jtzipi
 */
public class PathPane extends BorderPane {


    private static final Logger LOG = LoggerFactory.getLogger( "Pathpane" );

    private final ObjectProperty<IPathNode> fxPathNodeProp = new SimpleObjectProperty<>(this, "", null );

    private ComboBox<IPathNode> searchCombB;
    private TableView<PathNodeFX> dirTabV;
    private TabPane tabPane;
    private Button startSearchB;
    private Button cancelSearchB;
    private Button chooseDirB;

    private Set<IPathNode> cache = new HashSet<>();

    //private ExecutorService es = Executors.newSingleThreadExecutor(); // ;
    private SearchTask st;
    private BooleanProperty searchRunningProp = new SimpleBooleanProperty( this, "", false );


    PathPane(final IPathNode pn ) {


        createPathPane( pn );
        JBatGlobal.FX_CURRENT_DIR_PATH.addListener( ( obs, op, np )  -> onPathChanged(op, np) );
        // TODO:
        autosize();
    }

    private void createPathPane(IPathNode node) {

        dirTabV = createDirTableView( node );
        dirTabV.getSelectionModel().selectedItemProperty().addListener( (obs, oldPfx, newPfx ) -> onTablePathChanged( oldPfx, newPfx ) );

        Tab dirTab = new Tab( "Dir", dirTabV );
        dirTab.setClosable( false );


        tabPane = new TabPane( dirTab );
        setCenter( tabPane );
        setTop( createOptionPane() );
    }

    private void onTablePathChanged( PathNodeFX oldPfx, PathNodeFX newPfx ) {
        if( null != newPfx && oldPfx != newPfx ) {

            LOG.warn( "Path wird " + newPfx.getPathNodeProp().getValue().getValue() );
            JBatGlobal.FX_PATH_PROP.setValue( newPfx.getPathNodeProp().getValue() );
        }
    }

    private Node createOptionPane() {

        HBox optBox = new HBox(  );
        optBox.setSpacing( 4D );
        optBox.setPadding( new Insets( 2D ) );
        Label searchLabel = new Label("search...");
        searchLabel.setPrefWidth( 100 );

        searchCombB = new ComboBox<>();
        searchCombB.setCellFactory( cb -> new PathNodeListCellRenderer() );
        searchCombB.setButtonCell( new PathNodeListCellRenderer() );
        ProgressIndicator pi = new ProgressIndicator();

        chooseDirB = new Button( "Dir..." );
        startSearchB = new Button( "Start..." );
        cancelSearchB = new Button( "Cancel" );
        cancelSearchB.disableProperty().bind( searchRunningProp.not() );
        startSearchB.disableProperty().bind( searchRunningProp );
        chooseDirB.disableProperty().bind( searchRunningProp );

        startSearchB.setOnAction( ae -> startSearch( searchCombB.getValue() ) );
        chooseDirB.setOnAction( ae -> showDirChooser() );
        cancelSearchB.setOnAction( ae -> cancelSearch() );

        optBox.getChildren().add( searchLabel );
        optBox.getChildren().add( searchCombB );

        optBox.getChildren().add( chooseDirB );
        optBox.getChildren().add( startSearchB );
        optBox.getChildren().add( cancelSearchB );


return optBox;
    }


    private void showDirChooser() {
        DirectoryChooser dc = new DirectoryChooser();
        dc.setTitle( "Choose dir to search" );
        dc.setInitialDirectory( IOUtils.getHomeDir().toFile() );

        File file = dc.showDialog( JBatGlobal.MAIN_STAGE );
        if ( null != file ) {

            IPathNode dir = RegularPathNode.of( file.toPath(), null );
            if ( cache.add( dir ) ) {
                searchCombB.getItems().add( dir );
            }

        }
    }

    private void cancelSearch() {

        if ( null == st ) {

        }


        boolean canceled = st.cancel( true );

        if ( canceled ) {
            searchRunningProp.setValue( false );
        } else {
            Alert alert = new Alert( Alert.AlertType.WARNING );
            alert.setHeaderText( "Warning" );
            alert.setContentText( "Failed to cancel task" );

            alert.showAndWait();
        }
    }

    private void startSearch( IPathNode dirNode ) {

        if ( null == dirNode ) {

            LOG.error( "Fehler weil kein dirnode" );
            Alert alert = new Alert( Alert.AlertType.ERROR );
            alert.setHeaderText( "" );
            alert.setContentText( "Directory is null" );

            alert.showAndWait();
            return;
        }


        // get all sub dir of dirNode
        try {
            st = SearchTask.of( dirNode.getValue() );
            Tab searchTab = new Tab( "Search:" + dirNode.getName() );
            searchTab.setContent( createSearchTableView( dirNode ) );
            tabPane.getTabs().add( searchTab );
            searchRunningProp.setValue( true );
            JBatGlobal.SEARCH_EXE_SER.execute( st );

            st.setOnCancelled( eh -> onSearchOver() );
            st.setOnFailed( eh -> onSearchOver() );
            st.setOnSucceeded( eh -> onSearchOver() );


        } catch ( IOException ioE ) {

            Alert alert = new Alert( Alert.AlertType.ERROR );
            alert.setHeaderText( "" );
            alert.setContentText( "Fail" + ioE.getLocalizedMessage() );
        }

    }

    private void onSearchOver() {
        try {
            LOG.info( "Search Over " + st.get().size() );
        } catch ( InterruptedException e ) {
            e.printStackTrace();
        } catch ( ExecutionException e ) {
            e.printStackTrace();
        }
        searchRunningProp.setValue( false );

    }

    private TreeTableView<PathNodeFX> createSearchTableView( final IPathNode path ) {


        TreeItem<PathNodeFX> treeView = new TreeItem<>( PathNodeFX.of( path ) );
        TreeTableView<PathNodeFX> ttv = new TreeTableView<>( treeView );
        // Path name
        TreeTableColumn<PathNodeFX, IPathNode> nameTC = new TreeTableColumn<>( "File" );
        nameTC.setCellValueFactory( cb -> cb.getValue().getValue().getPathNodeProp() );
        nameTC.setCellFactory( cb -> new TreeTableCell<>() );
        nameTC.setPrefWidth( 500D );

        ttv.getColumns().add( nameTC );

        return ttv;
    }

    private TableView<PathNodeFX> createDirTableView( final IPathNode path ) {

        TableView<PathNodeFX> tv = new TableView<>();
        ObservableList<PathNodeFX> list = FXCollections.observableList( PathNodeFX.createPathNodeFXList( path ) );

        tv.getItems().setAll( list );

        // Path name
        TableColumn<PathNodeFX, IPathNode> nameTC = new TableColumn<>("File");
        nameTC.setCellValueFactory( cb-> cb.getValue().getPathNodeProp() );
        nameTC.setCellFactory( cb -> new PathNodeTableCell() );
        nameTC.setPrefWidth( 500D );

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

}
