package SDMSystem.product;

import SDMSystemDTO.product.DTOProductInDiscount;
import SDMSystemDTO.product.DTOProductInStore;
import SDMSystemDTO.product.IDTOProductInStore;
import SDMSystemDTO.product.WayOfBuying;

public class ProductInDiscount implements IProductInStore {

    private float discountPrice;
    private ProductInStore originalProductInStore;

    public ProductInDiscount(ProductInStore originalProductInStore,float discountPrice) {
        this.originalProductInStore = originalProductInStore;
        this.discountPrice = discountPrice;
    }

    @Override
    public void increaseAmountSoldInStore(float amountSold) {
        originalProductInStore.increaseAmountSoldInStore(amountSold);
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
    public IDTOProductInStore createIDTOProductInStore() {
        return new DTOProductInDiscount(
                originalProductInStore.createDTOProductInStore(),
                discountPrice);
    }
}
