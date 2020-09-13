package components.details.storeDetails.deleteProduct;

import SDMSystem.system.SDMSystem;
import SDMSystemDTO.product.DTOProductInStore;
import SDMSystemDTO.store.DTOStore;
import components.details.storeDetails.StoreDetailsController;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;

import java.util.Optional;

public class DeleteProductController {

    private SDMSystem sdmSystem;
    private StoreDetailsController storeDetailsController;
    private final SimpleBooleanProperty deleteTryWasMade;
    private ScrollPane storeDetailsScrollPain;

    @FXML private HBox selectProductHBox;
    @FXML private FlowPane productsFlowPane;
    @FXML private ScrollPane productTabScrollPane;
    @FXML private ChoiceBox<DTOProductInStore> productChoiseBox;
    @FXML private Button deleteProductButton;
    @FXML private HBox statusHBox;
    @FXML private Label statusLabel;
    @FXML private Label statusStaticLabel;
    @FXML private Button backButton;
    @FXML private ScrollPane mainFormScrollPane;

    private BorderPane mainBorderPane;

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
        statusStaticLabel.visibleProperty().bind(statusLabel.visibleProperty());
    }

    public void initDetails(SDMSystem sdmSystem, StoreDetailsController storeDetailsController, BorderPane mainBorderPane, ScrollPane storeDetailsScrollPain){
        mainFormScrollPane.fitToWidthProperty().set(true);
        mainFormScrollPane.fitToHeightProperty().set(true);
        this.sdmSystem = sdmSystem;
        this.storeDetailsController = storeDetailsController;
        this.mainBorderPane = mainBorderPane;
        this.storeDetailsScrollPain = storeDetailsScrollPain;
    }

    public void setSdmSystem(SDMSystem sdmSystem) {
        this.sdmSystem = sdmSystem;
    }

    public void setStoreDetailsController(StoreDetailsController storeDetailsController) {
        this.storeDetailsController = storeDetailsController;
    }

    public void setMainBorderPane(BorderPane mainBorderPane) {
        this.mainBorderPane = mainBorderPane;
    }

    public void setStoreDetailsScrollPain(ScrollPane storeDetailsScrollPain) {
        this.storeDetailsScrollPain = storeDetailsScrollPain;
    }

    public FlowPane getProductsFlowPane() {
        return productsFlowPane;
    }

    public ChoiceBox<DTOProductInStore> getProductChoiseBox() {
        return productChoiseBox;
    }

    @FXML
    void onClickDelete(ActionEvent event) {
        DTOProductInStore productToDelete = productChoiseBox.getSelectionModel().getSelectedItem();
        //in order to send by ref
        String[] reason = new String[1];
        try {
            if (sdmSystem.isProductDeletable(storeDetailsController.getStoreSerialNumber(), productToDelete.getProductSerialNumber(), reason)) {
                if (sdmSystem.isPartOfDiscount(
                        storeDetailsController.getStoreSerialNumber(),
                        productToDelete.getProductSerialNumber())) {
                    createAndShowProductInDiscountDeleteAlert();
                    sdmSystem.deleteDiscountsTheProductIsPartOf(
                            storeDetailsController.getStoreSerialNumber(),
                            productToDelete.getProductSerialNumber());
                }
                sdmSystem.deleteProductFromStore(productToDelete);
                statusLabel.setText("The product was deleted successfully!");
                //update DTOStore with deleted product
                storeDetailsController.setStore(sdmSystem.getStoreFromStores(storeDetailsController.getStoreSerialNumber()));
                storeDetailsController.addProductsFromStoreToFlowPane(productsFlowPane);
                storeDetailsController.addProductsToProductChoisBox(productChoiseBox);
            }
            else {
                statusLabel.setText(reason[0]);
            }
        } catch (RuntimeException e) {
            statusLabel.setText(e.getMessage());
        }
        finally {
            deleteTryWasMade.set(true);
        }
    }

    private void createAndShowProductInDiscountDeleteAlert() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Warning");
        alert.setHeaderText("You are about to delete a product that is a part of a discount!");
        alert.setContentText("The discount will be deleted as well.\nAre you ok with this?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() != ButtonType.OK) {
            throw new RuntimeException("Cancelled");
        }
    }

    @FXML
    void onBackButton(ActionEvent event) {
        mainBorderPane.setCenter(storeDetailsScrollPain);
    }
}
