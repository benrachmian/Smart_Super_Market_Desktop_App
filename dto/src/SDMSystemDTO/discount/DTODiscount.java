package SDMSystemDTO.discount;

import SDMSystemDTO.product.WayOfBuying;
import javafx.util.Pair;

import java.util.Collection;

public class DTODiscount {
    String discountName;
    //pair: key = product id, value: product quantity
    Pair<Integer,Double> ifYouBuyProductAndAmount;
    DiscountKind discountKind;
    Collection<DTOOffer> offers;
    WayOfBuying productWayOfBuying;
    String productNameToBuyForDiscount;


    public DTODiscount(String discountName,
                       Pair<Integer, Double> ifYouBuyProductAndAmount,
                       DiscountKind discountKind,
                       Collection<DTOOffer> offers,
                       WayOfBuying productWayOfBuying,
                       String productNameToBuyForDiscount) {
        this.discountName = discountName;
        this.ifYouBuyProductAndAmount = ifYouBuyProductAndAmount;
        this.discountKind = discountKind;
        this.offers = offers;
        this.productWayOfBuying = productWayOfBuying;
        this.productNameToBuyForDiscount = productNameToBuyForDiscount;
    }

    public String getProductNameToBuyForDiscount() {
        return productNameToBuyForDiscount;
    }

    public String getDiscountName() {
        return discountName;
    }

    public WayOfBuying getProductWayOfBuying() {
        return productWayOfBuying;
    }

    public Pair<Integer, Double> getIfYouBuyProductAndAmount() {
        return ifYouBuyProductAndAmount;
    }

    public DiscountKind getDiscountKind() {
        return discountKind;
    }

    public Collection<DTOOffer> getOffers() {
        return offers;
    }
}
