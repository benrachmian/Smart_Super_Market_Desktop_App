package SDMSystemDTO.customer;


import SDMSystemDTO.order.DTOOrder;

import java.awt.*;
import java.util.Collection;

public class DTOCustomer {
    private final String customerName;
    private final int customerSerialNumber;
    private final Point customerLocation;
    private final Collection<DTOOrder> ordersMade;

    public DTOCustomer(String customerName, int customerSerialNumber, Point customerLocation, Collection<DTOOrder> ordersMade) {
        this.customerName = customerName;
        this.customerSerialNumber = customerSerialNumber;
        this.customerLocation = customerLocation;
        this.ordersMade = ordersMade;
    }

    public Point getCustomerLocation() {
        return customerLocation;
    }

    public String getCustomerLocationToString(){
        return "X: " + customerLocation.x + " Y: " + customerLocation.y;
    }

    public Collection<DTOOrder> getOrdersMade() {
        return ordersMade;
    }

    @Override
    public String toString() {
        return customerName + ", ID: " + customerSerialNumber;
    }

    public String getCustomerName() {
        return customerName;
    }

    public int getCustomerSerialNumber() {
        return customerSerialNumber;
    }

    public int getTotalOrders() {
        return ordersMade.size();
    }
}


