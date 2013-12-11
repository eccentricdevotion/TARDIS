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
    List<Integer> plat_blocks = Arrays.asList(new Integer[]{0, 6, 9, 8, 31, 32, 37, 38, 39, 40, 78, 106, 3019, 3020});
    HashMap<TARDISConstants.COMPASS, BlockFace[]> face_map = new HashMap<TARDISConstants.COMPASS, BlockFace[]>();
    public final List<TARDISConstants.PRESET> no_block_under_door;
    public final List<TARDISConstants.PRESET> notSubmarinePresets;
    Random rand;

    public TARDISPresetBuilderFactory(TARDIS plugin) {
        this.plugin = plugin;
        this.rand = new Random();
        face_map.put(TARDISConstants.COMPASS.NORTH, new BlockFace[]{BlockFace.SOUTH_WEST, BlockFace.SOUTH_SOUTH_WEST, BlockFace.SOUTH, BlockFace.SOUTH_SOUTH_EAST, BlockFace.SOUTH_EAST});
        face_map.put(TARDISConstants.COMPASS.WEST, new BlockFace[]{BlockFace.SOUTH_EAST, BlockFace.EAST_SOUTH_EAST, BlockFace.EAST, BlockFace.EAST_NORTH_EAST, BlockFace.NORTH_EAST});
        face_map.put(TARDISConstants.COMPASS.SOUTH, new BlockFace[]{BlockFace.NORTH_EAST, BlockFace.NORTH_NORTH_EAST, BlockFace.NORTH, BlockFace.NORTH_NORTH_WEST, BlockFace.NORTH_WEST});
        face_map.put(TARDISConstants.COMPASS.EAST, new BlockFace[]{BlockFace.NORTH_WEST, BlockFace.WEST_NORTH_WEST, BlockFace.WEST, BlockFace.WEST_SOUTH_WEST, BlockFace.SOUTH_WEST});
        no_block_under_door = new ArrayList<TARDISConstants.PRESET>();
        no_block_under_door.add(TARDISConstants.PRESET.ANGEL);
        no_block_under_door.add(TARDISConstants.PRESET.DUCK);
        no_block_under_door.add(TARDISConstants.PRESET.GAZEBO);
        no_block_under_door.add(TARDISConstants.PRESET.HELIX);
        no_block_under_door.add(TARDISConstants.PRESET.LIBRARY);
        no_block_under_door.add(TARDISConstants.PRESET.PANDORICA);
        no_block_under_door.add(TARDISConstants.PRESET.ROBOT);
        no_block_under_door.add(TARDISConstants.PRESET.TORCH);
        no_block_under_door.add(TARDISConstants.PRESET.WELL);
        notSubmarinePresets = new ArrayList<TARDISConstants.PRESET>();
        notSubmarinePresets.add(TARDISConstants.PRESET.LAMP);
        notSubmarinePresets.add(TARDISConstants.PRESET.MINESHAFT);
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
    public void buildPreset(int id, Location l, TARDISConstants.COMPASS d, boolean c, Player p, boolean rebuild, boolean mal) {
        HashMap<String, Object> where = new HashMap<String, Object>();
        where.put("tardis_id", id);
        ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
        if (rs.resultSet()) {
            TARDISConstants.PRESET preset = rs.getPreset();
            if (rs.isAdapti_on()) {
                Biome biome = l.getWorld().getBiome(l.getBlockX(), l.getBlockZ());
                preset = adapt(biome, preset);
            }
            TARDISConstants.PRESET demat = rs.getDemat();
            int cham_id = rs.getChameleon_id();
            byte cham_data = rs.getChameleon_data();
            if (c && (preset.equals(TARDISConstants.PRESET.NEW) || preset.equals(TARDISConstants.PRESET.OLD) || preset.equals(TARDISConstants.PRESET.SUBMERGED))) {
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
            int lamp = plugin.getConfig().getInt("tardis_lamp");
            boolean sub = false;
            boolean hidden = rs.isHidden();
            HashMap<String, Object> wherepp = new HashMap<String, Object>();
            wherepp.put("player", p.getName());
            ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, wherepp);
            if (rsp.resultSet()) {
                lamp = rsp.getLamp();
                sub = (rsp.isSubmarine_on() && plugin.trackSubmarine.contains(Integer.valueOf(id)));
            }
            if (sub && notSubmarinePresets.contains(preset)) {
                preset = TARDISConstants.PRESET.YELLOW;
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
                final TARDISInstaPreset trp = new TARDISInstaPreset(plugin, l, preset, id, d, p.getName(), mal, lamp, sub, cham_id, cham_data, true);
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                    @Override
                    public void run() {
                        trp.buildPreset();
                    }
                }, 10L);
            } else {
                if (plugin.getConfig().getBoolean("materialise")) {
                    plugin.tardisMaterialising.add(id);
                    TARDISMaterialisationPreset runnable = new TARDISMaterialisationPreset(plugin, l, preset, id, d, p, mal, lamp, sub, cham_id, cham_data);
                    int taskID = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, runnable, 10L, 20L);
                    runnable.setTask(taskID);
                } else {
                    plugin.tardisMaterialising.add(id);
                    TARDISInstaPreset insta = new TARDISInstaPreset(plugin, l, preset, id, d, p.getName(), mal, lamp, sub, cham_id, cham_data, false);
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
    public void addPlatform(Location l, boolean rebuild, TARDISConstants.COMPASS d, String p, int id) {
        int plusx, minusx, x, y, plusz, minusz, z;
        int platform_id = plugin.getConfig().getInt("platform_id");
        byte platform_data = (byte) plugin.getConfig().getInt("platform_data");
        // add platform if configured and necessary
        World world = l.getWorld();
        x = l.getBlockX();
        plusx = (l.getBlockX() + 1);
        minusx = (l.getBlockX() - 1);
        if (plugin.getConfig().getBoolean("materialise") && rebuild == false) {
            y = (l.getBlockY() - 1);
        } else {
            y = (l.getBlockY() - 3);
        }
        z = (l.getBlockZ());
        plusz = (l.getBlockZ() + 1);
        minusz = (l.getBlockZ() - 1);
        QueryFactory qf = new QueryFactory(plugin);
        if (plugin.getConfig().getBoolean("platform")) {
            // check if user has platform pref
            HashMap<String, Object> wherep = new HashMap<String, Object>();
            wherep.put("player", p);
            ResultSetPlayerPrefs pp = new ResultSetPlayerPrefs(plugin, wherep);
            boolean userPlatform;
            if (pp.resultSet()) {
                userPlatform = pp.isPlatform_on();
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
                    if (plat_blocks.contains(matint)) {
                        plugin.utils.setBlockAndRemember(world, pb.getX(), pb.getY(), pb.getZ(), platform_id, platform_data, id);
                    }
                }
            }
        }
    }

    public TARDISConstants.PRESET adapt(Biome biome, TARDISConstants.PRESET preset) {
        switch (biome) {
            case DEEP_OCEAN:
            case FROZEN_OCEAN:
            case OCEAN:
                return TARDISConstants.PRESET.YELLOW;
            case DESERT:
            case DESERT_HILLS:
                return TARDISConstants.PRESET.DESERT;
            case HELL:
                return TARDISConstants.PRESET.NETHER;
            case JUNGLE:
            case JUNGLE_HILLS:
                return TARDISConstants.PRESET.JUNGLE;
            case PLAINS:
                return TARDISConstants.PRESET.VILLAGE;
            case MUSHROOM_ISLAND:
            case MUSHROOM_SHORE:
                return TARDISConstants.PRESET.SHROOM;
            case SWAMPLAND:
                return TARDISConstants.PRESET.SWAMP;
            case SKY:
                return TARDISConstants.PRESET.THEEND;
            default:
                return preset;
        }
    }

    public BlockFace getSkullDirection(TARDISConstants.COMPASS d) {
        BlockFace[] faces = face_map.get(d);
        return faces[rand.nextInt(5)];
    }
}
