package me.eccentric_nz.TARDIS.rooms;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.ClearEyeControls;
import me.eccentric_nz.TARDIS.rooms.eye.EyeOfHarmonyParticles;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Locale;

public class RoomCleaner {

    private final TARDIS plugin;

    public RoomCleaner(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void removeRecords(String room, int id, World world, Player player) {
        // if it is a secondary console room remove the controls
        if (room.equals("BAKER") || room.equals("WOOD")) {
            int secondary = (room.equals("BAKER")) ? 1 : 2;
            HashMap<String, Object> del = new HashMap<>();
            del.put("tardis_id", id);
            del.put("secondary", secondary);
            plugin.getQueryFactory().doDelete("controls", del);
        }
        // if it is a shell room remove the button control
        if (room.equals("SHELL")) {
            HashMap<String, Object> del = new HashMap<>();
            del.put("tardis_id", id);
            del.put("type", 25);
            plugin.getQueryFactory().doDelete("controls", del);
        }
        // if it is a smelter room remove the chest records
        if (room.equals("SMELTER")) {
            HashMap<String, Object> del = new HashMap<>();
            del.put("tardis_id", id);
            del.put("x", 0);
            del.put("y", 0);
            del.put("z", 0);
            plugin.getQueryFactory().doDelete("vaults", del);
        }
        // if it is a maze room remove the controls
        if (room.equals("MAZE")) {
            for (int c = 40; c < 45; c++) {
                HashMap<String, Object> del = new HashMap<>();
                del.put("tardis_id", id);
                del.put("type", c);
                plugin.getQueryFactory().doDelete("controls", del);
            }
        }
        if (room.equals("RENDERER")) {
            // remove stored location from the database
            HashMap<String, Object> setd = new HashMap<>();
            setd.put("renderer", "");
            HashMap<String, Object> where = new HashMap<>();
            where.put("tardis_id", id);
            plugin.getQueryFactory().doUpdate("tardis", setd, where);
            // remove WorldGuard protection
            if (plugin.isWorldGuardOnServer() && plugin.getConfig().getBoolean("preferences.use_worldguard")) {
                plugin.getWorldGuardUtils().removeRoomRegion(world, player.getName(), "renderer");
            }
        }
        // remove mob farming locations
        if (room.equals("ALLAY") || room.equals("APIARY") || room.equals("AQUARIUM") || room.equals("BAMBOO")
                || room.equals("BIRDCAGE") || room.equals("FARM") || room.equals("GEODE") || room.equals("HUTCH")
                || room.equals("IGLOO") || room.equals("IISTUBIL") || room.equals("LAVA") || room.equals("MANGROVE")
                || room.equals("PEN") || room.equals("STABLE") || room.equals("STALL") || room.equals("VILLAGE")
        ) {
            HashMap<String, Object> wheref = new HashMap<>();
            wheref.put("tardis_id", id);
            HashMap<String, Object> setf = new HashMap<>();
            setf.put(room.toLowerCase(Locale.ROOT), "");
            plugin.getQueryFactory().doUpdate("farming", setf, wheref);
        }
        // remove eye controls, set capacitors to 1
        if (room.equals("EYE")) {
            // stop eye particles runnable
            EyeOfHarmonyParticles.stop(plugin, id);
            // remove database records
            new ClearEyeControls(plugin).removeRecords(id);
        }
    }
}
