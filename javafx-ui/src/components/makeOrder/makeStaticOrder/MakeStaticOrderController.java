package components.makeOrder.makeStaticOrder;

import SDMSystem.system.SDMSystem;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class MakeStaticOrderController {

    private SDMSystem sdmSystem;

    @FXML private ScrollPane makeStaticOrderMainScrollPain;
    @FXML private Button continueButton;
    @FXML private ScrollPane storesScrollPane;
    @FXML private ScrollPane tableScrollPane;
    @FXML private TableView<?> storesTable11;
    @FXML private TableColumn<?, ?> storeNameCol11;
    @FXML private TableColumn<?, ?> storeIdCol11;
    @FXML private TableColumn<?, ?> locationCol11;
    @FXML private TableColumn<?, ?> ppkCol11;
    @FXML private ScrollPane storesScrollPane1;
    @FXML private ScrollPane tableScrollPane1;
    @FXML private TableView<?> storesTable1;
    @FXML private TableColumn<?, ?> storeNameCol1;
    @FXML private TableColumn<?, ?> storeIdCol1;
    @FXML private TableColumn<?, ?> locationCol1;
    @FXML private TableColumn<?, ?> ppkCol1;
    @FXML private ComboBox<?> chooseStoreComboBox1;
    @FXML private TextField priceTextField;


    @FXML
    public void initialize(){
        makeStaticOrderMainScrollPain.setMinWidth(250);
        makeStaticOrderMainScrollPain.setMinHeight(250);
        makeStaticOrderMainScrollPain.setMaxHeight(1000);
        makeStaticOrderMainScrollPain.setMaxWidth(4000);
        makeStaticOrderMainScrollPain.fitToWidthProperty().set(true);
        makeStaticOrderMainScrollPain.fitToHeightProperty().set(true);
    }

    public void setSdmSystem(SDMSystem sdmSystem) {
        this.sdmSystem = sdmSystem;
    }

    @FXML void onClickCancel(ActionEvent event) {

    }

    @FXML void onClickContinue(ActionEvent event) {

    }
}
