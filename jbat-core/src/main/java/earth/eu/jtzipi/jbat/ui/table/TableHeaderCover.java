package earth.eu.jtzipi.jbat.ui.table;

import earth.eu.jtzipi.jbat.ui.Painter;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.SkinBase;
import javafx.scene.paint.LinearGradient;
import javafx.scene.shape.ArcType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.image.BufferedImage;


/**
 *
 */
public class TableHeaderCover extends SkinBase<TableHeader> {

    private static final Logger LOG = LoggerFactory.getLogger( "TableHCover" );

    /**
     * Minimum width.
     */
    public static final double WIDTH_MIN = 9D;
    /**
     * Maximum width.
     */
    public static final double WIDTH_MAX = 9999D;
    /** Minimum height. */
    public static final double HEIGHT_MIN = 9D;
    /** Preferred height. */
    public static final double HEIGHT_PREF = 26D;

    public static final double HEIGHT_MAX = 100D;

    private BufferedImage backgroundCache;
    private GraphicsContext gc;
    Canvas canvas;
    LinearGradient bgGradient = Painter.LINEAR_GRADIENT_TABLE_HEADER;
    LinearGradient bgGradientInverse = Painter.LINEAR_GRADIENT_TABLE_HEADER_INVERSE;

    double w;   // width of canvas
    double h;   //

    public TableHeaderCover( final TableHeader th ) {
        super( th );
        this.w = th.widthProperty().doubleValue();
        this.h = th.heightProperty().doubleValue();
        init();
        draw( w, h );
    }

    private void init() {

        TableHeader th = getSkinnable();

        th.setOnMouseClicked( evt -> redraw() );
        th.widthProperty().addListener( evt -> redraw() );
        th.setOnMouseEntered( evt -> onMouseEntered() );

    }

    @Override
    protected double computeMinHeight( double width, double topInset, double rightInset, double bottomInset, double leftInset ) {

        return HEIGHT_MIN;
    }

    @Override
    protected double computePrefHeight( double width, double topInset, double rightInset, double bottomInset, double leftInset ) {
        return Math.max( HEIGHT_PREF, h );
    }

    private void onMouseEntered() {

        LOG.warn( "Gysi" );
    }

    private void redraw() {
        LOG.warn( "Mouse" );
    }

    private void draw( double width, double height ) {
        canvas = new Canvas( width, height );
        TableHeader th = getSkinnable();


        gc = canvas.getGraphicsContext2D();


        // background gradient
        gc.setFill( bgGradient );
        gc.fillRect( 0D, 0D, w, h );
        // text
        gc.setFont( th.fxFontProp.getValue() );
        gc.setStroke( th.fxTextPaintProp.get() );
        gc.strokeText( th.textPropFX().getValue(), 9D, 30D );


        gc.setLineWidth( 1.4D );
        gc.setStroke( bgGradientInverse );
        gc.strokeLine( 0D, 0D, 0D, h );

        gc.setStroke( Painter.COLOR_GRAY_60 );
        gc.strokeArc( w - 24D, 2D, 24D, 24D, 0D, 360D, ArcType.CHORD );
        getChildren().add( canvas );

    }
}
