package components.details.storeDetails;

import SDMSystem.system.SDMSystem;
import SDMSystemDTO.product.DTOProductInStore;
import SDMSystemDTO.store.DTOStore;
import components.details.storeDetails.productInStoreDetails.ProductInStoreController;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TabPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.net.URL;

public class StoreDetailsController {

    private SDMSystem sdmSystem;
    private GridPane productGridPane;
    private ProductInStoreController productInStoreController;
    private static final String Product_In_Store_Details_Fxml_Path = "/components/details/storeDetails/productInStoreDetails/productInStoreDetails.fxml";

    @FXML private TabPane storeTabPane;
    @FXML private ScrollPane productTabScrollPane;
    @FXML private Label storeSerialNumberAnswerLabel;
    @FXML private Label storeNameAnswerLabel;
    @FXML private Label storePpkAnswerLabel;
    @FXML private Label totalProfitFromDeliveryAnswerLabel;
    @FXML private FlowPane productsFlowPane;



    public void setSdmSystem(SDMSystem sdmSystem) {
        this.sdmSystem = sdmSystem;
    }

    @FXML
    public void initialize(){
        productTabScrollPane.fitToWidthProperty().set(true);
//        productsFlowPane.setPadding(new Insets(10,10,10,10));
//        productsFlowPane.setVgap(4);
//        productsFlowPane.setHgap(4);
    }

    public void updatePane(DTOStore store) {
        storeSerialNumberAnswerLabel.setText(String.format("%d",store.getStoreSerialNumber()));
        storeNameAnswerLabel.setText(store.getStoreName());
        storePpkAnswerLabel.setText(String.format("%.2f",store.getPpk()));
        totalProfitFromDeliveryAnswerLabel.setText(String.format("%.2f",store.getTotalProfitFromDelivery()));
        productsFlowPane.getChildren().clear();
        updateProducts(store);
        Task<Boolean> currentRunningTask = new Task<Boolean>() {
            @Override
            protected Boolean call() throws Exception {
                updateProducts(store);
                return true;
            }
        };
        new Thread(currentRunningTask).start();
    }

    private synchronized void updateProducts(DTOStore store) {
        for(DTOProductInStore product : store.getProductsInStore().values()){
            //GridPane newProductGridPane = getGridPaneWithSameProperties();

            //for(Node node : productGridPane.getChildren()){
                loadProductInStoreDetails();
                //productInStoreController = new ProductInStoreController();
                productInStoreController.initDetails(
                        product.getProductSerialNumber(),
                        product.getProductName(),
                        product.getWayOfBuying().toString(),
                        product.getPrice(),
                        product.getAmountSoldInStore());

                productsFlowPane.getChildren().add(productGridPane);


//                if(node.getId() != null && node.getId().equals("productNameLabel")){
//                    ((Label)node).setText("ben");
//                }
           // }
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
