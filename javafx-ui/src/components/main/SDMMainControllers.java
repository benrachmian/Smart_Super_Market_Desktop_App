package components.main;

import SDMSystem.system.SDMSystem;
import SDMSystemDTO.customer.DTOCustomer;
import SDMSystemDTO.order.DTOOrder;
import SDMSystemDTO.product.DTOProduct;
import SDMSystemDTO.store.DTOStore;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.FileNotFoundException;

public class SDMMainControllers {

    @FXML private ListView<DTOCustomer> customerListView;
    @FXML private ListView<DTOStore> storeListView;
    @FXML private ListView<DTOProduct> productsListView;
    @FXML private ListView<DTOOrder> ordersListView;
    @FXML private MenuItem loadSystemXmlMenuItem;
    @FXML private Accordion systemAccordion;



    private SDMSystem sdmSystem;
    private Stage primaryStage;
    private SimpleStringProperty selectedFileProperty;
    private SimpleBooleanProperty isFileSelected;


    public SDMMainControllers() {
        selectedFileProperty = new SimpleStringProperty();
        isFileSelected = new SimpleBooleanProperty(false);
    }

    @FXML
    public void initialize(){
        //customerListView.getItems().add("bennnn");
        //customerListView.getItems().add("mikeee");
       /* DTOStore store = new DTOStore(null,null,4,4,"ben",4);
        storeListView.getItems().add(store);
        storeListView.getItems().add(store);*/

//        ListView<String> storeOption = new ListView<>();
//        storeOption.getItems().add("Update store");
//        TitledPane newStore = new TitledPane("Store1",storeOption);
//        storesAccordion.getPanes().add(newStore);
//        newStore = new TitledPane("Store2",storeOption);
//        storesAccordion.getPanes().add(newStore);
//        newStore = new TitledPane("Store3",storeOption);
//        storesAccordion.getPanes().add(newStore);

    }

    @FXML
    void loadSystemXmlMenuItemAction(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select XML file");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("xml files","*.xml"));
        File selectedFile = fileChooser.showOpenDialog(primaryStage);
        if(selectedFile == null){
            return;
        }

        String absolutePath = selectedFile.getAbsolutePath();
        selectedFileProperty.set(absolutePath);
        isFileSelected.set(true);
        try {
            sdmSystem.loadSystem(absolutePath);
            initMainAccordion();
        } catch (FileNotFoundException | JAXBException | RuntimeException e) {
            showLoadingFileError(e.getMessage());
        }
    }

    private void initMainAccordion() {
        initCustomersInAccordion();
        initStoresInAccordion();
        initProductsInAccordion();
    }

    private void initProductsInAccordion() {
        for(DTOProduct product : sdmSystem.getProductsInSystem().values()){
            productsListView.getItems().add(product);
        }
    }

    private void initStoresInAccordion() {
        for(DTOStore store : sdmSystem.getStoresInSystemBySerialNumber().values()){
            storeListView.getItems().add(store);
        }
    }

    private void initCustomersInAccordion() {
        for(DTOCustomer customer : sdmSystem.getCustomers().values()){
            customerListView.getItems().add(customer);
        }
    }

    private void showLoadingFileError(String errorMsg){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Error");
        alert.setHeaderText(errorMsg);
        alert.setContentText("Please try different file");
        alert.showAndWait();
    }



    @FXML
    void customerItemClicked(MouseEvent event) {

    }

    public void setSdmSystem(SDMSystem sdmSystem) {
        this.sdmSystem = sdmSystem;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }
}
