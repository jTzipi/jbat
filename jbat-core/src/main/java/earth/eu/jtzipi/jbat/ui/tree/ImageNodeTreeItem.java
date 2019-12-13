package earth.eu.jtzipi.jbat.ui.tree;

import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;


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

    public ImageNodeTreeItem addIfAbsent( final Path path ) {
        Objects.requireNonNull( path );


        ImageNodeTreeItem item;
        Optional<TreeItem<Path>> top = getChildren().stream().filter( ti -> ti.getValue().equals( path ) ).findAny();
        if ( !top.isPresent() ) {

            item = new ImageNodeTreeItem( path );
            getChildren().add( item );
        } else {
            item = ( ImageNodeTreeItem ) top.get();
        }

        return item;
    }

}
