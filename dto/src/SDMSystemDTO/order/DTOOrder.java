package SDMSystemDTO.order;

import SDMSystemDTO.customer.DTOCustomer;
import SDMSystemDTO.product.IDTOProductInStore;
import SDMSystemDTO.store.DTOStore;
import javafx.util.Pair;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Map;

public class DTOOrder {
    private final LocalDate orderDate;
    private final Collection<Pair<IDTOProductInStore,Float>> productsInOrder;
    private int customerOrderedId;
    private final float productsCost;
    private final float deliveryCost;
    private final int orderSerialNumber;
    private final Collection<DTOStore> storesFromWhomTheOrderWasMade;
    private final int amountOfProducts;
    private final int amountOfProductsKinds;
    private final Map<Integer,Collection<Pair<IDTOProductInStore,Float>>> productsInOrderByStores;
    private final DTOOrder mainOrder;
    private boolean isStaticOrder;



    public DTOOrder(LocalDate orderDate,
                    Collection<Pair<IDTOProductInStore,Float>> productsInOrder,
                    float productsCost,
                    float deliveryCost,
                    int orderSerialNumber,
                    Collection<DTOStore> storesFromWhomTheOrderWasMade,
                    int amountOfProducts,
                    int amountOfProductsKinds,
                    Map<Integer,Collection<Pair<IDTOProductInStore,Float>>> productsInOrderByStores,
                    int customerOrderedId,
                    DTOOrder mainOrder,
                    boolean isStaticOrder) {
        this.orderDate = orderDate;
        this.productsInOrder = productsInOrder;
        this.productsCost = productsCost;
        this.deliveryCost = deliveryCost;
        this.orderSerialNumber = orderSerialNumber;
        this.storesFromWhomTheOrderWasMade = storesFromWhomTheOrderWasMade;
        this.amountOfProducts = amountOfProducts;
        this.amountOfProductsKinds = amountOfProductsKinds;
        this.productsInOrderByStores = productsInOrderByStores;
        this.customerOrderedId = customerOrderedId;
        this.mainOrder = mainOrder;
        this.isStaticOrder = isStaticOrder;
    }

    public Collection<DTOStore> getStoresFromWhomTheOrderWasMade() {
        return storesFromWhomTheOrderWasMade;
    }

    public DTOOrder getMainOrder() {
        return mainOrder;
    }

    public boolean isStaticOrder() {
        return isStaticOrder;
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



    @Override
    public String toString() {
        return "Order ID: " + orderSerialNumber;
    }

    public int getCustomerOrderedId(){
        return customerOrderedId;
    }


    public  Map<Integer,Collection<Pair<IDTOProductInStore,Float>>> getProductsInOrderByStores() {
        return productsInOrderByStores;
    }
}
