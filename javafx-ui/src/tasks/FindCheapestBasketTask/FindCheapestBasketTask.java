package tasks.FindCheapestBasketTask;

import SDMSystem.system.SDMSystem;
import SDMSystemDTO.customer.DTOCustomer;
import SDMSystemDTO.product.DTOProduct;
import SDMSystemDTO.product.IDTOProductInStore;
import SDMSystemDTO.store.DTOStore;
import components.main.SDMMainControllers;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.concurrent.Task;
import javafx.scene.control.Alert;
import javafx.util.Pair;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.Map;

public class FindCheapestBasketTask extends Task<Boolean> {

    private final SDMSystem sdmSystem;
    private final SimpleBooleanProperty taskDone;
    private final Map<Integer, Collection<Pair<IDTOProductInStore, Float>>> cheapestBasket;
    private final Collection<Pair<DTOProduct, Float>> productsInOrder;
    private final SimpleFloatProperty totalProductsCost;
    private final SimpleFloatProperty totalDeliveryCost;
    private final DTOCustomer customerMakingTheOrder;

    public FindCheapestBasketTask(SDMSystem sdmSystem,
                                  Map<Integer, Collection<Pair<IDTOProductInStore, Float>>> cheapestBasket,
                                  Collection<Pair<DTOProduct, Float>> productsInOrder,
                                  SimpleFloatProperty totalProductsCost,
                                  SimpleFloatProperty totalDeliveryCost,
                                  DTOCustomer customerMakingTheOrder,
                                  SimpleBooleanProperty done) {
        this.sdmSystem = sdmSystem;
        this.cheapestBasket = cheapestBasket;
        this.productsInOrder = productsInOrder;
        this.totalProductsCost = totalProductsCost;
        this.totalDeliveryCost = totalDeliveryCost;
        this.customerMakingTheOrder = customerMakingTheOrder;
        taskDone = done;
    }

    @Override
    protected Boolean call() throws Exception {
        try {
            float deliveryCost;
            float costOfProductsInStore;
            double i = 1;

            updateMessage("Start finding cheapest basket...");
            updateProgress(0, 1);
            sdmSystem.makeCheapestBasket(cheapestBasket,productsInOrder);

            for(Pair<DTOProduct, Float> product : productsInOrder){
                updateMessage("Finding the cheapest " + product.getKey().getProductName());
                updateProgress(i / productsInOrder.size(), 1);
                i++;
                Thread.sleep(300);
            }

            updateMessage("Done!");
            updateProgress(1, 1);
            taskDone.set(true);
            return true;

        } catch (RuntimeException e) {
            updateMessage("Error!");
            return false;
        }
    }
}
