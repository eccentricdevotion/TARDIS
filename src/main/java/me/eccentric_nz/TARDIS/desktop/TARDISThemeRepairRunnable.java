/*
 * Copyright (C) 2019 eccentric_nz
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
package me.eccentric_nz.TARDIS.desktop;

import me.eccentric_nz.TARDIS.JSON.JSONArray;
import me.eccentric_nz.TARDIS.JSON.JSONObject;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISBuilderInstanceKeeper;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.api.event.TARDISDesktopThemeEvent;
import me.eccentric_nz.TARDIS.builders.TARDISInteriorPostioning;
import me.eccentric_nz.TARDIS.builders.TARDISTIPSData;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.data.Archive;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.enumeration.ConsoleSize;
import me.eccentric_nz.TARDIS.enumeration.SCHEMATIC;
import me.eccentric_nz.TARDIS.rooms.TARDISCondenserData;
import me.eccentric_nz.TARDIS.schematic.ArchiveReset;
import me.eccentric_nz.TARDIS.schematic.ResultSetArchive;
import me.eccentric_nz.TARDIS.schematic.TARDISSchematicGZip;
import me.eccentric_nz.TARDIS.utility.TARDISBlockSetters;
import me.eccentric_nz.TARDIS.utility.TARDISMaterials;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import me.eccentric_nz.TARDIS.utility.TARDISStaticLocationGetters;
import org.bukkit.*;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.block.data.Bisected;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.entity.*;

import java.io.File;
import java.util.*;

/**
 * There was also a safety mechanism for when TARDIS rooms were deleted, automatically relocating any living beings in
 * the deleted room, depositing them in the control room.
 *
 * @author eccentric_nz
 */
public class TARDISThemeRepairRunnable extends TARDISThemeRunnable {

    private final TARDIS plugin;
    private final UUID uuid;
    private final TARDISUpgradeData tud;
    private boolean running;
    private int id;
    private int slot;
    private int level = 0;
    private int row = 0;
    private int h;
    private int w;
    private int c;
    private int startx;
    private int starty;
    private int startz;
    private int j = 2;
    private World world;
    private final List<Block> lampblocks = new ArrayList<>();
    private List<Chunk> chunks;
    private final HashMap<Block, BlockData> postDoorBlocks = new HashMap<>();
    private final HashMap<Block, BlockData> postRedstoneTorchBlocks = new HashMap<>();
    private final HashMap<Block, BlockData> postTorchBlocks = new HashMap<>();
    private final HashMap<Block, BlockData> postLeverBlocks = new HashMap<>();
    private final HashMap<Block, BlockData> postSignBlocks = new HashMap<>();
    private final HashMap<Block, BlockData> postRepeaterBlocks = new HashMap<>();
    private final HashMap<Block, BlockData> postPistonBaseBlocks = new HashMap<>();
    private final HashMap<Block, BlockData> postStickyPistonBaseBlocks = new HashMap<>();
    private final HashMap<Block, BlockData> postPistonExtensionBlocks = new HashMap<>();
    private Block postBedrock;
    private JSONArray arr;
    private Material wall_type;
    private Material floor_type;
    private final QueryFactory qf;
    private HashMap<String, Object> set;
    private HashMap<String, Object> where;
    private boolean own_world;
    private Location wg1;
    private Location wg2;
    private Player player;
    private Location ender = null;
    private Archive archive;
    private final boolean clean;

    TARDISThemeRepairRunnable(TARDIS plugin, UUID uuid, TARDISUpgradeData tud, boolean clean) {
        this.plugin = plugin;
        this.uuid = uuid;
        this.tud = tud;
        this.clean = clean;
        qf = new QueryFactory(this.plugin);
    }

    @Override

    public void run() {
        // initialise
        if (!running) {
            // get Archive if nescessary
            if (tud.getSchematic().getPermission().equals("archive")) {
                HashMap<String, Object> wherean = new HashMap<>();
                wherean.put("uuid", uuid.toString());
                wherean.put("use", 1);
                ResultSetArchive rs = new ResultSetArchive(plugin, wherean);
                if (rs.resultSet()) {
                    archive = rs.getArchive();
                } else {
                    // abort
                    Player cp = plugin.getServer().getPlayer(uuid);
                    TARDISMessage.send(cp, "ARCHIVE_NOT_FOUND");
                    // cancel task
                    plugin.getServer().getScheduler().cancelTask(taskID);
                    return;
                }
            }
            set = new HashMap<>();
            where = new HashMap<>();
            JSONObject obj;
            if (archive == null) {
                String directory = (tud.getSchematic().isCustom()) ? "user_schematics" : "schematics";
                String path = plugin.getDataFolder() + File.separator + directory + File.separator + tud.getSchematic().getPermission() + ".tschm";
                File file = new File(path);
                if (!file.exists()) {
                    plugin.debug("Could not find a schematic with that name!");
                    // cancel task
                    plugin.getServer().getScheduler().cancelTask(taskID);
                    return;
                }
                // get JSON
                obj = TARDISSchematicGZip.unzip(path);
            } else {
                obj = archive.getJSON();
            }
            // get dimensions
            JSONObject dimensions = (JSONObject) obj.get("dimensions");
            h = dimensions.getInt("height");
            w = dimensions.getInt("width");
            c = dimensions.getInt("length");
            // calculate startx, starty, startz
            HashMap<String, Object> wheret = new HashMap<>();
            wheret.put("uuid", uuid.toString());
            ResultSetTardis rs = new ResultSetTardis(plugin, wheret, "", false, 0);
            if (!rs.resultSet()) {
                // ?
                return;
            }
            Tardis tardis = rs.getTardis();
            slot = tardis.getTIPS();
            id = tardis.getTardis_id();
            Chunk chunk = TARDISStaticLocationGetters.getChunk(tardis.getChunk());
            if (tud.getPrevious().getPermission().equals("ender")) {
                // remove ender crystal
                for (Entity end : chunk.getEntities()) {
                    if (end.getType().equals(EntityType.ENDER_CRYSTAL)) {
                        end.remove();
                    }
                }
            }
            chunks = getChunks(chunk, tud.getSchematic());
            if (!tardis.getCreeper().isEmpty()) {
                // remove the charged creeper
                Location creeper = TARDISStaticLocationGetters.getLocationFromDB(tardis.getCreeper());
                Entity ent = creeper.getWorld().spawnEntity(creeper, EntityType.EGG);
                ent.getNearbyEntities(1.5d, 1.5d, 1.5d).forEach((e) -> {
                    if (e instanceof Creeper) {
                        e.remove();
                    }
                });
                ent.remove();
            }
            if (slot != -1) { // default world - use TIPS
                TARDISInteriorPostioning tintpos = new TARDISInteriorPostioning(plugin);
                TARDISTIPSData pos = tintpos.getTIPSData(slot);
                startx = pos.getCentreX();
                startz = pos.getCentreZ();
            } else {
                int gsl[] = plugin.getLocationUtils().getStartLocation(tardis.getTardis_id());
                startx = gsl[0];
                startz = gsl[2];
            }
            starty = (tud.getSchematic().getPermission().equals("redstone")) ? 65 : 64;
            world = TARDISStaticLocationGetters.getWorld(tardis.getChunk());
            own_world = plugin.getConfig().getBoolean("creation.create_worlds");
            wg1 = new Location(world, startx, starty, startz);
            wg2 = new Location(world, startx + (w - 1), starty + (h - 1), startz + (c - 1));
            // wall/floor block prefs
            String wall[] = tud.getWall().split(":");
            String floor[] = tud.getFloor().split(":");
            wall_type = Material.valueOf(wall[0]);
            floor_type = Material.valueOf(floor[0]);
            // get input array
            arr = (JSONArray) obj.get("input");
            // clear existing lamp blocks
            HashMap<String, Object> whered = new HashMap<>();
            whered.put("tardis_id", id);
            qf.doDelete("lamps", whered);
            // clear existing precious blocks
            HashMap<String, Object> wherep = new HashMap<>();
            wherep.put("tardis_id", id);
            wherep.put("police_box", 0);
            qf.doDelete("blocks", wherep);
            // set running
            running = true;
            player = plugin.getServer().getPlayer(uuid);
            // remove upgrade data
            plugin.getTrackerKeeper().getUpgrades().remove(uuid);
            // clear lamps table as we'll be adding the new lamp positions later
            HashMap<String, Object> wherel = new HashMap<>();
            wherel.put("tardis_id", id);
            qf.doDelete("lamps", wherel);
            plugin.getPM().callEvent(new TARDISDesktopThemeEvent(player, tardis, tud));
        }
        if (level == (h - 1) && row == (w - 1)) {
            // we're finished
            // remove items
            chunks.forEach((chink) -> {
                // remove dropped items
                for (Entity e : chink.getEntities()) {
                    if (e instanceof Item) {
                        e.remove();
                    }
                }
            });
            // put on the door, redstone torches, signs, and the repeaters
            postDoorBlocks.forEach((pdb, value) -> {
                pdb.setBlockData(value);
            });
            postRedstoneTorchBlocks.forEach((prtb, value) -> prtb.setBlockData(value));
            postLeverBlocks.forEach((plb, value) -> {
                plb.setBlockData(value);
            });
            postTorchBlocks.forEach((ptb, value) -> ptb.setBlockData(value));
            postRepeaterBlocks.forEach((prb, value) -> {
                prb.setBlockData(value);
            });
            postStickyPistonBaseBlocks.forEach((pspb, value) -> {
                plugin.getGeneralKeeper().getDoorPistons().add(pspb);
                pspb.setBlockData(value);
            });
            postPistonBaseBlocks.forEach((ppb, value) -> {
                plugin.getGeneralKeeper().getDoorPistons().add(ppb);
                ppb.setBlockData(value);
            });
            postPistonExtensionBlocks.forEach((ppeb, value) -> {
                ppeb.setBlockData(value);
            });
            int s = 0;
            for (Map.Entry<Block, BlockData> entry : postSignBlocks.entrySet()) {
                if (s == 0) {
                    Block psb = entry.getKey();
                    psb.setBlockData(entry.getValue());
                    if (Tag.WALL_SIGNS.isTagged(psb.getType())) {
                        Sign cs = (Sign) psb.getState();
                        cs.setLine(0, "");
                        cs.setLine(1, plugin.getSigns().getStringList("control").get(0));
                        cs.setLine(2, plugin.getSigns().getStringList("control").get(1));
                        cs.setLine(3, "");
                        cs.update();
                        String controlloc = psb.getLocation().toString();
                        qf.insertSyncControl(id, 22, controlloc, 0);
                    }
                }
                s++;
            }
            lampblocks.forEach((lamp) -> {
                BlockData l = (tud.getSchematic().hasLanterns() || (archive != null && archive.isLanterns())) ? TARDISConstants.LANTERN : TARDISConstants.LAMP;
                lamp.setBlockData(l);
            });
            lampblocks.clear();
            if (postBedrock != null) {
                postBedrock.setBlockData(TARDISConstants.GLASS);
            }
            if (plugin.isWorldGuardOnServer() && plugin.getConfig().getBoolean("preferences.use_worldguard")) {
                if (slot == -1) {
                    plugin.getWorldGuardUtils().addWGProtection(player, wg1, wg2);
                }
            }
            if (ender != null) {
                Entity ender_crystal = world.spawnEntity(ender, EntityType.ENDER_CRYSTAL);
                ((EnderCrystal) ender_crystal).setShowingBottom(false);
            }
            // finished processing - update tardis table!
            if (set.size() > 0) {
                where.put("tardis_id", id);
                qf.doUpdate("tardis", set, where);
            }
            if (!tud.getSchematic().getPermission().equals("archive")) {
                // reset archive use back to 0
                new ArchiveReset(plugin, uuid.toString(), 0).resetUse();
            }
            if (tud.getSchematic().getPermission().equals("coral") && tud.getPrevious().getConsoleSize().equals(ConsoleSize.TALL)) {
                // clean up space above coral console
                int tidy = starty + h;
                int plus = 32 - h;
                chunks.forEach((chk) -> setAir(chk.getBlock(0, 64, 0).getX(), tidy, chk.getBlock(0, 64, 0).getZ(), chk.getWorld(), plus));
            }
            // add / remove chunks from the chunks table
            HashMap<String, Object> wherec = new HashMap<>();
            wherec.put("tardis_id", id);
            qf.doDelete("chunks", wherec);
            List<Chunk> chunkList = plugin.getInteriorBuilder().getChunks(world, wg1.getChunk().getX(), wg1.getChunk().getZ(), w, c);
            // update chunks list in DB
            chunkList.forEach((hunk) -> {
                HashMap<String, Object> setc = new HashMap<>();
                setc.put("tardis_id", id);
                setc.put("world", world.getName());
                setc.put("x", hunk.getX());
                setc.put("z", hunk.getZ());
                qf.doInsert("chunks", setc);
            });
            // remove blocks from condenser table if necessary
            if (!clean && plugin.getGeneralKeeper().getRoomCondenserData().containsKey(uuid)) {
                TARDISCondenserData c_data = plugin.getGeneralKeeper().getRoomCondenserData().get(uuid);
                int amount = 0;
                for (Map.Entry<String, Integer> entry : c_data.getBlockIDCount().entrySet()) {
                    HashMap<String, Object> whered = new HashMap<>();
                    whered.put("tardis_id", c_data.getTardis_id());
                    whered.put("block_data", entry.getKey());
                    qf.alterCondenserBlockCount(entry.getValue(), whered);
                    amount += entry.getValue() * plugin.getCondensables().get(entry.getKey());
                }
                plugin.getGeneralKeeper().getRoomCondenserData().remove(uuid);
                if (amount > 0) {
                    HashMap<String, Object> wheret = new HashMap<>();
                    wheret.put("tardis_id", id);
                    qf.alterEnergyLevel("tardis", -amount, wheret, player);
                }
            }
            // cancel the task
            plugin.getServer().getScheduler().cancelTask(taskID);
            taskID = 0;
            String message = (clean) ? "REPAIR_CLEAN" : "REPAIR_DONE";
            TARDISMessage.send(player, message);
        } else {
            JSONArray floor = (JSONArray) arr.get(level);
            JSONArray r = (JSONArray) floor.get(row);
            // place a row of blocks
            for (int col = 0; col < c; col++) {
                JSONObject bb = (JSONObject) r.get(col);
                int x = startx + row;
                int y = starty + level;
                int z = startz + col;
                // if we're setting the biome to sky, do it now
                if (plugin.getConfig().getBoolean("creation.sky_biome") && level == 0) {
                    world.setBiome(x, z, Biome.THE_VOID);
                }
                BlockData data = plugin.getServer().createBlockData(bb.getString("data"));
                Material type = data.getMaterial();
                if (type.equals(Material.BEDROCK)) {
                    // remember bedrock location to block off the beacon light
                    String bedrocloc = world.getName() + ":" + x + ":" + y + ":" + z;
                    set.put("beacon", bedrocloc);
                    postBedrock = world.getBlockAt(x, y, z);
                }
                if (type.equals(Material.NOTE_BLOCK)) {
                    // remember the location of this Disk Storage
                    String storage = TARDISStaticLocationGetters.makeLocationStr(world, x, y, z);
                    qf.insertSyncControl(id, 14, storage, 0);
                }
                if (type.equals(Material.ORANGE_WOOL)) {
                    type = wall_type;
                }
                if (type.equals(Material.LIGHT_GRAY_WOOL)) {
                    type = floor_type;
                }
                if (type.equals(Material.SPAWNER)) { // scanner button
                    /*
                     * mob spawner will be converted to the correct id by
                     * setBlock(), but remember it for the scanner.
                     */
                    String scanloc = world.getName() + ":" + x + ":" + y + ":" + z;
                    set.put("scanner", scanloc);
                }
                if (type.equals(Material.CHEST)) {
                    // remember the location of the condenser chest
                    String chest = world.getName() + ":" + x + ":" + y + ":" + z;
                    set.put("condenser", chest);
                }
                if (type.equals(Material.IRON_DOOR)) {
                    Bisected bisected = (Bisected) data;
                    if (bisected.getHalf().equals(Bisected.Half.BOTTOM)) { // iron door bottom
                        HashMap<String, Object> setd = new HashMap<>();
                        String doorloc = world.getName() + ":" + x + ":" + y + ":" + z;
                        setd.put("door_location", doorloc);
                        HashMap<String, Object> whered = new HashMap<>();
                        whered.put("tardis_id", id);
                        whered.put("door_type", 1);
                        qf.doUpdate("doors", setd, whered);
                        // if create_worlds is true, set the world spawn
                        if (own_world) {
                            world.setSpawnLocation(x, y, (z + 1));
                        }
                    }
                }
                if (type.equals(Material.STONE_BUTTON)) { // random button
                    // remember the location of this button
                    String button = TARDISStaticLocationGetters.makeLocationStr(world, x, y, z);
                    qf.insertSyncControl(id, 1, button, 0);
                }
                if (type.equals(Material.JUKEBOX)) {
                    // remember the location of this Advanced Console
                    String advanced = TARDISStaticLocationGetters.makeLocationStr(world, x, y, z);
                    qf.insertSyncControl(id, 15, advanced, 0);
                }
                if (type.equals(Material.CAKE)) {
                    /*
                     * This block will be converted to a lever by setBlock(),
                     * but remember it so we can use it as the handbrake!
                     */
                    String handbrakeloc = TARDISStaticLocationGetters.makeLocationStr(world, x, y, z);
                    qf.insertSyncControl(id, 0, handbrakeloc, 0);
                }
                if (type.equals(Material.REDSTONE_LAMP) || type.equals(Material.SEA_LANTERN)) {
                    // remember lamp blocks
                    Block lamp = world.getBlockAt(x, y, z);
                    lampblocks.add(lamp);
                    // remember lamp block locations for malfunction and light switch
                    HashMap<String, Object> setlb = new HashMap<>();
                    String lloc = world.getName() + ":" + x + ":" + y + ":" + z;
                    setlb.put("tardis_id", id);
                    setlb.put("location", lloc);
                    qf.doInsert("lamps", setlb);
                }
                if (type.equals(Material.COMMAND_BLOCK) || ((tud.getSchematic().getPermission().equals("bigger") || tud.getSchematic().getPermission().equals("coral") || tud.getSchematic().getPermission().equals("deluxe") || tud.getSchematic().getPermission().equals("twelfth")) && type.equals(Material.BEACON))) {
                    /*
                     * command block - remember it to spawn the creeper on.
                     * could also be a beacon block, as the creeper sits over
                     * the beacon in the deluxe and bigger consoles.
                     */
                    String creeploc = world.getName() + ":" + (x + 0.5) + ":" + y + ":" + (z + 0.5);
                    set.put("creeper", creeploc);
                    if (type.equals(Material.COMMAND_BLOCK)) {
//                            type = Material.STONE_BRICKS;
                        data = Material.STONE_BRICKS.createBlockData();
                        if (tud.getSchematic().getPermission().equals("ender")) {
                            data = Material.END_STONE_BRICKS.createBlockData();
                        }
                    }
                }
                if (type.equals(Material.OAK_BUTTON)) {
                    /*
                     * Remember it for the Artron Energy Capacitor.
                     */
                    String woodbuttonloc = TARDISStaticLocationGetters.makeLocationStr(world, x, y, z);
                    qf.insertSyncControl(id, 6, woodbuttonloc, 0);
                }
                if (type.equals(Material.DAYLIGHT_DETECTOR)) {
                    /*
                     * remember the telepathic circuit.
                     */
                    String telepathicloc = TARDISStaticLocationGetters.makeLocationStr(world, x, y, z);
                    qf.insertSyncControl(id, 23, telepathicloc, 0);
                }
                if (type.equals(Material.BEACON) && tud.getSchematic().getPermission().equals("ender")) {
                    /*
                     * get the ender crytal location
                     */
                    ender = world.getBlockAt(x, y, z).getLocation().add(0.5d, 4d, 0.5d);
                }
                // if it's an iron/gold/diamond/emerald/beacon/redstone block put it in the blocks table
                if (TARDISBuilderInstanceKeeper.getPrecious().contains(type) && !type.equals(Material.BEDROCK)) {
                    HashMap<String, Object> setpb = new HashMap<>();
                    String loc = TARDISStaticLocationGetters.makeLocationStr(world, x, y, z);
                    setpb.put("tardis_id", id);
                    setpb.put("location", loc);
                    setpb.put("data", "minecraft:air");
                    setpb.put("police_box", 0);
                    qf.doInsert("blocks", setpb);
                    plugin.getGeneralKeeper().getProtectBlockMap().put(loc, id);
                }
                // if it's the door, don't set it just remember its block then do it at the end
                if (type.equals(Material.IRON_DOOR)) { // doors
                    postDoorBlocks.put(world.getBlockAt(x, y, z), data);
                } else if (type.equals(Material.LEVER)) {
                    postLeverBlocks.put(world.getBlockAt(x, y, z), data);
                } else if (type.equals(Material.REDSTONE_TORCH)) {
                    postRedstoneTorchBlocks.put(world.getBlockAt(x, y, z), data);
                } else if (type.equals(Material.TORCH)) {
                    postTorchBlocks.put(world.getBlockAt(x, y, z), data);
                } else if (type.equals(Material.STICKY_PISTON)) {
                    postStickyPistonBaseBlocks.put(world.getBlockAt(x, y, z), data);
                } else if (type.equals(Material.PISTON)) {
                    postPistonBaseBlocks.put(world.getBlockAt(x, y, z), data);
                } else if (type.equals(Material.PISTON_HEAD)) {
                    postPistonExtensionBlocks.put(world.getBlockAt(x, y, z), data);
                } else if (Tag.WALL_SIGNS.isTagged(type)) {
                    postSignBlocks.put(world.getBlockAt(x, y, z), data);
                } else if (TARDISMaterials.infested.contains(type)) {
                    // legacy monster egg stone for controls
                    TARDISBlockSetters.setBlock(world, x, y, z, Material.AIR);
                } else if (type.equals(Material.MUSHROOM_STEM)) { // mushroom stem for repeaters
                    // save repeater location
                    if (j < 6) {
                        String repeater = world.getName() + ":" + x + ":" + y + ":" + z;
                        data = Material.REPEATER.createBlockData();
                        Directional directional = (Directional) data;
                        switch (j) {
                            case 2:
                                directional.setFacing(BlockFace.EAST);
                                data = directional;
                                postRepeaterBlocks.put(world.getBlockAt(x, y, z), data);
                                qf.insertSyncControl(id, 3, repeater, 0);
                                break;
                            case 3:
                                directional.setFacing(BlockFace.SOUTH);
                                data = directional;
                                postRepeaterBlocks.put(world.getBlockAt(x, y, z), data);
                                qf.insertSyncControl(id, 2, repeater, 0);
                                break;
                            case 4:
                                directional.setFacing(BlockFace.NORTH);
                                data = directional;
                                postRepeaterBlocks.put(world.getBlockAt(x, y, z), data);
                                qf.insertSyncControl(id, 5, repeater, 0);
                                break;
                            default:
                                directional.setFacing(BlockFace.WEST);
                                data = directional;
                                postRepeaterBlocks.put(world.getBlockAt(x, y, z), data);
                                qf.insertSyncControl(id, 4, repeater, 0);
                                break;
                        }
                        j++;
                    }
                } else if (type.equals(Material.SPONGE)) {
                    TARDISBlockSetters.setBlock(world, x, y, z, Material.AIR);
                } else {
                    Block tmp = world.getBlockAt(x, y, z);
                    if (clean && !tmp.getType().equals(type) && !plugin.getUtils().isAir(tmp.getType())) {
                        TARDISBlockSetters.setBlock(world, x, y, z, data);
                    } else if (!tmp.getType().equals(type)) {
                        TARDISBlockSetters.setBlock(world, x, y, z, data);
                    }
                }
            }
            // remove items
            chunks.forEach((chink) -> {
                // remove dropped items
                for (Entity e : chink.getEntities()) {
                    if (e instanceof Item) {
                        e.remove();
                    }
                }
            });
            if (row < w) {
                row++;
            }
            if (row == w && level < h) {
                row = 0;
                level++;
            }
        }
    }

    private void setAir(int jx, int jy, int jz, World jw, int plusy) {
        for (int yy = jy; yy < (jy + plusy); yy++) {
            for (int xx = jx; xx < (jx + 16); xx++) {
                for (int zz = jz; zz < (jz + 16); zz++) {
                    Block b = jw.getBlockAt(xx, yy, zz);
                    b.setBlockData(TARDISConstants.AIR);
                }
            }
        }
    }

    private List<Chunk> getChunks(Chunk c, SCHEMATIC s) {
        List<Chunk> chinks = new ArrayList<>();
        chinks.add(c);
        if (!s.getConsoleSize().equals(ConsoleSize.SMALL)) {
            chinks.add(c.getWorld().getChunkAt(c.getX() + 1, c.getZ()));
            chinks.add(c.getWorld().getChunkAt(c.getX(), c.getZ() + 1));
            chinks.add(c.getWorld().getChunkAt(c.getX() + 1, c.getZ() + 1));
        }
        return chinks;
    }
}
