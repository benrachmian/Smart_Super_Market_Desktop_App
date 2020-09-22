package components.main.addNewProduct;

import SDMSystem.system.SDMSystem;
import SDMSystemDTO.product.WayOfBuying;
import common.JavaFxHelper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;

public class AddNewProductController {

    public static final String NEW_PRODUCT_FXML_PATH = "/components/main/addNewProduct/addNewProduct.fxml";
    private static final int LIMIT_ID = 8;

    private SimpleIntegerProperty productId;
    private String productName;
    private WayOfBuying productWayOfBuying;
    private SimpleBooleanProperty limitCharsError;
    private SimpleBooleanProperty notIntError;
    private SimpleBooleanProperty availableId;
    private SimpleBooleanProperty validProductName;
    private SimpleBooleanProperty validWayOfBuying;


    @FXML private ScrollPane makeOrderMainScrollPain;
    @FXML private TextField productIdTextField;
    @FXML private Button continueButton;
    @FXML private TextField productNameTextField;
    @FXML private HBox errorHBox;
    @FXML private Label errorMsgLabel;
    @FXML private ComboBox<WayOfBuying> wayOfBuyingComboBox;
    private SDMSystem sdmSystem;


    public AddNewProductController() {
        productId = new SimpleIntegerProperty();
        limitCharsError = new SimpleBooleanProperty(false);
        notIntError = new SimpleBooleanProperty(false);
        availableId = new SimpleBooleanProperty(true);
        validProductName = new SimpleBooleanProperty(true);
        validWayOfBuying = new SimpleBooleanProperty(true);
    }

    @FXML
    public void initialize(){
        wayOfBuyingComboBox.getItems().add(WayOfBuying.BY_QUANTITY);
        wayOfBuyingComboBox.getItems().add(WayOfBuying.BY_WEIGHT);
        errorHBox.visibleProperty().bind(
                limitCharsError.or(
                notIntError).or(
                availableId.not().or(
                        validProductName.not().or(
                                validWayOfBuying.not()
                        ))
        ));
        JavaFxHelper.makeTextFieldInputOnlyInteger(productIdTextField,errorHBox,errorMsgLabel,productId, notIntError);
        JavaFxHelper.initTextFieldLimitCharsError(productIdTextField,errorHBox,errorMsgLabel,LIMIT_ID, limitCharsError);
    }

    public void initDetails(SDMSystem sdmSystem) {
        this.sdmSystem = sdmSystem;

    }

    @FXML
    void onClickCancel(ActionEvent event) {

    }

    @FXML
    void onClickContinue(ActionEvent event) {
        if(checkIfAvailableId() && checkIfProductNameIsValid() && checkIfWayOfBuyingWasSelected()){

//            loadChooseProductsForStoreForm();
//            JavaFxHelper.initMainScrollPane(chooseProductsForStoreScrollPane);
//            mainBorderPane.setCenter(chooseProductsForStoreScrollPane);
//            chooseProductsForStoreController.initDetails(sdmSystem,
//                    storeID.get(),
//                    storeName,
//                    x.get(),
//                    y.get(),
//                    ppk.get(),
//                    mainBorderPane,
//                    startingFormScrollPane,
//                    sdmMainControllers);
        }

    }

    private boolean checkIfWayOfBuyingWasSelected() {
        boolean answer = true;
        if(wayOfBuyingComboBox.getSelectionModel().getSelectedItem() == null){
            answer = false;
            errorMsgLabel.textProperty().set("You must select way of buying!");
            validWayOfBuying.set(false);
        }
        else{
            validWayOfBuying.set(true);
        }
        return answer;
    }

    private boolean checkIfProductNameIsValid() {
        boolean answer = false;
        if (productNameTextField.getText().trim().length() > 0 && !productNameTextField.getText().equals("")) {
            productName = productNameTextField.getText().trim();
            validProductName.set(true);
            answer = true;
        } else {
            validProductName.set(false);
            errorMsgLabel.setText("The name field can't be empty!");
        }
        return answer;
    }

    private boolean checkIfAvailableId() {
        boolean answer = false;
        if (!(productIdTextField.getText().length() > 0 && !productIdTextField.getText().equals(""))) {
            availableId.set(false);
            errorMsgLabel.setText("The product ID field can't be empty!");
        }
        else {
            if (sdmSystem.isAvailableProductId(productId.get())) {
                answer = true;
                availableId.set(true);
            } else {
                availableId.set(false);
                errorMsgLabel.setText("The product ID is already taken, please try different ID");
            }
        }

        return answer;
    }


}
