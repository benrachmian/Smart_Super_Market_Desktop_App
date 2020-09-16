package components.main.map;

import SDMSystemDTO.store.DTOStore;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class StoreInfoController {
    public static final String STORE_INFO_FXML_PATH = "/components/main/map/storeInfo.fxml";



    @FXML private Button okButton;
    @FXML private Label idLabel;
    @FXML private Label ppkLabel;
    @FXML private Label totalOrdersLabel;
    @FXML private Label storeNameLabel;

    @FXML
    void onClickOk(ActionEvent event) {
        Stage stage = (Stage) okButton.getScene().getWindow();
        stage.close();

    }

    public void initDetails(DTOStore store) {
        idLabel.setText(String.valueOf(store.getStoreSerialNumber()));
        storeNameLabel.setText(store.getStoreName());
        ppkLabel.setText(String.valueOf(store.getPpk()));
        totalOrdersLabel.setText(String.valueOf(store.getTotalOrders()));
    }
}
