package components.main;

import SDMSystem.system.SDMSystem;
import SDMSystemDTO.customer.DTOCustomer;
import SDMSystemDTO.order.DTOOrder;
import SDMSystemDTO.product.DTOProduct;
import SDMSystemDTO.store.DTOStore;

import common.FxmlLoader;
import common.JavaFxHelper;
import components.details.customersDetails.CustomerDetailsController;
import components.details.productsDetails.ProductDetailsController;
import components.details.storeDetails.StoreDetailsController;
import components.makeOrder.MakeOrderMainController;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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

public class SDMMainControllers {

    @FXML private ListView<DTOCustomer> customerListView;
    @FXML private ListView<DTOStore> storeListView;
    @FXML private ListView<DTOProduct> productsListView;
    @FXML private ListView<DTOOrder> ordersListView;
    @FXML private MenuItem loadSystemXmlMenuItem;
    @FXML private Accordion systemAccordion;
    @FXML private AnchorPane customerAccordionAnchorPane;
    @FXML private BorderPane mainBorderPane;
    @FXML private Button makeOrderButton;
    @FXML private Button showMapButton;


    private SDMSystem sdmSystem;
    private Stage primaryStage;
    private SimpleStringProperty selectedFileProperty;
    private SimpleBooleanProperty isFileSelected;
    private ScrollPane productsDetailsScrollPane;
    private ScrollPane customersDetailsScrollPane;
    private ScrollPane storesDetailsScrollPane;
    private ProductDetailsController productDetailsController;
    private CustomerDetailsController customerDetailsController;
    private StoreDetailsController storeDetailsController;
    private static final String MAKE_ORDER_MAIN_FXML_PATH = "/components/makeOrder/makeOrderMain.fxml";
    private ScrollPane makeOrderMainScrollPain;
    private MakeOrderMainController makeOrderMainController;
    private SimpleBooleanProperty fileLoaded;



    public SDMMainControllers() {
        selectedFileProperty = new SimpleStringProperty();
        isFileSelected = new SimpleBooleanProperty(false);
        fileLoaded = new SimpleBooleanProperty(false);
    }

    @FXML
    public void initialize(){
        //systemAccordion.disableProperty().bind(isFileSelected.not());
        customerListView.setPlaceholder(new Label("No content yet"));
        storeListView.setPlaceholder(new Label("No content yet"));
        productsListView.setPlaceholder(new Label("No content yet"));
        ordersListView.setPlaceholder(new Label("No content yet"));
        makeOrderButton.disableProperty().bind(fileLoaded.not());
        showMapButton.disableProperty().bind(fileLoaded.not());
    }

    public void setProductsDetailsScrollPane(ScrollPane productsDetailsScrollPane) {
        this.productsDetailsScrollPane = productsDetailsScrollPane;
        JavaFxHelper.initMainScrollPane(productsDetailsScrollPane);
    }

    public void setCustomersDetailsScrollPane(ScrollPane customersDetailsScrollPane) {
        this.customersDetailsScrollPane = customersDetailsScrollPane;
        JavaFxHelper.initMainScrollPane(customersDetailsScrollPane);
    }

    public void setStoreDetailsScrollPane(ScrollPane storeDetailsScrollPane){
        this.storesDetailsScrollPane = storeDetailsScrollPane;
        storeDetailsScrollPane.fitToHeightProperty().set(true);
        storeDetailsScrollPane.fitToWidthProperty().set(true);
    }

    public void setStoreDetailsController(StoreDetailsController storeDetailsController){
        this.storeDetailsController = storeDetailsController;
        storeDetailsController.setSdmSystem(sdmSystem);
        storeDetailsController.setMainBorderPane(mainBorderPane);
    }

    public void setProductDetailsController(ProductDetailsController productDetailsController) {
        this.productDetailsController = productDetailsController;
        productDetailsController.setSdmSystem(sdmSystem);
    }

    public void setCustomerDetailsController(CustomerDetailsController customerDetailsController) {
        this.customerDetailsController = customerDetailsController;
        customerDetailsController.setSdmSystem(sdmSystem);
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
            fileLoaded.set(true);
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
        productsListView.getItems().clear();
        productsListView.setPlaceholder(new Label("No content yet"));
        for(DTOProduct product : sdmSystem.getProductsInSystem().values()){
            productsListView.getItems().add(product);
        }
    }

    private void initStoresInAccordion() {
        storeListView.getItems().clear();
        storeListView.setPlaceholder(new Label("No content yet"));
        for(DTOStore store : sdmSystem.getStoresInSystemBySerialNumber().values()){
            storeListView.getItems().add(store);
        }
    }

    private void initCustomersInAccordion() {
        customerListView.getItems().clear();
        customerListView.setPlaceholder(new Label("No content yet"));
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
        if(customerListView.getSelectionModel().getSelectedIndex() != -1) {
            mainBorderPane.setCenter(customersDetailsScrollPane);
            customerDetailsController.updateGrid(sdmSystem.getCustomer(customerListView.getSelectionModel().getSelectedItem().getCustomerSerialNumber()));
        }
    }

    @FXML
    void productItemClicked(MouseEvent event) {
        if(productsListView.getSelectionModel().getSelectedIndex() != -1) {
            mainBorderPane.setCenter(productsDetailsScrollPane);
            productDetailsController.updateGrid(sdmSystem.getProductFromSystem(productsListView.getSelectionModel().getSelectedItem().getProductSerialNumber()));
        }
    }


    @FXML
    void storeItemClicked(MouseEvent event) {
        if(storeListView.getSelectionModel().getSelectedIndex() != -1) {
            mainBorderPane.setCenter(storesDetailsScrollPane);
            //in order to get the most updated store I always get the store from the system even though I got it in the listview.
            storeDetailsController.setStore(sdmSystem.getStoreFromStores(storeListView.getSelectionModel().getSelectedItem().getStoreSerialNumber()));
            storeDetailsController.updateStoreDetailsTab();
        }
    }

    @FXML
    void clickOnMakeOrder(ActionEvent event) {
        //loadMakeOrderMainForm();
        loadMakeOrderMainForm();
        mainBorderPane.setCenter(makeOrderMainScrollPain);
        makeOrderMainController.setSdmSystem(sdmSystem);
        makeOrderMainController.setMainBorderPane(mainBorderPane);
        makeOrderMainController.initDetails();

    }

    private void loadMakeOrderMainForm() {
        FxmlLoader<ScrollPane, MakeOrderMainController> loaderMakeOrderMainForm = new FxmlLoader<>(MAKE_ORDER_MAIN_FXML_PATH);
        makeOrderMainScrollPain = loaderMakeOrderMainForm.getFormBasePane();
        makeOrderMainController = loaderMakeOrderMainForm.getFormController();
    }

//    private void loadMakeOrderMainForm() {
//        FXMLLoader loader;
//        URL mainFXML;
//        loader = new FXMLLoader();
//        mainFXML = getClass().getResource(MAKE_ORDER_MAIN_FXML_PATH);
//        loader.setLocation(mainFXML);
//        try {
//            makeOrderMainScrollPain = loader.load();
//            makeOrderMainController = loader.getController();
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    public void setSdmSystem(SDMSystem sdmSystem) {
        this.sdmSystem = sdmSystem;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }


}
