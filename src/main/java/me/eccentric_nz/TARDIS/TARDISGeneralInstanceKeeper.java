/*
 * Copyright (C) 2023 eccentric_nz
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
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import me.eccentric_nz.TARDIS.commands.TARDISCommandHelper;
import me.eccentric_nz.TARDIS.commands.config.TARDISConfigCommand;
import me.eccentric_nz.TARDIS.commands.travel.TARDISTravelCommands;
import me.eccentric_nz.TARDIS.enumeration.ChameleonPreset;
import me.eccentric_nz.TARDIS.listeners.TARDISRenderRoomListener;
import me.eccentric_nz.TARDIS.move.TARDISDoorListener;
import me.eccentric_nz.TARDIS.planets.TARDISAliasResolver;
import me.eccentric_nz.TARDIS.rooms.TARDISCondenserData;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 * Keeps instances of various classes, maps and lists for easy access in other classes.
 *
 * @author eccentric_nz
 */
public class TARDISGeneralInstanceKeeper {

    private final HashMap<String, Double[]> gravityEastList = new HashMap<>();
    private final HashMap<String, Double[]> gravityNorthList = new HashMap<>();
    private final HashMap<String, Double[]> gravitySouthList = new HashMap<>();
    private final HashMap<String, Double[]> gravityUpList = new HashMap<>();
    private final HashMap<String, Double[]> gravityWestList = new HashMap<>();
    private final HashMap<String, Integer> protectBlockMap = new HashMap<>();
    private final HashMap<String, String> sign_lookup;
    private final HashMap<UUID, TARDISCondenserData> roomCondenserData = new HashMap<>();
    private final Set<Material> transparent;
    private final Set<Block> artronFurnaces = new HashSet<>();
    private final Set<Block> doorPistons = new HashSet<>();
    private final List<BlockFace> blockFaces = Arrays.asList(BlockFace.UP, BlockFace.DOWN, BlockFace.NORTH, BlockFace.WEST, BlockFace.SOUTH, BlockFace.EAST);
    private final List<BlockFace> faces = Arrays.asList(BlockFace.NORTH, BlockFace.WEST, BlockFace.SOUTH, BlockFace.EAST);
    private final List<BlockFace> surrounding = Arrays.asList(BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST, BlockFace.NORTH, BlockFace.NORTH_EAST, BlockFace.NORTH_WEST, BlockFace.SOUTH_EAST, BlockFace.SOUTH_WEST);
    private final Set<Location> rechargers = new HashSet<>();
    private final List<Material> goodNether = Arrays.asList(Material.NETHERRACK, Material.SOUL_SAND, Material.GLOWSTONE, Material.NETHER_BRICK, Material.NETHER_BRICK_FENCE, Material.NETHER_BRICK_STAIRS);
    private final Set<String> gravityDownList = new HashSet<>();
    private final List<String> roomArgs;
    private final Set<String> sonicLamps = new HashSet<>();
    private final Set<String> sonicPistons = new HashSet<>();
    private final Set<String> sonicRails = new HashSet<>();
    private final Set<String> sonicWires = new HashSet<>();
    private final Set<UUID> junkTravellers = new HashSet<>();
    private final Set<UUID> timeRotors = new HashSet<>();
    private final TARDIS plugin;
    private final TARDISDoorListener doorListener;
    private final YamlConfiguration pluginYAML;
    private boolean junkTravelling = false;
    private List<String> quotes = new ArrayList<>();
    private Location junkDestination = null;
    private long junkTime;
    private TARDISConfigCommand tardisConfigCommand;
    private TARDISRenderRoomListener rendererListener;
    private TARDISTravelCommands tardisTravelCommand;

    TARDISGeneralInstanceKeeper(TARDIS plugin) {
        this.plugin = plugin;
        roomArgs = buildRoomArgs();
        transparent = buildTransparent();
        doorListener = new TARDISDoorListener(plugin);
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

    public Set<Block> getArtronFurnaces() {
        return artronFurnaces;
    }

    public List<BlockFace> getFaces() {
        return faces;
    }

    public List<BlockFace> getShuffledFaces() {
        List<BlockFace> shuffled = new ArrayList<>(faces);
        Collections.shuffle(shuffled);
        return shuffled;
    }

    public List<BlockFace> getBlockFaces() {
        return blockFaces;
    }

    public List<BlockFace> getSurrounding() {
        return surrounding;
    }

    public List<Material> getGoodNether() {
        return goodNether;
    }

    public HashMap<String, Double[]> getGravityUpList() {
        return gravityUpList;
    }

    public Set<String> getGravityDownList() {
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

    public Set<UUID> getTimeRotors() {
        return timeRotors;
    }

    public Set<Block> getDoorPistons() {
        return doorPistons;
    }

    public TARDISConfigCommand getTardisConfigCommand() {
        return tardisConfigCommand;
    }

    void setTardisConfigCommand(TARDISConfigCommand tardisConfigCommand) {
        this.tardisConfigCommand = tardisConfigCommand;
    }

    public TARDISDoorListener getDoorListener() {
        return doorListener;
    }

    public TARDISRenderRoomListener getRendererListener() {
        return rendererListener;
    }

    void setRendererListener(TARDISRenderRoomListener rendererListener) {
        this.rendererListener = rendererListener;
    }

    public TARDISTravelCommands getTardisTravelCommand() {
        return tardisTravelCommand;
    }

    void setTardisTravelCommand(TARDISTravelCommands tardisTravelCommand) {
        this.tardisTravelCommand = tardisTravelCommand;
    }

    public List<String> getRoomArgs() {
        return roomArgs;
    }

    public Set<Material> getTransparent() {
        return transparent;
    }

    public Set<String> getSonicLamps() {
        return sonicLamps;
    }

    public Set<String> getSonicPistons() {
        return sonicPistons;
    }

    public Set<String> getSonicRails() {
        return sonicRails;
    }

    public Set<String> getSonicWires() {
        return sonicWires;
    }

    public Set<Location> getRechargers() {
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

    public Set<UUID> getJunkTravellers() {
        return junkTravellers;
    }

    private void setRechargers() {
        if (plugin.getConfig().isConfigurationSection("rechargers")) {
            Set<String> therechargers = plugin.getConfig().getConfigurationSection("rechargers").getKeys(false);
            therechargers.forEach((s) -> {
                World w = TARDISAliasResolver.getWorldFromAlias(plugin.getConfig().getString("rechargers." + s + ".world"));
                if (w != null) {
                    int x = plugin.getConfig().getInt("rechargers." + s + ".x");
                    int y = plugin.getConfig().getInt("rechargers." + s + ".y");
                    int z = plugin.getConfig().getInt("rechargers." + s + ".z");
                    Location rc_loc = new Location(w, x, y, z);
                    rechargers.add(rc_loc);
                }
            });
        }
    }

    private Set<Material> buildTransparent() {
        Set<Material> trans = new HashSet<>();
        // add transparent blocks
        trans.add(Material.ACACIA_SAPLING);
        trans.add(Material.AIR);
        trans.add(Material.CAVE_AIR);
        trans.add(Material.VOID_AIR);
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

    private HashMap<String, String> buildSignLookup() {
        HashMap<String, String> lookup = new HashMap<>();
        for (ChameleonPreset p : ChameleonPreset.values()) {
            if (!p.getFirstLine().isEmpty() && !lookup.containsKey(p.getFirstLine())) {
                lookup.put(p.getFirstLine(), p.getSecondLine());
            }
        }
        return lookup;
    }
}
