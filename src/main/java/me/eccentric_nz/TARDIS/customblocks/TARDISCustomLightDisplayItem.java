package me.eccentric_nz.TARDIS.customblocks;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;

public class TARDISCustomLightDisplayItem implements TARDISDisplayItem {
    private final Material material;
    private final boolean on;

    public TARDISCustomLightDisplayItem(Material material, boolean on) {
        this.material = material;
        this.on = on;
    }

    @Override
    public NamespacedKey getCustomModel() {
        return null;
    }

    @Override
    public Material getMaterial() {
        return this.material;
    }

    @Override
    public Material getCraftMaterial() {
        return null;
    }

    @Override
    public boolean isLight() {
        return true;
    }

    @Override
    public boolean isLit() {
        return this.on;
    }

    @Override
    public boolean isVariable() {
        return false;
    }

    @Override
    public boolean isSeed() {
        return false;
    }

    @Override
    public boolean isDoor() {
        return false;
    }

    @Override
    public boolean isClosedDoor() {
        return false;
    }
}
