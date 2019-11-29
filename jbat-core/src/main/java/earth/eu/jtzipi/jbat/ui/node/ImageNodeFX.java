package earth.eu.jtzipi.jbat.ui.node;

import earth.eu.jtzipi.modules.io.image.ImageDimension;
import javafx.beans.property.ObjectProperty;

import java.nio.file.Path;

public class ImageNodeFX {

    private Path path;
    private ObjectProperty<ImageDimension> fxImageDimProp;


    private ImageNodeFX( final Path path ) {

    }

    public static ImageNodeFX of( final Path path ) {

        return new ImageNodeFX( path );
    }


}
