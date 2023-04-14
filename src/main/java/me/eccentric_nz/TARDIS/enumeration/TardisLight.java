package me.eccentric_nz.TARDIS.enumeration;

import me.eccentric_nz.TARDIS.customblocks.TARDISDisplayItem;
import org.bukkit.Material;

public enum TardisLight {

    CLASSIC(TARDISDisplayItem.LIGHT_CLASSIC, TARDISDisplayItem.LIGHT_CLASSIC_ON),
    TENTH(TARDISDisplayItem.LIGHT_TENTH, TARDISDisplayItem.LIGHT_TENTH_ON),
    ELEVENTH(TARDISDisplayItem.LIGHT_ELEVENTH, TARDISDisplayItem.LIGHT_ELEVENTH_ON),
    TWELFTH(TARDISDisplayItem.LIGHT_TWELFTH, TARDISDisplayItem.LIGHT_TWELFTH_ON),
    THIRTEENTH(TARDISDisplayItem.LIGHT_THIRTEENTH, TARDISDisplayItem.LIGHT_THIRTEENTH_ON),
    LAMP(Material.REDSTONE_LAMP, TARDISDisplayItem.LIGHT_LAMP, TARDISDisplayItem.LIGHT_LAMP_ON),
    LANTERN(TARDISDisplayItem.LIGHT_LANTERN, TARDISDisplayItem.LIGHT_LANTERN_ON),
    WOOL(Material.BLACK_WOOL, TARDISDisplayItem.LIGHT_WOOL, TARDISDisplayItem.LIGHT_TENTH);

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
