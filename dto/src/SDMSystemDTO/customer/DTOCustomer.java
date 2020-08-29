package SDMSystemDTO.customer;


public class DTOCustomer {
    private final String customerName;
    private final int customerSerialNumber;

    public DTOCustomer(String customerName, int customerSerialNumber) {
        this.customerName = customerName;
        this.customerSerialNumber = customerSerialNumber;
    }

    public String getCustomerName() {
        return customerName;
    }

    public int getCustomerSerialNumber() {
        return customerSerialNumber;
    }
}


