package earth.eu.jtzipi.jbat.ui.node;

import earth.eu.jtzipi.jbat.node.path.IOUtils;
import earth.eu.jtzipi.jbat.node.path.IPathNode;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.Objects;

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


}
