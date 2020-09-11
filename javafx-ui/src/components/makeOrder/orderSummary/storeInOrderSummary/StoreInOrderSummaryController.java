package components.makeOrder.orderSummary.storeInOrderSummary;

import SDMSystemDTO.store.DTOStore;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class StoreInOrderSummaryController {

    public static final String STORE_IN_ORDER_SUMMARY_FORM_FXML_PATH = "/components/makeOrder/orderSummary/storeInOrderSummary/storeInOrderSummary.fxml";

    @FXML private Label storeIdLabel;
    @FXML private Label PpkLabel;
    @FXML private Label distanceFromCustomerLabel;
    @FXML private Label deliveryCostLabel;
    @FXML private Label storeNameLabel;

    public void initDetails(DTOStore storeInOrder, float distanceFromCustomer) {
        storeIdLabel.setText(storeInOrder.getStoreName());
        PpkLabel.setText(String.format("%.2f",storeInOrder.getPpk()));
        distanceFromCustomerLabel.setText(String.format("%.2f",distanceFromCustomer));
        deliveryCostLabel.setText(String.format("%.2f",distanceFromCustomer * storeInOrder.getPpk()));
    }
}
