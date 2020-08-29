package SDMSystem.order;

import SDMSystem.product.ProductInStore;
import SDMSystemDTO.order.DTOOrder;
import SDMSystemDTO.store.DTOStore;
import javafx.util.Pair;

import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;

public class DynamicOrder extends Order {
    private Collection<StaticOrder> subOrders;

    public DynamicOrder(Date orderDate,
                        Collection<Pair<Float, ProductInStore>> productsInOrder,
                        float productsCost,
                        float deliveryCost,
                        int amountOfProducts,
                        int amountOfProductsKinds,
                        Collection<StaticOrder> subOrders) {
        super(orderDate, productsInOrder, productsCost, deliveryCost, amountOfProducts, amountOfProductsKinds);
        this.subOrders = subOrders;
    }

    @Override
    public DTOOrder createDTOOrderFromOrder() {

        return new DTOOrder(getOrderDate(),
                getDTOProductsInOrder(),
                getProductsCost(),
                getDeliveryCost(),
                getSerialNumber(),
                createDTOStoresFromWhomTheOrderWasMade(),
                getAmountOfProducts(),
                getAmountOfProductsKinds());
    }

    private Collection<DTOStore> createDTOStoresFromWhomTheOrderWasMade() {
        Collection<DTOStore> storesFromWhomTheOrderWasMade = new LinkedList<>();
        for(StaticOrder order : subOrders){
            storesFromWhomTheOrderWasMade.add(order.getStoreFromWhomTheOrderWasMade().createDTOStore());
        }

        return storesFromWhomTheOrderWasMade;
    }

    public Collection<StaticOrder> getSubOrders() {
        return subOrders;
    }
}
