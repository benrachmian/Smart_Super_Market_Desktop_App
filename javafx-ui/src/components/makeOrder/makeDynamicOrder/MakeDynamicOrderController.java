package components.makeOrder.makeDynamicOrder;

import SDMSystem.system.SDMSystem;
import SDMSystemDTO.customer.DTOCustomer;
import SDMSystemDTO.product.DTOProduct;
import SDMSystemDTO.product.IDTOProductInStore;
import SDMSystemDTO.product.WayOfBuying;
import SDMSystemDTO.store.DTOStore;
import common.FxmlLoader;
import common.JavaFxHelper;
import components.makeOrder.MakeOrderMainController;
import components.makeOrder.discountsInOrder.DiscountsInOrderController;
import components.makeOrder.makeDynamicOrder.storesParticipatingInDyanmicOrder.StoresParticipatingInDynamicOrderController;
import components.makeOrder.makeStaticOrder.ProductInTable;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleFloatProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.util.Pair;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;

public class MakeDynamicOrderController {

    public static final String DYNAMIC_ORDER_FORM_FXML_PATH = "/components/makeOrder/makeDynamicOrder/makeDynamicOrder.fxml";
    private SDMSystem sdmSystem;
    private DTOCustomer customerMakingTheOrder;
    private DTOStore storeFromWhomTheOrderIsMade;
    private SimpleFloatProperty selectedProductPrice;
    private SimpleFloatProperty amountInTextField;
    private SimpleFloatProperty totalDeliveryCost;
    private SimpleFloatProperty totalProductsCost;
    private MakeOrderMainController makeOrderMainController;
    private DiscountsInOrderController discountsInOrderController;
    //pair: key = product bought, value: amount
    private Map<Integer, Collection<Pair<IDTOProductInStore, Float>>> cheapestBasket;
    private Collection<Pair<DTOProduct, Float>> productsInOrder;
    private ScrollPane storesParticipatingInDynamicOrderScrollPane;
    private StoresParticipatingInDynamicOrderController storesParticipatingInDynamicOrderController;
    private BorderPane mainBorderPane;



    @FXML private ScrollPane makeDynamicOrderMainScrollPain;
    @FXML private Button continueButton;
    @FXML private ComboBox<DTOProduct> chooseProductComboBox;
    @FXML private TextField amountTextField;
    @FXML private Label errorInputLabel;
    @FXML private Label zeroErrorLabel;
    @FXML private Label notIntegerErrorLabel;
    @FXML private Label productCostLabel;
    @FXML private Button addToCartButton;
    @FXML private TableView<ProductInTable> productsToBuyTable;
    @FXML private TableColumn<ProductInTable, String> productNameCol;
    @FXML private TableColumn<ProductInTable, Integer> productIdCol;
    @FXML private TableColumn<ProductInTable, String> wayOfBuyingCol;
    @FXML private TableView<ProductInTable> cartTable;
    @FXML private TableColumn<ProductInTable, String> productNameCartTableCol;
    @FXML private TableColumn<ProductInTable, Integer> idCartTableCol;
    @FXML private TableColumn<ProductInTable, String> wayOfBuyingCartTableCol;
    @FXML private TableColumn<ProductInTable, Float> amountCartTableCol;

    public MakeDynamicOrderController() {
        productsInOrder = new LinkedList<>();
        amountInTextField = new SimpleFloatProperty();
    }

    @FXML
    public void initialize(){
        makeMainScrollPaneSettings();
        initErrorsLabel();
        continueButton.disableProperty().bind(Bindings.isEmpty(cartTable.getItems()));

        amountTextField.disableProperty().bind(chooseProductComboBox.valueProperty().isNull());
        JavaFxHelper.makeTextFieldInputOnlyFloat(amountTextField,errorInputLabel,amountInTextField);
//        amountTextField.textProperty().addListener(new ChangeListener<String>() {
//            @Override
//            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
//                if(!newValue.matches("\\d*(\\.\\d*)?")) {
//                    amountTextField.setText(oldValue);
//                    errorInputLabel.visibleProperty().set(true);
//                    errorInputLabel.setManaged(true);
//                }
//                else{
//                    errorInputLabel.visibleProperty().set(false);
//                    errorInputLabel.setManaged(false);
//                    if(!amountTextField.getText().isEmpty() && !amountTextField.getText().equals(".")) {
//                        amountInTextField.set(Float.parseFloat(amountTextField.getText()));
//                    }
//                }
//            }
//        });
        addToCartButton.disableProperty().bind(
                chooseProductComboBox.valueProperty().isNull().or(
                        amountTextField.textProperty().isEmpty()
                ));

        initProductsToBuyTableSettings();
        initProductsCartTableSettings();
    }

    private void initProductsCartTableSettings() {
        productNameCartTableCol.setCellValueFactory(new PropertyValueFactory<>("productName"));
        idCartTableCol.setCellValueFactory(new PropertyValueFactory<>("productId"));
        wayOfBuyingCartTableCol.setCellValueFactory(new PropertyValueFactory<>("wayOfBuying"));
        amountCartTableCol.setCellValueFactory(new PropertyValueFactory<>("amount"));
        //make the cell to present two digits after the dot
        amountCartTableCol.setCellFactory(tc -> new TableCell<ProductInTable, Float>() {
            @Override
            protected void updateItem(Float amountCartTableCol, boolean empty) {
                super.updateItem(amountCartTableCol, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText(String.format("%.2f", amountCartTableCol));
                }}});
    }

    private void initProductsToBuyTableSettings() {
        productNameCol.setCellValueFactory(new PropertyValueFactory<>("productName"));
        productIdCol.setCellValueFactory(new PropertyValueFactory<>("productId"));
        wayOfBuyingCol.setCellValueFactory(new PropertyValueFactory<>("wayOfBuying"));
    }

    private void initErrorsLabel() {
        initErrorLabel(errorInputLabel);
        initErrorLabel(zeroErrorLabel);
        initErrorLabel(notIntegerErrorLabel);
    }

    private void initErrorLabel(Label errorLabel) {
        errorLabel.setAlignment(Pos.CENTER);
        errorLabel.visibleProperty().set(false);
        errorLabel.setManaged(false);
    }

    private void makeMainScrollPaneSettings() {
        makeDynamicOrderMainScrollPain.setMinWidth(250);
        makeDynamicOrderMainScrollPain.setMinHeight(250);
        makeDynamicOrderMainScrollPain.setMaxHeight(1000);
        makeDynamicOrderMainScrollPain.setMaxWidth(4000);
        makeDynamicOrderMainScrollPain.fitToWidthProperty().set(true);
        makeDynamicOrderMainScrollPain.fitToHeightProperty().set(true);
    }


    @FXML
    void onAddToCart(ActionEvent event) {
        Float amountEntered = Float.parseFloat(amountTextField.getText());
        DTOProduct chosenProduct = chooseProductComboBox.getValue();
        if(amountEntered <= 0){
            zeroErrorLabel.visibleProperty().set(true);
            zeroErrorLabel.setManaged(true);
        }
        else if(chosenProduct.getWayOfBuying() == WayOfBuying.BY_QUANTITY && !JavaFxHelper.isInteger(amountEntered)){
            notIntegerErrorLabel.visibleProperty().set(true);
            notIntegerErrorLabel.setManaged(true);
        }
        else {
            //add product to table
            cartTable.getItems().add(new ProductInTable(
                    chosenProduct.getProductName(),
                    chosenProduct.getProductSerialNumber(),
                    chosenProduct.getWayOfBuying(),
                    null,
                    amountEntered
            ));
            //add product to shopping cart
            productsInOrder.add(new Pair(chosenProduct,amountEntered));
            clearLabelsAfterAddingProductToCart();
        }
    }

    private void clearLabelsAfterAddingProductToCart() {
        amountTextField.setText("");
        chooseProductComboBox.getSelectionModel().clearSelection();
        zeroErrorLabel.visibleProperty().set(false);
        zeroErrorLabel.setManaged(false);
        notIntegerErrorLabel.visibleProperty().set(false);
        notIntegerErrorLabel.setManaged(false);
    }

    @FXML
    void onClickCancel(ActionEvent event) {
        makeOrderMainController.cancelOrderAlert();
    }

    @FXML
    void onClickContinue(ActionEvent event) {
        cheapestBasket = sdmSystem.getCheapestBasket(productsInOrder);
        createStoresParticipatingInDynamicOrderForm();

    }

    private void createStoresParticipatingInDynamicOrderForm() {
        loadStoresParticipatingInDynamicOrderForm();
        mainBorderPane.setCenter(storesParticipatingInDynamicOrderScrollPane);
        storesParticipatingInDynamicOrderController.initDetails(sdmSystem,cheapestBasket,customerMakingTheOrder);

    }

    private void loadStoresParticipatingInDynamicOrderForm() {
        FxmlLoader<ScrollPane,StoresParticipatingInDynamicOrderController> loaderStoresParticipatingInDynamicOrderForm = new FxmlLoader<>(StoresParticipatingInDynamicOrderController.STORES_PARTICIPATING_IN_DYNAMIC_ORDER_FORM_FXML_PATH);
        storesParticipatingInDynamicOrderScrollPane = loaderStoresParticipatingInDynamicOrderForm.getFormBasePane();
        storesParticipatingInDynamicOrderController = loaderStoresParticipatingInDynamicOrderForm.getFormController();
    }

    @FXML
    void onSelectProduct(ActionEvent event) {

    }

    public void initDetails(SDMSystem sdmSystem,
                            DTOCustomer customerMakingTheOrder,
                            MakeOrderMainController makeOrderMainController,
                            Map<Integer, Collection<Pair<IDTOProductInStore, Float>>> shoppingCart,
                            SimpleFloatProperty totalProductsCost,
                            SimpleFloatProperty totalDeliveryCost,
                            BorderPane mainBorderPane) {
        this.sdmSystem = sdmSystem;
        this.customerMakingTheOrder = customerMakingTheOrder;
        this.makeOrderMainController = makeOrderMainController;
        this.cheapestBasket = shoppingCart;
        this.totalProductsCost = totalProductsCost;
        this.totalDeliveryCost = totalDeliveryCost;
        this.mainBorderPane = mainBorderPane;
        initProductsToBuyTable();
    }

    private void initProductsToBuyTable() {
        for(DTOProduct product : sdmSystem.getProductsInSystem().values()){
            productsToBuyTable.getItems().add(new ProductInTable(
                    product.getProductName(),
                    product.getProductSerialNumber(),
                    product.getWayOfBuying(),
                    null,
                    null
            ));
            chooseProductComboBox.getItems().add(product);
        }
    }
}
