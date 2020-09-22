package components.makeOrder.makeDynamicOrder;

import SDMSystem.system.SDMSystem;
import SDMSystemDTO.customer.DTOCustomer;
import SDMSystemDTO.product.DTOProduct;
import SDMSystemDTO.product.IDTOProductInStore;
import SDMSystemDTO.product.WayOfBuying;
import SDMSystemDTO.store.DTOStore;
import common.FxmlLoader;
import common.JavaFxHelper;
import components.main.loadingSystemBar.LoadingSystemBarController;
import components.makeOrder.MakeOrderMainController;
import components.makeOrder.discountsInOrder.DiscountsInOrderController;
import components.makeOrder.makeDynamicOrder.storesParticipatingInDyanmicOrder.StoresParticipatingInDynamicOrderController;
import components.makeOrder.makeStaticOrder.ProductInTable;
import javafx.animation.Animation;
import javafx.animation.PathTransition;
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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.util.Duration;
import javafx.util.Pair;
import tasks.loadXmlTask.FindCheapestBasketTask;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;

public class MakeDynamicOrderController {

    public static final String DYNAMIC_ORDER_FORM_FXML_PATH = "/components/makeOrder/makeDynamicOrder/makeDynamicOrder.fxml";
    private SDMSystem sdmSystem;
    private DTOCustomer customerMakingTheOrder;
    private DTOStore storeFromWhomTheOrderIsMade;
    private SimpleFloatProperty selectedProductPrice;
    private SimpleFloatProperty amountInTextField;
    private SimpleFloatProperty totalDeliveryCost;
    private SimpleFloatProperty totalProductsCost;
    private MakeOrderMainController makeOrderMainController;
    private DiscountsInOrderController discountsInOrderController;
    //pair: key = product bought, value: amount
    private Map<Integer, Collection<Pair<IDTOProductInStore, Float>>> cheapestBasket;
    private Collection<Pair<DTOProduct, Float>> productsInOrder;
    private ScrollPane storesParticipatingInDynamicOrderScrollPane;
    private StoresParticipatingInDynamicOrderController storesParticipatingInDynamicOrderController;
    private BorderPane mainBorderPane;
    private SimpleBooleanProperty animationStatus;



    @FXML private ScrollPane makeDynamicOrderMainScrollPain;
    @FXML private Button continueButton;
    @FXML private ComboBox<DTOProduct> chooseProductComboBox;
    @FXML private TextField amountTextField;
    @FXML private Label errorInputLabel;
    @FXML private Label zeroErrorLabel;
    @FXML private Label notIntegerErrorLabel;
    @FXML private Label productCostLabel;
    @FXML private Button addToCartButton;
    @FXML private TableView<ProductInTable> productsToBuyTable;
    @FXML private TableColumn<ProductInTable, String> productNameCol;
    @FXML private TableColumn<ProductInTable, Integer> productIdCol;
    @FXML private TableColumn<ProductInTable, String> wayOfBuyingCol;
    @FXML private TableView<ProductInTable> cartTable;
    @FXML private TableColumn<ProductInTable, String> productNameCartTableCol;
    @FXML private TableColumn<ProductInTable, Integer> idCartTableCol;
    @FXML private TableColumn<ProductInTable, String> wayOfBuyingCartTableCol;
    @FXML private TableColumn<ProductInTable, Float> amountCartTableCol;
    @FXML private GridPane orderGridPane;
    private GridPane findingCheapestBasketGridPane;
    private ScrollPane findingCheapestBasketScrollPane;
    private FindingCheapestBasketController findingCheapestBasketController;

    public MakeDynamicOrderController() {
        productsInOrder = new LinkedList<>();
        amountInTextField = new SimpleFloatProperty();
    }

    public void initDetails(SDMSystem sdmSystem,
                            DTOCustomer customerMakingTheOrder,
                            MakeOrderMainController makeOrderMainController,
                            Map<Integer, Collection<Pair<IDTOProductInStore, Float>>> shoppingCart,
                            SimpleFloatProperty totalProductsCost,
                            SimpleFloatProperty totalDeliveryCost,
                            BorderPane mainBorderPane, SimpleBooleanProperty animationStatus) {
        this.sdmSystem = sdmSystem;
        this.customerMakingTheOrder = customerMakingTheOrder;
        this.makeOrderMainController = makeOrderMainController;
        this.cheapestBasket = shoppingCart;
        this.totalProductsCost = totalProductsCost;
        this.totalDeliveryCost = totalDeliveryCost;
        this.mainBorderPane = mainBorderPane;
        this.animationStatus = animationStatus;
        initProductsToBuyTable();
    }

    @FXML
    public void initialize(){
        makeMainScrollPaneSettings();
        initErrorsLabel();
        continueButton.disableProperty().bind(Bindings.isEmpty(cartTable.getItems()));

        amountTextField.disableProperty().bind(chooseProductComboBox.valueProperty().isNull());
        JavaFxHelper.makeTextFieldInputOnlyFloat(amountTextField,errorInputLabel,amountInTextField);
        addToCartButton.disableProperty().bind(
                chooseProductComboBox.valueProperty().isNull().or(
                        amountTextField.textProperty().isEmpty()
                ));

        initProductsToBuyTableSettings();
        initProductsCartTableSettings();
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

    private void initProductsToBuyTableSettings() {
        productNameCol.setCellValueFactory(new PropertyValueFactory<>("productName"));
        productIdCol.setCellValueFactory(new PropertyValueFactory<>("productId"));
        wayOfBuyingCol.setCellValueFactory(new PropertyValueFactory<>("wayOfBuying"));
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
        makeDynamicOrderMainScrollPain.setMinWidth(250);
        makeDynamicOrderMainScrollPain.setMinHeight(250);
        makeDynamicOrderMainScrollPain.setMaxHeight(1000);
        makeDynamicOrderMainScrollPain.setMaxWidth(4000);
        makeDynamicOrderMainScrollPain.fitToWidthProperty().set(true);
        makeDynamicOrderMainScrollPain.fitToHeightProperty().set(true);
    }


    @FXML
    void onAddToCart(ActionEvent event) {
        Float amountEntered = Float.parseFloat(amountTextField.getText());
        DTOProduct chosenProduct = chooseProductComboBox.getValue();
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
            productsInOrder.add(new Pair(chosenProduct,amountEntered));
            clearLabelsAfterAddingProductToCart();
            if(animationStatus.getValue() == true) {
                startProductToCartAnimation(chosenProduct);
            }
        }
    }

    private void startProductToCartAnimation(DTOProduct chosenProduct) {
        PathTransition transition = new PathTransition();
        Label productNameLabel = new Label(chosenProduct.getProductName());
        orderGridPane.add(productNameLabel,0,2);
        transition.statusProperty().addListener(new ChangeListener<Animation.Status>() {
            @Override
            public void changed(ObservableValue<? extends Animation.Status> observable, Animation.Status oldValue, Animation.Status newValue) {
                if(newValue == Animation.Status.STOPPED){
                    orderGridPane.getChildren().remove(productNameLabel);
                }
            }
        });
        Path path = new Path();
        path.getElements().add(new MoveTo(0,0));
        path.getElements().add(new LineTo(650,420));
        transition.setNode(productNameLabel);
        transition.setDuration(Duration.seconds(1.5));
        transition.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
        transition.setPath(path);
        transition.setCycleCount(1);
        transition.play();
    }

    private void clearLabelsAfterAddingProductToCart() {
        amountTextField.setText("");
        chooseProductComboBox.getSelectionModel().clearSelection();
        zeroErrorLabel.visibleProperty().set(false);
        zeroErrorLabel.setManaged(false);
        notIntegerErrorLabel.visibleProperty().set(false);
        notIntegerErrorLabel.setManaged(false);
    }

    @FXML
    void onClickCancel(ActionEvent event) {
        makeOrderMainController.cancelOrderAlert();
    }

    @FXML
    void onClickContinue(ActionEvent event) {
        SimpleBooleanProperty done = new SimpleBooleanProperty(false);

        loadFindingCheapestBasketForm();
        FindCheapestBasketTask findCheapestBasketTask = new FindCheapestBasketTask(
                sdmSystem,
                cheapestBasket,
                productsInOrder,
                totalProductsCost,
                totalDeliveryCost,
                customerMakingTheOrder,
                done);
        findingCheapestBasketController.init(findCheapestBasketTask,done,this);
        mainBorderPane.setCenter(findingCheapestBasketScrollPane);
        findCheapestBasketAndUpdateTotalPriceAndDeliveryCost(findCheapestBasketTask);
        //createStoresParticipatingInDynamicOrderForm();
    }

    private void loadFindingCheapestBasketForm() {
        FxmlLoader<GridPane, FindingCheapestBasketController> loaderFindingCheapestBasketForm = new FxmlLoader<>(FindingCheapestBasketController.FINDING_CHEAPEST_BASKET_FORM_FXML_PATH);
        findingCheapestBasketGridPane = loaderFindingCheapestBasketForm.getFormBasePane();
        findingCheapestBasketController = loaderFindingCheapestBasketForm.getFormController();
        findingCheapestBasketScrollPane = new ScrollPane();
        JavaFxHelper.initMainScrollPane(findingCheapestBasketScrollPane);
        findingCheapestBasketScrollPane.setContent(findingCheapestBasketGridPane);
    }

    private void findCheapestBasketAndUpdateTotalPriceAndDeliveryCost(FindCheapestBasketTask findCheapestBasketTask) {
        //sdmSystem.makeCheapestBasket(cheapestBasket,productsInOrder);
        //findCheapestBasketTask.run();
        new Thread(findCheapestBasketTask).start();
        //calcTotalProductsPriceAndDeliveryCost();
    }

    public void calcTotalProductsPriceAndDeliveryCost() {
        float deliveryCost;
        float costOfProductsInStore;
        for (Integer storeSerialNumber : cheapestBasket.keySet()) {
            DTOStore storeSellingTheProducts = sdmSystem.getStoreFromStores(storeSerialNumber);
            deliveryCost = sdmSystem.getDeliveryCost(storeSellingTheProducts.getStoreSerialNumber(), customerMakingTheOrder.getCustomerLocation());
            totalDeliveryCost.set(totalDeliveryCost.floatValue() + deliveryCost);
            costOfProductsInStore = sdmSystem.calcProductsInOrderCost(cheapestBasket.get(storeSerialNumber));
            totalProductsCost.set(totalProductsCost.floatValue() + costOfProductsInStore);
        }
    }

    public void createStoresParticipatingInDynamicOrderForm() {
        loadStoresParticipatingInDynamicOrderForm();
        mainBorderPane.setCenter(storesParticipatingInDynamicOrderScrollPane);
        storesParticipatingInDynamicOrderController.initDetails(sdmSystem,cheapestBasket,customerMakingTheOrder,makeOrderMainController,this);

    }

    private void loadStoresParticipatingInDynamicOrderForm() {
        FxmlLoader<ScrollPane,StoresParticipatingInDynamicOrderController> loaderStoresParticipatingInDynamicOrderForm = new FxmlLoader<>(StoresParticipatingInDynamicOrderController.STORES_PARTICIPATING_IN_DYNAMIC_ORDER_FORM_FXML_PATH);
        storesParticipatingInDynamicOrderScrollPane = loaderStoresParticipatingInDynamicOrderForm.getFormBasePane();
        storesParticipatingInDynamicOrderController = loaderStoresParticipatingInDynamicOrderForm.getFormController();
    }

    @FXML
    void onSelectProduct(ActionEvent event) {

    }


    private void initProductsToBuyTable() {
        for(DTOProduct product : sdmSystem.getProductsInSystem().values()){
            productsToBuyTable.getItems().add(new ProductInTable(
                    product.getProductName(),
                    product.getProductSerialNumber(),
                    product.getWayOfBuying(),
                    null,
                    null
            ));
            chooseProductComboBox.getItems().add(product);
        }
    }

    public void ifHasDiscountMakeDiscountsForm() {
        if(checkIfDeserveDiscount()){
            makeOrderMainController.createDiscountsInOrderForm(cartTable.getItems());
        }
        else{
            makeOrderMainController.createOrderSummaryForm();
        }
    }

    private boolean checkIfDeserveDiscount() {
        boolean answer = false;
        for(Integer storeId : cheapestBasket.keySet()){
            if(sdmSystem.storeHasDiscountWithOneOfTheProducts(storeId,cheapestBasket.get(storeId))) {
                answer = true;
                break;
            }
        }

        return answer;
    }
}
