package me.eccentric_nz.TARDIS.commands.tardis;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.database.data.Transmat;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTransmat;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTransmatList;
import me.eccentric_nz.TARDIS.desktop.PreviewData;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.planets.RoomsWorld;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class TransmatUtility {

    public static void list(TARDIS plugin, Player player, int id) {
        Transmat transmat = null;
        if (plugin.getPlanetsConfig().getBoolean("planets.rooms.enabled") && plugin.getServer().getWorld("rooms") != null) {
            transmat = new RoomsWorld().getTransmat(plugin);
        }
        ResultSetTransmatList rslist = new ResultSetTransmatList(plugin, id);
        if (rslist.resultSet()) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "TRANSMAT_LIST");
            for (Transmat t : rslist.getData()) {
                plugin.getMessenger().sendTransmat(player, t);
            }
            if (transmat != null) {
                plugin.getMessenger().sendTransmat(player, transmat);
            }
        } else {
            if (transmat != null) {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "TRANSMAT_LIST");
                plugin.getMessenger().sendTransmat(player, transmat);
            } else {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "TRANSMAT_NO_LIST");
            }
        }
    }

    public static void toRoomsWorld(TARDIS plugin, Player player, int id) {
        if (!TARDISPermission.hasPermission(player, "tardis.transmat.rooms")) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "NO_PERMS");
            return;
        }
        ResultSetTransmat rsm = new ResultSetTransmat(plugin, -1, "rooms");
        transmat(plugin, player, rsm);
        plugin.getTrackerKeeper().getPreviewers().put(player.getUniqueId(), new PreviewData(player.getLocation().clone(), player.getGameMode(), id));
        plugin.getMessenger().send(player, TardisModule.TARDIS, "PREVIEW_DONE");
    }

    public static void tp(TARDIS plugin, Player player, String to, Integer id) {
        if (to.equalsIgnoreCase("console")) {
            plugin.getGeneralKeeper().getRendererListener().transmat(player);
        } else {
            ResultSetTransmat rsm = new ResultSetTransmat(plugin, id, to);
            transmat(plugin, player, rsm);
        }
    }

    private static void transmat(TARDIS plugin, Player player, ResultSetTransmat rsm) {
        if (rsm.resultSet()) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "TRANSMAT");
            Location tp_loc = rsm.getLocation();
            tp_loc.setYaw(rsm.getYaw());
            tp_loc.setPitch(player.getLocation().getPitch());
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                player.playSound(tp_loc, Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
                player.teleport(tp_loc);
            }, 10L);
        } else {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "TRANSMAT_NOT_FOUND");
        }
    }
}
