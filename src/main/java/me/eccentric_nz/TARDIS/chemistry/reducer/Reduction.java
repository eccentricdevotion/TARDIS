package me.eccentric_nz.TARDIS.chemistry.reducer;

import org.bukkit.Material;

public enum Reduction {

    Acacia_Log(Material.ACACIA_LOG, "Carbon:49-Hydrogen:6-Oxygen:44-Nitrogen:1"),
    Andesite(Material.ANDESITE, "Silicon:33-Oxygen:64-Oxygen:3"),
    Birch_Log(Material.BIRCH_LOG, "Carbon:49-Hydrogen:6-Oxygen:44-Nitrogen:1"),
    Charcoal(Material.CHARCOAL, "Carbon:7-Hydrogen:4-Oxygen:1"),
    Coal(Material.COAL, "Carbon:50-Hydrogen:6-Oxygen:43-Nitrogen:1"),
    Cobblestone(Material.COBBLESTONE, "Oxygen:47-Silicon:28-Aluminium:8-Iron:5-Calcium:4-Sodium:3-Potassium:3-Magnesium:2"),
    Dark_Oak_Log(Material.DARK_OAK_LOG, "Carbon:49-Hydrogen:6-Oxygen:44-Nitrogen:1"),
    Diamond(Material.DIAMOND, "Carbon:64"),
    Diorite(Material.DIORITE, "Silicon:33-Oxygen:64-Oxygen:3"),
    Dirt(Material.DIRT, "Silicon:23-Calcium:8-Phosphorus:3-Oxygen:42-Magnesium:6-Nitrogen:3-Iron:13-Zinc:2-Copper:2"),
    Emerald(Material.EMERALD, "Beryllium:25-Aluminum:15-Oxygem:40-Silicon:20"),
    End_Stone(Material.END_STONE, "Silicon:59-Carbon:13-Unknown:3"),
    Glowstone(Material.GLOWSTONE, "Argon:20-Boron:20-Krypton:20-Neon:20-Unknown:3"),
    Gold(Material.GOLD_INGOT, "Gold:64"),
    Granite(Material.GRANITE, "Silicon:33-Oxygen:64-Oxygen:3"),
    Grass(Material.GRASS_BLOCK, "Carbon:15-Oxygen:64-Oxygen:6-Nitrogen:8-Phosphorus:7"),
    Ice(Material.ICE, "Hydrogen:64-Hydrogen:3-Oxygen:33"),
    Iron(Material.IRON_INGOT, "Iron:64"),
    Jungle_Log(Material.JUNGLE_LOG, "Carbon:49-Hydrogen:6-Oxygen:44-Nitrogen:1"),
    Lapis_Lazuli(Material.LAPIS_LAZULI, "Sodium:13-Sulfur:8-Aluminum:13-Oxygem:53-Silicon:13"),
    Magma(Material.MAGMA_BLOCK, "Silicon:28-Oxygen:47-Aluminium:8-Iron:5-Magnesium:2-Calcium:4-Sodium:3-Potassium:3"),
    Netherrack(Material.NETHERRACK, "Silicon:64-Oxygen:18-Mercury:15-Unknown:3"),
    Oak_Log(Material.OAK_LOG, "Carbon:49-Hydrogen:6-Oxygen:44-Nitrogen:1"),
    Obsidian(Material.OBSIDIAN, "Silicon:33-Oxygen:64-Oxygen:3"),
    Red_Sand(Material.RED_SAND, "Silicon:33-Oxygen:64-Oxygen:3"),
    Redstone(Material.REDSTONE, "Carbon:31-Uranium:31-Unknown:3"),
    Sand(Material.SAND, "Silicon:33-Oxygen:64-Oxygen:3"),
    Snow(Material.SNOW_BLOCK, "Hydrogen:64-Hydrogen:3-Oxygen:33"),
    Spruce_Log(Material.SPRUCE_LOG, "Carbon:49-Hydrogen:6-Oxygen:44-Nitrogen:1"),
    Terracotta(Material.TERRACOTTA, "Silicon:64-Silicon:13-Aluminium:17-Iron:3-Magnesium:1-Calcium:1-Oxygen:1");

    private final Material material;
    private final String elements;

    Reduction(Material material, String elements) {
        this.material = material;
        this.elements = elements;
    }

    public Material getMaterial() {
        return material;
    }

    public String getElements() {
        return elements;
    }
}
