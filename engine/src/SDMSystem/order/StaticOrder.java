package SDMSystem.order;

import SDMSystem.customer.Customer;
import SDMSystem.product.IProductInStore;
import SDMSystem.store.Store;
import SDMSystemDTO.order.DTOOrder;
import SDMSystemDTO.store.DTOStore;
import javafx.util.Pair;

import java.time.LocalDate;
import java.util.Collection;
import java.util.LinkedList;

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
                       Order mainOrder) {
        super(orderDate, productsInOrder, productsCost, deliveryCost, amountOfProducts, amountOfProductsKinds, whoOrdered, mainOrder);
        this.storeFromWhomTheOrderWasMade = storeFromWhomTheOrderWasMade;
    }

    @Override
    public DTOOrder createDTOOrderFromOrder() {
        Collection<DTOStore> storesFromWhomTheOrderWasMade = new LinkedList();
        storesFromWhomTheOrderWasMade.add(storeFromWhomTheOrderWasMade.createDTOStore());

        return new DTOOrder(getOrderDate(),
                getDTOProductsInOrder(),
                getProductsCost(),
                getDeliveryCost(),
                getSerialNumber(),
                storesFromWhomTheOrderWasMade,
                getAmountOfProducts(),
                getAmountOfProductsKinds());
    }

    public Store getStoreFromWhomTheOrderWasMade() {
        return storeFromWhomTheOrderWasMade;
    }
}

