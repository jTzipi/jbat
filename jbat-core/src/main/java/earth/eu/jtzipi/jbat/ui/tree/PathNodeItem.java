package earth.eu.jtzipi.jbat.ui.tree;

import earth.eu.jtzipi.modules.node.path.IPathNode;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;

import java.nio.file.Files;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Path Tree Node Item.
 *
 * @author jTz
 */
public class PathNodeItem extends TreeItem<IPathNode> {
private boolean created;

    PathNodeItem( final IPathNode pathNode ) {
        super(pathNode);
        this.created = false;
    }

    public static TreeItem<IPathNode> of( final IPathNode pn ) {
        Objects.requireNonNull(pn);

        return new PathNodeItem( pn );
    }

    @Override
    public boolean isLeaf() {
        return getValue().isLeaf();
    }

    @Override
    public ObservableList<TreeItem<IPathNode>> getChildren() {
        IPathNode pathNode = getValue();

        if( null == pathNode || pathNode.isLeaf() ) {
            return FXCollections.emptyObservableList();
        }

        if( !created ) {
            super.getChildren().setAll( pathNode.getSubnodes( Files::isDirectory )
                    .stream()
                    .map( pn -> PathNodeItem.of( pn ) )
                    .collect( Collectors.toList() ) );

            created = true;
        }


        return super.getChildren();
    }
}
