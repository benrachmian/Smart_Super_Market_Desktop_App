package components.makeOrder.makeStaticOrder;

import SDMSystem.system.SDMSystem;
import SDMSystemDTO.customer.DTOCustomer;
import SDMSystemDTO.product.DTOProduct;
import SDMSystemDTO.product.DTOProductInStore;
import SDMSystemDTO.product.WayOfBuying;
import SDMSystemDTO.store.DTOStore;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class MakeStaticOrderController {

    private SDMSystem sdmSystem;
    private DTOCustomer customerMakingTheOrder;
    private DTOStore storeFromWhomTheOrderIsMade;
    private SimpleFloatProperty selectedProductPrice;
    private SimpleFloatProperty amountInTextField;

    @FXML private ScrollPane makeStaticOrderMainScrollPain;
    @FXML private Button continueButton;
    @FXML private ScrollPane storesScrollPane;
    @FXML private ScrollPane tableScrollPane;
    @FXML private TableView<?> storesTable11;
    @FXML private TableColumn<?, ?> storeNameCol11;
    @FXML private TableColumn<?, ?> storeIdCol11;
    @FXML private TableColumn<?, ?> locationCol11;
    @FXML private TableColumn<?, ?> ppkCol11;
    @FXML private ScrollPane storesScrollPane1;
    @FXML private ScrollPane tableScrollPane1;
    @FXML private TableView<?> storesTable1;
    @FXML private TableColumn<?, ?> storeNameCol1;
    @FXML private TableColumn<?, ?> storeIdCol1;
    @FXML private TableColumn<?, ?> locationCol1;
    @FXML private TableColumn<?, ?> ppkCol1;
    @FXML private ComboBox<?> chooseStoreComboBox1;
    @FXML private TextField priceTextField;
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

    @FXML
    public void initialize(){
        makeMainScrollPaneSettings();
        errorInputLabel.setAlignment(Pos.CENTER);
        errorInputLabel.visibleProperty().set(false);
        errorInputLabel.setManaged(false);
       zeroErrorLabel.setAlignment(Pos.CENTER);
       zeroErrorLabel.visibleProperty().set(false);
       zeroErrorLabel.setManaged(false);
       notIntegerErrorLabel.setAlignment(Pos.CENTER);
       notIntegerErrorLabel.visibleProperty().set(false);
       notIntegerErrorLabel.setManaged(false);

        //not allow to write chars that aren't digits
        amountTextField.disableProperty().bind(chooseProductComboBox.valueProperty().isNull());
        amountTextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(!newValue.matches("\\d*(\\.\\d*)?")) {
                    amountTextField.setText(oldValue);
                    errorInputLabel.visibleProperty().set(true);
                    errorInputLabel.setManaged(true);
                }
                else{
                    errorInputLabel.visibleProperty().set(false);
                    errorInputLabel.setManaged(false);
                    if(!amountTextField.getText().isEmpty() && !amountTextField.getText().equals(".")) {
                        amountInTextField.set(Float.parseFloat(amountTextField.getText()));
                    }
                }
            }
        });
        addToCartButton.disableProperty().bind(
                chooseProductComboBox.valueProperty().isNull().or(
                        amountTextField.textProperty().isEmpty()
                ));

        bindCostLabel();
        initProductsToBuyTableSettings();
        initProductsCartTableSettings();
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

    }

    @FXML void onClickContinue(ActionEvent event) {

    }

    public void initDetails() {
        initProductsToBuyTable();

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
        else if(chosenProduct.getWayOfBuying() == WayOfBuying.BY_QUANTITY && !isInteger(amountEntered)){
            notIntegerErrorLabel.visibleProperty().set(true);
            notIntegerErrorLabel.setManaged(true);
        }
        else {
            cartTable.getItems().add(new ProductInTable(
                    chosenProduct.getProductName(),
                    chosenProduct.getProductSerialNumber(),
                    chosenProduct.getWayOfBuying(),
                    null,
                    amountEntered
            ));
            amountTextField.setText("");
            chooseProductComboBox.getSelectionModel().clearSelection();
            zeroErrorLabel.visibleProperty().set(false);
            zeroErrorLabel.setManaged(false);
            notIntegerErrorLabel.visibleProperty().set(false);
            notIntegerErrorLabel.setManaged(false);
        }
    }

    private boolean isInteger(Float number) {
        return number % 1 == 0;// if the modulus(remainder of the division) of the argument(number) with 1 is 0 then return true otherwise false.
    }
}
