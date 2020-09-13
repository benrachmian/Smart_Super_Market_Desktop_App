package components.makeOrder.makeDynamicOrder.storesParticipatingInDyanmicOrder;

import SDMSystem.system.SDMSystem;
import SDMSystemDTO.customer.DTOCustomer;
import SDMSystemDTO.discount.DTODiscount;
import SDMSystemDTO.product.IDTOProductInStore;
import SDMSystemDTO.store.DTOStore;
import common.FxmlLoader;
import common.JavaFxHelper;
import components.details.storeDetails.discountsInStoreDetails.SingleDiscountController;
import components.makeOrder.MakeOrderMainController;
import components.makeOrder.makeDynamicOrder.MakeDynamicOrderController;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.util.Pair;

import java.util.Collection;
import java.util.Map;

public class StoresParticipatingInDynamicOrderController {

    public static final String STORES_PARTICIPATING_IN_DYNAMIC_ORDER_FORM_FXML_PATH = "/components/makeOrder/makeDynamicOrder/storesParticipatingInDyanmicOrder/storesParticipatingInDynamicOrder.fxml";
    private Map<Integer, Collection<Pair<IDTOProductInStore, Float>>> cheapestBasket;
    private SDMSystem sdmSystem;
    private GridPane singleStoreGridPane;
    private SingleStoreParticipatingInDynamicStoreController singleStoreParticipatingInDynamicStoreController;
    private DTOCustomer customerOrdering;
    private MakeDynamicOrderController makeDynamicOrderController;


    @FXML private ScrollPane storesParticipatingScrollPane;
    @FXML  private FlowPane storesParticipatingFlowPane;
    @FXML private ScrollPane singleStoreScrollPane;

    @FXML
    public void initialize(){
        JavaFxHelper.initMainScrollPane(storesParticipatingScrollPane);
        singleStoreScrollPane.fitToWidthProperty().set(true);
        singleStoreScrollPane.fitToHeightProperty().set(true);
//        initErrorsLabel();
//        continueButton.disableProperty().bind(Bindings.isEmpty(cartTable.getItems()));
//
//        amountTextField.disableProperty().bind(chooseProductComboBox.valueProperty().isNull());
//        JavaFxHelper.makeTextFieldInputOnlyFloat(amountTextField,errorInputLabel,amountInTextField);
//        addToCartButton.disableProperty().bind(
//                chooseProductComboBox.valueProperty().isNull().or(
//                        amountTextField.textProperty().isEmpty()
//                ));
//
//        initProductsToBuyTableSettings();
//        initProductsCartTableSettings();
    }


    @FXML
    private ScrollPane orderSummaryMainScrollPane;

    @FXML
    private ScrollPane storeAndProductsParticipatingScrollPane;

    @FXML
    private VBox storesInOrderSummaryVBox;

    @FXML
    void onContinue(ActionEvent event) {
        makeDynamicOrderController.ifHasDiscountMakeDiscountsForm();
    }



    public void initDetails(SDMSystem sdmSystem, Map<Integer, Collection<Pair<IDTOProductInStore, Float>>> cheapestBasket, DTOCustomer customerMakingTheOrder, MakeOrderMainController makeOrderMainController, MakeDynamicOrderController makeDynamicOrderController)
     {
        this.cheapestBasket = cheapestBasket;
        this.sdmSystem = sdmSystem;
        this.customerOrdering = customerMakingTheOrder;
        this.makeDynamicOrderController = makeDynamicOrderController;
        for(Integer storeId : cheapestBasket.keySet()){
            DTOStore storeParticipating = sdmSystem.getStoreFromStores(storeId);
            addStoreToFlowPane(storeParticipating);
        }
    }

    private void addStoreToFlowPane(DTOStore storeParticipating) {
        loadStoreParticipatingForm();
        singleStoreParticipatingInDynamicStoreController.initDetails(storeParticipating,customerOrdering,cheapestBasket,sdmSystem);
        storesParticipatingFlowPane.getChildren().add(singleStoreGridPane);

    }

    private void loadStoreParticipatingForm() {
        FxmlLoader<GridPane, SingleStoreParticipatingInDynamicStoreController> loaderStoreParticipatingForm = new FxmlLoader<>(SingleStoreParticipatingInDynamicStoreController.SINGLE_STORE_PARTICIPATING_IN_DYNAMIC_FORM_FXML_PATH);
        singleStoreGridPane = loaderStoreParticipatingForm.getFormBasePane();
        singleStoreParticipatingInDynamicStoreController = loaderStoreParticipatingForm.getFormController();
    }
}
