package components.main.loadingSystemBar;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import tasks.loadXmlTask.LoadXmlTask;

public class LoadingSystemBarController {

    public static final String LOADING_SYSTEM_FXML_PATH = "/components/main/loadingSystemBar/loadingSystemBar.fxml";

    @FXML private ProgressBar loadingProgressBar;
    @FXML private Label statusLabel;
    @FXML private Label filePathLabel;


    public ProgressBar getLoadingProgressBar() {
        return loadingProgressBar;
    }

    public Label getStatusLabel() {
        return statusLabel;
    }

    public void init(LoadXmlTask loadXmlTask, String absolutePath) {
        loadingProgressBar.progressProperty().bind(loadXmlTask.progressProperty());
        statusLabel.textProperty().bind(loadXmlTask.messageProperty());
        filePathLabel.setText(absolutePath);
    }
}
