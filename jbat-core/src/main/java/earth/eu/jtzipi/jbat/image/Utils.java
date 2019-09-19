package earth.eu.jtzipi.jbat.image;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Objects;

public class Utils {

    /**
     * Convenience method that returns a scaled instance of the
     * provided BufferedImage.
     * <p>
     *     Attention:
     *     Copyright (c) 2015, Romain Guy
     *      All rights reserved.
     * </p>
     *
     * @param img the original image to be scaled
     * @param targetWidth the desired width of the scaled instance,
     *    in pixels
     * @param targetHeight the desired height of the scaled instance,
     *    in pixels
     * @param hint one of the rendering hint
     * @param progressiveBilinear if true, this method will use a multi-step
     *    scaling technique that provides higher quality than the usual
     *    one-step technique (only useful in down-scaling cases, where
     *    targetWidth or targetHeight is
     *    smaller than the original dimensions)
     * @return a scaled version of the original BufferedImage
     * @throws IllegalArgumentException if either {@code targetWidth} or {@code targetHeight} are {@literal < 1}.
     */
    private static BufferedImage scale( BufferedImage img,
                                        int targetWidth,
                                        int targetHeight,
                                        RenderingHint hint,
                                        boolean progressiveBilinear) {
        Objects.requireNonNull(img);
        // size
        if( 1 > targetWidth || 1 > targetHeight ) {

            throw new IllegalArgumentException("width[=+targetWidth+] or height[=+targetHeight+] is < 1");
        }
        // if null set to
        if( null == hint ) {
            hint = RenderingHint.BICU;
        }
        int type = (img.getTransparency() == Transparency.OPAQUE) ?
                BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB;


        BufferedImage ret = img;
        BufferedImage scratchImage = null;
        Graphics2D g2 = null;
        int w, h;
        int prevW = ret.getWidth();
        int prevH = ret.getHeight();
        boolean isTranslucent = img.getTransparency() !=  Transparency.OPAQUE;

        if (progressiveBilinear) {
            // Use multi-step technique: start with original size, then
            // scale down in multiple passes with drawImage()
            // until the target size is reached
            w = img.getWidth();
            h = img.getHeight();
        } else {
            // Use one-step technique: scale directly from original
            // size to target size with a single drawImage() call
            w = targetWidth;
            h = targetHeight;
        }

        do {
            if (progressiveBilinear && w > targetWidth) {
                w /= 2;
                if (w < targetWidth) {
                    w = targetWidth;
                }
            }

            if (progressiveBilinear && h > targetHeight) {
                h /= 2;
                if (h < targetHeight) {
                    h = targetHeight;
                }
            }

            if (scratchImage == null || isTranslucent) {
                // Use a single scratch buffer for all iterations
                // and then copy to the final, correctly-sized image
                // before returning
                scratchImage = new BufferedImage(w, h, type);
                g2 = scratchImage.createGraphics();
            }
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, hint);
            g2.drawImage(ret, 0, 0, w, h, 0, 0, prevW, prevH, null);
            prevW = w;
            prevH = h;

            ret = scratchImage;
        } while (w != targetWidth || h != targetHeight);

        if (g2 != null) {
            g2.dispose();
        }

        // If we used a scratch buffer that is larger than our target size,
        // create an image of the right size and copy the results into it
        if (targetWidth != ret.getWidth() || targetHeight != ret.getHeight()) {
            scratchImage = new BufferedImage(targetWidth, targetHeight, type);
            g2 = scratchImage.createGraphics();
            g2.drawImage(ret, 0, 0, null);
            g2.dispose();
            ret = scratchImage;
        }

        return ret;
    }

    public enum RenderingHint {
        NN( RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR ),
        BILI( RenderingHints.VALUE_INTERPOLATION_BILINEAR ),
        BICU( RenderingHints.VALUE_INTERPOLATION_BICUBIC  );

        private final Object ren;

        RenderingHint( Object renderingHint ) {
            this.ren = renderingHint;
        }
    }

}
