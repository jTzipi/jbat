package earth.eu.jtzipi.jbat.ui;

import earth.eu.jtzipi.modules.node.path.IPathNode;
import javafx.scene.control.ListCell;

public class PathNodeListCellRenderer extends ListCell<IPathNode> {


    @Override
    protected void updateItem( IPathNode item, boolean empty ) {
        super.updateItem( item, empty );

        if ( empty || null == item ) {
            setText( "" );
            setGraphic( null );
        } else {

            setText( item.getName() );
        }
    }
}
