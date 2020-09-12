package components.makeOrder.orderSummary;

import SDMSystem.location.LocationUtility;
import SDMSystem.system.SDMSystem;
import SDMSystemDTO.customer.DTOCustomer;
import SDMSystemDTO.product.DTOProductInStore;
import SDMSystemDTO.product.IDTOProductInStore;
import SDMSystemDTO.store.DTOStore;
import common.FxmlLoader;
import components.makeOrder.orderSummary.storeInOrderSummary.StoreInOrderSummaryController;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleFloatProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.util.Pair;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

public class OrderSummaryMainController {

    public static final String ORDER_SUMMARY_FORM_FXML_PATH = "/components/makeOrder/orderSummary/orderSummaryMain.fxml";
    private SDMSystem sdmSystem;
    private Map<Integer, Collection<Pair<IDTOProductInStore, Float>>> shoppingCart;
    private GridPane storeInSummaryGridPane;
    private StoreInOrderSummaryController storeInOrderSummaryController;
    private DTOCustomer customerMakingTheOrder;
    private SimpleFloatProperty totalProductsCost;
    private SimpleFloatProperty totalDeliveryCost;
    private boolean isStaticOrder;
    //for static order
    private DTOStore storeFromWhomTheOrderWasMade = null;
    //for orders:
    private LocalDate orderDate;



    @FXML private ScrollPane orderSummaryMainScrollPane;
    @FXML private VBox storesInOrderSummaryVBox;
    @FXML private ScrollPane storeAndProductsParticipatingScrollPane;
    @FXML private Label totalProductsCostLabel;
    @FXML private Label totalDeliveryCostLabel;
    @FXML private Label totalOrderCostLabel;

    @FXML
    public void initialize(){
        orderSummaryMainScrollPane.setMinWidth(250);
        orderSummaryMainScrollPane.setMinHeight(250);
        orderSummaryMainScrollPane.setMaxHeight(1000);
        orderSummaryMainScrollPane.setMaxWidth(4000);
        orderSummaryMainScrollPane.fitToWidthProperty().set(true);
        orderSummaryMainScrollPane.fitToHeightProperty().set(true);
        storeAndProductsParticipatingScrollPane.fitToWidthProperty().set(true);
        storeAndProductsParticipatingScrollPane.fitToHeightProperty().set(true);
    }

    public void initDetails(
            Map<Integer, Collection<Pair<IDTOProductInStore, Float>>> shoppingCart,
            DTOCustomer customerMakingTheOrder,
            SimpleFloatProperty totalProductsCost,
            SimpleFloatProperty totalDeliveryCost,
            boolean isStaticOrder,
            DTOStore storeFromWhomTheOrderWasMade,
            LocalDate orderDate) {
        this.shoppingCart = shoppingCart;
        this.customerMakingTheOrder = customerMakingTheOrder;
        totalProductsCostLabel.textProperty().bind(totalProductsCost.asString());
        totalDeliveryCostLabel.textProperty().bind(totalDeliveryCost.asString());
        totalOrderCostLabel.textProperty().bind(Bindings.add(totalDeliveryCost,totalProductsCost).asString());
        this.isStaticOrder = isStaticOrder;
        this.storeFromWhomTheOrderWasMade = storeFromWhomTheOrderWasMade;
        this.orderDate = orderDate;

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
                    LocationUtility.calcDistance(storeInOrder.getStoreLocation(),customerMakingTheOrder.getCustomerLocation()),
                    shoppingCart.get(storeId));

        }
    }

    private void loadStoreInOrderSummaryForm() {
        FxmlLoader<GridPane,StoreInOrderSummaryController> loaderStoreInOrderSummaryForm = new FxmlLoader<>(StoreInOrderSummaryController.STORE_IN_ORDER_SUMMARY_FORM_FXML_PATH);
        storeInSummaryGridPane = loaderStoreInOrderSummaryForm.getFormBasePane();
        storeInOrderSummaryController = loaderStoreInOrderSummaryForm.getFormController();
    }

    @FXML
    void onConfirmOrder(ActionEvent event) {
        if(isStaticOrder){
            sdmSystem.makeNewStaticOrder(
                    storeFromWhomTheOrderWasMade,
                    orderDate,
                    totalDeliveryCost.floatValue(),
                    shoppingCart.get(storeFromWhomTheOrderWasMade),
                    customerMakingTheOrder
            );
        }

    }
}
