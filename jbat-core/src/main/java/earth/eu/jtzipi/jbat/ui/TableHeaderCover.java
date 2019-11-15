package earth.eu.jtzipi.jbat.ui;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.SkinBase;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;

public class TableHeaderCover extends SkinBase<TableHeader> {
    public static final double WIDTH_MIN = 9D;

    public static final double WIDTH_MAX = 9999D;

    public static final double HEIGHT_MIN = 9D;

    public static final double HEIGHT_PREF = 26D;

    GraphicsContext gc;
    Canvas canvas;
    LinearGradient bgGradient;

    double w;
    double h;

    public TableHeaderCover( final TableHeader th ) {
        super( th );
        this.w = th.fxWidthProp.doubleValue();
        this.h = th.fxHeightProp.doubleValue();
        init();
        draw();
    }

    private void init() {

        bgGradient = new LinearGradient( 0D, 0D, 0D, 1D, true, CycleMethod.NO_CYCLE, Painter.STOP_BG_TOP, Painter.bgTopD, Painter.bgBottomUp, Painter.bgBottom );
    }

    @Override
    protected double computeMinHeight( double width, double topInset, double rightInset, double bottomInset, double leftInset ) {

        return HEIGHT_MIN;
    }

    @Override
    protected double computePrefHeight( double width, double topInset, double rightInset, double bottomInset, double leftInset ) {
        return Math.max( HEIGHT_PREF, h );
    }

    private void draw() {
        canvas = new Canvas( w, h );
        TableHeader th = getSkinnable();


        gc = canvas.getGraphicsContext2D();


        // background gradient
        gc.setFill( bgGradient );
        gc.fillRect( 0D, 0D, w, h );
        // text
        gc.setFill( th.fxTextPaintProp.get() );
        gc.strokeText( th.textPropFX().getValue(), 9D, 30D );


        getChildren().add( canvas );

    }
}
