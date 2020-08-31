package SDMSystem.system;

import SDMSystem.customer.Customer;
import SDMSystem.exceptions.ExistenceException;
import SDMSystem.store.Store;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class CustomersInSystem {
    private Map<Integer, Customer> customersInSystemBySerialNumber;
    private Map<Point, Customer> customersInSystemByLocation;

    public CustomersInSystem() {
        customersInSystemByLocation = new HashMap<>();
        customersInSystemBySerialNumber = new HashMap<>();
    }

    public void addCustomerToSystem(Customer newCustomer, Point newCustomerLocation){
        if (customersInSystemBySerialNumber.putIfAbsent(newCustomer.getSerialNumber(),newCustomer) !=null) {
            throw new ExistenceException(true,newCustomer.getSerialNumber(),"Customer","system");
        }
        if (customersInSystemByLocation.putIfAbsent(newCustomerLocation,newCustomer) != null){
            customersInSystemBySerialNumber.remove(newCustomer.getSerialNumber());
            throw new RuntimeException("There is already a customer in that location!");
        }
    }

    public Map<Integer, Customer> getCustomersInSystemBySerialNumber() {
        return customersInSystemBySerialNumber;
    }

    public Map<Point, Customer> getCustomersInSystemByLocation() {
        return customersInSystemByLocation;
    }

    public Customer getCustomer(int customerSerialNumber) {
        return customersInSystemBySerialNumber.get(customerSerialNumber);
    }
}
