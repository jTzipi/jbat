package earth.eu.jtzipi.jbat.ui;

import javafx.scene.control.Cell;

public abstract class AbstractCellRenderer<T> extends Cell<T> {

    public static final String DEFAULT_TEXT_EMPTY = "";
    public static final Object DEFAULT_VAL_EMPTY = null;

    String nullOrEmptyText;
    T nullOrEmptyVal;


}
