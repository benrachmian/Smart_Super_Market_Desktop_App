package components.main.addNewProduct;

import SDMSystem.system.SDMSystem;
import SDMSystemDTO.product.DTOProduct;
import SDMSystemDTO.product.DTOProductInStore;
import SDMSystemDTO.product.WayOfBuying;
import SDMSystemDTO.store.DTOStore;
import common.JavaFxHelper;
import components.main.SDMMainControllers;
import components.makeOrder.StoreInTable;
import components.makeOrder.makeStaticOrder.ProductInTable;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


public class ChooseStoresForProductController {

    public static final String ADD_STORES_FOR_PRODUCT_FXML_PATH = "/components/main/addNewProduct/chooseStoresForProduct.fxml";
    private SimpleFloatProperty productPrice;
    private SDMSystem sdmSystem;
    //key: storeID, value: product price
    private Map<Integer,Float> storesSellingTheProductAndPrice;

    @FXML private Button continueButton;
    @FXML private ComboBox<DTOStore> chooseStoreComboBox;
    @FXML private TextField priceTextField;
    @FXML private Label errorInputLabel;
    @FXML private Label zeroErrorLabel;
    @FXML private Button addStoreButton;
    @FXML private TableView<StoreInTable> storesToAddTable;
    @FXML private TableColumn<StoreInTable,String> storeNameAddStoreTableCol;
    @FXML private TableColumn<StoreInTable,String> storeIDAddStoreTableCol;
    @FXML private TableColumn<StoreInTable,String> locationAddStoreTableCol;
    @FXML private TableColumn<StoreInTable,String> ppkAddStoreTableCol;
    @FXML private TableView<StoreInTable> storesSellingTable;
    @FXML private TableColumn<StoreInTable,String> storeNameSellingStoresTableCol;
    @FXML private TableColumn<StoreInTable,String> storeIDSellingStoresTableCol;
    @FXML private TableColumn<StoreInTable,String> productPriceSellingStoresTableCol;
    private BorderPane mainBorderPane;
    private ScrollPane startingFormScrollPane;
    private SimpleIntegerProperty productId;
    private String productName;
    private WayOfBuying productWayOfBuying;
    private SDMMainControllers sdmMainController;


    public ChooseStoresForProductController() {
        productPrice = new SimpleFloatProperty();
        storesSellingTheProductAndPrice = new HashMap<>();
    }

    @FXML
    public void initialize(){
        initErrorsLabel();
        continueButton.disableProperty().bind(Bindings.isEmpty(storesSellingTable.getItems()));
        priceTextField.disableProperty().bind(chooseStoreComboBox.valueProperty().isNull());
        JavaFxHelper.makeTextFieldInputOnlyFloat(priceTextField,errorInputLabel,productPrice);
        addStoreButton.disableProperty().bind(
                chooseStoreComboBox.valueProperty().isNull().or(
                        priceTextField.textProperty().isEmpty()
                ));

        initStoresToAddTableSettings();
        initStoresSellingTableSettings();
    }

    public void initDetails(SDMSystem sdmSystem,
                            BorderPane mainBorderPane,
                            ScrollPane startingFormScrollPane,
                            SimpleIntegerProperty productId,
                            String productName,
                            WayOfBuying productWayOfBuying,
                            SDMMainControllers sdmMainController){
        this.sdmSystem = sdmSystem;
        this.mainBorderPane = mainBorderPane;
        this.startingFormScrollPane = startingFormScrollPane;
        this.productId = productId;
        this.productName = productName;
        this.productWayOfBuying = productWayOfBuying;
        this.sdmMainController = sdmMainController;
        initStoresToAddTable();
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

    private void initStoresToAddTable() {
        for(DTOStore store : sdmSystem.getStoresInSystemBySerialNumber().values()){
            storesToAddTable.getItems().add(new StoreInTable(
                    store.getStoreName(),
                    store.getStoreSerialNumber(),
                    store.getStoreLocation(),
                    store.getPpk()
            ));
            chooseStoreComboBox.getItems().add(store);
        }
    }

    private void initStoresSellingTableSettings() {
        storeNameSellingStoresTableCol.setCellValueFactory(new PropertyValueFactory<>("storeName"));
        storeIDSellingStoresTableCol.setCellValueFactory(new PropertyValueFactory<>("storeId"));
        productPriceSellingStoresTableCol.setCellValueFactory(new PropertyValueFactory<>("newProductPrice"));
    }

    private void initStoresToAddTableSettings() {
        storeNameAddStoreTableCol.setCellValueFactory(new PropertyValueFactory<>("storeName"));
        storeIDAddStoreTableCol.setCellValueFactory(new PropertyValueFactory<>("storeId"));
        locationAddStoreTableCol.setCellValueFactory(new PropertyValueFactory<>("storeLocation"));
        ppkAddStoreTableCol.setCellValueFactory(new PropertyValueFactory<>("storePPK"));
    }


    @FXML
    void onAddStore(ActionEvent event) {
        Float priceEntered = Float.parseFloat(priceTextField.getText());
        DTOStore chosenStore = chooseStoreComboBox.getValue();
        if(priceEntered <= 0){
            zeroErrorLabel.visibleProperty().set(true);
            zeroErrorLabel.setManaged(true);
        }
        else {
            //add store to table
            addStoreToTableAndRemoveFromAddTable(priceEntered, chosenStore);
            //add store to stores selling map
            storesSellingTheProductAndPrice.put(
                    chosenStore.getStoreSerialNumber(),
                    priceEntered
            );
            clearLabelsAfterAddingStore();
        }
    }

    private void clearLabelsAfterAddingStore() {
        priceTextField.setText("");
        chooseStoreComboBox.getSelectionModel().clearSelection();
        zeroErrorLabel.visibleProperty().set(false);
        zeroErrorLabel.setManaged(false);
    }

    private void addStoreToTableAndRemoveFromAddTable(Float priceEntered, DTOStore chosenStore) {
        storesSellingTable.getItems().add(new StoreInTable(
                chosenStore.getStoreName(),
                chosenStore.getStoreSerialNumber(),
                chosenStore.getStoreLocation(),
                chosenStore.getPpk(),
                priceEntered
        ));
        FilteredList<StoreInTable> storeToDeleteFromTableInList =  storesToAddTable.getItems().filtered(item -> item.getStoreId() == chosenStore.getStoreSerialNumber());
        StoreInTable storeToDeleteFromTable = storeToDeleteFromTableInList.get(0);
        storesToAddTable.getItems().remove(storeToDeleteFromTable);
        FilteredList<DTOStore> storeToDeleteFromComboBoxInList =  chooseStoreComboBox.getItems().filtered(item -> item.getStoreSerialNumber() == chosenStore.getStoreSerialNumber());
        chooseStoreComboBox.getItems().remove(storeToDeleteFromComboBoxInList.get(0));
    }

    @FXML
    void onClickCancel(ActionEvent event) {
        JavaFxHelper.cancelAlert(mainBorderPane,startingFormScrollPane,"You are in a middle of adding new product and you are about to cancel");
    }

    @FXML
    void onClickContinue(ActionEvent event) {
        sdmSystem.addNewProductToSystem(productId.get(),productName,productWayOfBuying,storesSellingTheProductAndPrice);
        sdmMainController.initProductsInAccordion();
       addProductSuccessfullyMsg();
       mainBorderPane.setCenter(startingFormScrollPane);
    }

    private void addProductSuccessfullyMsg() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Great News");
        alert.setHeaderText(null);
        alert.setContentText("The product was added successfully!");
        alert.showAndWait();
    }


}
