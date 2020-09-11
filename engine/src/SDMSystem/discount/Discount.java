package SDMSystem.discount;

import SDMSystem.product.Product;
import SDMSystem.store.Store;
import SDMSystemDTO.discount.DTODiscount;
import SDMSystemDTO.discount.DTOOffer;
import SDMSystemDTO.discount.DiscountKind;
import SDMSystemDTO.product.WayOfBuying;
import javafx.util.Pair;

import java.util.Collection;
import java.util.LinkedList;

public class Discount {
  private String discountName;
   //pair: key = product id, value: product quantity
  private Pair<Integer,Double> ifYouBuyProductAndAmount;
  private String productNameToBuyForDiscount;
  private DiscountKind discountKind;
  private Collection<Offer> offers;
  private WayOfBuying productWayOfBuying;
  private int storeWithThisDiscountSerialNumber;

    public Discount(String discountName,
                    Pair<Integer, Double> ifYouBuyProductAndAmount,
                    DiscountKind discountKind,
                    Collection<Offer> offers,
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

    public String getDiscountName() {
        return discountName;
    }

    public int getStoreWithThisDiscountSerialNumber() {
        return storeWithThisDiscountSerialNumber;
    }

    public Pair<Integer, Double> getIfYouBuyProductAndAmount() {
        return ifYouBuyProductAndAmount;
    }

    public DiscountKind getDiscountKind() {
        return discountKind;
    }

    public Collection<Offer> getOffers() {
        return offers;
    }

    public WayOfBuying getProductWayOfBuying() {
        return productWayOfBuying;
    }

    public Discount(String discountName, Pair<Integer, Double> ifYouBuyProductAndAmount, Collection<Offer> offers) {
        this.discountName = discountName;
        this.ifYouBuyProductAndAmount = ifYouBuyProductAndAmount;
        this.offers = offers;
        discountKind = DiscountKind.IRRELEVANT;
    }

    public DTODiscount createDTODiscount() {
        return new DTODiscount(
                discountName,
                ifYouBuyProductAndAmount,
                discountKind,
                getDTOOffer(),
                productWayOfBuying,
                productNameToBuyForDiscount,
                storeWithThisDiscountSerialNumber);
    }

    private Collection<DTOOffer> getDTOOffer() {
        Collection<DTOOffer> dtoOffers = new LinkedList<>();
        for(Offer offer : this.offers){
            dtoOffers.add(offer.createDTOOffer());
        }

        return dtoOffers;
    }
}
