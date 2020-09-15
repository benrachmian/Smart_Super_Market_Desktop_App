package tasks.loadXmlTask;

import SDMSystem.system.SDMSystem;
import components.main.SDMMainControllers;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.concurrent.Task;
import javafx.scene.control.Alert;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;

public class LoadXmlTask extends Task<Boolean> {

    private final SDMSystem sdmSystem;
    private final SDMMainControllers sdmMainControllers;
    private final SimpleBooleanProperty fileLoaded;
    private String filePath;


    public LoadXmlTask(SDMSystem sdmSystem, String filePath, SDMMainControllers sdmMainControllers, SimpleBooleanProperty fileLoaded) {
        this.filePath = filePath;
        this.sdmSystem = sdmSystem;
        this.sdmMainControllers = sdmMainControllers;
        this.fileLoaded = fileLoaded;
    }

    @Override
    protected Boolean call() throws Exception {
        try {
            updateMessage("Start loading...");
            updateProgress(0, 1);
            sdmSystem.loadSystem(filePath);
            updateMessage("init customers...");
            updateProgress(0.2, 1);
            Thread.sleep(120);
            new Thread(() -> {
                Platform.runLater(
                        () -> sdmMainControllers.initCustomersInAccordion()
                );
            }).start();
            updateMessage("init products...");
            updateProgress(0.4, 1);
            Thread.sleep(120);
            new Thread(() -> {
                Platform.runLater(
                        () -> sdmMainControllers.initProductsInAccordion()
                );
            }).start();
            updateMessage("init stores...");
            updateProgress(0.5, 1);
            Thread.sleep(120);
            new Thread(() -> {
                Platform.runLater(
                        () -> sdmMainControllers.initStoresInAccordion()
                );
            }).start();
            updateMessage("init orders...");
            updateProgress(0.6, 1);
            Thread.sleep(120);
            new Thread(() -> {
                Platform.runLater(
                        () -> sdmMainControllers.initOrdersInAccordion()
                );
            }).start();
            updateMessage("calc max X coordinate...");
            updateProgress(0.7, 1);
            Thread.sleep(120);
            new Thread(() -> {
                Platform.runLater(
                        () -> sdmMainControllers.maxXCoordinateProperty().set(sdmSystem.getMaxXCoordinate() + 1)
                );
            }).start();
            updateMessage("calc max Y coordinate...");
            updateProgress(0.87, 1);
            Thread.sleep(120);
            new Thread(() -> {
                Platform.runLater(
                        () -> sdmMainControllers.maxYCoordinateProperty().set(sdmSystem.getMaxYCoordinate() + 1)
                );
            }).start();

            updateMessage("Done!");
            updateProgress(1, 1);
            fileLoaded.set(true);
            return true;
        } catch (FileNotFoundException | JAXBException | RuntimeException e) {
            updateMessage("Error!");
            new Thread(() -> {
                Platform.runLater(
                        () -> {
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Error");
                            alert.setHeaderText(e.getMessage());
                            alert.setContentText("Please try different file");
                            alert.showAndWait();
                        });}).start();
            return false;
        }
    }

//    private void showLoadingFileError(String errorMsg) {
//        Alert alert = new Alert(Alert.AlertType.INFORMATION);
//        alert.setTitle("Error");
//        alert.setHeaderText(errorMsg);
//        alert.setContentText("Please try different file");
//        alert.showAndWait();
//    }

}
