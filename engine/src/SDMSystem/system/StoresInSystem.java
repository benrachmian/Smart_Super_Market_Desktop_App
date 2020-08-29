package SDMSystem.system;

import SDMSystem.store.Store;
import SDMSystem.exceptions.*;
import SDMSystemDTO.store.DTOStore;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;


class StoresInSystem {

    private final Map<Integer, Store> storesInSystemBySerialNumber;
    private final Map<Point, Store> storesInSystemByLocation;

    public StoresInSystem() {
        storesInSystemBySerialNumber = new HashMap<>();
        storesInSystemByLocation = new HashMap<>();
    }

    public void addStoreToSystem(Store newStore, Point newStoreLocation){
        if (storesInSystemBySerialNumber.putIfAbsent(newStore.getSerialNumber(),newStore) !=null) {
            throw new ExistenceException(true,newStore.getSerialNumber(),"Store","System");
        }
        if (storesInSystemByLocation.putIfAbsent(newStoreLocation,newStore) != null){
            storesInSystemBySerialNumber.remove(newStore.getSerialNumber());
            throw new RuntimeException("There is already a store in that location!");
        }
    }

    public Map<Integer, DTOStore> getDTOStoresInSystemBySerialNumber() {
        Map<Integer, DTOStore> storesInSystemBySerialNumber = new HashMap<>();
        for(Store store : this.storesInSystemBySerialNumber.values()){
            DTOStore newDTOStore = store.createDTOStore();
            storesInSystemBySerialNumber.put(newDTOStore.getStoreSerialNumber(),newDTOStore);
        }
        return storesInSystemBySerialNumber;
    }

//    private DTOStore createDTOStoreFromStore(Store store) {
//        DTOStore newDTOStore = new DTOStore(
//                store.getDTOProductsInStore(),
//                store.getLocation(),
//                store.getPpk(),
//                store.getSerialNumber(),
//                store.getStoreName(),
//                store.getDTOOrdersFromStore(),
//                store.getTotalProfitFromDelivary());
//        return newDTOStore;
//    }

    public Map<Integer, Store> getStoresInSystemBySerialNumber() {
        return storesInSystemBySerialNumber;
    }

    public Map<Point, Store> getStoresInSystemByLocation() {
        return storesInSystemByLocation;
    }

    public Map<Point, DTOStore> getDTOStoresInSystemByLocation() {
        Map<Point, DTOStore> storesInSystemByLocation = new HashMap<>();
        for(Store store : this.storesInSystemByLocation.values()){
            DTOStore newDTOStore = store.createDTOStore();
            storesInSystemByLocation.put(newDTOStore.getStoreLocation(),newDTOStore);
        }
        return storesInSystemByLocation;
    }

    public Store getStoreInSystem(int storeSerialNumber) {
        return storesInSystemBySerialNumber.get(storeSerialNumber);
    }
}
