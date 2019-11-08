package earth.eu.jtzipi.jbat.ui.task;

import earth.eu.jtzipi.modules.io.IOUtils;
import earth.eu.jtzipi.modules.io.task.FindPathTask;
import earth.eu.jtzipi.modules.io.task.TaskIO;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.*;

/**
 * SearchTask.
 *
 * JavaFX Task based async computation.
 * <br>
 * We use a dir to start search for image files.
 * For each sub dir we start a separate thread via a {@link CompletionService}.
 * Meanwhile dir is searched for image files.
 * Later we wait for all threads returning.
 *
 * @author jTzipi
 */
public class SearchTask extends Task<List<Path>> {

    private static final Logger Log = LoggerFactory.getLogger( "SearchTask" );

    private final Path dir; // readable directory

    /**
     * Search Task for dir.
     * @param dir directory
     */
    private SearchTask( final Path dir ) {
        this.dir = dir;
    }

    /**
     * Create new search task for dir.
     *
     * @param dir dir
     * @return created task
     * @throws IOException {@code dir} is not readable or is not a dir
     */
    public static SearchTask of( final Path dir ) throws IOException {
        Objects.requireNonNull( dir );
        if ( !Files.isReadable( dir ) ) {
            throw new IOException( "Path '" + dir + "' can't be read" );
        }

        if ( !Files.isDirectory( dir, LinkOption.NOFOLLOW_LINKS ) ) {
            Log.warn( "Path no 'dir'" );
            throw new IOException( "No dir '" + dir + "'" );
        }

        return new SearchTask( dir );
    }

    public List<Path> call() throws InterruptedException {

        // get all sub dirs of dir
        List<Path> pathL = IOUtils.lookupDir( dir, IOUtils.PATH_ACCEPT_DIR );
        Log.debug( "Start '" + pathL );

        // nothing to search for else
        if( pathL.isEmpty() ) {
            Log.info( "No dirs found in '" + dir + "' scan only this dir" );
            return scanDir(dir);
        }

        int cpu = Runtime.getRuntime().availableProcessors();
        // for each dir store a list of found path
        // use completion service
        CompletionService<List<Path>> pcs = new ExecutorCompletionService<>( Executors.newFixedThreadPool(cpu ) );
        // dirs we can scan
        int scanDir = 0;
        // try to scan each dir
        for ( Path path : pathL ) {

            try {
                FindPathTask fpt = FindPathTask.of( path, IOUtils.PATH_ACCEPT_IMAGE );

                pcs.submit( fpt );
                scanDir++;

            } catch ( IOException ioE ) {
                Log.warn( "Error scanning dir '" + path, ioE );
            }

        }
        // Here we can
        // do something
        // since we do search for images in sub dirs we need to search also this dir for image
        //

        // return list
        List<Path> foundImageL = new ArrayList<>( scanDir(dir) );
        updateValue(  foundImageL );

        try {
            // for all path we added wait for completion
            for ( int gadi = 0; gadi < scanDir; gadi++ ) {
                // take next ready task
                List<Path> list = pcs.take().get();
                Log.debug( "Ready with task  " + gadi );
                // Update user info
                updateProgress( gadi, scanDir );
// found some
            if( !list.isEmpty()) {
                Log.warn( "Dir '" + dir.relativize( list.get( 0 ) ) + "' hat " + list.size() + " Img" );
            }


                 updateValue(  list  );

                 foundImageL.addAll( list );
            }


            return foundImageL;
        } catch ( final ExecutionException eE ) {

            throw TaskIO.launderThrowable( eE );
        }
    }

    private static List<Path> scanDir( final Path dir) {
        return IOUtils.lookupDir( dir, IOUtils.PATH_ACCEPT_IMAGE );
    }

}
