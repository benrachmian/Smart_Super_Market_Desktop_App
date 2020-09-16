package components.main.map;

import SDMSystemDTO.customer.DTOCustomer;
import SDMSystemDTO.store.DTOStore;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class CustomerInfoController {

    public static final String CUSTOMER_INFO_FXML_PATH = "/components/main/map/customerInfo.fxml";

    @FXML
    private Button okButton;
    @FXML private Label idLabel;
    @FXML private Label totalOrdersLabel;
    @FXML private Label storeNameLabel;

    @FXML
    void onClickOk(ActionEvent event) {
        Stage stage = (Stage) okButton.getScene().getWindow();
        stage.close();

    }

    public void initDetails(DTOCustomer customer) {
        idLabel.setText(String.valueOf(customer.getCustomerSerialNumber()));
        storeNameLabel.setText(customer.getCustomerName());
        totalOrdersLabel.setText(String.valueOf(customer.getTotalOrders()));
    }

}
