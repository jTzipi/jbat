package earth.eu.jtzipi.jbat;

import earth.eu.jtzipi.jbat.ui.MainPane;
import earth.eu.jtzipi.modules.io.IOUtils;
import earth.eu.jtzipi.modules.io.image.ImageDimension;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.CompletableFuture;


/**
 * Main class.
 * @author jTzipi
 */
public class JBat extends Application {

    private static final Path PATH_TO_RES = IOUtils.getProgramDir().resolve( "jbat-core/src/main/resources" );

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

        CompletableFuture<Map<String, Font>> cfFM = loadFonts();

        cfFM.whenComplete( ( map, t ) -> Resources.setFonts( map ) );

        CompletableFuture<Map<ImageDimension, Map<String, Image>>> cfIM = loadImage();

        cfIM.whenComplete( ( map, t ) -> Resources.setImages( map ) );
    }

    private static CompletableFuture<Map<String, Font>> loadFonts() {
        return CompletableFuture.supplyAsync( () -> {


            Path path = PATH_TO_RES.resolve( "font" );
            Map<String, Font> fontMap = new TreeMap<>();

            for ( Path fp : IOUtils.lookupDir( path ) ) {
                String fn = fp.getFileName().toString().toLowerCase();
                try {
                    fontMap.put( fn, IOUtils.loadFont( fp, IOUtils.DEFAULT_FONT_SIZE ) );
                } catch ( IOException ioE ) {
                    throw new IllegalStateException();
                }
            }

            return fontMap;

        } );
    }

    private static CompletableFuture<Map<ImageDimension, Map<String, Image>>> loadImage() {
        return CompletableFuture.supplyAsync( () -> {

            Path imgPath = PATH_TO_RES.resolve( "img" );
            List<Path> dimL = IOUtils.lookupDir( imgPath, IOUtils.PATH_ACCEPT_DIR );
            Map<ImageDimension, Map<String, Image>> imgM = new HashMap<>();
            for ( Path folder : dimL ) {

                int dim = Integer.parseInt( folder.getFileName().toString() );
                ImageDimension imgDim = ImageDimension.of( dim, dim );

                imgM.put( imgDim, new HashMap<>() );
                for ( Path ipath : IOUtils.lookupDir( folder, IOUtils.PATH_ACCEPT_IMAGE ) ) {
                    String fname = ipath.getFileName().toString().toLowerCase();


                    try {
                        Image img = IOUtils.loadImage( ipath );
                        imgM.get( imgDim ).putIfAbsent( fname, img );
                    } catch ( IOException e ) {

                    }

                }
            }

            return imgM;
        } );

    }
    /**
     *
     * @param primaryStage primary
     *
     */
    public void start( Stage primaryStage ) {

        loadResource(); // load resources

        final Scene scene = new Scene( MainPane.getInstance(), JBatGlobal.WIDTH_DEF, JBatGlobal.HEIGHT_DEF );

        primaryStage.setOnCloseRequest( event -> onClose() );

        JBatGlobal.MAIN_STAGE = primaryStage;
        primaryStage.setTitle("Java Batch Tool");
        primaryStage.setScene(scene);
        primaryStage.show();


    }
}
