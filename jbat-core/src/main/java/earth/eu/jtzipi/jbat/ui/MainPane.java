package earth.eu.jtzipi.jbat.ui;


import earth.eu.jtzipi.jbat.JBatGlobal;
import earth.eu.jtzipi.jbat.node.path.IPathNode;

import earth.eu.jtzipi.jbat.ui.node.PathNodeFX;
import earth.eu.jtzipi.jbat.ui.tree.PathNodeCell;
import earth.eu.jtzipi.jbat.ui.tree.PathNodeItem;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



import java.util.List;
import java.util.stream.Collectors;

public final class MainPane extends Pane {



    private static final Logger LOG = LoggerFactory.getLogger( "Pane" );

    private static final MainPane SINGLETON =  new MainPane();

    private TreeView<IPathNode> dirTreeV;
    private TableView<PathNodeFX> dirTabV;

    /**
     * Private.
     */
    private MainPane() {

        createMainPane();

        JBatGlobal.FX_CURRENT_DIR_PATH.addListener( (obs, op, np )  -> onPathChanged(op, np) );
    }


    public static MainPane getInstance() {

        return SINGLETON;
    }


    private void createMainPane() {
        BorderPane bp = new BorderPane();

        final IPathNode root = JBatGlobal.FX_CURRENT_DIR_PATH.getValue();
        dirTreeV = createDirTreeView( root );

        dirTreeV.setOnMouseClicked( event -> pathChanged( dirTreeV.getSelectionModel().getSelectedItem() ) );

        dirTabV = createDirTableView( root );

        bp.setLeft( dirTreeV );
        bp.setCenter( dirTabV );

        getChildren().add( 0, bp );
    }

    private void onPathChanged( IPathNode oldPath, IPathNode newPath ) {

        // null
        if( null == newPath ) {

            return;
        }
        if( oldPath == newPath) {


            return;
        }

        dirTabV.getItems().setAll( createTableNodes( newPath ) );
    }

    private void pathChanged( TreeItem<IPathNode> nodeItem ) {

        if( nodeItem == null ) {
            LOG.warn( "Node ist null" );
            return;
        }

        JBatGlobal.FX_CURRENT_DIR_PATH.setValue( nodeItem.getValue() );
    }

    private TableView<PathNodeFX> createDirTableView( final IPathNode path ) {

        TableView<PathNodeFX> tv = new TableView<>();

        tv.getItems().setAll( createTableNodes( path ) );

        TableColumn<PathNodeFX, String> nameTC = new TableColumn<>("File");
        nameTC.setCellValueFactory(new PropertyValueFactory("name") );

        TableColumn<PathNodeFX, String> lengthTC = new TableColumn<>("Length");
        lengthTC.setCellValueFactory( new PropertyValueFactory<>( "length" ) );

        tv.getColumns().add(nameTC);
        tv.getColumns().add( lengthTC );
        return tv;
    }
    private TreeView<IPathNode> createDirTreeView( final IPathNode rootPath ) {




        TreeItem<IPathNode> rootItem = PathNodeItem.of( rootPath );

        TreeView<IPathNode> view = new TreeView<>(rootItem);
        view.setCellFactory( (cb -> new PathNodeCell() ) );


            return view;
    }

    private ObservableList<PathNodeFX> createTableNodes( final IPathNode pathNode ) {

        LOG.info( "Create FX Pathnode " + pathNode );

        List<PathNodeFX> pnfx = pathNode.getSubnodes().stream().map( pnode -> PathNodeFX.of( pnode ) ).collect( Collectors.toList());
        return FXCollections.observableList( pnfx );
    }
}
