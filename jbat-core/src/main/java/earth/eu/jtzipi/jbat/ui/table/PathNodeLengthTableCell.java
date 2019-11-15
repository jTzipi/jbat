package earth.eu.jtzipi.jbat.ui.table;

import earth.eu.jtzipi.modules.io.IOUtils;


public class PathNodeLengthTableCell extends PathNodeBasicTableCell<Long> {

    @Override
    protected void updateItem( Long item, boolean empty ) {
        super.updateItem( item, empty );

        if( null == item || empty ) {

            setText( "" );
            setGraphic( null );
        }        else {

            String text;

            if( -1L == item ) {
                text = "---";
            } else if( -2L == item ) {
                text = "---";
            } else {
                text = IOUtils.formatFileSize( item, false );
            }

            setText( text );
        }


    }

}
