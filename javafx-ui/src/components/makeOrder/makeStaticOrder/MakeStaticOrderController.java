package components.makeOrder.makeStaticOrder;

import SDMSystem.system.SDMSystem;
import SDMSystemDTO.customer.DTOCustomer;
import SDMSystemDTO.product.DTOProductInStore;
import SDMSystemDTO.store.DTOStore;
import javafx.beans.binding.Bindings;
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
    @FXML private TableView<ProductsToBuyTable> productsToBuyTable;
    @FXML private TableColumn<ProductsToBuyTable, String> productNameCol;
    @FXML private TableColumn<ProductsToBuyTable, Integer> productIdCol;
    @FXML private TableColumn<ProductsToBuyTable, String> wayOfBuyingCol;
    @FXML private TableColumn<ProductsToBuyTable, Float> priceCol;
    @FXML private ComboBox<DTOProductInStore> chooseProductComboBox;
    @FXML private TextField amountTextField;
    @FXML private Label errorInputLabel;
    @FXML private Button addToCartButton;
    @FXML private Label productCostLabel;


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
        priceCol.setCellFactory(tc -> new TableCell<ProductsToBuyTable, Float>() {
            @Override
            protected void updateItem(Float priceCol, boolean empty) {
                super.updateItem(priceCol, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText(String.format("%.2f", priceCol));
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
            productsToBuyTable.getItems().add(new ProductsToBuyTable(
                    product.getProductName(),
                    product.getProductSerialNumber(),
                    product.getWayOfBuying(),
                    product.getPrice()
            ));
            chooseProductComboBox.getItems().add(product);
        }
    }

    @FXML
    void onSelectProduct(ActionEvent event) {
        selectedProductPrice.set(chooseProductComboBox.getValue().getPrice());
    }
}
