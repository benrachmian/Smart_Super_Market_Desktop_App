package components.makeOrder.makeStaticOrder;

import SDMSystem.system.SDMSystem;
import SDMSystemDTO.customer.DTOCustomer;
import SDMSystemDTO.product.DTOProductInStore;
import SDMSystemDTO.product.IDTOProductInStore;
import SDMSystemDTO.product.WayOfBuying;
import SDMSystemDTO.store.DTOStore;
import common.JavaFxHelper;
import components.makeOrder.MakeOrderMainController;
import components.makeOrder.discountsInOrder.DiscountsInOrderController;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleFloatProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Pair;

import java.util.Collection;

public class MakeStaticOrderController {

    private SDMSystem sdmSystem;
    private DTOCustomer customerMakingTheOrder;
    private DTOStore storeFromWhomTheOrderIsMade;
    private SimpleFloatProperty selectedProductPrice;
    private SimpleFloatProperty amountInTextField;
    private SimpleFloatProperty deliveryCost;
    private SimpleFloatProperty totalProductsCost;
    private MakeOrderMainController makeOrderMainController;
    private DiscountsInOrderController discountsInOrderController;
    //pair: key = product bought, value: amount
    private Collection<Pair<IDTOProductInStore, Float>> shoppingCart;

    @FXML private ScrollPane makeStaticOrderMainScrollPain;
    @FXML private Button continueButton;
//    @FXML private ScrollPane storesScrollPane;
//    @FXML private ScrollPane tableScrollPane;
//    @FXML private TableView<?> storesTable11;
//    @FXML private TableColumn<?, ?> storeNameCol11;
//    @FXML private TableColumn<?, ?> storeIdCol11;
//    @FXML private TableColumn<?, ?> locationCol11;
//    @FXML private TableColumn<?, ?> ppkCol11;
//    @FXML private ScrollPane storesScrollPane1;
//    @FXML private ScrollPane tableScrollPane1;
//    @FXML private TableView<?> storesTable1;
//    @FXML private TableColumn<?, ?> storeNameCol1;
//    @FXML private TableColumn<?, ?> storeIdCol1;
//    @FXML private TableColumn<?, ?> locationCol1;
//    @FXML private TableColumn<?, ?> ppkCol1;
//    @FXML private ComboBox<?> chooseStoreComboBox1;
//    @FXML private TextField priceTextField;
    @FXML private TableView<ProductInTable> productsToBuyTable;
    @FXML private TableColumn<ProductInTable, String> productNameCol;
    @FXML private TableColumn<ProductInTable, Integer> productIdCol;
    @FXML private TableColumn<ProductInTable, String> wayOfBuyingCol;
    @FXML private TableColumn<ProductInTable, Float> priceCol;
    @FXML private ComboBox<DTOProductInStore> chooseProductComboBox;
    @FXML private TextField amountTextField;
    @FXML private Label errorInputLabel;
    @FXML private Button addToCartButton;
    @FXML private Label productCostLabel;
    @FXML private TableColumn<ProductInTable, String> productNameCartTableCol;
    @FXML private TableColumn<ProductInTable, Integer> idCartTableCol;
    @FXML private TableColumn<ProductInTable, String> wayOfBuyingCartTableCol;
    @FXML private TableColumn<ProductInTable, Float> amountCartTableCol;
    @FXML private TableView<ProductInTable> cartTable;
    @FXML private Label zeroErrorLabel;
    @FXML private Label notIntegerErrorLabel;
    @FXML private Label deliveryCostLabel;
    @FXML private Label productsCostLabel;
    @FXML private Label totalOrderCostLabel;

    public MakeStaticOrderController() {
        selectedProductPrice = new SimpleFloatProperty();
        amountInTextField = new SimpleFloatProperty();
    }

    public void setCustomerMakingTheOrder(DTOCustomer customerMakingTheOrder) {
        this.customerMakingTheOrder = customerMakingTheOrder;
    }

    public void setStoreFromWhomTheOrderIsMade(DTOStore storeFromWhomTheOrderIsMade) {
        this.storeFromWhomTheOrderIsMade = storeFromWhomTheOrderIsMade;
    }

    public void setMakeOrderMainController(MakeOrderMainController makeOrderMainController) {
        this.makeOrderMainController = makeOrderMainController;
    }

    public void setShoppingCart(Collection<Pair<IDTOProductInStore,Float>> shoppingCart) {
        this.shoppingCart = shoppingCart;
    }

    public void setDeliveryCost(float deliveryCost) {
        this.deliveryCost.set(deliveryCost);
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

        bindCostLabel();
        initProductsToBuyTableSettings();
        initProductsCartTableSettings();
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
        makeStaticOrderMainScrollPain.setMinWidth(250);
        makeStaticOrderMainScrollPain.setMinHeight(250);
        makeStaticOrderMainScrollPain.setMaxHeight(1000);
        makeStaticOrderMainScrollPain.setMaxWidth(4000);
        makeStaticOrderMainScrollPain.fitToWidthProperty().set(true);
        makeStaticOrderMainScrollPain.fitToHeightProperty().set(true);
    }

    private void bindCostLabel() {
        productCostLabel.visibleProperty().bind(amountTextField.textProperty().isNotEmpty());
        productCostLabel.textProperty().bind(
                Bindings.multiply(
                        amountInTextField,selectedProductPrice
                ).asString());
    }

    private void initProductsToBuyTableSettings() {
        productNameCol.setCellValueFactory(new PropertyValueFactory<>("productName"));
        productIdCol.setCellValueFactory(new PropertyValueFactory<>("productId"));
        wayOfBuyingCol.setCellValueFactory(new PropertyValueFactory<>("wayOfBuying"));
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        //make the cell to present two digits after the dot
        priceCol.setCellFactory(tc -> new TableCell<ProductInTable, Float>() {
            @Override
            protected void updateItem(Float priceCol, boolean empty) {
                super.updateItem(priceCol, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText(String.format("%.2f", priceCol));
                }}});
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


    public void setSdmSystem(SDMSystem sdmSystem) {
        this.sdmSystem = sdmSystem;
    }

    @FXML void onClickCancel(ActionEvent event) {
        makeOrderMainController.cancelOrderAlert();
    }



    public void initDetails(SimpleFloatProperty totalProductsCost, SimpleFloatProperty totalDeliveryCost) {
        initProductsToBuyTable();
        this.totalProductsCost = totalProductsCost;
        this.deliveryCost = totalDeliveryCost;
        deliveryCostLabel.setText(String.format("%.2f",deliveryCost.get()));
        productsCostLabel.textProperty().bind(this.totalProductsCost.asString());
        totalOrderCostLabel.textProperty().bind(Bindings.add(deliveryCost, this.totalProductsCost).asString());
    }

    private void initProductsToBuyTable() {
        for(DTOProductInStore product : storeFromWhomTheOrderIsMade.getProductsInStore().values()){
            productsToBuyTable.getItems().add(new ProductInTable(
                    product.getProductName(),
                    product.getProductSerialNumber(),
                    product.getWayOfBuying(),
                    product.getPrice(),
                    null
            ));
            chooseProductComboBox.getItems().add(product);
        }
    }

    @FXML
    void onSelectProduct(ActionEvent event) {
        if(chooseProductComboBox.getValue() != null) {
            selectedProductPrice.set(chooseProductComboBox.getValue().getPrice());
        }
    }

    @FXML
    void onAddToCart(ActionEvent event) {
        Float amountEntered = Float.parseFloat(amountTextField.getText());
        DTOProductInStore chosenProduct = chooseProductComboBox.getValue();
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
            shoppingCart.add(new Pair(
                    storeFromWhomTheOrderIsMade.getProductFromStore(chosenProduct.getProductSerialNumber()),
                    amountEntered));
            totalProductsCost.set(totalProductsCost.get() + (chosenProduct.getPrice() * amountEntered));
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



    @FXML void onClickContinue(ActionEvent event) {
        if(sdmSystem.storeHasDiscountWithOneOfTheProducts(storeFromWhomTheOrderIsMade.getStoreSerialNumber(),shoppingCart)) {
            makeOrderMainController.createDiscountsInOrderForm(cartTable.getItems());
        }
        else{
            makeOrderMainController.createOrderSummaryForm();
        }
    }

    private void createDiscountsInOrderForm() {
        discountsInOrderController = makeOrderMainController.createDiscountsInOrderForm(cartTable.getItems());

    }
}
