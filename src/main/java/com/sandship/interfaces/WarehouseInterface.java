//Specifies methods for managing multiple warehouses owned by a player.
package com.sandship.interfaces;

import com.sandship.DTO.Warehouse;

import java.util.List;

public interface WarehouseInterface {
    void createWarehouse(Warehouse warehouse);
    void insertIntoWarehouse(long id, int count, long warehouseId);
    void deleteFromWarehouse(long id, int count);
    void deleteWarehouse(long id);
    Warehouse getWarehouse(long id);
    int getWarehouseCapacity(int id);
    int getQuantityInWarehouse(int id);
    List<Warehouse> getAllWarehouses();
}