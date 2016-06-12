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
import me.eccentric_nz.TARDIS.rooms.TARDISWalls;
import me.eccentric_nz.TARDIS.schematic.TARDISSchematicGZip;
import org.bukkit.Material;
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
    int startx, starty, startz, resetx, resetz;
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
        HashMap<String, Object> wheret = new HashMap<String, Object>();
        wheret.put("uuid", uuid.toString());
        ResultSetTardis rs = new ResultSetTardis(plugin, wheret, "", false, 0);
        if (rs.resultSet()) {
            Tardis tardis = rs.getTardis();
            int slot = tardis.getTIPS();
            if (slot != -1) { // default world - use TIPS
                TARDISInteriorPostioning tintpos = new TARDISInteriorPostioning(plugin);
                TARDISTIPSData pos = tintpos.getTIPSData(slot);
                startx = pos.getCentreX();
                resetx = pos.getCentreX();
                startz = pos.getCentreZ();
                resetz = pos.getCentreZ();
            } else {
                int gsl[] = plugin.getLocationUtils().getStartLocation(tardis.getTardis_id());
                startx = gsl[0];
                resetx = gsl[1];
                startz = gsl[2];
                resetz = gsl[3];
            }
            starty = (tud.getPrevious().getPermission().equals("redstone")) ? 65 : 64;
            String[] split = tardis.getChunk().split(":");
            World world = plugin.getServer().getWorld(split[0]);
            Material wall_type;
            Material floor_type;
            // get wall/floor block prefs from database...
            HashMap<String, Object> where = new HashMap<String, Object>();
            where.put("uuid", uuid.toString());
            ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, where);
            if (rsp.resultSet()) {
                TARDISWalls.Pair wid_data = plugin.getTardisWalls().blocks.get(rsp.getWall());
                wall_type = wid_data.getType();
                TARDISWalls.Pair fid_data = plugin.getTardisWalls().blocks.get(rsp.getFloor());
                floor_type = fid_data.getType();
            } else {
                wall_type = Material.WOOL;
                floor_type = Material.WOOL;
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
                        if (type.equals(Material.WOOL) && data == 1) {
                            type = wall_type;
                        }
                        if (type.equals(Material.WOOL) && data == 8) {
                            type = floor_type;
                        }
                        if (type.equals(Material.SPONGE)) {
                            type = Material.AIR;
                        }
                        if (type.equals(Material.CAKE_BLOCK)) {
                            type = Material.LEVER;
                        }
                        if (type.equals(Material.MOB_SPAWNER)) {
                            type = Material.WOOD_BUTTON;
                        }
                        if (type.equals(Material.HUGE_MUSHROOM_2)) {
                            type = Material.DIODE_BLOCK_OFF;
                        }
                        if (type.equals(Material.MONSTER_EGGS)) {
                            type = Material.WALL_SIGN;
                        }
                        if (type.equals(Material.BEDROCK)) {
                            type = Material.GLASS;
                            beacon = world.getName() + ":" + x + ":" + y + ":" + z;
                        }
                        if (type.equals(Material.COMMAND)) {
                            type = Material.SMOOTH_BRICK;
                        }
                        if (type.equals(Material.WOOL) && plugin.getConfig().getBoolean("creation.use_clay")) {
                            type = Material.STAINED_CLAY;
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
