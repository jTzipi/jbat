package earth.eu.jtzipi.jbat.ui;


import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;

/**
 * Painting Stuff .
 */
public final class Painter {

    //
    // Colors
    public static final Color COLOR_GRAY_212 = Color.grayRgb( 212 );
    public static final Color COLOR_GRAY_60 = Color.grayRgb( 60 );
    public static final Color COLOR_GRAY_47 = Color.grayRgb( 47 );
    public static final Color COLOR_GRAY_29 = Color.grayRgb( 29 );

    public static final Color COLOR_GRAY_19 = Color.grayRgb( 19 );
    public static final Color COLOR_GRAY_10 = Color.grayRgb( 10 );

    public static final Color COLOR_RGB_77_77_254 = Color.rgb( 77, 77, 254 );
    public static final Color COLOR_RGB_110_110_255 = Color.rgb( 110, 110, 255 );
    public static final Color COLOR_RGB_60_60_125 = Color.rgb( 60, 60, 125, 1D );
    // Stops

    //
    // Table header background gradient stop
    //
    static final Stop STOP_BG_TOP = new Stop( 0D, COLOR_GRAY_60 );
    static final Stop STOP_BG_TOP_DOWN = new Stop( 0.21D, COLOR_GRAY_47 );
    static final Stop STOP_BG_BOTTOM_UP = new Stop( 0.75D, COLOR_GRAY_29 );
    static final Stop bgBottom = new Stop( 1D, COLOR_GRAY_10 );

    //
    //
    //
    static final Stop STOP_01_CG29 = new Stop( 0.1D, COLOR_GRAY_29 );
    static final Stop STOP_05_CG60 = new Stop( 0.5D, COLOR_GRAY_47 );
    static final Stop STOP_09_CG29 = new Stop( 0.9D, COLOR_GRAY_29 );


    /**
     *
     */

    /**
     *
     */
    public static final LinearGradient LINEAR_GRADIENT_TABLE_HEADER_INVERSE = new LinearGradient( 0D, 1D, 0D, 0D, true, CycleMethod.NO_CYCLE, STOP_BG_TOP, STOP_BG_TOP_DOWN, STOP_BG_BOTTOM_UP, bgBottom );
    /**
     * Linear Gradient Dark Black to Darker Black.
     *
     */
    public static final LinearGradient LINEAR_GRADIENT_TABLE_HEADER = new LinearGradient( 0D, 0D, 0D, 1D, true, CycleMethod.NO_CYCLE, STOP_BG_TOP, STOP_BG_TOP_DOWN, STOP_BG_BOTTOM_UP, bgBottom );

    }

