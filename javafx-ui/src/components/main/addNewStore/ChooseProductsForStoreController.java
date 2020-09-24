package components.main.addNewStore;

import SDMSystem.system.SDMSystem;
import SDMSystemDTO.product.DTOProduct;
import SDMSystemDTO.product.DTOProductInStore;
import common.JavaFxHelper;
import components.main.SDMMainControllers;
import components.makeOrder.makeStaticOrder.ProductInTable;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleFloatProperty;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;

import java.util.HashMap;
import java.util.Map;

public class ChooseProductsForStoreController {

    public static final String ADD_PRODUCTS_TO_STORE_DETAILS_FXML_PATH = "/components/main/addNewStore/chooseProductsForStore.fxml";
    private SimpleFloatProperty price;
    private SDMSystem sdmSystem;
    private final Map<Integer, DTOProductInStore> productsInStore;

    @FXML private TableView<ProductInTable> productsToAddTable;
    @FXML private TableColumn<ProductInTable, String> productNameCol;
    @FXML private TableColumn<ProductInTable, Integer> productIdCol;
    @FXML private TableColumn<ProductInTable, String> wayOfBuyingCol;
    @FXML private TableView<ProductInTable> productsInStoreTable;
    @FXML private TableColumn<ProductInTable, String> productNameCartTableCol;
    @FXML private TableColumn<ProductInTable, Integer> idCartTableCol;
    @FXML private TableColumn<ProductInTable, String> wayOfBuyingCartTableCol;
    @FXML private TableColumn<ProductInTable, Float> priceTableCol;
    @FXML private Button continueButton;
    @FXML private ComboBox<DTOProduct> chooseProductComboBox;
    @FXML private TextField priceTextField;
    @FXML private Label errorInputLabel;
    @FXML private Label zeroErrorLabel;
    @FXML private Button addToStoreButton;
    @FXML private Label notIntegerErrorLabel;
    private int storeID;
    private String storeName;
    private int x;
    private int y;
    private float ppk;
    private BorderPane mainBorderPane;
    private ScrollPane startingFormScrollPane;
    private SDMMainControllers sdmMainControllers;


    public ChooseProductsForStoreController() {
        price = new SimpleFloatProperty();
        productsInStore = new HashMap<>();
    }

    @FXML
    public void initialize(){
        initErrorsLabel();
        continueButton.disableProperty().bind(Bindings.isEmpty(productsInStoreTable.getItems()));

        priceTextField.disableProperty().bind(chooseProductComboBox.valueProperty().isNull());
        JavaFxHelper.makeTextFieldInputOnlyFloat(priceTextField,errorInputLabel,price);
        addToStoreButton.disableProperty().bind(
                chooseProductComboBox.valueProperty().isNull().or(
                        priceTextField.textProperty().isEmpty()
                ));

        initProductsToAddTableSettings();
        initProductsInStoreTableSettings();
    }

    public void initDetails(SDMSystem sdmSystem,
                            int storeID,
                            String storeName,
                            int x,
                            int y,
                            float ppk,
                            BorderPane mainBorderPane,
                            ScrollPane startingFormScrollPane,
                            SDMMainControllers sdmMainControllers) {
        this.sdmSystem = sdmSystem;
        this.storeID = storeID;
        this.storeName = storeName;
        this.x = x;
        this.y = y;
        this.ppk = ppk;
        this.mainBorderPane = mainBorderPane;
        this.startingFormScrollPane = startingFormScrollPane;
        this.sdmMainControllers = sdmMainControllers;
        initProductsToAddTable();
    }

    private void initProductsToAddTable() {
        for(DTOProduct product : sdmSystem.getProductsInSystem().values()){
            productsToAddTable.getItems().add(new ProductInTable(
                    product.getProductName(),
                    product.getProductSerialNumber(),
                    product.getWayOfBuying(),
                    null,
                    null
            ));
            chooseProductComboBox.getItems().add(product);
        }
    }

    private void initProductsInStoreTableSettings() {
        productNameCartTableCol.setCellValueFactory(new PropertyValueFactory<>("productName"));
        idCartTableCol.setCellValueFactory(new PropertyValueFactory<>("productId"));
        wayOfBuyingCartTableCol.setCellValueFactory(new PropertyValueFactory<>("wayOfBuying"));
        priceTableCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        //make the cell to present two digits after the dot
        priceTableCol.setCellFactory(tc -> new TableCell<ProductInTable, Float>() {
            @Override
            protected void updateItem(Float amountCartTableCol, boolean empty) {
                super.updateItem(amountCartTableCol, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText(String.format("%.2f", amountCartTableCol));
                }}});
    }

    private void initProductsToAddTableSettings() {
        productNameCol.setCellValueFactory(new PropertyValueFactory<>("productName"));
        productIdCol.setCellValueFactory(new PropertyValueFactory<>("productId"));
        wayOfBuyingCol.setCellValueFactory(new PropertyValueFactory<>("wayOfBuying"));
    }


    private void initErrorsLabel() {
        initErrorLabel(errorInputLabel);
        initErrorLabel(zeroErrorLabel);
        //initErrorLabel(notIntegerErrorLabel);
    }

    private void initErrorLabel(Label errorLabel) {
        errorLabel.setAlignment(Pos.CENTER);
        errorLabel.visibleProperty().set(false);
        errorLabel.setManaged(false);
    }

    @FXML
    void onAddToStore(ActionEvent event) {
        Float priceEntered = Float.parseFloat(priceTextField.getText());
        DTOProduct chosenProduct = chooseProductComboBox.getValue();
        if(priceEntered <= 0){
            zeroErrorLabel.visibleProperty().set(true);
            zeroErrorLabel.setManaged(true);
        }
//        else if(chosenProduct.getWayOfBuying() == WayOfBuying.BY_QUANTITY && !JavaFxHelper.isInteger(priceEntered)){
//            notIntegerErrorLabel.visibleProperty().set(true);
//            notIntegerErrorLabel.setManaged(true);
//        }
        else {
            //add product to table
            addProductToTableAndRemoveFromAddTable(priceEntered, chosenProduct);
            //add product to products map
            productsInStore.put(
                    chosenProduct.getProductSerialNumber(),
                    new DTOProductInStore(
                            chosenProduct.getProductSerialNumber(),
                            chosenProduct.getProductName(),
                            chosenProduct.getWayOfBuying(),
                            0,
                            priceEntered,
                            0,
                            storeID
                            ));
            //productsInOrder.add(new Pair(chosenProduct,priceEntered));
            clearLabelsAfterAddingProduct();
        }
    }

    private void addProductToTableAndRemoveFromAddTable(Float priceEntered, DTOProduct chosenProduct) {
        productsInStoreTable.getItems().add(new ProductInTable(
                chosenProduct.getProductName(),
                chosenProduct.getProductSerialNumber(),
                chosenProduct.getWayOfBuying(),
                priceEntered,
                null
        ));
        FilteredList<ProductInTable> productToDeleteFromTableInList =  productsToAddTable.getItems().filtered(item -> item.getProductId() == chooseProductComboBox.getValue().getProductSerialNumber());
        ProductInTable productToDeleteFromTable = productToDeleteFromTableInList.get(0);
        productsToAddTable.getItems().remove(productToDeleteFromTable);
        FilteredList<DTOProduct> productToDeleteFromComboBoxInList =  chooseProductComboBox.getItems().filtered(item -> item.getProductSerialNumber() == chooseProductComboBox.getValue().getProductSerialNumber());
        chooseProductComboBox.getItems().remove(productToDeleteFromComboBoxInList.get(0));
    }

    private void clearLabelsAfterAddingProduct() {
        priceTextField.setText("");
        chooseProductComboBox.getSelectionModel().clearSelection();
        zeroErrorLabel.visibleProperty().set(false);
        zeroErrorLabel.setManaged(false);
//        notIntegerErrorLabel.visibleProperty().set(false);
//        notIntegerErrorLabel.setManaged(false);
    }

    @FXML
    void onClickCancel(ActionEvent event) {
        JavaFxHelper.cancelAlert(mainBorderPane,startingFormScrollPane,"You are in a middle of adding new store and you are about to cancel");
    }

    @FXML
    void onClickContinue(ActionEvent event) {
        sdmSystem.addStoreToSystem(storeID,storeName,x,y,ppk,productsInStore);
        sdmMainControllers.initStoresInAccordion();
        sdmMainControllers.maxXCoordinateProperty().set(Math.max(sdmMainControllers.getMaxXCoordinate(), x));
        sdmMainControllers.maxYCoordinateProperty().set(Math.max(sdmMainControllers.getMaxYCoordinate(), y));
        addStoreSuccessfullyMsg();
        mainBorderPane.setCenter(startingFormScrollPane);
        sdmMainControllers.initMapThread();
    }



    private void addStoreSuccessfullyMsg() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Great News");
        alert.setHeaderText(null);
        alert.setContentText("The store was added successfully!");
        alert.showAndWait();
    }

}
