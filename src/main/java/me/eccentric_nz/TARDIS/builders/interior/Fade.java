package me.eccentric_nz.TARDIS.builders.interior;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.customblocks.TARDISBlockDisplayItem;
import me.eccentric_nz.TARDIS.customblocks.TARDISDisplayItemUtils;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetAchievements;
import me.eccentric_nz.TARDIS.discovery.Mutator;
import me.eccentric_nz.TARDIS.discovery.Placer;
import me.eccentric_nz.TARDIS.discovery.StructureVariant;
import me.eccentric_nz.TARDIS.enumeration.UseClay;
import me.eccentric_nz.TARDIS.floodgate.TARDISFloodgate;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Fade {

    private final TARDIS plugin;
    private final StructureVariant variant;
    private final Location location;
    private final int dbID;
    private final Player player;
    private UseClay use_clay;
    private int counter = 0;
    private BossBar bb;
    private final List<BlockData> bars = new ArrayList<>();
    private final HashMap<String, Object> set = new HashMap<>();
    private final HashMap<String, Object> where = new HashMap<>();

    public Fade(TARDIS plugin, StructureVariant variant, Location location, int id, Player player) {
        this.plugin = plugin;
        this.variant = variant;
        this.location = location;
        this.dbID = id;
        this.player = player;
        try {
            use_clay = UseClay.valueOf(plugin.getConfig().getString("creation.use_clay"));
        } catch (IllegalArgumentException e) {
            use_clay = UseClay.WOOL;
        }
        Chunk c = location.getChunk();
        String chunk = location.getWorld().getKey().asString() + ":" + c.getX() + ":" + c.getZ();
        set.put("chunk", chunk);
        where.put("tardis_id", id);
    }

    public void build() {
        // async task to populate box with block fade
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            String[][][] template = new Template().load(plugin);
            Material[][][] base = setBaseBlocks(template, variant);
            Material[][][] fade = new Mutator(TARDISConstants.RANDOM.nextLong(), 7, null).applyGradient(base, variant.gradient());
            // switch back to the main thread
            plugin.getServer().getScheduler().runTask(plugin, () -> {
                place(location, fade, variant);
                // start progress bar
                bb = Bukkit.createBossBar(TARDISConstants.GROWTH_STATES.getFirst(), BarColor.WHITE, BarStyle.SOLID, TARDISConstants.EMPTY_ARRAY);
                bb.setProgress(0);
                bb.addPlayer(player);
                bb.setVisible(true);
            });
        });
    }

    public Material[][][] setBaseBlocks(String[][][] array, StructureVariant variant) {
        Material[][][] materials = new Material[array.length][array[0].length][array[0][0].length];
        Pattern pattern = Pattern.compile("\\[.*?\\]");
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[i].length; j++) {
                for (int k = 0; k < array[i][j].length; k++) {
                    counter++;
                    String material = array[i][j][k];
                    if (material.contains("IRON_BARS")) {
                        materials[i][j][k] = Material.IRON_BARS;
                        // add BlockData to bars list
                        Matcher matcher = pattern.matcher(material);
                        if (matcher.find()) {
                            bars.add(Material.IRON_BARS.createBlockData(matcher.group()));
                        }
                    } else if (material.equals("TUFF")) {
                        materials[i][j][k] = variant.base();
                    } else if (material.equals("TUFF_WALL")) {
                        materials[i][j][k] = variant.wall();
                    } else if (material.equals("MUDDY_MANGROVE_ROOTS")) {
                        materials[i][j][k] = variant.seed();
                    } else {
                        materials[i][j][k] = Material.getMaterial(material);
                    }
                }
            }
        }
        return materials;
    }

    private void place(Location location, Material[][][] materials, StructureVariant variant) {
        int b = 0;
        int d = 0;
        int p = 0;
        String playerUUID = player.getUniqueId().toString();
        Placer placer = new Placer(plugin);
        List<Block> baseBlocks = new ArrayList<>();
        World world = location.getWorld();
        int x = location.getBlockX();
        int y = location.getBlockY();
        int z = location.getBlockZ();
        int h = materials.length;
        int r = materials[0].length;
        int w = materials[0][0].length;
        double div = (h + 1.0d) * w * (d + 1.0d);
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < r; j++) {
                for (int k = 0; k < w; k++) {
                    Material material = materials[i][j][k];
                    Block block = world.getBlockAt(x + j, y + i, z + k);
                    BlockState state = block.getState();
                    if (material == Material.IRON_BARS) {
                        state.setBlockData(bars.get(b));
                        b++;
                    } else {
                        state.setType(material);
                    }
                    // add hanging lights
                    if (i > 12 && i < 15 && material != Material.AIR) {
                        Block below = block.getRelative(BlockFace.DOWN);
                        if (below.getType() == Material.AIR && TARDISConstants.RANDOM.nextInt(100) < 4) {
                            placer.addHangingLights(below, variant, 5);
                        }
                    }
                    if (material == Material.CLAY) {
                        placer.addWaterFeature(block, variant);
                    }
                    // process TARDIS special blocks
                    // TODO Pistons?
                    if (material == Material.BLUE_WOOL) {
                        switch (use_clay) {
                            case TERRACOTTA -> state.setType(Material.BLUE_TERRACOTTA);
                            case CONCRETE -> state.setType(Material.BLUE_CONCRETE);
                            default -> {
                                if (!TARDISFloodgate.isFloodgateEnabled() || !TARDISFloodgate.isBedrockPlayer(player)) {
                                    state.setType(Material.BARRIER);
                                    // spawn an item display entity
                                    TARDISDisplayItemUtils.set(TARDISBlockDisplayItem.BLUE_BOX, world, x, y, z);
                                }
                            }
                        }
                    }
                    if (material == Material.NOTE_BLOCK) {
                        // remember the location of this Disk Storage
                        String storage = block.getLocation().toString();
                        plugin.getQueryFactory().insertSyncControl(dbID, 14, storage, 0);
                        if (!TARDISFloodgate.isFloodgateEnabled() || !TARDISFloodgate.isBedrockPlayer(player)) {
                            // set block data to BARRIER
                            state.setType(Material.BARRIER);
                            // spawn an item display entity
                            TARDISDisplayItemUtils.set(TARDISBlockDisplayItem.DISK_STORAGE, world, x + j, y + i, z + k);
                        }
                    }
                    if (material == Material.CHEST) {
                        // remember the location of the condenser chest
                        String condenser = block.getLocation().toString();
                        plugin.getQueryFactory().insertSyncControl(dbID, 34, condenser, 0);
                    }
                    if (material == Material.IRON_DOOR) {
                        if (d == 0) { // iron door bottom
                            state.setBlockData(Material.IRON_DOOR.createBlockData("[facing=north,half=lower,hinge=left]"));
                            HashMap<String, Object> setd = new HashMap<>();
                            String doorLocation = world.getKey().asString() + ":" + (x + j) + ":" + (y + i) + ":" + (z + k);
                            setd.put("tardis_id", dbID);
                            setd.put("door_type", 1);
                            setd.put("door_location", doorLocation);
                            setd.put("door_direction", "SOUTH");
                            plugin.getQueryFactory().doInsert("doors", setd);
                            // if create_worlds is true, set the world spawn
                            if (plugin.getConfig().getBoolean("creation.create_worlds")) {
                                world.setSpawnLocation(x + j, y + i, (z + k + 1));
                            }
                            d++;
                        } else {
                            state.setBlockData(Material.IRON_DOOR.createBlockData("[facing=north,half=upper,hinge=left]"));
                        }
                    }
                    if (material == Material.STONE_BUTTON) { // random button
                        state.setBlockData(Material.STONE_BUTTON.createBlockData("[facing=south,face=floor]"));
                        // remember the location of this button
                        String button = block.getLocation().toString();
                        plugin.getQueryFactory().insertSyncControl(dbID, 1, button, 0);
                    }
                    if (material == Material.JUKEBOX) {
                        // remember the location of this Advanced Console
                        String advanced = block.getLocation().toString();
                        plugin.getQueryFactory().insertSyncControl(dbID, 15, advanced, 0);
                        // check if player has storage record, and update the tardis_id field
                        plugin.getUtils().updateStorageId(playerUUID, dbID);
                        if (!TARDISFloodgate.isFloodgateEnabled() || !TARDISFloodgate.isBedrockPlayer(player)) {
                            // set block data to correct BARRIER + Item Display
                            state.setType(Material.BARRIER);
                            // spawn an item display entity
                            TARDISDisplayItemUtils.set(TARDISBlockDisplayItem.ADVANCED_CONSOLE, world, x + j, y + i, z + k);
                        }
                    }
                    if (material == Material.LEVER) {
                        state.setBlockData(Material.LEVER.createBlockData("[facing=south,face=floor]"));
                        // create default json for ARS
                        String[][][] empty = new String[3][9][9];
                        for (int ars_y = 0; ars_y < 3; ars_y++) {
                            for (int ars_x = 0; ars_x < 9; ars_x++) {
                                for (int ars_z = 0; ars_z < 9; ars_z++) {
                                    empty[ars_y][ars_x][ars_z] = "STONE";
                                }
                            }
                        }
                        String control = "ORANGE_POPLAR_LEAVES";
                        empty[1][4][4] = control;
                        empty[1][4][5] = control;
                        empty[1][5][4] = control;
                        empty[1][5][5] = control;
                        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
                        JsonArray json = JsonParser.parseString(gson.toJson(empty)).getAsJsonArray();
                        HashMap<String, Object> seta = new HashMap<>();
                        seta.put("tardis_id", dbID);
                        seta.put("uuid", playerUUID);
                        seta.put("json", json.toString());
                        plugin.getQueryFactory().doInsert("ars", seta);
                    }
                    if (material == Material.COMMAND_BLOCK) {
                        // command block - remember it to spawn the creeper on
                        String creeperLocation = world.getKey().asString() + ":" + (x + j + 0.5) + ":" + (y + i) + ":" + (z + k + 0.5);
                        set.put("creeper", creeperLocation);
                        state.setType(variant.base());
                    }
                    if (material == Material.OAK_BUTTON) {
                        state.setBlockData(Material.OAK_BUTTON.createBlockData("[facing=west,face=floor]"));
                        // wood button - remember it for the Artron Energy Capacitor
                        String woodButtonLocation = block.getLocation().toString();
                        plugin.getQueryFactory().insertSyncControl(dbID, 6, woodButtonLocation, 0);
                    }
                    if (material == Material.OAK_WALL_SIGN) {
                        state.setBlockData(Material.OAK_SIGN.createBlockData("[facing=north]"));
                        String controlLocation = block.getLocation().toString();
                        plugin.getQueryFactory().insertSyncControl(dbID, 22, controlLocation, 0);
                        ((Sign) state).setWaxed(true);
                    }
                    if (material == Material.RED_MUSHROOM_BLOCK) { // red mushroom block for repeaters
                        // save repeater location
                        String repeater = world.getKey().asString() + ":" + (x + j) + ":" + (y + i) + ":" + (z + k);
                        BlockData data = Material.REPEATER.createBlockData();
                        Directional directional = (Directional) data;
                        switch (p) {
                            case 1 -> {
                                directional.setFacing(BlockFace.WEST);
                                state.setBlockData(directional);
                                plugin.getQueryFactory().insertSyncControl(dbID, 3, repeater, 0);
                            }
                            case 2 -> {
                                directional.setFacing(BlockFace.NORTH);
                                state.setBlockData(directional);
                                plugin.getQueryFactory().insertSyncControl(dbID, 2, repeater, 0);
                            }
                            case 3 -> {
                                directional.setFacing(BlockFace.SOUTH);
                                state.setBlockData(directional);
                                plugin.getQueryFactory().insertSyncControl(dbID, 5, repeater, 0);
                            }
                            default -> { // 0
                                directional.setFacing(BlockFace.EAST);
                                state.setBlockData(directional);
                                plugin.getQueryFactory().insertSyncControl(dbID, 4, repeater, 0);
                            }
                        }
                        p++;
                    }
                    state.update(true, false);
                    if (i < 24 && material == variant.base()) {
                        baseBlocks.add(block);
                    }
                    double progress = counter / div;
                    if (progress > 1.0) {
                        progress = 1.0;
                    }
                    bb.setProgress(progress);
                }
            }
            // set progress bar title
            if (i == h - 1) {
                bb.setTitle(TARDISConstants.GROWTH_STATES.get(31));
            } else {
                bb.setTitle(TARDISConstants.GROWTH_STATES.get(i));
            }
        }
        for (Block block : baseBlocks) {
            placer.addGlowLichen(block);
            placer.addCarpet(block, variant);
            placer.addHead(block, variant);
        }
        // finished processing - update tardis table!
        plugin.getQueryFactory().doUpdate("tardis", set, where);
        // give kit?
        if (plugin.getKitsConfig().getBoolean("give.create.enabled")) {
            if (TARDISPermission.hasPermission(player, "tardis.kit.create")) {
                // check if they have the tardis kit
                HashMap<String, Object> wherek = new HashMap<>();
                wherek.put("uuid", playerUUID);
                wherek.put("name", "createkit");
                ResultSetAchievements rsa = new ResultSetAchievements(plugin, wherek);
                if (!rsa.resultSet()) {
                    //add a record
                    HashMap<String, Object> setk = new HashMap<>();
                    setk.put("uuid", playerUUID);
                    setk.put("name", "createkit");
                    plugin.getQueryFactory().doInsert("achievements", setk);
                    // give the join kit
                    String kit = plugin.getKitsConfig().getString("give.create.kit");
                    plugin.getServer().dispatchCommand(plugin.getConsole(), "tardisgive " + player.getName() + " kit " + kit);
                }
            }
        }
        bb.setProgress(1);
        bb.setVisible(false);
        bb.removeAll();
    }
}
