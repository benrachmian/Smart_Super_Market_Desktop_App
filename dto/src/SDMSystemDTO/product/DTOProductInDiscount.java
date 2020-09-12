package SDMSystemDTO.product;

public class DTOProductInDiscount implements IDTOProductInStore{

    private float discountPrice;
    private DTOProductInStore originalProductInStore;

    public DTOProductInDiscount(DTOProductInStore originalProductInStore,float discountPrice) {
        this.originalProductInStore = originalProductInStore;
        this.discountPrice = discountPrice;
    }

    @Override
    public float getPrice() {
        return discountPrice;
    }

    @Override
    public int getSerialNumber() {
        return originalProductInStore.getSerialNumber();
    }

    @Override
    public WayOfBuying getWayOfBuying() {
        return originalProductInStore.getWayOfBuying();
    }

    @Override
    public int getProductSerialNumber() {
        return originalProductInStore.getProductSerialNumber();
    }

    @Override
    public String getProductName() {
        return originalProductInStore.productName;
    }

    @Override
    public int getStoreTheProductBelongsID() {
        return originalProductInStore.getStoreTheProductBelongsID();
    }
}
