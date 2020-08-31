package components.details.customersDetails;

import SDMSystem.system.SDMSystem;
import SDMSystemDTO.customer.DTOCustomer;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class CustomerDetailsController {


    @FXML
    private Label IdAnswerLabel;

    @FXML
    private Label customerNameAnswerLabel;

    @FXML
    private Label customerLocationAnswerLabel;

    @FXML
    private Label numOfOrdersMadeAnswerLabel;

    @FXML
    private Label averageOrdersProductCostAnswerLabel;

    @FXML
    private Label averageOrdersDeliveryCostAnswerLabel;

    private SDMSystem sdmSystem;
    private final String NO_ORDERS = "The customer hasn't made any orders yet!";


    public void setSdmSystem(SDMSystem sdmSystem) {
        this.sdmSystem = sdmSystem;
    }

    public void updateGrid(DTOCustomer selectedItem) {
        IdAnswerLabel.setText(String.format("%d",selectedItem.getCustomerSerialNumber()));
        customerNameAnswerLabel.setText(selectedItem.getCustomerName());
        customerLocationAnswerLabel.setText(selectedItem.getCustomerLocationToString());
        if(selectedItem.getOrdersMade().size() > 0) {
            numOfOrdersMadeAnswerLabel.setText(String.format("%d", selectedItem.getOrdersMade().size()));
            averageOrdersProductCostAnswerLabel.setText(String.format("%.2f", sdmSystem.getOrdersProductCostAverage(selectedItem.getCustomerSerialNumber())));
            averageOrdersDeliveryCostAnswerLabel.setText(String.format("%.2f", sdmSystem.getOrdersDeliveryCostAverage(selectedItem.getCustomerSerialNumber())));
        }
        else {
            numOfOrdersMadeAnswerLabel.setText(NO_ORDERS);
            averageOrdersProductCostAnswerLabel.setText(NO_ORDERS);
            averageOrdersDeliveryCostAnswerLabel.setText(NO_ORDERS);
        }
    }
}
