package earth.eu.jtzipi.jbat.ui;


import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;

public final class Painter {

    /**
     * Dark gray 60 RGB    .
     */
    public static final Color COLOR_GRAY_60 = Color.rgb( 60, 60, 60 );
    public static final Color COLOR_GRAY_47 = Color.grayRgb( 47 );
    public static final Color COLOR_GRAY_29 = Color.grayRgb( 29 );
    public static final Color COLOR_GRAY_10 = Color.rgb( 10, 10, 10 );
    public static final Color COLOR_RGB_77_77_254 = Color.rgb( 77, 77, 254 );
    public static final Color COLOR_RGB_110_110_255 = Color.rgb( 110, 110, 255 );

    public static final Stop STOP_BG_TOP = new Stop( 0D, COLOR_GRAY_60 );

    public static final Stop bgTopD = new Stop( 0.21D, COLOR_GRAY_47 );

    public static final Stop bgBottomUp = new Stop( 0.75D, COLOR_GRAY_29 );

    public static final Stop bgBottom = new Stop( 1D, COLOR_GRAY_10 );

    public static LinearGradient lgBlack0To100() {
        return new LinearGradient( 0D, 0D, 0D, 1D, true, CycleMethod.NO_CYCLE, STOP_BG_TOP, bgTopD, bgBottomUp, bgBottom );
    }


}
