package SDMSystem.discount;

import SDMSystemDTO.discount.DTODiscount;
import SDMSystemDTO.discount.DTOOffer;
import SDMSystemDTO.discount.DiscountKind;
import SDMSystemDTO.product.WayOfBuying;
import javafx.util.Pair;

import java.util.Collection;
import java.util.LinkedList;

public class Discount {
    String discountName;
    //pair: key = product id, value: product quantity
    Pair<Integer,Double> ifYouBuyProductAndAmount;
    DiscountKind discountKind;
    Collection<Offer> offers;
    WayOfBuying productWayOfBuying;

    public Discount(String discountName,
                    Pair<Integer, Double> ifYouBuyProductAndAmount,
                    DiscountKind discountKind,
                    Collection<Offer> offers,
                    WayOfBuying productWayOfBuying) {
        this.discountName = discountName;
        this.ifYouBuyProductAndAmount = ifYouBuyProductAndAmount;
        this.discountKind = discountKind;
        this.offers = offers;
        this.productWayOfBuying = productWayOfBuying;
    }

    public String getDiscountName() {
        return discountName;
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
                productWayOfBuying);
    }

    private Collection<DTOOffer> getDTOOffer() {
        Collection<DTOOffer> dtoOffers = new LinkedList<>();
        for(Offer offer : this.offers){
            dtoOffers.add(offer.createDTOOffer());
        }

        return dtoOffers;
    }
}
