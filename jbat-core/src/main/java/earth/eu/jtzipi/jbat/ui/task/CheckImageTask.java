package earth.eu.jtzipi.jbat.ui.task;

import earth.eu.jtzipi.modules.io.image.ImageDimension;
import earth.eu.jtzipi.modules.io.image.ImageUtils;

import java.nio.file.Path;
import java.util.concurrent.Callable;

public class CheckImageTask implements Callable<ImageDimension> {
    private final Path imgPath;

    private CheckImageTask( final Path path ) {
        this.imgPath = path;
    }

    @Override
    public ImageDimension call() throws Exception {

        return ImageUtils.getImageDimension( imgPath );
    }
}
