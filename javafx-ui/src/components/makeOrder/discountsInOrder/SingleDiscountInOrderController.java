package components.makeOrder.discountsInOrder;

import SDMSystemDTO.discount.DTODiscount;
import SDMSystemDTO.discount.DTOOffer;
import SDMSystemDTO.discount.DiscountKind;
import SDMSystemDTO.product.WayOfBuying;
import common.FxmlLoader;
import components.details.storeDetails.discountsInStoreDetails.SingleDiscountController;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

public class SingleDiscountInOrderController {

    public static final String SINGLE_DISCOUNT_IN_ORDER_FORM_FXML_PATH = "/components/makeOrder/discountsInOrder/SingleDiscountInOrder.fxml";
    private GridPane singleDiscountGridPane;
    private SingleDiscountController singleDiscountController;
    private DTODiscount discount;
    private SimpleIntegerProperty amountLeftToImplement;

    @FXML private HBox singleDiscountMainHBox;
    @FXML private Label amountLeftToImplementLabel;

    public SingleDiscountInOrderController() {
        amountLeftToImplement = new SimpleIntegerProperty();
    }

    @FXML
    private void initialize() {
        FxmlLoader<GridPane,SingleDiscountController> loaderSingleDiscount = new FxmlLoader<>(SingleDiscountController.SINGLE_DISCOUNT_FORM_FXML_PATH);
        singleDiscountGridPane = loaderSingleDiscount.getFormBasePane();
        singleDiscountController = loaderSingleDiscount.getFormController();
        amountLeftToImplementLabel.textProperty().bind(amountLeftToImplement.asString());
        singleDiscountMainHBox.getChildren().add(0,singleDiscountGridPane);
    }

    public int getAmountLeftToImplement() {
        return amountLeftToImplement.get();
    }

    public void setAmountLeftToImplement(int amountLeftToImplement) {
        this.amountLeftToImplement.set(amountLeftToImplement);
    }

    public SimpleIntegerProperty amountLeftToImplementProperty() {
        return amountLeftToImplement;
    }

    public void initDetails(Integer value){
        singleDiscountController.initDetails(discount);
        singleDiscountController.setLabelsForDiscountInOrder();
        amountLeftToImplement.set(value);
    }

    public void setDiscount(DTODiscount discount) {
        this.discount = discount;
    }
}
