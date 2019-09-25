package earth.eu.jtzipi.jbat.ui;

import earth.eu.jtzipi.jbat.JBatGlobal;
import earth.eu.jtzipi.jbat.ui.node.PathNodeFX;
import earth.eu.jtzipi.jbat.ui.table.PathNodeLengthTableCell;
import earth.eu.jtzipi.jbat.ui.table.PathNodeTableCell;
import earth.eu.jtzipi.modules.node.path.IPathNode;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;


public class PathPane extends BorderPane {

    private final ObjectProperty<IPathNode> fxPathNodeProp = new SimpleObjectProperty<>(this, "", null );

    private TableView<PathNodeFX> dirTabV;

    PathPane(final IPathNode pn ) {


        createPathPane( pn );
        JBatGlobal.FX_CURRENT_DIR_PATH.addListener( ( obs, op, np )  -> onPathChanged(op, np) );
        autosize();
    }

    private void createPathPane(IPathNode node) {


        dirTabV = createDirTableView( node );

        setCenter( dirTabV );
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

        TableColumn<PathNodeFX, IPathNode> nameTC = new TableColumn<>("File");
        nameTC.setCellValueFactory( cb-> cb.getValue().getPathNodeProp() );
        nameTC.setCellFactory( cb -> new PathNodeTableCell() );

        nameTC.setPrefWidth( 500D );
        TableColumn<PathNodeFX, Long> lengthTC = new TableColumn<>("Length");
        lengthTC.setCellValueFactory( new PropertyValueFactory<>( "length" ) );
        lengthTC.setCellFactory( cb -> new PathNodeLengthTableCell() );
        lengthTC.setPrefWidth( 100D );

        TableColumn<PathNodeFX, String> typeTC = new TableColumn<>("Type");
        typeTC.setCellValueFactory( new PropertyValueFactory<>( "type" ) );
        typeTC.setPrefWidth( 200D );

            TableColumn<PathNodeFX, String> pathExtTC =  new TableColumn<>("Ext");
pathExtTC.setCellValueFactory( new PropertyValueFactory<>( "extension" ) );

        tv.getColumns().add(nameTC);
        tv.getColumns().add(pathExtTC);
        tv.getColumns().add( lengthTC );
        return tv;
    }

    final ObjectProperty<IPathNode> getFxPathNodeProp() {
        return this.fxPathNodeProp;
    }
}
