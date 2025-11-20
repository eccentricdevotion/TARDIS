package me.eccentric_nz.TARDIS.customblocks;

import me.eccentric_nz.TARDIS.custommodels.keys.*;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;

public enum TARDISBlockDisplayItem implements TARDISDisplayItem {
    // tardis blocks
    ADVANCED_CONSOLE(Whoniverse.ADVANCED_CONSOLE.getKey(), Material.JUKEBOX),
    ARTRON_CAPACITOR_STORAGE(Whoniverse.EYE_STORAGE.getKey(), Material.GRAY_SHULKER_BOX),
    ARTRON_FURNACE(Whoniverse.ARTRON_FURNACE.getKey(), Material.FURNACE),
    ARTRON_FURNACE_LIT(Whoniverse.ARTRON_FURNACE_LIT.getKey(), Material.FURNACE),
    BLUE_BOX(Wool.BLUE_BOX.getKey(), Material.BLUE_WOOL, Material.BLUE_DYE),
    COG(Wool.COG.getKey(), Material.GRAY_WOOL, Material.GRAY_DYE),
    DISK_STORAGE(Whoniverse.DISK_STORAGE.getKey(), Material.NOTE_BLOCK),
    TELEVISION(Whoniverse.TV.getKey(), Material.BROWN_STAINED_GLASS),
    HEXAGON(Wool.HEXAGON.getKey(), Material.ORANGE_WOOL, Material.ORANGE_DYE),
    ROUNDEL(Wool.ROUNDEL.getKey(), Material.WHITE_WOOL, Material.WHITE_DYE),
    ROUNDEL_OFFSET(Wool.ROUNDEL_OFFSET.getKey(), Material.WHITE_WOOL, Material.LIGHT_GRAY_DYE),
    PANDORICA(Whoniverse.PANDORICA.getKey(), Material.BLACK_CONCRETE),
    SIEGE_CUBE(Whoniverse.SIEGE_CUBE.getKey(), Material.CYAN_CONCRETE),
    THE_MOMENT(Wool.THE_MOMENT.getKey(), Material.BROWN_WOOL, Material.REDSTONE_BLOCK),
    UNTEMPERED_SCHISM(Schism.UNTEMPERED_SCHISM.getKey(), Material.ANCIENT_DEBRIS),
    DOOR(TardisDoorVariant.TARDIS_DOOR_CLOSED.getKey(), Material.IRON_DOOR, Material.IRON_DOOR),
    DOOR_OPEN(TardisDoorVariant.TARDIS_DOOR_OPEN.getKey(), Material.IRON_DOOR),
    DOOR_BOTH_OPEN(TardisDoorVariant.TARDIS_DOOR_EXTRA.getKey(), Material.IRON_DOOR),
    BONE_DOOR(BoneDoorVariant.BONE_DOOR_CLOSED.getKey(), Material.IRON_DOOR, Material.BIRCH_DOOR),
    BONE_DOOR_OPEN(BoneDoorVariant.BONE_DOOR_OPEN.getKey(), Material.BIRCH_DOOR),
    CLASSIC_DOOR(ClassicDoorVariant.CLASSIC_DOOR_CLOSED.getKey(), Material.IRON_DOOR, Material.CHERRY_DOOR),
    CLASSIC_DOOR_OPEN(ClassicDoorVariant.CLASSIC_DOOR_OPEN.getKey(), Material.CHERRY_DOOR),
    CUSTOM_DOOR(null, Material.IRON_DOOR),
    SONIC_DOCK(SonicItem.SONIC_DOCK_OFF.getKey(), Material.FLOWER_POT),
    SONIC_GENERATOR(SonicItem.SONIC_GENERATOR.getKey(), Material.FLOWER_POT),
    NONE(null, null);
    private final NamespacedKey customModel;
    private final Material material;
    private final Material craftMaterial;

    TARDISBlockDisplayItem(NamespacedKey customModel, Material item, Material craftMaterial) {
        this.customModel = customModel;
        this.material = item;
        this.craftMaterial = craftMaterial;
    }

    TARDISBlockDisplayItem(NamespacedKey customModel, Material item) {
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
            case UNTEMPERED_SCHISM -> {
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
            case UNTEMPERED_SCHISM -> {
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
        switch (this) {
            case DOOR, DOOR_OPEN, DOOR_BOTH_OPEN, BONE_DOOR, BONE_DOOR_OPEN, CLASSIC_DOOR, CLASSIC_DOOR_OPEN,
                 CUSTOM_DOOR -> {
                return true;
            }
            default -> {
                return false;
            }
        }
    }
}
