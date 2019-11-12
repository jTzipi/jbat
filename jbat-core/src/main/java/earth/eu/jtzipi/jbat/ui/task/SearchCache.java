package earth.eu.jtzipi.jbat.ui.task;

import java.nio.file.Path;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class SearchCache {
    private static final ConcurrentMap<Path, SearchTask> taskMap = new ConcurrentHashMap<>();


    public static boolean isTask( final Path path ) {
        return taskMap.containsKey( path );
    }

    public static SearchTask putTask( final Path path, final SearchTask task ) {

        return taskMap.putIfAbsent( path, task );

    }
}
