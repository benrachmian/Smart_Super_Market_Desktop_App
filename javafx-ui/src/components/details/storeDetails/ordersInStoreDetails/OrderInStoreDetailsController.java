package components.details.storeDetails.ordersInStoreDetails;

import SDMSystemDTO.order.DTOOrder;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import java.time.LocalDate;

public class OrderInStoreDetailsController {

    public static final String ORDER_IN_STORE_DETAILS_FXML_PATH = "/components/details/storeDetails/ordersInStoreDetails/orderInStoreDetails.fxml";


    @FXML private GridPane productGridPane;
    @FXML private Label orderDateLabel;
    @FXML private Label numOfProductsLabel;
    @FXML private Label productsCostLabel;
    @FXML private Label deliveryCostLabel;
    @FXML private Label orderCostLabel;
    @FXML private Label theOrderIsPartOfLabel;
    @FXML private Label mainOrderIdLabel;

    public void initDetails(LocalDate orderDate,
                            int amountOfProducts,
                            float productsCost,
                            float deliveryCost,
                            float orderCost,
                            DTOOrder mainOrder){
        orderDateLabel.setText(orderDate.toString());
        numOfProductsLabel.setText(String.valueOf(amountOfProducts));
        productsCostLabel.setText(String.valueOf(productsCost));
        deliveryCostLabel.setText(String.valueOf(deliveryCost));
        orderCostLabel.setText(String.valueOf(orderCost));
        if(mainOrder != null){
            mainOrderIdLabel.setText(String.valueOf(mainOrder.getOrderSerialNumber()));

        }
        else{
            theOrderIsPartOfLabel.visibleProperty().set(false);
            mainOrderIdLabel.visibleProperty().set(false);
            theOrderIsPartOfLabel.setManaged(false);
            mainOrderIdLabel.setManaged(false);
        }

    }


}
