package components.details.storeDetails.addProduct;

import SDMSystem.system.SDMSystem;
import SDMSystemDTO.product.DTOProduct;
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
import javafx.util.converter.IntegerStringConverter;
import javafx.util.converter.NumberStringConverter;

import java.util.Collection;
import java.util.function.UnaryOperator;

public class AddProductController {

    private SDMSystem sdmSystem;
    private StoreDetailsController storeDetailsController;
    private final SimpleBooleanProperty addTryWasMade;

    @FXML private HBox selectProductHBox;
    @FXML private ScrollPane productTabScrollPane;
    @FXML private Button addProductButton;
    @FXML private HBox statusHBox;
    @FXML private FlowPane productsFlowPane;
    @FXML private Label statusLabel;
    @FXML private ChoiceBox<DTOProduct> productChoiseBox;
    @FXML private TextField priceTextField;
    @FXML private Label errorInputLabel;
    @FXML private Label statusStaticLabel;


    public AddProductController() {
        addTryWasMade = new SimpleBooleanProperty(false);
    }

    @FXML
    public void initialize(){
        selectProductHBox.setAlignment( Pos.CENTER );
        productTabScrollPane.fitToHeightProperty().set(true);
        productTabScrollPane.fitToWidthProperty().set(true);
        BooleanBinding binding = productChoiseBox.valueProperty().isNull().or(Bindings.createBooleanBinding(() ->
                priceTextField.getText().trim().isEmpty(),priceTextField.textProperty()));
        addProductButton.disableProperty().bind(binding);
        statusHBox.setAlignment(Pos.CENTER);
        productsFlowPane.setAlignment(Pos.CENTER);
        statusLabel.visibleProperty().bind(addTryWasMade);
        errorInputLabel.setAlignment(Pos.CENTER);
        errorInputLabel.visibleProperty().set(false);
        statusStaticLabel.visibleProperty().bind(statusLabel.visibleProperty());
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

    @FXML
    void onClickAdd(ActionEvent event) {
        DTOProduct productToAdd = productChoiseBox.getSelectionModel().getSelectedItem();
        try {
            addTryWasMade.set(true);
            float price = Float.parseFloat(priceTextField.getText());
            priceTextField.setText("");
            if(price >0) {
                sdmSystem.addProductToStore(storeDetailsController.getStore(), productToAdd, price);
                statusLabel.setText("The product was added successfully!");
                //update DTOStore with added product
                storeDetailsController.setStore(sdmSystem.getStoreFromStores(storeDetailsController.getStoreSerialNumber()));
                storeDetailsController.initProductsYouCanAddToStore();
            }
            else{
                statusLabel.setText("The price must be a positive number!");
            }
        } catch (NumberFormatException e) {
            statusLabel.setText("The price must be a float number!");
        }
    }

    public FlowPane getProductsFlowPane() {
        return productsFlowPane;
    }

    public ChoiceBox<DTOProduct> getProductChoiseBox() {
        return productChoiseBox;
    }


}
