package earth.eu.jtzipi.jbat.ui.table;

import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import earth.eu.jtzipi.modules.node.path.IPathNode;
import javafx.scene.Node;


public class PathNodeTableCell extends PathNodeBasicTableCell<IPathNode> {


    private static MaterialDesignIconView iconForNode( IPathNode node ) {

        MaterialDesignIcon icon;
        if( node.isDir() ) {
             icon = node.isReadable() ? MaterialDesignIcon.FOLDER : MaterialDesignIcon.FOLDER_LOCK;

        } else {
            icon = node.isReadable() ?  MaterialDesignIcon.FILE_DOCUMENT : MaterialDesignIcon.FILE_LOCK;
        }

        MaterialDesignIconView view = new MaterialDesignIconView( icon  );
        view.setGlyphSize( 29D );

        return view;
    }

    @Override
    protected void updateItem( IPathNode item, boolean empty ) {
        super.updateItem( item, empty );

        String text;
        Node graphics;
        // Not valid
        if( null == item || empty ) {

            text = "";
            graphics = null;
        }

        else {

            text = item.isDir() ? "["+ item.getName() + "]":  item.getName();

            graphics = iconForNode( item );
        }

        setText(text);
        setGraphic( graphics );

    }
}
