/*
 * Copyright (C) 2016 eccentric_nz
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
package me.eccentric_nz.TARDIS.planets;

import com.khorn.terraincontrol.bukkit.BukkitWorld;
import com.khorn.terraincontrol.bukkit.TXPlugin;
import com.khorn.terraincontrol.customobjects.bo3.BO3Loader;
import com.khorn.terraincontrol.util.NamedBinaryTag;
import java.io.File;
import java.util.HashMap;
import me.eccentric_nz.TARDIS.JSON.JSONArray;
import me.eccentric_nz.TARDIS.JSON.JSONObject;
import me.eccentric_nz.TARDIS.TARDIS;
import static me.eccentric_nz.TARDIS.schematic.TARDISBannerSetter.setBanners;
import me.eccentric_nz.TARDIS.schematic.TARDISSchematicGZip;
import me.eccentric_nz.TARDIS.utility.TARDISBlockSetters;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.Plugin;

/**
 * The TARDIS was prone to a number of technical faults, ranging from depleted
 * resources to malfunctioning controls to a simple inability to arrive at the
 * proper time or location. While the Doctor did not build the TARDIS from
 * scratch, he has substantially modified/rebuilt it.
 *
 * @author eccentric_nz
 */
public class TARDISBuildGallifreyanStructure {

    private final TARDIS plugin;
    private final TARDISRandomCollection<String> nbtFiles = new TARDISRandomCollection<>();

    public TARDISBuildGallifreyanStructure(TARDIS plugin) {
        this.plugin = plugin;
        this.nbtFiles.add(1, "RareE-1.nbt")
                .add(1, "RareE-2.nbt")
                .add(1, "RareE-3.nbt")
                .add(2, "RareE-4.nbt")
                .add(2, "RareE-5.nbt")
                .add(2, "RareE-6.nbt")
                .add(3, "RareE-7.nbt")
                .add(3, "RareE-8.nbt")
                .add(3, "RareE-9.nbt")
                .add(4, "RareE-10.nbt")
                .add(4, "RareE-11.nbt")
                .add(4, "RareE-12.nbt")
                .add(5, "RareE-13.nbt")
                .add(5, "RareE-14.nbt")
                .add(5, "RareE-15.nbt")
                .add(6, "Rare-1.nbt")
                .add(6, "Rare-2.nbt")
                .add(6, "Rare-3.nbt")
                .add(7, "Rare-4.nbt")
                .add(7, "Rare-5.nbt")
                .add(7, "Rare-6.nbt")
                .add(8, "Rare-7.nbt")
                .add(8, "Rare-8.nbt")
                .add(8, "Rare-9.nbt")
                .add(9, "Rare-10.nbt")
                .add(9, "Rare-11.nbt")
                .add(9, "Rare-12.nbt")
                .add(10, "Rare-13.nbt")
                .add(10, "Rare-14.nbt")
                .add(10, "Rare-15.nbt")
                .add(20, "BasicE-1.nbt")
                .add(20, "BasicE-2.nbt")
                .add(21, "BasicE-3.nbt")
                .add(22, "BasicE-4.nbt")
                .add(23, "BasicE-5.nbt")
                .add(24, "BasicE-6.nbt")
                .add(25, "BasicE-7.nbt")
                .add(25, "BasicE-8.nbt")
                .add(26, "BasicE-9.nbt")
                .add(26, "BasicE-10.nbt")
                .add(27, "BasicE-11.nbt")
                .add(28, "BasicE-12.nbt")
                .add(28, "BasicE-13.nbt")
                .add(29, "BasicE-14.nbt")
                .add(30, "BasicE-15.nbt")
                .add(33, "Basic-1.nbt")
                .add(38, "Basic-2.nbt")
                .add(43, "Basic-3.nbt")
                .add(48, "Basic-4.nbt")
                .add(53, "Basic-5.nbt")
                .add(58, "Basic-6.nbt")
                .add(63, "Basic-7.nbt")
                .add(68, "Basic-8.nbt")
                .add(73, "Basic-9.nbt")
                .add(78, "Basic-10.nbt")
                .add(83, "Basic-11.nbt")
                .add(88, "Basic-12.nbt")
                .add(93, "Basic-13.nbt")
                .add(98, "Basic-14.nbt")
                .add(100, "Basic-15.nbt");
    }

    /**
     * Builds a Gallifreyan structure.
     *
     * @param startx
     * @param starty
     * @param startz
     */
    @SuppressWarnings("deprecation")
    public void buildCity(int startx, int starty, int startz) {
        World world = plugin.getServer().getWorld("Gallifrey");
        String path = plugin.getDataFolder() + File.separator + "schematics" + File.separator + "gallifrey.tschm";
        File file = new File(path);
        if (!file.exists()) {
            plugin.debug(plugin.getPluginName() + "Could not find the Gallifrey schematic!");
            return;
        }
        // get JSON
        JSONObject obj = TARDISSchematicGZip.unzip(path);
        // get dimensions
        JSONObject dimensions = (JSONObject) obj.get("dimensions");
        int h = dimensions.getInt("height");
        int w = dimensions.getInt("width");
        int l = dimensions.getInt("length");
        HashMap<Block, Byte> postDoorBlocks = new HashMap<>();
        HashMap<Block, Byte> postRedstoneTorchBlocks = new HashMap<>();
        HashMap<Block, Byte> postTorchBlocks = new HashMap<>();
        HashMap<Block, Byte> postSignBlocks = new HashMap<>();
        HashMap<Block, Byte> postRepeaterBlocks = new HashMap<>();
        HashMap<Block, Byte> postPistonBaseBlocks = new HashMap<>();
        HashMap<Block, Byte> postStickyPistonBaseBlocks = new HashMap<>();
        HashMap<Block, Byte> postPistonExtensionBlocks = new HashMap<>();
        HashMap<Block, Byte> postLeverBlocks = new HashMap<>();
        HashMap<Block, Byte> postLadderBlocks = new HashMap<>();
        HashMap<Block, JSONObject> postStandingBanners = new HashMap<>();
        HashMap<Block, JSONObject> postWallBanners = new HashMap<>();
        Block chest = null;
        Material type;
        byte data;

        // get input array
        JSONArray arr = (JSONArray) obj.get("input");
        // loop like crazy
        for (int level = 0; level < h; level++) {
            JSONArray floor = (JSONArray) arr.get(level);
            for (int row = 0; row < w; row++) {
                JSONArray r = (JSONArray) floor.get(row);
                for (int col = 0; col < l; col++) {
                    JSONObject c = (JSONObject) r.get(col);
                    int x = startx + row;
                    int y = starty + level;
                    int z = startz + col;

                    type = Material.valueOf((String) c.get("type"));
                    data = c.getByte("data");

                    // if it's the door, don't set it just remember its block then do it at the end
                    switch (type) {
                        case CHEST:
                            chest = world.getBlockAt(x, y, z);
                            TARDISBlockSetters.setBlock(world, x, y, z, type, data);
                            break;
                        case IRON_DOOR_BLOCK:
                            // doors
                            postDoorBlocks.put(world.getBlockAt(x, y, z), data);
                            break;
                        case REDSTONE_TORCH_ON:
                            postRedstoneTorchBlocks.put(world.getBlockAt(x, y, z), data);
                            break;
                        case TORCH:
                            postTorchBlocks.put(world.getBlockAt(x, y, z), data);
                            break;
                        case PISTON_STICKY_BASE:
                            postStickyPistonBaseBlocks.put(world.getBlockAt(x, y, z), data);
                            break;
                        case PISTON_BASE:
                            postPistonBaseBlocks.put(world.getBlockAt(x, y, z), data);
                            break;
                        case PISTON_EXTENSION:
                            postPistonExtensionBlocks.put(world.getBlockAt(x, y, z), data);
                            break;
                        case LEVER:
                            postLeverBlocks.put(world.getBlockAt(x, y, z), data);
                            break;
                        case LADDER:
                            postLadderBlocks.put(world.getBlockAt(x, y, z), data);
                            break;
                        case WALL_SIGN:
                            postSignBlocks.put(world.getBlockAt(x, y, z), data);
                            break;
                        case STANDING_BANNER:
                        case WALL_BANNER:
                            JSONObject state = c.optJSONObject("banner");
                            if (state != null) {
                                if (type.equals(Material.STANDING_BANNER)) {
                                    postStandingBanners.put(world.getBlockAt(x, y, z), state);
                                } else {
                                    postWallBanners.put(world.getBlockAt(x, y, z), state);
                                }
                            }
                            break;
                        case SPONGE:
                            Block swap_block = world.getBlockAt(x, y, z);
                            if (!swap_block.getType().isOccluding()) {
                                TARDISBlockSetters.setBlock(world, x, y, z, Material.AIR, (byte) 0);
                            }
                            break;
                        case MOB_SPAWNER:
                            Block spawner = world.getBlockAt(x, y, z);
                            spawner.setType(type);
                            CreatureSpawner cs = (CreatureSpawner) spawner.getState();
                            cs.setSpawnedType(EntityType.VILLAGER);
                            break;
                        default:
                            TARDISBlockSetters.setBlock(world, x, y, z, type, data);
                            break;
                    }
                }
            }
        }
        // put on the door, redstone torches, signs, and the repeaters
        postDoorBlocks.entrySet().forEach((entry) -> {
            Block pdb = entry.getKey();
            byte pddata = entry.getValue();
            pdb.setType(Material.IRON_DOOR_BLOCK);
            pdb.setData(pddata, true);
        });
        postRedstoneTorchBlocks.entrySet().forEach((entry) -> {
            Block prtb = entry.getKey();
            byte ptdata = entry.getValue();
            prtb.setTypeIdAndData(76, ptdata, true);
        });
        postTorchBlocks.entrySet().forEach((entry) -> {
            Block ptb = entry.getKey();
            byte ptdata = entry.getValue();
            ptb.setTypeIdAndData(50, ptdata, true);
        });
        postRepeaterBlocks.entrySet().forEach((entry) -> {
            Block prb = entry.getKey();
            byte ptdata = entry.getValue();
            prb.setType(Material.DIODE_BLOCK_OFF);
            prb.setData(ptdata, true);
        });
        postStickyPistonBaseBlocks.entrySet().forEach((entry) -> {
            Block pspb = entry.getKey();
            plugin.getGeneralKeeper().getDoorPistons().add(pspb);
            byte pspdata = entry.getValue();
            pspb.setType(Material.PISTON_STICKY_BASE);
            pspb.setData(pspdata, true);
        });
        postPistonBaseBlocks.entrySet().forEach((entry) -> {
            Block ppb = entry.getKey();
            plugin.getGeneralKeeper().getDoorPistons().add(ppb);
            byte ppbdata = entry.getValue();
            ppb.setType(Material.PISTON_BASE);
            ppb.setData(ppbdata, true);
        });
        postPistonExtensionBlocks.entrySet().forEach((entry) -> {
            Block ppeb = entry.getKey();
            byte ppedata = entry.getValue();
            ppeb.setType(Material.PISTON_EXTENSION);
            ppeb.setData(ppedata, true);
        });
        postLeverBlocks.entrySet().forEach((entry) -> {
            Block plb = entry.getKey();
            byte pldata = entry.getValue();
            plb.setType(Material.LEVER);
            plb.setData(pldata, true);
        });
        postLadderBlocks.entrySet().forEach((entry) -> {
            Block pldb = entry.getKey();
            byte plddata = entry.getValue();
            pldb.setType(Material.LADDER);
            pldb.setData(plddata, true);
        });
        setBanners(176, postStandingBanners);
        setBanners(177, postWallBanners);
        // finished processing - set chest contents
        if (chest != null) {
            // get nbt
            Plugin tc = plugin.getPM().getPlugin("TerrainControl");
            if (tc != null) {
                File f = new File(tc.getDataFolder(), "worlds" + File.separator + "Skaro" + File.separator + "WorldObjects" + File.separator + "NBT");
                NamedBinaryTag tag = BO3Loader.loadMetadata(nbtFiles.next(), f);
                BukkitWorld bw = ((TXPlugin) tc).worlds.get("Gallifrey");
                bw.attachMetadata(chest.getX(), chest.getY(), chest.getZ(), tag);
            }
        }
    }
}
