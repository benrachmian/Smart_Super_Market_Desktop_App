package SDMSystemDTO.product;

public class DTOProductInDiscount extends  DTOProductInStore{
    private float discountPrice;

    public DTOProductInDiscount(int productSerialNumber,
                                String productName,
                                WayOfBuying wayOfBuying,
                                float amountSoldInAllStores,
                                float price,
                                float amountSoldInStore,
                                int storeTheProductBelongsID,
                                float discountPrice) {
        super(productSerialNumber,
                productName,
                wayOfBuying,
                amountSoldInAllStores,
                price,
                amountSoldInStore,
                storeTheProductBelongsID);
        this.discountPrice = discountPrice;
    }

    @Override
    public float getPrice() {
        return discountPrice;
    }
}
