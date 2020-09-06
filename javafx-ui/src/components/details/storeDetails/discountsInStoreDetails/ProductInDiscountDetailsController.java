package components.details.storeDetails.discountsInStoreDetails;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ProductInDiscountDetailsController {

    private SimpleIntegerProperty productId;
    private SimpleDoubleProperty productQuantityToBuy;
    private SimpleFloatProperty pricePerUnit;
    private SimpleStringProperty productName;

    @FXML private Label productIdLabel;
    @FXML private Label quantityLabel;
    @FXML private Label pricePerUnitLabel;
    @FXML private Label productNameLabel;

    public ProductInDiscountDetailsController(){
        productId = new SimpleIntegerProperty();
        productQuantityToBuy = new SimpleDoubleProperty();
        pricePerUnit = new SimpleFloatProperty();
        productName = new SimpleStringProperty();
    }

    @FXML
    private void initialize() {
        productIdLabel.textProperty().bind(productId.asString());
        quantityLabel.textProperty().bind(productQuantityToBuy.asString());
        pricePerUnitLabel.textProperty().bind(pricePerUnit.asString());
        productNameLabel.textProperty().bind(productName);
    }

    public void initDetails(int productId, double productQuantityToBuy, float pricePerUnit, String productName){
        this.productId.set(productId);
        this.productQuantityToBuy.set(productQuantityToBuy);
        this.pricePerUnit.set(pricePerUnit);
        this.productName.set(productName);
    }

}
