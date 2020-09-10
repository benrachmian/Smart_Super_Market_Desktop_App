package SDMSystemDTO.discount;

public class DTOOffer {
    private int productSerialNumber;
    private double productQuantity;
    private float pricePerUnit;
    private String productName;


    public DTOOffer(int productSerialNumber, double productQuantity, float pricePerUnit, String productName) {
        this.productSerialNumber = productSerialNumber;
        this.productQuantity = productQuantity;
        this.pricePerUnit = pricePerUnit;
        this.productName = productName;
    }

    public String getProductName() {
        return productName;
    }

    public int getProductSerialNumber() {
        return productSerialNumber;
    }

    public double getProductQuantity() {
        return productQuantity;
    }

    public float getPricePerUnit() {
        return pricePerUnit;
    }

    @Override
    public String toString() {
        return productName + ", ID: " + productSerialNumber;
    }
}

