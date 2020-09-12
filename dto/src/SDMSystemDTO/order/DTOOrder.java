package SDMSystemDTO.order;

import SDMSystemDTO.product.DTOProductInStore;
import SDMSystemDTO.product.IDTOProductInStore;
import SDMSystemDTO.store.DTOStore;
import javafx.util.Pair;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Date;

public class DTOOrder {
    private final LocalDate orderDate;
    private final Collection<Pair<IDTOProductInStore,Float>> productsInOrder;
    //private Customer whoOrdered;
    private final float productsCost;
    private final float deliveryCost;
    private final int orderSerialNumber;
    private final Collection<DTOStore> storesFromWhomTheOrderWasMade;
    private final int amountOfProducts;
    private final int amountOfProductsKinds;


    public DTOOrder(LocalDate orderDate,
                    Collection<Pair<IDTOProductInStore,Float>> productsInOrder,
                    float productsCost,
                    float deliveryCost,
                    int orderSerialNumber,
                    Collection<DTOStore> storesFromWhomTheOrderWasMade,
                    int amountOfProducts,
                    int amountOfProductsKinds) {
        this.orderDate = orderDate;
        this.productsInOrder = productsInOrder;
        this.productsCost = productsCost;
        this.deliveryCost = deliveryCost;
        this.orderSerialNumber = orderSerialNumber;
        this.storesFromWhomTheOrderWasMade = storesFromWhomTheOrderWasMade;
        this.amountOfProducts = amountOfProducts;
        this.amountOfProductsKinds = amountOfProductsKinds;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public int getAmountOfProducts() {
        return amountOfProducts;
    }

    public float getOrderCost(){
        return deliveryCost + productsCost;
    }

    public int getAmountOfProductsKinds() {
        return amountOfProductsKinds;
    }

    public Collection<DTOStore> getStoreFromWhomTheOrderWasMade() {
        return storesFromWhomTheOrderWasMade;
    }

    public int getOrderSerialNumber() {
        return orderSerialNumber;
    }

    public Collection<Pair<IDTOProductInStore,Float>> getProductsInOrder() {
        return productsInOrder;
    }

    public float getProductsCost() {
        return productsCost;
    }

    public float getDeliveryCost() {
        return deliveryCost;
    }
}
