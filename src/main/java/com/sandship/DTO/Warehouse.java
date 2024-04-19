package com.sandship.DTO;

public class Warehouse {
    private int id;
    private String name;
    private int capacity;

    public Warehouse() {}

    public Warehouse(int id, String name, int capacity) {
        this.id = id;
        this.name = name;
        if (capacity < 20000) {
            capacity = 20000;
        } else if (capacity > 50000) {
            capacity = 50000;
        }
        this.capacity = capacity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        if (capacity < 20000) {
            capacity = 20000;
        } else if (capacity > 50000) {
            capacity = 50000;
        }
        this.capacity = capacity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
