package earth.eu.jtzipi.jbat.ui.table;

import earth.eu.jtzipi.jbat.Resources;
import earth.eu.jtzipi.modules.utils.Utils;
import javafx.beans.property.*;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import javafx.scene.control.TableColumn;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;

/**
 * Custom Table Header Component .
 *
 *
 * @author jTzipi
 */
public class TableHeader extends Control {
    /**
     * Sort order.
     */
    public enum Sort {

        /**
         * Ascending.
         */
        ASC( 0, TableColumn.SortType.ASCENDING ),
        /**
         *
         */
        DESC( 1, TableColumn.SortType.DESCENDING ),
        /**
         *
         */
        NONE( 2, null );

        private int id;
        private TableColumn.SortType type;

        Sort( int id, TableColumn.SortType type ) {
            this.id = id;
            this.type = type;
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

        /**
         * TableColumn sort type.
         *
         * @return sort type
         */
        public TableColumn.SortType getSort() {
            return this.type;
        }
    }

    /** Table Column Sort Order Property.*/
    ObjectProperty<TableColumn.SortType> fxTableColumnSortProp = new SimpleObjectProperty<>();
    /**
     * control sort type.
     */
    ObjectProperty<Sort> fxSortTypeProp = new SimpleObjectProperty<>( this, "FX_SORT_TYPE_PROP" );
    /**
     * Text of header .
     */
    StringProperty fxTextProp = new SimpleStringProperty( this, "FX_TEXT_PROP", "" );     // text of header

    /**
     * Text paint.
     */
    ObjectProperty<Paint> fxTextPaintProp = new SimpleObjectProperty<>( this, "FX_TEXT_FILL_PROP", Color.grayRgb( 9 ) );
    /** Icon .*/
    ObjectProperty<Image> fxIconProp = new SimpleObjectProperty<>( this, "FX_ICON_PROP" );

    ObjectProperty<Font> fxFontProp = new SimpleObjectProperty<>();

    BooleanProperty fxSortProp = new SimpleBooleanProperty( this, "FX_SORT_PROP" );

    Sort sort = Sort.NONE;
    boolean cacheBG;
    boolean canSort;

    TableHeader( double w, double h, boolean cachedProp, boolean canSortProp ) {
        setWidth( w );
        setHeight( h );
        setPrefWidth( w );
        setPrefHeight( h );
        this.cacheBG = cachedProp;

        this.canSort = canSortProp;
        this.fxSortProp.setValue( canSortProp );
        this.fxSortTypeProp.setValue( sort );

    }

    public final ObjectProperty<TableColumn.SortType> fyTableColumnSortTypeProp() {
        return this.fxTableColumnSortProp;
    }

    public final StringProperty textPropFX() {
        return this.fxTextProp;
    }

    public final ObjectProperty<Font> fontPropFX() {
        return this.fxFontProp;
    }

    public final ObjectProperty<Sort> sortTypeProp() {
        return fxSortTypeProp;
    }

    public final BooleanProperty sortPropFX() {
        return this.fxSortProp;
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
     *
     * @return
     */
    public static TableHeader of( String text, double width, double height, boolean cachedProp ) {
        width = Utils.clamp( width, TableHeaderCover.WIDTH_MIN, TableHeaderCover.WIDTH_MAX );
        height = Utils.clamp( height, TableHeaderCover.WIDTH_MIN, TableHeaderCover.HEIGHT_MAX );

        TableHeader theader = new TableHeader( width, height, cachedProp, false );
        theader.init( text );

        return theader;
    }

    public static TableHeader ofBackgroundOnly( double width, double height ) {

        return new TableHeader( width, height, false, false );
    }

    private void onClick() {

        sort = sort.click();
        fxSortTypeProp.setValue( sort );
        fxTableColumnSortProp.setValue( sort.getSort() );

        System.out.println( "SOrt ist nun " + sort );


    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new TableHeaderCover( this );
    }
}
