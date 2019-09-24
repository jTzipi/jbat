package earth.eu.jtzipi.jbat;

import earth.eu.jtzipi.modules.node.path.IPathNode;
import earth.eu.jtzipi.modules.node.path.PathNode;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.nio.file.Paths;

public final class JBatGlobal {

    public static final double WIDTH_DEF = 1200D;
    public static final double HEIGHT_DEF = 770D;
    /**
     * Directory currently viewed.
     */
    public static final ObjectProperty<IPathNode> FX_CURRENT_DIR_PATH = new SimpleObjectProperty<>();

    public static final DoubleProperty FX_WIDTH_PROP = new SimpleDoubleProperty(WIDTH_DEF);
    public static final DoubleProperty FX_HEIGHT_PROP = new SimpleDoubleProperty(HEIGHT_DEF);


    public static final  DoubleBinding FX_PREF_WIDTH_MAIN_75 = FX_HEIGHT_PROP.multiply( 0.78D );
    static {

        FX_CURRENT_DIR_PATH.setValue( PathNode.of( Paths.get("C:/"), null) );
    }

    private JBatGlobal() {

    }
}
