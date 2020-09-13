package components.makeOrder.makeDynamicOrder.storesParticipatingInDyanmicOrder;

import SDMSystem.location.LocationUtility;
import SDMSystem.system.SDMSystem;
import SDMSystemDTO.customer.DTOCustomer;
import SDMSystemDTO.product.IDTOProductInStore;
import SDMSystemDTO.store.DTOStore;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;

import java.util.Collection;
import java.util.Map;

public class SingleStoreParticipatingInDynamicStoreController {

    @FXML private GridPane productGridPane;
    @FXML private Label storeNameLabel;
    @FXML private Label pricePerUnitLabel;
    @FXML private Label storeIdLabel;
    @FXML private Label locationLabel;
    @FXML private Label ppkLabel;
    @FXML private Label numProductsKindLabel;
    @FXML private Label distanceFromCustomerLabel;
    @FXML private Label deliveryCostLabel;
    @FXML private Label totalProductsCostLabel;
    @FXML private Label isPartOfDiscountLabel11;
    @FXML private Label totalCostLabel;

    public static final String SINGLE_STORE_PARTICIPATING_IN_DYNAMIC_FORM_FXML_PATH = "/components/makeOrder/makeDynamicOrder/storesParticipatingInDyanmicOrder/singleStoreParticipatingInDynamicStore.fxml";

    public void initDetails(DTOStore storeParticipating, DTOCustomer customerOrdering, Map<Integer, Collection<Pair<IDTOProductInStore, Float>>> cheapestBasket, SDMSystem sdmSystem) {
        storeNameLabel.setText(storeParticipating.getStoreName());
        storeIdLabel.setText(String.valueOf(storeParticipating.getStoreSerialNumber()));
        locationLabel.setText(LocationUtility.locationToString(storeParticipating.getStoreLocation()));
        float distanceFromCustomer = LocationUtility.calcDistance(storeParticipating.getStoreLocation(),customerOrdering.getCustomerLocation());
        distanceFromCustomerLabel.setText(String.format("%.2f",distanceFromCustomer));
        ppkLabel.setText(String.valueOf(storeParticipating.getPpk()));
        float deliveryCost = storeParticipating.getPpk() * distanceFromCustomer;
        deliveryCostLabel.setText(String.format("%.2f",deliveryCost));
        numProductsKindLabel.setText(String.valueOf(cheapestBasket.get(storeParticipating.getStoreSerialNumber()).size()));
        float productsCost = sdmSystem.calcProductsInOrderCost(cheapestBasket.get(storeParticipating.getStoreSerialNumber()));
        totalProductsCostLabel.setText(String.format("%.2f",productsCost));
        totalCostLabel.setText(String.format("%.2f",productsCost + deliveryCost));

    }
}
