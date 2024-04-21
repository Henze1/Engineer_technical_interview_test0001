//The Service class is responsible for running the program.
//It contains methods for managing multiple warehouses owned by a player.
//It also contains methods for transferring materials between warehouses.
//and for creating, updating, and deleting warehouses.
package com.sandship;

import com.sandship.DTO.Material;
import com.sandship.DTO.Warehouse;
import com.sandship.controllers.MaterialsController;
import com.sandship.controllers.WarehouseController;
import com.sandship.interfaces.ServiceInterface;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Service implements ServiceInterface {
    Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        Service service = new Service();
        service.service();
    }

    public void service() {
        do {
            System.out.println("1. Create new warehouse");
            System.out.println("2. Update warehouse");
            System.out.println("3. Transfer materials");
            System.out.println("4. Delete warehouse");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");
            try {
                String input = scanner.nextLine();
                int choice = Integer.parseInt(input);

                switch (choice) {
                    case 1:
                        createWarehouse();
                        break;
                    case 2:
                        updateWarehouse();
                        break;
                    case 3:
                        transferFromWarehouse();
                        break;
                    case 4:
                        deleteWarehouse();
                        break;
                    case 5:
                        return;
                    default:
                        System.out.println("Invalid choice. Please enter a number between 1 and 5.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        } while (true);
    }


    //Create new warehouse
    @Override
    public void createWarehouse() {
        WarehouseController controller = new WarehouseController();

        Warehouse warehouse = new Warehouse();
        System.out.print("Enter warehouse name: ");
        warehouse.setName(scanner.nextLine());

        int capacity = 0;
        boolean validInput = false;
        while (!validInput) {
            System.out.println("Choose warehouse capacity: 20000 - 50000 units");
            System.out.print("Enter warehouse capacity: ");
            try {
                capacity = scanner.nextInt();
                if (capacity >= 20000 && capacity <= 50000) {
                    validInput = true;
                } else {
                    System.out.println("Invalid capacity. Please enter a value between 20000 and 50000.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid integer value.");
                scanner.next();
            }
        }
        warehouse.setCapacity(capacity);

        controller.createWarehouse(warehouse);
    }
    //Update warehouse
    @Override
    public void updateWarehouse() {
        int option;
        try {
            System.out.println("1. Add material");
            System.out.println("2. Delete material");
            System.out.print("Choose option: ");
            option = scanner.nextInt();
            if (option == 1) {
                insertIntoWarehouse();
            } else if (option == 2) {
                deleteFromWarehouse();
            } else {
                System.out.println("Wrong option!");
                updateWarehouse();
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter a valid integer option.");
            scanner.next();
            updateWarehouse();
        }
    }

    //Insert material into warehouse
    @Override
    public void insertIntoWarehouse() {
        MaterialsController controller = new MaterialsController();
        WarehouseController warehouseController = new WarehouseController();

        Material material = new Material();

        material.setId(0L);

        try {
            System.out.print("Enter material name: ");
            material.setName(scanner.next());

            System.out.print("Enter material quantity: ");
            material.setQuantity(scanner.nextInt());

            System.out.print("Enter material capacity: ");
            material.setCapacity(scanner.nextInt());

            int typeId;
            do {
                System.out.println("Choose material type: \n1. Iron \n2. Carbon \n3. Copper \n4. Silicon");
                System.out.print("Enter material type id: ");
                typeId = scanner.nextInt();
            } while (typeId < 1 || typeId > 4);
            material.setType_id(typeId);

            int warehouseId;
            do {
                System.out.println("Choose warehouse:");
                List<Warehouse> warehouses = warehouseController.getAllWarehouses();
                System.out.println("| id | name | capacity |");
                for (Warehouse warehouse : warehouses) {
                    System.out.println("| " +
                            warehouse.getId() + " | "
                            + warehouse.getName() + " | "
                            + warehouse.getCapacity() + " |"
                    );
                }
                System.out.print("Enter warehouse id: ");
                warehouseId = scanner.nextInt();
            } while (warehouseId < 1 || warehouseId > 4);
            material.setWarehouse_id(warehouseId);

            if ((warehouseController.getQuantityInWarehouse(material.getWarehouse_id()) + material.getQuantity() < material.getCapacity())
                    && (warehouseController.getQuantityInWarehouse(material.getWarehouse_id()) + material.getQuantity() <= warehouseController.getWarehouseCapacity(material.getWarehouse_id()))) {
                controller.addMaterial(material);
            } else {
                System.out.println("Not enough space in warehouse");
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter a valid integer.");
            scanner.next();
            insertIntoWarehouse();
        }
    }

    //Delete material from warehouse
    @Override
    public void deleteFromWarehouse() {
        MaterialsController controller = new MaterialsController();
        WarehouseController warehouseController = new WarehouseController();

        List<Material> materials = controller.getAllMaterials();
        Material material = new Material();
        try {
            do {
                System.out.println("| id | name | quantity | capacity |");
                for (Material allMaterials : materials) {
                    System.out.println("| " +
                            allMaterials.getId() + " | "
                            + allMaterials.getName() + " | "
                            + allMaterials.getQuantity() + " | "
                            + allMaterials.getCapacity() + " |"
                    );
                }
                System.out.print("Choose the item id you want to delete: ");
                material.setId(scanner.nextLong());
            } while (controller.doesIdExist(material.getId()));

            Material material1 = controller.getMaterialById(material.getId());

            if (warehouseController.getQuantityInWarehouse(material1.getWarehouse_id()) - material1.getQuantity() >= 0) {
                warehouseController.deleteFromWarehouse(material1.getWarehouse_id(), material1.getQuantity());
            }

            controller.deleteMaterial(material.getId());
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter a valid item id.");
            scanner.next();
            deleteFromWarehouse();
        }
    }

    //Delete warehouse
    @Override
    public void deleteWarehouse() {
        WarehouseController warehouseController = new WarehouseController();

        Warehouse newWarehouse = new Warehouse();
        try {
            do {
                System.out.println("Choose warehouse:");
                List<Warehouse> warehouses = warehouseController.getAllWarehouses();
                System.out.println("| id | name | capacity |");
                for (Warehouse warehouse : warehouses) {
                    System.out.println("| " +
                            warehouse.getId() + " | "
                            + warehouse.getName() + " | "
                            + warehouse.getCapacity() + " |"
                    );
                }
                System.out.print("Enter warehouse id: ");
                newWarehouse.setId(scanner.nextInt());
            } while (warehouseController.doesIdExist(newWarehouse.getId()));

            if (warehouseController.getQuantityInWarehouse(newWarehouse.getId()) > 0) {
                System.out.println("Warehouse is not empty");
                System.out.println("Do you want to transfer from warehouse? 1 for 'yes', any other key for 'no': ");
                if (scanner.nextInt() == 1) {
                    transferFromWarehouse();
                } else {
                    warehouseController.deleteWarehouse(newWarehouse.getId());
                }
            } else {
                warehouseController.deleteWarehouse(newWarehouse.getId());
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter a valid integer warehouse id.");
            scanner.next();
            deleteWarehouse();
        }
    }

    //Transfer items from one warehouse to another
    @Override
    public void transferFromWarehouse() {
        WarehouseController warehouseController = new WarehouseController();
        MaterialsController materialsController = new MaterialsController();

        List<Warehouse> warehouses = warehouseController.getAllWarehouses();

        Warehouse warehouseToTransfer = new Warehouse();
        Warehouse warehouseFromTransfer = new Warehouse();

        try {
            do {
                System.out.println("Choose warehouse to move items from:");
                System.out.println("| id | name | capacity |");
                for (Warehouse warehouse1 : warehouses) {
                    System.out.println("| " +
                            warehouse1.getId() + " | "
                            + warehouse1.getName() + " | "
                            + warehouse1.getCapacity() + " |"
                    );
                }
                System.out.print("Enter warehouse id: ");
                warehouseFromTransfer.setId(scanner.nextInt());
            } while (warehouseController.doesIdExist(warehouseFromTransfer.getId()));

            do {
                System.out.println("Choose warehouse to move items to:");
                System.out.println("| id | name | capacity |");
                for (Warehouse warehouse1 : warehouses) {
                    System.out.println("| " +
                            warehouse1.getId() + " | "
                            + warehouse1.getName() + " | "
                            + warehouse1.getCapacity() + " |"
                    );
                }
                System.out.print("Enter warehouse id: ");
                warehouseToTransfer.setId(scanner.nextInt());
            } while (warehouseController.doesIdExist(warehouseToTransfer.getId()));

            warehouseToTransfer = warehouseController.getWarehouse(warehouseToTransfer.getId());
            warehouseFromTransfer = warehouseController.getWarehouse(warehouseFromTransfer.getId());

            Material material = new Material();

            do {
                System.out.println("Choose material:");
                List<Material> materials = materialsController.getAllMaterials();
                System.out.println("| id | name | quantity | capacity |");
                for (Material allMaterials : materials) {
                    System.out.println("| " +
                            allMaterials.getId() + " | "
                            + allMaterials.getName() + " | "
                            + allMaterials.getQuantity() + " | "
                            + allMaterials.getCapacity() + " |"
                    );
                }
                System.out.print("Enter material id: ");
                material.setId(scanner.nextLong());
            } while (materialsController.doesIdExist(material.getId()));

            material = materialsController.getMaterialById(material.getId());

            if ((warehouseController.getQuantityInWarehouse(warehouseFromTransfer.getId()) - material.getQuantity()) >= 0) {

                if (warehouseController.getQuantityInWarehouse(warehouseToTransfer.getId()) + material.getQuantity() <= warehouseToTransfer.getCapacity()) {
                    warehouseController.insertIntoWarehouse(material.getId(), material.getQuantity(), warehouseToTransfer.getId());
                    warehouseController.deleteFromWarehouse(material.getId(), material.getQuantity());
                } else {
                    System.out.println("No empty space in Warehouse!");
                }
            } else {
                System.out.println("Not enough items in warehouse to transfer");
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter a valid integer.");
            scanner.next();
            transferFromWarehouse();
        }
    }

}