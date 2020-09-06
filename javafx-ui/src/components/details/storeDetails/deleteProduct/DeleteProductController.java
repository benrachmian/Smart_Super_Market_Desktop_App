package components.details.storeDetails.deleteProduct;

import SDMSystem.system.SDMSystem;
import SDMSystemDTO.product.DTOProductInStore;
import components.details.storeDetails.StoreDetailsController;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;

public class DeleteProductController {

    private SDMSystem sdmSystem;
    private StoreDetailsController storeDetailsController;
    private SimpleBooleanProperty deleteTryWasMade;

    @FXML private HBox selectProductHBox;
    @FXML private FlowPane productsFlowPane;
    @FXML private ScrollPane productTabScrollPane;
    @FXML private ChoiceBox<DTOProductInStore> productChoiseBox;
    @FXML private Button deleteProductButton;
    @FXML private HBox statusHBox;
    @FXML private Label statusLabel;

    public DeleteProductController() {
        deleteTryWasMade = new SimpleBooleanProperty(false);
    }

    @FXML
    public void initialize(){
        selectProductHBox.setAlignment( Pos.CENTER );
        productTabScrollPane.fitToHeightProperty().set(true);
        productTabScrollPane.fitToWidthProperty().set(true);
        deleteProductButton.disableProperty().bind(productChoiseBox.valueProperty().isNull());
        statusHBox.setAlignment(Pos.CENTER);
        productsFlowPane.setAlignment(Pos.CENTER);
        statusLabel.visibleProperty().bind(deleteTryWasMade);

    }

    public void setSdmSystem(SDMSystem sdmSystem) {
        this.sdmSystem = sdmSystem;
    }

    public void setStoreDetailsController(StoreDetailsController storeDetailsController) {
        this.storeDetailsController = storeDetailsController;
    }




    //    public void setStore(DTOStore store) {
//        this.store = store;
//    }

    public FlowPane getProductsFlowPane() {
        return productsFlowPane;
    }

    public ChoiceBox<DTOProductInStore> getProductChoiseBox() {
        return productChoiseBox;
    }

    @FXML
    void onClickDelete(ActionEvent event) {
        DTOProductInStore productToDelete = productChoiseBox.getSelectionModel().getSelectedItem();
        //if deleted successfully
        if(sdmSystem.deleteProductFromStore(productToDelete)){
            statusLabel.setText("The product was deleted successfully!");
            //update DTOStore with deleted product
            storeDetailsController.setStore(sdmSystem.getStoreFromStores(storeDetailsController.getStoreSerialNumber()));
            storeDetailsController.addProductsFromStoreToFlowPane(productsFlowPane);
            storeDetailsController.addProductsToProductChoisBox(productChoiseBox);
        }
        else{
            statusLabel.setText(String.format("the product %s is available only in this store, therefore you can't delete it!", productToDelete.getProductName()));
        }
        deleteTryWasMade.set(true);
    }
}
