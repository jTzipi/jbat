package earth.eu.jtzipi.jbat.ui.treetable;

import javafx.scene.control.TreeTableCell;

import java.nio.file.Path;

public class PathTypeTreeTableCell extends TreeTableCell<Path, String> {


    @Override
    protected void updateItem( String item, boolean empty ) {
        super.updateItem( item, empty );

        String text = null == item || empty ? "" : item;

        setText( text );
    }
}
