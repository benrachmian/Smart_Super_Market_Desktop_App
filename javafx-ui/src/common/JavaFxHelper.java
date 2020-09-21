package common;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;

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


    public static void makeTextFieldInputOnlyInteger(TextField textField, Node errorNode, Label errorMsgLabel, SimpleIntegerProperty intInTextFieldValue, SimpleBooleanProperty notIntError){
        //not allow to write chars that aren't digits
        textField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(!newValue.matches("\\d*?")) {
                    textField.setText(oldValue);
                    if(!notIntError.getValue()) {
                        notIntError.set(true);
                    }
                    errorMsgLabel.setText("You must enter an integer number!");
                }
                else{
                    if(notIntError.getValue()) {
                        notIntError.set(false);
                    }
                    if(!textField.getText().isEmpty() && !textField.getText().equals(".")) {
                        intInTextFieldValue.set(Integer.parseInt(textField.getText()));
                    }
                }
            }
        });
    }

    public static void makeTextFieldInputOnlyFloat(TextField textField, Node errorNode, Label errorMsgLabel, SimpleFloatProperty floatInTextFieldValue, SimpleBooleanProperty notFloatError){
        //not allow to write chars that aren't digits
        textField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(!newValue.matches("\\d*(\\.\\d*)?")) {
                    textField.setText(oldValue);
                    if(!notFloatError.getValue()) {
                        notFloatError.set(true);
                    }
                    errorMsgLabel.setText("You must enter a float number!");
                }
                else{
                    if(notFloatError.getValue()) {
                        notFloatError.set(false);
                    }
                    if(!textField.getText().isEmpty() && !textField.getText().equals(".")) {
                        floatInTextFieldValue.set(Float.parseFloat(textField.getText()));
                    }
                }
            }
        });
    }

    public static void initTextFieldLimitCharsError(TextField textField, Node errorNode, Label errorMsgLabel, int limit, SimpleBooleanProperty limitCharsError){
        textField.lengthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                    if (textField.getText().length() > limit) {
                        if (!limitCharsError.getValue()) {
                            limitCharsError.set(true);
                        }
                        errorMsgLabel.setText("You must enter " + limit + " characters max!");
                        textField.setText(textField.getText().substring(0, limit));
                    } else {
                        if (limitCharsError.getValue() && !(newValue.intValue() == limit && oldValue.intValue() == limit+1)) {
                            limitCharsError.set(false);
                        }
                    }
            }
        });
    }

    public static void makeTextFieldInputOnlyIntegerWithLimitValue(TextField textField,
                                                                   Node errorNode,
                                                                   Label errorMsgLabel,
                                                                   SimpleIntegerProperty intInTextFieldValue,
                                                                   SimpleBooleanProperty notIntError,
                                                                   SimpleBooleanProperty limitValueError,
                                                                   int limitValue){
        //not allow to write chars that aren't digits
        textField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(!newValue.matches("\\d*?")) {
                    textField.setText(oldValue);
                    if(!notIntError.getValue()) {
                        notIntError.set(true);
                    }
                    errorMsgLabel.setText("You must enter an integer number!");
                }
                //its integer
                else{
                    if(notIntError.getValue()) {
                        notIntError.set(false);
                    }
                    if(!textField.getText().isEmpty() && !textField.getText().equals(".")) {
                        intInTextFieldValue.set(Integer.parseInt(textField.getText()));
                        if (intInTextFieldValue.getValue() > limitValue) {
                            if (!limitValueError.getValue()) {
                                limitValueError.set(true);
                            }
                            errorMsgLabel.setText("The maximum number is " + limitValue + "!");
                        } else {
                            if (limitValueError.getValue()) {
                                limitValueError.set(false);
                            }
                        }
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

    public static boolean cancelOrderAlert(BorderPane mainBorderPane, ScrollPane startingFormScrollPane, SimpleBooleanProperty orderInProgress) {
        boolean answer = false;
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Cancel Warning");
        alert.setHeaderText("You are about to cancel the order");
        alert.setContentText("You can't undo the action.\nAre you sure about it?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            mainBorderPane.setCenter(startingFormScrollPane);
            orderInProgress.set(false);
            answer = true;
        }

        return answer;
    }


}
