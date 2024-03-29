package earth.eu.jtzipi.jbat.ui;

import earth.eu.jtzipi.jbat.JBatGlobal;
import earth.eu.jtzipi.jbat.ui.node.PathNodeFX;
import earth.eu.jtzipi.jbat.ui.table.*;
import earth.eu.jtzipi.modules.io.IOUtils;
import earth.eu.jtzipi.modules.node.path.IPathNode;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import org.controlsfx.control.StatusBar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.attribute.FileTime;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;


/**
 * Path Pane.
 *
 * @author jtzipi
 */
class PathPane extends BorderPane {


    private static final Logger LOG = LoggerFactory.getLogger( "Pathpane" );

    private List<PathNodeFX> cacheList;     // cache of directory
    private TableView<PathNodeFX> dirTabV;  // directory table
    private StatusBar pathStatBar;          // status

    // private BooleanProperty searchRunningProp = new SimpleBooleanProperty(this, ""


    PathPane( final IPathNode pn ) {


        createPathPane( pn );
        JBatGlobal.PATH_FILTER.getPathPredicatePropFX().addListener( ( obs, oldFilter, newFilter ) -> onFilterChanged( oldFilter, newFilter ) );
        JBatGlobal.FX_CURRENT_DIR_PATH.addListener( ( obs, op, np ) -> onPathChanged( op, np ) );
        //

    }


    private void createPathPane(IPathNode node) {

        dirTabV = createDirTableView( node );
        dirTabV.getSelectionModel().selectedItemProperty().addListener( ( obs, oldPfx, newPfx ) -> onTablePathChanged( oldPfx, newPfx ) );
        dirTabV.setOnDragDetected( evt -> onDragStarted( evt ) );
        onPathChanged( null, node );

        setCenter( dirTabV );

        pathStatBar = new StatusBar();
        setBottom( pathStatBar );
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

    private TableView<PathNodeFX> createDirTableView( final IPathNode path ) {

        TableView<PathNodeFX> tv = new TableView<>();
        ObservableList<PathNodeFX> list = FXCollections.observableList( PathNodeFX.createPathNodeFXList( path ) );

        tv.getItems().setAll( list );
        tv.setEditable( false );


        // Rectangle header = new Rectangle( 500, 50D );
        // header.setFill( Color.rgb( 77, 77, 254 ) );
        // new TableHeader( 500D, 46D )
        TableHeader fileHeader = TableHeader.of( "Path", 500D, 46D, true );

        fileHeader.setTranslateX( -3D );

        Comparator<IPathNode> fileComp = new FileTableComparator();
        // Path name
        TableColumn<PathNodeFX, IPathNode> nameTC = new TableColumn<>( "Path" );
        nameTC.setCellValueFactory( cb-> cb.getValue().getPathNodeProp() );
        nameTC.setCellFactory( cb -> new PathNodeTableCell() );
        nameTC.setPrefWidth( 500D );
        nameTC.setComparator( fileComp.thenComparing( IPathNode::isDir ).thenComparing( IPathNode::isReadable ).thenComparing( IPathNode::getName ) );
        nameTC.setGraphic( fileHeader );
        nameTC.sortTypeProperty().bind( fileHeader.fyTableColumnSortTypeProp() );

        TableHeader lenHeader = TableHeader.of( "Length", 100D, 46D, false );
        lenHeader.setTranslateX( -3D );
        // Path size
        TableColumn<PathNodeFX, Long> lengthTC = new TableColumn<>("Length");
        lengthTC.setCellValueFactory( new PropertyValueFactory<>( "length" ) );
        lengthTC.setCellFactory( cb -> new PathNodeLengthTableCell() );
        lengthTC.setPrefWidth( 100D );
        lengthTC.setGraphic( lenHeader );

        TableHeader typeHeader = TableHeader.of( "Type", 100D, 46D, false );
        typeHeader.setTranslateX( -3D );
        // Type
        TableColumn<PathNodeFX, FileTime> typeTC = new TableColumn<>("L.A.");
        typeTC.setCellValueFactory( new PropertyValueFactory<>( "type" ) );
        typeTC.setPrefWidth( 100D );
// Path last Access
        TableHeader createdHeader = TableHeader.of( "", 100D, 46D, false );
        createdHeader.setTranslateX( -3D );

        // Path created
        TableColumn<PathNodeFX, FileTime> createdTC = new TableColumn<>("Created");
        createdTC.setCellValueFactory( new PropertyValueFactory<>( "created" ) );
        createdTC.setCellFactory( callback -> new PathNodeTimeTableCell() );
        createdTC.setPrefWidth( 100D );
        createdTC.setGraphic( createdHeader );

        TableHeader extHeader = TableHeader.of( "Ext", 77D, 46D, false );
        extHeader.setTranslateX( -3D );
        // Path extension
        TableColumn<PathNodeFX, String> extTC = new TableColumn<>( "Ext" );
        extTC.setCellValueFactory( new PropertyValueFactory<>( "extension" ) );
        extTC.setCellFactory( callback -> new PathNodeTypeTableCell() );
        extTC.setGraphic( extHeader );
        extTC.setPrefWidth( 77D );

        TableHeader placeHeader = TableHeader.of( "", 1000D, 46D, false );
        placeHeader.setTranslateX( -3D );
        TableColumn<PathNodeFX, String> placeholder = new TableColumn<>( "placeholder" );
        placeholder.setPrefWidth( 1000 );
        placeholder.setCellFactory( callback -> new PathNodePlaceholderTableCell() );
        placeholder.setGraphic( placeHeader );
        tv.getColumns().addAll( nameTC, extTC, lengthTC, createdTC, placeholder );


        tv.getSortOrder().setAll( nameTC );
        return tv;
    }

    private void onFilterChanged( Predicate<IPathNode> oldFilter, Predicate<IPathNode> newFilter ) {
        // filter cached items
        List<PathNodeFX> filterL = cacheList.stream().filter( fxp -> newFilter.test( fxp.getPathNodeProp().getValue() ) ).collect( toList() );
        dirTabV.getItems().setAll( filterL );

    }

    private void onPathChanged( IPathNode oldPath, IPathNode newPath ) {

        if ( null == newPath || newPath == oldPath ) {

            return;
        }
        List<PathNodeFX> pathList = PathNodeFX.createPathNodeFXList( newPath );
        dirTabV.getItems().setAll( pathList );
        cacheList = pathList;

        dirTabV.getItems().setAll( PathNodeFX.createPathNodeFXList( newPath ) );
    }

    private void onDragStarted( MouseEvent mouseEvent ) {
        LOG.info( "Start D" );
        // selected nodes
        List<PathNodeFX> nodes = dirTabV.getSelectionModel().getSelectedItems();
        // filter for images
        List<File> pathNodeL = nodes.stream()
                .map( fxnode -> fxnode.getPathNodeProp().getValue().getValue() )
                .filter( IOUtils::isImage )
                .map( path -> path.toFile() )
                .collect( Collectors.toList() );
        // no images
        if ( pathNodeL.isEmpty() ) {
            return;
        }

        ClipboardContent cb = new ClipboardContent();

        cb.putFiles( pathNodeL );

        Dragboard db = dirTabV.startDragAndDrop( TransferMode.COPY );
        db.setContent( cb );


        mouseEvent.consume();

    }

    private void dirSummary() {

        IPathNode pn = JBatGlobal.FX_CURRENT_DIR_PATH.getValue();
        int dir = pn.getSubnodes( Files::isDirectory ).size();
        int img = pn.getSubnodes( IOUtils::isImage ).size();

    }

    private static final class FileTableComparator implements Comparator<IPathNode> {

        @Override
        public int compare( IPathNode pathNode, IPathNode nodeTwo ) {
            if ( pathNode.equals( PathNodeFX.PATH_LEVEL_UP ) ) {
                return -1;
            } else if ( nodeTwo.equals( PathNodeFX.PATH_LEVEL_UP ) ) {
                return 1;
            } else {
                return pathNode.getValue().compareTo( nodeTwo.getValue() );
            }
        }
    }
}
