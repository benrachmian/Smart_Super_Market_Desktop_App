package components.makeOrder.makeDynamicOrder;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import tasks.loadXmlTask.FindCheapestBasketTask;

public class FindingCheapestBasketController {

    public static final String FINDING_CHEAPEST_BASKET_FORM_FXML_PATH = "/components/makeOrder/makeDynamicOrder/findingCheapestBasketTask.fxml";

    @FXML private ProgressBar loadingProgressBar;
    @FXML private Label statusLabel;
    @FXML private Button continueButton;

    private SimpleBooleanProperty done;
    private MakeDynamicOrderController makeDynamicOrderController;


    public void init(FindCheapestBasketTask findCheapestBasketTask,
                     SimpleBooleanProperty done,
                     MakeDynamicOrderController makeDynamicOrderController) {
        loadingProgressBar.progressProperty().bind(findCheapestBasketTask.progressProperty());
        statusLabel.textProperty().bind(findCheapestBasketTask.messageProperty());
        this.done = done;
        continueButton.disableProperty().bind(done.not());
        this.makeDynamicOrderController = makeDynamicOrderController;
    }


    @FXML
    void onContinue(ActionEvent event) {
        makeDynamicOrderController.calcTotalProductsPriceAndDeliveryCost();
        makeDynamicOrderController.createStoresParticipatingInDynamicOrderForm();

    }

}
