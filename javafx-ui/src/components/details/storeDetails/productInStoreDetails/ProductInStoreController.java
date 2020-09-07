package components.details.storeDetails.productInStoreDetails;

import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class ProductInStoreController {

    private SimpleIntegerProperty productId;
    private SimpleStringProperty productName;
    private SimpleStringProperty wayOfBuying;
    private SimpleFloatProperty productPrice;
    private SimpleFloatProperty productTotalBought;

    @FXML private GridPane productGridPane;
    @FXML private Label productIdLabel;
    @FXML private Label productNameLabel;
    @FXML private Label wayOfBuyingLabel;
    @FXML private Label productPriceLabel;
    @FXML private Label productTotalBoughtLabel;



    public ProductInStoreController()
    {
        this.productId = new SimpleIntegerProperty();
        this.productName = new SimpleStringProperty();
        this.wayOfBuying = new SimpleStringProperty();
        this.productPrice = new SimpleFloatProperty();
        this.productTotalBought = new SimpleFloatProperty();
    }

    @FXML
    private void initialize() {
        productIdLabel.textProperty().bind(productId.asString());
        productNameLabel.textProperty().bind(productName);
        wayOfBuyingLabel.textProperty().bind(wayOfBuying);
        productPriceLabel.textProperty().bind(productPrice.asString());
        productTotalBoughtLabel.textProperty().bind(productTotalBought.asString());
    }

    public void initDetails(int productSerialNumber, String productName, String wayOfBuying, float price, float amountSoldInStore) {
        this.productId.set(productSerialNumber);
        this.productName.set(productName);
        this.wayOfBuying.set(wayOfBuying);
        this.productPrice.set(price);
        this.productTotalBought.set(amountSoldInStore);
    }
}
