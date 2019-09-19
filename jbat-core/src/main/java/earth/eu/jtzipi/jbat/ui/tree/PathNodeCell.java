package earth.eu.jtzipi.jbat.ui.tree;

import earth.eu.jtzipi.jbat.node.path.IPathNode;
import javafx.scene.control.TreeCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class PathNodeCell extends TreeCell<IPathNode> {

    private ImageView iv;

    public PathNodeCell() {

    }

    @Override
    protected void updateItem(IPathNode item, boolean empty) {
        super.updateItem(item, empty);

        String text = "";
        Image image = null;

        if (null == item) {
            // System.out.println( "NULL" );
            iv = null;
        } else if (empty) {
            System.out.println("Empty");
            text = "IGN";
            iv = null;
        } else {
            text = item.getName();
            // image = item.getName() == "PC" ? IO.getImage( "PC") :  IO.getFileImageBySystem( item.getValue().toFile() );


            //iv = new ImageView(image );
            //iv.setPreserveRatio( true );
            //iv.setFitWidth(ImageSize.DEFAULT.getWidth());

        }


        setText(text);
        setGraphic(iv);
    }
}
