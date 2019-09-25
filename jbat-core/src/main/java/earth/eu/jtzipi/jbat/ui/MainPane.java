package earth.eu.jtzipi.jbat.ui;

import earth.eu.jtzipi.jbat.JBatGlobal;
import earth.eu.jtzipi.jbat.ui.tree.PathNodeCell;
import earth.eu.jtzipi.jbat.ui.tree.PathNodeItem;
import earth.eu.jtzipi.modules.node.path.IPathNode;
import javafx.geometry.Orientation;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main Pane.
 *
 * @author jTzipi
 */
public final class MainPane extends Pane {

    private static final Logger LOG = LoggerFactory.getLogger( "Pane" );

    private static final MainPane SINGLETON =  new MainPane();

    private TreeView<IPathNode> dirTreeV;
    private PathPane pp;
    private BatchPathPane bpp;



    private SplitPane mainSplit;
    /**
     * Private.
     */
    private MainPane() {

        createMainPane();


    }


    public static MainPane getInstance() {

        return SINGLETON;
    }


    private void createMainPane() {
        setPrefWidth( JBatGlobal.WIDTH_DEF );

        BorderPane bp = new BorderPane();

        final IPathNode root = JBatGlobal.FX_CURRENT_DIR_PATH.getValue();
        dirTreeV = createDirTreeView( root );
        dirTreeV.setOnMouseClicked( event -> pathChanged( dirTreeV.getSelectionModel().getSelectedItem() ) );
        dirTreeV.prefHeightProperty().bind( prefHeightProperty() );

        pp = new PathPane( root );


        bpp = new BatchPathPane(  );

        mainSplit = new SplitPane( pp, bpp );
        mainSplit.setOrientation( Orientation.VERTICAL );
        mainSplit.setPrefHeight( JBatGlobal.HEIGHT_DEF );


        bp.setLeft( dirTreeV );
        bp.setCenter( mainSplit );

        getChildren().add( 0, bp );
    }


    private void pathChanged( TreeItem<IPathNode> nodeItem ) {

        if( nodeItem == null ) {
            LOG.warn( "Node ist null" );
            return;
        }
        LOG.info( "Create FX Pathnode " + nodeItem );
        JBatGlobal.FX_CURRENT_DIR_PATH.setValue( nodeItem.getValue() );
    }



    private TreeView<IPathNode> createDirTreeView( final IPathNode rootPath ) {


        TreeItem<IPathNode> rootItem = PathNodeItem.of( rootPath );

        TreeView<IPathNode> view = new TreeView<>(rootItem);
        view.setCellFactory( (cb -> new PathNodeCell() ) );


            return view;
    }



}
