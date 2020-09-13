package components.details.productsDetails;

import SDMSystem.system.SDMSystem;
import SDMSystemDTO.customer.DTOCustomer;
import SDMSystemDTO.product.DTOProduct;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;

public class ProductDetailsController {
    @FXML private Label serialNumberAnswerLabel;
    @FXML private Label productNameAnswerLabel;
    @FXML private Label wayOfBuyingAnswerLabel;
    @FXML private Label numOfStoresSellingAnswerLabel;
    @FXML private Label averagePriceAnswerLabel;
    @FXML private Label amountSoldAnswerLabel;
    @FXML private ScrollPane productsDetailsScrollPane;
    @FXML private GridPane productsDetailsGridPane;

    private SDMSystem sdmSystem;

    @FXML
    public void initialize(){
        productsDetailsGridPane.setAlignment(Pos.CENTER);
    }

    public void updateGrid(DTOProduct selectedItem) {
        serialNumberAnswerLabel.setText(String.format("%d",selectedItem.getProductSerialNumber()));
        productNameAnswerLabel.setText(selectedItem.getProductName());
        wayOfBuyingAnswerLabel.setText(selectedItem.getWayOfBuying().toString());
        numOfStoresSellingAnswerLabel.setText(String.format("%d",sdmSystem.getNumberOfStoresSellingProduct(selectedItem.getProductSerialNumber())));
        if(sdmSystem.getNumberOfStoresSellingProduct(selectedItem.getProductSerialNumber()) == 0){
            averagePriceAnswerLabel.setText( "There are no stores selling the product! ");
        }
        else{
            averagePriceAnswerLabel.setText(String.format("%.2f",sdmSystem.getAveragePriceOfProduct(selectedItem.getProductSerialNumber())));
        }
        amountSoldAnswerLabel.setText(String.format("%.2f",selectedItem.getAmountSoldInAllStores()));
    }

    public void setSdmSystem(SDMSystem sdmSystem) {
        this.sdmSystem = sdmSystem;
    }
}
