package earth.eu.jtzipi.jbat.ui.table;

import earth.eu.jtzipi.jbat.Resources;
import earth.eu.jtzipi.jbat.ui.Painter;
import earth.eu.jtzipi.modules.utils.Utils;
import javafx.beans.property.*;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import javafx.scene.control.TableColumn;
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


    enum Sort {

        /**
         * Ascending.
         */
        ASC( 0 ),
        /**
         *
         */
        DESC( 1 ),
        /**
         *
         */
        NONE( 2 );

        private int id;

        private Sort( int id ) {
            this.id = id;
        }

        Sort click() {

            Sort next;
            switch ( id ) {
                case 0:
                    next = Sort.DESC;
                    break;
                case 1:
                    next = Sort.NONE;
                    break;
                case 2:
                    next = Sort.ASC;
                    break;
                default:
                    throw new IllegalStateException( "ID is Quark" );
            }

            return next;
        }
    }

    /** Table Column Sort Order Property.*/
    private ObjectProperty<TableColumn.SortType> fySortProp = new SimpleObjectProperty<>();
    ObjectProperty<Sort> fxSortTypeProp = new SimpleObjectProperty<>( this, "FX_SORT_TYPE_PROP" );
    StringProperty fxTextProp = new SimpleStringProperty( this, "FX_TEXT_PROP", "" );     // text of header

    DoubleProperty fxCacheWidthProp = new SimpleDoubleProperty( this, "FX_CACHE_WIDTH_PROP" );

    ObjectProperty<Paint> fxTextPaintProp = new SimpleObjectProperty<>( this, "FX_TEXT_FILL_PROP", Painter.COLOR_RGB_77_77_254 );

    ObjectProperty<Image> fxIconProp = new SimpleObjectProperty<>( this, "FX_ICON_PROP" );

    ObjectProperty<Font> fxFontProp = new SimpleObjectProperty<>();

    Sort sort = Sort.ASC;

    TableHeader( double w, double h ) {
        setWidth( w );
        setHeight( h );
        this.fxCacheWidthProp.setValue( 4 * w );
        this.fxSortTypeProp.setValue( sort );

    }

    public final StringProperty textPropFX() {
        return this.fxTextProp;
    }

    public final ObjectProperty<Font> fontPropFX() {
        return this.fxFontProp;
    }

    public final ObjectProperty<TableColumn.SortType> sortPropFY() {
        return this.fySortProp;
    }

    public final ObjectProperty<Sort> sortTypeProp() {
        return fxSortTypeProp;
    }

    private void init( String textStr ) {
        if ( null != textStr ) {
            this.fxTextProp.setValue( textStr );
        }
        setOnMouseClicked( event -> onClick() );
        //;
        this.fxFontProp.setValue( Resources.getUIFont( "ui.table.header.font", 32D ) );
    }

    /**
     * Return TableHeader.
     *
     * @param text   text optional
     * @param width  width
     * @param height
     * @return
     */
    public static TableHeader of( String text, double width, double height ) {
        width = Utils.clamp( width, TableHeaderCover.WIDTH_MIN, TableHeaderCover.WIDTH_MAX );


        TableHeader theader = new TableHeader( width, height );
        theader.init( text );

        return theader;
    }

    private void onClick() {

        sort = sort.click();
        fxSortTypeProp.setValue( sort );
        fySortProp.setValue( sort == Sort.ASC ? TableColumn.SortType.ASCENDING : TableColumn.SortType.DESCENDING );

    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new TableHeaderCover( this );
    }
}
