/*
 *  Copyright 2015 eccentric_nz.
 */
package me.eccentric_nz.TARDIS.desktop;

import java.io.File;
import java.util.HashMap;
import java.util.UUID;
import me.eccentric_nz.TARDIS.JSON.JSONArray;
import me.eccentric_nz.TARDIS.JSON.JSONObject;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.builders.TARDISInteriorPostioning;
import me.eccentric_nz.TARDIS.builders.TARDISTIPSData;
import me.eccentric_nz.TARDIS.database.ResultSetPlayerPrefs;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.enumeration.USE_CLAY;
import me.eccentric_nz.TARDIS.schematic.TARDISSchematicGZip;
import me.eccentric_nz.TARDIS.utility.TARDISMaterials;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.World;
import org.bukkit.block.Block;

/**
 *
 * @author eccentric_nz
 */
public class TARDISUpgradeBlockScanner {

    private final TARDIS plugin;
    private final TARDISUpgradeData tud;
    private final UUID uuid;
    int startx, starty, startz;
    int count = 0;
    Material type;
    byte data;

    public TARDISUpgradeBlockScanner(TARDIS plugin, TARDISUpgradeData tud, UUID uuid) {
        this.plugin = plugin;
        this.tud = tud;
        this.uuid = uuid;
    }

    public TARDISBlockScannerData check() {
        String directory = (tud.getPrevious().isCustom()) ? "user_schematics" : "schematics";
        String path = plugin.getDataFolder() + File.separator + directory + File.separator + tud.getPrevious().getPermission() + ".tschm";
        File file = new File(path);
        if (!file.exists()) {
            plugin.debug(plugin.getPluginName() + "Could not find a schematic with that name!");
            return null;
        }
        // get JSON
        JSONObject obj = TARDISSchematicGZip.unzip(path);
        // get dimensions
        JSONObject dimensions = (JSONObject) obj.get("dimensions");
        int h = dimensions.getInt("height");
        int w = dimensions.getInt("width");
        int l = dimensions.getInt("length");
        float v = h * w * l;
        // calculate startx, starty, startz
        HashMap<String, Object> wheret = new HashMap<>();
        wheret.put("uuid", uuid.toString());
        ResultSetTardis rs = new ResultSetTardis(plugin, wheret, "", false, 0);
        if (rs.resultSet()) {
            Tardis tardis = rs.getTardis();
            int slot = tardis.getTIPS();
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
            starty = (tud.getPrevious().getPermission().equals("redstone")) ? 65 : 64;
            String[] split = tardis.getChunk().split(":");
            World world = plugin.getServer().getWorld(split[0]);
            Material wall_type;
            Material floor_type;
            // get wall/floor block prefs from database...
            HashMap<String, Object> where = new HashMap<>();
            where.put("uuid", uuid.toString());
            ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, where);
            if (rsp.resultSet()) {
                wall_type = Material.getMaterial(rsp.getWall());
                floor_type = Material.getMaterial(rsp.getFloor());
            } else {
                wall_type = Material.ORANGE_WOOL;
                floor_type = Material.LIGHT_GRAY_WOOL;
            }
            String beacon = "";
            // get input array
            JSONArray arr = (JSONArray) obj.get("input");
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
                        Block b = world.getBlockAt(x, y, z);
                        if (type.equals(Material.ORANGE_WOOL)) {
                            type = wall_type;
                        }
                        if (type.equals(Material.LIGHT_GRAY_WOOL)) {
                            type = floor_type;
                        }
                        if (type.equals(Material.SPONGE)) {
                            type = Material.AIR;
                        }
                        if (type.equals(Material.CAKE)) {
                            type = Material.LEVER;
                        }
                        if (type.equals(Material.MOB_SPAWNER)) {
                            type = Material.OAK_BUTTON;
                        }
                        if (type.equals(Material.RED_MUSHROOM_BLOCK)) {
                            type = Material.REPEATER;
                        }
                        if (TARDISMaterials.infested.contains(type)) {
                            type = Material.AIR;
                        }
                        if (type.equals(Material.BEDROCK)) {
                            type = Material.GLASS;
                            beacon = world.getName() + ":" + x + ":" + y + ":" + z;
                        }
                        if (type.equals(Material.COMMAND_BLOCK)) {
                            type = Material.STONE_BRICKS;
                        }
                        if (Tag.WOOL.isTagged(type)) {
                            // determine 'use_clay' material
                            USE_CLAY use_clay;
                            try {
                                use_clay = USE_CLAY.valueOf(plugin.getConfig().getString("creation.use_clay"));
                            } catch (IllegalArgumentException e) {
                                use_clay = USE_CLAY.WOOL;
                            }
                            switch (type) {
                                case ORANGE_WOOL:
                                    switch (wall_type) {
                                        case LAPIS_BLOCK: // if using the default Lapis Block - then use Orange Wool / Terracotta
                                            switch (use_clay) {
                                                case TERRACOTTA:
                                                    type = Material.ORANGE_TERRACOTTA;
                                                    break;
                                                case CONCRETE:
                                                    type = Material.ORANGE_CONCRETE;
                                                    break;
                                                default:
                                                    type = Material.ORANGE_WOOL;
                                                    break;
                                            }
                                            break;
                                        default:
                                            type = wall_type;
                                    }
                                    break;
                                case LIGHT_GRAY_WOOL:
                                    if (!tud.getSchematic().getPermission().equals("eleventh")) {
                                        switch (floor_type) {
                                            case LAPIS_BLOCK: // if using the default Lapis Block - then use Light Grey Wool / Terracotta
                                                switch (use_clay) {
                                                    case TERRACOTTA:
                                                        type = Material.LIGHT_GRAY_TERRACOTTA;
                                                        break;
                                                    case CONCRETE:
                                                        type = Material.LIGHT_GRAY_CONCRETE;
                                                        break;
                                                    default:
                                                        type = Material.LIGHT_GRAY_WOOL;
                                                        break;
                                                }
                                                break;
                                            default:
                                                type = floor_type;
                                        }
                                    } else {
                                        String[] tsplit = type.toString().split("_");
                                        type = Material.getMaterial(tsplit[0] + "_" + use_clay.toString());
                                    }
                                    break;
                                default:
                                    String[] tsplit = type.toString().split("_");
                                    type = Material.getMaterial(tsplit[0] + "_" + use_clay.toString());
                            }
                        }
                        if (type.equals(Material.AIR)) {
                            v--;
                        }
                        if (!b.getType().equals(type)) {
                            //plugin.debug(b.getType().toString() + " != " + type.toString());
                            count++;
                        }
                    }
                }
            }
            TARDISBlockScannerData tbsd = new TARDISBlockScannerData();
            tbsd.setCount(count);
            tbsd.setVolume(v);
            int changed = (int) ((count / v) * 100);
            tbsd.setChanged(changed);
            // should return false if changed is higher than config
            tbsd.setAllow(changed < plugin.getConfig().getInt("desktop.block_change_percent"));
            tbsd.setBeacon(beacon);
            return tbsd;
        } else {
            return null;
        }
    }
}
