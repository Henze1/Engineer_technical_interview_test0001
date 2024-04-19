package com.sandship.controllers;

import com.sandship.DTO.Material;
import com.sandship.DTO.Warehouse;
import com.sandship.interfaces.WarehouseInterface;
import org.jetbrains.annotations.NotNull;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class WarehouseController extends DBConnection implements WarehouseInterface {


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
    public void updateWarehouse(List<Material> materials) {
//        TODO("Implement")
    }

    @Override
    public void insertIntoWarehouse(int id, int count) {
        int warehouseId = getWarehouseId(id);
        int warehouseCapacity = getWarehouseCapacity(warehouseId);

        MaterialsController materialsController = new MaterialsController();

        int amount = 0;
        try(PreparedStatement statement = connection.prepareStatement(
            "select quantity " +
                "from Materials " +
                "where warehouse_id = ?")
            )
        {
            statement.setInt(1, warehouseId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                amount += resultSet.getInt("quantity");
            }

            if (amount == 0) {
                System.out.println("There's nothing to add!");
            } else if ((count + amount) > warehouseCapacity){
                System.out.println("No empty space in Warehouse!");
            } else {
                Material material = materialsController.getMaterialById(id);
                try(PreparedStatement statement1 = connection.prepareStatement(
                    "insert into Materials (id, name, quantity, capacity, type_id, warehouse_id) " +
                        "values (?, ?, ?, ?, ?, ?) " +
                        "on duplicate key update name = values(name), quantity = values(quantity) + ?, capacity = values(capacity), type_id = values(type_id), warehouse_id = values(warehouse_id))")
                    )
                {
                    statement.setLong(1,material.getId());
                    statement.setString(2,material.getName());
                    statement.setInt(3,material.getQuantity());
                    statement.setInt(4,material.getCapacity());
                    statement.setInt(5,material.getType_id());
                    statement.setInt(6,material.getWarehouse_id());
                    statement.setInt(7,material.getQuantity());

                    statement.execute();
                } catch (SQLException e) {
                    System.out.println("Error occurred: " + e);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error occurred: " + e);
        }
    }

    @Override
    public void deleteFromWarehouse(int id, int count) {
        int amount = 0;
        try(PreparedStatement statement = connection.prepareStatement(
            "select quantity " +
                "from Materials " +
                "where id = ?")
            )
        {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                amount = resultSet.getInt("quantity");
            }
        } catch (SQLException e) {
            System.out.println("Error occurred: " + e);
        }

        if (amount > count) {
            try(PreparedStatement statement = connection.prepareStatement(
                "update Materials " +
                    "set quantity = ? " +
                    "where id = ?")
                )
            {
                statement.setInt(1, amount - count);
                statement.setLong(2, id);
                statement.execute();
            } catch (SQLException e) {
                System.out.println("Error occurred: " + e);
            }
        } else {
            try(PreparedStatement statement = connection.prepareStatement(
                "update Materials " +
                    "set quantity = ? " +
                    "where id = ?")
            )
            {
                statement.setInt(1, 0);
                statement.setLong(2, id);
                statement.execute();
            } catch (SQLException e) {
                System.out.println("Error occurred: " + e);
            }
        }
    }

    @Override
    public void deleteWarehouse(@NotNull Warehouse warehouse) {
        try(PreparedStatement statement = connection.prepareStatement(
            "delete from Materials " +
                "where warehouse_id = ?")
            )
        {
            statement.setInt(1, warehouse.getId());
            statement.execute();
        } catch (SQLException e) {
            System.out.println("Error occurred: " + e);
        }

        try(PreparedStatement statement = connection.prepareStatement(
            "delete form Warehouses " +
                "where id = ?")
            )
        {
            statement.setInt(1, warehouse.getId());
            statement.execute();
        } catch (SQLException e) {
            System.out.println("Error occurred: " + e);
        }
    }

    public int getWarehouseId(int id) {
        int warehouseId = 0;

        try(PreparedStatement statement = connection.prepareStatement(
            "select warehouse_id " +
                "from Materials " +
                "where id = ?")
            )
        {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                warehouseId = resultSet.getInt("warehouse_id");
            } else {
                System.out.println("Wrong ID!!");
            }
        } catch (SQLException e) {
            System.out.println("Error occurred: " + e);
        }

        return warehouseId;
    }

    public int getWarehouseCapacity(int id) {
        int capacity = 0;

        try(PreparedStatement statement = connection.prepareStatement(
            "select capacity " +
                "from Warehouses " +
                "where id  = ?")
            )
        {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                capacity = resultSet.getInt("capacity");
            } else {
                System.out.println("Wrong ID!!");
            }
        } catch (SQLException e) {
            System.out.println("Error occurred: " + e);
        }

        return capacity;
    }
}