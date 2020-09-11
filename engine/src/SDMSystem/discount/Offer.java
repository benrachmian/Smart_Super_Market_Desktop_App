package SDMSystem.discount;

import SDMSystemDTO.discount.DTOOffer;

public class Offer {
    private int productSerialNumber;
    private String productName;
    private double productQuantity;
    private float pricePerUnit;


    public Offer(int productSerialNumber, double productQuantity, float pricePerUnit, String productName) {
        this.productSerialNumber = productSerialNumber;
        this.productQuantity = productQuantity;
        this.pricePerUnit = pricePerUnit;
        this.productName = productName;
    }

    public DTOOffer createDTOOffer() {
        return new DTOOffer(productSerialNumber,productQuantity,pricePerUnit,productName);
    }

    public int getProductSerialNumber() {
        return productSerialNumber;
    }

    public String getProductName() {
        return productName;
    }

    public double getProductQuantity() {
        return productQuantity;
    }

    public float getPricePerUnit() {
        return pricePerUnit;
    }
}
