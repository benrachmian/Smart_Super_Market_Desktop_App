package SDMSystem.system;

import SDMSystem.customer.Customer;
import SDMSystem.discount.Discount;
import SDMSystem.location.LocationUtility;
import SDMSystem.product.IProductInStore;
import SDMSystem.product.ProductInDiscount;
import SDMSystemDTO.discount.DTODiscount;
import SDMSystemDTO.discount.DiscountKind;
import SDMSystem.discount.Offer;
import SDMSystem.location.Locationable;
import SDMSystem.order.DynamicOrder;
import SDMSystem.order.StaticOrder;
import SDMSystem.product.Product;
import SDMSystem.product.ProductInStore;
import SDMSystem.order.Order;
import SDMSystem.store.Store;
import SDMSystem.exceptions.*;
import SDMSystem.validation.*;
import SDMSystemDTO.customer.DTOCustomer;
import SDMSystemDTO.product.*;
import SDMSystemDTO.order.DTOOrder;
import SDMSystemDTO.store.DTOStore;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.util.Pair;
import xml.XMLHelper;
import xml.generated.*;

import javax.xml.bind.JAXBException;
import java.awt.*;
import java.io.*;
import java.time.LocalDate;
import java.util.*;
import java.util.List;

//Use it as Singleton
public class SDMSystem {
    public static final int MAX_COORDINATE = 50;
    public static final int MIN_COORDINATE = 1;
    //In order to create two different way to find a store - by serial number and by location
    private StoresInSystem storesInSystem;
    private CustomersInSystem customersInSystem;
    private Map<Integer,Product> productsInSystem;
    private Map<Integer, Order> ordersInSystem;
    private Map<Point, Locationable> customersAndStoresLocationMap;
    private Map<String,Discount> discountsInSystem;
    private static SDMSystem single_Instance = null;

    private SDMSystem() {
        storesInSystem = new StoresInSystem();
        productsInSystem = new HashMap<>();
        ordersInSystem = new HashMap<>();
        customersInSystem = new CustomersInSystem();
        customersAndStoresLocationMap = new HashMap<>();
        discountsInSystem = new HashMap<>();
    }

    public static SDMSystem getInstance(){
        if(single_Instance == null){
            single_Instance = new SDMSystem();
        }
        return single_Instance;
    }


    private void addProductToSystem(Product newProduct){
        //if already in system throws exception
        if(productsInSystem.putIfAbsent(newProduct.getSerialNumber(),newProduct) != null) {
            throw new ExistenceException(true,newProduct.getSerialNumber(),"Product","System");
        }
    }

    public Map<Point, Locationable> getCustomersAndStoresLocationMap() {
        return customersAndStoresLocationMap;
    }

    private void addStoreToSystem(Store newStore) {
        Point newStoreLocation = newStore.getStoreLocation();
        //if the store doesn't exist
        if(!checkIfLocationIsUnique(newStoreLocation)){
            throw new RuntimeException("There is already a store/customer in location " + LocationUtility.locationToString(newStore.getStoreLocation()) + " !");
        }
        else {
            storesInSystem.addStoreToSystem(newStore, newStoreLocation);
            customersAndStoresLocationMap.put(newStoreLocation,newStore);
        }
    }


    public void loadSystem(String filePath) throws FileNotFoundException, JAXBException {
        SuperDuperMarketDescriptor superDuperMarketDescriptor = XMLHelper.FromXmlFileToObject(filePath);
        StoresInSystem oldStoresInSystem = this.storesInSystem;
        Map<Integer, Product> oldProductsInSystem = this.productsInSystem;
        Map<Integer, Order> oldOrdersInSystem = this.ordersInSystem;
        CustomersInSystem oldCustomersInSystem = this.customersInSystem;
        Map<Point, Locationable> oldCustomersAndStoresLocationMap = this.customersAndStoresLocationMap;
        Map<String,Discount> oldDiscountsInSystem = this.discountsInSystem;
        storesInSystem = new StoresInSystem();
        productsInSystem = new HashMap<>();
        ordersInSystem = new HashMap<>();
        customersInSystem = new CustomersInSystem();
        customersAndStoresLocationMap = new HashMap<>();
        discountsInSystem = new HashMap<>();
        try {
            loadProducts(superDuperMarketDescriptor.getSDMItems());
            loadStores(superDuperMarketDescriptor.getSDMStores(), superDuperMarketDescriptor.getSDMItems());
            scanForProductsWithoutStore();
            loadCustomers(superDuperMarketDescriptor.getSDMCustomers());
        }
        catch (Exception e){
            storesInSystem = oldStoresInSystem;
            productsInSystem = oldProductsInSystem;
            ordersInSystem = oldOrdersInSystem;
            customersInSystem = oldCustomersInSystem;
            customersAndStoresLocationMap = oldCustomersAndStoresLocationMap;
            discountsInSystem = oldDiscountsInSystem;
            throw e;
        }
    }



    private void scanForProductsWithoutStore() {
        for(Product product : productsInSystem.values()){
            if(product.numberOfStoresSellingTheProduct() == 0){
                throw new RuntimeException("There are no stores selling product " + product.getSerialNumber());
            }
        }
    }

    private void loadCustomers(SDMCustomers sdmCustomers) {
        Customer loadedCustomer;
        List<SDMCustomer> sdmCustomersList = sdmCustomers.getSDMCustomer();
        for(SDMCustomer sdmCustomer : sdmCustomersList){
            loadedCustomer = new Customer(
                    sdmCustomer.getName(),
                    sdmCustomer.getId(),
                    getLoadedLocation(sdmCustomer.getLocation()));
            addCustomerToSystem(loadedCustomer);
        }
    }

    private void addCustomerToSystem(Customer loadedCustomer) {
        Point newCustomerLocation = loadedCustomer.getLocation();
        if(!checkIfLocationIsUnique(newCustomerLocation)){
            throw new RuntimeException("There is already a store/customer in location " + LocationUtility.locationToString(loadedCustomer.getLocation()) + " !");

        }
        else {
            customersInSystem.addCustomerToSystem(loadedCustomer, newCustomerLocation);
            customersAndStoresLocationMap.put(newCustomerLocation,loadedCustomer);
        }
    }

    private void loadStores(SDMStores sdmStores, SDMItems sdmItems) {
        Store loadedStore;
        List<SDMStore> sdmStoreList = sdmStores.getSDMStore();
        for (SDMStore sdmStore : sdmStoreList) {
            Point loadedStoreLocation = getLoadedLocation(sdmStore.getLocation());
            Collection<Discount> loadedDiscounts = getLoadedDiscounts(sdmStore.getSDMDiscounts(), sdmStore.getSDMPrices().getSDMSell(),sdmItems,sdmStore.getId());
            loadedStore = new Store(sdmStore.getId(),loadedStoreLocation,sdmStore.getDeliveryPpk(),sdmStore.getName(), loadedDiscounts);
            addStoreToSystem(loadedStore);
            List<SDMSell> sdmSellList = sdmStore.getSDMPrices().getSDMSell();
            loadProductsToStore(sdmSellList,loadedStore);
        }
    }


    private Collection<Discount> getLoadedDiscounts(SDMDiscounts sdmDiscounts, List<SDMSell> productsTheStoreSelling, SDMItems sdmItems, int storeId) {
        Collection<Discount> discounts = null;
        if(sdmDiscounts != null) {
            discounts = new LinkedList<>();
            for (SDMDiscount sdmDiscount : sdmDiscounts.getSDMDiscount()) {
               // checkIfProductInSystemAndThrowException(sdmDiscount.getIfYouBuy().getItemId());
               // checkIfProductInStoreAndThrowException(sdmDiscount.getIfYouBuy().getItemId(), storeSerialNumber);
                checkLoadedProductInDiscountAndThrowException(sdmDiscount.getIfYouBuy().getItemId(), productsTheStoreSelling, sdmDiscount.getName());
                Discount newDiscount = new Discount(
                        sdmDiscount.getName().trim(),
                        new Pair<>(sdmDiscount.getIfYouBuy().getItemId(), sdmDiscount.getIfYouBuy().getQuantity()),
                        getDiscountKind(sdmDiscount.getThenYouGet().getOperator()),
                        getOffers(sdmDiscount.getThenYouGet().getSDMOffer(),productsTheStoreSelling,sdmDiscount.getName()),
                        //getStoreFromStores(sdmStore.getId()).getProductFromStore(sdmDiscount.getIfYouBuy().getItemId()).getWayOfBuying()
                        getWayOfBuyingOfProductFromSDMStore(sdmItems,sdmDiscount.getIfYouBuy().getItemId()),
                        getProductFromSystem(sdmDiscount.getIfYouBuy().getItemId()).getProductName(),
                        storeId
                );
                discounts.add(newDiscount);
                discountsInSystem.put(newDiscount.getDiscountName(),newDiscount);
            }
        }

        return discounts;
    }

    private WayOfBuying getWayOfBuyingOfProductFromSDMStore(SDMItems sdmItems, int productId) {
        WayOfBuying answer = null;
        for(SDMItem product : sdmItems.getSDMItem()){
            if(product.getId() == productId){
                answer = getLoadedProductWayOfBuying(product.getPurchaseCategory());
            }
        }

        return answer;
    }

    private void checkLoadedProductInDiscountAndThrowException(int productSerialNumber, List<SDMSell> productsInStore, String discountName) {
        if(!productsInSystem.containsKey(productSerialNumber)){
            throw new RuntimeException(String.format("The product %d in the discount %s is not in the system!", productSerialNumber, discountName));
        }
            if(!isProductInSDMSellList(productSerialNumber, productsInStore)) {
                throw new RuntimeException(String.format("The product %d in the discount %s is not in the store!", productSerialNumber, discountName));
            }
    }

    private boolean isProductInSDMSellList(int productSerialNumber, List<SDMSell> productsInStore) {
        boolean res = false;
        for(SDMSell product : productsInStore){
            if(product.getItemId() == productSerialNumber){
                res = true;
                break;
            }
        }

        return res;
    }

//    private void checkIfProductInStoreAndThrowException(int productSerialNumber, int storeSerialNumber) {
//        Store storeSellingTheProduct = storesInSystem.getStoreInSystem(storeSerialNumber);
//        if(!storeSellingTheProduct.isAvailableInStore(productSerialNumber)){
//            throw new ExistenceException(false,productSerialNumber,"Product","store");
//        }
//    }

//    private void checkIfProductInSystemAndThrowException(int productSerialNumber) {
//        if(!productsInSystem.containsKey(productSerialNumber)){
//            throw new ExistenceException(false,productSerialNumber,"Product","system");
//        }
//    }

    private Collection<Offer> getOffers(List<SDMOffer> sdmOffers, List<SDMSell> productsTheStoreSelling, String discountName) {
        Collection<Offer> offers = new LinkedList<>();
        for(SDMOffer sdmOffer : sdmOffers){
            checkLoadedProductInDiscountAndThrowException(sdmOffer.getItemId(),productsTheStoreSelling, discountName);
            offers.add(new Offer(
                    sdmOffer.getItemId(),
                    sdmOffer.getQuantity(),
                    sdmOffer.getForAdditional(),
                    getProductFromSystem(sdmOffer.getItemId()).getProductName()));
        }

        return offers;
    }

    private DiscountKind getDiscountKind(String operator) {
        DiscountKind res;
        switch (operator){
            case ("ONE-OF"):
                res = DiscountKind.ONE_OF;
                break;

            case ("ALL-OR-NOTHING"):
                res = DiscountKind.ALL_OR_NOTHING;
                break;
            default:
                res = DiscountKind.IRRELEVANT;
                break;
        }

        return res;
    }

    private void loadProductsToStore(List<SDMSell> sdmSellList, Store loadedStore) {
        for(SDMSell sdmSell : sdmSellList){
            Product productToLoad = productsInSystem.get(sdmSell.getItemId());
            if(productToLoad == null){
                throw new ExistenceException(false,sdmSell.getItemId(),"Product", "System");
            }
            else if(sdmSell.getPrice() <=0){
                throw new RuntimeException(String.format("The price of products %d must be a positive number!",sdmSell.getItemId()));
            }
            loadedStore.addNewProductToStore(productToLoad,sdmSell.getPrice());
        }
    }

    private Point getLoadedLocation(Location location) {
        Point loadedLocation = new Point(location.getX(),location.getY());
        LocationValidation.checkLocationValidation2D(
                loadedLocation,
                MIN_COORDINATE,
                MAX_COORDINATE,
                MIN_COORDINATE,
                MAX_COORDINATE);
        return loadedLocation;
    }

    private void loadProducts(SDMItems sdmItems) {
        Product loadedProduct;
        List<SDMItem> sdmItemsList = sdmItems.getSDMItem();
        for(SDMItem sdmItem : sdmItemsList){
            WayOfBuying loadedProductWayOfBuying = getLoadedProductWayOfBuying(sdmItem.getPurchaseCategory());
            loadedProduct = new Product(sdmItem.getId(),sdmItem.getName(), loadedProductWayOfBuying);
            addProductToSystem(loadedProduct);
        }
    }

    private WayOfBuying getLoadedProductWayOfBuying(String purchaseCategory) {
        WayOfBuying res;
        switch (purchaseCategory.toLowerCase()){
            case ("quantity")   :
                res = WayOfBuying.BY_QUANTITY;
                break;
            case ("weight")     :
                res = WayOfBuying.BY_WEIGHT;
                break;
            default:
                throw new EnumConstantNotPresentException(WayOfBuying.class,purchaseCategory);
        }
        
        return res;
    }

    public Map<Integer, DTOStore> getStoresInSystemBySerialNumber() {
        return storesInSystem.getDTOStoresInSystemBySerialNumber();
    }

    public DTOStore getStoreFromStores(int storeSerialNumber){
        DTOStore res = null;
        Store store = storesInSystem.getStoresInSystemBySerialNumber().get(storeSerialNumber);
        if(store != null)
        {
            res = store.createDTOStore();
        }
        return res;
    }

    public Map<Point, DTOStore> getStoresInSystemByLocation(){
        return storesInSystem.getDTOStoresInSystemByLocation();
    }

    public Map<Integer, DTOProduct> getProductsInSystem() {
        Map<Integer, DTOProduct> dtoProductsInSystem = new HashMap<>();
        for(Product product : this.productsInSystem.values()){
            DTOProduct newDTOProduct = product.createDTOProduct();
            dtoProductsInSystem.put(newDTOProduct.getProductSerialNumber(),newDTOProduct);
        }
        return dtoProductsInSystem;
    }


    public DTOProductInStore getProductFromStore(int chosenProductSerialNumber, int chosenStoreSerialNumber) {
        DTOProductInStore dtoProductInStore = null;
        Store chosenStore = storesInSystem.getStoresInSystemBySerialNumber().get(chosenStoreSerialNumber);
        if(chosenStore!= null){
            ProductInStore chosenProductInStore = chosenStore.getProductInStore(chosenProductSerialNumber);
            dtoProductInStore = chosenProductInStore.createDTOProductInStore();
        }
        return dtoProductInStore;
    }

    public void makeNewStaticOrder(DTOStore chosenStore,
                                   LocalDate orderDate,
                                   float deliveryCost,
                                   Collection<Pair<IDTOProductInStore,Float>> dtoProductsInOrder,
                                   DTOCustomer whoOrdered) {
        Collection<Pair<IProductInStore,Float>> productsInOrder = createProductsInOrderCollectionFromDTO(dtoProductsInOrder);
        Store storeTheProductBelong = storesInSystem.getStoreInSystem(chosenStore.getStoreSerialNumber());
        Customer customerOrdered = customersInSystem.getCustomer(whoOrdered.getCustomerSerialNumber());
        Order newOrder = createdNewStaticOrderObjectAndUpdateAmountSoldInStore(orderDate,deliveryCost,productsInOrder,storeTheProductBelong, customerOrdered,dtoProductsInOrder);
        updateAmountSoldInSystemForEveryProductInOrder(productsInOrder);
        storeTheProductBelong.addOrder(newOrder,deliveryCost);
        customerOrdered.addOrder(newOrder);
        ordersInSystem.put(newOrder.getSerialNumber(),newOrder);
    }


    public void makeNewDynamicOrder(LocalDate orderDate,
                                    Map<Integer, Collection<Pair<IDTOProductInStore, Float>>> cheapestBasketDTO, DTOCustomer whoOrdered) {
        Collection<StaticOrder> subOrders = new LinkedList<>();
        Customer customerMakingTheOrder = customersInSystem.getCustomer(whoOrdered.getCustomerSerialNumber());
        float totalDeliveryCost;
        //int[0] = amount of products
        //int[1] = amount of products kinds
        int[] amountOfProductsAndKinds = new int[2];
        makeSubOrderFromEachStore(orderDate, customerMakingTheOrder.getLocation(), cheapestBasketDTO, subOrders,customerMakingTheOrder);
        totalDeliveryCost = calcTotalDeliveryCostInDynamicOrder(subOrders);
        Collection<Pair<IProductInStore,Float>> allProductsInOrder = getAllProductsFromSubOrdersAndAmountOfProductsAndKinds(subOrders,amountOfProductsAndKinds);
        Map<Integer, Collection<Pair<IProductInStore, Float>>> shoppingCartForOrder = createCheapestBasketFromCheapestBasketTO(cheapestBasketDTO);
        //Map<Integer,Store> storesSellingTheProducts = getStoresSellingTheProductsFromBasket(cheapestBasketDTO);
        Order dynamicOrder = new DynamicOrder(orderDate,
                allProductsInOrder,
                calcProductsCost(allProductsInOrder),
                totalDeliveryCost,
                amountOfProductsAndKinds[0],
                amountOfProductsAndKinds[1],
                subOrders,
                customerMakingTheOrder,
                shoppingCartForOrder,
                cheapestBasketDTO);
        //updateAmountsSoldOfProduct(allProductsInOrder);
        updateSubOrdersToTheirMainOrder((DynamicOrder)dynamicOrder);
        customerMakingTheOrder.addOrder(dynamicOrder);
        updateAmountSoldInSystemForEveryProductInOrder(allProductsInOrder);
        ordersInSystem.put(dynamicOrder.getSerialNumber(),dynamicOrder);
    }

    private Map<Integer, Collection<Pair<IProductInStore, Float>>> createCheapestBasketFromCheapestBasketTO(Map<Integer, Collection<Pair<IDTOProductInStore, Float>>> cheapestBasketDTO) {
        Map<Integer, Collection<Pair<IProductInStore, Float>>> cheapestBasket = new HashMap<>();
        Collection<Pair<IProductInStore, Float>> productsInStore;
        for(Integer storeId : cheapestBasketDTO.keySet()){
                productsInStore = new LinkedList<>();
            for(Pair<IDTOProductInStore,Float> dtoProduct : cheapestBasketDTO.get(storeId)){
                if(dtoProduct.getKey() instanceof  DTOProductInStore){
                    productsInStore.add(new Pair<IProductInStore,Float>(
                            storesInSystem.getStoreInSystem(storeId).getProductInStore(dtoProduct.getKey().getProductSerialNumber())
                            ,dtoProduct.getValue()));
                }
                //product in discount
                else{
                    productsInStore.add(
                            new Pair<IProductInStore,Float>(
                                    new ProductInDiscount(
                                            storesInSystem.getStoreInSystem(storeId).getProductInStore(dtoProduct.getKey().getProductSerialNumber()),
                                            dtoProduct.getKey().getPrice()),
                                    dtoProduct.getValue()));
                }
            }
            cheapestBasket.put(storeId,productsInStore);
        }

        return cheapestBasket;
    }

    private void updateSubOrdersToTheirMainOrder(DynamicOrder mainOrder) {
        for(StaticOrder subOrder : mainOrder.getSubOrders()){
            subOrder.setMainOrder(mainOrder);
        }
    }

//    private Map<Integer, Store> getStoresSellingTheProductsFromBasket(Map<Integer, Collection<Pair<Float, DTOProductInStore>>> cheapestBasketDTO) {
//        Map<Integer,Store> storesSellingTheProducts = new HashMap<>();
//        for(Integer storeSerialNumber : cheapestBasketDTO.keySet()){
//            storesSellingTheProducts.put(storeSerialNumber,storesInSystem.getStoreInSystem(storeSerialNumber));
//        }
//
//        return storesSellingTheProducts;
//    }

    private Collection<Pair<IProductInStore,Float>> getAllProductsFromSubOrdersAndAmountOfProductsAndKinds(Collection<StaticOrder> subOrders, int[] amountOfProductsAndKinds) {
        Collection<Pair<IProductInStore,Float>> allProductsInOrder = new LinkedList<>();
        amountOfProductsAndKinds[0] = amountOfProductsAndKinds[1] = 0;
        for(Order order : subOrders) {
            for(Pair<IProductInStore,Float> productInOrder : order.getProductsInOrder()){
                //update amount of products
                if(productInOrder.getKey().getWayOfBuying() == WayOfBuying.BY_QUANTITY){
                    amountOfProductsAndKinds[0] += productInOrder.getValue();
                }
                else{
                    amountOfProductsAndKinds[0]++;
                }
                amountOfProductsAndKinds[1]++; //update amount of products kinds
                allProductsInOrder.add(productInOrder);
            }
        }

        return allProductsInOrder;
    }

    public float calcProductsInOrderCost(Collection<Pair<IDTOProductInStore, Float>> productsInOrder) {
        float res = 0;
        for(Pair<IDTOProductInStore, Float> dtoProductInorder : productsInOrder){
            res += (dtoProductInorder.getKey().getPrice() * dtoProductInorder.getValue());
        }

        return res;
    }

    private float calcTotalDeliveryCostInDynamicOrder(Collection<StaticOrder> subOrders) {
        float totalDeliveryCost = 0;
        for(Order order : subOrders) {
            totalDeliveryCost += order.getDeliveryCost();
        }

        return totalDeliveryCost;
    }

    private void makeSubOrderFromEachStore(LocalDate orderDate,
                                           Point userLocation,
                                           Map<Integer, Collection<Pair<IDTOProductInStore, Float>>> cheapestBasketDTO,
                                           Collection<StaticOrder> subOrders, Customer whoOrdered) {
        StaticOrder subOrder;
        float deliveryCost;
        for(Integer storeSerialNumber : cheapestBasketDTO.keySet()){
            Store storeSellingTheProducts = storesInSystem.getStoreInSystem(storeSerialNumber);
            deliveryCost = getDeliveryCost(storeSerialNumber, userLocation);
//            subOrder = makeOrderAndAddToStore(storeSerialNumber,
//                    orderDate,
//                    deliveryCost,
//                    createProductsInOrderCollectionFromDTO(cheapestBasketDTO.get(storeSerialNumber)));
            subOrder = createdNewStaticOrderObjectAndUpdateAmountSoldInStore(orderDate,deliveryCost,createProductsInOrderCollectionFromDTO(cheapestBasketDTO.get(storeSerialNumber)), storeSellingTheProducts, whoOrdered,cheapestBasketDTO.get(storeSerialNumber));
            storeSellingTheProducts.addOrder(subOrder,deliveryCost);
            subOrders.add(subOrder);
        }
    }

    private StaticOrder createdNewStaticOrderObjectAndUpdateAmountSoldInStore(LocalDate orderDate,
                                                                              float deliveryCost,
                                                                              Collection<Pair<IProductInStore, Float>> productsInOrder,
                                                                              Store storeTheProductBelong, Customer whoOrdered,Collection<Pair<IDTOProductInStore,Float>> dtoProductsInOrder) {
        int amountOfProducts = 0, amountKindsOfProducts = 0;
        for(Pair<IProductInStore, Float> productInOrderAndAmount : productsInOrder){
            IProductInStore currProduct = productInOrderAndAmount.getKey();
            currProduct.increaseAmountSoldInStore(productInOrderAndAmount.getValue());
            //currProduct.increaseAmountSoldInAllStores(productInOrderAndAmount.getKey());
            amountKindsOfProducts++;
            if(productInOrderAndAmount.getKey().getWayOfBuying() == WayOfBuying.BY_QUANTITY){
                amountOfProducts += productInOrderAndAmount.getValue();
            }
            else{
                amountOfProducts++;
            }
        }
        float productsCost = calcProductsCost(productsInOrder);
        Map<Integer,Collection<Pair<IProductInStore, Float>>> shoppingCart = new HashMap<>();
        shoppingCart.put(storeTheProductBelong.getSerialNumber(),productsInOrder);

        Map<Integer,Collection<Pair<IDTOProductInStore,Float>>> shoppingCartAsDto = new HashMap<>();
        shoppingCartAsDto.put(storeTheProductBelong.getSerialNumber(),dtoProductsInOrder);

        return new StaticOrder(orderDate,
                productsInOrder,
                productsCost,
                deliveryCost,
                amountOfProducts,
                amountKindsOfProducts,
                storeTheProductBelong,
                whoOrdered,
                null,
                shoppingCart,
                shoppingCartAsDto);
    }

    private float calcProductsCost(Collection<Pair<IProductInStore, Float>> productsInOrder) {
        float totalCost = 0;
        for(Pair<IProductInStore,Float> productInOrder : productsInOrder){
            totalCost += productInOrder.getValue() * productInOrder.getKey().getPrice();
        }

        return totalCost;
    }
//לבדוק אם יש מוצר במבצע ואן כן לעשות אותו כזה
    private Collection<Pair<IProductInStore, Float>> createProductsInOrderCollectionFromDTO(Collection<Pair<IDTOProductInStore, Float>> dtoProductsInOrder) {
        Collection<Pair<IProductInStore,Float>> productsInOrder = new LinkedList<>();
        for(Pair<IDTOProductInStore, Float> dtoProductInOrder : dtoProductsInOrder){
            Store storeWithTheProduct = storesInSystem.getStoreInSystem(dtoProductInOrder.getKey().getStoreTheProductBelongsID());
            ProductInStore productInStore = storeWithTheProduct.getProductInStore(dtoProductInOrder.getKey().getProductSerialNumber());
            if(dtoProductInOrder.getKey() instanceof  DTOProductInDiscount){
                productsInOrder.add(new Pair<>(new ProductInDiscount(productInStore,dtoProductInOrder.getKey().getPrice()),dtoProductInOrder.getValue()));
            }
            else {
                Pair<IProductInStore, Float> newProductInOrder = new Pair<>(productInStore, dtoProductInOrder.getValue());
                productsInOrder.add(newProductInOrder);
            }
        }

        return productsInOrder;
    }


    private void updateAmountSoldInSystemForEveryProductInOrder(Collection<Pair<IProductInStore, Float>> productsInOrder) {
        for(Pair<IProductInStore, Float> productInOrder : productsInOrder)
        {
            Product productInSystem = productsInSystem.get(productInOrder.getKey().getSerialNumber());
            productInSystem.increaseAmountSoldInAllStores(productInOrder.getValue());
        }
    }

    public boolean isAvailableInStore(int storeSerialNumber, int productSerialNumber) {
        Store store = storesInSystem.getStoreInSystem(storeSerialNumber);
        return store.isAvailableInStore(productSerialNumber);
    }

    public float getProductPrice(int storeSerialNumber, int productSerialNumber) {
        Store store = storesInSystem.getStoreInSystem(storeSerialNumber);
        return store.getProductInStore(productSerialNumber).getPrice();
    }

    public float getAveragePriceOfProduct(int productSerialNumber) {
        return productsInSystem.get(productSerialNumber).averagePriceOfProduct();
    }

    public int getNumberOfStoresSellingProduct(int productSerialNumber) {
        return productsInSystem.get(productSerialNumber).numberOfStoresSellingTheProduct();
    }

    public float getDeliveryCost(int storeSerialNumber, Point locationFromTheUser) {
        Store store = storesInSystem.getStoreInSystem(storeSerialNumber);
        return store.getDeliveryCost(locationFromTheUser);
    }

    public float getDistanceFromStore(DTOStore chosenStore, Point userLocation) {
        Store store = storesInSystem.getStoreInSystem(chosenStore.getStoreSerialNumber());
        return store.getDistanceFrom(userLocation);
    }

    public Collection<DTOOrder> getAllOrders() {
        Collection<DTOOrder> dtoOrders = new LinkedList<>();
//        for(Store store : storesInSystem.getStoresInSystemBySerialNumber().values()){
//            for(DTOOrder order : store.getDTOOrdersFromStore()){
//                dtoOrders.add(order);
//            }
//        }
        for(Order order : ordersInSystem.values()){
            dtoOrders.add(order.createDTOOrderFromOrder());
        }

        return dtoOrders;
    }

    public Collection<DTOOrder> getOrdersFromStore(int storeSerialNumber) {
        Store store = storesInSystem.getStoreInSystem(storeSerialNumber);
        return store.getDTOOrdersFromStore();
    }

    public DTOProduct getProductFromSystem(int chosenProductSerialNumber) {
        DTOProduct dtoProduct = null;
        Product chosenProduct = productsInSystem.get(chosenProductSerialNumber);
        if(chosenProduct != null){
            dtoProduct = chosenProduct.createDTOProduct();
        }
        return dtoProduct;
    }

    public boolean checkIfLocationIsUnique(Point userLocation) {
//        Collection<Point> storesLocation = createStoresLocationCollection();
//        return LocationValidation.checkIfUniqueLocation(userLocation,storesLocation);
        return !customersAndStoresLocationMap.containsKey(userLocation);
    }

    private Collection<Point> createStoresLocationCollection() {

        return new LinkedList<>(storesInSystem.getStoresInSystemByLocation().keySet());
    }

    public void makeCheapestBasket(Map<Integer, Collection<Pair<IDTOProductInStore, Float>>> cheapestBasket, Collection<Pair<DTOProduct, Float>> productsInOrder) {
        //Map<Integer, Collection<Pair<IDTOProductInStore,Float>>> cheapestBasket = new HashMap<>();
        Collection <Pair<IDTOProductInStore,Float>> productsFromSameStore;
        int storeWithCheapestProductSerialNumber;
        for(Pair<DTOProduct, Float> dtoProductInOrder : productsInOrder){
            ProductInStore cheapestProduct = findCheapestProduct(dtoProductInOrder.getKey().getProductSerialNumber());
            DTOProductInStore cheapestProductAsDTO = cheapestProduct.createDTOProductInStore();
            storeWithCheapestProductSerialNumber = cheapestProduct.getStoreTheProductBelongs().getSerialNumber();
            //If there is already a product from this store in the basket
            if(cheapestBasket.containsKey(storeWithCheapestProductSerialNumber)){
                productsFromSameStore = cheapestBasket.get(storeWithCheapestProductSerialNumber);
                productsFromSameStore.add(new Pair(cheapestProductAsDTO, dtoProductInOrder.getValue()));
            }
            else{
                productsFromSameStore = new LinkedList<>();
                productsFromSameStore.add(new Pair(cheapestProductAsDTO, dtoProductInOrder.getValue()));
                cheapestBasket.put(storeWithCheapestProductSerialNumber,productsFromSameStore);
            }
        }
    }

    private ProductInStore findCheapestProduct(int productSerialNumber) {
        ProductInStore currCheapestProduct = null;
        for(Store store : storesInSystem.getStoresInSystemBySerialNumber().values()){
            if(store.isAvailableInStore(productSerialNumber)) {
                ProductInStore productInStore = store.getProductInStore(productSerialNumber);
                if(currCheapestProduct == null){
                    currCheapestProduct = productInStore;
                }
                else{
                    if(productInStore.getPrice() < currCheapestProduct.getPrice()){
                        currCheapestProduct = productInStore;
                    }
                }
            }
        }

        return currCheapestProduct;
    }


    public void deleteProductFromStore(DTOProductInStore chosenProductToDeleteDTO) {
        Store storeSellingTheProduct = storesInSystem.getStoreInSystem(chosenProductToDeleteDTO.getStoreTheProductBelongsID());
        // if the store selling only one product
        if(storeSellingTheProduct.getDTOProductsInStore().values().size() == 1){
            throw new RuntimeException(String.format("The product %s, ID: %d can't be deleted because it's the only product the store selling!",
                    chosenProductToDeleteDTO.getProductName(),chosenProductToDeleteDTO.getProductSerialNumber()));
        }
        ProductInStore productToDelete = storeSellingTheProduct.getProductInStore(chosenProductToDeleteDTO.getProductSerialNumber());
        //if more then one store selling the product
        if(productToDelete.getStoresSellingTheProduct().size() > 1){
            storeSellingTheProduct.deleteProduct(productToDelete);
        }
        else{
            throw new RuntimeException(String.format("The product %s, ID: %d can't be deleted because it's the only store selling the product!",
                    chosenProductToDeleteDTO.getProductName(), chosenProductToDeleteDTO.getProductSerialNumber()));
        }
    }

    public Map<Integer, DTOProduct> getProductsTheStoreDoesntSell(DTOStore storeToUpdate) {
        Store targetStore = storesInSystem.getStoreInSystem(storeToUpdate.getStoreSerialNumber());
        Map<Integer, DTOProduct> productsTheStoreDoesntSell = new HashMap<>();
        for(Product product : productsInSystem.values()){
            if(!targetStore.isAvailableInStore(product.getSerialNumber())){
                productsTheStoreDoesntSell.put(product.getSerialNumber(),product.createDTOProduct());
            }
        }
        if(productsTheStoreDoesntSell.isEmpty()){
            throw new RuntimeException("The store already selling every product possible!");
        }

        return productsTheStoreDoesntSell;
    }

    public void addProductToStore(DTOStore storeToUpdateDTO, DTOProduct productToAddDTO, float productPrice) {
        if(productPrice <= 0){
            throw new RuntimeException("The price must be a positive number!");
        }
        Store storeToUpdate = storesInSystem.getStoreInSystem(storeToUpdateDTO.getStoreSerialNumber());
        Product productToAdd = productsInSystem.get(productToAddDTO.getProductSerialNumber());
        storeToUpdate.addNewProductToStore(productToAdd,productPrice);
    }

    public void updateProductPrice(DTOStore storeToUpdateDTO, DTOProductInStore chosenProductToUpdateDTO, float newPrice) {
        Store storeToUpdate = storesInSystem.getStoreInSystem(storeToUpdateDTO.getStoreSerialNumber());
        storeToUpdate.updateProductPrice(chosenProductToUpdateDTO.getProductSerialNumber(),newPrice);
    }

    public void saveOrdersToFile(String filePath) throws IOException {
        try (ObjectOutputStream out =
                     new ObjectOutputStream(
                             new FileOutputStream(filePath))) {
            out.writeObject(ordersInSystem);
            out.flush();
        }
    }

    public void loadOrdersFromFile(String filePath) throws IOException, ClassNotFoundException {
        Map<Integer, Order> orders = getOrdersFromFile(filePath);
        addLoadedOrdersToSystem(orders);
    }

    private void addLoadedOrdersToSystem(Map<Integer, Order> orders) {
        for(Order order : orders.values()){
            order.generateNewSerialNumber();
            if(order instanceof StaticOrder){
                addLoadedStaticOrderToSystem((StaticOrder)order);
            }
            //else = instanceof Dynamic order. need to add every sub order to its store
            else{
                addLoadedDynamicOrderToSystem((DynamicOrder)order);
            }
            //ordersInSystem.put(order.getOrderSerialNumber(),order);
            //addOrderToSystem
            //makeNewStaticOrder();
//            addOrderWithoutSubOrdersToSystem(order,
//                    order.getStoresFromWhomTheOrderWasMade().g);
        }
    }

    private void addLoadedDynamicOrderToSystem(DynamicOrder order) {
        for(StaticOrder staticOrder : order.getSubOrders()){
            staticOrder.generateNewSerialNumber();
            addLoadedStaticOrderToItsStoreAndUpdateAmounts(staticOrder);
        }
        ordersInSystem.put(order.getSerialNumber(),order);
    }

    private void addLoadedStaticOrderToSystem(StaticOrder order) {
        addLoadedStaticOrderToItsStoreAndUpdateAmounts(order);
        ordersInSystem.put(order.getSerialNumber(),order);
    }

    private void addLoadedStaticOrderToItsStoreAndUpdateAmounts(StaticOrder order) {
        increaseProductsInLoadedOrderAmountSoldInStore(order.getStoreFromWhomTheOrderWasMade().getSerialNumber(),
                order.getProductsInOrder());
        updateAmountSoldInSystemForEveryProductInOrder(order.getProductsInOrder());
        storesInSystem.getStoreInSystem(order.getStoreFromWhomTheOrderWasMade().getSerialNumber()).addOrder(order, order.getDeliveryCost());
    }

    private void increaseProductsInLoadedOrderAmountSoldInStore(Integer storeSerialNumber, Collection<Pair<IProductInStore, Float>> productsInOrder) {
        Store storeSellingTheProduct = storesInSystem.getStoreInSystem(storeSerialNumber);
        for (Pair<IProductInStore, Float> loadedProductAndAmount : productsInOrder) {
            ProductInStore currProduct = storeSellingTheProduct.getProductInStore(loadedProductAndAmount.getKey().getSerialNumber());
            currProduct.increaseAmountSoldInStore(loadedProductAndAmount.getValue());
        }
    }

    private  Map<Integer,Order> getOrdersFromFile(String filePath) throws IOException, ClassNotFoundException {
        Map<Integer, Order> orders;
        try (ObjectInputStream in =
                     new ObjectInputStream(
                             new FileInputStream(filePath))) {
            orders = (Map<Integer, Order>) in.readObject();
        }

        return orders;
    }

    public int getNumOfOrders() {
        return ordersInSystem.size();
    }

    public Map<Integer,DTOCustomer> getCustomers() {
        Map<Integer,DTOCustomer> dtoCustomers = new HashMap<>();
        for(Customer customer : customersInSystem.getCustomersInSystemBySerialNumber().values()){
            dtoCustomers.put(customer.getSerialNumber(),customer.createDTOCustomer());
        }

        return dtoCustomers;
    }

    public float getOrdersProductCostAverage(int customerSerialNumber) {
        Customer customer = customersInSystem.getCustomer(customerSerialNumber);
        float sum = 0;
        for(Order order : customer.getOrdersMade()){
            sum += order.getProductsCost();
        }

        return sum / customer.getOrdersMade().size();
    }

    public float getOrdersDeliveryCostAverage(int customerSerialNumber) {
        Customer customer = customersInSystem.getCustomer(customerSerialNumber);
        float sum = 0;
        for(Order order : customer.getOrdersMade()){
            sum += order.getDeliveryCost();
        }

        return sum / customer.getOrdersMade().size();
    }

    public boolean isPartOfDiscount(int storeSerialNumber, int productSerialNumber) {
        return storesInSystem.getStoresInSystemBySerialNumber().get(storeSerialNumber).isProductPartOfDiscount(productSerialNumber);
    }


    public boolean isProductDeletable(int storeSerialNumber, int productSerialNumber, String[] reasonRef){
        boolean answer = true;
        Store storeSellingTheProduct = storesInSystem.getStoreInSystem(storeSerialNumber);
        ProductInStore productToDelete = storeSellingTheProduct.getProductInStore(productSerialNumber);

        if(storeSellingTheProduct.getDTOProductsInStore().values().size() == 1){
            reasonRef[0] = String.format("The product %s, ID: %d can't be deleted because it's the only product the store selling!",
                    productToDelete.getProductName(),productSerialNumber);
            answer = false;
        }

        //if not more then one store selling the product
        if(productToDelete.getStoresSellingTheProduct().size() <= 1){
            reasonRef[0] = String.format("The product %s, ID: %d can't be deleted because it's the only store selling the product!",
                    productToDelete.getProductName(),productSerialNumber);
            answer = false;
        }

        return answer;

    }

    public void deleteDiscountsTheProductIsPartOf(int storeSerialNumber, int productSerialNumber) {
        Store storeSellingTheProduct = storesInSystem.getStoreInSystem(storeSerialNumber);
        storeSellingTheProduct.deleteDiscountsTheProductIsPartOf(productSerialNumber);
    }

    public boolean storeHasDiscountWithOneOfTheProducts(int storeSerialNumber, Collection<Pair<IDTOProductInStore,Float>> shoppingCart) {
        Store storeSelling = storesInSystem.getStoreInSystem(storeSerialNumber);
        return storeSelling.hasDiscountWithOneOfTheProducts(shoppingCart);
    }

    public Collection<Pair<DTODiscount, Integer>> getDiscountsForProductFromDiscountsCollection(Pair<IDTOProductInStore, Float> productSold, Collection<DTODiscount> storeDiscounts, Collection<DTODiscount> discountsInOrder) {
        Collection<Pair<DTODiscount, Integer>> discountsForProduct = null;
        if(storeDiscounts != null) {
            discountsForProduct = new LinkedList<>();
            for (DTODiscount discount : storeDiscounts) {
                if (discount.getIfYouBuyProductAndAmount().getKey() == productSold.getKey().getProductSerialNumber()
                        && discount.getIfYouBuyProductAndAmount().getValue() <= productSold.getValue()) {
                    discountsForProduct.add(new Pair(
                            discount,
                            (int) (productSold.getValue() / discount.getIfYouBuyProductAndAmount().getValue())));
                    discountsInOrder.add(discount);
                }
            }
        }

        return discountsForProduct;
    }

    public WayOfBuying getProductWayOfBuying(int productSerialNumber) {
        return getProductsInSystem().get(productSerialNumber).getWayOfBuying();
    }

    public DTOProductInDiscount getProductInDiscount(String discountName, int productSerialNumber) {
        Discount discountChosen = discountsInSystem.get(discountName);
        ProductInStore originalProduct = storesInSystem.getStoreInSystem(discountChosen.getStoreWithThisDiscountSerialNumber()).getProductInStore(productSerialNumber);
        DTOProductInDiscount productFromDiscount = null;
        for(Offer product : discountChosen.getOffers()){
            if (product.getProductSerialNumber() == productSerialNumber){
                productFromDiscount = new DTOProductInDiscount(originalProduct.createDTOProductInStore(),product.getPricePerUnit());
                break;
            }
        }

        return productFromDiscount;
    }

    public DTOCustomer getCustomer(int customerSerialNumber) {
        return customersInSystem.getCustomer(customerSerialNumber).createDTOCustomer();
    }

    public int getMaxXCoordinate() {
        int maxX = 0;
        for(Point currPoint : customersAndStoresLocationMap.keySet()){
            if(currPoint.x > maxX){
                maxX = currPoint.x;
            }
        }

        return maxX;
    }

    public int getMaxYCoordinate() {
        int maxY = 0;
        for(Point currPoint : customersAndStoresLocationMap.keySet()){
            if(currPoint.y > maxY){
                maxY = currPoint.y;
            }
        }

        return maxY;
    }

    public boolean ifStoreInLocation(Point currPoint) {
        return customersAndStoresLocationMap.get(currPoint) instanceof Store;
    }

    public DTOStore getStoreInSystemByLocation(Point currPoint) {
        return storesInSystem.getStoresInSystemByLocation().get(currPoint).createDTOStore();
    }

    public DTOCustomer getCustomer(Point customerLocation) {
        return customersInSystem.getCustomersInSystemByLocation().get(customerLocation).createDTOCustomer();
    }

    public boolean isAvailableStoreId(SimpleIntegerProperty storeID) {
        return storesInSystem.getStoresInSystemBySerialNumber().get(storeID.get()) == null;
    }

    public boolean isAvailableLocation(int x, int y) {
        return customersAndStoresLocationMap.get(new Point(x,y)) == null;
    }

    public void addStoreToSystem(int storeID, String storeName, int x, int y, float ppk, Map<Integer, DTOProductInStore> productsInStore) {
        Store newStore = new Store(
                storeID,
                new Point(x,y),
                ppk,
                storeName,
                null
        );
        for(DTOProductInStore product : productsInStore.values()){
            newStore.addNewProductToStore(
                    productsInSystem.get(product.getSerialNumber()),
                    product.getPrice()
            );
        }

        addStoreToSystem(newStore);
    }

    public boolean isAvailableProductId(int productId) {
        return productsInSystem.get(productId) == null;
    }

    public void addNewProductToSystem(int productId, String productName, WayOfBuying productWayOfBuying, Map<Integer, Float> storesSellingTheProductAndPrice) {
        Product newProduct = new Product(productId,productName,productWayOfBuying);
        addProductToSystem(newProduct);
        for(Integer storeId : storesSellingTheProductAndPrice.keySet()){
            Store storeWouldSellTheProduct = storesInSystem.getStoreInSystem(storeId);
            storeWouldSellTheProduct.addNewProductToStore(newProduct,storesSellingTheProductAndPrice.get(storeId));
        }

    }
}
