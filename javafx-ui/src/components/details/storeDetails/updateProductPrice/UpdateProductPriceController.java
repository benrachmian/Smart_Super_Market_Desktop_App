package components.details.storeDetails.updateProductPrice;

import SDMSystem.system.SDMSystem;
import SDMSystemDTO.product.DTOProductInStore;
import components.details.storeDetails.StoreDetailsController;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;

public class UpdateProductPriceController {

    private SDMSystem sdmSystem;
    private StoreDetailsController storeDetailsController;
    private final SimpleBooleanProperty updateTryWasMade;

    @FXML private HBox selectProductHBox;
    @FXML private FlowPane productsFlowPane;
    @FXML private ScrollPane productTabScrollPane;
    @FXML private ChoiceBox<DTOProductInStore> productChoiseBox;
    @FXML private Button updateProductButton;
    @FXML private HBox statusHBox;
    @FXML private Label statusLabel;
    @FXML private Label statusStaticLabel;
    @FXML private Label errorInputLabel;
    @FXML private TextField priceTextField;



    public UpdateProductPriceController() {
        updateTryWasMade = new SimpleBooleanProperty(false);
    }

    @FXML
    public void initialize(){
        selectProductHBox.setAlignment( Pos.CENTER );
        productTabScrollPane.fitToHeightProperty().set(true);
        productTabScrollPane.fitToWidthProperty().set(true);
        updateProductButton.disableProperty().bind(productChoiseBox.valueProperty().isNull());
        statusHBox.setAlignment(Pos.CENTER);
        productsFlowPane.setAlignment(Pos.CENTER);
        statusLabel.visibleProperty().bind(updateTryWasMade);
        statusStaticLabel.visibleProperty().bind(statusLabel.visibleProperty());
        BooleanBinding twoBoolBind = productChoiseBox.valueProperty().isNull().or(Bindings.createBooleanBinding(() ->
                priceTextField.getText().trim().isEmpty(),priceTextField.textProperty()));
        updateProductButton.disableProperty().bind(twoBoolBind);
        errorInputLabel.setAlignment(Pos.CENTER);
        errorInputLabel.visibleProperty().set(false);
        //not allow to write chars that aren't digits
        priceTextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(!newValue.matches("\\d*(\\.\\d*)?")) {
                    priceTextField.setText(oldValue);
                    errorInputLabel.visibleProperty().set(true);
                }
                else{
                    errorInputLabel.visibleProperty().set(false);
                }
            }
        });
    }

    public void setSdmSystem(SDMSystem sdmSystem) {
        this.sdmSystem = sdmSystem;
    }

    public void setStoreDetailsController(StoreDetailsController storeDetailsController) {
        this.storeDetailsController = storeDetailsController;
    }

    public FlowPane getProductsFlowPane() {
        return productsFlowPane;
    }

    public ChoiceBox<DTOProductInStore> getProductChoiseBox() {
        return productChoiseBox;
    }

    @FXML
    void onClickUpdate(ActionEvent event) {


    }

}