package me.eccentric_nz.tardischunkgenerator.worldgen;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetPlotCount;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.utility.TARDISNumberParsers;
import org.bukkit.Location;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.block.sign.Side;
import org.bukkit.block.sign.SignSide;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

import java.util.HashMap;

public class PlotListener implements Listener {

    private final TARDIS plugin;
    private final int size;
    private final int claim_max;

    public PlotListener(TARDIS plugin) {
        this.plugin = plugin;
        size = Math.max(plugin.getGeneratorConfig().getInt("plot_size", 3), 2);
        claim_max = plugin.getGeneratorConfig().getInt("plot_size", 3);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (!TARDISPermission.hasPermission(player, "tardis.plot")) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "NO_PERM_PLOT");
            return;
        }
        if (!(player.getWorld().getGenerator() instanceof PlotGenerator)) {
            return;
        }
        if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK) && event.getHand() != null && event.getHand().equals(EquipmentSlot.HAND)) {
            if (event.getClickedBlock() == null) {
                return;
            }
            Block block = event.getClickedBlock().getRelative(BlockFace.UP);
            if (Tag.STANDING_SIGNS.isTagged(block.getType())) {
                int x = block.getChunk().getX();
                int z = block.getChunk().getZ();
                Sign sign = (Sign) block.getState();
                if (sign.isWaxed()) {
                    // check name on the sign
                    // if it matches, and we are tracking the player
                    // update the plot name
                    if (sign.getSide(Side.FRONT).getLine(1).equals(player.getName()) && plugin.getTrackerKeeper().getPlotters().containsKey(player.getUniqueId())) {
                        String name = plugin.getTrackerKeeper().getPlotters().get(player.getUniqueId());
                        sign.getSide(Side.FRONT).setLine(0, name);
                        sign.getSide(Side.BACK).setLine(0, name);
                        sign.update();
                        // update the database
                        HashMap<String, Object> set = new HashMap<>();
                        set.put("name", name);
                        HashMap<String, Object> where = new HashMap<>();
                        where.put("uuid", player.getUniqueId().toString());
                        where.put("world", player.getWorld().getName());
                        String[] split = sign.getSide(Side.FRONT).getLine(3).split(", ");
                        String[] sx = split[0].split(":");
                        int dx = TARDISNumberParsers.parseInt(sx[1]);
                        String[] sz = split[1].split(":");
                        int dz = TARDISNumberParsers.parseInt(sz[1]);
                        where.put("chunk_x", dx);
                        where.put("chunk_z", dz);
                        plugin.getQueryFactory().doUpdate("plots", set, where);
                        // untrack player
                        plugin.getTrackerKeeper().getPlotters().remove(player.getUniqueId());
                        plugin.getMessenger().send(player, TardisModule.TARDIS, "PLOT_NAMED");
                    } else {
                        plugin.getMessenger().send(player, TardisModule.TARDIS, "PLOT_ALREADY_CLAIMED");
                    }
                    return;
                }
                // check if player has a plot already only allow up to limit
                ResultSetPlotCount rsp = new ResultSetPlotCount(plugin);
                if (rsp.fromUUID(player.getUniqueId().toString()) >= claim_max) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "");
                    return;
                }
                SignSide front = sign.getSide(Side.FRONT);
                // suggest command
                front.setLine(0, "/tplot name [name]");
                front.setLine(1, player.getName());
                front.setLine(3, "X:" + x + ", Z:" + z);
                SignSide back = sign.getSide(Side.BACK);
                back.setLine(0, "/tplot name [name]");
                back.setLine(1, player.getName());
                back.setLine(3, "X:" + x + ", Z:" + z);
                sign.setWaxed(true);
                sign.update(true);
                // make locations
                Location min = block.getLocation();
                int add = 11 + ((size - 1) * 16);
                Location max = min.clone().add(add, 0, add);
                // save plot
                HashMap<String, Object> set = new HashMap<>();
                set.put("uuid", player.getUniqueId().toString());
                set.put("world", min.getWorld().getName());
                set.put("chunk_x", x);
                set.put("chunk_z", z);
                set.put("size", size);
                plugin.getQueryFactory().doInsert("plots", set);
                // add protection
                if (plugin.isWorldGuardOnServer()) {
                    plugin.getWorldGuardUtils().addPlotProtection(player.getUniqueId(), min, max);
                }
                plugin.getMessenger().send(player, TardisModule.TARDIS, "PLOT_CLAIMED");
            }
        }
    }
}
