package earth.eu.jtzipi.jbat.node.path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.filechooser.FileSystemView;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public final class IOUtils {

    static final FileSystemView FSV = FileSystemView.getFileSystemView();
    static final DirectoryStream.Filter<Path> ACCEPT = path -> true;
    static final Predicate<Path> PATH_ACCEPT_ALL = path -> true;
    static final Predicate<Path> PATH_ACCEPT_DIR = path -> Files.isReadable( path ) && Files.isDirectory( path );
    private static final Logger LOG = LoggerFactory.getLogger( "IOUtils" );
    public static String readableFileSize(long size) {
        if(size <= 0) return "0";
        final String[] units = new String[] { "B", "kB", "MB", "GB", "TB" };
        int digitGroups = (int) (Math.log10(size)/Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size/Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }

    /**
     * Format bytes.
     * @param bytes byte
     * @param si standard unit
     * @return formatted file size
     */
    public static String formatFileSize( long bytes, boolean si ) {
        if( 0 >= bytes ) {
            return "0 B";
        }
        int unit = si ? 1000 : 1024;
        if( unit < bytes ) {
            return bytes + " B";
        }
        String unitSymbol = si ? "kMGT" : "KMGT";

        int exp = (int) (Math.log(bytes) / Math.log(unit));

        LOG.warn( "formatiere " + bytes + " " + exp );

        String pre = unitSymbol.charAt(exp - 1) +  ( si ? "" : "i" );
        return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
    }

    static String getFileName( final Path path ) {

        return FSV.getSystemDisplayName( path.toFile() );
    }

    static String getFileDescription( final Path path ) {
        return FSV.getSystemTypeDescription( path.toFile() );
    }

    static List<Path> lookupDir( final Path p ) {
        return IOUtils.lookupDir( p, null );
    }

    static List<Path> lookupDir( final Path p, Predicate<Path> pathPredicate ) {
        Objects.requireNonNull( p );
        DirectoryStream.Filter<Path> filter;
        if ( null != pathPredicate ) {
            filter = pathPredicate::test;
        } else {
            filter = ACCEPT;
        }
        List<Path> nodeL = new ArrayList<>();

        try ( final DirectoryStream<Path> ds = Files.newDirectoryStream( p, filter ) ) {

            for ( final Path path : ds ) {
                System.out.println( "Gadi : [" + path.toString() + "]" );
                nodeL.add( path );
            }

        } catch ( final IOException ioE ) {

        }


        return nodeL;
    }
}
