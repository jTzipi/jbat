package earth.eu.jtzipi.jbat.ui;

import javafx.beans.property.*;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import javafx.scene.image.Image;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;

/**
 * Custom Table Header Component .
 */
public class TableHeader extends Control {

    public enum Sort {

    }

    StringProperty fxTextProp = new SimpleStringProperty();     // text of header

    ObjectProperty<Paint> fxBackgroundProp = new SimpleObjectProperty<>( this, "FX_BG_PROP" );

    ObjectProperty<Paint> fxTextPaintProp = new SimpleObjectProperty<>( this, "FX_TEXT_FILL_PROP" );

    ObjectProperty<Image> fxIconProp = new SimpleObjectProperty<>( this, "FX_ICON_PROP" );

    ObjectProperty<Font> fxFontProp = new SimpleObjectProperty<>();

    DoubleProperty fxWidthProp = new SimpleDoubleProperty( this, "FX_WIDTH_PROP" );

    DoubleProperty fxHeightProp = new SimpleDoubleProperty( this, "FX_HEIGHT_PROP" );

    TableHeader( String textStr, double w, double h ) {

        this.fxWidthProp.setValue( w );
        this.fxHeightProp.setValue( h );

        setMouseTransparent( true );
    }

    public final StringProperty textPropFX() {
        return this.fxTextProp;
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new TableHeaderCover( this );
    }
}
