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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import me.eccentric_nz.TARDIS.commands.TARDISTravelCommands;
import me.eccentric_nz.TARDIS.commands.admin.TARDISAdminCommands;
import me.eccentric_nz.TARDIS.listeners.TARDISButtonListener;
import me.eccentric_nz.TARDIS.move.TARDISDoorClickListener;
import me.eccentric_nz.TARDIS.listeners.TARDISRenderRoomListener;
import me.eccentric_nz.TARDIS.listeners.TARDISScannerListener;
import me.eccentric_nz.TARDIS.rooms.TARDISCondenserData;
import me.eccentric_nz.TARDIS.sonic.TARDISSonicListener;
import me.eccentric_nz.TARDIS.utility.TARDISUUIDCache;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

/**
 *
 * @author eccentric_nz
 */
public class TARDISGeneralInstanceKeeper {

    private HashSet<Byte> transparent = new HashSet<Byte>();
    private List<Block> doorPistons = new ArrayList<Block>();
    private List<Integer> npcIDs = new ArrayList<Integer>();
    private List<String> quotes = new ArrayList<String>();
    private TARDISAdminCommands tardisAdminCommand;
    private TARDISButtonListener buttonListener;
    private TARDISDoorClickListener doorListener;
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
    private final List<BlockFace> faces = Arrays.asList(BlockFace.NORTH, BlockFace.WEST, BlockFace.SOUTH, BlockFace.EAST);
    private final List<Chunk> roomChunkList = new ArrayList<Chunk>();
    private final List<Chunk> tardisChunkList = new ArrayList<Chunk>();
    private final List<Material> rails = Arrays.asList(Material.POWERED_RAIL, Material.RAILS, Material.DETECTOR_RAIL, Material.ACTIVATOR_RAIL);
    private final List<String> gravityDownList = new ArrayList<String>();
    private final List<String> roomArgs;
    private final List<String> sonicLamps = new ArrayList<String>();
    private final List<String> sonicPistons = new ArrayList<String>();
    private final List<String> sonicRails = new ArrayList<String>();
    private final List<String> sonicWires = new ArrayList<String>();
    private final TARDIS plugin;
    private final TARDISUUIDCache UUIDCache;

    public TARDISGeneralInstanceKeeper(TARDIS plugin) {
        this.plugin = plugin;
        this.roomArgs = buildRoomArgs();
        this.transparent = buildTransparent();
        this.UUIDCache = new TARDISUUIDCache(plugin);
    }

    public List<String> getQuotes() {
        return quotes;
    }

    public void setQuotes(ArrayList<String> quotes) {
        this.quotes = quotes;
    }

    public List<BlockFace> getFaces() {
        return faces;
    }

    public List<Chunk> getTardisChunkList() {
        return tardisChunkList;
    }

    public List<Chunk> getRoomChunkList() {
        return roomChunkList;
    }

    public List<Material> getRails() {
        return rails;
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

    public TARDISDoorClickListener getDoorListener() {
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

    public HashSet<Byte> getTransparent() {
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

    private HashSet<Byte> buildTransparent() {
        HashSet<Byte> trans = new HashSet<Byte>();
        // add transparent blocks
        trans.add((byte) 0); // AIR
        trans.add((byte) 8); // WATER
        trans.add((byte) 9); // STATIONARY_WATER
        trans.add((byte) 31); // LONG_GRASS
        trans.add((byte) 32); // DEAD_BUSH
        trans.add((byte) 55); // REDSTONE_WIRE
        trans.add((byte) 78); // SNOW
        trans.add((byte) 101); // IRON_FENCE
        trans.add((byte) 106); // VINE
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
