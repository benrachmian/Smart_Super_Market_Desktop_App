package components.details.storeDetails.discountsInStoreDetails;

import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ProductInDiscountDetailsController {

    private SimpleIntegerProperty productId;
    private SimpleFloatProperty productQuantityToBuy;
    private SimpleFloatProperty pricePerUnit;

    @FXML private Label productIdLabel;
    @FXML private Label quantityLabel;
    @FXML private Label pricePerUnitLabel;

    public ProductInDiscountDetailsController(){
        productId = new SimpleIntegerProperty();
        productQuantityToBuy = new SimpleFloatProperty();
        pricePerUnit = new SimpleFloatProperty();
    }

    @FXML
    private void initialize() {
        productIdLabel.textProperty().bind(productId.asString());
        quantityLabel.textProperty().bind(productQuantityToBuy.asString());
        pricePerUnitLabel.textProperty().bind(pricePerUnit.asString());
    }

    public void initDetails(int productId, float productQuantityToBuy, float pricePerUnit){
        this.productId.set(productId);
        this.productQuantityToBuy.set(productQuantityToBuy);
        this.pricePerUnit.set(pricePerUnit);
    }

}
