package SDMSystemDTO.discount;

import SDMSystemDTO.product.WayOfBuying;
import javafx.util.Pair;

import java.util.Collection;

public class DTODiscount {
    private String discountName;
    //pair: key = product id, value: product quantity
    private Pair<Integer,Double> ifYouBuyProductAndAmount;
    private DiscountKind discountKind;
    private Collection<DTOOffer> offers;
    private WayOfBuying productWayOfBuying;
    private String productNameToBuyForDiscount;
    private int storeWithThisDiscountSerialNumber;



    public DTODiscount(String discountName,
                       Pair<Integer, Double> ifYouBuyProductAndAmount,
                       DiscountKind discountKind,
                       Collection<DTOOffer> offers,
                       WayOfBuying productWayOfBuying,
                       String productNameToBuyForDiscount,
                       int storeWithThisDiscountSerialNumber) {
        this.discountName = discountName;
        this.ifYouBuyProductAndAmount = ifYouBuyProductAndAmount;
        this.discountKind = discountKind;
        this.offers = offers;
        this.productWayOfBuying = productWayOfBuying;
        this.productNameToBuyForDiscount = productNameToBuyForDiscount;
        this.storeWithThisDiscountSerialNumber = storeWithThisDiscountSerialNumber;
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

    public int getStoreWithThisDiscountSerialNumber() {
        return storeWithThisDiscountSerialNumber;
    }

    @Override
    public String toString() {
        return discountName;
    }
}
