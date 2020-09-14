package SDMSystem.customer;


import SDMSystem.HasSerialNumber;
import SDMSystem.location.LocationUtility;
import SDMSystem.location.Locationable;
import SDMSystem.order.Order;
import SDMSystemDTO.customer.DTOCustomer;
import SDMSystemDTO.order.DTOOrder;

import java.awt.*;
import java.util.Collection;
import java.util.LinkedList;

public class Customer implements HasSerialNumber<Integer>, Locationable {

    //private static int generatedSerialNumber = 1000;
    private final String customerName;
    private final int customerId;
    private final Point customerLocation;
    private Collection<Order> ordersMade;

    public Customer(String customerName, int customerId, Point customerLocation) {
        this.customerName = customerName;
        this.customerId = customerId;
        this.customerLocation = customerLocation;
        this.ordersMade = new LinkedList<>();
    }

    public String getCustomerName() {
        return customerName;
    }

    public Collection<Order> getOrdersMade() {
        return ordersMade;
    }

    @Override
    public Integer getSerialNumber() {
        return customerId;
    }

    @Override
    public Point getLocation() {
        return customerLocation;
    }

    @Override
    public float getDistanceFrom(Point target) {
        return LocationUtility.calcDistance(this.customerLocation,target);
    }

    public DTOCustomer createDTOCustomer(){
        Collection<DTOOrder> dtoOrders = null;
//        ordersMade.forEach(
//                (order) -> dtoOrders.add(order.createDTOOrderFromOrder())
        if(ordersMade != null) {
            dtoOrders = new LinkedList<>();
            for (Order order : ordersMade) {
                dtoOrders.add(order.createDTOOrderFromOrder());
            }
        }

        return new DTOCustomer(this.customerName,this.customerId,this.customerLocation, dtoOrders);
    }

    public void addOrder(Order newOrder) {
        ordersMade.add(newOrder);
    }
}
