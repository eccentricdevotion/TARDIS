package me.eccentric_nz.TARDIS.customblocks;

import me.eccentric_nz.TARDIS.custommodels.keys.SeedBlock;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;

public enum TARDISSeedDisplayItem implements TARDISDisplayItem {
    // seed blocks
    ANCIENT(SeedBlock.ANCIENT.getKey(), Material.SCULK),
    ARS(SeedBlock.ARS.getKey(), Material.QUARTZ_BLOCK),
    BIGGER(SeedBlock.BIGGER.getKey(), Material.GOLD_BLOCK),
    BONE(SeedBlock.BONE.getKey(), Material.WAXED_OXIDIZED_CUT_COPPER),
    BUDGET(SeedBlock.BUDGET.getKey(), Material.IRON_BLOCK),
    CAVE(SeedBlock.CAVE.getKey(), Material.DRIPSTONE_BLOCK),
    COPPER(SeedBlock.COPPER.getKey(), Material.COPPER_BLOCK),
    CORAL(SeedBlock.CORAL.getKey(), Material.FIRE_CORAL_BLOCK),
    CURSED(SeedBlock.CURSED.getKey(), Material.BLACK_CONCRETE),
    DELTA(SeedBlock.DELTA.getKey(), Material.CRYING_OBSIDIAN),
    DELUXE(SeedBlock.DELUXE.getKey(), Material.DIAMOND_BLOCK),
    DIVISION(SeedBlock.DIVISION.getKey(), Material.PINK_GLAZED_TERRACOTTA),
    EIGHTH(SeedBlock.EIGHTH.getKey(), Material.CHISELED_STONE_BRICKS),
    ELEVENTH(SeedBlock.ELEVENTH.getKey(), Material.EMERALD_BLOCK),
    ENDER(SeedBlock.ENDER.getKey(), Material.PURPUR_BLOCK),
    FACTORY(SeedBlock.FACTORY.getKey(), Material.GRAY_CONCRETE),
    FIFTEENTH(SeedBlock.FIFTEENTH.getKey(), Material.OCHRE_FROGLIGHT),
    FUGITIVE(SeedBlock.FUGITIVE.getKey(), Material.POLISHED_DEEPSLATE),
    HOSPITAL(SeedBlock.HOSPITAL.getKey(), Material.WHITE_CONCRETE),
    MASTER(SeedBlock.MASTER.getKey(), Material.NETHER_BRICKS),
    MECHANICAL(SeedBlock.MECHANICAL.getKey(), Material.POLISHED_ANDESITE),
    ORIGINAL(SeedBlock.ORIGINAL.getKey(), Material.PACKED_MUD),
    PLANK(SeedBlock.PLANK.getKey(), Material.BOOKSHELF),
    PYRAMID(SeedBlock.PYRAMID.getKey(), Material.SANDSTONE_STAIRS),
    REDSTONE(SeedBlock.REDSTONE.getKey(), Material.REDSTONE_BLOCK),
    ROTOR(SeedBlock.ROTOR.getKey(), Material.HONEYCOMB_BLOCK),
    RUSTIC(SeedBlock.RUSTIC.getKey(), Material.COPPER_BULB),
    SIDRAT(SeedBlock.SIDRAT.getKey(), Material.GREEN_CONCRETE),
    STEAMPUNK(SeedBlock.STEAMPUNK.getKey(), Material.COAL_BLOCK),
    THIRTEENTH(SeedBlock.THIRTEENTH.getKey(), Material.HORN_CORAL_BLOCK),
    TOM(SeedBlock.TOM.getKey(), Material.LAPIS_BLOCK),
    TWELFTH(SeedBlock.TWELFTH.getKey(), Material.PRISMARINE),
    WAR(SeedBlock.WAR.getKey(), Material.WHITE_TERRACOTTA),
    WEATHERED(SeedBlock.WEATHERED.getKey(), Material.WEATHERED_COPPER),
    SMALL(SeedBlock.SMALL.getKey(), Material.COBBLESTONE),
    MEDIUM(SeedBlock.MEDIUM.getKey(), Material.COBBLESTONE),
    TALL(SeedBlock.TALL.getKey(), Material.COBBLESTONE),
    LEGACY_BIGGER(SeedBlock.LEGACY_BIGGER.getKey(), Material.ORANGE_GLAZED_TERRACOTTA),
    LEGACY_DELUXE(SeedBlock.LEGACY_DELUXE.getKey(), Material.LIME_GLAZED_TERRACOTTA),
    LEGACY_ELEVENTH(SeedBlock.LEGACY_ELEVENTH.getKey(), Material.CYAN_GLAZED_TERRACOTTA),
    LEGACY_REDSTONE(SeedBlock.LEGACY_REDSTONE.getKey(), Material.RED_GLAZED_TERRACOTTA),
    CUSTOM(SeedBlock.CUSTOM.getKey(), Material.POLISHED_BLACKSTONE),
    // growing seed block
    GROW(SeedBlock.GROW.getKey(), Material.LIGHT_GRAY_TERRACOTTA, Material.NETHERITE_BLOCK),
    ;

    private final NamespacedKey customModel;
    private final Material material;
    private final Material craftMaterial;

    TARDISSeedDisplayItem(NamespacedKey customModel, Material item) {
        this.customModel = customModel;
        this.material = item;
        this.craftMaterial = null;
    }

    TARDISSeedDisplayItem(NamespacedKey customModel, Material item, Material craftMaterial) {
        this.customModel = customModel;
        this.material = item;
        this.craftMaterial = craftMaterial;
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
        return false;
    }

    @Override
    public boolean isLit() {
        return false;
    }

    @Override
    public boolean isVariable() {
        return false;
    }

    @Override
    public boolean isSeed() {
        return true;
    }

    @Override
    public boolean isDoor() {
        return false;
    }

    @Override
    public boolean isClosedDoor() {
        return false;
    }

    @Override
    public boolean isPipe() {
        return false;
    }
}
