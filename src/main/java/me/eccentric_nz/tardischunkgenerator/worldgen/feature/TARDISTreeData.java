package me.eccentric_nz.tardischunkgenerator.worldgen.feature;

import org.bukkit.Material;

public class TARDISTreeData {

    private final Material base;
    private final Material stem;
    private final Material hat;
    private final Material decor;
    private final boolean planted;

    public TARDISTreeData(Material base, Material stem, Material hat, Material decor, boolean planted) {
        this.base = base;
        this.stem = stem;
        this.hat = hat;
        this.decor = decor;
        this.planted = planted;
    }

    public Material getBase() {
        return base;
    }

    public Material getStem() {
        return stem;
    }

    public Material getHat() {
        return hat;
    }

    public Material getDecor() {
        return decor;
    }

    public boolean isPlanted() {
        return planted;
    }
}
