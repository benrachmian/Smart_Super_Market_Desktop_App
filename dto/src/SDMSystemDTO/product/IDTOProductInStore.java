package SDMSystemDTO.product;

public interface IDTOProductInStore {
    float getPrice();
    int getSerialNumber();
    WayOfBuying getWayOfBuying();
    int getProductSerialNumber();
    String getProductName();
    int getStoreTheProductBelongsID();
}
