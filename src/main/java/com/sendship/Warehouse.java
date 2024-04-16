package com.sendship;

public class Warehouse {
    private int id;
    private String name;
    private int MAX_CAP;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMAX_CAP() {
        return MAX_CAP;
    }

    public void setMAX_CAP(int MAX_CAP) {
        this.MAX_CAP = MAX_CAP;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
