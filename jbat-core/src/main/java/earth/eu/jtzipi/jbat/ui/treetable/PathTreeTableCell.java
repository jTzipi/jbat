package earth.eu.jtzipi.jbat.ui.treetable;

import earth.eu.jtzipi.modules.io.IOUtils;
import javafx.scene.control.TreeTableCell;

import java.nio.file.Path;

public class PathTreeTableCell extends TreeTableCell<Path, Path> {


    public PathTreeTableCell() {

    }

    @Override
    protected void updateItem( Path item, boolean empty ) {
        super.updateItem( item, empty );

        String text = null == item || empty ? "" : IOUtils.getPathDisplayName( item );

        setText( text );
    }
}
