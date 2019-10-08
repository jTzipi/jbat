package earth.eu.jtzipi.jbat.image;

import javafx.concurrent.Task;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;


public class WriteImageTask extends Task<Boolean> {


    private final Path path;
    private final BufferedImage img;

    private WriteImageTask( Path pathToWrite, BufferedImage bufImg ) {
    this.path  =  pathToWrite;
    this.img = bufImg;
    }

    @Override
    protected Boolean call() throws Exception {

        try {
            return ImageIO.write( img, path.getFileName().toString(), path.toFile()  );
        } catch ( final IOException ioe ) {
setException( ioe );
            return false;
        }

    }
}
