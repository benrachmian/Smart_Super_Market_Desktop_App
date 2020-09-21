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
    private SimpleBooleanProperty availableId;
    private SimpleBooleanProperty validStoreName;
    private SimpleBooleanProperty validXandY;
    private SimpleBooleanProperty validPPK;
    private SDMSystem sdmSystem;

    @FXML private HBox errorHBox;
    @FXML private Label errorMsgLabel;
    @FXML private TextField storeIdTextField;
    @FXML private TextField xTextField;
    @FXML private TextField yTextField;
    @FXML private TextField ppkTextField;
    @FXML private TextField storeNameTextField;


    public AddNewStoreInsertDetailsController() {
        storeID = new SimpleIntegerProperty();
        x = new SimpleIntegerProperty();
        y = new SimpleIntegerProperty();
        limitCharsError = new SimpleBooleanProperty(false);
        notIntError = new SimpleBooleanProperty(false);
        limitValueError = new SimpleBooleanProperty(false);
        ppk = new SimpleFloatProperty();
        notFloatError = new SimpleBooleanProperty(false);
        availableId = new SimpleBooleanProperty(true);
        validStoreName = new SimpleBooleanProperty(true);
        validXandY = new SimpleBooleanProperty(true);
        validPPK = new SimpleBooleanProperty(true);
    }

    @FXML
    public void initialize(){
        errorHBox.visibleProperty().bind(limitCharsError.or(
                notIntError).or(
                        limitValueError).or(
                                notFloatError).or(
                                        availableId.not().or(
                                                validStoreName.not().or(
                                                        validXandY.not().or(
                                                                validPPK.not()
                                                        )
                                                )
                                        )
        ));
        JavaFxHelper.makeTextFieldInputOnlyInteger(storeIdTextField,errorHBox,errorMsgLabel,storeID, notIntError);
        JavaFxHelper.initTextFieldLimitCharsError(storeIdTextField,errorHBox,errorMsgLabel,LIMIT_ID, limitCharsError);
        JavaFxHelper.makeTextFieldInputOnlyFloat(ppkTextField,errorHBox,errorMsgLabel,ppk,notFloatError);

    }

    @FXML
    void onClickCancel(ActionEvent event) {

    }

    @FXML
    void onClickContinue(ActionEvent event) {
        if(checkIfAvailableId() && checkIfStoreNameIsValid() && checkIfXandYAreValid() && checkIfPpkIsValid()){

        }

    }

    private boolean checkIfPpkIsValid() {
        boolean answer = false;
        if (ppkTextField.getText().length() > 0 && !ppkTextField.getText().equals("")) {
            ppk.set(Float.parseFloat(ppkTextField.getText()));
            if(ppk.get() != 0) {
                validPPK.set(true);
                answer = true;
            }
        } if(!answer) {
            validPPK.set(false);
            errorMsgLabel.setText("The ppk field can't be empty / 0!");
        }
        return answer;
    }

    private boolean checkIfXandYAreValid() {
        boolean answer = false;
        if (xTextField.getText().length() > 0 && !xTextField.getText().equals("") &&
                yTextField.getText().length() > 0 && !yTextField.getText().equals("") &&
                    !limitValueError.getValue()) {
            x.set(Integer.parseInt(xTextField.getText()));
            y.set(Integer.parseInt(yTextField.getText()));
            if(x.get() != 0 && y.get() != 0) {
                validXandY.set(true);
                answer = true;
            }
        }
        if(!answer) {
            validXandY.set(false);
            errorMsgLabel.setText("The x and y fields can't be empty / 0!");
        }
        return answer;

    }

    private boolean checkIfStoreNameIsValid() {
        boolean answer = false;
        if (storeNameTextField.getText().length() > 0 && !storeNameTextField.getText().equals("")) {
            storeName = storeNameTextField.getText().trim();
            validStoreName.set(true);
            answer = true;
        } else {
            validStoreName.set(false);
            errorMsgLabel.setText("The name field can't be empty!");
        }
        return answer;
    }

    private boolean checkIfAvailableId() {
        boolean answer = false;
        if (!(storeIdTextField.getText().length() > 0 && !storeIdTextField.getText().equals(""))) {
            availableId.set(false);
            errorMsgLabel.setText("The store ID field can't be empty!");
        }
        else {
            if (sdmSystem.isAvailableStoreId(storeID)) {
                answer = true;
                availableId.set(true);
            } else {
                availableId.set(false);
                errorMsgLabel.setText("The store ID is already taken, please try different ID");
            }
        }

        return answer;
    }


    public void initDetails(SDMSystem sdmSystem) {
        this.sdmSystem = sdmSystem;
        JavaFxHelper.makeTextFieldInputOnlyIntegerWithLimitValue(xTextField,errorHBox,errorMsgLabel,x,notIntError,limitValueError,sdmSystem.MAX_COORDINATE);
        JavaFxHelper.makeTextFieldInputOnlyIntegerWithLimitValue(yTextField,errorHBox,errorMsgLabel,y,notIntError,limitValueError,sdmSystem.MAX_COORDINATE);
    }
}
