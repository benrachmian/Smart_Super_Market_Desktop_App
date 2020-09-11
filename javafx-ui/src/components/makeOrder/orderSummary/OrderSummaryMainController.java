package components.makeOrder.orderSummary;

import SDMSystemDTO.product.DTOProduct;
import components.makeOrder.MakeOrderMainController;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.util.Pair;

import java.util.Collection;

public class OrderSummaryMainController {

    Collection<Pair<DTOProduct, Float>> shoppingCart;
    public static final String ORDER_SUMMARY_FORM_FXML_PATH = "/components/makeOrder/orderSummary/orderSummaryMain.fxml";


    @FXML private ScrollPane orderSummaryMainScrollPane;

    @FXML
    public void initialize(){
        orderSummaryMainScrollPane.setMinWidth(250);
        orderSummaryMainScrollPane.setMinHeight(250);
        orderSummaryMainScrollPane.setMaxHeight(1000);
        orderSummaryMainScrollPane.setMaxWidth(4000);
        orderSummaryMainScrollPane.fitToWidthProperty().set(true);
        orderSummaryMainScrollPane.fitToHeightProperty().set(true);
    }

    public void initDetails(Collection<Pair<DTOProduct, Float>> shoppingCart) {
        this.shoppingCart = shoppingCart;
        addStoresToSummary();

    }

    private void addStoresToSummary() {

    }
}
