package earth.eu.jtzipi.jbat.ui.tree;

import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;


public class ImageNodeTreeItem extends TreeItem<Path> {
    private static final Logger Log = LoggerFactory.getLogger( "TPNI" );


    ImageNodeTreeItem( final Path node ) {
        super( node );

    }

    public static ImageNodeTreeItem of( final Path pn ) {
        Objects.requireNonNull( pn );
        return new ImageNodeTreeItem( pn );
    }

    @Override
    public boolean isLeaf() {
        return !Files.isDirectory( getValue() );
    }

    @Override
    public ObservableList<TreeItem<Path>> getChildren() {

        return super.getChildren();
    }

    public void addIfAbesent( TreeItem<Path> item ) {
        // item already?
        if ( getChildren().stream().map( TreeItem::getValue ).noneMatch( path -> path.compareTo( item.getValue() ) == 0 ) ) {
            getChildren().add( item );

        }
    }

}
