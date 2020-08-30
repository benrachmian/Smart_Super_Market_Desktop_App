package SDMSystemDTO.store;


import SDMSystemDTO.product.DTOProductInStore;

import java.awt.*;
import java.util.Map;

public class DTOStore {
    private final Map<Integer, DTOProductInStore> productsInStore;
    private final Point storeLocation;
    private final float ppk;
    private final int storeSerialNumber;
    private final String storeName;
    //private Collection<DTOOrder> ordersFromStore;
    //private Collection<DTOFeedback> storeFeedbacks;
    private final float totalProfitFromDelivery;

    @Override
    public String toString() {
        return storeName;
    }

    public DTOStore(Map<Integer, DTOProductInStore> productsInStore,
                    Point storeLocation,
                    float ppk,
                    int storeSerialNumber,
                    String storeName,
                    //Collection<DTOFeedback> storeFeedbacks,
                    float totalProfitFromDelivery) {
        this.productsInStore = productsInStore;
        this.storeLocation = storeLocation;
        this.ppk = ppk;
        this.storeSerialNumber = storeSerialNumber;
        this.storeName = storeName;
       // this.ordersFromStore = ordersFromStore;
        //this.storeFeedbacks = storeFeedbacks;
        this.totalProfitFromDelivery = totalProfitFromDelivery;
    }

    public DTOProductInStore getProductFromStore(int productSerialNumber){
        return productsInStore.get(productSerialNumber);
    }


    public Map<Integer, DTOProductInStore> getProductsInStore() {
        return productsInStore;
    }

    public Point getStoreLocation() {
        return storeLocation;
    }

    public float getPpk() {
        return ppk;
    }

    public int getStoreSerialNumber() {
        return storeSerialNumber;
    }

    public String getStoreName() {
        return storeName;
    }

//    public Collection<DTOOrder> getOrdersFromStore() {
//        return ordersFromStore;
//    }

//    public Collection<DTOFeedback> getStoreFeedbacks() {
//        return storeFeedbacks;
//    }

    public float getTotalProfitFromDelivery() {
        return totalProfitFromDelivery;
    }


//    public DTOProductInStore getProductInStore(int chosenProductSerialNumber) {
//        DTOProductInStore askedProduct = productsInStore.get(chosenProductSerialNumber);
//        if(askedProduct == null){
//            throw new ExistenceException(false,chosenProductSerialNumber,"Product","Store");
//        }
//        return askedProduct;
//    }
}
