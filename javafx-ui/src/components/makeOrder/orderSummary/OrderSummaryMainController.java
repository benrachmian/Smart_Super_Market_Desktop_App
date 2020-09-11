package components.makeOrder.orderSummary;

import SDMSystem.location.LocationUtility;
import SDMSystem.system.SDMSystem;
import SDMSystemDTO.customer.DTOCustomer;
import SDMSystemDTO.product.DTOProductInStore;
import SDMSystemDTO.store.DTOStore;
import common.FxmlLoader;
import components.makeOrder.orderSummary.storeInOrderSummary.StoreInOrderSummaryController;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.util.Pair;

import java.util.Collection;
import java.util.Map;

public class OrderSummaryMainController {

    public static final String ORDER_SUMMARY_FORM_FXML_PATH = "/components/makeOrder/orderSummary/orderSummaryMain.fxml";
    private SDMSystem sdmSystem;
    private Map<Integer, Collection<Pair<DTOProductInStore, Float>>> shoppingCart;
    private GridPane storeInSummaryGridPane;
    private StoreInOrderSummaryController storeInOrderSummaryController;
    private DTOCustomer customerMakingTheOrder;



    @FXML private ScrollPane orderSummaryMainScrollPane;
    @FXML private VBox storesInOrderSummaryVBox;

    @FXML
    public void initialize(){
        orderSummaryMainScrollPane.setMinWidth(250);
        orderSummaryMainScrollPane.setMinHeight(250);
        orderSummaryMainScrollPane.setMaxHeight(1000);
        orderSummaryMainScrollPane.setMaxWidth(4000);
        orderSummaryMainScrollPane.fitToWidthProperty().set(true);
        orderSummaryMainScrollPane.fitToHeightProperty().set(true);
    }

    public void initDetails(Map<Integer, Collection<Pair<DTOProductInStore, Float>>> shoppingCart, DTOCustomer customerMakingTheOrder) {
        this.shoppingCart = shoppingCart;
        this.customerMakingTheOrder = customerMakingTheOrder;
        addStoresToSummary();

    }

    public void setSdmSystem(SDMSystem sdmSystem) {
        this.sdmSystem = sdmSystem;
    }

    private void addStoresToSummary() {
        for(Integer storeId : shoppingCart.keySet()){
            DTOStore storeInOrder = sdmSystem.getStoreFromStores(storeId);
            loadStoreInOrderSummaryForm();
            storesInOrderSummaryVBox.getChildren().add(storeInSummaryGridPane);
            storeInOrderSummaryController.initDetails(
                    storeInOrder,
                    LocationUtility.calcDistance(storeInOrder.getStoreLocation(),customerMakingTheOrder.getCustomerLocation()));

        }
    }

    private void loadStoreInOrderSummaryForm() {
        FxmlLoader<GridPane,StoreInOrderSummaryController> loaderStoreInOrderSummaryForm = new FxmlLoader<>(StoreInOrderSummaryController.STORE_IN_ORDER_SUMMARY_FORM_FXML_PATH);
        storeInSummaryGridPane = loaderStoreInOrderSummaryForm.getFormBasePane();
        storeInOrderSummaryController = loaderStoreInOrderSummaryForm.getFormController();
    }
}
