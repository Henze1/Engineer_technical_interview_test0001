//Specifies methods for managing multiple warehouses owned by a player.
package com.sendship.interfaces;

import com.sendship.Warehouse;

public interface PlayerInterface {
    Warehouse creatWarehouse(String name, int capacity);
    Warehouse updateWarehouse();
    Warehouse insertIntoWarehouse();
    void deleteWarehouse();
}