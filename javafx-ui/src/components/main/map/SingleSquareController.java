package components.main.map;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class SingleSquareController {
        public enum SquareType{
                EMPTY,CUSTOMER,STORE
        }

        public static final String SINGLE_SQUARE_FXML_PATH = "/components/main/map/singleSquare.fxml";
        private int x;
        private int y;
        private SquareType squareType;

        @FXML private GridPane productGridPane;


        @FXML
        public void initialize(){
                productGridPane.setAlignment(Pos.CENTER);

        }

        public void initDetails(int x, int y){
                this.x = x;
                this.y = y;
                squareType = SquareType.EMPTY;
                Tooltip tooltip = new Tooltip("X: " + x + ", Y: " + y);
                Tooltip.install(productGridPane, tooltip);
        }

        public void setSquareType(SquareType squareType) {
                this.squareType = squareType;
        }


        @FXML
        void onClick(MouseEvent event) {
                if(squareType == SquareType.STORE){
                        Stage stage = new Stage();
                        stage.setTitle("My New Stage Title");
                        stage.setScene(new Scene(new Label("Ben Rachmian"), 450, 450));
                        stage.show();
                }
        }


}
