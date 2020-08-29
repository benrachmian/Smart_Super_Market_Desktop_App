package SDMSystem.product;

import SDMSystem.store.Store;
import SDMSystemDTO.product.DTOProductInStore;
import SDMSystemDTO.product.WayOfBuying;

public class ProductInStore extends Product {
    private float price;
    private float amountSoldInStore;
    Store storeTheProductBelongs;


    public ProductInStore(Product newProduct, float price, Store storeTheProductBelongs) {
        super(newProduct);
        this.price = price;
        this.storeTheProductBelongs = storeTheProductBelongs;
    }

    public void increaseAmountSoldInStore(float amountSold){
        amountSoldInStore += amountSold;
    }

    public Store getStoreTheProductBelongs() {
        return storeTheProductBelongs;
    }

    public float getPrice() {
        return price;
    }

    public float getAmountSoldInStore() {
        return amountSoldInStore;
    }

    public DTOProductInStore createDTOProductInStore() {
        return new DTOProductInStore(
                productSerialNumber,
                productName,
                wayOfBuying,
                amountSoldInAllStores,
                getPrice(),
                amountSoldInStore,
                storeTheProductBelongs.getSerialNumber());
    }

    public void setPrice(float newPrice) {
        price = newPrice;
    }

//    @Override
//    public String toString() {
//        return super.toString() +
//                "\nPrice: " + price +
//                "\nAmount sold: " + amountSoldInStore + "\n";
//    }
}
