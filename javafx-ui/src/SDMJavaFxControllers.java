import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.Observable;
import java.util.ResourceBundle;

public class SDMJavaFxControllers implements Initializable {

    @FXML
    private ListView<String> customerListView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ObservableList<String> data = FXCollections.observableArrayList(
                "Try1","tri2","Try3"
        );
        customerListView.setItems(data);
    }

    @FXML
    void customerItemClicked(MouseEvent event) {
    }

}
