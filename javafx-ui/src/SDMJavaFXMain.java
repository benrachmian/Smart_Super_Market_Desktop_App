import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SDMJavaFXMain extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("First JavaFX App!");
        Parent load = FXMLLoader.load(getClass().getResource("SDMJavaFxFxml.fxml"));
        Scene scene = new Scene(load, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
