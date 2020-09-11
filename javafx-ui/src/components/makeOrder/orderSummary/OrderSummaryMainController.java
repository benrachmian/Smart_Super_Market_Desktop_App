package components.makeOrder.orderSummary;

import SDMSystemDTO.product.DTOProductInStore;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.util.Pair;

import java.util.Collection;
import java.util.Map;

public class OrderSummaryMainController {

    public static final String ORDER_SUMMARY_FORM_FXML_PATH = "/components/makeOrder/orderSummary/orderSummaryMain.fxml";
    private Map<Integer, Collection<Pair<DTOProductInStore, Float>>> shoppingCart;



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

    public void initDetails(Map<Integer, Collection<Pair<DTOProductInStore, Float>>> shoppingCart) {
        this.shoppingCart = shoppingCart;
        addStoresToSummary();

    }

    private void addStoresToSummary() {

    }
}
