package earth.eu.jtzipi.jbat.ui;

import earth.eu.jtzipi.jbat.ui.node.PathNodeFX;
import earth.eu.jtzipi.jbat.ui.task.SearchTask;
import earth.eu.jtzipi.modules.node.path.IPathNode;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableCell;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.layout.Pane;

import java.nio.file.Path;

public class SearchPane extends Pane {

    TreeTableView<PathNodeFX> ttv;
    private SearchTask searchTask;
    private BooleanProperty fxTaskRunningProp = new SimpleBooleanProperty( this, "", false );
    private ObjectProperty<Path> fxSearchPathProp = new SimpleObjectProperty<>( this, "", null );

    SearchPane( final Path dir ) {

    }

    private void createSearchPane() {

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
}
