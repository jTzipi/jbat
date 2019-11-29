package earth.eu.jtzipi.jbat.ui.table;

import earth.eu.jtzipi.jbat.ui.Painter;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.SkinBase;
import javafx.scene.paint.LinearGradient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * Table Header Cover.
 *
 * This is a custom table header 'cover' with a gradient, custom font and highlighting.
 * We use several cache techniques.
 *
 * <ul>
 *     <li>static sized canvas with multiple length of table column to ensure that canvas is large enough</li>
 *
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
    static final double HEIGHT_PREF = 46D;

    static final double HEIGHT_MAX = 100D;

    static final double SCALE_TEXT = 0.75D;
    static final double SCALE_LOW = 0.19D;
    static final double ARROW_SEGMENT = 5D;
    static final double SPACE = 5D;
    static final double OFF_X_LEFT = SPACE * 2;
    static final double LEN_ARROW = ARROW_SEGMENT + ARROW_SEGMENT;
    static final double OFF_Y_ARROW_UP = 14D;
    static final double OFF_Y_ARROW_DOWN = OFF_Y_ARROW_UP + 3D + ARROW_SEGMENT;
    static final double OFF_X_TEXT = OFF_X_LEFT + LEN_ARROW + SPACE * 4;

    static final double[] ARROW_PATH_X = {OFF_X_LEFT, OFF_X_LEFT + ARROW_SEGMENT, OFF_X_LEFT + ARROW_SEGMENT + ARROW_SEGMENT};
    static final double[] ARROW_DOWN_PATH_Y = {OFF_Y_ARROW_DOWN, OFF_Y_ARROW_DOWN + ARROW_SEGMENT, OFF_Y_ARROW_DOWN};
    static final double[] upY = {OFF_Y_ARROW_UP + ARROW_SEGMENT, OFF_Y_ARROW_UP, OFF_Y_ARROW_UP + ARROW_SEGMENT};


    private GraphicsContext gc;
    Canvas canvas;
    LinearGradient bgGradient = Painter.LINEAR_GRADIENT_TABLE_HEADER;
    LinearGradient bgGradientInverse = Painter.LINEAR_GRADIENT_TABLE_HEADER_INVERSE;

    double canvasWidth;     // width of canvas
    double canvasHeight;    // height of

    double fontSize;
    double offYBottom;
    boolean highlight;      // draw highlight when mouse over
    boolean clicked;        // draw bright when clicked

    public TableHeaderCover( final TableHeader th ) {
        super( th );
        this.canvasWidth = th.cacheBG ? 2 * th.getWidth() : th.getWidth();
        this.canvasHeight = th.getHeight();
        this.canvas = new Canvas( canvasWidth, canvasHeight );
        this.fontSize = canvasHeight * SCALE_TEXT;
        this.offYBottom = canvasHeight * SCALE_LOW + SPACE;

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
        // gc.clearRect( 0D, 0D, canvasWidth,canvasHeight );
        // background gradient
        gc.setFill( bgGradient );
        gc.fillRect( 0D, 0D, canvasWidth, canvasHeight );
        // text
        gc.setFont( th.fxFontProp.getValue() );
        gc.setFill( th.fxTextPaintProp.get() );
        gc.fillText( th.textPropFX().getValue(), OFF_X_TEXT, canvasHeight - offYBottom );


        gc.setLineWidth( 1.4D );
        gc.setStroke( bgGradientInverse );
        gc.strokeLine( 0D, 0D, 0D, canvasHeight );

        gc.setStroke( Painter.COLOR_GRAY_60 );
        // gc.strokeArc( canvasWidth - 24D, 2D, 24D, 24D, 0D, 360D, ArcType.CHORD );

        TableHeader.Sort type = th.sortTypeProp().getValue();

        gc.setStroke( type == TableHeader.Sort.ASC ? Painter.COLOR_RGB_77_77_254 : Painter.COLOR_GRAY_60 );
        gc.strokePolyline( ARROW_PATH_X, upY, 3 );
        gc.setStroke( type == TableHeader.Sort.DESC ? Painter.COLOR_RGB_77_77_254 : Painter.COLOR_GRAY_60 );
        gc.strokePolyline( ARROW_PATH_X, ARROW_DOWN_PATH_Y, 3 );

        // getChildren().set( 0, canvas );

    }


}
