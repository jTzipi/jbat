package earth.eu.jtzipi.jbat.ui;


import earth.eu.jtzipi.jbat.ui.node.PathNodeFX;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.Pane;


public class BatchPathPane extends Pane {





    private TableView<PathNodeFX> batchFileTabV;

    BatchPathPane(  ) {



    }

    private void createBatchPath() {


    }

    private TableView<PathNodeFX> createBatchFileTableView() {

        TableView<PathNodeFX> tv = new TableView<>();

        TableColumn<PathNodeFX, String> bfNameTC = new TableColumn<>("");
        TableColumn<PathNodeFX, String> bfTypeTC = new TableColumn<>("Type");

        tv.getColumns().addAll( bfNameTC, bfTypeTC );

        return tv;
    }
}
