package me.eccentric_nz.TARDIS.enumeration;

import me.eccentric_nz.TARDIS.customblocks.TARDISDisplayItem;
import org.bukkit.Material;

public enum TardisLight {

    CLASSIC(TARDISDisplayItem.LIGHT_CLASSIC_ON, TARDISDisplayItem.LIGHT_CLASSIC),
    TENTH(Material.REDSTONE_LAMP, TARDISDisplayItem.LIGHT_TENTH_ON, TARDISDisplayItem.LIGHT_TENTH),
    ELEVENTH(TARDISDisplayItem.LIGHT_ELEVENTH_ON, TARDISDisplayItem.LIGHT_ELEVENTH),
    TWELFTH(TARDISDisplayItem.LIGHT_TWELFTH_ON, TARDISDisplayItem.LIGHT_TWELFTH),
    THIRTEENTH(TARDISDisplayItem.LIGHT_THIRTEENTH_ON, TARDISDisplayItem.LIGHT_THIRTEENTH),
    LAMP(Material.REDSTONE_LAMP, TARDISDisplayItem.LIGHT_LAMP_ON, TARDISDisplayItem.LIGHT_LAMP),
    LANTERN(TARDISDisplayItem.LIGHT_LANTERN_ON, TARDISDisplayItem.LIGHT_LANTERN),
    WOOL(Material.BLACK_WOOL, TARDISDisplayItem.LIGHT_TENTH_ON, TARDISDisplayItem.LIGHT_WOOL);

    private final Material material;
    private final TARDISDisplayItem on;
    private final TARDISDisplayItem off;

    TardisLight(TARDISDisplayItem on, TARDISDisplayItem off) {
        this.material = Material.SEA_LANTERN;
        this.on = on;
        this.off = off;
    }

    TardisLight(Material material, TARDISDisplayItem on, TARDISDisplayItem off) {
        this.material = material;
        this.on = on;
        this.off = off;
    }

    public Material getMaterial() {
        return material;
    }

    public TARDISDisplayItem getOn() {
        return on;
    }

    public TARDISDisplayItem getOff() {
        return off;
    }
}
