package components.makeOrder;

import SDMSystem.location.LocationUtility;
import SDMSystem.system.SDMSystem;
import SDMSystemDTO.customer.DTOCustomer;
import SDMSystemDTO.product.DTOProductInStore;
import SDMSystemDTO.product.IDTOProductInStore;
import SDMSystemDTO.store.DTOStore;
import common.FxmlLoader;
import components.makeOrder.discountsInOrder.DiscountsInOrderController;
import components.makeOrder.makeDynamicOrder.MakeDynamicOrderController;
import components.makeOrder.makeStaticOrder.MakeStaticOrderController;
import components.makeOrder.makeStaticOrder.ProductInTable;
import components.makeOrder.orderSummary.OrderSummaryMainController;
import javafx.beans.property.SimpleFloatProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.util.Pair;

import java.time.LocalDate;
import java.util.*;

public class MakeOrderMainController {

    public enum OrderType {
        STATIC_ORDER {
            @Override
            public String toString() {
                return "Static order";
            }
        }, DYNAMIC_ORDER{
            @Override
            public String toString() {
                return "Dynamic order";
            }
        }
    }

    private SDMSystem sdmSystem;
    private BorderPane mainBorderPane;
    private static final String STATIC_ORDER_FORM_FXML_PATH = "/components/makeOrder/makeStaticOrder/makeStaticOrder.fxml";
    private static final String DISCOUNTS_IN_ORDER_FORM_FXML_PATH = "/components/makeOrder/discountsInOrder/discountsInOrder.fxml";
    private ScrollPane staticOrderFormScrollPane;
    private MakeStaticOrderController makeStaticOrderController;
    private ScrollPane dynamicOrderFormScrollPane;
    private MakeDynamicOrderController makeDynamicOrderController;
    private ScrollPane discountsInOrderScrollPane;
    private DiscountsInOrderController discountsInOrderController;
    private ScrollPane orderSummaryMainScrollPane;
    private OrderSummaryMainController orderSummaryMainController;
    private DTOCustomer customerMakingTheOrder;
    //Map: key = storeID, value: Collection of products bought from the store
    //pair: key = product bought, value: amount
    private Map<Integer, Collection<Pair<IDTOProductInStore, Float>>> shoppingCart;
    private SimpleFloatProperty totalDeliveryCost;
    private SimpleFloatProperty totalProductsCost;
    private boolean isStaticOrder;
    //for static order:
    private DTOStore storeFromWhomTheOrderWasMade = null;
    //for all orders:
    private LocalDate orderDate;


    @FXML private ScrollPane makeOrderMainScrollPain;
    @FXML private ScrollPane storesScrollPane;
    @FXML private ComboBox<DTOCustomer> chooseCustomerComboBox;
    @FXML private Button continueButton;
    @FXML private DatePicker pickDateBox;
    @FXML private ComboBox<OrderType> orderTypeComboBox;
    @FXML private TableView<StoreInTable> storesTable;
    @FXML private TableColumn<StoreInTable, String> storeNameCol;
    @FXML private TableColumn<StoreInTable, String> storeIdCol;
    @FXML private TableColumn<StoreInTable, String> locationCol;
    @FXML private TableColumn<StoreInTable, String> ppkCol;
    @FXML private TableColumn<SimpleFloatProperty, Float> deliveryCostCol;
    @FXML private ScrollPane tableScrollPane;
    @FXML private ComboBox<DTOStore> chooseStoreComboBox;

    public MakeOrderMainController() {
        shoppingCart = new HashMap<>();
        totalDeliveryCost = new SimpleFloatProperty();
        totalProductsCost = new SimpleFloatProperty();
    }

    @FXML
    public void initialize(){
        initMainScrollPainSettings();
        initContinueButtonBinding();
        orderTypeComboBox.getItems().add(OrderType.STATIC_ORDER);
        orderTypeComboBox.getItems().add(OrderType.DYNAMIC_ORDER);
        storesScrollPane.visibleProperty().bind(orderTypeComboBox.valueProperty().isEqualTo(OrderType.STATIC_ORDER));
        initTableSettings();
    }

    private void initContinueButtonBinding() {
        continueButton.disableProperty().bind(
                //customer wasn't chosen
                chooseCustomerComboBox.valueProperty().isNull().or(
                        //date wasn't chosen
                        pickDateBox.valueProperty().isNull().or(
                                //order type wasn't chosen
                                orderTypeComboBox.valueProperty().isNull().or(
                                        //static order was chosen and store wasn't chosen
                                        orderTypeComboBox.valueProperty().isEqualTo(OrderType.STATIC_ORDER).and(
                                                chooseStoreComboBox.valueProperty().isNull())
                                )
                        )
                ));
    }

    private void initMainScrollPainSettings() {
        makeOrderMainScrollPain.setMinWidth(250);
        makeOrderMainScrollPain.setMinHeight(250);
        makeOrderMainScrollPain.setMaxHeight(1000);
        makeOrderMainScrollPain.setMaxWidth(4000);
        makeOrderMainScrollPain.fitToWidthProperty().set(true);
        makeOrderMainScrollPain.fitToHeightProperty().set(true);
    }

    private void initTableSettings() {
        tableScrollPane.fitToHeightProperty().set(true);
        tableScrollPane.fitToWidthProperty().set(true);
        storeNameCol.setCellValueFactory(new PropertyValueFactory<>("storeName"));
        storeIdCol.setCellValueFactory(new PropertyValueFactory<>("storeId"));
        locationCol.setCellValueFactory(new PropertyValueFactory<>("storeLocation"));
        ppkCol.setCellValueFactory(new PropertyValueFactory<>("storePPK"));
        deliveryCostCol.setCellValueFactory(new PropertyValueFactory<>("deliveryCost"));
        //make the cell to present two digits after the dot
        deliveryCostCol.setCellFactory(tc -> new TableCell<SimpleFloatProperty, Float>() {
            @Override
            protected void updateItem(Float deliveryCost, boolean empty) {
                super.updateItem(deliveryCost, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText(String.format("%.2f", deliveryCost.floatValue()));
                }}});
    }

    public void setSdmSystem(SDMSystem sdmSystem) {
        this.sdmSystem = sdmSystem;
    }


    public void initDetails() {
        initChooseCustomerComboBox();
        initStoresTableWithDetails();
    }

    private void initStoresTableWithDetails() {
        for(DTOStore store : sdmSystem.getStoresInSystemBySerialNumber().values()){
            storesTable.getItems().add(new StoreInTable(
                    store.getStoreName(),
                    store.getStoreSerialNumber(),
                    store.getStoreLocation(),
                    store.getPpk()
            ));
            chooseStoreComboBox.getItems().add(store);
        }
    }

    private void initChooseCustomerComboBox() {
        for(DTOCustomer customer : sdmSystem.getCustomers().values()){
            chooseCustomerComboBox.getItems().add(customer);
        }

    }

    @FXML
    void chooseCustomerAction(ActionEvent event) {
        DTOCustomer chosenCustomer = chooseCustomerComboBox.getValue();
        for(StoreInTable storeInTable : storesTable.getItems()){
            storeInTable.setDeliveryCost(
                    LocationUtility.calcDistance(chosenCustomer.getCustomerLocation(),storeInTable.getStoreLocationInPoint())
                            * storeInTable.getStorePPK());
        }
        customerMakingTheOrder = chosenCustomer;
    }

    public void setMainBorderPane(BorderPane mainBorderPane) {
        this.mainBorderPane = mainBorderPane;
    }

    @FXML
    void onClickCancel(ActionEvent event) {
        cancelOrderAlert();
    }

    public void cancelOrderAlert() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Cancel Warning");
        alert.setHeaderText("You are about to cancel the order");
        alert.setContentText("You can't undo the action.\nAre you sure about it?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            mainBorderPane.setCenter(null);
        }
    }

    @FXML
    void onClickContinue(ActionEvent event) {
        if(orderTypeComboBox.selectionModelProperty().getValue().getSelectedItem().equals(OrderType.STATIC_ORDER)){
            createStaticOrderForm();
            isStaticOrder = true;
        }
        else{
            createDynamicOrderForm();
            isStaticOrder = false;
        }
    }

    private void createDynamicOrderForm() {
        loadMakeDynamicOrderForm();
        mainBorderPane.setCenter(dynamicOrderFormScrollPane);
        makeDynamicOrderController.initDetails(
                sdmSystem,
                customerMakingTheOrder,
                this,
                shoppingCart,
                totalProductsCost,
                totalDeliveryCost
        );
    }




    private void createStaticOrderForm() {
        loadMakeStaticOrderForm();
        mainBorderPane.setCenter(staticOrderFormScrollPane);
        makeStaticOrderController.setSdmSystem(sdmSystem);
        makeStaticOrderController.setCustomerMakingTheOrder(chooseCustomerComboBox.getValue());
        storeFromWhomTheOrderWasMade = chooseStoreComboBox.getValue();
        makeStaticOrderController.setStoreFromWhomTheOrderIsMade(storeFromWhomTheOrderWasMade);
        makeStaticOrderController.setMakeOrderMainController(this);
        totalDeliveryCost.set(LocationUtility.calcDistance(
                chooseStoreComboBox.getValue().getStoreLocation(), chooseCustomerComboBox.getValue().getCustomerLocation())
                * chooseStoreComboBox.getValue().getPpk());
        Collection<Pair<IDTOProductInStore, Float>> shoppingCartFromStaticOrder = new LinkedList<>();
        shoppingCart.put(chooseStoreComboBox.getValue().getStoreSerialNumber(),shoppingCartFromStaticOrder);
        makeStaticOrderController.setShoppingCart(shoppingCartFromStaticOrder);
        makeStaticOrderController.initDetails(totalProductsCost,totalDeliveryCost);
    }

    public DiscountsInOrderController createDiscountsInOrderForm(ObservableList<ProductInTable> productsInTable) {
        loadDiscountsInOrderForm();
        mainBorderPane.setCenter(discountsInOrderScrollPane);
        discountsInOrderController.updateItemsInCartTable(productsInTable);
        discountsInOrderController.setSdmSystem(sdmSystem);
        discountsInOrderController.setMakeOrderMainController(this);
        discountsInOrderController.initDetails(
                totalDeliveryCost,
                totalProductsCost,
                shoppingCart
        );
        return discountsInOrderController;
    }

    private void loadDiscountsInOrderForm() {
        FxmlLoader<ScrollPane,DiscountsInOrderController> loaderDiscountsInOrderForm = new FxmlLoader<>(DISCOUNTS_IN_ORDER_FORM_FXML_PATH);
        discountsInOrderScrollPane = loaderDiscountsInOrderForm.getFormBasePane();
        discountsInOrderController = loaderDiscountsInOrderForm.getFormController();
    }

    private void loadMakeDynamicOrderForm() {
        FxmlLoader<ScrollPane,MakeDynamicOrderController> loaderMakeDynamicForm = new FxmlLoader<>(MakeDynamicOrderController.DYNAMIC_ORDER_FORM_FXML_PATH);
        dynamicOrderFormScrollPane = loaderMakeDynamicForm.getFormBasePane();
        makeDynamicOrderController = loaderMakeDynamicForm.getFormController();

    }

    private void loadMakeStaticOrderForm() {
        FxmlLoader<ScrollPane,MakeStaticOrderController> loaderMakeStaticOrderForm = new FxmlLoader<>(STATIC_ORDER_FORM_FXML_PATH);
        staticOrderFormScrollPane = loaderMakeStaticOrderForm.getFormBasePane();
        makeStaticOrderController = loaderMakeStaticOrderForm.getFormController();
    }

    public void createOrderSummaryForm() {
        loadOrderSummaryForm();
        mainBorderPane.setCenter(orderSummaryMainScrollPane);
        orderSummaryMainController.setSdmSystem(sdmSystem);
        orderSummaryMainController.initDetails(
                shoppingCart,
                customerMakingTheOrder,
                totalProductsCost,
                totalDeliveryCost,
                isStaticOrder,
                storeFromWhomTheOrderWasMade,
                orderDate,
                mainBorderPane
                );
    }

    private void loadOrderSummaryForm() {
        FxmlLoader<ScrollPane,OrderSummaryMainController> loaderOrderSummaryForm = new FxmlLoader<>(OrderSummaryMainController.ORDER_SUMMARY_FORM_FXML_PATH);
        orderSummaryMainScrollPane = loaderOrderSummaryForm.getFormBasePane();
        orderSummaryMainController = loaderOrderSummaryForm.getFormController();
    }

    @FXML
    void onPickDate(ActionEvent event) {
        orderDate = pickDateBox.getValue();
    }

}
