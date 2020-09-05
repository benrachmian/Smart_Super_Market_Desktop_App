package components.details.storeDetails.discountsInStoreDetails;

import SDMSystemDTO.discount.DTODiscount;
import SDMSystemDTO.product.WayOfBuying;
import SDMSystemDTO.store.DTOStore;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.util.Collection;

public class SingleDiscountController {
    private SimpleDoubleProperty quantityToBuyToGetDiscount;
    private SimpleStringProperty wayOfBuying; //kilos or Units
    private SimpleIntegerProperty productIdForGettingDiscount;
    private DTODiscount discount;



    @FXML private Label discountTitleLabel;

    public SingleDiscountController(){
        quantityToBuyToGetDiscount = new SimpleDoubleProperty();
        wayOfBuying = new SimpleStringProperty();
        productIdForGettingDiscount = new SimpleIntegerProperty();
    }

    @FXML
    private void initialize() {
        discountTitleLabel.textProperty().bind(Bindings.concat(
                "If you buy " + quantityToBuyToGetDiscount + " " +
                       wayOfBuying + " of product " + productIdForGettingDiscount + " you get:" ));
    }

    public void initDetails(DTODiscount discount) {
        this.discount = discount;
        quantityToBuyToGetDiscount.set(discount.getIfYouBuyProductAndAmount().getValue());
        if (discount.getProductWayOfBuying() == WayOfBuying.BY_QUANTITY) {
            wayOfBuying.set("units");
        } else {
            wayOfBuying.set("kilos");
        }
        productIdForGettingDiscount.set(discount.getIfYouBuyProductAndAmount().getKey());
    }
}
