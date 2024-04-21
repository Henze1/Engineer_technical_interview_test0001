//Specifies methods for accessing the material
//type, quantity, and modifying quantity.
//Defines methods for accessing properties of a material type
//such as name, description, icon, and capacity.
package com.sandship.interfaces;

import com.sandship.DTO.Material;

import java.util.List;

public interface MaterialsInterface {
    Material getMaterialById(long id);
    Material getMaterialByName(String name);
    List<Material> getAllMaterials();
    void addMaterial(Material material);
    void addMaterialCount(int count, Material material);
    void deleteMaterial(long id);
    int getMaterialQuantity(Material material);
    String getName(Material material);
    String getTypeDescription(Material material);
    String getIcon(Material material);
    int getCapacity(Material material);
}