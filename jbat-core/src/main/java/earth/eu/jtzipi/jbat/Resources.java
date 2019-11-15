package earth.eu.jtzipi.jbat;


import earth.eu.jtzipi.modules.io.image.ImageDimension;
import javafx.scene.image.Image;
import javafx.scene.text.Font;

import java.util.HashMap;
import java.util.Map;

public class Resources {

    private static final Map<String, Font> FONT_MAP = new HashMap<>();

    private static final Map<ImageDimension, Map<String, Image>> ICON_MAP = new HashMap<>();

    static void setImages( final Map<ImageDimension, Map<String, Image>> imgMap ) {
        ICON_MAP.putAll( imgMap );
    }

    static void setFonts( final Map<String, Font> fontMap ) {
        FONT_MAP.putAll( fontMap );
    }

}
