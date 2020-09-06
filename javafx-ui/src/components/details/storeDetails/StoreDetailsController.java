package components.details.storeDetails;

import SDMSystem.system.SDMSystem;
import SDMSystemDTO.discount.DTODiscount;
import SDMSystemDTO.product.DTOProductInStore;
import SDMSystemDTO.store.DTOStore;
import components.details.storeDetails.deleteProduct.DeleteProductController;
import components.details.storeDetails.discountsInStoreDetails.SingleDiscountController;
import components.details.storeDetails.productInStoreDetails.ProductInStoreController;
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

public class StoreDetailsController {

    private SDMSystem sdmSystem;
    private DTOStore store;
    private GridPane productGridPane;
    private ProductInStoreController productInStoreController;
    private GridPane discountGridPane;
    private SingleDiscountController singleDiscountController;
    private static final String PRODUCT_IN_STORE_DETAILS_FXML_PATH = "/components/details/storeDetails/productInStoreDetails/productInStoreDetails.fxml";
    private static final String SINGLE_DISCOUNT_DETAILS_FXML_PATH = "/components/details/storeDetails/discountsInStoreDetails/SingleDiscount.fxml";
    private static final String DELETE_PRODUCT_FXML_PATH = "/components/details/storeDetails/deleteProduct/deleteProduct.fxml";
    private ScrollPane deleteProductScrollPane;
    private DeleteProductController deleteProductController;
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
        productTabScrollPane.fitToWidthProperty().set(true);
        discountsTabScrollPane.fitToWidthProperty().set(true);
        discountsTabScrollPane.fitToHeightProperty().set(true);
//        productsFlowPane.setPadding(new Insets(10,10,10,10));
//        productsFlowPane.setVgap(4);
//        productsFlowPane.setHgap(4);
    }

    public void updateStoreDetailsTab() {
        updateStoreGeneralDetailsTab();
        updateProductsTab();
        updateDiscountsTab();
    }

    private void updateDiscountsTab( ) {
        discountsVbox.getChildren().clear();
        new Thread(() -> {
            Platform.runLater(
                    () -> updateDiscountsInStore()
            );
        }).start();
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
        if(store.getStoreDiscounts() != null) {
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
        FXMLLoader loader;
        URL mainFXML;
        loader = new FXMLLoader();
        mainFXML = getClass().getResource(SINGLE_DISCOUNT_DETAILS_FXML_PATH);
        loader.setLocation(mainFXML);
        try {
            discountGridPane = loader.load();
            singleDiscountController = loader.getController();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadProductInStoreDetails() {
        FXMLLoader loader;
        URL mainFXML;
        loader = new FXMLLoader();
        mainFXML = getClass().getResource(PRODUCT_IN_STORE_DETAILS_FXML_PATH);
        loader.setLocation(mainFXML);
        try {
            productGridPane = loader.load();
            productInStoreController = loader.getController();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void clickOnDeleteProduct(ActionEvent event) {
        //show delete product form
        loadDeleteProductForm();
        mainBorderPane.setCenter(deleteProductScrollPane);
        deleteProductScrollPane.fitToWidthProperty().set(true);
        deleteProductScrollPane.fitToHeightProperty().set(true);
        deleteProductController.setSdmSystem(sdmSystem);
        deleteProductController.setStoreDetailsController(this);
        //deleteProductController.setStore(store);
        addProductsFromStoreToFlowPane(deleteProductController.getProductsFlowPane());
        addProductsToProductChoisBox(deleteProductController.getProductChoiseBox());


    }

    public void addProductsToProductChoisBox(ChoiceBox<DTOProductInStore> productChoiseBox) {
        productChoiseBox.getItems().clear();
        for(DTOProductInStore product : store.getProductsInStore().values()){
            productChoiseBox.getItems().add(product);
        }

    }

    private void loadDeleteProductForm() {
        FXMLLoader loader;
        URL mainFXML;
        loader = new FXMLLoader();
        mainFXML = getClass().getResource(DELETE_PRODUCT_FXML_PATH);
        loader.setLocation(mainFXML);
        try {
            deleteProductScrollPane = loader.load();
            deleteProductController = loader.getController();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }




}
