package earth.eu.jtzipi.jbat;

import earth.eu.jtzipi.modules.io.IOUtils;
import earth.eu.jtzipi.modules.io.RegUs;
import earth.eu.jtzipi.modules.io.image.ImageDimension;
import earth.eu.jtzipi.modules.node.path.IPathNode;
import earth.eu.jtzipi.modules.utils.Utils;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.util.function.Predicate;
import java.util.regex.Pattern;


/**
 * Filter for image path.
 * <p>
 * Global Filter to filter properties of image file.
 * <br>
 * <br>
 * To filter text we use a Pattern and Matcher.
 * <br>
 * we may filter for
 *          <ul>
 *              <li>text</li>
 *              <li>type (like png, jpg)</li>
 *              <li>size</li>
 *          </ul>
 * <p>
 *      Important: <br>
 * <p>
 *          Each time a property changed, the {@link Predicate<IPathNode>} changed.
 * </p>
 */
public class ImagePathFilter {

    private static final Predicate<IPathNode> TRUE = ip -> true;


    private String filterText = "";     // filter text
    private String pathType = null;     // extension
    private long size = -1L;            // file size
    private ImageDimension imgDim = ImageDimension.EMPTY;   // if image
    private boolean casi;       // case insensitive
    private boolean word;       // word boundary
    private boolean regx;       // regular
    private Pattern pattern;            // Pattern

    private final ObjectProperty<Predicate<IPathNode>> fxPathPredicateProp = new SimpleObjectProperty<>( this, "FX_PATH_PREDICATE_PROP", TRUE );

    ImagePathFilter() {

    }

    public void setWordBound( final boolean wordProp ) {
        if ( this.word != wordProp ) {
            this.word = wordProp;
            createFilter();
        }
    }

    /**
     * Set case insensitive property .
     *
     * @param casiProp case insensitive
     */
    public void setCaseInsensitive( final boolean casiProp ) {
        if ( this.casi != casiProp ) {
            this.casi = casiProp;

            createFilter();
        }
    }

    /**
     * Set regular expression property.
     *
     * @param regProp text is regular expression or not
     */
    public void setReg( final boolean regProp ) {
        if ( this.regx != regProp ) {
            this.regx = regProp;

            createFilter();
        }
    }

    /**
     * Set minimal file size.
     * <p>
     * Size will be clamped to [0, {@link Long#MAX_VALUE}].
     * </p>
     *
     * @param minSize minimal file size
     */
    public void setSize( final long minSize ) {
        Utils.clamp( minSize, 0L, Long.MAX_VALUE );
        if ( minSize != size ) {
            this.size = minSize;
            createFilter();

        }
    }

    /**
     * Set new filter text.
     * <p>
     * if {@code text} is null or empty filter will be reset.
     *
     * </p>
     *
     * @param text filter
     */
    public void setFilterText( String text ) {
        if ( null == text ) {
            text = "";
        }
        if ( !text.equals( filterText ) ) {
            this.filterText = text;
            createFilter();
        }
    }

    /**
     * Predicate Property.
     *
     * @return predicate property
     */
    public final ObjectProperty<Predicate<IPathNode>> getPathPredicatePropFX() {
        return fxPathPredicateProp;
    }

    /**
     * Create predicate based on properties.
     */
    private void createFilter() {
        // if empty reset filter
        if ( filterText.isEmpty() ) {
            this.fxPathPredicateProp.setValue( TRUE );
            return;
        }

        // escape meta if noreg
        if ( !this.regx ) {
            // first escape all meta chars
            for ( String meta : RegUs.META_MAP.keySet() ) {
                filterText.replace( meta, RegUs.META_MAP.get( meta ) );
            }
        }

        // Toggle case
        if ( casi ) {
            this.pattern = Pattern.compile( filterText, Pattern.CASE_INSENSITIVE );
        } else {
            this.pattern = Pattern.compile( filterText );
        }

        if ( this.word ) {
            this.filterText = "\\b" + filterText + "\\b";
        }

        Predicate<IPathNode> typePred = null == pathType ? TRUE : ip -> IOUtils.getPathSuffixSafe( ip.getValue() ).equalsIgnoreCase( pathType );
        Predicate<IPathNode> lp = 0L > size ? TRUE : ip -> IOUtils.getFileSizeSafe( ip.getValue() ) >= size;
        // Predicate<IPathNode> dimp = null == imgDim ? TRUE : ip -> ImageUtils.getImageDimension( ip.getValue() ).compareTo( imgDim ) >= 0;

        Predicate<IPathNode> tp = path -> pattern.matcher( path.getName() ).find();


        this.fxPathPredicateProp.setValue( tp.and( lp ).and( typePred ) );
    }
}
