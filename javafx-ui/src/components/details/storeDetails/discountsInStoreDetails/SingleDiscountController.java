package components.details.storeDetails.discountsInStoreDetails;

import SDMSystemDTO.discount.DTODiscount;
import SDMSystemDTO.discount.DTOOffer;
import SDMSystemDTO.discount.DiscountKind;
import SDMSystemDTO.product.WayOfBuying;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.net.URL;

public class SingleDiscountController {
    private SimpleDoubleProperty quantityToBuyToGetDiscount;
    private SimpleStringProperty wayOfBuying; //kilos or Units
    private SimpleIntegerProperty productIdForGettingDiscount;
    private SimpleStringProperty productNameForGettingDiscount;
    private SimpleStringProperty discountKindString;
    private DTODiscount discount;
    private GridPane productsInDiscountGridPane;
    private ProductInDiscountDetailsController productInDiscountDetailsController;
    private static final String Product_In_Discount_Details_Fxml_Path = "/components/details/storeDetails/discountsInStoreDetails/ProductInDiscountDetails.fxml";




    @FXML private Label discountTitleLabel;
    @FXML private FlowPane productsInDiscountFlowPane;
    @FXML private Label whatYouGetLabel;

    public SingleDiscountController(){
        quantityToBuyToGetDiscount = new SimpleDoubleProperty();
        wayOfBuying = new SimpleStringProperty();
        productIdForGettingDiscount = new SimpleIntegerProperty();
        productNameForGettingDiscount = new SimpleStringProperty();
        discountKindString = new SimpleStringProperty();
    }

    @FXML
    private void initialize() {
        discountTitleLabel.textProperty().bind(Bindings.concat(
                "If you buy ", quantityToBuyToGetDiscount, " " ,
                       wayOfBuying , " of " , productNameForGettingDiscount , " (ID:" , productIdForGettingDiscount , ")"));
        whatYouGetLabel.textProperty().bind(Bindings.concat(
                "you get ", discountKindString, ":"
        ));

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
        productNameForGettingDiscount.set(discount.getProductNameToBuyForDiscount());
        getDiscountKind(discount.getDiscountKind());

        initProductsInDiscount();
    }

    private void getDiscountKind(DiscountKind discountKind) {
        if(discountKind == DiscountKind.ALL_OR_NOTHING){
            discountKindString.set("all of the following products");
        } else if (discountKind == DiscountKind.ONE_OF) {
            discountKindString.set("one of the following products");
        }
        else {
            discountKindString.set("");
        }
    }

    private void initProductsInDiscount() {
        for(DTOOffer offer : discount.getOffers()){
            loadProductInDiscountDetails();
            productInDiscountDetailsController.initDetails(
                    offer.getProductSerialNumber(),
                    offer.getProductQuantity(),
                    offer.getPricePerUnit(),
                    offer.getProductName()
            );

            productsInDiscountFlowPane.getChildren().add(productsInDiscountGridPane);
        }
    }

    private void loadProductInDiscountDetails() {
        FXMLLoader loader;
        URL mainFXML;
        loader = new FXMLLoader();
        mainFXML = getClass().getResource(Product_In_Discount_Details_Fxml_Path);
        loader.setLocation(mainFXML);
        try {
            productsInDiscountGridPane = loader.load();
            productInDiscountDetailsController = loader.getController();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
