package common;

import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

import java.util.Optional;


public class JavaFxHelper {

    public static void makeTextFieldInputOnlyFloat(TextField textField, Label errorLabel, SimpleFloatProperty amountInTextField){
        //not allow to write chars that aren't digits
        textField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(!newValue.matches("\\d*(\\.\\d*)?")) {
                    textField.setText(oldValue);
                    errorLabel.visibleProperty().set(true);
                    errorLabel.setManaged(true);
                }
                else{
                    errorLabel.visibleProperty().set(false);
                    errorLabel.setManaged(false);
                    if(!textField.getText().isEmpty() && !textField.getText().equals(".")) {
                        amountInTextField.set(Float.parseFloat(textField.getText()));
                    }
                }
            }
        });
    }

    public static boolean isInteger(Float number) {
        return number % 1 == 0;// if the modulus(remainder of the division) of the argument(number) with 1 is 0 then return true otherwise false.
    }

    public static void initMainScrollPane(ScrollPane scrollPane) {
        scrollPane.setMinWidth(250);
        scrollPane.setMinHeight(250);
        scrollPane.setMaxHeight(1000);
        scrollPane.setMaxWidth(4000);
        scrollPane.fitToWidthProperty().set(true);
        scrollPane.fitToHeightProperty().set(true);
    }

    public static void cancelOrderAlert(BorderPane mainBorderPane, GridPane startingFormGridPane) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Cancel Warning");
        alert.setHeaderText("You are about to cancel the order");
        alert.setContentText("You can't undo the action.\nAre you sure about it?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            mainBorderPane.setCenter(startingFormGridPane);
        }
    }
}
