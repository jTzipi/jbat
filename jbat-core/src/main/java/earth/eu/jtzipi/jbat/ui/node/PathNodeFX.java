package earth.eu.jtzipi.jbat.ui.node;

import earth.eu.jtzipi.modules.node.path.IPathNode;
import earth.eu.jtzipi.modules.node.path.RegularPathNode;
import javafx.beans.property.*;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * IPath Node Wrapper.
 * <p>This is a wrapper for {@linkplain IPathNode}</p>
 */
public class PathNodeFX {

    public static final Path PATH_LEVEL_UP = Paths.get( "../" );

    // Level up path
    private static final PathNodeFX PATH_LEVEL_UP_FX = new PathNodeFX();

    static {
        PATH_LEVEL_UP_FX.fxLengthProp.setValue( -2L );
        PATH_LEVEL_UP_FX.fxPathNodeProp.setValue( RegularPathNode.of( Paths.get( "../" ), null ) );
        PATH_LEVEL_UP_FX.fxExtProp.setValue( "" );
        PATH_LEVEL_UP_FX.fxTypeProp.setValue( "" );
        PATH_LEVEL_UP_FX.fxNameProp.setValue( "[..]" );
        PATH_LEVEL_UP_FX.fxCreatedFileTimeProp.setValue( FileTime.fromMillis( 0L ) );
    }


    private StringProperty fxNameProp = new SimpleStringProperty(this, "FX_PATH_NAME_PROP", IPathNode.PATH_NOT_READABLE);
    private StringProperty fxTypeProp = new SimpleStringProperty(this, "FX_PATH_TYPE_PROP", "" );
    private StringProperty fxExtProp =  new SimpleStringProperty( this, "FX_PATH_EXT_PROP", "" );
    private LongProperty fxLengthProp = new SimpleLongProperty();
    private ObjectProperty<FileTime> fxLastAccFileTimeProp = new SimpleObjectProperty<>();
    private ObjectProperty<FileTime> fxLastModFileTimeProp =  new SimpleObjectProperty<>();
    private ObjectProperty<FileTime> fxCreatedFileTimeProp = new SimpleObjectProperty<>();
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
        list.add( 0, PATH_LEVEL_UP_FX );
        return list;
    }

    /**
     * Path name property.
     * @return property of name
     */
    public final StringProperty getNameProp() {
        return fxNameProp;
    }

    /**
     * Name of path.
     * @return name of path
     */
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

    public FileTime getCreated() {
        return this.fxCreatedFileTimeProp.getValue();
    }


    public String getPlaceholder() {
        return "";
    }
    private void init( final IPathNode pn ) {
        this.fxNameProp.setValue( pn.getName() );
        boolean dir = pn.isDir();

        this.fxTypeProp.setValue( dir ? "[DIR]" : pn.getType() );
        this.fxExtProp.setValue( dir? "[DIR]" : "" );
        this.fxLengthProp.setValue( pn.getFileLength() );
        this.fxPathNodeProp.setValue( pn );
        this.fxCreatedFileTimeProp.setValue( pn.getCreated().orElse( FileTime.fromMillis( 0L ) ) );
    }


}
