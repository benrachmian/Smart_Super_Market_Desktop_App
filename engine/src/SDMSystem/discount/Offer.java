package SDMSystem.discount;

import SDMSystemDTO.discount.DTOOffer;

public class Offer {
    private int productSerialNumber;
    private double productQuantity;
    private float pricePerUnit;

    public Offer(int productSerialNumber, double productQuantity, float pricePerUnit) {
        this.productSerialNumber = productSerialNumber;
        this.productQuantity = productQuantity;
        this.pricePerUnit = pricePerUnit;
    }

    public DTOOffer createDTOOffer() {
        return new DTOOffer(productSerialNumber,productQuantity,pricePerUnit);
    }
}
