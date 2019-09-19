package earth.eu.jtzipi.jbat;

import earth.eu.jtzipi.jbat.ui.MainPane;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;


/**
 * Main class.
 * @author jTzipi
 */
public class JBat extends Application {

    public static final double WIDTH_DEF = 1200D;
    public static final double HEIGHT_DEF = 770D;

    /**
     * JML start.
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    /**
     *
     * @param primaryStage
     * @throws Exception
     */
    public void start( Stage primaryStage ) throws Exception {

                Scene scene = new Scene( MainPane.getInstance(), WIDTH_DEF, HEIGHT_DEF );

        primaryStage.setTitle("Java Batch Tool");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
