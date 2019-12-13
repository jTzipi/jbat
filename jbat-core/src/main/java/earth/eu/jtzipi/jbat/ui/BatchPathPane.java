package earth.eu.jtzipi.jbat.ui;


import earth.eu.jtzipi.jbat.JBatGlobal;
import earth.eu.jtzipi.jbat.ui.tree.ImageNodeTreeItem;
import earth.eu.jtzipi.jbat.ui.treetable.PathTreeTableCell;
import earth.eu.jtzipi.jbat.ui.treetable.PathTypeTreeTableCell;
import earth.eu.jtzipi.modules.io.IOUtils;
import earth.eu.jtzipi.modules.io.image.ImageDimension;
import earth.eu.jtzipi.modules.io.task.FindPathTask;
import earth.eu.jtzipi.modules.io.task.PathCrawler;
import earth.eu.jtzipi.modules.io.task.TaskIO;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.cell.CheckBoxTreeTableCell;
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

    private static final ImageNodeTreeItem ROOT = ImageNodeTreeItem.of( IOUtils.getHomeDir() );


    private ObservableMap<Integer, SimpleBooleanProperty> checkedImageM = FXCollections.observableHashMap();
    private TreeTableView<Path> ttv;  // tree table for found path

    private TreeTableColumn<Path, Boolean> selTTC;
    private TreeTableColumn<Path, Path> fileTTC;
    private TreeTableColumn<Path, String> typeTTC;
    private TreeTableColumn<Path, Long> sizeTTC;
    private TreeTableColumn<Path, ImageDimension> dimTTC;


    BatchPathPane() {

        createPane();
    }

    private void createPane() {
        createBatchFileTableView();

        // treeViewDirM.put( IOUtils.getHomeDir(), ROOT );


        this.setCenter( ttv );
    }

    private void createBatchFileTableView() {

        // TreeItem<Path> root = ImageNodeTreeItem.of( IOUtils.getHomeDir() );

        ttv = new TreeTableView<>( ROOT );


        selTTC = new TreeTableColumn<>( "" );
        selTTC.setPrefWidth( 29D );
        selTTC.setCellFactory( cba -> new CheckBoxTreeTableCell<>() );

        /*Callback<TreeTableColumn.CellDataFeatures<Path, Boolean>, ObservableValue<Boolean>> callback = param -> {

            int row = param.getTreeTableView().getRow( param.getValue() );
            SimpleBooleanProperty old = checkedImageM.computeIfAbsent( row, ro -> new SimpleBooleanProperty() );
            old.setValue( !old.getValue() );


            checkedImageM.put( row, old );
            return old;
        };*/
        // selTTC.setCellValueFactory( callback );

        fileTTC = new TreeTableColumn<>( "File" );

        fileTTC.setCellValueFactory( new Callback<TreeTableColumn.CellDataFeatures<Path, Path>, ObservableValue<Path>>() {
            @Override
            public ObservableValue<Path> call( TreeTableColumn.CellDataFeatures<Path, Path> param ) {
                return new ReadOnlyObjectWrapper<>( param.getValue().getValue() );
            }
        } );
        fileTTC.setCellFactory( cab -> new PathTreeTableCell() );
        fileTTC.setPrefWidth( 500D );

        typeTTC = new TreeTableColumn<>( "Type" );
        typeTTC.setPrefWidth( 100D );
        typeTTC.setCellValueFactory( new Callback<TreeTableColumn.CellDataFeatures<Path, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call( TreeTableColumn.CellDataFeatures<Path, String> param ) {
                return new ReadOnlyStringWrapper( IOUtils.getPathSuffixSafe( param.getValue().getValue() ) );
            }
        } );
        typeTTC.setCellFactory( cb -> new PathTypeTreeTableCell() );

        sizeTTC = new TreeTableColumn<>( "Size" );
        sizeTTC.setPrefWidth( 150D );


        dimTTC = new TreeTableColumn<>( "Dim" );
        dimTTC.setPrefWidth( 150D );

        ttv.getColumns().add( selTTC );
        ttv.getColumns().add( fileTTC );
        ttv.getColumns().add( typeTTC );
        ttv.getColumns().add( sizeTTC );
        ttv.getColumns().add( dimTTC );


        ttv.setTreeColumn( fileTTC );
        ttv.setPlaceholder( createPlaceholder() );
        ttv.setOnDragEntered( de -> onDragEntered( de ) );
        ttv.setOnDragOver( de -> onDragOver( de ) );
        ttv.setOnDragDropped( de -> onDragDrop( de ) );
        ttv.setOnDragDone( de -> onDragDone( de ) );
    }

    private void onDragEntered( DragEvent dragEvent ) {

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

        // get files
        List<File> dropL = dragEvent.getDragboard().getFiles();

        //
        for ( File file : dropL ) {
            Path imagePath = file.toPath();
            LOG.warn( "P " + imagePath );

            // Path item to add
            ImageNodeTreeItem item = ROOT;
            Path rootPath = item.getValue();
            //
            // below home dir
            for ( Path subPath : rootPath.relativize( imagePath ) ) {

                Path absPath = item.getValue().resolve( subPath );
                LOG.warn( "Ig " + absPath );
                //

                item = item.addIfAbsent( absPath );
                // TODO: if image get Image dIm

                // } else {
                //     LOG.warn( "habe Image " + subPath );
                //     item.addIfAbsent( absPath );

                // }

            }


        }

        dragEvent.consume();
    }

    private void onDragDone( DragEvent dragEvent ) {

        dragEvent.consume();
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
            this.root = rootPath;


            this.sharedQ = sharedBlockQ;

        }

        protected Path call() {

            // scan for directories
            List<Path> dirs = IOUtils.lookupDir( root );
            // none
            if ( dirs.isEmpty() ) {
                return null;
            }

            for ( final Path dir : dirs ) {

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
            for ( ; ; ) {
                try {
                    Path imgPath = sharedQ.take();
                    if ( PathCrawler.__NULL__ == imgPath ) {
                        countdown--;
                        // last dir finished
                        if ( countdown == 0 ) {
                            break;
                        }
                    }
                } catch ( final InterruptedException e ) {
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

        private LookForImageTask( final Path rootPath ) {
            this.root = rootPath;
            // this.service = Executors.newFixedThreadPool( Runtime.getRuntime().availableProcessors() );

        }

        @Override
        protected Long call() {

            List<Path> dirs = IOUtils.lookupDir( root );
            if ( dirs.isEmpty() ) {
                return 0L;
            }

            long match = 0L;
            int threads = TaskIO.CPUS;
            service = Executors.newFixedThreadPool( threads );
            CompletionService<List<Path>> imgCSer =
                    new ExecutorCompletionService<>( service );


            for ( Path dir : dirs ) {
                try {


                    FindPathTask fpt = FindPathTask.of( dir, imgPred );
                    Future<List<Path>> ft = imgCSer.submit( fpt );
                    ftM.put( dir, ft );

                } catch ( final IOException ioe ) {

                }
            }
            // while search...

            for ( int t = 0; t < ftM.size(); t++ ) {

                try {
                    Future<List<Path>> res = imgCSer.take();        // get next result

                    match += res.get().size();                      // Increase
                    updateValue( match );                           // Update
                } catch ( ExecutionException eE ) {
                    cancel( true );
                    LOG.error( "Error ", eE );
                } catch ( InterruptedException iE ) {
                    Thread.currentThread().interrupt();
                    LOG.error( "break ", iE );

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
