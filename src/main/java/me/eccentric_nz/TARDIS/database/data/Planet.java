package me.eccentric_nz.TARDIS.database.data;

import org.bukkit.Material;

public class Planet implements Comparable<Planet> {

    private final String name;
    private final Material material;

    public Planet(String name, Material material) {
        this.name = name;
        this.material = material;
    }

    public String getName() {
        return name;
    }

    public Material getMaterial() {
        return material;
    }

    @Override
    public int compareTo(Planet o) {
        return getName().compareToIgnoreCase(o.getName());
    }
}
