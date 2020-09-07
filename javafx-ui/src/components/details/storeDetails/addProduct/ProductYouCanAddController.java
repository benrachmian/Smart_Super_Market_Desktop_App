package components.details.storeDetails.addProduct;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ProductYouCanAddController {
    private SimpleIntegerProperty productId;
    private SimpleStringProperty productName;
    private SimpleStringProperty wayOfBuying;

    @FXML private Label productIdLabel;
    @FXML private Label productNameLabel;
    @FXML private Label wayOfBuyingLabel;

    public ProductYouCanAddController() {
        productId = new SimpleIntegerProperty();
        productName = new SimpleStringProperty();
        wayOfBuying = new SimpleStringProperty();
    }

    @FXML
    private void initialize() {
        productIdLabel.textProperty().bind(productId.asString());
        productNameLabel.textProperty().bind(productName);
        wayOfBuyingLabel.textProperty().bind(wayOfBuying);
    }

    public void initDetails(int productSerialNumber, String productName, String wayOfBuying) {
        this.productId.set(productSerialNumber);
        this.productName.set(productName);
        this.wayOfBuying.set(wayOfBuying);
    }
}
