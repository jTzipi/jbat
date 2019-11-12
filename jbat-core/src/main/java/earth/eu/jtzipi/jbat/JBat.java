package earth.eu.jtzipi.jbat;

import earth.eu.jtzipi.jbat.ui.MainPane;
import earth.eu.jtzipi.modules.io.IOUtils;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.CompletableFuture;


/**
 * Main class.
 * @author jTzipi
 */
public class JBat extends Application {

    private static final Path resPath = IOUtils.getProgramDir().resolve( "jbat-core/src/main/resources" );

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


        CompletableFuture<Map<Path, Image>> imgC = CompletableFuture.supplyAsync( () -> {

            try {

                Path path = resPath.resolve( "img" );
                Map<Path, Image> imgMap = new TreeMap<>();

                for ( Path ip : IOUtils.lookupDir( path, IOUtils.PATH_ACCEPT_IMAGE ) ) {

                    imgMap.put( ip, IOUtils.loadImage( ip ) );

                }

                return imgMap;
            } catch ( IOException ioE ) {
                throw new IllegalStateException();
            }
        } );

        // imgC.whenComplete( ( map, t ) -> Resources.setImages(map)  );







        Scene scene = new Scene( MainPane.getInstance(), JBatGlobal.WIDTH_DEF, JBatGlobal.HEIGHT_DEF );


        JBatGlobal.MAIN_STAGE = primaryStage;
        primaryStage.setTitle("Java Batch Tool");
        primaryStage.setScene(scene);
        primaryStage.show();


    }
}
