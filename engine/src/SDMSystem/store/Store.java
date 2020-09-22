package SDMSystem.store;

import SDMSystem.HasSerialNumber;
import SDMSystem.discount.Discount;
import SDMSystem.discount.Offer;
import SDMSystem.location.LocationUtility;
import SDMSystem.location.Locationable;
import SDMSystem.order.Order;
import SDMSystem.product.Product;
import SDMSystem.product.ProductInStore;
import SDMSystem.exceptions.*;
import SDMSystem.system.ItemInSystem;
import SDMSystemDTO.discount.DTODiscount;
import SDMSystemDTO.product.DTOProduct;
import SDMSystemDTO.product.DTOProductInDiscount;
import SDMSystemDTO.product.DTOProductInStore;
import SDMSystemDTO.order.DTOOrder;
import SDMSystemDTO.product.IDTOProductInStore;
import SDMSystemDTO.store.DTOStore;
import javafx.util.Pair;

import java.awt.*;
import java.io.Serializable;
import java.util.*;

public class Store implements Locationable, HasSerialNumber<Integer>, Serializable {

    private static int generatedSerialNumber = 1000;
    private final Map<Integer,ProductInStore> productsInStore;
    private final Point storeLocation;
    private final float ppk;
    private final int storeSerialNumber;
    private final String storeName;
    private final Collection<Order> ordersFromStore;
    //private final Collection<Feedback> storeFeedbacks;
    private float totalProfitFromDelivery;
    private final Collection<Discount> storeDiscounts;

//    public Store(Point storeLocation, float ppk, String storeName) { //ctor
//        this.productsInStore = new HashMap<>();
//        this.storeLocation = storeLocation;
//        this.ppk = ppk;
//        this.storeSerialNumber = generatedSerialNumber++;
//        this.storeName = storeName;
//        this.ordersFromStore = new HashSet<>();
//        //this.storeFeedbacks = null;
//        this.totalProfitFromDelivery = 0;
//        this.storeDiscounts = new LinkedList<>();
//    }

    public Collection<Discount> getStoreDiscounts() {
        return storeDiscounts;
    }

    public Store(int storeSerialNumber, Point storeLocation, float ppk, String storeName, Collection<Discount> storeDiscounts) {
        this.storeDiscounts = storeDiscounts; //ctor
        this.productsInStore = new HashMap<>();
        this.storeLocation = storeLocation;
        this.ppk = ppk;
        this.storeSerialNumber = storeSerialNumber;
        this.storeName = storeName;
        this.ordersFromStore = new HashSet<>();
        //this.storeFeedbacks = null;
    }

    public void addNewProductToStore(Product newProduct, float price){
        //if already in store throws exception
        if(productsInStore.putIfAbsent(newProduct.getSerialNumber(),
                                        new ProductInStore(newProduct,price,this))
                                        != null){
            throw new ExistenceException(true,newProduct.getSerialNumber(),"Product", "Store");
        }
        newProduct.addStore(this);
    }

    public Point getStoreLocation() {
        return storeLocation;
    }

    public float getPpk() {
        return ppk;
    }

    public ProductInStore getProductInStore(int productSerialNumber){
        ProductInStore askedProduct = productsInStore.get(productSerialNumber);
        if(askedProduct == null){
            throw new ExistenceException(false,productSerialNumber,"Product","Store");
        }
       return askedProduct;
    }

    public boolean isAvailableInStore(int productSerialNumber){
        boolean isAvailable = false;
        if(productsInStore.get(productSerialNumber) != null){
            isAvailable = true;
        }
        return isAvailable;
    }

    public String getStoreName() {
        return storeName;
    }

    @Override
    public Point getLocation() {
        return storeLocation;
    }

    @Override
    public float getDistanceFrom(Point target) {
        return LocationUtility.calcDistance(this.storeLocation,target);
    }

    @Override
    public Integer getSerialNumber() {
        return storeSerialNumber;
    }


    public Map<Integer, DTOProductInStore> getDTOProductsInStore() {
        Map<Integer, DTOProductInStore> DTOProductsInStore = new HashMap<>();
        for(ProductInStore productInStore : this.productsInStore.values()){
            DTOProductInStore newProduct = productInStore.createDTOProductInStore();
            DTOProductsInStore.put(newProduct.getProductSerialNumber(),newProduct);
        }
        return DTOProductsInStore;
    }


    public DTOStore createDTOStore() {
        return new DTOStore(
                getDTOProductsInStore(),
                storeLocation,
                ppk,
                storeSerialNumber,
                storeName,
                totalProfitFromDelivery,
                getDTODiscountsInStore(),
                ordersFromStore.size());
    }

    private Collection<DTODiscount> getDTODiscountsInStore() {
        Collection<DTODiscount> dtoDiscounts = null;
        if(storeDiscounts != null) {
            dtoDiscounts = new LinkedList<>();
            for (Discount discount : this.storeDiscounts) {
                dtoDiscounts.add(discount.createDTODiscount());
            }
        }

        return dtoDiscounts;
    }


    public float getTotalProfitFromDelivery() {
        return totalProfitFromDelivery;
    }

    public Collection<DTOOrder> getDTOOrdersFromStore() {
        Collection<DTOOrder> dtoOrders = new LinkedList<>();
        for(Order order : ordersFromStore){
            DTOOrder newDTOOrder = order.createDTOOrderFromOrder();
            dtoOrders.add(newDTOOrder);
        }

        return dtoOrders;
    }

//    private Collection<DTOProductInStore> createDTOProductInStoreCollection(Collection<ProductInStore> productsCollection) {
//        Collection<DTOProductInStore> dtoProductInStoreCollection = new LinkedList();
//        for (ProductInStore productInStore : productsCollection){
//            DTOProductInStore dtoProductInStore = productInStore.createDTOProductInStore();
//            dtoProductInStoreCollection.add(dtoProductInStore);
//        }
//
//        return dtoProductInStoreCollection;
//    }

//    public void makeNewOrderAndAddToOrdersIsSystem(Date orderDate,
//                                                   float deliveryCost,
//                                                   Collection<Pair<Float, ProductInStore>> productsInOrder,
//                                                   Map<Integer, Order> ordersInSystem) {
//        Order newOrder = createdNewOrderObject(orderDate,deliveryCost,productsInOrder);
//
//        ordersFromStore.add(newOrder);
//        ordersInSystem.put(newOrder.getOrderSerialNumber(),newOrder);
//    }

//    private Order createdNewOrderObject(Date orderDate,
//                                        float deliveryCost,
//                                        Collection<Pair<Float, ProductInStore>> productsInOrder) {
//
//        Collection<Pair<Float,ProductInStore>> productsInOrder = new LinkedList<>();
//        int amountOfProducts = 0, amountKindsOfProducts = 0;
//        for(Pair<Float, DTOProductInStore> dtoProductInOrder : productsInOrder){
//            ProductInStore productInStore = productsInStore.get(dtoProductInOrder.getValue().getProductSerialNumber());
//            productInStore.increaseAmountSoldInStore(dtoProductInOrder.getKey());
//            Pair<Float,ProductInStore> newProductInOrder = new Pair<>(dtoProductInOrder.getKey(),productInStore);
//            productsInOrder.add(newProductInOrder);
//            amountKindsOfProducts++;
//            if(dtoProductInOrder.getValue().getWayOfBuying() == WayOfBuying.BY_QUANTITY){
//                amountOfProducts += dtoProductInOrder.getKey();
//            }
//            else{
//                amountOfProducts++;
//            }
//        }
//        float productsCost = calcProductsCost(productsInOrder);
//
//        return new Order(orderDate,
//                productsInOrder,
//                productsCost,
//                deliveryCost,
//                this,
//                amountOfProducts,
//                amountKindsOfProducts);
//    }

//    private float calcProductsCost(Collection<Pair<Float, ProductInStore>> productsInOrder) {
//        float totalCost = 0;
//        for(Pair<Float,ProductInStore> productInOrder : productsInOrder){
//            totalCost += productInOrder.getKey() * productInOrder.getValue().getPrice();
//        }
//
//        return totalCost;
//    }

    public float getDeliveryCost(Point locationFromTheUser) {
        return this.getDistanceFrom(locationFromTheUser) * ppk;
    }

    private void increaseTotalProfitFromDelivery(float deliveryCost) {
        totalProfitFromDelivery += deliveryCost;
    }

    public void addOrder(Order newOrder, float deliveryCost) {
        ordersFromStore.add(newOrder);
        increaseTotalProfitFromDelivery(deliveryCost);
    }

    public void deleteProduct(ProductInStore productToDelete) {
        productsInStore.remove(productToDelete.getSerialNumber());
        productToDelete.deleteStoreFromStoresSellingTheProduct(this);
    }

    public void updateProductPrice(int productSerialNumber, float newPrice) {
        ProductInStore productToUpdate = productsInStore.get(productSerialNumber);
        productToUpdate.setPrice(newPrice);
    }

    public boolean isProductPartOfDiscount(int productSerialNumber) {
        boolean answer = false;
        if(storeDiscounts != null) {
            for (Discount discount : storeDiscounts) {
                if (discount.getIfYouBuyProductAndAmount().getKey() == productSerialNumber) {
                    answer = true;
                    break;
                }
                for (Offer offer : discount.getOffers()) {
                    if (offer.getProductSerialNumber() == productSerialNumber) {
                        answer = true;
                        break;
                    }
                }
            }
        }

        return answer;
    }

    public void deleteDiscountsTheProductIsPartOf(int productSerialNumber) {
        Collection<Discount> discountsToDelete = new LinkedList<>();
        for(Discount discount : storeDiscounts){
            if(discount.getIfYouBuyProductAndAmount().getKey() == productSerialNumber){
                discountsToDelete.add(discount);
            }
            else{
                for(Offer offer : discount.getOffers()){
                    if(offer.getProductSerialNumber() == productSerialNumber){
                        storeDiscounts.remove(discount);
                        break;
                    }
                }
            }
        }

        for(Discount discountToDelete : discountsToDelete){
            storeDiscounts.remove(discountToDelete);
        }
    }

    public boolean hasDiscountWithOneOfTheProducts(Collection<Pair<IDTOProductInStore, Float>> shoppingCart) {
        boolean answer = false;
        if(storeDiscounts != null) {
            for (Discount discount : storeDiscounts) {
                for (Pair<IDTOProductInStore, Float> product : shoppingCart) {
                    if (product.getKey().getProductSerialNumber() == discount.getIfYouBuyProductAndAmount().getKey()
                            &&
                            product.getValue() >= discount.getIfYouBuyProductAndAmount().getValue()) {
                        answer = true;
                        break;
                    }
                }
            }
        }

        return answer;
    }


}
