package SDMSystem.order;

import SDMSystem.HasSerialNumber;
import SDMSystem.customer.Customer;
import SDMSystem.product.IProductInStore;
import SDMSystemDTO.product.IDTOProductInStore;
import SDMSystemDTO.product.WayOfBuying;
import SDMSystemDTO.order.DTOOrder;
import javafx.util.Pair;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;

public abstract class Order implements Serializable, HasSerialNumber<Integer> {
    private static int generatedSerialNumber = 1000;
    protected LocalDate orderDate;
    protected final Collection<Pair<IProductInStore,Float>> productsInOrder;
    protected float productsCost;
    protected final float deliveryCost;
    protected int orderSerialNumber;
    protected int amountOfProducts;
    protected int amountOfProductsKinds;
    protected Customer whoOrdered;
    protected Order mainOrder;
    protected Map<Integer, Collection<Pair<IProductInStore, Float>>> productsInOrderByStores;
    protected Map<Integer, Collection<Pair<IDTOProductInStore, Float>>> shoppingCartAsDTO;


    public Order(LocalDate orderDate,
                 Collection<Pair<IProductInStore, Float>> productsInOrder,
                 float productsCost,
                 float deliveryCost,
                 int amountOfProducts,
                 int amountOfProductsKinds,
                 Customer whoOrdered,
                 Order mainOrder,
                 Map<Integer, Collection<Pair<IProductInStore, Float>>> shoppingCart,
                 Map<Integer, Collection<Pair<IDTOProductInStore, Float>>> shoppingCartAsDTO){
        this.whoOrdered = whoOrdered;
        this.mainOrder = mainOrder;
        this.orderSerialNumber = generatedSerialNumber++;
        this.orderDate = orderDate;
        this.productsInOrder = productsInOrder;
        this.productsCost = productsCost;
        this.deliveryCost = deliveryCost;
        this.amountOfProducts = amountOfProducts;
        this.amountOfProductsKinds = amountOfProductsKinds;
        this.productsInOrderByStores = shoppingCart;
        this.shoppingCartAsDTO = shoppingCartAsDTO;
    }

    public void setMainOrder(Order mainOrder) {
        this.mainOrder = mainOrder;
    }

    public void generateNewSerialNumber(){
        this.orderSerialNumber = generatedSerialNumber++;
    }

    public abstract DTOOrder createDTOOrderFromOrder();

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public int getAmountOfProducts() {
        return amountOfProducts;
    }

    public int getAmountOfProductsKinds() {
        return amountOfProductsKinds;
    }

    @Override
    public Integer getSerialNumber() {
        return orderSerialNumber;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }

    public void addProductToOrder(IProductInStore product, float amount){
        Pair<IProductInStore,Float> newProduct = new Pair<>(product,amount);
        productsInOrder.add(newProduct);
        productsCost += product.getPrice() * amount;
        amountOfProductsKinds++;
        if(product.getWayOfBuying() == WayOfBuying.BY_QUANTITY){
            amountOfProducts += amount;
        }
        else{
            amountOfProducts++;
        }
    }



    public float getDeliveryCost() {
        return deliveryCost;
    }

    public float getProductsCost() {
        return productsCost;
    }

    public Collection<Pair<IProductInStore,Float>> getProductsInOrder() {
        return productsInOrder;
    }

    public Collection<Pair<IDTOProductInStore, Float>> getDTOProductsInOrder() {
        Collection<Pair<IDTOProductInStore, Float>> dtoProductsInOrder = new LinkedList<>();
        for(Pair<IProductInStore,Float> productsInOrder : productsInOrder){
            dtoProductsInOrder.add(new Pair<>(productsInOrder.getKey().createIDTOProductInStore(),productsInOrder.getValue()));
        }

        return dtoProductsInOrder;
    }
}
