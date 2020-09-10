package components.makeOrder.discountsInOrder;

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

public class DiscountsInOrderController {

    private SimpleFloatProperty deliveryCost;
    private SimpleFloatProperty productsCost;
    private Collection<DTODiscount> discounts;
    private Collection<Pair<DTOProduct,Float>> shoppingCart;
    private SDMSystem sdmSystem;
    private VBox singleDiscountInOrderVBox;
    private SingleDiscountInOrderController singleDiscountInOrderController;
    private SimpleBooleanProperty discountWithChooseProduct;



    @FXML private ScrollPane discountsInOrderScrollPain;
    @FXML private Button continueButton;
    @FXML private Button addToCartButton1;
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


    public DiscountsInOrderController() {
        discountWithChooseProduct = new SimpleBooleanProperty(false);
    }

    @FXML
    public void initialize(){
        initDiscountsInOrderScrollPainSettings();
        initProductsCartTableSettings();
        chooseProductComboBox.disableProperty().bind(discountWithChooseProduct.not());

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

    }

    private void loadSingleDiscountForm() {
        FxmlLoader<VBox,SingleDiscountInOrderController> loaderSingleDiscountForm = new FxmlLoader<>(SingleDiscountInOrderController.SINGLE_DISCOUNT_IN_ORDER_FORM_FXML_PATH);
        singleDiscountInOrderVBox = loaderSingleDiscountForm.getFormBasePane();
        singleDiscountInOrderController = loaderSingleDiscountForm.getFormController();
    }

    @FXML
    void onAddToCart(ActionEvent event) {
        DTODiscount discountSelected = selectDiscountComboBox.getValue();
        if(discountSelected.getDiscountKind() == DiscountKind.ONE_OF){
            DTOOffer selectedProduct = chooseProductComboBox.getValue();
            cartTable.getItems().add(
                    new ProductInTable(
                            selectedProduct.getProductName(),
                            selectedProduct.getProductSerialNumber(),
                            selectedProduct.
                    ))
        }
    }

    @FXML
    void onClickCancel(ActionEvent event) {

    }

    @FXML
    void onClickContinue(ActionEvent event) {

    }

    @FXML
    void onSelectDiscount(ActionEvent event) {
        DTODiscount discountchosen = selectDiscountComboBox.getValue();
        if(discountchosen.getDiscountKind() == DiscountKind.ONE_OF){
            discountWithChooseProduct.set(true);
            chooseProductComboBox.getItems().clear();
            chooseProductComboBox.getItems().addAll(discountchosen.getOffers());
        }
        else{
            discountWithChooseProduct.set(false);
        }

    }

    @FXML
    void onSelectProduct(ActionEvent event) {
    }

    public void updateItemsInCartTable(ObservableList<ProductInTable> items) {
        cartTable.getItems().addAll(items);
    }
}
