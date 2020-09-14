package components.details.storeDetails;

import SDMSystem.product.Product;
import SDMSystem.system.SDMSystem;
import SDMSystemDTO.discount.DTODiscount;
import SDMSystemDTO.order.DTOOrder;
import SDMSystemDTO.product.DTOProduct;
import SDMSystemDTO.product.DTOProductInStore;
import SDMSystemDTO.store.DTOStore;
import common.FxmlLoader;
import common.JavaFxHelper;
import components.details.storeDetails.addProduct.AddProductController;
import components.details.storeDetails.addProduct.ProductYouCanAddController;
import components.details.storeDetails.deleteProduct.DeleteProductController;
import components.details.storeDetails.discountsInStoreDetails.SingleDiscountController;
import components.details.storeDetails.ordersInStoreDetails.OrderInStoreDetailsController;
import components.details.storeDetails.productInStoreDetails.ProductInStoreController;
import components.details.storeDetails.updateProductPrice.UpdateProductPriceController;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.util.Pair;

import java.io.IOException;
import java.net.URL;
import java.util.Collection;

public class StoreDetailsController {

    private SDMSystem sdmSystem;
    private DTOStore store;
    private GridPane productGridPane;
    private ProductInStoreController productInStoreController;
    private GridPane productYouCanAddGridPane;
    private ProductYouCanAddController productYouCanAddController;
    private GridPane orderGridPane;
    private OrderInStoreDetailsController orderInStoreDetailsController;
    private GridPane discountGridPane;
    private SingleDiscountController singleDiscountController;
    private static final String PRODUCT_IN_STORE_DETAILS_FXML_PATH = "/components/details/storeDetails/productInStoreDetails/productInStoreDetails.fxml";
    private static final String PRODUCT_YOU_CAN_ADD_FXML_PATH = "/components/details/storeDetails/addProduct/productYouCanAddDetails.fxml";
    private static final String SINGLE_DISCOUNT_DETAILS_FXML_PATH = "/components/details/storeDetails/discountsInStoreDetails/SingleDiscount.fxml";
    private static final String DELETE_PRODUCT_FXML_PATH = "/components/details/storeDetails/deleteProduct/deleteProduct.fxml";
    private static final String ADD_PRODUCT_FXML_PATH = "/components/details/storeDetails/addProduct/addProduct.fxml";
    private static final String UPDATE_PRODUCT_FXML_PATH = "/components/details/storeDetails/updateProductPrice/updateProductPrice.fxml";
    private ScrollPane deleteProductScrollPane;
    private DeleteProductController deleteProductController;
    private ScrollPane addProductScrollPane;
    private AddProductController addProductController;
    private ScrollPane updateProductScrollPane;
    private UpdateProductPriceController updateProductPriceController;
    private BorderPane mainBorderPane;

    @FXML private TabPane storeTabPane;
    @FXML private ScrollPane productTabScrollPane;
    @FXML private Label storeSerialNumberAnswerLabel;
    @FXML private Label storeNameAnswerLabel;
    @FXML private Label storePpkAnswerLabel;
    @FXML private Label totalProfitFromDeliveryAnswerLabel;
    @FXML private FlowPane productsFlowPane;
    @FXML private VBox discountsVbox;
    @FXML private ScrollPane discountsTabScrollPane;
    @FXML private ScrollPane storeDetailsScrollPain;
    @FXML private ScrollPane ordersTabScrollPane;
    @FXML private FlowPane ordersFlowPane;



    public void setSdmSystem(SDMSystem sdmSystem) {
        this.sdmSystem = sdmSystem;
    }

    public void setMainBorderPane(BorderPane mainBorderPane) {
        this.mainBorderPane = mainBorderPane;
    }

    public void setStore(DTOStore store) {
        this.store = store;
    }

    @FXML
    public void initialize(){
       // JavaFxHelper.initMainScrollPane(ordersTabScrollPane);
//        ordersTabScrollPane.fitToHeightProperty().set(true);
//        ordersTabScrollPane.fitToWidthProperty().set(true);
//        productTabScrollPane.fitToWidthProperty().set(true);
//        discountsTabScrollPane.fitToWidthProperty().set(true);
//        discountsTabScrollPane.fitToHeightProperty().set(true);
    }

    public DTOStore getStore() {
        return store;
    }

    public void updateStoreDetailsTab() {
        updateStoreGeneralDetailsTab();
        updateProductsTab();
        updateDiscountsTab();
        updateOrdersTab();
    }



    private void updateDiscountsTab( ) {
        discountsVbox.getChildren().clear();
        new Thread(() -> {
            Platform.runLater(
                    () -> updateDiscountsInStore()
            );
        }).start();
    }

    private void updateOrdersTab() {
        ordersFlowPane.getChildren().clear();
        addOrdersFromStoreToFlowPane();
//        new Thread(() -> {
//            Platform.runLater(
//                    () -> addOrdersFromStoreToFlowPane()
//            );
//        }).start();
    }



    private void updateProductsTab( ) {
        productsFlowPane.getChildren().clear();
        new Thread(() -> {
            Platform.runLater(
                    () -> addProductsFromStoreToFlowPane(productsFlowPane)
            );
        }).start();
    }

    private void updateStoreGeneralDetailsTab( ) {
        storeSerialNumberAnswerLabel.setText(String.format("%d", store.getStoreSerialNumber()));
        storeNameAnswerLabel.setText(store.getStoreName());
        storePpkAnswerLabel.setText(String.format("%.2f", store.getPpk()));
        totalProfitFromDeliveryAnswerLabel.setText(String.format("%.2f", store.getTotalProfitFromDelivery()));
    }

    private synchronized void addOrdersFromStoreToFlowPane() {
        ordersFlowPane.getChildren().clear();
        if(sdmSystem.getOrdersFromStore(store.getStoreSerialNumber()).size() > 0){
            for(DTOOrder order : sdmSystem.getOrdersFromStore(store.getStoreSerialNumber())){
                loadOrderInStoreDetails();
                orderInStoreDetailsController.initDetails(
                        order.getOrderDate(),
                        order.getAmountOfProducts(),
                        order.getProductsCost(),
                        order.getDeliveryCost(),
                        order.getOrderCost(),
                        order.getMainOrder()
                );
                ordersFlowPane.getChildren().add(orderGridPane);
            }
        }
        else {
            Label noOrders = new Label("There are no any discounts yet!");
            noOrders.paddingProperty().setValue(new Insets(8, 8, 8, 8));
            ordersFlowPane.getChildren().add(noOrders);
        }
    }



    public synchronized void addProductsFromStoreToFlowPane(FlowPane productsFlowPane ) {
        productsFlowPane.getChildren().clear();
        for(DTOProductInStore product : store.getProductsInStore().values()){
                loadProductInStoreDetails();
                productInStoreController.initDetails(
                        product.getProductSerialNumber(),
                        product.getProductName(),
                        product.getWayOfBuying().toString(),
                        product.getPrice(),
                        product.getAmountSoldInStore());

                productsFlowPane.getChildren().add(productGridPane);
        }
    }

    public int getStoreSerialNumber(){
        return store.getStoreSerialNumber();
    }

    private synchronized void updateDiscountsInStore( ) {
        //at least one discount
        if(store.getStoreDiscounts() != null && store.getStoreDiscounts().size() >= 1) {
            discountsVbox.getChildren().clear();
            for (DTODiscount discount : store.getStoreDiscounts()) {
                loadDiscountDetails();
                singleDiscountController.initDetails(discount);
                discountsVbox.getChildren().add(discountGridPane);
            }
        }
        else{
            Label noDiscounts = new Label("There are no any discounts yet!");
            noDiscounts.paddingProperty().setValue(new Insets(8,8,8,8));
            discountsVbox.getChildren().add(noDiscounts);
        }

    }

    private void loadDiscountDetails() {
        FxmlLoader<GridPane,SingleDiscountController> loaderDiscountDetails = new FxmlLoader<>(SINGLE_DISCOUNT_DETAILS_FXML_PATH);
        discountGridPane = loaderDiscountDetails.getFormBasePane();
        singleDiscountController = loaderDiscountDetails.getFormController();
    }

    private void loadOrderInStoreDetails() {
        FxmlLoader<GridPane,OrderInStoreDetailsController> loaderOrderInStoreDetails = new FxmlLoader<>(OrderInStoreDetailsController.ORDER_IN_STORE_DETAILS_FXML_PATH);
        orderGridPane = loaderOrderInStoreDetails.getFormBasePane();
        orderInStoreDetailsController = loaderOrderInStoreDetails.getFormController();

    }

     private void loadProductInStoreDetails() {
         FxmlLoader<GridPane,ProductInStoreController> loaderProductInStoreDetails = new FxmlLoader<>(PRODUCT_IN_STORE_DETAILS_FXML_PATH);
         productGridPane = loaderProductInStoreDetails.getFormBasePane();
         productInStoreController = loaderProductInStoreDetails.getFormController();
    }

    private void loadProductYouCanAddDetails() {
        FxmlLoader<GridPane,ProductYouCanAddController> loaderProductYouCanAddDetails = new FxmlLoader<>(PRODUCT_YOU_CAN_ADD_FXML_PATH);
        productYouCanAddGridPane = loaderProductYouCanAddDetails.getFormBasePane();
        productYouCanAddController = loaderProductYouCanAddDetails.getFormController();
    }

    @FXML
    void clickOnDeleteProduct(ActionEvent event) {
        //show delete product form
        loadDeleteProductForm();
        mainBorderPane.setCenter(deleteProductScrollPane);
        deleteProductController.initDetails(
                sdmSystem,
                this,
                mainBorderPane,
                storeDetailsScrollPain
        );
        addProductsFromStoreToFlowPane(deleteProductController.getProductsFlowPane());
        addProductsToProductChoisBox(deleteProductController.getProductChoiseBox());
    }

    @FXML
    void clickOnAddProduct(ActionEvent event) {
        //show add product form
        loadAddProductForm();
        mainBorderPane.setCenter(addProductScrollPane);
        addProductController.initDetails(sdmSystem,this, mainBorderPane,storeDetailsScrollPain);
        initProductsYouCanAddToStore();
    }

    @FXML
    void clickOnUpdateProduct(ActionEvent event) {
        //show update product form
        loadUpdateProductForm();
        mainBorderPane.setCenter(updateProductScrollPane);
        updateProductPriceController.initDetails(sdmSystem,this,mainBorderPane,storeDetailsScrollPain);
        initProductsYouCanUpdate();
    }

    public void initProductsYouCanUpdate() {
        addProductsFromStoreToFlowPane(updateProductPriceController.getProductsFlowPane());
        addProductsToProductChoisBox(updateProductPriceController.getProductChoiseBox());
    }


    public void initProductsYouCanAddToStore() {
       addProductController.getProductsFlowPane().getChildren().clear();
       addProductController.getProductChoiseBox().getItems().clear();
        Collection<DTOProduct> productsCanBeAdded = sdmSystem.getProductsTheStoreDoesntSell(store).values();

        for(DTOProduct product : productsCanBeAdded){
            loadProductYouCanAddDetails();
            productYouCanAddController.initDetails(
                    product.getProductSerialNumber(),
                    product.getProductName(),
                    product.getWayOfBuying().toString());

            addProductController.getProductsFlowPane().getChildren().add(productYouCanAddGridPane);
            addProductController.getProductChoiseBox().getItems().add(product);
        }
    }


    public void addProductsToProductChoisBox(ChoiceBox<DTOProductInStore> productChoiseBox) {
        productChoiseBox.getItems().clear();
        for(DTOProductInStore product : store.getProductsInStore().values()){
            productChoiseBox.getItems().add(product);
        }

    }

    private void loadAddProductForm() {
        FxmlLoader<ScrollPane,AddProductController> loaderAddProductForm = new FxmlLoader<>(ADD_PRODUCT_FXML_PATH);
        addProductScrollPane = loaderAddProductForm.getFormBasePane();
        addProductController = loaderAddProductForm.getFormController();
    }

    private void loadUpdateProductForm() {
        FxmlLoader<ScrollPane,UpdateProductPriceController> loaderUpdateProductForm = new FxmlLoader<>(UPDATE_PRODUCT_FXML_PATH);
        updateProductScrollPane = loaderUpdateProductForm.getFormBasePane();
        updateProductPriceController = loaderUpdateProductForm.getFormController();
    }

    private void loadDeleteProductForm() {
        FxmlLoader<ScrollPane,DeleteProductController> loaderDeleteProductForm = new FxmlLoader<>(DELETE_PRODUCT_FXML_PATH);
        deleteProductScrollPane = loaderDeleteProductForm.getFormBasePane();
        deleteProductController = loaderDeleteProductForm.getFormController();
    }

}
