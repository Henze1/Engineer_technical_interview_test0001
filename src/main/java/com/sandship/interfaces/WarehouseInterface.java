//Specifies methods for managing multiple warehouses owned by a player.
package com.sandship.interfaces;

import com.sandship.DTO.Material;
import com.sandship.DTO.Warehouse;

import java.util.List;

public interface WarehouseInterface {
    void createWarehouse(Warehouse warehouse);
    void updateWarehouse(List<Material> materials);
    void insertIntoWarehouse(int id, int count);
    void deleteFromWarehouse(int id, int count);
    void deleteWarehouse(Warehouse warehouse);
}