package earth.eu.jtzipi.jbat.ui.node;

import earth.eu.jtzipi.modules.io.IOUtils;
import earth.eu.jtzipi.modules.node.path.IPathNode;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class PathNodeFX {

    private IPathNode pn;

    private StringProperty fxNameProp = new SimpleStringProperty(this, "FX_PATH_NAME_PROP", IPathNode.PATH_NOT_READABLE);
    private StringProperty fxTypeProp = new SimpleStringProperty(this, "FX_PATH_TYPE_PROP", "" );
    private StringProperty fxLengthProp = new SimpleStringProperty();


    PathNodeFX( final IPathNode pathNode ) {
    this.pn = pathNode;
    }

    public static PathNodeFX of( IPathNode pathNode ) {
        Objects.requireNonNull(pathNode);

        PathNodeFX pnfx = new PathNodeFX(pathNode);
        pnfx.init( pathNode );

        return pnfx;
    }


    public final StringProperty fxNameProp() {
        return fxNameProp;
    }

    public final String getName() {
        return fxNameProp().getValue();
    }

       public final StringProperty fxLengthProp() {
        return fxLengthProp;
    }

    public String getLength() {
        return fxLengthProp().getValue();
    }

    private void init( final IPathNode pn ) {
        this.fxNameProp.setValue( pn.getName() );
        this.fxTypeProp.setValue( pn.getType() );
        this.fxLengthProp.setValue( IOUtils.formatFileSize( pn.getFileLength(), true ) );
    }

    /**
     * Create list of pathnodefx.
     * @param node path node
     * @return
     */
    public static List<PathNodeFX> createPathNodeFXList( IPathNode  node ) {
        return node.getSubnodes().stream().map( pnode -> PathNodeFX.of( pnode ) ).collect( Collectors.toList());
    }
}
