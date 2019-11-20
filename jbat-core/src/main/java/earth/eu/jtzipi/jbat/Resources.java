package earth.eu.jtzipi.jbat;


import earth.eu.jtzipi.modules.io.IOUtils;
import earth.eu.jtzipi.modules.io.image.ImageDimension;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.CompletableFuture;

/**
 * Resource Cache.
 *
 * @author jTzipi
 */
public final class Resources {

    private static final Path PATH_TO_RES = IOUtils.getProgramDir().resolve( "jbat-core/src/main/resources" );

    private static final Logger LOG = LoggerFactory.getLogger( "Resources" );
    private static final Properties PROP_UI = new Properties();             // Properties
    private static final List<String> FONT_LIST = new ArrayList<>(); // new HashMap<>();      // All fonts
    private static final Map<String, Map<Double, Font>> FONT_MAP = new HashMap<>();
    static final Map<ImageDimension, Map<String, Image>> ICON_MAP = new HashMap<>();


    static void loadResource() {

        // lookup font path
        CompletableFuture<List<String>> cfFM = loadFonts();

        cfFM.whenComplete( ( fL, t ) -> FONT_LIST.addAll( fL ) );
        // lookup icon path and sub path
        CompletableFuture<Map<ImageDimension, Map<String, Image>>> cfIM = loadImage();

        cfIM.whenComplete( ( map, t ) -> ICON_MAP.putAll( map ) );

        // load properties
        Path pathToPropUI = PATH_TO_RES.resolve( "properties/ui.properties" );

        try {
            Properties prop = IOUtils.loadProperties( pathToPropUI );
            PROP_UI.putAll( prop );
        } catch ( IOException ioE ) {
            LOG.warn( "Error ", ioE );
        }
    }

    private static CompletableFuture<List<String>> loadFonts() {
        return CompletableFuture.supplyAsync( () -> {


            Path path = PATH_TO_RES.resolve( "font" );
            List<String> fL = new ArrayList<>();

            for ( Path fp : IOUtils.lookupDir( path ) ) {

                String fn = fp.getFileName().toString();
                fL.add( fn );


            }

            return fL;

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
     * Return font for property descriptor.
     *
     * @param propertyDescriptor
     * @return font if found or default system font
     */
    public static Font getUIFont( String propertyDescriptor, double fs ) {
        String fontName = PROP_UI.getProperty( propertyDescriptor );

        if ( FONT_MAP.containsKey( propertyDescriptor ) && FONT_MAP.get( propertyDescriptor ).containsKey( fs ) ) {
            return FONT_MAP.get( propertyDescriptor ).get( fs );
        }

        Path fp = PATH_TO_RES.resolve( "font" ).resolve( fontName );
        Font font;
        try {
            font = IOUtils.loadFont( fp, fs );
            FONT_MAP.computeIfAbsent( propertyDescriptor, p -> new HashMap<>() ).putIfAbsent( fs, font );
        } catch ( IOException e ) {
            LOG.error( "Error loading font!", e );
            font = Font.font( fs );
        }
        LOG.info( "Lade '" + font );

        return font;
    }


}
