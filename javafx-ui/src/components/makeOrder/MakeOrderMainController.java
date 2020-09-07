package components.makeOrder;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;

public class MakeOrderMainController {
    @FXML private ScrollPane makeOrderMainScrollPain;
    @FXML private ScrollPane storesScrollPane;

    @FXML
    public void initialize(){
        makeOrderMainScrollPain.setMinWidth(250);
        makeOrderMainScrollPain.setMinHeight(250);
        makeOrderMainScrollPain.setMaxHeight(1000);
        makeOrderMainScrollPain.setMaxWidth(4000);
        makeOrderMainScrollPain.fitToWidthProperty().set(true);
        makeOrderMainScrollPain.fitToHeightProperty().set(true);
        storesScrollPane.visibleProperty().set(false);
    }
}
