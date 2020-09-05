package SDMSystemDTO.discount;

public class DTOOffer {
    private int productSerialNumber;
    private double productQuantity;
    private float pricePerUnit;

    public DTOOffer(int productSerialNumber, double productQuantity, float pricePerUnit) {
        this.productSerialNumber = productSerialNumber;
        this.productQuantity = productQuantity;
        this.pricePerUnit = pricePerUnit;
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
}

