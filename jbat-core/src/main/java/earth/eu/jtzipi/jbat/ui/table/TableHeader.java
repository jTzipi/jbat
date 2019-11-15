package earth.eu.jtzipi.jbat.ui.table;

import javafx.beans.property.*;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import javafx.scene.image.Image;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;

/**
 * Custom Table Header Component .
 *
 *
 * @author jTzipi
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

    public TableHeader( String textStr, double w, double h ) {

        this.fxWidthProp.setValue( w );
        this.fxHeightProp.setValue( h );

        setMouseTransparent( true );
    }

    public final StringProperty textPropFX() {
        return this.fxTextProp;
    }

    public static TableHeader of( String text, double width, double height ) {

        if ( null == text ) {
            text = "";
        }

        return new TableHeader( text, width, height );
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new TableHeaderCover( this );
    }
}
