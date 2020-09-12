package SDMSystem.product;

import SDMSystemDTO.product.IDTOProductInStore;
import SDMSystemDTO.product.WayOfBuying;

public interface IProductInStore {
    void increaseAmountSoldInStore(float amountSold);
    float getPrice();
    int getSerialNumber();
    WayOfBuying getWayOfBuying();
    IDTOProductInStore createIDTOProductInStore();
}
