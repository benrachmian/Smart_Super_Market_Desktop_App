package components.makeOrder;

import javafx.beans.property.SimpleFloatProperty;

import java.awt.*;



public class StoreInTable {
    private final String storeName;
    private final int storeId;
    private final Point storeLocation;
    private final float storePPK;
    private final SimpleFloatProperty deliveryCost;


    public StoreInTable(String storeName, int storeId, Point storeLocation, float storePPK) {
        this.storeName = storeName;
        this.storeId = storeId;
        this.storeLocation = storeLocation;
        this.storePPK = storePPK;
        this.deliveryCost = new SimpleFloatProperty();
    }

    public String getStoreName() {
        return storeName;
    }

    public int getStoreId() {
        return storeId;
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

