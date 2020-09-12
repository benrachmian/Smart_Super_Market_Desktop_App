package components.makeOrder.orderSummary.productInOrderSummary;

import SDMSystemDTO.product.DTOProductInDiscount;
import SDMSystemDTO.product.DTOProductInStore;
import SDMSystemDTO.product.IDTOProductInStore;
import SDMSystemDTO.product.WayOfBuying;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.util.Pair;

public class ProductInOrderSummaryController {
    public static final String STORE_IN_ORDER_SUMMARY_FORM_FXML_PATH = "/components/makeOrder/orderSummary/productInOrderSummary/productInOrderSummary.fxml";

    @FXML private Label productIdLabel;
    @FXML private Label productNameLabel;
    @FXML private Label wayOfBuyingLabel;
    @FXML private Label pricePerUnitLabel;
    @FXML private Label pricePerUnitAnswerLabel;
    @FXML private Label isPartOfDiscountLabel;
    @FXML private Label quantityLabel;
    @FXML private Label totalCostLabel;


    public void initDetails(Pair<IDTOProductInStore, Float> product) {
        boolean isPartOfDiscount = product.getKey() instanceof DTOProductInDiscount;
        productIdLabel.setText(Integer.toString(product.getKey().getProductSerialNumber()) );
        productNameLabel.setText(product.getKey().getProductName());
        wayOfBuyingLabel.setText(product.getKey().getWayOfBuying().toString());
        pricePerUnitLabel.setText(product.getKey().getWayOfBuying() == WayOfBuying.BY_QUANTITY ? "Price per unit:" : "Price per kilo:");
        pricePerUnitAnswerLabel.setText(Float.toString(product.getKey().getPrice()));
        isPartOfDiscountLabel.setText(isPartOfDiscount? "Yes" : "No");
        quantityLabel.setText(Float.toString(product.getValue()));
        totalCostLabel.setText(Float.toString(product.getValue() * product.getKey().getPrice()));
    }
}
