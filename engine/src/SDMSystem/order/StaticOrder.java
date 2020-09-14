package SDMSystem.order;

import SDMSystem.customer.Customer;
import SDMSystem.product.IProductInStore;
import SDMSystem.store.Store;
import SDMSystemDTO.order.DTOOrder;
import SDMSystemDTO.product.IDTOProductInStore;
import SDMSystemDTO.store.DTOStore;
import javafx.util.Pair;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class StaticOrder extends Order {
    private final Store storeFromWhomTheOrderWasMade;

    public StaticOrder(LocalDate orderDate,
                       Collection<Pair<IProductInStore, Float>> productsInOrder,
                       float productsCost,
                       float deliveryCost,
                       int amountOfProducts,
                       int amountOfProductsKinds,
                       Store storeFromWhomTheOrderWasMade,
                       Customer whoOrdered,
                       Order mainOrder,
                       Map<Integer, Collection<Pair<IProductInStore, Float>>> shoppingCart, Map<Integer, Collection<Pair<IDTOProductInStore, Float>>> shoppingCartAsDto) {
        super(orderDate, productsInOrder, productsCost, deliveryCost, amountOfProducts, amountOfProductsKinds, whoOrdered, mainOrder,shoppingCart,shoppingCartAsDto);
        this.storeFromWhomTheOrderWasMade = storeFromWhomTheOrderWasMade;
    }

    @Override
    public DTOOrder createDTOOrderFromOrder() {
        Collection<DTOStore> storesFromWhomTheOrderWasMade = new LinkedList();
        storesFromWhomTheOrderWasMade.add(storeFromWhomTheOrderWasMade.createDTOStore());
        DTOOrder mainOrderAsDTO = null;
        if(mainOrder != null){
            mainOrderAsDTO = mainOrder.createDTOOrderFromOrder();
        }

        return new DTOOrder(getOrderDate(),
                getDTOProductsInOrder(),
                getProductsCost(),
                getDeliveryCost(),
                getSerialNumber(),
                storesFromWhomTheOrderWasMade,
                getAmountOfProducts(),
                getAmountOfProductsKinds(),
                shoppingCartAsDTO,
                whoOrdered.getSerialNumber(),
                mainOrderAsDTO,
                true);
    }

    public Store getStoreFromWhomTheOrderWasMade() {
        return storeFromWhomTheOrderWasMade;
    }
}

