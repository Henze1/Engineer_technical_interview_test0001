package com.sandship.controllers;

import com.sandship.DTO.Material;
import com.sandship.DTO.Warehouse;
import com.sandship.interfaces.WarehouseInterface;
import org.jetbrains.annotations.NotNull;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class WarehouseController extends DBConnection implements WarehouseInterface {
    @Override
    public void createWarehouse(@NotNull Warehouse warehouse) {
        try(PreparedStatement statement = connection.prepareStatement(
                "insert into Warehouses " +
                    "(name, capacity) " +
                    "values (?, ?)"))
        {
            statement.setString(1, warehouse.getName());
            statement.setInt(2, warehouse.getCapacity());
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error occurred: " + e);
        }
    }

    @Override
    public void insertIntoWarehouse(long id, int count, long warehouseId) {
        int oldWarehouseId = getWarehouseId(id);
        int warehouseCapacity = getWarehouseCapacity(oldWarehouseId);

        MaterialsController materialsController = new MaterialsController();

        int amount = 0;
        try(PreparedStatement statement = connection.prepareStatement(
            "select quantity " +
                "from Materials " +
                "where warehouse_id = ?")
            )
        {
            statement.setInt(1, oldWarehouseId);
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
                        "INSERT INTO Materials (name, quantity, capacity, type_id, warehouse_id) " +
                            "VALUES (?, ?, ?, ?, ?) " +
                            "ON DUPLICATE KEY UPDATE " +
                            "name = VALUES(name), " +
                            "quantity = quantity + ?, " +
                            "capacity = VALUES(capacity), " +
                            "type_id = VALUES(type_id), " +
                            "warehouse_id = VALUES(warehouse_id)")
                ){
                    statement1.setString(1, material.getName());
                    statement1.setInt(2, count);
                    statement1.setInt(3, material.getCapacity());
                    statement1.setInt(4, material.getType_id());
                    statement1.setLong(5, warehouseId);
                    statement1.setInt(6, count);

                    statement1.execute();
                } catch (SQLException e) {
                    System.out.println("Error occurred: " + e);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error occurred: " + e);
        }
    }

    @Override
    public void deleteFromWarehouse(long id, int count) {
        int amount = 0;
        try(PreparedStatement statement = connection.prepareStatement(
            "select quantity " +
                "from Materials " +
                "where id = ?")
            )
        {
            statement.setLong(1, id);
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
    public void deleteWarehouse(long id) {
        try(PreparedStatement statement = connection.prepareStatement(
            "delete from Materials " +
                "where warehouse_id = ?")
            )
        {
            statement.setLong(1, id);
            statement.execute();
        } catch (SQLException e) {
            System.out.println("Error occurred: " + e);
        }

        try(PreparedStatement statement = connection.prepareStatement(
            "delete from Warehouses " +
                "where id = ?")
            )
        {
            statement.setLong(1, id);
            statement.execute();
        } catch (SQLException e) {
            System.out.println("Error occurred: " + e);
        }
    }

    @Override
    public Warehouse getWarehouse(long id) {
        Warehouse warehouse = new Warehouse();
        try(PreparedStatement statement = connection.prepareStatement(
            "select * " +
                "from Warehouses " +
                "where id = ?")
            )
        {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                warehouse.setId(resultSet.getInt("id"));
                warehouse.setName(resultSet.getString("name"));
                warehouse.setCapacity(resultSet.getInt("capacity"));
            }
        } catch (SQLException e) {
            System.out.println("Error occurred: " + e);
        }

        return warehouse;
    }

    @Override
    public List<Warehouse> getAllWarehouses() {
        List<Warehouse> warehouses = new ArrayList<>();
        try(Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("select * from Warehouses");

            while (resultSet.next()) {
                Warehouse warehouse = new Warehouse();
                warehouse.setId(resultSet.getInt("id"));
                warehouse.setName(resultSet.getString("name"));
                warehouse.setCapacity(resultSet.getInt("capacity"));
                warehouses.add(warehouse);
            }
        } catch (SQLException e) {
            System.out.println("Error occurred: " + e);
        }

        return warehouses;
    }

    public int getWarehouseId(long id) {
        int warehouseId = 0;

        try(PreparedStatement statement = connection.prepareStatement(
            "select warehouse_id " +
                "from Materials " +
                "where id = ?")
            )
        {
            statement.setInt(1, (int) id);
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

    @Override
    public int getQuantityInWarehouse(int id) {
        int quantity = 0;
        try(PreparedStatement statement = connection.prepareStatement(
            "select quantity " +
                "from Materials " +
                "where warehouse_id = ?")
            )
        {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                quantity += resultSet.getInt("quantity");
            }
        } catch (SQLException e) {
            System.out.println("Error occurred: " + e);
        }

        return quantity;
    }

    public boolean doesIdExist(long id) {
        boolean exists = false;
        try (PreparedStatement statement = connection.prepareStatement(
                "SELECT 1 FROM Warehouses WHERE id = ?")
        )
        {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            exists = resultSet.next();
        } catch (SQLException e) {
            System.out.println("Error occurred: " + e);
        }
        return !exists;
    }
}