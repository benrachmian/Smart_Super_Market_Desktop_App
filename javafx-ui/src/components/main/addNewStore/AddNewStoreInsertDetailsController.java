package components.main.addNewStore;

import SDMSystem.system.SDMSystem;
import common.JavaFxHelper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

import java.awt.*;


public class AddNewStoreInsertDetailsController {

    public static final String NEW_STORE_DETAILS_FXML_PATH = "/components/main/addNewStore/addNewStoreInsertDetails.fxml";
    private static final int LIMIT_ID = 8;
    private SimpleIntegerProperty storeID;
    private SimpleIntegerProperty x;
    private SimpleIntegerProperty y;
    private SimpleFloatProperty ppk;
    private String storeName;
    private Point storeLocation;
    private float storePPK;
    private SimpleBooleanProperty limitCharsError;
    private SimpleBooleanProperty notIntError;
    private SimpleBooleanProperty notFloatError;
    private SimpleBooleanProperty limitValueError;
    private SDMSystem sdmSystem;

    @FXML private HBox errorHBox;
    @FXML private Label errorMsgLabel;
    @FXML private TextField storeIdTextField;
    @FXML private TextField xTextField;
    @FXML private TextField yTextField;
    @FXML private TextField ppkTextField;


    public AddNewStoreInsertDetailsController() {
        storeID = new SimpleIntegerProperty();
        x = new SimpleIntegerProperty();
        y = new SimpleIntegerProperty();
        limitCharsError = new SimpleBooleanProperty(false);
        notIntError = new SimpleBooleanProperty(false);
        limitValueError = new SimpleBooleanProperty(false);
        ppk = new SimpleFloatProperty();
        notFloatError = new SimpleBooleanProperty(false);
    }

    @FXML
    public void initialize(){
        errorHBox.visibleProperty().bind(limitCharsError.or(notIntError).or(limitValueError).or(notFloatError));
        JavaFxHelper.makeTextFieldInputOnlyInteger(storeIdTextField,errorHBox,errorMsgLabel,storeID, notIntError);
        JavaFxHelper.initTextFieldLimitCharsError(storeIdTextField,errorHBox,errorMsgLabel,LIMIT_ID, limitCharsError);
        JavaFxHelper.makeTextFieldInputOnlyFloat(ppkTextField,errorHBox,errorMsgLabel,ppk,notFloatError);

    }

    @FXML
    void onClickCancel(ActionEvent event) {

    }

    @FXML
    void onClickContinue(ActionEvent event) {

    }


    public void initDetails(SDMSystem sdmSystem) {
        this.sdmSystem = sdmSystem;
        JavaFxHelper.makeTextFieldInputOnlyIntegerWithLimitValue(xTextField,errorHBox,errorMsgLabel,x,notIntError,limitValueError,sdmSystem.MAX_COORDINATE);
        JavaFxHelper.makeTextFieldInputOnlyIntegerWithLimitValue(yTextField,errorHBox,errorMsgLabel,y,notIntError,limitValueError,sdmSystem.MAX_COORDINATE);
    }
}
