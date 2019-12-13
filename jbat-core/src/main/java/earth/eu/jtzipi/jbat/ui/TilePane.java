package earth.eu.jtzipi.jbat.ui;


import earth.eu.jtzipi.jbat.JBatGlobal;
import earth.eu.jtzipi.modules.node.path.IPathNode;
import javafx.beans.property.*;
import javafx.geometry.Insets;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.Glow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

public class TilePane extends FlowPane {

    private static final String FX_PATH_NODE_FILTER_PROP = "FX_PATH_NODE_FILTER";

    private static final String FX_BACKGROUND_COLOR_PROP = "FX_BACKGROUND_COLOR";


    private static final Logger LOG = LoggerFactory.getLogger( TilePane.class );


    // view of all nodes
    //private FlowPane tileP;
    // wrapper for flow pane
    private ScrollPane tileScrP;


    /**
     *
     */
    TilePane() {

        createTilePane();

        gadi();
        // set first dir
        pathChanged( JBatGlobal.FX_CURRENT_DIR_PATH.getValue() );
    }

    /**
     * Create pane.
     */
    private void createTilePane() {
        //tileOL = FXCollections.observableArrayList();
        //tileP = new FlowPane();
        //tileP.setPrefWrapLength(PREF_HWRAP);


        //setCenter(tileScrP);
        Color colorBG = Color.rgb( 7, 7, 77, 1D );
        // DEBUG
        setBackground( new Background( new BackgroundFill( colorBG, CornerRadii.EMPTY, Insets.EMPTY ) ) );
    }

    /**
     * Init Property Listener.
     */
    private void gadi() {
        // If dir node change
        JBatGlobal.FX_CURRENT_DIR_PATH.addListener( ( obs, oldPath, newPath ) -> {

            LOG.warn( "gysi : " + oldPath + " > " + newPath );
            // real change
            if ( null != newPath && oldPath != newPath ) {
                pathChanged( newPath );
            }

        } );
    }

    /**
     * Directory changed.
     *
     * @param path path
     */
    private void pathChanged( IPathNode path ) {
        //tileOL.clear();

        // Get sub nodes and map them to tiles
        List<Tile> tileL = path.getSubnodes()
                .stream()
                .map( Tile::new )
                .collect( Collectors.toList() );

        getChildren().setAll( tileL );
    }

    private void onFilterChanged() {

    }

    /**
     * A tile of flow pane.
     */
    private static final class Tile extends Label {


        // background if not mouse over
        private static final Background MOUSE_NOT_OVER_BACKGROUND = Background.EMPTY;
        // background if mouse over
        private Background mouseOverBG;

        // Border stroke of tile
        // private BorderStroke borderStroke;
        private Color textCol = Color.rgb( 47, 127, 247, 1D );

        // file icon
        private ImageView iconIV;

        private Glow hoverGlow;
        private Glow selectGlow;

        // Tile size FX Property
        private ObjectProperty<Size> fxPropSize = new SimpleObjectProperty<>( this, "FX_PROP_TILE_SIZE", Size.MEDIUM );

        /**
         * Tile size.
         */
        enum Size {

            DEFAULT( 128D, true ),
            LARGE( 106D, true ),
            MEDIUM( 56D, false ),
            SMALL( 49D, false ),
            TINY( 41D, false );

            private final double size;
            private final boolean drawText;

            private DoubleProperty fxSizeProp;
            private BooleanProperty fxDrawTextProp;

            /**
             * @param size
             * @param drawTextProp
             */
            Size( final double size, final boolean drawTextProp ) {
                this.size = size;
                this.drawText = drawTextProp;
            }

            /**
             * @return
             */
            DoubleProperty fxSizeProp() {
                if ( null == fxSizeProp ) {
                    fxSizeProp = new SimpleDoubleProperty( this, "", getSize() );
                }

                return fxSizeProp;
            }

            BooleanProperty fxDrawTextProp() {
                if ( null == fxDrawTextProp ) {
                    fxDrawTextProp = new SimpleBooleanProperty( this, "", isDrawText() );
                }
                return fxDrawTextProp;
            }

            /**
             * Size of tile.
             *
             * @return size
             */
            double getSize() {
                return this.size;
            }

            /**
             * Draw text.
             *
             * @return draw text
             */
            boolean isDrawText() {
                return this.drawText;
            }
        }

        // path to wrap
        private final IPathNode pathNode;

        private Tile( IPathNode pn ) {
            this.pathNode = pn;
            createTile();

        }

        private void createTile() {


            // create image view for tile and set width
            iconIV = new ImageView();
            iconIV.fitWidthProperty().bind( fxPropSize.getValue().fxSizeProp() );
            iconIV.setPreserveRatio( true );

            Font f = Font.getDefault();
            setFont( f ); // Font.font( 24D )
            setText( pathNode.getName() );
            setGraphic( iconIV );
            setTextFill( textCol );
            setContentDisplay( ContentDisplay.TOP );
            setPadding( new Insets( 10 ) );

            Size s = fxPropSize.getValue();
            setPrefSize( s.getSize(), s.getSize() );

            selectGlow = new Glow( 0.7D );

            setOnMouseExited( me -> setEffect( null ) );
            setOnMouseEntered( me -> setEffect( selectGlow ) );

            setOnMouseClicked( mousE -> {
                // on


            } );
        }

        public String toString() {

            return "TPath  { " + pathNode + " }";
        }
    }


}
