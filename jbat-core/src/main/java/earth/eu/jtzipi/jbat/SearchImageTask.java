package earth.eu.jtzipi.jbat;

import earth.eu.jtzipi.modules.io.IOUtils;
import earth.eu.jtzipi.modules.node.path.IPathNode;
import javafx.concurrent.Task;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;
import java.util.concurrent.BlockingQueue;


public class SearchImageTask extends Task<List<IPathNode>> {



    private final Path dir;
    private FileSearchVisitor fsv;
    private BlockingQueue<Path> pathBQ;

    private SearchImageTask( final Path pathToDir ) {
        this.dir = pathToDir;

    }

    @Override
    protected List<IPathNode> call() throws Exception {

        Files.walkFileTree( dir, fsv  );

        return null;
    }

    private static final class FileSearchVisitor implements FileVisitor<Path> {

        private BlockingQueue<Path> bq;

        private FileSearchVisitor( final BlockingQueue<Path> bq ) {
            this.bq = bq;
        }

        private static boolean ir( final Path p ) {
            return Files.isReadable( p );
        }

        @Override
        public FileVisitResult preVisitDirectory( Path path, BasicFileAttributes basicFileAttributes ) throws IOException {


            return ir( path ) ? FileVisitResult.CONTINUE : FileVisitResult.SKIP_SUBTREE;
        }

        @Override
        public FileVisitResult visitFile( Path path, BasicFileAttributes basicFileAttributes ) throws IOException {

            if( IOUtils.isImage( path ) ) {
                bq.add( path );
            }

            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult visitFileFailed( Path path, IOException e ) throws IOException {

            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult postVisitDirectory( Path path, IOException e ) throws IOException {
            return FileVisitResult.CONTINUE;
        }
    }
}
