package earth.eu.jtzipi.jbat.ui.table;

import earth.eu.jtzipi.jbat.ui.Painter;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.SkinBase;
import javafx.scene.paint.LinearGradient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.image.BufferedImage;



/**
 * Table Header Cover.
 *
 * This is a custom table header 'cover' with a gradient, custom font and highlighting.
 * We use several cache techniques.
 *
 * <ul>
 *     <li>static sized canvas with multiple length of table column to ensure that canvas is large enough</li>
 *     <li>cache background gradient as buffered image</li>
 * </ul>
 */
public final class TableHeaderCover extends SkinBase<TableHeader> {

    //  double[] arrowX = { canvasWidth - 17D, canvasWidth - 22D, canvasWidth - 17D };
    //        double[] arrowY = { 4D, 9D, 14D };
    private static final Logger LOG = LoggerFactory.getLogger( "TableHCover" );


    private static final double X_EAST_OFF = 5D;
    /**
     * Minimum width.
     */
    static final double WIDTH_MIN = 9D;
    /**
     * Maximum width.
     */
    static final double WIDTH_MAX = 9999D;
    /** Minimum height. */
    static final double HEIGHT_MIN = 9D;
    /** Preferred height. */
    static final double HEIGHT_PREF = 26D;

    static final double HEIGHT_MAX = 100D;

    private BufferedImage backgroundCache;
    private GraphicsContext gc;
    Canvas canvas;
    LinearGradient bgGradient = Painter.LINEAR_GRADIENT_TABLE_HEADER;
    LinearGradient bgGradientInverse = Painter.LINEAR_GRADIENT_TABLE_HEADER_INVERSE;

    double canvasWidth;     // width of canvas
    double canvasHeight;    // height of
    boolean highlight;      // draw highlight when mouse over
    boolean clicked;        // draw bright when clicked

    public TableHeaderCover( final TableHeader th ) {
        super( th );
        this.canvasWidth = th.cacheBG ? 4 * th.getWidth() : th.getWidth();
        this.canvasHeight = th.getHeight();
        this.canvas = new Canvas( canvasWidth, canvasHeight );
        init();
        draw();
        getChildren().setAll( canvas );
    }

    private void init() {

        TableHeader th = getSkinnable();


        th.setOnMouseEntered( evt -> onMouseEntered() );
        th.setOnMouseExited( evt -> onMouseExited() );

        th.sortTypeProp().addListener( change -> draw() );
    }

    @Override
    protected double computeMinHeight( double width, double topInset, double rightInset, double bottomInset, double leftInset ) {

        return HEIGHT_MIN;
    }

    @Override
    protected double computePrefHeight( double width, double topInset, double rightInset, double bottomInset, double leftInset ) {
        return Math.max( HEIGHT_PREF, canvasHeight );
    }

    @Override
    protected double computeMaxHeight( double width, double topInset, double rightInset, double bottomInset, double leftInset ) {
        return HEIGHT_MAX;
    }

    private void onMouseEntered() {
        this.highlight = true;

        LOG.warn( "Gysi" );
        draw();
    }

    private void onMouseExited() {
        this.highlight = false;
        LOG.warn( "Gysi" );
        draw();
    }


    private void draw() {

        TableHeader th = getSkinnable();


        gc = canvas.getGraphicsContext2D();

        // background gradient
        gc.setFill( bgGradient );
        gc.fillRect( 0D, 0D, canvasWidth, canvasHeight );
        // text
        gc.setFont( th.fxFontProp.getValue() );
        gc.setStroke( th.fxTextPaintProp.get() );
        gc.strokeText( th.textPropFX().getValue(), 9D, 30D );


        gc.setLineWidth( 1.4D );
        gc.setStroke( bgGradientInverse );
        gc.strokeLine( 0D, 0D, 0D, canvasHeight );

        gc.setStroke( Painter.COLOR_GRAY_60 );
        // gc.strokeArc( canvasWidth - 24D, 2D, 24D, 24D, 0D, 360D, ArcType.CHORD );

        double realWidth = th.getPrefWidth();
        TableHeader.Sort type = th.sortTypeProp().getValue();
        double[] downX = {realWidth - 17D, realWidth - 22D, realWidth - 27D};
        double[] downY = {24D, 29D, 24D};

        double[] upX = {realWidth - 17D, realWidth - 22D, realWidth - 27D};
        double[] upY = {19D, 14D, 19D};


        gc.setStroke( type == TableHeader.Sort.ASC ? Painter.COLOR_RGB_77_77_254 : Painter.COLOR_GRAY_60 );
        gc.strokePolyline( upX, upY, 3 );
        gc.setStroke( type == TableHeader.Sort.DESC ? Painter.COLOR_RGB_77_77_254 : Painter.COLOR_GRAY_60 );
        gc.strokePolyline( downX, downY, 3 );

        // getChildren().set( 0, canvas );

    }


}
