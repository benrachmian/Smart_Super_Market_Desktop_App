package common;

import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;


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
}
