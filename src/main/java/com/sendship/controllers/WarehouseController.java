package com.sendship.controllers;

import com.sendship.Warehouse;
import com.sendship.database.DBConnection;
import com.sendship.interfaces.PlayerInterface;

public class WarehouseController extends DBConnection implements PlayerInterface {


    @Override
    public Warehouse creatWarehouse(String name, int capacity) {
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