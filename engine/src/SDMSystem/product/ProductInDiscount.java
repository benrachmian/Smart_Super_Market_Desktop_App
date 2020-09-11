package SDMSystem.product;

import SDMSystem.store.Store;

public class ProductInDiscount extends ProductInStore {

    private float discountPrice;

    public ProductInDiscount(Product newProduct, float price, Store storeTheProductBelongs, float discountPrice) {
        super(newProduct, price, storeTheProductBelongs);
        this.discountPrice = discountPrice;
    }

    @Override
    public float getPrice() {
        return discountPrice;
    }
}
