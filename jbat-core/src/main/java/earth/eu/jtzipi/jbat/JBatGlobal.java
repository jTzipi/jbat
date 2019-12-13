package earth.eu.jtzipi.jbat;

import earth.eu.jtzipi.jbat.ui.IconStyle;
import earth.eu.jtzipi.modules.io.IOUtils;
import earth.eu.jtzipi.modules.io.image.ImageDimension;
import earth.eu.jtzipi.modules.node.path.IPathNode;
import earth.eu.jtzipi.modules.node.path.RegularPathNode;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.nio.file.Path;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Global Properties.
 * @author jtzipi
 */
public final class JBatGlobal {


    /**
     * Default width.
     */
    public static final double WIDTH_DEF = 1200D;
    /**
     * Default height.
     */
    public static final double HEIGHT_DEF = 770D;
    /**
     * Directory currently viewed.
     */
    public static final ObjectProperty<IPathNode> FX_CURRENT_DIR_PATH = new SimpleObjectProperty<>();

    public static final ObjectProperty<IPathNode> FX_PATH_PROP = new SimpleObjectProperty<>();

    public static final DoubleProperty FX_WIDTH_PROP = new SimpleDoubleProperty(WIDTH_DEF);

    public static final DoubleProperty FX_HEIGHT_PROP = new SimpleDoubleProperty(HEIGHT_DEF);

    public static final DoubleBinding FX_PREF_WIDTH_MAIN_75 = FX_HEIGHT_PROP.multiply( 0.78D );

    public static final ObjectProperty<IconStyle> FX_ICON_STYLE_PROP = new SimpleObjectProperty<>( IconStyle.BUUF );

    public static final ObjectProperty<Font> FX_FONT_MAIN_PROP = new SimpleObjectProperty<>();
    /**
     * Single main stage.
     */
    public static Stage MAIN_STAGE;

    public static ImagePathFilter PATH_FILTER = new ImagePathFilter();
    /**
     * search task executor.
     */
    public static ExecutorService SEARCH_EXE_SER = Executors.newFixedThreadPool( Runtime.getRuntime().availableProcessors() );
    /**
     * Cache dimension of images.
     */
    public static WeakHashMap<Path, ImageDimension> IMAGE_DIM_CACHE = new WeakHashMap<>(); // cache for image dimension

    static {

        FX_CURRENT_DIR_PATH.setValue( RegularPathNode.of( IOUtils.getHomeDir(), null ) );
        FX_FONT_MAIN_PROP.setValue( Font.font( 18D ) );
    }

    private JBatGlobal() {

    }
}
