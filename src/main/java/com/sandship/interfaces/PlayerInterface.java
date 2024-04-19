//Specifies methods for managing multiple warehouses owned by a player.
package com.sandship.interfaces;

import com.sandship.DTO.Warehouse;

public interface PlayerInterface {
    void createWarehouse(Warehouse warehouse);
    Warehouse updateWarehouse();
    Warehouse insertIntoWarehouse();
    void deleteWarehouse();
}