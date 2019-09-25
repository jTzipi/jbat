package earth.eu.jtzipi.jbat.ui.table;

import earth.eu.jtzipi.jbat.ui.node.PathNodeFX;
import earth.eu.jtzipi.modules.io.IOUtils;
import earth.eu.jtzipi.modules.node.path.IPathNode;
import javafx.scene.control.TableCell;

public class PathNodeTableCell extends TableCell<PathNodeFX, IPathNode> {

    @Override
    protected void updateItem( IPathNode item, boolean empty ) {
        super.updateItem( item, empty );

        if( null == item || empty ) {

           setText( "" );
           setGraphic( null );
        }

        else {
            setText( item.isDir() ? "["+ item.getName() + "]": IOUtils.getPathNameSafe( item.getValue() ) );

        }


    }
}
