package earth.eu.jtzipi.jbat.ui;

import earth.eu.jtzipi.jbat.JBatGlobal;
import earth.eu.jtzipi.jbat.ui.list.PathNodeListCellRenderer;
import earth.eu.jtzipi.jbat.ui.task.SearchCache;
import earth.eu.jtzipi.jbat.ui.tree.PathNodeTreeCell;
import earth.eu.jtzipi.jbat.ui.tree.TreePathNodeItem;
import earth.eu.jtzipi.modules.io.IOUtils;
import earth.eu.jtzipi.modules.node.path.IPathNode;
import earth.eu.jtzipi.modules.node.path.RegularPathNode;
import impl.org.controlsfx.skin.BreadCrumbBarSkin;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.stage.DirectoryChooser;
import org.controlsfx.control.BreadCrumbBar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * Main Pane.
 *
 * @author jTzipi
 */
public final class MainPane extends BorderPane {

    private static final Logger LOG = LoggerFactory.getLogger( "Pane" );
    private static final MainPane SINGLETON =  new MainPane();

    private TreeView<IPathNode> dirTreeV;


    private PathPane pp;
    private PreviewPane prevp;
    private TabPane pathTabPane;
    private ComboBox<IPathNode> searchCombB;    // store search path
    private Button startSearchB;                // start search
    private Button cancelSearchB;               // cancel search
    private Button chooseDirB;                  // choose dir
    private Button searchOptionB;               // search option
    private SplitPane mainSplit;
    private BreadCrumbBar<IPathNode> pathBCB;   // Breadcrumb Path
    /**
     * Private.
     */
    private MainPane() {

        createMainPane();

    }

    /**
     * Single MainPane.
     *
     * @return
     */
    public static MainPane getInstance() {


        return SINGLETON;
    }

    private void createMainPane() {
        setPrefWidth( JBatGlobal.WIDTH_DEF );
        setPrefHeight( JBatGlobal.HEIGHT_DEF );

        IPathNode root = JBatGlobal.FX_CURRENT_DIR_PATH.getValue();

        pp = new PathPane( root );
        TreeItem<IPathNode> rootItem = TreePathNodeItem.of( root );

        // TreeView<IPathNode> view = new TreeView<>(rootItem);

        dirTreeV = new TreeView<>( rootItem );
        // dirTreeV.setOnMouseClicked( event -> pathChanged( dirTreeV.getSelectionModel().getSelectedItem() ) );
        dirTreeV.prefHeightProperty().bind( prefHeightProperty() );
        dirTreeV.setCellFactory( ( cb -> new PathNodeTreeCell() ) );
        dirTreeV.getSelectionModel().selectedItemProperty().addListener( ( obs, oldItem, newItem ) -> {

            //
            if ( null != newItem && oldItem != newItem ) {
                pathChanged( newItem );
            }
        } );


        Tab pathTab = new Tab( "Folder", pp );
        pathTab.setClosable( false );
        pathTabPane = new TabPane( pathTab );

        prevp = new PreviewPane();

        Node optionPane = createOptionBar( rootItem );


        setLeft( dirTreeV );
        setCenter( pathTabPane );
        setTop( optionPane );
        setRight( prevp );

        // getChildren().add( 0, bp );
    }

    private void initListener() {
        JBatGlobal.FX_CURRENT_DIR_PATH.addListener( ( obs, oldPath, newPath ) -> {

        } );
    }

    private void pathChanged( TreeItem<IPathNode> nodeItem ) {

        if( nodeItem == null ) {
            LOG.warn( "Node ist null" );
            return;
        }
        // LOG.info( "Path Tree Item bei 'pathChanged' " + nodeItem );
        // set crumb
        pathBCB.setSelectedCrumb( nodeItem );
        // set changed path
        JBatGlobal.FX_CURRENT_DIR_PATH.setValue( nodeItem.getValue() );
    }


    private Node createOptionBar( TreeItem<IPathNode> rootItem ) {

        GridPane gridPane = new GridPane();

        FlowPane flowPane = new FlowPane();
        flowPane.setHgap( 3D );


        Label searchLabel = new Label( "search..." );
        searchLabel.setPrefWidth( 100 );

        searchCombB = new ComboBox<>();
        searchCombB.setCellFactory( cb -> new PathNodeListCellRenderer() );
        searchCombB.setButtonCell( new PathNodeListCellRenderer() );


        chooseDirB = new Button( "Dir..." );
        startSearchB = new Button( "Start" );
        cancelSearchB = new Button( "Cancel" );


        startSearchB.setOnAction( ae -> startSearch( searchCombB.getValue() ) );
        chooseDirB.setOnAction( ae -> showDirChooser() );
        cancelSearchB.setOnAction( ae -> cancelSearch() );

        flowPane.getChildren().add( searchLabel );
        flowPane.getChildren().add( searchCombB );
        flowPane.getChildren().add( chooseDirB );
        flowPane.getChildren().add( startSearchB );
        flowPane.getChildren().add( cancelSearchB );
        //optBox.getChildren().add( cancelSearchB );

        pathBCB = new BreadCrumbBar<>( rootItem );
        pathBCB.setAutoNavigationEnabled( false );
        pathBCB.setCrumbFactory( cb -> new BreadCrumbBarSkin.BreadCrumbButton( cb.getValue().getName() ) );
        pathBCB.setOnCrumbAction( evth -> pathChanged( evth.getSelectedCrumb() ) );

        gridPane.add( pathBCB, 0, 0 );
        gridPane.add( flowPane, 0, 1 );

        return gridPane;
    }

    private void startSearch( IPathNode pathNode ) {

    }

    private void cancelSearch() {

    }

    private void showDirChooser() {
        DirectoryChooser dc = new DirectoryChooser();
        dc.setTitle( "Choose dir to search" );
        dc.setInitialDirectory( IOUtils.getHomeDir().toFile() );

        File file = dc.showDialog( JBatGlobal.MAIN_STAGE );

        if ( null != file ) {

            if ( !SearchCache.isTask( file.toPath() ) ) {
                IPathNode dir = RegularPathNode.of( file.toPath(), null );
                searchCombB.getItems().add( dir );
                searchCombB.getSelectionModel().select( dir );
            }

        }
    }

}
