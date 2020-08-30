package SDMSystem.customer;


import SDMSystem.HasSerialNumber;
import SDMSystem.location.LocationUtility;
import SDMSystem.location.Locationable;
import SDMSystemDTO.customer.DTOCustomer;

import java.awt.*;

public class Customer implements HasSerialNumber<Integer>, Locationable {

    //private static int generatedSerialNumber = 1000;
    private final String customerName;
    private final int customerId;
    private final Point customerLocation;

    public Customer(String customerName, int customerId, Point customerLocation) {
        this.customerName = customerName;
        this.customerId = customerId;
        this.customerLocation = customerLocation;
    }

    public String getCustomerName() {
        return customerName;
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
        return new DTOCustomer(this.customerName,this.customerId,this.customerLocation);
    }
}
