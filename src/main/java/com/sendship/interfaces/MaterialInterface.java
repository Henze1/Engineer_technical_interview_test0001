//Defines methods for accessing properties of a material type
//such as name, description, icon, and capacity.
package com.sendship.interfaces;

public interface MaterialInterface<T>{
    public final int MAX_CAP = 5000;
    String getName(T material);
    String getDescription(T material);
    String getIcon(T material);
    int getCapacity(T material);

}
