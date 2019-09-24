package earth.eu.jtzipi.jbat.ui;

import earth.eu.jtzipi.jbat.JBatGlobal;
import earth.eu.jtzipi.jbat.ui.node.PathNodeFX;
import earth.eu.jtzipi.modules.node.path.IPathNode;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;

public class PathPane extends Pane {

    private final ObjectProperty<IPathNode> fxPathNodeProp = new SimpleObjectProperty<>(this, "", null );

    private TableView<PathNodeFX> dirTabV;

    PathPane(final IPathNode pn ) {


        createPathPane( pn );
        JBatGlobal.FX_CURRENT_DIR_PATH.addListener( ( obs, op, np )  -> onPathChanged(op, np) );
    }

    private void createPathPane(IPathNode node) {


        dirTabV = createDirTableView( node );


        getChildren().addAll( dirTabV );
    }

    private void onPathChanged( IPathNode oldPath, IPathNode newPath ) {

        if( null == newPath || newPath == oldPath ) {

            return;
        }

        dirTabV.getItems().setAll( PathNodeFX.createPathNodeFXList( newPath ) );
    }
    private TableView<PathNodeFX> createDirTableView( final IPathNode path ) {

        TableView<PathNodeFX> tv = new TableView<>();
        ObservableList<PathNodeFX> list = FXCollections.observableList( PathNodeFX.createPathNodeFXList( path ) );
        tv.getItems().setAll( list );

        TableColumn<PathNodeFX, String> nameTC = new TableColumn<>("File");
        nameTC.setCellValueFactory(new PropertyValueFactory("name") );

        TableColumn<PathNodeFX, String> lengthTC = new TableColumn<>("Length");
        lengthTC.setCellValueFactory( new PropertyValueFactory<>( "length" ) );

        tv.getColumns().add(nameTC);
        tv.getColumns().add( lengthTC );
        return tv;
    }

    final ObjectProperty<IPathNode> getFxPathNodeProp() {
        return this.fxPathNodeProp;
    }
}
