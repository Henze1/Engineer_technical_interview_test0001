package com.sandship;

import com.sandship.DTO.Material;
import com.sandship.DTO.Warehouse;
import com.sandship.controllers.MaterialsController;
import com.sandship.controllers.WarehouseController;

public class Main {
    public static void main(String[] args) {
        MaterialsController materialsController = new MaterialsController();

        WarehouseController warehouseController = new WarehouseController();

        Material material = new Material(1L, "Bolt", 1, 2000, 1, 1);
//
//        Warehouse warehouse = new Warehouse(1, "Warehouse 1", 25000);
//
//        warehouseController.createWarehouse(warehouse);

//        materialsController.addMaterial(material);
//
        materialsController.addMaterialCount(30, material);

//        String description = materialsController.getTypeDescription(material);
//
//        System.out.println(description);
    }
}