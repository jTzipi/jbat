package earth.eu.jtzipi.jbat.ui;


import earth.eu.jtzipi.jbat.JBatGlobal;
import earth.eu.jtzipi.jbat.ui.tree.ImageNodeTreeItem;
import earth.eu.jtzipi.modules.io.IOUtils;
import earth.eu.jtzipi.modules.io.image.ImageDimension;
import earth.eu.jtzipi.modules.io.task.FindPathTask;
import earth.eu.jtzipi.modules.io.task.PathCrawler;
import earth.eu.jtzipi.modules.io.task.TaskIO;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.*;
import javafx.scene.text.TextAlignment;
import javafx.util.Callback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.function.Predicate;

/**
 * Pane for Adding Files to later Batch format.
 *
 * @author jTzipi
 */
public class BatchPathPane extends BorderPane {

    private static final Logger LOG = LoggerFactory.getLogger( "BPPane" );
    private static final Border DOTTED_BORDER = new Border( new BorderStroke( Painter.COLOR_GRAY_47, BorderStrokeStyle.DASHED, new CornerRadii( 14D ), new BorderWidths( 3D ) ) );


    static final ImageNodeTreeItem ROOT = ImageNodeTreeItem.of( IOUtils.getHomeDir() );
    Map<Path, ImageNodeTreeItem> treeViewDirM = new HashMap<>(); // folder and list of img

    ObservableList<ImageNodeTreeItem> imageNodeL;
    private TreeTableView<Path> ttv;  // tree table for found path
    private TreeTableColumn<Path, Path> fileTTC;
    private TreeTableColumn<Path, String> typeTTC;
    private TreeTableColumn<Path, Long> sizeTTC;
    private TreeTableColumn<Path, ImageDimension> dimTTC;


    BatchPathPane(  ) {

        createPane();
    }

    private void createPane() {
        createBatchFileTableView();

        treeViewDirM.put( IOUtils.getHomeDir(), ROOT );
        ttv.setOnDragEntered( de -> onDragEntered( de ) );
        ttv.setOnDragOver( de -> onDragOver( de ) );
        ttv.setOnDragDropped( de -> onDragDrop( de ) );

        ttv.setOnDragDone( de -> onDragDone( de ) );


        this.setCenter( ttv );
    }

    private void createBatchFileTableView() {

        // TreeItem<Path> root = ImageNodeTreeItem.of( IOUtils.getHomeDir() );

        ttv = new TreeTableView<>( ROOT );
        // ttv.setShowRoot( true );
        fileTTC = new TreeTableColumn<>( "File" );
        fileTTC.setCellValueFactory( new Callback<TreeTableColumn.CellDataFeatures<Path, Path>, ObservableValue<Path>>() {
            @Override
            public ObservableValue<Path> call( TreeTableColumn.CellDataFeatures<Path, Path> param ) {
                return new ReadOnlyObjectWrapper<>( param.getValue().getValue() );
            }
        } );
        typeTTC = new TreeTableColumn<>( "" );
        sizeTTC = new TreeTableColumn<>( "" );
        dimTTC = new TreeTableColumn<>( "Dim" );

        ttv.getColumns().add( fileTTC );


        ttv.setTreeColumn( fileTTC );
        ttv.setPlaceholder( createPlaceholder() );



    }

    private void onDragEntered( DragEvent dragEvent ) {
        LOG.warn( dragEvent.getDragboard().getContentTypes() + "" );
        if ( ttv != dragEvent.getGestureSource() ) {

        }

        dragEvent.consume();
    }

    private void onDragOver( DragEvent dragEvent ) {


        if ( dragEvent.getDragboard().hasFiles() ) {
            dragEvent.acceptTransferModes( TransferMode.COPY );
        }

        dragEvent.consume();
    }

    private void onDragDrop( DragEvent dragEvent ) {
        LOG.warn( dragEvent.getDragboard().getFiles().toString() );
        List<File> dropL = dragEvent.getDragboard().getFiles();


        for ( File file : dropL ) {

            // to path and relative to home dir
            Path relPath = IOUtils.getHomeDir().relativize( file.toPath() ); //
            Path root = relPath.getRoot();


            LOG.info( root.normalize().toString() );
            ImageNodeTreeItem tti = treeViewDirM.putIfAbsent( root, ImageNodeTreeItem.of( root ) );
            ROOT.addIfAbesent( tti );
            Iterator<Path> pit = relPath.iterator();
            while ( pit.hasNext() ) {

                Path next = pit.next();

                ImageNodeTreeItem imageItem = treeViewDirM.putIfAbsent( next, ImageNodeTreeItem.of( next ) );
                tti.addIfAbesent( imageItem );
                tti = imageItem;


            }


        }

        dragEvent.consume();
    }

    private void onDragDone( DragEvent dragEvent ) {


    }

    private void onDragExited( DragEvent dragEvent ) {

    }

    private static Node createPlaceholder() {

        Label label = new Label( "Drag & Drop Images here" );
        label.setBorder( DOTTED_BORDER );
        label.setOpaqueInsets( new Insets( 5D ) );
        label.setPrefSize( 500D, 90D );
        label.setTextAlignment( TextAlignment.CENTER );
        label.setAlignment( Pos.CENTER );
        label.fontProperty().bind( JBatGlobal.FX_FONT_MAIN_PROP );
        return label;
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
