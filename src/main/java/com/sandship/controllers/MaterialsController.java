package com.sandship.controllers;

import com.sandship.DTO.Material;
import com.sandship.interfaces.MaterialsInterface;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MaterialsController extends DBConnection implements MaterialsInterface {
    @Override
    public void addMaterial(@NotNull Material material) {
        try(PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO Materials " +
                        "(name, quantity, type_id, warehouse_id)" +
                        "VALUES (?, ?, ?, ?)"))
        {
            statement.setString(1, material.getName());
            statement.setInt(2, material.getQuantity());
            statement.setInt(3, material.getType_id());
            statement.setInt(4, material.getWarehouse_id());
            statement.executeQuery();
        }catch (SQLException e) {
            System.out.println("Error occurred: " + e);
        }

    }

    @Override
    public void addMaterialCount(int count, @NotNull Material material) {
        material.setQuantity(material.getQuantity() + 1);
    }

    @Override
    public void deleteMaterial(@NotNull Material material) {
        try(PreparedStatement statement = connection.prepareStatement(
                "DELETE FROM Materials" +
                    " WHERE id = ?"))
        {
            statement.setInt(1, material.getId());
            statement.executeQuery();
        } catch (SQLException e) {
            System.out.println("Error occurred: " + e);
        }
    }

    @Override
    public String getMaterialType(@NotNull Material material) {
        int typeId = 0;
        try(PreparedStatement statement = connection.prepareStatement(
                "SELECT type_id " +
                    "FROM Materials " +
                    "WHERE id = ?"))
        {
            statement.setInt(1, material.getId());
            ResultSet resultSet = statement.executeQuery();

            typeId = resultSet.getInt("type_id");
        } catch (SQLException e) {
            System.out.println("Error occurred: " + e);
        }
        return materialType(typeId);
    }

    @Override
    public int getMaterialCount(@NotNull Material material) {
        return material.getQuantity();
    }

    @Override
    public String getName(@NotNull Material material) {
        return material.getName();
    }

    @Override
    public String getDescription(@NotNull Material material) {
        return material.getDescription();
    }

    @Override
    public String getIcon(@NotNull Material material) {
        String icon = "";
        try(PreparedStatement statement = connection.prepareStatement(
                "SELECT icon " +
                    "FROM MaterialTypes " +
                    "WHERE id = ?"))
        {
            statement.setInt(1, material.getType_id());
            ResultSet resultSet = statement.executeQuery();
            icon = resultSet.getString("icon");
        } catch (SQLException e) {
            System.out.println("Error occurred: " + e);
        }

        return icon;
    }

    @Override
    public int getCapacity(@NotNull Material material) {
        int capacity = 0;
        try(PreparedStatement statement = connection.prepareStatement(
                "SELECT capacity " +
                    "FROM Materials " +
                    "WHERE id = ?"))
        {
            statement.setInt(1, material.getId());
            ResultSet resultSet = statement.executeQuery();
            capacity = resultSet.getInt("capacity");
        } catch (SQLException e) {
            System.out.println("Error occurred: " + e);
        }
        return capacity;
    }

    @Contract(pure = true)
    private @NotNull String materialType(int n) {
        String type;
        type = switch (n) {
            case 1 -> "Copper Ore";
            case 2 -> "Iron Ore";
            case 3 -> "Carbon";
            case 4 -> "Silicon";
            default -> throw new IllegalStateException("Unexpected value: " + n);
        };

        return type;
    }
}