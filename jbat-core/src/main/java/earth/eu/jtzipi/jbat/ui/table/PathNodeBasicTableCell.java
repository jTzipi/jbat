package earth.eu.jtzipi.jbat.ui.table;

import earth.eu.jtzipi.jbat.ui.Painter;
import earth.eu.jtzipi.jbat.ui.node.PathNodeFX;
import javafx.scene.control.TableCell;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.text.Font;

public abstract class PathNodeBasicTableCell<S> extends TableCell<PathNodeFX, S> {

    Background bgEven = new Background( new BackgroundFill( Painter.COLOR_GRAY_29, null, null ) );
    Background bgOdd = new Background( new BackgroundFill( Painter.COLOR_GRAY_10, null, null ) );
    Font font = Font.font( 18D );

    PathNodeBasicTableCell() {
        setFont( font );
        setTextFill( Painter.COLOR_RGB_110_110_255 );
    }


    @Override
    protected void updateItem( S item, boolean empty ) {
        super.updateItem( item, empty );

        setBackground( getIndex() % 2 == 0 ? bgEven : bgOdd );


    }
}
