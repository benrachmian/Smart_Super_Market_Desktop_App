package components.makeOrder.discountsInOrder;

import SDMSystem.discount.Offer;
import SDMSystem.system.SDMSystem;
import SDMSystemDTO.discount.DTODiscount;
import SDMSystemDTO.discount.DTOOffer;
import SDMSystemDTO.discount.DiscountKind;
import SDMSystemDTO.product.DTOProduct;
import common.FxmlLoader;
import components.makeOrder.makeStaticOrder.ProductInTable;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.util.Pair;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class DiscountsInOrderController {

    private SimpleFloatProperty deliveryCost;
    private SimpleFloatProperty productsCost;
    private Collection<DTODiscount> discounts;
    private Collection<Pair<DTOProduct,Float>> shoppingCart;
    private SDMSystem sdmSystem;
    private VBox singleDiscountInOrderVBox;
    private SingleDiscountInOrderController singleDiscountInOrderController;
    private SimpleBooleanProperty discountWithChooseProduct;
    private SimpleBooleanProperty discountWasSelected;
    private SimpleBooleanProperty productInDiscountWasSelected;
    private SimpleIntegerProperty currentDiscountAmountToImplement;
    private SimpleFloatProperty currentDiscountCost;
    private SimpleBooleanProperty atLeastOneDiscountWasImplemented;
    private Map<String,SingleDiscountInOrderController> singleDiscountsInOrderControllers = new HashMap<>();



    @FXML private ScrollPane discountsInOrderScrollPain;
    @FXML private Button continueButton;
    @FXML private ComboBox<DTOOffer> chooseProductComboBox;
    @FXML private Label productCostLabel;
    @FXML private Button addToCartButton;
    @FXML private TableView<ProductInTable> cartTable;
    @FXML private TableColumn<ProductInTable, String> productNameCartTableCol;
    @FXML private TableColumn<ProductInTable, Integer> idCartTableCol;
    @FXML private TableColumn<ProductInTable, String> wayOfBuyingCartTableCol;
    @FXML private TableColumn<ProductInTable, Float> amountCartTableCol;
    @FXML private Label deliveryCostLabel;
    @FXML private Label productsCostLabel;
    @FXML private Label totalOrderCostLabel;
    @FXML private Pane cartTablePane;
    @FXML private FlowPane discountsFlowPane;
    @FXML private ScrollPane discountsScrollPain;
    @FXML private ComboBox<DTODiscount> selectDiscountComboBox;
    @FXML private Button continueWithoutButton;


    public DiscountsInOrderController() {
        discountWithChooseProduct = new SimpleBooleanProperty(false);
        discountWasSelected = new SimpleBooleanProperty(false);
        productInDiscountWasSelected = new SimpleBooleanProperty(false);
        currentDiscountAmountToImplement = new SimpleIntegerProperty(-1);
        currentDiscountCost = new SimpleFloatProperty();
        atLeastOneDiscountWasImplemented = new SimpleBooleanProperty(false);
    }

    @FXML
    public void initialize(){
        initDiscountsInOrderScrollPainSettings();
        initProductsCartTableSettings();
        chooseProductComboBox.disableProperty().bind(discountWithChooseProduct.not());
        addToCartButton.disableProperty().bind(
                discountWasSelected.not().
                        or(
                        discountWithChooseProduct.and(productInDiscountWasSelected.not())).
                        or(
                        discountWasSelected.and(currentDiscountAmountToImplement.isEqualTo(0))));
        productCostLabel.textProperty().bind(currentDiscountCost.asString());
        continueWithoutButton.disableProperty().bind(atLeastOneDiscountWasImplemented);


//        initContinueButtonBinding();
//        orderTypeComboBox.getItems().add(MakeOrderMainController.OrderType.STATIC_ORDER);
//        orderTypeComboBox.getItems().add(MakeOrderMainController.OrderType.DYNAMIC_ORDER);
//        storesScrollPane.visibleProperty().bind(orderTypeComboBox.valueProperty().isEqualTo(MakeOrderMainController.OrderType.STATIC_ORDER));
//        initTableSettings();
    }

    public void setSdmSystem(SDMSystem sdmSystem) {
        this.sdmSystem = sdmSystem;
    }

    private void initProductsCartTableSettings() {
        productNameCartTableCol.setCellValueFactory(new PropertyValueFactory<>("productName"));
        idCartTableCol.setCellValueFactory(new PropertyValueFactory<>("productId"));
        wayOfBuyingCartTableCol.setCellValueFactory(new PropertyValueFactory<>("wayOfBuying"));
        amountCartTableCol.setCellValueFactory(new PropertyValueFactory<>("amount"));
        //make the cell to present two digits after the dot
        amountCartTableCol.setCellFactory(tc -> new TableCell<ProductInTable, Float>() {
            @Override
            protected void updateItem(Float amountCartTableCol, boolean empty) {
                super.updateItem(amountCartTableCol, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText(String.format("%.2f", amountCartTableCol));
                }}});
    }

    private void initDiscountsInOrderScrollPainSettings() {
        discountsInOrderScrollPain.setMinWidth(250);
        discountsInOrderScrollPain.setMinHeight(250);
        discountsInOrderScrollPain.setMaxHeight(1000);
        discountsInOrderScrollPain.setMaxWidth(4000);
        discountsInOrderScrollPain.fitToWidthProperty().set(true);
        discountsInOrderScrollPain.fitToHeightProperty().set(true);
    }

    public void initDetails(SimpleFloatProperty deliveryCost,
                            SimpleFloatProperty productsCost,
                            Collection<Pair<DTOProduct, Float>> shoppingCart,
                            Collection<DTODiscount> storeDiscounts) {
        this.deliveryCost = deliveryCost;
        this.productsCost = productsCost;
        this.shoppingCart = shoppingCart;
        this.discounts = storeDiscounts;
        updateDiscountsToForm();

        deliveryCostLabel.textProperty().bind(deliveryCost.asString());
        productsCostLabel.textProperty().bind(productsCost.asString());
        totalOrderCostLabel.textProperty().bind(Bindings.add(deliveryCost,productsCost).asString());
    }

    private void updateDiscountsToForm() {
        //pair: key = discount, value = amount deserved
        Collection<Pair<DTODiscount,Integer>> discountsForProduct;
        for(Pair<DTOProduct,Float> product : shoppingCart){
            discountsForProduct = sdmSystem.getDiscountsForProductFromDiscountsCollection(product,discounts);
            for(Pair<DTODiscount,Integer> discountForProduct : discountsForProduct){
                createSingleDiscountFormAndAddIt(discountForProduct);
                selectDiscountComboBox.getItems().add(discountForProduct.getKey());
            }


        }
    }

    private void createSingleDiscountFormAndAddIt(Pair<DTODiscount, Integer> discount) {
        loadSingleDiscountForm();
        singleDiscountInOrderController.setDiscount(discount.getKey());
        singleDiscountInOrderController.initDetails(discount.getValue());
        discountsFlowPane.getChildren().add(singleDiscountInOrderVBox);
        singleDiscountsInOrderControllers.put(discount.getKey().getDiscountName(),singleDiscountInOrderController);

    }

    private void loadSingleDiscountForm() {
        FxmlLoader<VBox,SingleDiscountInOrderController> loaderSingleDiscountForm = new FxmlLoader<>(SingleDiscountInOrderController.SINGLE_DISCOUNT_IN_ORDER_FORM_FXML_PATH);
        singleDiscountInOrderVBox = loaderSingleDiscountForm.getFormBasePane();
        singleDiscountInOrderController = loaderSingleDiscountForm.getFormController();
    }

    @FXML
    void onAddToCart(ActionEvent event) {
        DTODiscount discountSelected = selectDiscountComboBox.getValue();
        atLeastOneDiscountWasImplemented.set(true);
        //SimpleIntegerProperty amountLeftToImplement =  singleDiscountsInOrderControllers.get(discountSelected.getDiscountName()).amountLeftToImplementProperty();
        if(discountSelected.getDiscountKind() == DiscountKind.ONE_OF){
            DTOOffer selectedProduct = chooseProductComboBox.getValue();
            //add to shopping cart
            addDiscountProductToCart(selectedProduct);
        }
        //else if(discountSelected.getDiscountKind() == DiscountKind.ALL_OR_NOTHING){
        else {
            for (DTOOffer productInDiscount : discountSelected.getOffers()) {
                addDiscountProductToCart(productInDiscount);
            }
        }
        selectDiscountComboBox.getSelectionModel().clearSelection();
        discountWasSelected.set(false);
        chooseProductComboBox.getSelectionModel().clearSelection();
        productInDiscountWasSelected.set(false);
        //amountLeftToImplement.set(amountLeftToImplement.getValue() - 1);
        subAmountInAllDiscountsContainTheProduct(discountSelected);
        ifAmountZeroRemoveFromComboBox(discountSelected);
        currentDiscountCost.set(0);
    }

    private void ifAmountZeroRemoveFromComboBox(DTODiscount discountSelected) {
        if(singleDiscountsInOrderControllers.get(discountSelected.getDiscountName()).amountLeftToImplementProperty().get() == 0){
            selectDiscountComboBox.getItems().remove(discountSelected);
        }
    }

    private void subAmountInAllDiscountsContainTheProduct(DTODiscount discountSelected) {
        int triggerDiscountProductSerialNumber = discountSelected.getIfYouBuyProductAndAmount().getKey();
        for(DTODiscount discount : discounts){
            if(discount.getIfYouBuyProductAndAmount().getKey().intValue() == triggerDiscountProductSerialNumber){
                SimpleIntegerProperty discountAmountToImplement =  singleDiscountsInOrderControllers.get(discount.getDiscountName()).amountLeftToImplementProperty();
                discountAmountToImplement.set(discountAmountToImplement.getValue() - 1);
            }
        }
    }

    private void addDiscountProductToCart(DTOOffer selectedProduct) {
        cartTable.getItems().add(
                new ProductInTable(
                        selectedProduct.getProductName() + " (Discount)",
                        selectedProduct.getProductSerialNumber(),
                        sdmSystem.getProductWayOfBuying(selectedProduct.getProductSerialNumber()),
                        null,
                        (float) selectedProduct.getProductQuantity())
                );
        productsCost.set(productsCost.get() + (float)(selectedProduct.getPricePerUnit() * selectedProduct.getProductQuantity()));
    }

    @FXML
    void onClickCancel(ActionEvent event) {

    }

    @FXML
    void onClickContinue(ActionEvent event) {

    }

    @FXML
    void onSelectDiscount(ActionEvent event) {
        if(selectDiscountComboBox.getValue() != null) {
            SimpleIntegerProperty amountLeftToImplement = singleDiscountsInOrderControllers.get(selectDiscountComboBox.getValue().getDiscountName()).amountLeftToImplementProperty();
            currentDiscountAmountToImplement.bind(amountLeftToImplement);
            discountWasSelected.set(true);
            DTODiscount discountChosen = selectDiscountComboBox.getValue();
            if (discountChosen.getDiscountKind() == DiscountKind.ONE_OF) {
                currentDiscountCost.set(0);
                discountWithChooseProduct.set(true);
                chooseProductComboBox.getItems().clear();
                chooseProductComboBox.getItems().addAll(discountChosen.getOffers());
            } else {
                discountWithChooseProduct.set(false);
                calcDiscountCost(discountChosen);
            }
        }
        else{
            discountWasSelected.set(false);
            discountWithChooseProduct.set(false);
        }

    }

    private void calcDiscountCost(DTODiscount discountChosen) {
        float totalCost = 0;
        for(DTOOffer productInDiscount : discountChosen.getOffers()){
            totalCost += (productInDiscount.getPricePerUnit() * productInDiscount.getProductQuantity());
        }

        currentDiscountCost.set(totalCost);
    }

    @FXML
    void onSelectProduct(ActionEvent event) {
        if(chooseProductComboBox.getValue() != null) {
            productInDiscountWasSelected.set(true);
            currentDiscountCost.set((float)(chooseProductComboBox.getValue().getPricePerUnit() * chooseProductComboBox.getValue().getProductQuantity()));
        }
        else {
            productInDiscountWasSelected.set(false);
            chooseProductComboBox.getSelectionModel().clearSelection();
        }

    }

    public void updateItemsInCartTable(ObservableList<ProductInTable> items) {
        cartTable.getItems().addAll(items);
    }
}
