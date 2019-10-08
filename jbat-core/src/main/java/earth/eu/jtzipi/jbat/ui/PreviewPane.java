package earth.eu.jtzipi.jbat.ui;

import earth.eu.jtzipi.jbat.JBatGlobal;
import earth.eu.jtzipi.jbat.image.GraphicsUtilities;
import earth.eu.jtzipi.modules.io.IOUtils;
import earth.eu.jtzipi.modules.node.path.IPathNode;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;

/**
 *
 */
public class PreviewPane extends Pane {
    private static final Logger  LOG = LoggerFactory.getLogger( PreviewPane.class );

    private ImageView previewView;
    private IPathNode imagePath;
    private boolean aspectRatio;


    PreviewPane() {

        createPreviewPane();
        JBatGlobal.FX_PATH_PROP.addListener( (obs, oldPath, newPath) -> onPathChanged( oldPath, newPath ) );
    }

    private void createPreviewPane() {

        previewView = new ImageView();
getChildren().add(previewView);
    }

    private void onPathChanged( final IPathNode oldPath, final IPathNode newPath ) {
        if( null != newPath && oldPath != newPath ) {




            Path path = newPath.getValue();
            boolean img = IOUtils.isImage( path );
            LOG.info( "Bild Gadi" + img );
            if( img ) {
try {

    BufferedImage bufImg = GraphicsUtilities.loadCompatibleImage( path.toUri().toURL() );
    BufferedImage thBufImg = GraphicsUtilities.createThumbnail( bufImg, 57 );
    previewView.setImage( SwingFXUtils.toFXImage( thBufImg, null ) );
} catch ( final IOException ioe ) {
LOG.warn( ioe.getMessage() );
}



        } }

    }
}
