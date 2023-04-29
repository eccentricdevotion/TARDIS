package me.eccentric_nz.TARDIS.enumeration;

import me.eccentric_nz.TARDIS.customblocks.TARDISDisplayItem;
import org.bukkit.Material;

public enum TardisLight {

    CLASSIC(TARDISDisplayItem.LIGHT_CLASSIC_ON, TARDISDisplayItem.LIGHT_CLASSIC),
    CLASSIC_OFFSET(TARDISDisplayItem.LIGHT_CLASSIC_OFFSET_ON, TARDISDisplayItem.LIGHT_CLASSIC_OFFSET),
    TENTH(Material.REDSTONE_LAMP, TARDISDisplayItem.LIGHT_TENTH_ON, TARDISDisplayItem.LIGHT_TENTH),
    ELEVENTH(TARDISDisplayItem.LIGHT_ELEVENTH_ON, TARDISDisplayItem.LIGHT_ELEVENTH),
    TWELFTH(TARDISDisplayItem.LIGHT_TWELFTH_ON, TARDISDisplayItem.LIGHT_TWELFTH),
    THIRTEENTH(TARDISDisplayItem.LIGHT_THIRTEENTH_ON, TARDISDisplayItem.LIGHT_THIRTEENTH),
    LAMP(Material.REDSTONE_LAMP, TARDISDisplayItem.LIGHT_LAMP_ON, TARDISDisplayItem.LIGHT_LAMP),
    LANTERN(TARDISDisplayItem.LIGHT_LANTERN_ON, TARDISDisplayItem.LIGHT_LANTERN),
    BLUE_LAMP(TARDISDisplayItem.BLUE_LAMP_ON, TARDISDisplayItem.BLUE_LAMP),
    GREEN_LAMP(TARDISDisplayItem.GREEN_LAMP_ON, TARDISDisplayItem.GREEN_LAMP),
    PURPLE_LAMP(TARDISDisplayItem.PURPLE_LAMP_ON, TARDISDisplayItem.PURPLE_LAMP),
    RED_LAMP(TARDISDisplayItem.RED_LAMP_ON, TARDISDisplayItem.RED_LAMP);

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

    public static TARDISDisplayItem getToggled(TARDISDisplayItem tdi) {
        for (TardisLight light : TardisLight.values()) {
            if (light.getOff() == tdi) {
                return light.getOn();
            }
            if (light.getOn() == tdi) {
                return light.getOff();
            }
        }
        return null;
    }

    public static TardisLight getFromDisplayItem(TARDISDisplayItem tdi) {
        String s = tdi.toString();
        return switch (tdi) {
            case RED_LAMP, GREEN_LAMP, PURPLE_LAMP, BLUE_LAMP -> TardisLight.valueOf(s);
            case RED_LAMP_ON, GREEN_LAMP_ON, PURPLE_LAMP_ON, BLUE_LAMP_ON -> TardisLight.valueOf(s.substring(0, s.length() - 3)); // remove _ON
            case LIGHT_CLASSIC, LIGHT_CLASSIC_OFFSET, LIGHT_TENTH, LIGHT_ELEVENTH, LIGHT_TWELFTH, LIGHT_THIRTEENTH, LIGHT_LAMP, LIGHT_LANTERN -> TardisLight.valueOf(s.substring(6)); // remove LIGHT_
            case LIGHT_CLASSIC_ON, LIGHT_CLASSIC_OFFSET_ON, LIGHT_TENTH_ON, LIGHT_ELEVENTH_ON, LIGHT_TWELFTH_ON, LIGHT_THIRTEENTH_ON, LIGHT_LAMP_ON, LIGHT_LANTERN_ON -> TardisLight.valueOf(s.substring(6, s.length() - 3));
            default -> TENTH;
        };
    }
}
