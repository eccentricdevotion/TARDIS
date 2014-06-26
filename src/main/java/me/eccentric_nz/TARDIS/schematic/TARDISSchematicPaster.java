/*
 *  Copyright 2014 eccentric_nz.
 */
package me.eccentric_nz.TARDIS.schematic;

import java.util.UUID;
import me.eccentric_nz.TARDIS.JSON.JSONArray;
import me.eccentric_nz.TARDIS.JSON.JSONObject;
import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

/**
 *
 * @author eccentric_nz
 */
public class TARDISSchematicPaster {

    private final TARDIS plugin;
    private final Player player;

    public TARDISSchematicPaster(TARDIS plugin, Player player) {
        this.plugin = plugin;
        this.player = player;
    }

    @SuppressWarnings("deprecation")
    public boolean paste() {
        UUID uuid = player.getUniqueId();
        if (!plugin.getTrackerKeeper().getPastes().containsKey(uuid)) {
            player.sendMessage(plugin.getPluginName() + "No schematic loaded! " + ChatColor.GREEN + "/ts load [name]");
            return true;
        }
        JSONObject obj = plugin.getTrackerKeeper().getPastes().get(uuid);
        // get dimensions
        JSONObject d = (JSONObject) obj.get("dimensions");
        int hei = d.getInt("height");
        int wid = d.getInt("width");
        int len = d.getInt("length");
        // get start location
        JSONObject r = (JSONObject) obj.get("relative");
        int rx = r.getInt("x");
        int ry = r.getInt("y");
        int rz = r.getInt("z");
        int x = player.getLocation().getBlockX() - rx;
        int y = player.getLocation().getBlockY() - ry;
        int z = player.getLocation().getBlockZ() - rz;
        World world = player.getWorld();
        // get input array
        JSONArray arr = (JSONArray) obj.get("input");
        // loop like crazy
        for (int h = 0; h < hei; h++) {
            JSONArray level = (JSONArray) arr.get(h);
            for (int w = 0; w < wid; w++) {
                JSONArray row = (JSONArray) level.get(w);
                for (int l = 0; l < len; l++) {
                    JSONObject col = (JSONObject) row.get(l);
                    Material m = Material.valueOf((String) col.get("type"));
                    byte b = col.getByte("data");
                    Block block = world.getBlockAt(x + w, y + h, z + l);
                    block.setType(m);
                    block.setData(b, true);
                }
            }
        }
        return true;
    }
}
