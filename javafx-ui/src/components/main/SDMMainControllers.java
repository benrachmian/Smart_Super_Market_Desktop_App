package components.main;

import SDMSystem.system.SDMSystem;
import SDMSystemDTO.customer.DTOCustomer;
import SDMSystemDTO.order.DTOOrder;
import SDMSystemDTO.product.DTOProduct;
import SDMSystemDTO.store.DTOStore;

import common.SDMResourcesConstants;
import components.details.productsDetails.ProductDetailsController;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;

public class SDMMainControllers {

    @FXML private ListView<DTOCustomer> customerListView;
    @FXML private ListView<DTOStore> storeListView;
    @FXML private ListView<DTOProduct> productsListView;
    @FXML private ListView<DTOOrder> ordersListView;
    @FXML private MenuItem loadSystemXmlMenuItem;
    @FXML private Accordion systemAccordion;
    @FXML private AnchorPane customerAccordionAnchorPane;
    @FXML private BorderPane mainBorderPane;


    private SDMSystem sdmSystem;
    private Stage primaryStage;
    private SimpleStringProperty selectedFileProperty;
    private SimpleBooleanProperty isFileSelected;
    private GridPane productsDetailsGridPane;
    private ProductDetailsController productDetailsController;


    public SDMMainControllers() {
        selectedFileProperty = new SimpleStringProperty();
        isFileSelected = new SimpleBooleanProperty(false);
    }

    @FXML
    public void initialize(){
        //systemAccordion.disableProperty().bind(isFileSelected.not());
        customerListView.setPlaceholder(new Label("No content yet"));
        storeListView.setPlaceholder(new Label("No content yet"));
        productsListView.setPlaceholder(new Label("No content yet"));
        ordersListView.setPlaceholder(new Label("No content yet"));



    }

    public void setProductsDetailsGridPane(GridPane productsDetailsGridPane) {
        this.productsDetailsGridPane = productsDetailsGridPane;
        productsDetailsGridPane.setAlignment(Pos.CENTER);

    }

    public void setProductDetailsController(ProductDetailsController productDetailsController) {
        this.productDetailsController = productDetailsController;
        productDetailsController.setSdmSystem(sdmSystem);
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

    @FXML
    void productItemClicked(MouseEvent event) {
        if(productsListView.getSelectionModel().getSelectedIndex() != -1) {
            mainBorderPane.setCenter(productsDetailsGridPane);
            productDetailsController.updateGrid(productsListView.getSelectionModel().getSelectedItem());
        }
    }


    public void setSdmSystem(SDMSystem sdmSystem) {
        this.sdmSystem = sdmSystem;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }
}
