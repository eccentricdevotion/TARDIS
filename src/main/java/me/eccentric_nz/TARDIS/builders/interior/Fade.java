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
import me.eccentric_nz.TARDIS.discovery.Placer;
import me.eccentric_nz.TARDIS.discovery.StructureVariant;
import me.eccentric_nz.TARDIS.enumeration.UseClay;
import me.eccentric_nz.TARDIS.floodgate.TARDISFloodgate;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
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

public class Fade {

    private final TARDIS plugin;
    private final StructureVariant variant;
    private final Location location;
    private final int dbID;
    private final Player player;
    private UseClay use_clay;
    private int counter = 0;
    private BossBar bb;
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
        where.put("tardis_id", id);
    }

    public void build(boolean bossbar) {
        // async task to populate box with block fade
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            String[][][] template = new Template().load(plugin);
            BlockData[][][] base = BaseBlocks.set(template, variant);
            BlockData[][][] fade = new FadeMutator(TARDISConstants.RANDOM.nextLong(), 7, variant).applyGradient(base, variant.gradient());
            // switch back to the main thread
            plugin.getServer().getScheduler().runTask(plugin, () -> {
                if (bossbar) {
                    // start progress bar
                    bb = Bukkit.createBossBar(TARDISConstants.GROWTH_STATES.getFirst(), BarColor.WHITE, BarStyle.SOLID, TARDISConstants.EMPTY_ARRAY);
                    bb.setProgress(0);
                    bb.addPlayer(player);
                    bb.setVisible(true);
                }
                // place blocks
                place(location, fade, variant, bossbar);
            });
        });
    }

    private void place(Location location, BlockData[][][] materials, StructureVariant variant, boolean bossbar) {
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
                    counter++;
                    BlockData data = materials[i][j][k];
                    Block block = world.getBlockAt(x + j, y + i, z + k);
                    BlockState state = block.getState();
                    state.setBlockData(data);
                    // add hanging lights
                    if (i > 12 && i < 15 && !data.getMaterial().isAir()) {
                        Block below = block.getRelative(BlockFace.DOWN);
                        if (below.getType().isAir() && TARDISConstants.RANDOM.nextInt(100) < 4) {
                            placer.addHangingLights(below, variant, 5);
                        }
                    }
                    if (data.getMaterial() == Material.CLAY) {
                        placer.addWaterFeature(block, variant);
                    }
                    // process TARDIS special blocks
                    if (data == Material.BLUE_WOOL.createBlockData()) {
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
                    if (data.getMaterial() == Material.NOTE_BLOCK) {
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
                    if (data.getMaterial() == Material.CHEST) {
                        // remember the location of the condenser chest
                        String condenser = block.getLocation().toString();
                        plugin.getQueryFactory().insertSyncControl(dbID, 34, condenser, 0);
                    }
                    if (data.getMaterial() == Material.IRON_DOOR) {
                        if (d == 0) { // iron door bottom
                            HashMap<String, Object> setd = new HashMap<>();
                            String doorLocation = world.getKey().asString() + ":" + (x + j) + ":" + (y + i) + ":" + (z + k);
                            setd.put("door_location", doorLocation);
                            if (bossbar) {
                                setd.put("tardis_id", dbID);
                                setd.put("door_type", 1);
                                setd.put("door_direction", "SOUTH");
                                plugin.getQueryFactory().doInsert("doors", setd);
                            } else {
                                HashMap<String, Object> whered = new HashMap<>();
                                whered.put("tardis_id", dbID);
                                whered.put("door_type", 1);
                                plugin.getQueryFactory().doSyncUpdate("doors", setd, whered);
                            }
                            // if create_worlds is true, set the world spawn
                            if (plugin.getConfig().getBoolean("creation.create_worlds")) {
                                world.setSpawnLocation(x + j, y + i, (z + k + 1));
                            }
                            d++;
                        }
                    }
                    if (data.getMaterial() == Material.STONE_BUTTON) { // random button
                        // remember the location of this button
                        String button = block.getLocation().toString();
                        plugin.getQueryFactory().insertSyncControl(dbID, 1, button, 0);
                    }
                    if (data.getMaterial() == Material.JUKEBOX) {
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
                    // create default json for ARS only if this is a new TARDIS
                    if (data.getMaterial() == Material.LEVER && bossbar) {
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
                    if (data.getMaterial() == Material.COMMAND_BLOCK) {
                        // command block - remember it to spawn the creeper on
                        String creeperLocation = world.getKey().asString() + ":" + (x + j + 0.5) + ":" + (y + i) + ":" + (z + k + 0.5);
                        set.put("creeper", creeperLocation);
                        state.setType(variant.base());
                    }
                    if (data.getMaterial() == Material.OAK_BUTTON) {
                        // wood button - remember it for the Artron Energy Capacitor
                        String woodButtonLocation = block.getLocation().toString();
                        plugin.getQueryFactory().insertSyncControl(dbID, 6, woodButtonLocation, 0);
                    }
                    if (data.getMaterial() == Material.OAK_WALL_SIGN) {
                        String controlLocation = block.getLocation().toString();
                        plugin.getQueryFactory().insertSyncControl(dbID, 22, controlLocation, 0);
                        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                            BlockState newState = block.getState();
                            if (newState instanceof Sign sign) {
                                sign.setWaxed(true);
                                sign.update(true, false);
                            }
                        }, 5L);
                    }
                    if (data.getMaterial() == Material.RED_MUSHROOM_BLOCK) { // red mushroom block for repeaters
                        // save repeater location
                        String rl = world.getKey().asString() + ":" + (x + j) + ":" + (y + i) + ":" + (z + k);
                        BlockData repeater = Material.REPEATER.createBlockData();
                        Directional directional = (Directional) repeater;
                        switch (p) {
                            case 1 -> {
                                directional.setFacing(BlockFace.NORTH);
                                state.setBlockData(directional);
                                plugin.getQueryFactory().insertSyncControl(dbID, 2, rl, 0);
                            }
                            case 2 -> {
                                directional.setFacing(BlockFace.SOUTH);
                                state.setBlockData(directional);
                                plugin.getQueryFactory().insertSyncControl(dbID, 5, rl, 0);
                            }
                            case 3 -> {
                                directional.setFacing(BlockFace.EAST);
                                state.setBlockData(directional);
                                plugin.getQueryFactory().insertSyncControl(dbID, 4, rl, 0);
                            }
                            default -> { // 0
                                directional.setFacing(BlockFace.WEST);
                                state.setBlockData(directional);
                                plugin.getQueryFactory().insertSyncControl(dbID, 3, rl, 0);
                            }
                        }
                        p++;
                    }
                    state.update(true, false);
                    if (i < 13 && data.getMaterial() == variant.base()) {
                        baseBlocks.add(block);
                    }
                    if (bossbar) {
                        double progress = counter / div;
                        if (progress > 1.0) {
                            progress = 1.0;
                        }
                        bb.setProgress(progress);
                    }
                }
            }
            if (bossbar) {
                // set progress bar title
                if (i == h - 1) {
                    bb.setTitle(TARDISConstants.GROWTH_STATES.get(31));
                } else {
                    bb.setTitle(TARDISConstants.GROWTH_STATES.get(i));
                }
            }
        }
        for (Block block : baseBlocks) {
            placer.addGlowLichen(block);
            placer.addCarpet(block, variant);
            placer.addHead(block, variant);
        }
        // finished processing - update tardis table!
        plugin.getQueryFactory().doUpdate("tardis", set, where);
        // give kit? only if new TARDIS
        if (plugin.getKitsConfig().getBoolean("give.create.enabled") && bossbar) {
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
        if (bossbar) {
            bb.setProgress(1);
            bb.setVisible(false);
            bb.removeAll();
        }
    }
}
