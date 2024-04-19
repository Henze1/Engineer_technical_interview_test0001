package com.sandship.controllers;

import com.sandship.DTO.Warehouse;
import com.sandship.interfaces.PlayerInterface;
import org.jetbrains.annotations.NotNull;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class WarehouseController extends DBConnection implements PlayerInterface {


    @Override
    public void createWarehouse(@NotNull Warehouse warehouse) {
        try(PreparedStatement statement = connection.prepareStatement(
                "insert into Warehouses " +
                    "(id, name, capacity) " +
                    "values (?, ?, ?)"))
        {
            statement.setInt(1, warehouse.getId());
            statement.setString(2, warehouse.getName());
            statement.setInt(3, warehouse.getCapacity());
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error occurred: " + e);
        }
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