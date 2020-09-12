package SDMSystem.product;

import SDMSystem.HasSerialNumber;
import SDMSystem.store.Store;
import SDMSystemDTO.product.DTOProduct;
import SDMSystemDTO.product.WayOfBuying;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedList;

public class Product implements Serializable {

    protected static int generatedSerialNumber = 1000;
    protected final int productSerialNumber;
    protected final String productName;
    protected final WayOfBuying wayOfBuying;
    protected Collection<Store> storesSellingTheProduct = new LinkedList<>();
    protected float amountSoldInAllStores = 0;


    public Product(String productName, WayOfBuying wayOfBuying) {
        this.productSerialNumber = generatedSerialNumber++;
        this.productName = productName;
        this.wayOfBuying = wayOfBuying;
    }

    public Product(int productSerialNumber ,String productName, WayOfBuying wayOfBuying) {
        this.productSerialNumber = productSerialNumber;
        this.productName = productName;
        this.wayOfBuying = wayOfBuying;
    }

    public Product(Product newProduct) {
        this.productSerialNumber = newProduct.getSerialNumber();
        this.productName = newProduct.getProductName();
        this.wayOfBuying = newProduct.getWayOfBuying();
        this.storesSellingTheProduct = newProduct.getStoresSellingTheProduct();
        this.amountSoldInAllStores = newProduct.getAmountSoldInAllStores();
    }

    public  DTOProduct createDTOProduct() {
        return new DTOProduct(
                productSerialNumber,
                productName,
                wayOfBuying,
                amountSoldInAllStores);
    }

    public Collection<Store> getStoresSellingTheProduct() {
        return storesSellingTheProduct;
    }

    public void increaseAmountSoldInAllStores(float amount){
        amountSoldInAllStores += amount;
    }

//    public Collection<DTOStore> getDTOStoresSellingTheProduct() {
//        Collection<DTOStore> DTOStoresSellingTheProduct = new LinkedList<>();
//        for(Store store : storesSellingTheProduct){
//            DTOStore dtoStore = new DTOStore(store);
//        }
//        return storesSellingTheProduct;
//    }




    public int getSerialNumber() {
        return productSerialNumber;
    }

    public String getProductName() {
        return productName;
    }

    public WayOfBuying getWayOfBuying(){
        return wayOfBuying;
    }

//    @Override
//    public String toString() {
//        return "Product serial number: " + productSerialNumber +
//                "\nProduct name: " + productName +
//                "\nWay of buying: " + wayOfBuying;
//    }

    public int numberOfStoresSellingTheProduct(){
        return storesSellingTheProduct.size();
    }

    public float averagePriceOfProduct(){
        float overallPrice = 0;
        float avgPrice = 0;

        if(numberOfStoresSellingTheProduct() != 0) {
            for (Store store : storesSellingTheProduct) {
                ProductInStore productInStore = store.getProductInStore(productSerialNumber);
                if (productInStore != null) {
                    overallPrice += productInStore.getPrice();
                }
            }
            avgPrice = overallPrice / numberOfStoresSellingTheProduct();
        }

        return avgPrice;
    }

    public float getAmountSoldInAllStores() {
        return amountSoldInAllStores;
    }

    public void addStore(Store store){
        storesSellingTheProduct.add(store);
    }

    public void deleteStoreFromStoresSellingTheProduct(Store store) {
        storesSellingTheProduct.remove(store);
    }
}
