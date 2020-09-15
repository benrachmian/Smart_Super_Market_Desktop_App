package components.main.map;

import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;

public class SingleSquareController {

        public static final String SINGLE_SQUARE_FXML_PATH = "/components/main/map/singleSquare.fxml";
        private int x;
        private int y;

        public void initDetails(int x, int y){
                this.x = x;
                this.y = y;
        }

        @FXML
        void onClick(MouseEvent event) {
        }


}
