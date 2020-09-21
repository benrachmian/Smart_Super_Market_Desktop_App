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
import components.main.loadingSystemBar.LoadingSystemBarController;
import components.main.map.SingleSquareController;
import components.main.startingForm.StartingFormController;
import components.makeOrder.MakeOrderMainController;
import components.makeOrder.orderSummary.OrderSummaryMainController;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import tasks.loadXmlTask.LoadXmlTask;

import java.awt.*;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

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
    @FXML private TitledPane ordersTitledPane;
    @FXML private ToggleButton animationToggle;


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
    private ScrollPane orderSummaryScrollPane;
    private OrderSummaryMainController orderSummaryMainController;
    private ScrollPane loadingSystemBarScrollPane;
    private LoadingSystemBarController loadingSystemBarController;
    private ScrollPane startingFormScrollPane;
    private StartingFormController startingFormController;
    private SimpleBooleanProperty orderInProgress;
    private SimpleIntegerProperty maxXCoordinate;
    private SimpleIntegerProperty maxYCoordinate;
    private GridPane singleSquareGridPane;
    private SingleSquareController singleSquareController;
    private GridPane map;
    private ScrollPane mapScrollPane;
    private SimpleBooleanProperty mapLoaded;
    private Map<GridPane,SingleSquareController> cellsControllersInMap;
    private SimpleBooleanProperty animationStatus;



    public SDMMainControllers() {
        selectedFileProperty = new SimpleStringProperty();
        isFileSelected = new SimpleBooleanProperty(false);
        fileLoaded = new SimpleBooleanProperty(false);
        orderInProgress = new SimpleBooleanProperty(false);
        maxXCoordinate = new SimpleIntegerProperty();
        maxYCoordinate = new SimpleIntegerProperty();
        mapLoaded = new SimpleBooleanProperty(false);
        animationStatus = new SimpleBooleanProperty(false);
    }


    @FXML
    public void initialize(){
        loadStartingForm();
        customerListView.setPlaceholder(new Label("No content yet"));
        storeListView.setPlaceholder(new Label("No content yet"));
        productsListView.setPlaceholder(new Label("No content yet"));
        ordersListView.setPlaceholder(new Label("No content yet"));
        makeOrderButton.disableProperty().bind(fileLoaded.not().or(orderInProgress));
        showMapButton.disableProperty().bind(fileLoaded.not().or(orderInProgress));
        animationStatus.addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                animationToggle.textProperty().set(newValue ? "Animation: ON" : "Animation: OFF");
            }
        });


        ordersTitledPane.expandedProperty().addListener((observable, oldValue, newValue) -> {
            initOrdersInAccordion();
        });

    }

    public void createNewMapInstance() {
        map = new GridPane();
        mapScrollPane = new ScrollPane();
        cellsControllersInMap = new HashMap<>();
        mapScrollPane.fitToWidthProperty().set(true);
        mapScrollPane.fitToHeightProperty().set(true);
        mapScrollPane.setContent(map);
        map.setAlignment(Pos.CENTER);
    }

    private void loadStartingForm() {
        FxmlLoader<ScrollPane,StartingFormController> loaderStartingForm = new FxmlLoader<>(StartingFormController.STARTING_FORM_FXML_PATH);
        startingFormScrollPane = loaderStartingForm.getFormBasePane();
        startingFormController = loaderStartingForm.getFormController();
        startingFormController.init(fileLoaded);
//        ScrollPane scrollPane = new ScrollPane();
//        scrollPane.setContent(startingFormGridPane);
//        scrollPane.fitToHeightProperty().set(true);
//        scrollPane.fitToWidthProperty().set(true);
        //startingFormScrollPane.setAlignment(Pos.CENTER);
        mainBorderPane.setCenter(startingFormScrollPane);

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
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("xml files", "*.xml"));
        File selectedFile = fileChooser.showOpenDialog(primaryStage);
        if (selectedFile == null) {
            return;
        }
        String absolutePath = selectedFile.getAbsolutePath();
        selectedFileProperty.set(absolutePath);
        isFileSelected.set(true);

        loadLoadingSystemBar();

        LoadXmlTask loadXmlTask = new LoadXmlTask(sdmSystem, absolutePath, this,fileLoaded);
        loadingSystemBarController.init(loadXmlTask,absolutePath);
        mainBorderPane.setCenter(loadingSystemBarScrollPane);
        new Thread(loadXmlTask).start();
    }

    public void initMapThread() {
        new Thread(() -> {
            Platform.runLater(
                    () -> {
                        initMap();
                        mapLoaded.set(true);
                    }
            );
        }).start();
    }

    public int getMaxXCoordinate() {
        return maxXCoordinate.get();
    }

    public SimpleIntegerProperty maxXCoordinateProperty() {
        return maxXCoordinate;
    }

    public int getMaxYCoordinate() {
        return maxYCoordinate.get();
    }

    public SimpleIntegerProperty maxYCoordinateProperty() {
        return maxYCoordinate;
    }

    private void loadLoadingSystemBar() {
        FxmlLoader<ScrollPane,LoadingSystemBarController> loaderLoadingSystemBar = new FxmlLoader<>(LoadingSystemBarController.LOADING_SYSTEM_FXML_PATH);
        loadingSystemBarScrollPane = loaderLoadingSystemBar.getFormBasePane();
        //JavaFxHelper.initMainScrollPane(loadingSystemBarScrollPane);
        loadingSystemBarController = loaderLoadingSystemBar.getFormController();
    }

    private void loadSingleSquare() {
        FxmlLoader<GridPane,SingleSquareController> loaderSingleSquare = new FxmlLoader<>(SingleSquareController.SINGLE_SQUARE_FXML_PATH);
        singleSquareGridPane = loaderSingleSquare.getFormBasePane();
        singleSquareController = loaderSingleSquare.getFormController();
    }

    public void initOrdersInAccordion() {
        ordersListView.getItems().clear();
        ordersListView.setPlaceholder(new Label("No content yet"));
        for(DTOOrder order : sdmSystem.getAllOrders()){
            ordersListView.getItems().add(order);
        }
    }

    public void initProductsInAccordion() {
        productsListView.getItems().clear();
        productsListView.setPlaceholder(new Label("No content yet"));
        for(DTOProduct product : sdmSystem.getProductsInSystem().values()){
            productsListView.getItems().add(product);
        }
    }

    public void initStoresInAccordion() {
        storeListView.getItems().clear();
        storeListView.setPlaceholder(new Label("No content yet"));
        for(DTOStore store : sdmSystem.getStoresInSystemBySerialNumber().values()){
            storeListView.getItems().add(store);
        }
    }

    public void initCustomersInAccordion() {
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
        if(orderInProgress.getValue()){
            if(JavaFxHelper.cancelOrderAlert(mainBorderPane, startingFormScrollPane,orderInProgress)){
                showCustomerDetails();
            }
        }
        else {
            if (customerListView.getSelectionModel().getSelectedIndex() != -1) {
                showCustomerDetails();
            }
        }
    }

    private void showCustomerDetails() {
        mainBorderPane.setCenter(customersDetailsScrollPane);
        customerDetailsController.updateGrid(sdmSystem.getCustomer(customerListView.getSelectionModel().getSelectedItem().getCustomerSerialNumber()));
    }

    @FXML
    void productItemClicked(MouseEvent event) {
        if(orderInProgress.getValue()){
            if(JavaFxHelper.cancelOrderAlert(mainBorderPane, startingFormScrollPane,orderInProgress)){
                showProductDetails();
            }
        }
        else{
            if (productsListView.getSelectionModel().getSelectedIndex() != -1) {
                showProductDetails();
            }
        }
    }

    private void showProductDetails() {
        mainBorderPane.setCenter(productsDetailsScrollPane);
        productDetailsController.updateGrid(sdmSystem.getProductFromSystem(productsListView.getSelectionModel().getSelectedItem().getProductSerialNumber()));
    }

    @FXML
    void orderItemClicked(MouseEvent event) {
        if(orderInProgress.getValue()){
            if(JavaFxHelper.cancelOrderAlert(mainBorderPane, startingFormScrollPane,orderInProgress)){
                showOrderDetails();
            }
        }
        else {
            if (ordersListView.getSelectionModel().getSelectedIndex() != -1) {
                showOrderDetails();
            }
        }
    }

    private void showOrderDetails() {
        DTOOrder order = ordersListView.getSelectionModel().getSelectedItem();
        loadOrderSummaryForm();
        mainBorderPane.setCenter(orderSummaryScrollPane);
        orderSummaryMainController.initDetails(
                order.getProductsInOrderByStores(),
                sdmSystem.getCustomer(order.getCustomerOrderedId()),
                new SimpleFloatProperty(order.getProductsCost()),
                new SimpleFloatProperty(order.getDeliveryCost()),
                order.isStaticOrder(),
                null,
                order.getOrderDate(),
                mainBorderPane,
                sdmSystem,
                startingFormScrollPane, orderInProgress);
        orderSummaryMainController.makeButtonsInvisible();
    }

    private void loadOrderSummaryForm() {
        FxmlLoader<ScrollPane,OrderSummaryMainController> loaderOrderSummaryForm = new FxmlLoader<>(OrderSummaryMainController.ORDER_SUMMARY_FORM_FXML_PATH);
        orderSummaryScrollPane = loaderOrderSummaryForm.getFormBasePane();
        orderSummaryMainController = loaderOrderSummaryForm.getFormController();
    }

    @FXML
    void storeItemClicked(MouseEvent event) {
        if(orderInProgress.getValue()){
            if(JavaFxHelper.cancelOrderAlert(mainBorderPane, startingFormScrollPane,orderInProgress)){
                showStoreDetails();
            }
        }
        else {
            if (storeListView.getSelectionModel().getSelectedIndex() != -1) {
                showStoreDetails();

            }
        }
    }

    private void showStoreDetails() {
        mainBorderPane.setCenter(storesDetailsScrollPane);
        //in order to get the most updated store I always get the store from the system even though I got it in the listview.
        storeDetailsController.setStore(sdmSystem.getStoreFromStores(storeListView.getSelectionModel().getSelectedItem().getStoreSerialNumber()));
        storeDetailsController.updateStoreDetailsTab();
    }

    @FXML
    void clickOnMakeOrder(ActionEvent event) {
        orderInProgress.set(true);
        loadMakeOrderMainForm();
        mainBorderPane.setCenter(makeOrderMainScrollPain);
        makeOrderMainController.initDetails(
                sdmSystem,
                mainBorderPane,
                ordersListView,
                startingFormScrollPane,
                orderInProgress,
                animationStatus);

    }

    private void loadMakeOrderMainForm() {
        FxmlLoader<ScrollPane, MakeOrderMainController> loaderMakeOrderMainForm = new FxmlLoader<>(MAKE_ORDER_MAIN_FXML_PATH);
        makeOrderMainScrollPain = loaderMakeOrderMainForm.getFormBasePane();
        makeOrderMainController = loaderMakeOrderMainForm.getFormController();
    }


    @FXML
    void onShowMap(ActionEvent event) {
        if(mapLoaded.getValue()) {
            mainBorderPane.setCenter(mapScrollPane);
        }
        else{
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Bad News");
            alert.setHeaderText(null);
            alert.setContentText("The map is not fully loaded yet, please try again in a few seconds");
            alert.showAndWait();
        }
    }

    public void initMap() {
        Image customerImage =new Image("/components/main/map/customer.png");
        Image storeImage =new Image("/components/main/map/store.png");

        for(int i=0; i<maxYCoordinate.get() + 1; i++){
            for(int j=0; j<maxXCoordinate.get() + 1; j++) {
                loadSingleSquare();
                singleSquareController.initDetails(j,i,sdmSystem,primaryStage);
                if (i == 0 && j != 0) {
                    singleSquareGridPane.add(createNumLabel(j), 0, 0);
                }
                else if (j == 0 && i != 0) {
                    singleSquareGridPane.add(createNumLabel(i),0,0);
                }
                map.add(singleSquareGridPane, j, i);
                cellsControllersInMap.put(singleSquareGridPane,singleSquareController);
            }
        }

        addCustomersAndStoresToMap(customerImage, storeImage);
    }

    private void addCustomersAndStoresToMap(Image customerImage, Image storeImage) {
        for(Point currPoint : sdmSystem.getCustomersAndStoresLocationMap().keySet()) {
            singleSquareGridPane = (GridPane) map.getChildren().get(((maxXCoordinate.get() +1 ) * currPoint.y) + currPoint.x);
            if(sdmSystem.ifStoreInLocation(currPoint)){
                ImageView storeImageView = createImageViewForMap(storeImage);
                singleSquareGridPane.getChildren().add(storeImageView);
                SingleSquareController currController =  cellsControllersInMap.get(singleSquareGridPane);
                currController.setSquareType(SingleSquareController.SquareType.STORE);
                currController.getTooltip().setText(sdmSystem.getStoreInSystemByLocation(currPoint).getStoreName());
            }
            else{
                ImageView customerImageView = createImageViewForMap(customerImage);
                singleSquareGridPane.getChildren().add(customerImageView);
                SingleSquareController currController =  cellsControllersInMap.get(singleSquareGridPane);
                currController.setSquareType(SingleSquareController.SquareType.STORE);
                currController.setSquareType(SingleSquareController.SquareType.CUSTOMER);
                currController.getTooltip().setText(sdmSystem.getCustomer(currPoint).getCustomerName());
            }
        }
    }

    private ImageView createImageViewForMap(Image image){
        ImageView imageView = new ImageView(image);
        imageView.fitHeightProperty().setValue(40);
        imageView.fitWidthProperty().setValue(40);
        return imageView;
    }


    public Label createNumLabel(int value){
        Label numLabel = new Label(String.valueOf(value));
        numLabel.alignmentProperty().setValue(Pos.CENTER);
        numLabel.minWidth(35);
        numLabel.prefWidth(Region.USE_COMPUTED_SIZE);
        numLabel.setAlignment(Pos.CENTER);

        return numLabel;
    }

    public void setSdmSystem(SDMSystem sdmSystem) {
        this.sdmSystem = sdmSystem;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    @FXML
    void onAnimationToggle(ActionEvent event) {
        animationStatus.set(!animationStatus.getValue());
    }


}
