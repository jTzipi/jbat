package earth.eu.jtzipi.jbat.ui.task;


import earth.eu.jtzipi.modules.io.image.ImageDimension;
import earth.eu.jtzipi.modules.utils.IBuilder;

import java.time.LocalDateTime;

public class SearchOption {
    public static final LocalDateTime MIN_DATE_TIME = LocalDateTime.of( 1970, 1, 1, 0, 0 );
    public static final LocalDateTime MAX_DATE_TIME = LocalDateTime.now().plusDays( 1 );

    public static final ImageDimension DEFAULT_IMAGE_DIMENSION = ImageDimension.of( 512, 512 );

    public static final long MIN_SIZE = 1L;
    public static final long MAX_SIZE = Long.MAX_VALUE;


    private LocalDateTime fromWhenLDT;
    private LocalDateTime untilWhenLDT;
    private ImageDimension imageDimension;
    private long minSize;
    private long maxSize;

    private SearchOption( Builder builder ) {

    }


    public static final class Builder implements IBuilder<SearchOption> {


        public SearchOption build() {

            return new SearchOption( this );
        }
    }
}
