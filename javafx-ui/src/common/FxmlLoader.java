package common;

import javafx.fxml.FXMLLoader;

import java.io.IOException;
import java.net.URL;

public class FxmlLoader<FormBasePane,FormController> {
    FormBasePane formBasePane;
    FormController formController;
    private final String fxmlFilePath;

    public FxmlLoader(String fxmlFilePath) {
        this.fxmlFilePath = fxmlFilePath;
        loadForm();
    }

    public FormBasePane getFormBasePane() {
        return formBasePane;
    }

    public FormController getFormController() {
        return formController;
    }

    private void loadForm() {
        FXMLLoader loader;
        URL mainFXML;
        loader = new FXMLLoader();
        mainFXML = getClass().getResource(fxmlFilePath);
        loader.setLocation(mainFXML);
        try {
            formBasePane = loader.load();
            formController = loader.getController();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
