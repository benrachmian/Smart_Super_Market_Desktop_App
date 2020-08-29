package components.main;

import SDMSystem.system.SDMSystem;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Observable;
import java.util.ResourceBundle;

public class SDMMainControllers {

    @FXML private ListView<String> customerListView;
    @FXML private Button tryButtton;
    @FXML private MenuItem loadSystemXmlMenuItem;

    private SDMSystem sdmSystem;
    private Stage primaryStage;

    @FXML
    public void initialize(){
        customerListView.getItems().add("bennnn");
        customerListView.getItems().add("mikeee");
    }

    @FXML
    void loadSystemXmlMenuItemAction(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText("Look, an Information Dialog");
        alert.setContentText("I have a great message for you!");
        alert.showAndWait();
    }



    @FXML
    void customerItemClicked(MouseEvent event) {
        if(customerListView.getSelectionModel().getSelectedItem().equals("bennnn")){
            tryButtton.setText("Ben");

        }
        else if(customerListView.getSelectionModel().getSelectedItem().equals("mikeee")){
            tryButtton.setText("mikeee");
        }
        else{
            tryButtton.setText("other");
        }
    }

    public void setSdmSystem(SDMSystem sdmSystem) {
        this.sdmSystem = sdmSystem;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }
}
