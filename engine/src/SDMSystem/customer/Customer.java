package SDMSystem.customer;


import SDMSystem.HasSerialNumber;

public class Customer implements HasSerialNumber<Integer> {

    private static int generatedSerialNumber = 1000;
    private final String customerName;
    private final int customerSerialNumber;

    public Customer(String customerName) {
        this.customerName = customerName;
        this.customerSerialNumber = generatedSerialNumber++;
    }

    public String getCustomerName() {
        return customerName;
    }


    @Override
    public Integer getSerialNumber() {
        return customerSerialNumber;
    }
}
