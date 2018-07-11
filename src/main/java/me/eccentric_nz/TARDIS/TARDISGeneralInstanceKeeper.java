/*
 * Copyright (C) 2018 eccentric_nz
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.TARDIS;

import me.eccentric_nz.TARDIS.commands.TARDISCommandHelper;
import me.eccentric_nz.TARDIS.commands.TARDISTravelCommands;
import me.eccentric_nz.TARDIS.commands.admin.TARDISAdminCommands;
import me.eccentric_nz.TARDIS.enumeration.PRESET;
import me.eccentric_nz.TARDIS.listeners.TARDISButtonListener;
import me.eccentric_nz.TARDIS.listeners.TARDISRenderRoomListener;
import me.eccentric_nz.TARDIS.listeners.TARDISScannerListener;
import me.eccentric_nz.TARDIS.move.TARDISDoorClickListener;
import me.eccentric_nz.TARDIS.move.TARDISDoorListener;
import me.eccentric_nz.TARDIS.rooms.TARDISCondenserData;
import me.eccentric_nz.TARDIS.sonic.TARDISSonicListener;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Keeps instances of various classes, maps and lists for easy access in other classes.
 *
 * @author eccentric_nz
 */
public class TARDISGeneralInstanceKeeper {

    private final HashSet<Material> transparent;
    private List<Block> doorPistons = new ArrayList<>();
    private List<Integer> npcIDs = new ArrayList<>();
    private List<String> quotes = new ArrayList<>();
    private final HashMap<String, String> sign_lookup;
    private TARDISAdminCommands tardisAdminCommand;
    private TARDISButtonListener buttonListener;
    private TARDISDoorListener doorListener;
    private TARDISRenderRoomListener rendererListener;
    private TARDISScannerListener scannerListener;
    private TARDISSonicListener sonicListener;
    private TARDISTravelCommands tardisTravelCommand;
    private final HashMap<String, Double[]> gravityEastList = new HashMap<>();
    private final HashMap<String, Double[]> gravityNorthList = new HashMap<>();
    private final HashMap<String, Double[]> gravitySouthList = new HashMap<>();
    private final HashMap<String, Double[]> gravityUpList = new HashMap<>();
    private final HashMap<String, Double[]> gravityWestList = new HashMap<>();
    private final HashMap<String, Integer> protectBlockMap = new HashMap<>();
    private final HashMap<UUID, TARDISCondenserData> roomCondenserData = new HashMap<>();
    private final List<Block> artronFurnaces = new ArrayList<>();
    private final List<BlockFace> faces = Arrays.asList(BlockFace.NORTH, BlockFace.WEST, BlockFace.SOUTH, BlockFace.EAST);
    private final List<BlockFace> surrounding = Arrays.asList(BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST, BlockFace.NORTH, BlockFace.NORTH_EAST, BlockFace.NORTH_WEST, BlockFace.SOUTH_EAST, BlockFace.SOUTH_WEST);
    private final List<Chunk> roomChunkList = new ArrayList<>();
    private final List<Chunk> tardisChunkList = new ArrayList<>();
    private final List<Chunk> railChunkList = new ArrayList<>();
    private final List<Location> rechargers = new ArrayList<>();
    private final List<Material> doors = Arrays.asList(Material.IRON_DOOR, Material.OAK_DOOR, Material.SPRUCE_DOOR, Material.BIRCH_DOOR, Material.ACACIA_DOOR, Material.JUNGLE_DOOR, Material.DARK_OAK_DOOR);
    private final List<Material> rails = Arrays.asList(Material.POWERED_RAIL, Material.RAIL, Material.DETECTOR_RAIL, Material.ACTIVATOR_RAIL);
    private final List<Material> goodNether = Arrays.asList(Material.NETHERRACK, Material.SOUL_SAND, Material.GLOWSTONE, Material.NETHER_BRICK, Material.NETHER_BRICK_FENCE, Material.NETHER_BRICK_STAIRS);
    private final List<Material> interactables;
    private final List<String> gravityDownList = new ArrayList<>();
    private final List<String> roomArgs;
    private final List<String> sonicLamps = new ArrayList<>();
    private final List<String> sonicPistons = new ArrayList<>();
    private final List<String> sonicRails = new ArrayList<>();
    private final List<String> sonicWires = new ArrayList<>();
    private final TARDIS plugin;
    private final YamlConfiguration pluginYAML;
    private long junkTime;
    private boolean junkTravelling = false;
    private Location junkDestination = null;
    private final List<UUID> junkTravellers = new ArrayList<>();

    public TARDISGeneralInstanceKeeper(TARDIS plugin) {
        this.plugin = plugin;
        roomArgs = buildRoomArgs();
        transparent = buildTransparent();
        doorListener = new TARDISDoorListener(plugin);
        interactables = buildInteractables();
        sign_lookup = buildSignLookup();
        setRechargers();
        InputStream is = plugin.getResource("plugin.yml");
        InputStreamReader reader = new InputStreamReader(is);
        pluginYAML = new YamlConfiguration();
        try {
            pluginYAML.load(reader);
        } catch (IOException | InvalidConfigurationException ex) {
            Logger.getLogger(TARDISCommandHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<String> getQuotes() {
        return quotes;
    }

    public void setQuotes(ArrayList<String> quotes) {
        this.quotes = quotes;
    }

    public List<Block> getArtronFurnaces() {
        return artronFurnaces;
    }

    public List<BlockFace> getFaces() {
        return faces;
    }

    public List<BlockFace> getSurrounding() {
        return surrounding;
    }

    public List<Chunk> getTardisChunkList() {
        return tardisChunkList;
    }

    public List<Chunk> getRoomChunkList() {
        return roomChunkList;
    }

    public List<Chunk> getRailChunkList() {
        return railChunkList;
    }

    public List<Material> getDoors() {
        return doors;
    }

    public List<Material> getRails() {
        return rails;
    }

    public List<Material> getGoodNether() {
        return goodNether;
    }

    public HashMap<String, Double[]> getGravityUpList() {
        return gravityUpList;
    }

    public List<String> getGravityDownList() {
        return gravityDownList;
    }

    public HashMap<String, Double[]> getGravityNorthList() {
        return gravityNorthList;
    }

    public HashMap<String, Double[]> getGravityWestList() {
        return gravityWestList;
    }

    public HashMap<String, Double[]> getGravitySouthList() {
        return gravitySouthList;
    }

    public HashMap<String, Double[]> getGravityEastList() {
        return gravityEastList;
    }

    public HashMap<String, Integer> getProtectBlockMap() {
        return protectBlockMap;
    }

    public HashMap<String, String> getSign_lookup() {
        return sign_lookup;
    }

    public HashMap<UUID, TARDISCondenserData> getRoomCondenserData() {
        return roomCondenserData;
    }

    public List<Integer> getNpcIDs() {
        return npcIDs;
    }

    public void setNpcIDs(List<Integer> npcIDs) {
        this.npcIDs = npcIDs;
    }

    public List<Block> getDoorPistons() {
        return doorPistons;
    }

    public void setDoorPistons(List<Block> doorPistons) {
        this.doorPistons = doorPistons;
    }

    public TARDISAdminCommands getTardisAdminCommand() {
        return tardisAdminCommand;
    }

    public void setTardisAdminCommand(TARDISAdminCommands tardisAdminCommand) {
        this.tardisAdminCommand = tardisAdminCommand;
    }

    public TARDISButtonListener getButtonListener() {
        return buttonListener;
    }

    public void setButtonListener(TARDISButtonListener buttonListener) {
        this.buttonListener = buttonListener;
    }

    public TARDISDoorListener getDoorListener() {
        return doorListener;
    }

    public void setDoorListener(TARDISDoorClickListener doorListener) {
        this.doorListener = doorListener;
    }

    public TARDISRenderRoomListener getRendererListener() {
        return rendererListener;
    }

    public void setRendererListener(TARDISRenderRoomListener rendererListener) {
        this.rendererListener = rendererListener;
    }

    public TARDISScannerListener getScannerListener() {
        return scannerListener;
    }

    public void setScannerListener(TARDISScannerListener scannerListener) {
        this.scannerListener = scannerListener;
    }

    public TARDISSonicListener getSonicListener() {
        return sonicListener;
    }

    public void setSonicListener(TARDISSonicListener sonicListener) {
        this.sonicListener = sonicListener;
    }

    public TARDISTravelCommands getTardisTravelCommand() {
        return tardisTravelCommand;
    }

    public void setTardisTravelCommand(TARDISTravelCommands tardisTravelCommand) {
        this.tardisTravelCommand = tardisTravelCommand;
    }

    public List<String> getRoomArgs() {
        return roomArgs;
    }

    public HashSet<Material> getTransparent() {
        return transparent;
    }

    public List<String> getSonicLamps() {
        return sonicLamps;
    }

    public List<String> getSonicPistons() {
        return sonicPistons;
    }

    public List<String> getSonicRails() {
        return sonicRails;
    }

    public List<String> getSonicWires() {
        return sonicWires;
    }

    public List<Location> getRechargers() {
        return rechargers;
    }

    public YamlConfiguration getPluginYAML() {
        return pluginYAML;
    }

    public long getJunkTime() {
        return junkTime;
    }

    public void setJunkTime(long junkTime) {
        this.junkTime = junkTime;
    }

    public boolean isJunkTravelling() {
        return junkTravelling;
    }

    public void setJunkTravelling(boolean junkTravelling) {
        this.junkTravelling = junkTravelling;
    }

    public Location getJunkDestination() {
        return junkDestination;
    }

    public void setJunkDestination(Location junkDestination) {
        this.junkDestination = junkDestination;
    }

    public List<UUID> getJunkTravellers() {
        return junkTravellers;
    }

    public List<Material> getInteractables() {
        return interactables;
    }

    private void setRechargers() {
        if (plugin.getConfig().isConfigurationSection("rechargers")) {
            Set<String> therechargers = plugin.getConfig().getConfigurationSection("rechargers").getKeys(false);
            therechargers.forEach((s) -> {
                World w = plugin.getServer().getWorld(plugin.getConfig().getString("rechargers." + s + ".world"));
                int x = plugin.getConfig().getInt("rechargers." + s + ".x");
                int y = plugin.getConfig().getInt("rechargers." + s + ".y");
                int z = plugin.getConfig().getInt("rechargers." + s + ".z");
                Location rc_loc = new Location(w, x, y, z);
                rechargers.add(rc_loc);
            });
        }
    }

    private HashSet<Material> buildTransparent() {
        HashSet<Material> trans = new HashSet<>();
        // add transparent blocks
        trans.add(Material.ACACIA_SAPLING);
        trans.add(Material.AIR);
        trans.add(Material.ALLIUM);
        trans.add(Material.AZURE_BLUET);
        trans.add(Material.BIRCH_SAPLING);
        trans.add(Material.BLUE_ORCHID);
        trans.add(Material.DARK_OAK_SAPLING);
        trans.add(Material.DEAD_BUSH);
        trans.add(Material.END_ROD);
        trans.add(Material.FERN);
        trans.add(Material.GRASS);
        trans.add(Material.IRON_BARS);
        trans.add(Material.JUNGLE_SAPLING);
        trans.add(Material.LARGE_FERN);
        trans.add(Material.LILAC);
        trans.add(Material.OAK_SAPLING);
        trans.add(Material.ORANGE_TULIP);
        trans.add(Material.OXEYE_DAISY);
        trans.add(Material.PEONY);
        trans.add(Material.PINK_TULIP);
        trans.add(Material.POPPY);
        trans.add(Material.REDSTONE_WIRE);
        trans.add(Material.RED_TULIP);
        trans.add(Material.ROSE_BUSH);
        trans.add(Material.SNOW);
        trans.add(Material.SPRUCE_SAPLING);
        trans.add(Material.SUNFLOWER);
        trans.add(Material.TALL_GRASS);
        trans.add(Material.VINE);
        trans.add(Material.WATER);
        trans.add(Material.WHITE_TULIP);
        return trans;
    }

    private List<String> buildRoomArgs() {
        List<String> rooms = new ArrayList<>();
        // rooms - only add if enabled in the config
        plugin.getRoomsConfig().getConfigurationSection("rooms").getKeys(false).forEach((r) -> {
            if (plugin.getRoomsConfig().getBoolean("rooms." + r + ".enabled")) {
                rooms.add(r);
            }
        });
        return rooms;
    }

    private List<Material> buildInteractables() {
        List<Material> list = new ArrayList<>();
        list.add(Material.ACACIA_BUTTON);
        list.add(Material.ACACIA_DOOR);
        list.add(Material.ACACIA_FENCE_GATE);
        list.add(Material.ACACIA_PRESSURE_PLATE);
        list.add(Material.ACACIA_TRAPDOOR);
        list.add(Material.ANVIL);
        list.add(Material.BEACON);
        list.add(Material.BIRCH_BUTTON);
        list.add(Material.BIRCH_DOOR);
        list.add(Material.BIRCH_FENCE_GATE);
        list.add(Material.BIRCH_PRESSURE_PLATE);
        list.add(Material.BIRCH_TRAPDOOR);
        list.add(Material.BLACK_BED);
        list.add(Material.BLACK_SHULKER_BOX);
        list.add(Material.BLUE_BED);
        list.add(Material.BLUE_SHULKER_BOX);
        list.add(Material.BROWN_BED);
        list.add(Material.BROWN_SHULKER_BOX);
        list.add(Material.CHEST);
        list.add(Material.COMPARATOR);
        list.add(Material.CRAFTING_TABLE);
        list.add(Material.CYAN_BED);
        list.add(Material.CYAN_SHULKER_BOX);
        list.add(Material.DARK_OAK_BUTTON);
        list.add(Material.DARK_OAK_DOOR);
        list.add(Material.DARK_OAK_FENCE_GATE);
        list.add(Material.DARK_OAK_PRESSURE_PLATE);
        list.add(Material.DARK_OAK_TRAPDOOR);
        list.add(Material.DISPENSER);
        list.add(Material.DROPPER);
        list.add(Material.ENDER_CHEST);
        list.add(Material.FURNACE);
        list.add(Material.GRAY_BED);
        list.add(Material.GRAY_SHULKER_BOX);
        list.add(Material.GREEN_BED);
        list.add(Material.GREEN_SHULKER_BOX);
        list.add(Material.HEAVY_WEIGHTED_PRESSURE_PLATE);
        list.add(Material.HOPPER);
        list.add(Material.IRON_DOOR);
        list.add(Material.IRON_TRAPDOOR);
        list.add(Material.JUKEBOX);
        list.add(Material.JUNGLE_BUTTON);
        list.add(Material.JUNGLE_DOOR);
        list.add(Material.JUNGLE_FENCE_GATE);
        list.add(Material.JUNGLE_PRESSURE_PLATE);
        list.add(Material.JUNGLE_TRAPDOOR);
        list.add(Material.LEVER);
        list.add(Material.LIGHT_BLUE_BED);
        list.add(Material.LIGHT_BLUE_SHULKER_BOX);
        list.add(Material.LIGHT_GRAY_BED);
        list.add(Material.LIGHT_GRAY_SHULKER_BOX);
        list.add(Material.LIGHT_WEIGHTED_PRESSURE_PLATE);
        list.add(Material.LIME_BED);
        list.add(Material.LIME_SHULKER_BOX);
        list.add(Material.MAGENTA_BED);
        list.add(Material.MAGENTA_SHULKER_BOX);
        list.add(Material.NOTE_BLOCK);
        list.add(Material.OAK_BUTTON);
        list.add(Material.OAK_DOOR);
        list.add(Material.OAK_FENCE_GATE);
        list.add(Material.OAK_PRESSURE_PLATE);
        list.add(Material.OAK_TRAPDOOR);
        list.add(Material.ORANGE_BED);
        list.add(Material.ORANGE_SHULKER_BOX);
        list.add(Material.PINK_BED);
        list.add(Material.PINK_SHULKER_BOX);
        list.add(Material.PURPLE_BED);
        list.add(Material.PURPLE_SHULKER_BOX);
        list.add(Material.RED_BED);
        list.add(Material.RED_SHULKER_BOX);
        list.add(Material.REPEATER);
        list.add(Material.SHULKER_BOX);
        list.add(Material.SIGN);
        list.add(Material.SPRUCE_BUTTON);
        list.add(Material.SPRUCE_DOOR);
        list.add(Material.SPRUCE_FENCE_GATE);
        list.add(Material.SPRUCE_PRESSURE_PLATE);
        list.add(Material.SPRUCE_TRAPDOOR);
        list.add(Material.STONE_BUTTON);
        list.add(Material.STONE_PRESSURE_PLATE);
        list.add(Material.TRAPPED_CHEST);
        list.add(Material.WALL_SIGN);
        list.add(Material.WHITE_BED);
        list.add(Material.WHITE_SHULKER_BOX);
        list.add(Material.YELLOW_BED);
        list.add(Material.YELLOW_SHULKER_BOX);
        return list;
    }

    private HashMap<String, String> buildSignLookup() {
        HashMap<String, String> lookup = new HashMap<>();
        for (PRESET p : PRESET.values()) {
            if (!p.getFirstLine().isEmpty() && !lookup.containsKey(p.getFirstLine())) {
                lookup.put(p.getFirstLine(), p.getSecondLine());
            }
        }
        return lookup;
    }
}
