package earth.eu.jtzipi.jbat.ui.tree;

import earth.eu.jtzipi.modules.io.IOUtils;
import javafx.scene.control.TreeCell;
import javafx.scene.image.ImageView;

import java.nio.file.Path;

public class ImagePathTreeCell extends TreeCell<Path> {


    String text;
    ImageView iconView;

    ImagePathTreeCell() {

    }


    @Override
    protected void updateItem( Path item, boolean empty ) {
        super.updateItem( item, empty );

        if ( null == item || empty ) {
            text = "";
            iconView = null;
        } else {
            text = IOUtils.getPathNameSafe( item );
            iconView = null;
        }


        setText( text );
    }
}
