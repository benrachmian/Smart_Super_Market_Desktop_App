package components.details.storeDetails;

import SDMSystem.system.SDMSystem;
import SDMSystemDTO.store.DTOStore;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TabPane;

public class StoreDetailsController {

    private SDMSystem sdmSystem;

    @FXML private TabPane storeTabPane;
    @FXML private ScrollPane productTabScrollPane;
    @FXML private Label storeSerialNumberAnswerLabel;
    @FXML private Label storeNameAnswerLabel;
    @FXML private Label storePpkAnswerLabel;
    @FXML private Label totalProfitFromDeliveryAnswerLabel;

    public void setSdmSystem(SDMSystem sdmSystem) {
        this.sdmSystem = sdmSystem;
    }

    @FXML
    public void initialize(){
        productTabScrollPane.fitToWidthProperty().set(true);
    }

    public void updatePane(DTOStore store) {
        storeSerialNumberAnswerLabel.setText(String.format("%d",store.getStoreSerialNumber()));
        storeNameAnswerLabel.setText(store.getStoreName());
        storePpkAnswerLabel.setText(String.format("%.2f",store.getPpk()));
        totalProfitFromDeliveryAnswerLabel.setText(String.format("%.2f",store.getTotalProfitFromDelivery()));
    }
}
