/*
 * Copyright (C) 2013 eccentric_nz
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
package me.eccentric_nz.TARDIS.builders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.chameleon.TARDISChameleonCircuit;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetPlayerPrefs;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.destroyers.TARDISDeinstaPreset;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import me.eccentric_nz.TARDIS.enumeration.PRESET;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

/**
 * The Wibbly lever was a part of The Doctor's TARDIS console. The lever had at
 * least two functions: opening and closing doors and controlling implosions
 * used to revert paradoxes in which the TARDIS had materialised within itself.
 *
 * @author eccentric_nz
 */
public class TARDISPresetBuilderFactory {

    private final TARDIS plugin;
    HashMap<COMPASS, BlockFace[]> face_map = new HashMap<COMPASS, BlockFace[]>();
    public final List<PRESET> no_block_under_door;
    public final List<PRESET> notSubmarinePresets;
    Random rand;

    public TARDISPresetBuilderFactory(TARDIS plugin) {
        this.plugin = plugin;
        this.rand = new Random();
        face_map.put(COMPASS.NORTH, new BlockFace[]{BlockFace.SOUTH_WEST, BlockFace.SOUTH_SOUTH_WEST, BlockFace.SOUTH, BlockFace.SOUTH_SOUTH_EAST, BlockFace.SOUTH_EAST});
        face_map.put(COMPASS.WEST, new BlockFace[]{BlockFace.SOUTH_EAST, BlockFace.EAST_SOUTH_EAST, BlockFace.EAST, BlockFace.EAST_NORTH_EAST, BlockFace.NORTH_EAST});
        face_map.put(COMPASS.SOUTH, new BlockFace[]{BlockFace.NORTH_EAST, BlockFace.NORTH_NORTH_EAST, BlockFace.NORTH, BlockFace.NORTH_NORTH_WEST, BlockFace.NORTH_WEST});
        face_map.put(COMPASS.EAST, new BlockFace[]{BlockFace.NORTH_WEST, BlockFace.WEST_NORTH_WEST, BlockFace.WEST, BlockFace.WEST_SOUTH_WEST, BlockFace.SOUTH_WEST});
        no_block_under_door = new ArrayList<PRESET>();
        no_block_under_door.add(PRESET.ANGEL);
        no_block_under_door.add(PRESET.DUCK);
        no_block_under_door.add(PRESET.GAZEBO);
        no_block_under_door.add(PRESET.HELIX);
        no_block_under_door.add(PRESET.LIBRARY);
        no_block_under_door.add(PRESET.PANDORICA);
        no_block_under_door.add(PRESET.ROBOT);
        no_block_under_door.add(PRESET.TORCH);
        no_block_under_door.add(PRESET.WELL);
        notSubmarinePresets = new ArrayList<PRESET>();
        notSubmarinePresets.add(PRESET.LAMP);
        notSubmarinePresets.add(PRESET.MINESHAFT);
    }

    /**
     * Builds the TARDIS Police Box.
     *
     * @param id the unique key of the record for this TARDIS in the database.
     * @param l the location where the Police Box should be built.
     * @param d the direction the Police Box is built in.
     * @param c boolean determining whether to engage the chameleon circuit.
     * @param p an instance of the player who owns the TARDIS.
     * @param rebuild boolean determining whether the Police Box blocks should
     * be remembered in the database for protection purposes.
     * @param mal boolean determining whether a malfunction has occurred
     */
    public void buildPreset(int id, Location l, COMPASS d, boolean c, Player p, boolean rebuild, boolean mal) {
        HashMap<String, Object> where = new HashMap<String, Object>();
        where.put("tardis_id", id);
        ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
        if (rs.resultSet()) {
            PRESET preset = rs.getPreset();
            if (rs.isAdapti_on()) {
                Biome biome = l.getWorld().getBiome(l.getBlockX(), l.getBlockZ());
                preset = adapt(biome, preset);
            }
            PRESET demat = rs.getDemat();
            int cham_id = rs.getChameleon_id();
            byte cham_data = rs.getChameleon_data();
            if (c && (preset.equals(PRESET.NEW) || preset.equals(PRESET.OLD) || preset.equals(PRESET.SUBMERGED))) {
                Block chameleonBlock;
                // chameleon circuit is on - get block under TARDIS
                if (l.getBlock().getType() == Material.SNOW) {
                    chameleonBlock = l.getBlock();
                } else {
                    chameleonBlock = l.getBlock().getRelative(BlockFace.DOWN);
                }
                // determine cham_id
                TARDISChameleonCircuit tcc = new TARDISChameleonCircuit(plugin);
                int[] b_data = tcc.getChameleonBlock(chameleonBlock, p, false);
                cham_id = b_data[0];
                cham_data = (byte) b_data[1];
            }
            // get lamp and submarine preferences
            int lamp = plugin.getConfig().getInt("police_box.tardis_lamp");
            boolean sub = false;
            boolean minecart = false;
            boolean hidden = rs.isHidden();
            HashMap<String, Object> wherepp = new HashMap<String, Object>();
            wherepp.put("player", p.getName());
            ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, wherepp);
            if (rsp.resultSet()) {
                lamp = rsp.getLamp();
                sub = (rsp.isSubmarineOn() && plugin.trackSubmarine.contains(Integer.valueOf(id)));
                minecart = rsp.isMinecartOn();
            }
            if (sub && notSubmarinePresets.contains(preset)) {
                preset = PRESET.YELLOW;
                p.sendMessage(plugin.pluginName + "Selected preset unsuitable for submarine mode - changed to Yellow Submarine.");
            }
            // keep the chunk this Police box is in loaded
            Chunk thisChunk = l.getChunk();
            while (!thisChunk.isLoaded()) {
                thisChunk.load();
            }
            /*
             * We can always add the chunk, as List.remove() only removes the first
             * occurence - and we want the chunk to remain loaded if there are other
             * Police Boxes in it.
             */
            plugin.tardisChunkList.add(thisChunk);
            if (rebuild) {
                // always destroy it first as the player may just be switching presets
                if (!hidden) {
                    TARDISDeinstaPreset deinsta = new TARDISDeinstaPreset(plugin);
                    deinsta.instaDestroyPreset(l, d, id, sub, demat);
                }
                final TARDISInstaPreset trp = new TARDISInstaPreset(plugin, l, preset, id, d, p.getName(), mal, lamp, sub, cham_id, cham_data, true, minecart);
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                    @Override
                    public void run() {
                        trp.buildPreset();
                    }
                }, 10L);
            } else {
                if (plugin.getConfig().getBoolean("police_box.materialise")) {
                    plugin.tardisMaterialising.add(Integer.valueOf(id));
                    TARDISMaterialisationPreset runnable = new TARDISMaterialisationPreset(plugin, l, preset, id, d, p, mal, lamp, sub, cham_id, cham_data, minecart);
                    int taskID = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, runnable, 10L, 20L);
                    runnable.setTask(taskID);
                } else {
                    plugin.tardisMaterialising.add(Integer.valueOf(id));
                    TARDISInstaPreset insta = new TARDISInstaPreset(plugin, l, preset, id, d, p.getName(), mal, lamp, sub, cham_id, cham_data, false, minecart);
                    insta.buildPreset();
                }
            }
            // update demat so it knows about the current preset after it has changed
            HashMap<String, Object> whered = new HashMap<String, Object>();
            whered.put("tardis_id", id);
            HashMap<String, Object> set = new HashMap<String, Object>();
            set.put("chameleon_demat", preset.toString());
            new QueryFactory(plugin).doUpdate("tardis", set, whered);
        }
    }

    @SuppressWarnings("deprecation")
    public void addPlatform(Location l, boolean rebuild, COMPASS d, String p, int id) {
        int plusx, minusx, x, y, plusz, minusz, z;
        int platform_id = plugin.getConfig().getInt("police_box.platform_id");
        byte platform_data = (byte) plugin.getConfig().getInt("police_box.platform_data");
        // add platform if configured and necessary
        World world = l.getWorld();
        x = l.getBlockX();
        plusx = (l.getBlockX() + 1);
        minusx = (l.getBlockX() - 1);
        if (plugin.getConfig().getBoolean("police_box.materialise") && rebuild == false) {
            y = (l.getBlockY() - 1);
        } else {
            y = (l.getBlockY() - 3);
        }
        z = (l.getBlockZ());
        plusz = (l.getBlockZ() + 1);
        minusz = (l.getBlockZ() - 1);
        QueryFactory qf = new QueryFactory(plugin);
        if (plugin.getConfig().getBoolean("travel.platform")) {
            // check if user has platform pref
            HashMap<String, Object> wherep = new HashMap<String, Object>();
            wherep.put("player", p);
            ResultSetPlayerPrefs pp = new ResultSetPlayerPrefs(plugin, wherep);
            boolean userPlatform;
            if (pp.resultSet()) {
                userPlatform = pp.isPlatformOn();
            } else {
                userPlatform = true;
            }
            if (userPlatform) {
                List<Block> platform_blocks;
                switch (d) {
                    case SOUTH:
                        platform_blocks = Arrays.asList(world.getBlockAt(x - 1, y, minusz - 1), world.getBlockAt(x, y, minusz - 1), world.getBlockAt(x + 1, y, minusz - 1), world.getBlockAt(x - 1, y, minusz - 2), world.getBlockAt(x, y, minusz - 2), world.getBlockAt(x + 1, y, minusz - 2));
                        break;
                    case EAST:
                        platform_blocks = Arrays.asList(world.getBlockAt(minusx - 1, y, z - 1), world.getBlockAt(minusx - 1, y, z), world.getBlockAt(minusx - 1, y, z + 1), world.getBlockAt(minusx - 2, y, z - 1), world.getBlockAt(minusx - 2, y, z), world.getBlockAt(minusx - 2, y, z + 1));
                        break;
                    case NORTH:
                        platform_blocks = Arrays.asList(world.getBlockAt(x + 1, y, plusz + 1), world.getBlockAt(x, y, plusz + 1), world.getBlockAt(x - 1, y, plusz + 1), world.getBlockAt(x + 1, y, plusz + 2), world.getBlockAt(x, y, plusz + 2), world.getBlockAt(x - 1, y, plusz + 2));
                        break;
                    default:
                        platform_blocks = Arrays.asList(world.getBlockAt(plusx + 1, y, z + 1), world.getBlockAt(plusx + 1, y, z), world.getBlockAt(plusx + 1, y, z - 1), world.getBlockAt(plusx + 2, y, z + 1), world.getBlockAt(plusx + 2, y, z), world.getBlockAt(plusx + 2, y, z - 1));
                        break;
                }
                for (Block pb : platform_blocks) {
                    int matint = pb.getTypeId();
                    if (TARDISConstants.PLATFORM_BLOCKS.contains(matint)) {
                        plugin.utils.setBlockAndRemember(world, pb.getX(), pb.getY(), pb.getZ(), platform_id, platform_data, id);
                    }
                }
            }
        }
    }

    public PRESET adapt(Biome biome, PRESET preset) {
        switch (biome) {
            case DEEP_OCEAN:
            case FROZEN_OCEAN:
            case OCEAN:
                return PRESET.YELLOW;
            case DESERT:
            case DESERT_HILLS:
                return PRESET.DESERT;
            case HELL:
                return PRESET.NETHER;
            case JUNGLE:
            case JUNGLE_HILLS:
                return PRESET.JUNGLE;
            case PLAINS:
                return PRESET.VILLAGE;
            case MUSHROOM_ISLAND:
            case MUSHROOM_SHORE:
                return PRESET.SHROOM;
            case SWAMPLAND:
                return PRESET.SWAMP;
            case SKY:
                return PRESET.THEEND;
            default:
                return preset;
        }
    }

    public BlockFace getSkullDirection(COMPASS d) {
        BlockFace[] faces = face_map.get(d);
        return faces[rand.nextInt(5)];
    }
}
