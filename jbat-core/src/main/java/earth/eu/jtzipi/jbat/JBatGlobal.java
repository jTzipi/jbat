package earth.eu.jtzipi.jbat;

import earth.eu.jtzipi.jbat.node.path.IPathNode;
import earth.eu.jtzipi.jbat.node.path.PathNode;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.nio.file.Path;
import java.nio.file.Paths;

public final class JBatGlobal {
    /**
     * Directory currently viewed.
     */
    public static final ObjectProperty<IPathNode> FX_CURRENT_DIR_PATH = new SimpleObjectProperty<>();

    static {

        FX_CURRENT_DIR_PATH.setValue( PathNode.of( Paths.get("C:/"), null) );
    }

    private JBatGlobal() {

    }
}
