package earth.eu.jtzipi.jbat.ui.treetable;

import earth.eu.jtzipi.modules.io.image.ImageDimension;
import javafx.scene.control.TreeTableCell;

import java.nio.file.Path;

public class PathImageDimensionTreeTableCell extends TreeTableCell<Path, ImageDimension> {


    @Override
    protected void updateItem( ImageDimension item, boolean empty ) {
        super.updateItem( item, empty );
        String text;
        if ( null == item || empty ) {
            text = "";
        } else {
            text = item.getWidth() + " x " + item.getHeight();
        }

        setText( text );
    }
}
