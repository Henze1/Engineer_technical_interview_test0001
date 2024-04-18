//Specifies methods for accessing the material
//type, quantity, and modifying quantity.
//Defines methods for accessing properties of a material type
//such as name, description, icon, and capacity.
package com.sandship.interfaces;

import com.sandship.DTO.Material;

public interface MaterialsInterface {
    public final int MAX_CAP = 5000;
    void addMaterial(Material material);
    void addMaterialCount(int count, Material material);
    void deleteMaterial(Material material);
    String getMaterialType(Material material);
    int getMaterialCount(Material material);
    String getName(Material material);
    String getDescription(Material material);
    String getIcon(Material material);
    int getCapacity(Material material);
}