package earth.eu.jtzipi.jbat.ui;


import earth.eu.jtzipi.modules.io.IOUtils;
import earth.eu.jtzipi.modules.io.task.FindPathTask;
import earth.eu.jtzipi.modules.io.task.PathCrawler;
import earth.eu.jtzipi.modules.io.task.TaskIO;
import earth.eu.jtzipi.modules.node.path.IPathNode;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.layout.BorderPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.function.Predicate;


public class BatchPathPane extends BorderPane {



    private TreeTableView<IPathNode> ttv;  // tree table for found path
    private ObservableList<IPathNode> foundImgOL = FXCollections.observableArrayList();

    BatchPathPane(  ) {

        this.ttv = createBatchFileTableView();
        this.setCenter( ttv );

    }

    private TreeTableView<IPathNode> createBatchFileTableView() {

        TreeItem<IPathNode> root = new TreeItem<>();
        TreeTableView<IPathNode> ttv = new TreeTableView<>(root);

        TreeTableColumn<IPathNode, IPathNode> nameTTC = new TreeTableColumn<>("File");


        ttv.getColumns().add( nameTTC );

        return ttv;
    }

    static class CrawlForImageTask extends Task<Path> {

        private Logger LOG = LoggerFactory.getLogger( "Task" );

        private ExecutorService service;

        private Path root;
        // path predicate
        private Predicate<Path> imgPred = path -> IOUtils.isImage( path );
        private BlockingQueue<Path> sharedQ;
        // map of futures for each dir we search
        // this is for cancelling search task for specific dir
        private Map<Path, Future<Void>> ftM;
private CrawlForImageTask( final Path rootPath, final BlockingQueue<Path> sharedBlockQ ) {
    this.root =rootPath;


this.sharedQ = sharedBlockQ;

}

        protected Path call( ) {

        // scan for directories
            List<Path> dirs = IOUtils.lookupDir( root );
            // none
            if( dirs.isEmpty() ) {
                return null;
            }

            for( final Path dir : dirs ) {

try {
    Future<Void> ft = service.submit( PathCrawler.of( root, imgPred, sharedQ ) );
    ftM.put( dir, ft );
} catch ( final IOException ioE ) {

    ftM.put( dir, null );
}
            }
 int countdown = ftM.size();
            // until all dirs ready
            // we check this via countdown
            for( ; ; ) {
try {
               Path imgPath = sharedQ.take();
    if( PathCrawler.__NULL__ == imgPath ) {
countdown--;
        // last dir finished
        if (countdown == 0) {
        break;
        }
    }
            }catch ( final InterruptedException e ) {
Thread.currentThread().interrupt();
            }

            }



            return null;
        }
        @Override
        protected void succeeded() {
            quit();
        }

        @Override
        protected void cancelled() {
            quit();
        }

        @Override
        protected void failed() {
            quit();
        }

        private final void quit() {
            this.service.shutdown();

        }

    }
    static class LookForImageTask extends Task<Long> {

        private Logger LOG = LoggerFactory.getLogger( "Task" );

        private ExecutorService service;

        private Path root;
        // path predicate
        private Predicate<Path> imgPred = path -> IOUtils.isImage( path );
        // map of futures for each dir we search
        // this is for cancelling search task for specific dir
        private Map<Path, Future<List<Path>>> ftM;

        private LookForImageTask(final Path rootPath) {
            this.root = rootPath;
           // this.service = Executors.newFixedThreadPool( Runtime.getRuntime().availableProcessors() );

        }

        @Override
        protected Long call()  {

            List<Path> dirs = IOUtils.lookupDir( root );
            if( dirs.isEmpty() ) {
                return 0L;
            }

            long match = 0L;
            int threads = TaskIO.CPUS;
            service = Executors.newFixedThreadPool( threads );
            CompletionService<List<Path>> imgCSer =
                    new ExecutorCompletionService<>(service);


            for( Path dir : dirs ) {
                try {


                    FindPathTask fpt = FindPathTask.of( dir, imgPred );
                    Future<List<Path>> ft = imgCSer.submit( fpt );
                    ftM.put( dir, ft );

                }catch ( final IOException ioe ) {

                }
            }
            // while search...

            for( int t = 0; t < ftM.size(); t++ ) {

                try {
                    Future<List<Path>> res = imgCSer.take();        // get next result

                    match += res.get().size();                      // Increase
                    updateValue( match );                           // Update
                } catch ( ExecutionException eE  ) {
cancel( true );
LOG.error( "Error ",eE );
                } catch ( InterruptedException iE ) {
Thread.currentThread().interrupt();LOG.error( "break ", iE  );

                }


            }

            return match;
        }

        @Override
        protected void succeeded() {
            quit();
        }

        @Override
        protected void cancelled() {
            quit();
        }

        @Override
        protected void failed() {
            quit();
        }

        private final void quit() {
            this.service.shutdown();

        }
    }
}
