package SDMSystem.discount;

import javafx.util.Pair;

import java.util.Collection;

public class Discount {
    String discountName;
    //pair: key = product id, value: product quantity
    Pair<Integer,Double> ifYouBuyProductAndAmount;
    DiscountKind discountKind;
    Collection<Offer> offers;

    public Discount(String discountName, Pair<Integer, Double> ifYouBuyProductAndAmount, DiscountKind discountKind, Collection<Offer> offers) {
        this.discountName = discountName;
        this.ifYouBuyProductAndAmount = ifYouBuyProductAndAmount;
        this.discountKind = discountKind;
        this.offers = offers;
    }

    public Discount(String discountName, Pair<Integer, Double> ifYouBuyProductAndAmount, Collection<Offer> offers) {
        this.discountName = discountName;
        this.ifYouBuyProductAndAmount = ifYouBuyProductAndAmount;
        this.offers = offers;
        discountKind = DiscountKind.IRRELEVANT;
    }
}
