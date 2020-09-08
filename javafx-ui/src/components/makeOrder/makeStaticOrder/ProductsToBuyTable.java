package components.makeOrder.makeStaticOrder;

import SDMSystemDTO.product.WayOfBuying;

public class ProductsToBuyTable {
    private String productName;
    private int productId;
    private WayOfBuying wayOfBuying;
    private Float price;

    public ProductsToBuyTable(String productName, int productId, WayOfBuying wayOfBuying, Float price) {
        this.productName = productName;
        this.productId = productId;
        this.wayOfBuying = wayOfBuying;
        this.price = price;
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
}
