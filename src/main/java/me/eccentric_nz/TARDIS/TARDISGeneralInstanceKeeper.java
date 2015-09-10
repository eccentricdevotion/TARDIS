/*
 * Copyright (C) 2014 eccentric_nz
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

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import me.eccentric_nz.TARDIS.commands.TARDISCommandHelper;
import me.eccentric_nz.TARDIS.commands.TARDISTravelCommands;
import me.eccentric_nz.TARDIS.commands.admin.TARDISAdminCommands;
import me.eccentric_nz.TARDIS.listeners.TARDISButtonListener;
import me.eccentric_nz.TARDIS.listeners.TARDISRenderRoomListener;
import me.eccentric_nz.TARDIS.listeners.TARDISScannerListener;
import me.eccentric_nz.TARDIS.move.TARDISDoorClickListener;
import me.eccentric_nz.TARDIS.move.TARDISDoorListener;
import me.eccentric_nz.TARDIS.rooms.TARDISCondenserData;
import me.eccentric_nz.TARDIS.sonic.TARDISSonicListener;
import me.eccentric_nz.TARDIS.utility.TARDISUUIDCache;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 * Keeps instances of various classes, maps and lists for easy access in other
 * classes.
 *
 * @author eccentric_nz
 */
public class TARDISGeneralInstanceKeeper {

    private HashSet<Material> transparent = new HashSet<Material>();
    private List<Block> doorPistons = new ArrayList<Block>();
    private List<Integer> npcIDs = new ArrayList<Integer>();
    private List<String> quotes = new ArrayList<String>();
    private TARDISAdminCommands tardisAdminCommand;
    private TARDISButtonListener buttonListener;
    private TARDISDoorListener doorListener;
    private TARDISRenderRoomListener rendererListener;
    private TARDISScannerListener scannerListener;
    private TARDISSonicListener sonicListener;
    private TARDISTravelCommands tardisTravelCommand;
    private final HashMap<String, Double[]> gravityEastList = new HashMap<String, Double[]>();
    private final HashMap<String, Double[]> gravityNorthList = new HashMap<String, Double[]>();
    private final HashMap<String, Double[]> gravitySouthList = new HashMap<String, Double[]>();
    private final HashMap<String, Double[]> gravityUpList = new HashMap<String, Double[]>();
    private final HashMap<String, Double[]> gravityWestList = new HashMap<String, Double[]>();
    private final HashMap<String, Integer> protectBlockMap = new HashMap<String, Integer>();
    private final HashMap<UUID, TARDISCondenserData> roomCondenserData = new HashMap<UUID, TARDISCondenserData>();
    private final List<Block> artronFurnaces = new ArrayList<Block>();
    private final List<BlockFace> faces = Arrays.asList(BlockFace.NORTH, BlockFace.WEST, BlockFace.SOUTH, BlockFace.EAST);
    private final List<BlockFace> surrounding = Arrays.asList(BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST, BlockFace.NORTH, BlockFace.NORTH_EAST, BlockFace.NORTH_WEST, BlockFace.SOUTH_EAST, BlockFace.SOUTH_WEST);
    private final List<Chunk> roomChunkList = new ArrayList<Chunk>();
    private final List<Chunk> tardisChunkList = new ArrayList<Chunk>();
    private final List<Chunk> railChunkList = new ArrayList<Chunk>();
    private final List<Location> rechargers = new ArrayList<Location>();
    private final List<Material> doors = Arrays.asList(Material.IRON_DOOR_BLOCK, Material.WOODEN_DOOR, Material.SPRUCE_DOOR, Material.BIRCH_DOOR, Material.ACACIA_DOOR, Material.JUNGLE_DOOR, Material.DARK_OAK_DOOR);
    private final List<Material> rails = Arrays.asList(Material.POWERED_RAIL, Material.RAILS, Material.DETECTOR_RAIL, Material.ACTIVATOR_RAIL);
    private final List<Material> goodNether = Arrays.asList(Material.NETHERRACK, Material.SOUL_SAND, Material.GLOWSTONE, Material.NETHER_BRICK, Material.NETHER_FENCE, Material.NETHER_BRICK_STAIRS);
    private final List<String> gravityDownList = new ArrayList<String>();
    private final List<String> roomArgs;
    private final List<String> sonicLamps = new ArrayList<String>();
    private final List<String> sonicPistons = new ArrayList<String>();
    private final List<String> sonicRails = new ArrayList<String>();
    private final List<String> sonicWires = new ArrayList<String>();
    private final TARDIS plugin;
    private final TARDISUUIDCache UUIDCache;
    private final YamlConfiguration pluginYAML;
    private long junkTime;
    private boolean junkTravelling = false;
    private Location junkDestination = null;
    private final List<UUID> junkTravellers = new ArrayList<UUID>();

    public TARDISGeneralInstanceKeeper(TARDIS plugin) {
        this.plugin = plugin;
        this.roomArgs = buildRoomArgs();
        this.transparent = buildTransparent();
        this.UUIDCache = new TARDISUUIDCache(plugin);
        this.doorListener = new TARDISDoorListener(plugin);
        setRechargers();
        InputStream is = plugin.getResource("plugin.yml");
        InputStreamReader reader = new InputStreamReader(is);
        this.pluginYAML = new YamlConfiguration();
        try {
            this.pluginYAML.load(reader);
        } catch (IOException ex) {
            Logger.getLogger(TARDISCommandHelper.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidConfigurationException ex) {
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

    public TARDISUUIDCache getUUIDCache() {
        return UUIDCache;
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

    private void setRechargers() {
        if (plugin.getConfig().isConfigurationSection("rechargers")) {
            Set<String> therechargers = plugin.getConfig().getConfigurationSection("rechargers").getKeys(false);
            for (String s : therechargers) {
                World w = plugin.getServer().getWorld(plugin.getConfig().getString("rechargers." + s + ".world"));
                int x = plugin.getConfig().getInt("rechargers." + s + ".x");
                int y = plugin.getConfig().getInt("rechargers." + s + ".y");
                int z = plugin.getConfig().getInt("rechargers." + s + ".z");
                Location rc_loc = new Location(w, x, y, z);
                this.rechargers.add(rc_loc);
            }
        }
    }

    private HashSet<Material> buildTransparent() {
        HashSet<Material> trans = new HashSet<Material>();
        // add transparent blocks
        trans.add(Material.AIR);
        trans.add(Material.WATER);
        trans.add(Material.STATIONARY_WATER);
        trans.add(Material.LONG_GRASS);
        trans.add(Material.DEAD_BUSH);
        trans.add(Material.DOUBLE_PLANT);
        trans.add(Material.REDSTONE_WIRE);
        trans.add(Material.SNOW);
        trans.add(Material.IRON_FENCE);
        trans.add(Material.VINE);
        return trans;
    }

    private List<String> buildRoomArgs() {
        List<String> rooms = new ArrayList<String>();
        // rooms - only add if enabled in the config
        for (String r : plugin.getRoomsConfig().getConfigurationSection("rooms").getKeys(false)) {
            if (plugin.getRoomsConfig().getBoolean("rooms." + r + ".enabled")) {
                rooms.add(r);
            }
        }
        return rooms;
    }
}
