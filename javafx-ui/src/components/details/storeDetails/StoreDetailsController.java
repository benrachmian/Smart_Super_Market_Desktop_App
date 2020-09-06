package components.details.storeDetails;

import SDMSystem.system.SDMSystem;
import SDMSystemDTO.discount.DTODiscount;
import SDMSystemDTO.product.DTOProductInStore;
import SDMSystemDTO.store.DTOStore;
import components.details.storeDetails.discountsInStoreDetails.SingleDiscountController;
import components.details.storeDetails.productInStoreDetails.ProductInStoreController;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TabPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;

public class StoreDetailsController {

    private SDMSystem sdmSystem;
    private GridPane productGridPane;
    private ProductInStoreController productInStoreController;
    private GridPane discountGridPane;
    private SingleDiscountController singleDiscountController;
    private static final String Product_In_Store_Details_Fxml_Path = "/components/details/storeDetails/productInStoreDetails/productInStoreDetails.fxml";
    private static final String Single_Discount_Details_Fxml_Path = "/components/details/storeDetails/discountsInStoreDetails/SingleDiscount.fxml";

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

    @FXML
    public void initialize(){
        productTabScrollPane.fitToWidthProperty().set(true);
        discountsTabScrollPane.fitToWidthProperty().set(true);
        discountsTabScrollPane.fitToHeightProperty().set(true);
//        productsFlowPane.setPadding(new Insets(10,10,10,10));
//        productsFlowPane.setVgap(4);
//        productsFlowPane.setHgap(4);
    }

    public void updateStoreDetailsTab(DTOStore store) {
        updateStoreGeneralDetailsTab(store);
        updateProductsTab(store);
        updateDiscountsTab(store);
    }

    private void updateDiscountsTab(DTOStore store) {
        discountsVbox.getChildren().clear();
        new Thread(() -> {
            Platform.runLater(
                    () -> updateDiscountsInStore(store)
            );
        }).start();
    }

    private void updateProductsTab(DTOStore store) {
        productsFlowPane.getChildren().clear();
        new Thread(() -> {
            Platform.runLater(
                    () -> updateProducts(store)
            );
        }).start();
    }

    private void updateStoreGeneralDetailsTab(DTOStore store) {
        storeSerialNumberAnswerLabel.setText(String.format("%d", store.getStoreSerialNumber()));
        storeNameAnswerLabel.setText(store.getStoreName());
        storePpkAnswerLabel.setText(String.format("%.2f", store.getPpk()));
        totalProfitFromDeliveryAnswerLabel.setText(String.format("%.2f", store.getTotalProfitFromDelivery()));
    }

    private synchronized void updateProducts(DTOStore store) {
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
    private synchronized void updateDiscountsInStore(DTOStore store) {
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
        mainFXML = getClass().getResource(Single_Discount_Details_Fxml_Path);
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
        mainFXML = getClass().getResource(Product_In_Store_Details_Fxml_Path);
        loader.setLocation(mainFXML);
        try {
            productGridPane = loader.load();
            productInStoreController = loader.getController();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }




}
