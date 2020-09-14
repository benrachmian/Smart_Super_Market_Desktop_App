package SDMSystem.order;

import SDMSystem.customer.Customer;
import SDMSystem.product.IProductInStore;
import SDMSystem.product.ProductInStore;
import SDMSystemDTO.order.DTOOrder;
import SDMSystemDTO.product.IDTOProductInStore;
import SDMSystemDTO.store.DTOStore;
import javafx.util.Pair;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.Map;

public class DynamicOrder extends Order {
    private Collection<StaticOrder> subOrders;

    public DynamicOrder(LocalDate orderDate,
                        Collection<Pair<IProductInStore, Float>> productsInOrder,
                        float productsCost,
                        float deliveryCost,
                        int amountOfProducts,
                        int amountOfProductsKinds,
                        Collection<StaticOrder> subOrders,
                        Customer whoOrdered, Map<Integer, Collection<Pair<IProductInStore, Float>>> shoppingCartForOrder, Map<Integer, Collection<Pair<IDTOProductInStore, Float>>> cheapestBasketDTO) {
        super(orderDate, productsInOrder, productsCost, deliveryCost, amountOfProducts, amountOfProductsKinds, whoOrdered, null,shoppingCartForOrder,cheapestBasketDTO);
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
                getAmountOfProductsKinds(),
                shoppingCartAsDTO,
                whoOrdered.getSerialNumber(),
                null,
                false);

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
