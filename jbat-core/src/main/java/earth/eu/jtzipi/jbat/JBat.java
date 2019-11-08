package earth.eu.jtzipi.jbat;

import earth.eu.jtzipi.jbat.ui.MainPane;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;


/**
 * Main class.
 * @author jTzipi
 */
public class JBat extends Application {



    /**
     * JML start.
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    private static void onClose() {


        JBatGlobal.SEARCH_EXE_SER.shutdownNow();
        System.out.println( " <Close> " );
    }

    private static void loadResource() {

    }

    /**
     *
     * @param primaryStage primary
     * @throws Exception failed
     */
    public void start( Stage primaryStage ) throws Exception {
        primaryStage.setOnCloseRequest( event -> onClose() );


        Scene scene = new Scene( MainPane.getInstance(), JBatGlobal.WIDTH_DEF, JBatGlobal.HEIGHT_DEF );


        JBatGlobal.MAIN_STAGE = primaryStage;
        primaryStage.setTitle("Java Batch Tool");
        primaryStage.setScene(scene);
        primaryStage.show();


    }
}
