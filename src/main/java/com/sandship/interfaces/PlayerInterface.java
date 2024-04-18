//Specifies methods for managing multiple warehouses owned by a player.
package com.sandship.interfaces;

import com.sandship.DTO.Warehouse;

public interface PlayerInterface {
    Warehouse createWarehouse(String name, int capacity);
    Warehouse updateWarehouse();
    Warehouse insertIntoWarehouse();
    void deleteWarehouse();
}