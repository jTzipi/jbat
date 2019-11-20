package earth.eu.jtzipi.jbat;

import earth.eu.jtzipi.jbat.ui.MainPane;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Main class.
 * @author jTzipi
 */
public class JBat extends Application {


    private static final Logger JBAT_LOG = LoggerFactory.getLogger( "JBat" );
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



    /**
     *
     * @param primaryStage primary
     *
     */
    public void start( Stage primaryStage ) {

        Resources.loadResource(); // load resources

        final Scene scene = new Scene( MainPane.getInstance(), JBatGlobal.WIDTH_DEF, JBatGlobal.HEIGHT_DEF );

        primaryStage.setOnCloseRequest( event -> onClose() );

        JBatGlobal.MAIN_STAGE = primaryStage;
        primaryStage.setTitle("Java Batch Tool");
        primaryStage.setScene(scene);
        primaryStage.show();


    }
}
