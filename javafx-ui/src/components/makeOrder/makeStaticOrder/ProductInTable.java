package components.makeOrder.makeStaticOrder;

import SDMSystemDTO.product.WayOfBuying;

public class ProductInTable {
    private String productName;
    private int productId;
    private WayOfBuying wayOfBuying;
    private Float price;
    private Float amount;

    public ProductInTable(String productName, int productId, WayOfBuying wayOfBuying, Float price, Float amount) {
        this.productName = productName;
        this.productId = productId;
        this.wayOfBuying = wayOfBuying;
        this.price = price;
        this.amount = amount;
    }


    public String getProductName() {
        return productName;
    }

    public int getProductId() {
        return productId;
    }

    public WayOfBuying getWayOfBuying() {
        return wayOfBuying;
    }

    public float getPrice() {
        return price;
    }

    public Float getAmount() {
        return amount;
    }
}
