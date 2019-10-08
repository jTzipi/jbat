package earth.eu.jtzipi.jbat.ui.table;

import earth.eu.jtzipi.jbat.ui.node.PathNodeFX;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.TableCell;

import java.nio.file.attribute.FileTime;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class PathNodeTimeTableCell extends TableCell<PathNodeFX, FileTime> {

    public static final String FORMAT = "dd.MM.yy HH:mm";
    public static final DateTimeFormatter DEFAULT_FORMATTER = DateTimeFormatter.ofPattern( FORMAT );


    private LocalDateTime ld;
    private String dtFormat;


    private ObjectProperty<DateTimeFormatter> fxFormatterProp = new SimpleObjectProperty<>(this, "", DEFAULT_FORMATTER);

    @Override
    protected void updateItem(  FileTime item, boolean empty ) {
        super.updateItem( item, empty );

        String text;

        // Not valid
        if( null == item || empty ) {

            text = "";

        }

        else {

            ld = item.toInstant().atZone( ZoneId.systemDefault() ).toLocalDateTime();

            text = fxFormatterProp.getValue().format( ld );
        }
        setText(text);


    }

    public final ObjectProperty<DateTimeFormatter> getFormatterProp() {
        return this.fxFormatterProp;
    }
}
