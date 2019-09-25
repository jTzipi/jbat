package earth.eu.jtzipi.jbat.ui.node;

import earth.eu.jtzipi.modules.node.path.IPathNode;
import javafx.beans.property.*;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * PathNode Wrapper.
 * <p>This is a wrapper for {@linkplain IPathNode}</p>
 *
 */
public class PathNodeFX {

    // Level up path
    private static final PathNodeFX PATH_LEVEL_UP = new PathNodeFX();

    static  {
        PATH_LEVEL_UP.fxLengthProp.setValue( -2L );
        PATH_LEVEL_UP.fxExtProp.setValue( "" );
        PATH_LEVEL_UP.fxTypeProp.setValue( "" );
        PATH_LEVEL_UP.fxNameProp.setValue( "[..]" );
    }


    private StringProperty fxNameProp = new SimpleStringProperty(this, "FX_PATH_NAME_PROP", IPathNode.PATH_NOT_READABLE);
    private StringProperty fxTypeProp = new SimpleStringProperty(this, "FX_PATH_TYPE_PROP", "" );
    private StringProperty fxExtProp =  new SimpleStringProperty( this, "FX_PATH_EXT_PROP", "" );
    private LongProperty fxLengthProp = new SimpleLongProperty();
    private ReadOnlyObjectWrapper<IPathNode> fxPathNodeProp = new ReadOnlyObjectWrapper<>( this, "FX_PATH_NODE_PROP", null );

    PathNodeFX(  ) {

    }


    /**
     * Create a new instance of PathNodeFX .
     * @param pathNode
     * @return
     */
    public static PathNodeFX of( IPathNode pathNode ) {
        Objects.requireNonNull(pathNode);

        PathNodeFX pnfx = new PathNodeFX();
        pnfx.init( pathNode );

        return pnfx;
    }

    /**
     * Create list of pathnodefx.
     * @param node path node
     * @return
     */
    public static List<PathNodeFX> createPathNodeFXList( IPathNode  node ) {
        List<PathNodeFX> list = node.getSubnodes().stream().sorted().map( pnode -> PathNodeFX.of( pnode ) ).collect( Collectors.toList());
        list.add( 0, PATH_LEVEL_UP );
        return list;
    }

    public final StringProperty getNameProp() {
        return fxNameProp;
    }

    public final String getName() {
        return getNameProp().getValue();
    }

       public final LongProperty getLengthProp() {
        return fxLengthProp;
    }

    public long getLength() {
        return getLengthProp().getValue();
    }

    public String getType() {
        return fxTypeProp.getValue();
    }

    public final StringProperty getExtProp() {
        return this.fxExtProp;
    }

    public String getExtension() {
        return getExtProp().getValue();
    }

    public final ReadOnlyObjectProperty<IPathNode> getPathNodeProp() {
        return this.fxPathNodeProp.getReadOnlyProperty();
    }

    private void init( final IPathNode pn ) {
        this.fxNameProp.setValue( pn.getName() );
        boolean dir = pn.isDir();

        this.fxTypeProp.setValue( dir ? "[DIR]" : pn.getType() );
        this.fxExtProp.setValue( dir? "[DIR]" : "" );
        this.fxLengthProp.setValue( pn.getFileLength() );
        this.fxPathNodeProp.setValue( pn );
    }


}
