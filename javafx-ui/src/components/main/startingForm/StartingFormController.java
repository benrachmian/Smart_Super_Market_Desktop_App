package components.main.startingForm;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class StartingFormController {

    public static final String STARTING_FORM_FXML_PATH = "/components/main/startingForm/startingForm.fxml";

    @FXML private Label loadMsgLabel;

    @FXML
    public void initialize(){

    }


    public void init(SimpleBooleanProperty fileLoaded) {
        loadMsgLabel.visibleProperty().bind(fileLoaded.not());
    }
}
