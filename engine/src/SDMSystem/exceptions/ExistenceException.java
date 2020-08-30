package SDMSystem.exceptions;

public class ExistenceException extends RuntimeException{

    private final int objectSerialNumber;
    private final String objectRelevance; //for example: "Store", "System", "Bag"
    private final String objectKind; //for example: "Store", "Product", "Car"
    boolean doesExist;
    private final String EXCEPTION_MESSAGE_EXIST = "%s %d already exists in the %s...";
    private final String EXCEPTION_MESSAGE_NOT_EXIST = "%s %d doesn't exist in the %s...";

    public ExistenceException(boolean doesExist ,int objectSerialNumber, String objectKind, String objectRelevance) {
        this.doesExist = doesExist;
        this.objectSerialNumber = objectSerialNumber;
        this.objectKind = objectKind;
        this.objectRelevance = objectRelevance;
    }

    public int getObjectSerialNumber(){
        return objectSerialNumber;
    }

    @Override
    public String getMessage() {
        String msg;
        if(doesExist){
            msg =  String.format(EXCEPTION_MESSAGE_EXIST,objectKind, objectSerialNumber, objectRelevance);
        }
        else {
            msg =  String.format(EXCEPTION_MESSAGE_NOT_EXIST,objectKind, objectSerialNumber, objectRelevance);
        }
        return msg;
    }
}
