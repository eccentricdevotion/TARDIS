package me.eccentric_nz.TARDIS.builders.interior;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.discovery.StructureVariant;
import me.eccentric_nz.TARDIS.discovery.WaterFeature;
import org.bukkit.Material;

import java.util.HashMap;

public class FadeVariants {

    private final HashMap<String, StructureVariant> lookup = new HashMap<>();

    public void init() {
        lookup.put("END_STONE", new StructureVariant(
                TARDIS.plugin.getBuildKeeper().getFade().getGradients().get(Material.END_STONE), // gradient
                Material.END_STONE, // base
                Material.CRYING_OBSIDIAN, // seed
                Material.END_STONE_BRICK_WALL, // wall
                WaterFeature.endFloor, // floor
                WaterFeature.endColumn, // column
                Material.IRON_CHAIN.createBlockData(), // hanging
                Material.IRON_CHAIN.createBlockData(), // light
                Material.END_ROD.createBlockData("[facing=down]"), // end
                Material.SPORE_BLOSSOM, // decor
                Material.WHITE_CARPET.createBlockData() // carpet
        ));
        lookup.put("DEAD_HORN_CORAL_BLOCK", new StructureVariant(
                TARDIS.plugin.getBuildKeeper().getFade().getGradients().get(Material.DEAD_HORN_CORAL_BLOCK),
                Material.DEAD_HORN_CORAL_BLOCK,
                Material.CRIMSON_HYPHAE,
                Material.MUD_BRICK_WALL,
                WaterFeature.netherFloor,
                WaterFeature.netherColumn,
                Material.WEEPING_VINES_PLANT.createBlockData(),
                Material.WEEPING_VINES_PLANT.createBlockData(),
                Material.SHROOMLIGHT.createBlockData(),
                Material.HANGING_ROOTS,
                Material.WARPED_ROOTS.createBlockData()
        ));
        lookup.put("COBBLESTONE", new StructureVariant(
                TARDIS.plugin.getBuildKeeper().getFade().getGradients().get(Material.COBBLESTONE),
                Material.COBBLESTONE,
                Material.WARPED_HYPHAE,
                Material.COBBLESTONE_WALL,
                WaterFeature.cobbleFloor,
                WaterFeature.cobbleColumn,
                Material.CAVE_VINES_PLANT.createBlockData(),
                Material.CAVE_VINES_PLANT.createBlockData("[berries=true]"),
                Material.CAVE_VINES.createBlockData(),
                Material.SPORE_BLOSSOM,
                Material.SCULK_VEIN.createBlockData("[down=true,up=false,east=false,north=false,south=false,west=false]")
        ));
        lookup.put("TUFF", new StructureVariant(
                TARDIS.plugin.getBuildKeeper().getFade().getGradients().get(Material.TUFF),
                Material.TUFF,
                Material.MUDDY_MANGROVE_ROOTS,
                Material.TUFF_WALL,
                WaterFeature.tuffFloor,
                WaterFeature.tuffColumn,
                Material.CAVE_VINES_PLANT.createBlockData(),
                Material.CAVE_VINES_PLANT.createBlockData("[berries=true]"),
                Material.CAVE_VINES.createBlockData(),
                Material.SPORE_BLOSSOM,
                Material.MOSS_CARPET.createBlockData()
        ));
        lookup.put("SMOOTH_SANDSTONE", new StructureVariant(
                TARDIS.plugin.getBuildKeeper().getFade().getGradients().get(Material.SMOOTH_SANDSTONE),
                Material.SMOOTH_SANDSTONE,
                Material.CHISELED_RESIN_BRICKS,
                Material.SANDSTONE_WALL,
                WaterFeature.getFadeFloor(Material.SMOOTH_SANDSTONE, Material.SMOOTH_RED_SANDSTONE, Material.YELLOW_TERRACOTTA),
                WaterFeature.tuffColumn,
                Material.WAXED_COPPER_CHAIN.createBlockData(),
                Material.WAXED_COPPER_CHAIN.createBlockData(),
                Material.WAXED_COPPER_LANTERN.createBlockData("[hanging=true]"),
                Material.HANGING_ROOTS,
                Material.ORANGE_CARPET.createBlockData()
                ));
        lookup.put("STRIPPED_PALE_OAK_WOOD", new StructureVariant(
                TARDIS.plugin.getBuildKeeper().getFade().getGradients().get(Material.STRIPPED_PALE_OAK_WOOD),
                Material.STRIPPED_PALE_OAK_WOOD,
                Material.CINNABAR,
                Material.DIORITE_WALL,
                WaterFeature.getFadeFloor(Material.STRIPPED_PALE_OAK_WOOD, Material.PACKED_MUD, Material.STRIPPED_POPLAR_WOOD),
                WaterFeature.tuffColumn,
                Material.WAXED_COPPER_CHAIN.createBlockData(),
                Material.WAXED_COPPER_CHAIN.createBlockData(),
                Material.WAXED_COPPER_LANTERN.createBlockData("[hanging=true]"),
                Material.PALE_HANGING_MOSS,
                Material.LIGHT_GRAY_CARPET.createBlockData()
                ));
        lookup.put("NETHERRACK", new StructureVariant(
                TARDIS.plugin.getBuildKeeper().getFade().getGradients().get(Material.NETHERRACK),
                Material.NETHERRACK,
                Material.PINK_GLAZED_TERRACOTTA,
                Material.CINNABAR_WALL,
                WaterFeature.getFadeFloor(Material.NETHERRACK, Material.PINK_CONCRETE, Material.STRIPPED_CRIMSON_HYPHAE),
                WaterFeature.tuffColumn,
                Material.IRON_CHAIN.createBlockData(),
                Material.IRON_CHAIN.createBlockData(),
                Material.LANTERN.createBlockData("[hanging=true]"),
                Material.SPORE_BLOSSOM,
                Material.PINK_CARPET.createBlockData()
                ));
        lookup.put("MOSSY_COBBLESTONE", new StructureVariant(
                TARDIS.plugin.getBuildKeeper().getFade().getGradients().get(Material.MOSSY_COBBLESTONE),
                Material.MOSSY_COBBLESTONE,
                Material.GREEN_GLAZED_TERRACOTTA,
                Material.MOSSY_COBBLESTONE_WALL,
                WaterFeature.getFadeFloor(Material.MOSSY_COBBLESTONE, Material.LIME_TERRACOTTA, Material.MOSS_BLOCK),
                WaterFeature.tuffColumn,
                Material.WAXED_WEATHERED_COPPER_CHAIN.createBlockData(),
                Material.WAXED_WEATHERED_COPPER_CHAIN.createBlockData(),
                Material.WAXED_WEATHERED_COPPER_LANTERN.createBlockData("[hanging=true]"),
                Material.SPORE_BLOSSOM,
                Material.GREEN_CARPET.createBlockData()
        ));
        lookup.put("WHITE_TERRACOTTA", new StructureVariant(
                TARDIS.plugin.getBuildKeeper().getFade().getGradients().get(Material.WHITE_TERRACOTTA),
                Material.WHITE_TERRACOTTA,
                Material.RED_GLAZED_TERRACOTTA,
                Material.GRANITE_WALL,
                WaterFeature.getFadeFloor(Material.WHITE_TERRACOTTA, Material.STRIPPED_MANGROVE_WOOD, Material.CINNABAR),
                WaterFeature.tuffColumn,
                Material.WAXED_EXPOSED_COPPER_CHAIN.createBlockData(),
                Material.WAXED_EXPOSED_COPPER_CHAIN.createBlockData(),
                Material.WAXED_EXPOSED_COPPER_LANTERN.createBlockData("[hanging=true]"),
                Material.SPORE_BLOSSOM,
                Material.RED_CARPET.createBlockData()
        ));
        lookup.put("QUARTZ_BLOCK", new StructureVariant(
                TARDIS.plugin.getBuildKeeper().getFade().getGradients().get(Material.QUARTZ_BLOCK),
                Material.QUARTZ_BLOCK,
                Material.GRAY_GLAZED_TERRACOTTA,
                Material.SANDSTONE_WALL,
                WaterFeature.getFadeFloor(Material.QUARTZ_BLOCK, Material.PALE_MOSS_BLOCK, Material.STRIPPED_POPLAR_WOOD),
                WaterFeature.tuffColumn,
                Material.IRON_CHAIN.createBlockData(),
                Material.IRON_CHAIN.createBlockData(),
                Material.SOUL_LANTERN.createBlockData("[hanging=true]"),
                Material.PALE_HANGING_MOSS,
                Material.PALE_MOSS_CARPET.createBlockData()
       ));
        lookup.put("PRISMARINE", new StructureVariant(
                TARDIS.plugin.getBuildKeeper().getFade().getGradients().get(Material.PRISMARINE),
                Material.PRISMARINE,
                Material.CYAN_GLAZED_TERRACOTTA,
                Material.PRISMARINE_WALL,
                WaterFeature.getFadeFloor(Material.PRISMARINE, Material.CYAN_CONCRETE, Material.STRIPPED_WARPED_HYPHAE),
                WaterFeature.tuffColumn,
                Material.WAXED_EXPOSED_COPPER_CHAIN.createBlockData(),
                Material.WAXED_EXPOSED_COPPER_CHAIN.createBlockData(),
                Material.WAXED_EXPOSED_COPPER_LANTERN.createBlockData("[hanging=true]"),
                Material.PALE_HANGING_MOSS,
                Material.CYAN_CARPET.createBlockData()
        ));
        lookup.put("CINNABAR", new StructureVariant(
                TARDIS.plugin.getBuildKeeper().getFade().getGradients().get(Material.CINNABAR),
                Material.CINNABAR,
                Material.BLACK_GLAZED_TERRACOTTA,
                Material.CINNABAR_WALL,
                WaterFeature.getFadeFloor(Material.CINNABAR, Material.CHERRY_WOOD, Material.SOUL_SAND),
                WaterFeature.tuffColumn,
                Material.WAXED_EXPOSED_COPPER_CHAIN.createBlockData(),
                Material.WAXED_EXPOSED_COPPER_CHAIN.createBlockData(),
                Material.WAXED_EXPOSED_COPPER_LANTERN.createBlockData("[hanging=true]"),
                Material.HANGING_ROOTS,
                Material.BROWN_CARPET.createBlockData()
        ));
    }

    public HashMap<String, StructureVariant> getLookup() {
        return lookup;
    }
}
