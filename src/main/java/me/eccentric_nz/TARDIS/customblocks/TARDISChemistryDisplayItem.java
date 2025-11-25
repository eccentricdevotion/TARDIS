package me.eccentric_nz.TARDIS.customblocks;

import me.eccentric_nz.TARDIS.custommodels.keys.ChemistryEquipment;
import me.eccentric_nz.TARDIS.custommodels.keys.LampVariant;
import me.eccentric_nz.TARDIS.custommodels.keys.LanternVariant;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;

public enum TARDISChemistryDisplayItem implements TARDISDisplayItem {
    // chemistry lamps off
    BLUE_LAMP(LampVariant.BLUE_LAMP.getKey(), Material.REDSTONE_LAMP),
    GREEN_LAMP(LampVariant.GREEN_LAMP.getKey(), Material.REDSTONE_LAMP),
    PURPLE_LAMP(LampVariant.PURPLE_LAMP.getKey(), Material.REDSTONE_LAMP),
    RED_LAMP(LampVariant.RED_LAMP.getKey(), Material.REDSTONE_LAMP),
    // chemistry lamps on
    BLUE_LAMP_ON(LanternVariant.BLUE_LAMP_ON.getKey(), Material.SEA_LANTERN),
    GREEN_LAMP_ON(LanternVariant.GREEN_LAMP_ON.getKey(), Material.SEA_LANTERN),
    PURPLE_LAMP_ON(LanternVariant.PURPLE_LAMP_ON.getKey(), Material.SEA_LANTERN),
    RED_LAMP_ON(LanternVariant.RED_LAMP_ON.getKey(), Material.SEA_LANTERN),
    // chemistry gui blocks
    COMPOUND(ChemistryEquipment.COMPOUND.getKey(), Material.ORANGE_CONCRETE),
    CONSTRUCTOR(ChemistryEquipment.CONSTRUCTOR.getKey(), Material.LIGHT_BLUE_CONCRETE),
    CREATIVE(ChemistryEquipment.CREATIVE.getKey(), Material.LIGHT_GRAY_CONCRETE),
    LAB(ChemistryEquipment.LAB.getKey(), Material.YELLOW_CONCRETE),
    PRODUCT(ChemistryEquipment.PRODUCT.getKey(), Material.LIME_CONCRETE),
    REDUCER(ChemistryEquipment.REDUCER.getKey(), Material.MAGENTA_CONCRETE),
    HEAT_BLOCK(ChemistryEquipment.HEAT_BLOCK.getKey(), Material.RED_CONCRETE),
    ;

    private final NamespacedKey customModel;
    private final Material material;
    private final Material craftMaterial;

    TARDISChemistryDisplayItem(NamespacedKey customModel, Material item) {
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
        switch (this) {
            case BLUE_LAMP, GREEN_LAMP, PURPLE_LAMP, RED_LAMP, BLUE_LAMP_ON, GREEN_LAMP_ON, PURPLE_LAMP_ON,
                 RED_LAMP_ON -> {
                return true;
            }
            default -> {
                return false;
            }
        }
    }

    @Override
    public boolean isLit() {
        switch (this) {
            case BLUE_LAMP_ON, GREEN_LAMP_ON, PURPLE_LAMP_ON, RED_LAMP_ON -> {
                return true;
            }
            default -> {
                return false;
            }
        }
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
}
