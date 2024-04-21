//This class contains methods for accessing the material
//type, quantity, and modifying everything in the database.

package com.sandship.controllers;

import com.sandship.DTO.Material;
import com.sandship.interfaces.MaterialsInterface;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class MaterialsController extends DBConnection implements MaterialsInterface {
    private ScheduledExecutorService executorService;

    @Override
    public Material getMaterialById(long id) {
        Material material = new Material();

        try(PreparedStatement statement = connection.prepareStatement(
                "select * " +
                    "from Materials " +
                    "where id = ?")
            )
        {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                material.setId(resultSet.getLong("id"));
                material.setName(resultSet.getString("name"));
                material.setCapacity(resultSet.getInt("capacity"));
                material.setQuantity(resultSet.getInt("quantity"));
                material.setType_id(resultSet.getInt("type_id"));
                material.setWarehouse_id(resultSet.getInt("warehouse_id"));
            }
        } catch (SQLException e) {
            System.out.println("Error occurred: " + e);
        }

        return material;
    }

    @Override
    public Material getMaterialByName(String name) {
        Material material = new Material();

        try(PreparedStatement statement = connection.prepareStatement(
                "select * " +
                    "from Materials " +
                    "where name = ?")
            )
        {
            statement.setString(1, name);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                material.setId(resultSet.getLong("id"));
                material.setName(resultSet.getString("name"));
                material.setCapacity(resultSet.getInt("capacity"));
                material.setQuantity(resultSet.getInt("quantity"));
                material.setType_id(resultSet.getInt("type_id"));
                material.setWarehouse_id(resultSet.getInt("warehouse_id"));
            }
        } catch (SQLException e) {
            System.out.println("Error occurred: " + e);
        }

        return material;
    }

    @Override
    public List<Material> getAllMaterials() {
        List<Material> materials = new ArrayList<>();

        try(Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("select * from Materials");

            while (resultSet.next()) {
                Material material = new Material();
                material.setId(resultSet.getLong("id"));
                material.setName(resultSet.getString("name"));
                material.setQuantity(resultSet.getInt("quantity"));
                material.setCapacity(resultSet.getInt("capacity"));
                material.setType_id(resultSet.getInt("type_id"));
                material.setWarehouse_id(resultSet.getInt("warehouse_id"));
                materials.add(material);
            }
        } catch (SQLException e) {
            System.out.println("Error occurred: " + e);
        }

        return materials;
    }

    @Override
    public void addMaterial(@NotNull Material material) {
        if (doesNameExist(material.getName()) && doesTypeIdExist(material.getType_id()) && doesWarehouseIdExist(material.getWarehouse_id())) {
            Material materialByName = getMaterialByName(material.getName());
            addMaterialCount(material.getQuantity(), materialByName);
        } else {
            try (PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO Materials " +
                            "(name, quantity, capacity, type_id, warehouse_id)" +
                            "VALUES (?, ?, ?, ?, ?)")
            ) {
                statement.setString(1, material.getName());
                statement.setInt(2, material.getQuantity());
                statement.setInt(3, material.getCapacity());
                statement.setInt(4, material.getType_id());
                statement.setInt(5, material.getWarehouse_id());
                statement.executeUpdate();
            } catch (SQLException e) {
                System.out.println("Error occurred: " + e);
            }
        }
    }

//    This method is special for adding material to warehouse and for me
//    as it works in a separate thread, and this is my first attempt in using it.
    @Override
    public void addMaterialCount(int count, @NotNull Material material) {
        executorService = Executors.newSingleThreadScheduledExecutor();

        int initialQuantity = getMaterialQuantity(material);
        count += initialQuantity;

        long materialId = material.getId();

        AtomicInteger quantity = new AtomicInteger(initialQuantity);
        int finalCount = count;

        AtomicBoolean finalCountReached = new AtomicBoolean(false);

        executorService.scheduleAtFixedRate(() -> {
            try (PreparedStatement statement = connection.prepareStatement(
                    "UPDATE Materials " +
                            "SET quantity = ? " +
                            "WHERE id = ?")) {
                statement.setInt(1, quantity.get());
                statement.setLong(2, materialId);
                statement.executeUpdate();
            } catch (SQLException e) {
                System.out.println("Error occurred: " + e);
            }
            int currentQuantity = quantity.incrementAndGet();
            if (currentQuantity > finalCount) {
                executorService.shutdown();
                finalCountReached.set(true);
            }

            System.out.println(displayMaterial(material));
        }, 0, 1, TimeUnit.SECONDS);

        while (!finalCountReached.get()) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.out.println("Interrupted while waiting for final count");
                Thread.currentThread().interrupt();
            }
        }
    }


    @Override
    public void deleteMaterial(long id) {
        try(PreparedStatement statement = connection.prepareStatement(
                "DELETE FROM Materials " +
                    "WHERE id = ?"))
        {
            statement.setLong(1, id);
            statement.execute();
        } catch (SQLException e) {
            System.out.println("Error occurred: " + e);
        }
    }

    @Override
    public int getMaterialQuantity(@NotNull Material material) {
        int initialQuantity = 0;
        try (PreparedStatement statement = connection.prepareStatement(
                "Select quantity " +
                        "from Materials " +
                        "where id = ?")
        )
        {
            statement.setLong(1, material.getId());
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                initialQuantity = resultSet.getInt("quantity");
            }
        } catch (SQLException e) {
            System.out.println("Error occurred: " + e);
        }
        return initialQuantity;
    }

    @Override
    public String getName(@NotNull Material material) {
        String name = "";

        try(PreparedStatement statement = connection.prepareStatement(
                "select name " +
                    "from Materials " +
                    "where id = ?")
        )
        {
            statement.setLong(1, material.getId());
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                name += resultSet.getString("name");
            } else {
                System.out.println("Nothing has been found!");
            }
        } catch (SQLException e) {
            System.out.println("Error occurred: " + e);
        }

        return name;
    }

    @Override
    public String getTypeDescription(@NotNull Material material) {
        long typeId = 0L;
        String description = "";

        try(PreparedStatement statement = connection.prepareStatement(
                "select type_id " +
                    "from Materials " +
                    "where id = ?")) {
            statement.setLong(1, material.getId());
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                typeId = resultSet.getLong("type_id");
            } else {
                System.out.println("Wrong type ID: " + material.getId());
                return "";
            }
        } catch (SQLException e) {
            System.out.println("Error occurred: " + e);
        }

        try(PreparedStatement statement = connection.prepareStatement(
                "select description " +
                    "from MaterialTypes " +
                    "where id = ?")) {
            statement.setLong(1, typeId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                description = resultSet.getString("description");
            } else {
                description += "No description found";
            }
        } catch (SQLException e) {
            System.out.println("Error occurred: " + e);
        }

        return description;
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
            if (resultSet.next()) {
                icon = resultSet.getString("icon");
            } else {
                System.out.println("Nothing has been found!");
            }
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
            statement.setLong(1, material.getId());
            ResultSet resultSet = statement.executeQuery();
            capacity = resultSet.getInt("capacity");
        } catch (SQLException e) {
            System.out.println("Error occurred: " + e);
        }
        return capacity;
    }

    public String displayMaterial(@NotNull Material material) {
        String values = null;
        try(PreparedStatement statement = connection.prepareStatement(
                "Select * " +
                    "from Materials " +
                    "where id = ?")
            )
        {
            statement.setLong(1, material.getId());
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                values = "| " + resultSet.getLong("id") + " | "
                + resultSet.getString("name") + " | "
                + resultSet.getInt("quantity") + " | "
                + resultSet.getInt("capacity") + " |";
            }
        } catch (SQLException e) {
            System.out.println("Error occurred: " + e);
        }

        return values;
    }

    @Contract(pure = true)
    private @NotNull String materialType(int n) {
        String type;
        type = switch (n) {
            case 1 -> "Iron Ore";
            case 2 -> "Carbon";
            case 3 -> "Copper Ore";
            case 4 -> "Silicon";
            default -> throw new IllegalStateException("Unexpected value: " + n);
        };

        return type;
    }

    public boolean doesIdExist(long id) {
        boolean exists = false;
        try (PreparedStatement statement = connection.prepareStatement(
                "SELECT 1 FROM Materials WHERE id = ?")
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

    public boolean doesNameExist(String name) {
        boolean exists = false;
        try (PreparedStatement statement = connection.prepareStatement(
                "SELECT 1 FROM Materials WHERE name = ?")
        )
        {
            statement.setString(1, name);
            ResultSet resultSet = statement.executeQuery();
            exists = resultSet.next();
        } catch (SQLException e) {
            System.out.println("Error occurred: " + e);
        }
        return exists;
    }

    public boolean doesTypeIdExist(int id) {
        boolean exists = false;
        try (PreparedStatement statement = connection.prepareStatement(
                "SELECT 1 FROM Materials WHERE type_id = ?")
        )
        {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            exists = resultSet.next();
        } catch (SQLException e) {
            System.out.println("Error occurred: " + e);
        }
        return exists;
    }

    public boolean doesWarehouseIdExist(int id) {
        boolean exists = false;
        try (PreparedStatement statement = connection.prepareStatement(
                "SELECT 1 FROM Materials WHERE warehouse_id = ?")
        )
        {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            exists = resultSet.next();
        } catch (SQLException e) {
            System.out.println("Error occurred: " + e);
        }
        return exists;
    }
}