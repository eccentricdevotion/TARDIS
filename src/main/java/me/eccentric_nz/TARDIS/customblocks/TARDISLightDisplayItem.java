package me.eccentric_nz.TARDIS.customblocks;

import me.eccentric_nz.TARDIS.custommodels.keys.LampVariant;
import me.eccentric_nz.TARDIS.custommodels.keys.LanternVariant;
import me.eccentric_nz.TARDIS.custommodels.keys.LightVariant;
import me.eccentric_nz.TARDIS.custommodels.keys.Wool;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;

public enum TARDISLightDisplayItem implements TARDISDisplayItem {
    // lights off
    LIGHT_BULB(LightVariant.BULB.getKey(), Material.WAXED_COPPER_BULB, Material.COPPER_BULB),
    LIGHT_CLASSIC(Wool.CLASSIC.getKey(), Material.GRAY_WOOL, Material.TORCH),
    LIGHT_CLASSIC_OFFSET(Wool.CLASSIC_OFFSET.getKey(), Material.GRAY_WOOL, Material.SOUL_TORCH),
    LIGHT_TENTH(Wool.TENTH.getKey(), Material.GRAY_WOOL, Material.ORANGE_DYE),
    LIGHT_ELEVENTH(Wool.ELEVENTH.getKey(), Material.GRAY_WOOL, Material.YELLOW_DYE),
    LIGHT_TWELFTH(Wool.TWELFTH.getKey(), Material.GRAY_WOOL, Material.CYAN_DYE),
    LIGHT_THIRTEENTH(Wool.THIRTEENTH.getKey(), Material.GRAY_WOOL, Material.BLUE_DYE),
    LIGHT_LAMP(null, Material.REDSTONE_LAMP),
    LIGHT_LANTERN(null, Material.GRAY_WOOL),
    LIGHT_VARIABLE(LightVariant.OFF.getKey(), Material.GLASS),
    LIGHT_VARIABLE_BLUE(LightVariant.BLUE_OFF.getKey(), Material.GLASS),
    LIGHT_VARIABLE_GREEN(LightVariant.GREEN_OFF.getKey(), Material.GLASS),
    LIGHT_VARIABLE_ORANGE(LightVariant.ORANGE_OFF.getKey(), Material.GLASS),
    LIGHT_VARIABLE_PINK(LightVariant.PINK_OFF.getKey(), Material.GLASS),
    LIGHT_VARIABLE_PURPLE(LightVariant.PURPLE_OFF.getKey(), Material.GLASS),
    LIGHT_VARIABLE_YELLOW(LightVariant.YELLOW_OFF.getKey(), Material.GLASS),
    // lights on
    LIGHT_BULB_ON(LampVariant.BULB_ON.getKey(), Material.REDSTONE_LAMP),
    LIGHT_CLASSIC_ON(LanternVariant.CLASSIC_ON.getKey(), Material.SEA_LANTERN),
    LIGHT_CLASSIC_OFFSET_ON(LanternVariant.CLASSIC_OFFSET_ON.getKey(), Material.SEA_LANTERN),
    LIGHT_TENTH_ON(LampVariant.TENTH_ON.getKey(), Material.REDSTONE_LAMP),
    LIGHT_ELEVENTH_ON(LampVariant.ELEVENTH_ON.getKey(), Material.REDSTONE_LAMP),
    LIGHT_TWELFTH_ON(LanternVariant.TWELFTH_ON.getKey(), Material.SEA_LANTERN),
    LIGHT_THIRTEENTH_ON(LanternVariant.THIRTEENTH_ON.getKey(), Material.SEA_LANTERN),
    LIGHT_LAMP_ON(LampVariant.LAMP_ON.getKey(), Material.REDSTONE_LAMP),
    LIGHT_LANTERN_ON(null, Material.SEA_LANTERN),
    LIGHT_VARIABLE_ON(LightVariant.VARIABLE.getKey(), Material.GLASS),
    LIGHT_VARIABLE_BLUE_ON(LightVariant.BLUE.getKey(), Material.GLASS),
    LIGHT_VARIABLE_GREEN_ON(LightVariant.GREEN.getKey(), Material.GLASS),
    LIGHT_VARIABLE_ORANGE_ON(LightVariant.ORANGE.getKey(), Material.GLASS),
    LIGHT_VARIABLE_PINK_ON(LightVariant.PINK.getKey(), Material.GLASS),
    LIGHT_VARIABLE_PURPLE_ON(LightVariant.PURPLE.getKey(), Material.GLASS),
    LIGHT_VARIABLE_YELLOW_ON(LightVariant.YELLOW.getKey(), Material.GLASS),
    // lights cloister
    LIGHT_BULB_CLOISTER(LampVariant.BULB_CLOISTER.getKey(), Material.REDSTONE_LAMP),
    LIGHT_CLASSIC_CLOISTER(LanternVariant.CLASSIC_CLOISTER.getKey(), Material.SEA_LANTERN),
    LIGHT_CLASSIC_OFFSET_CLOISTER(LanternVariant.CLASSIC_OFFSET_CLOISTER.getKey(), Material.SEA_LANTERN),
    LIGHT_TENTH_CLOISTER(LampVariant.TENTH_CLOISTER.getKey(), Material.REDSTONE_LAMP),
    LIGHT_ELEVENTH_CLOISTER(LampVariant.ELEVENTH_CLOISTER.getKey(), Material.REDSTONE_LAMP),
    LIGHT_TWELFTH_CLOISTER(LanternVariant.TWELFTH_CLOISTER.getKey(), Material.SEA_LANTERN),
    LIGHT_THIRTEENTH_CLOISTER(LanternVariant.THIRTEENTH_CLOISTER.getKey(), Material.SEA_LANTERN),
    LIGHT_LAMP_CLOISTER(null, Material.REDSTONE_LAMP),
    LIGHT_LANTERN_CLOISTER(null, Material.SEA_LANTERN),
    LIGHT_VARIABLE_CLOISTER(LightVariant.CLOISTER.getKey(), Material.GLASS),
    // console lamp
    CONSOLE_LAMP(null, Material.GLASS, Material.REDSTONE_LAMP),
    ;
    private final NamespacedKey customModel;
    private final Material material;
    private final Material craftMaterial;

    TARDISLightDisplayItem(NamespacedKey customModel, Material item, Material craftMaterial) {
        this.customModel = customModel;
        this.material = item;
        this.craftMaterial = craftMaterial;
    }

    TARDISLightDisplayItem(NamespacedKey customModel, Material item) {
        this.customModel = customModel;
        this.material = item;
        this.craftMaterial = null;
    }

    @Override
    public NamespacedKey getCustomModel() {
        return customModel;
    }

    @Override
    public Material getMaterial() {
        return material;
    }

    @Override
    public Material getCraftMaterial() {
        return craftMaterial;
    }

    @Override
    public boolean isLight() {
        return true;
    }

    @Override
    public boolean isLit() {
        switch (this) {
            case LIGHT_BULB_ON, LIGHT_CLASSIC_ON, LIGHT_CLASSIC_OFFSET_ON, LIGHT_TENTH_ON, LIGHT_ELEVENTH_ON,
                 LIGHT_TWELFTH_ON, LIGHT_THIRTEENTH_ON, LIGHT_LAMP_ON, LIGHT_LANTERN_ON, LIGHT_VARIABLE_ON,
                 LIGHT_VARIABLE_BLUE_ON, LIGHT_VARIABLE_GREEN_ON, LIGHT_VARIABLE_ORANGE_ON, LIGHT_VARIABLE_PINK_ON,
                 LIGHT_VARIABLE_PURPLE_ON, LIGHT_VARIABLE_YELLOW_ON, CONSOLE_LAMP -> {
                return true;
            }
            default -> {
                return false;
            }
        }
    }

    @Override
    public boolean isVariable() {
        switch (this) {
            case LIGHT_VARIABLE_ON, LIGHT_VARIABLE_BLUE_ON, LIGHT_VARIABLE_GREEN_ON, LIGHT_VARIABLE_ORANGE_ON,
                 LIGHT_VARIABLE_PINK_ON, LIGHT_VARIABLE_PURPLE_ON, LIGHT_VARIABLE_YELLOW_ON, LIGHT_VARIABLE,
                 LIGHT_VARIABLE_BLUE, LIGHT_VARIABLE_GREEN, LIGHT_VARIABLE_ORANGE, LIGHT_VARIABLE_PINK,
                 LIGHT_VARIABLE_PURPLE, LIGHT_VARIABLE_YELLOW, LIGHT_VARIABLE_CLOISTER -> {
                return true;
            }
            default -> {
                return false;
            }
        }
    }

    @Override
    public boolean isSeed() {
        return false;
    }

    @Override
    public boolean isDoor() {
        return false;
    }
}
