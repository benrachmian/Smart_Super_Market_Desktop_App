package SDMSystemDTO.customer;


import java.awt.*;

public class DTOCustomer {
    private final String customerName;
    private final int customerSerialNumber;
    private final Point customerLocation;

    public DTOCustomer(String customerName, int customerSerialNumber, Point customerLocation) {
        this.customerName = customerName;
        this.customerSerialNumber = customerSerialNumber;
        this.customerLocation = customerLocation;
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
}


