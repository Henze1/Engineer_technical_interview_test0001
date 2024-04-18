package com.sandship.controllers;

import com.sandship.DTO.Warehouse;
import com.sandship.interfaces.PlayerInterface;

public class WarehouseController extends DBConnection implements PlayerInterface {


    @Override
    public Warehouse createWarehouse(String name, int capacity) {
        return null;
    }

    @Override
    public Warehouse updateWarehouse() {
        return null;
    }

    @Override
    public Warehouse insertIntoWarehouse() {
        return null;
    }

    @Override
    public void deleteWarehouse() {

    }
}