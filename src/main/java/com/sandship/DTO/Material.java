package com.sandship.DTO;

public class Material {
    private Long id;
    private String name;
    private int quantity;
    private int capacity;
    private int type_id;
    private int warehouse_id;

    public Material(Long id, String name, int quantity, int capacity, int type_id, int warehouse_id) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.capacity = capacity;
        this.type_id = type_id;
        this.warehouse_id = warehouse_id;
    }

    public Material() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getType_id() {
        return type_id;
    }

    public void setType_id(int type_id) {
        this.type_id = type_id;
    }

    public int getWarehouse_id() {
        return warehouse_id;
    }

    public void setWarehouse_id(int warehouse_id) {
        this.warehouse_id = warehouse_id;
    }
}
