package earth.eu.jtzipi.jbat.ui.tree;


import earth.eu.jtzipi.modules.io.IOUtils;
import earth.eu.jtzipi.modules.node.path.IPathNode;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;

import java.nio.file.Path;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Path Tree Node Item.
 *
 * @author jTz
 */
public class TreePathNodeItem extends TreeItem<IPathNode> {

    private boolean created;

    private ObjectProperty<Predicate<Path>> fxPathPredicateProp = new SimpleObjectProperty<>( this, "FX_PATH_FILTER_PROP", IOUtils.PATH_ACCEPT_ALL );

    TreePathNodeItem( final IPathNode pathNode ) {
        super(pathNode);
        this.created = false;
    }

    public static TreeItem<IPathNode> of( final IPathNode pn ) {
        Objects.requireNonNull(pn);

        return new TreePathNodeItem( pn );
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
            super.getChildren().setAll( pathNode.getSubnodes()
                    .stream()
                    .filter( IPathNode::isDir )
                    .map( pn -> TreePathNodeItem.of( pn ) )
                    .collect( Collectors.toList() ) );

            created = true;
        }


        return super.getChildren();
    }
}
