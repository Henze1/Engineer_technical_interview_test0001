//Specifies methods for accessing the material
//type, quantity, and modifying quantity.
package com.sendship.interfaces;

public interface MaterialControllerInterface<T> {
    void addMaterial(T material);
    void addMaterialCount(int count, T material);
    void deleteMaterial();
    T getMaterialType(T material);
    int getMaterialCount(T material);
}