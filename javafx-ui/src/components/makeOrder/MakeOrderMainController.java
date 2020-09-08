package components.makeOrder;

import SDMSystem.location.LocationUtility;
import SDMSystem.system.SDMSystem;
import SDMSystemDTO.customer.DTOCustomer;
import SDMSystemDTO.store.DTOStore;
import javafx.beans.property.SimpleFloatProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.cell.PropertyValueFactory;

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
    }

    @FXML
    public void initialize(){
        makeOrderMainScrollPain.setMinWidth(250);
        makeOrderMainScrollPain.setMinHeight(250);
        makeOrderMainScrollPain.setMaxHeight(1000);
        makeOrderMainScrollPain.setMaxWidth(4000);
        makeOrderMainScrollPain.fitToWidthProperty().set(true);
        makeOrderMainScrollPain.fitToHeightProperty().set(true);
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
        orderTypeComboBox.getItems().add(OrderType.STATIC_ORDER);
        orderTypeComboBox.getItems().add(OrderType.DYNAMIC_ORDER);
        storesScrollPane.visibleProperty().bind(orderTypeComboBox.valueProperty().isEqualTo(OrderType.STATIC_ORDER));
        initTableSettings();
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
    }
}