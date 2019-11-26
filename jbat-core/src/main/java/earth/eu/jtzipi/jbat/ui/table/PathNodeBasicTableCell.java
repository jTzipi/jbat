package earth.eu.jtzipi.jbat.ui.table;

import earth.eu.jtzipi.jbat.JBatGlobal;
import earth.eu.jtzipi.jbat.ui.IconStyle;
import earth.eu.jtzipi.jbat.ui.Painter;
import earth.eu.jtzipi.jbat.ui.node.PathNodeFX;
import javafx.scene.control.TableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import org.slf4j.LoggerFactory;

public abstract class PathNodeBasicTableCell<S> extends TableCell<PathNodeFX, S> {


    private final ImageView iconView = new ImageView();
    private Image icon;
    private Border border = new Border( new BorderStroke( Painter.COLOR_GRAY_47, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths( 0D, 1D, 0D, 0D ) ) );
    private Border bFocus = new Border( new BorderStroke( Painter.COLOR_RGB_110_110_255, BorderStrokeStyle.DASHED, CornerRadii.EMPTY, BorderWidths.DEFAULT ) );

    private final Background bgEven = new Background( new BackgroundFill( Painter.COLOR_GRAY_29, null, null ) );
    private Background bgOdd = new Background( new BackgroundFill( Painter.COLOR_GRAY_19, null, null ) );
    //Font font = Font.font( 15D );

    PathNodeBasicTableCell() {
        fontProperty().bind( JBatGlobal.FX_FONT_MAIN_PROP );
        setTextFill( Painter.COLOR_RGB_110_110_255 );


    }

    @Override
    protected void updateItem( S item, boolean empty ) {
        super.updateItem( item, empty );
        icon = null;
        setBackground( getIndex() % 2 == 0 ? bgEven : bgOdd );
        setBorder( border );
        setGraphic( iconView );

    }

    private void onSelectChanged( boolean oldFocus, boolean newFocus ) {
        LoggerFactory.getLogger( "" ).warn( "Habe ?" + newFocus );
        setBorder( newFocus ? bFocus : border );
    }

    private void onIconStyleChange( IconStyle changedStyle ) {

        switch ( changedStyle ) {
            case SVG:
                icon = null;
                break;
            case BUUF:
                icon = null;
                break;
            case SYSTEM:
                icon = null;
                break;
        }
    }
}
