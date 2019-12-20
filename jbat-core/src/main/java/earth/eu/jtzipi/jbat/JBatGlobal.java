package earth.eu.jtzipi.jbat;

import earth.eu.jtzipi.jbat.ui.IconStyle;
import earth.eu.jtzipi.jbat.ui.tree.PathNodeTreeItem;
import earth.eu.jtzipi.modules.io.IOUtils;
import earth.eu.jtzipi.modules.io.image.ImageDimension;
import earth.eu.jtzipi.modules.io.watcher.IWatchEventHandler;
import earth.eu.jtzipi.modules.io.watcher.IWatchTask;
import earth.eu.jtzipi.modules.io.watcher.Watcher;
import earth.eu.jtzipi.modules.node.path.IPathNode;
import earth.eu.jtzipi.modules.node.path.RegularPathNode;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.TreeItem;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.WatchService;
import java.util.WeakHashMap;
import java.util.concurrent.*;


/**
 * Global Properties.
 *
 * @author jtzipi
 */
public final class JBatGlobal {

    /**
     * Selected path.
     */
    public static final ObjectProperty<IPathNode> FX_PATH_PROP = new SimpleObjectProperty<>();
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
    /**
     * Width of this.
     */
    public static final DoubleProperty FX_WIDTH_PROP = new SimpleDoubleProperty( WIDTH_DEF );
    /**
     * Height Property.
     */
    public static final DoubleProperty FX_HEIGHT_PROP = new SimpleDoubleProperty( HEIGHT_DEF );
    /**
     * Common width.
     */
    public static final DoubleBinding FX_PREF_WIDTH_MAIN_75 = FX_HEIGHT_PROP.multiply( 0.78D );
    /**
     * Icon Style property.
     */
    public static final ObjectProperty<IconStyle> FX_ICON_STYLE_PROP = new SimpleObjectProperty<>( IconStyle.BUUF );
    /**
     * Font for main ui.
     */
    public static final ObjectProperty<Font> FX_FONT_MAIN_PROP = new SimpleObjectProperty<>();
    /**
     * Path filter.
     */
    public static final ImagePathFilter PATH_FILTER = new ImagePathFilter();
    /**
     * Single main stage.
     */
    public static Stage MAIN_STAGE;
    /**
     * search task executor.
     */
    public static final ExecutorService SEARCH_EXE_SER = Executors.newFixedThreadPool( Runtime.getRuntime().availableProcessors() );
    /**
     * Cache dimension of images.
     */
    public static final WeakHashMap<Path, ImageDimension> IMAGE_DIM_CACHE = new WeakHashMap<>(); // cache for image dimension
    /**
     * Root path.
     */
    public static final Path ROOT_PATH = IOUtils.getHomeDir();
    /**
     * Tree Item Root node.
     */
    public static final TreeItem<IPathNode> ROOT_PATH_NODE;
    /**
     * Root path node.
     */
    public static final IPathNode ROOT_NODE;
    static final Logger GLOBLOG = LoggerFactory.getLogger( "JBatGlobal" );
    //
    //
    // Cache for visited folders and their tree item
    // This is important to get O(1) access to changed folders
    private static final ConcurrentMap<Path, TreeItem<IPathNode>> NODE_CACHE = new ConcurrentHashMap<>();
    // Single threaded watch service
    private static final ExecutorService WATCH_EXE = Executors.newSingleThreadExecutor();// .newSingleThreadExecutor()
    //
    //
    // Watcher to listen for folder changes.
    private static Watcher W;
    // Register dirs to watch running on W
    private static IWatchTask RWT;
    // Placeholder for runnable
    private static Future<?> WF;

    static {

        ROOT_NODE = RegularPathNode.of( ROOT_PATH, null );
        ROOT_PATH_NODE = PathNodeTreeItem.of( ROOT_NODE );

        FX_CURRENT_DIR_PATH.setValue( ROOT_NODE );
        FX_FONT_MAIN_PROP.setValue( Font.font( 18D ) );

        FX_CURRENT_DIR_PATH.addListener( ( obs, oldp, newp ) -> {

            if ( null != newp && !oldp.equals( newp ) ) {

                Path dir = newp.getValue();
                if ( !NODE_CACHE.containsKey( dir ) ) {
                    try {
                        register( dir );
                    } catch ( final IOException ioE ) {

                        GLOBLOG.warn( "No Watcher for dir '" + dir + "'", ioE );
                    }
                }
            }

        } );
    }

    private JBatGlobal() {

    }

    static void init() throws IOException {

        WatchService ws = FileSystems.getDefault().newWatchService();
        W = Watcher.create( ws );
        RWT = W.forPath( ROOT_PATH, new WatchEventListener() );
        Thread wt = new Thread( () -> RWT.watch() );
        wt.setDaemon( true );
        WF = WATCH_EXE.submit( wt );
    }

    /**
     * Stop Watch Thread and watch service.
     */
    static void stopWatch() {
        GLOBLOG.warn( "Stop Watch!" );

        try {
            boolean canceled = WF.cancel( true );
            GLOBLOG.warn( "Canceled Watch? " + canceled );
            W.stop();

        } catch ( final IOException ioE ) {

            GLOBLOG.warn( "Fail to stop service", ioE );
        } finally {
            WATCH_EXE.shutdownNow();
        }


    }

    /**
     * Stop search thread.
     */
    static void stopSearch() {

        SEARCH_EXE_SER.shutdownNow();
    }

    /**
     * Register dir for watching.
     *
     * @param dir dir
     * @throws IOException fail to watch
     */
    public static void register( final Path dir ) throws IOException {
        GLOBLOG.info( "Try to register path '" + dir + "'" );
        RWT.register( dir );
    }


    private static final class WatchEventListener implements IWatchEventHandler {

        @Override
        public EventAction onOverflow( Path path, int i ) {

            GLOBLOG.warn( "Overflow '" + path + "' =? " + i );

            return EventAction.ADVANCE;
        }

        @Override
        public EventAction onCreate( Path path, int i ) {

            Path parent = path.getParent();

            TreeItem<IPathNode> parentNode = NODE_CACHE.get( parent );
            TreeItem<IPathNode> addedNode = PathNodeTreeItem.of( RegularPathNode.of( path, parentNode.getValue() ) );
            // If we have a dir add it to cache
            // and listen for change
            if ( addedNode.getValue().isDir() ) {
                try {
                    JBatGlobal.register( path );
                } catch ( final IOException ioE ) {


                    GLOBLOG.info( "Dir '" + path + "' failed watch", ioE );
                }

                NODE_CACHE.putIfAbsent( path, addedNode );
            }
            parentNode.getChildren().add( addedNode );

            return EventAction.ADVANCE;
        }

        @Override
        public EventAction onModify( Path path, int i ) {
            GLOBLOG.info( "File modified '" + path + "'" );

            return EventAction.ADVANCE;
        }

        @Override
        public EventAction onDelete( Path path, int i ) {

            GLOBLOG.info( "File deleted '" + path + "'" );

            return EventAction.ADVANCE;
        }
    }
}
