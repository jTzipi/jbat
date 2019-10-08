package earth.eu.jtzipi.jbat.ui;

import earth.eu.jtzipi.jbat.JBatGlobal;
import earth.eu.jtzipi.jbat.ui.node.PathNodeFX;
import earth.eu.jtzipi.jbat.ui.table.PathNodeLengthTableCell;
import earth.eu.jtzipi.jbat.ui.table.PathNodeTableCell;
import earth.eu.jtzipi.jbat.ui.table.PathNodeTimeTableCell;
import earth.eu.jtzipi.modules.node.path.IPathNode;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
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
    private final ObjectProperty<IPathNode> fxPathNodeProp = new SimpleObjectProperty<>(this, "", null );

    private TableView<PathNodeFX> dirTabV;

    PathPane(final IPathNode pn ) {


        createPathPane( pn );
        JBatGlobal.FX_CURRENT_DIR_PATH.addListener( ( obs, op, np )  -> onPathChanged(op, np) );
        autosize();
    }

    private void createPathPane(IPathNode node) {

        dirTabV = createDirTableView( node );
        dirTabV.getSelectionModel().selectedItemProperty().addListener( (obs, oldPfx, newPfx ) -> onTablePathChanged( oldPfx, newPfx ) );

        setCenter( dirTabV );
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
        ComboBox<? extends IPathNode> searchCombB = new ComboBox<>();
        ProgressIndicator pi = new ProgressIndicator();

        optBox.getChildren().add( searchLabel );
        optBox.getChildren().add( searchCombB );


return optBox;
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
    enum DisplayType {

        DETAILED,    SIMPLE,
        THUMB;

    }

    final ObjectProperty<IPathNode> getFxPathNodeProp() {
        return this.fxPathNodeProp;
    }
}
