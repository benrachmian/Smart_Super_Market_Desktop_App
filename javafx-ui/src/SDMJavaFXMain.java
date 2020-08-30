import SDMSystem.system.SDMSystem;
import common.SDMResourcesConstants;
import components.main.SDMMainControllers;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.net.URL;


public class SDMJavaFXMain extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {

        FXMLLoader loader = new FXMLLoader();

        //load main fxml
        URL mainFXML = getClass().getResource(SDMResourcesConstants.MAIN_FXML_RESOURCE_IDENTIFIER);
        loader.setLocation(mainFXML);
        BorderPane root = loader.load();

        //wire up controllers
        SDMMainControllers sdmMainControllers = loader.getController();
        SDMSystem sdmSystem = new SDMSystem();
        sdmMainControllers.setSdmSystem(sdmSystem);
        sdmMainControllers.setPrimaryStage(primaryStage);

        //set stage
        primaryStage.setTitle("Super Duper Market System");
        Scene scene = new Scene(root, 1100, 700);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
