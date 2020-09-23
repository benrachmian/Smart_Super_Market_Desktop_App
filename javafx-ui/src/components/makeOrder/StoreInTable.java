package components.makeOrder;

import javafx.beans.property.SimpleFloatProperty;

import java.awt.*;



public class StoreInTable {
    private final String storeName;
    private final int storeId;
    private final Point storeLocation;
    private final float storePPK;
    private final SimpleFloatProperty deliveryCost;
    private float newProductPrice;


    public StoreInTable(String storeName, int storeId, Point storeLocation, float storePPK) {
        this.storeName = storeName;
        this.storeId = storeId;
        this.storeLocation = storeLocation;
        this.storePPK = storePPK;
        this.deliveryCost = new SimpleFloatProperty();
    }

    //for add new product action
    public StoreInTable(String storeName, int storeId, Point storeLocation, float storePPK, float newProductPrice) {
        this.storeName = storeName;
        this.storeId = storeId;
        this.storeLocation = storeLocation;
        this.storePPK = storePPK;
        this.deliveryCost = new SimpleFloatProperty();
        this.newProductPrice = newProductPrice;
    }

    public String getStoreName() {
        return storeName;
    }

    public int getStoreId() {
        return storeId;
    }

    public float getNewProductPrice() {
        return newProductPrice;
    }

    public String getStoreLocation() {
        return "X = " + storeLocation.x + ", Y = " + storeLocation.y;
    }

    public Point getStoreLocationInPoint() {
        return storeLocation;
    }

    public float getStorePPK() {
        return storePPK;
    }

    public float getDeliveryCost() {
        return deliveryCost.get();
    }

    public SimpleFloatProperty deliveryCostProperty() {
        return deliveryCost;
    }

    public void setDeliveryCost(float deliveryCost) {
        this.deliveryCost.set(deliveryCost);
    }
}

