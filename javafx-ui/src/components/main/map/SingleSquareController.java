package components.main.map;

import SDMSystem.customer.Customer;
import SDMSystem.store.Store;
import SDMSystem.system.SDMSystem;
import common.FxmlLoader;
import common.JavaFxHelper;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.awt.*;

public class SingleSquareController {
        public enum SquareType{
                EMPTY,CUSTOMER,STORE
        }

        public static final String SINGLE_SQUARE_FXML_PATH = "/components/main/map/singleSquare.fxml";
        private int x;
        private int y;
        private SquareType squareType;
        private Tooltip tooltip;
        private SDMSystem sdmSystem;
        private GridPane storeInfoGridPane = null;
        private GridPane customerInfoGridPane = null;
        private StoreInfoController storeInfoController;
        private CustomerInfoController customerInfoController;
        private Stage primaryStage;

        @FXML private GridPane singleSquareGridPane;


        @FXML
        public void initialize(){
                singleSquareGridPane.setAlignment(Pos.CENTER);

        }

        public void initDetails(int x, int y, SDMSystem sdmSystem, Stage primaryStage){
                this.x = x;
                this.y = y;
                this.sdmSystem = sdmSystem;
                this.primaryStage = primaryStage;
                squareType = SquareType.EMPTY;
                tooltip = new Tooltip("X: " + x + ", Y: " + y);
                Tooltip.install(singleSquareGridPane,tooltip);
        }

        public void setSquareType(SquareType squareType) {
                this.squareType = squareType;
        }

        public Tooltip getTooltip() {
                return tooltip;
        }

        @FXML
        void onClick(MouseEvent event) {
                if(squareType == SquareType.STORE) {
                        loadStoreInfo();
                        storeInfoController.initDetails(sdmSystem.getStoreInSystemByLocation(new Point(x,y)));
                        createInfoStage("Store Information",storeInfoGridPane);
                }
                else if(squareType == SquareType.CUSTOMER){
                        loadCustomerInfo();
                        customerInfoController.initDetails(sdmSystem.getCustomer(new Point(x,y)));
                        createInfoStage("Customer Information",customerInfoGridPane);
                }
        }

        private void createInfoStage(String title, Parent nodeForScene) {
                Stage stage = new Stage();
                stage.setTitle(title);
                stage.setScene(new Scene(nodeForScene));
                stage.sizeToScene();
                stage.setResizable(false);
                stage.initModality(Modality.WINDOW_MODAL);
                stage.initOwner(primaryStage);
                stage.show();
        }

        private void loadCustomerInfo() {
                FxmlLoader<GridPane, CustomerInfoController> loaderCustomerInfo = new FxmlLoader<>(CustomerInfoController.CUSTOMER_INFO_FXML_PATH);
                customerInfoGridPane = loaderCustomerInfo.getFormBasePane();
                customerInfoController = loaderCustomerInfo.getFormController();
        }

        private void loadStoreInfo() {
                FxmlLoader<GridPane, StoreInfoController> loaderStoreInfo = new FxmlLoader<>(StoreInfoController.STORE_INFO_FXML_PATH);
                storeInfoGridPane = loaderStoreInfo.getFormBasePane();
                storeInfoController = loaderStoreInfo.getFormController();
        }

        @FXML
        void onHover(MouseEvent event) {

        }
}
