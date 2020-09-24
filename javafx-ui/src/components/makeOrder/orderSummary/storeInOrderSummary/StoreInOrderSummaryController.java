package components.makeOrder.orderSummary.storeInOrderSummary;

import SDMSystemDTO.product.DTOProductInStore;
import SDMSystemDTO.product.IDTOProductInStore;
import SDMSystemDTO.store.DTOStore;
import common.FxmlLoader;
import components.makeOrder.orderSummary.productInOrderSummary.ProductInOrderSummaryController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;

import java.util.Collection;

public class StoreInOrderSummaryController {

    public static final String STORE_IN_ORDER_SUMMARY_FORM_FXML_PATH = "/components/makeOrder/orderSummary/storeInOrderSummary/storeInOrderSummary.fxml";
    private GridPane productInOrderSummaryGridPane;
    private ProductInOrderSummaryController productInOrderSummaryController;

    @FXML private Label storeIdLabel;
    @FXML private Label PpkLabel;
    @FXML private Label distanceFromCustomerLabel;
    @FXML private Label deliveryCostLabel;
    @FXML private Label storeNameLabel;
    @FXML private FlowPane productsBoughtFromStoreFlowPane;
    @FXML private ScrollPane productsBoughtFromStoreScrollPane;

    @FXML
    public void initialize(){
        productsBoughtFromStoreScrollPane.fitToWidthProperty().set(true);
        productsBoughtFromStoreScrollPane.fitToHeightProperty().set(true);
    }

    public void initDetails(DTOStore storeInOrder, float distanceFromCustomer, Collection<Pair<IDTOProductInStore, Float>> productsBoughtFromStore) {
        storeNameLabel.setText(storeInOrder.getStoreName());
        storeIdLabel.setText(String.valueOf(storeInOrder.getStoreSerialNumber()));
        PpkLabel.setText(String.format("%.2f",storeInOrder.getPpk()));
        distanceFromCustomerLabel.setText(String.format("%.2f",distanceFromCustomer));
        deliveryCostLabel.setText(String.format("%.2f",distanceFromCustomer * storeInOrder.getPpk()));
        addProductsBoughtFromStore(productsBoughtFromStore);
    }

    private void addProductsBoughtFromStore(Collection<Pair<IDTOProductInStore, Float>> productsBoughtFromStore) {
        for(Pair<IDTOProductInStore,Float> product : productsBoughtFromStore){
            loadProductInOrderSummaryForm();
            productInOrderSummaryController.initDetails(product);
            productsBoughtFromStoreFlowPane.getChildren().add(productInOrderSummaryGridPane);
        }
    }

    private void loadProductInOrderSummaryForm() {
        FxmlLoader<GridPane,ProductInOrderSummaryController> loaderProductInOrderSummaryForm = new FxmlLoader<>(ProductInOrderSummaryController.STORE_IN_ORDER_SUMMARY_FORM_FXML_PATH);
        productInOrderSummaryGridPane = loaderProductInOrderSummaryForm.getFormBasePane();
        productInOrderSummaryController = loaderProductInOrderSummaryForm.getFormController();
    }
}
