package SDMSystem.discount;

public class Offer {
    private int productSerialNumber;
    private double productQuantity;
    private float pricePerUnit;

    public Offer(int productSerialNumber, double productQuantity, float pricePerUnit) {
        this.productSerialNumber = productSerialNumber;
        this.productQuantity = productQuantity;
        this.pricePerUnit = pricePerUnit;
    }
}
