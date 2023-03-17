package me.eccentric_nz.tardisvortexmanipulator.command;

import java.util.HashMap;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.MODULE;
import me.eccentric_nz.TARDIS.messaging.TARDISMessage;
import me.eccentric_nz.tardisvortexmanipulator.TVMUtils;
import me.eccentric_nz.tardisvortexmanipulator.database.TVMResultSetSaves;
import me.eccentric_nz.tardisvortexmanipulator.database.TVMResultSetWarpByName;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class TVMCommandSave implements CommandExecutor {

    private final TARDIS plugin;

    public TVMCommandSave(TARDIS plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("vms")) {
            Player player = null;
            if (sender instanceof Player) {
                player = (Player) sender;
            }
            if (player == null) {
                TARDISMessage.send(sender, MODULE.VORTEX_MANIPULATOR, "CMD_PLAYER");
                return true;
            }
            if (!player.hasPermission("vm.teleport")) {
                TARDISMessage.send(player, MODULE.VORTEX_MANIPULATOR, "VM_PERM_CMD");
                return true;
            }
            ItemStack is = player.getInventory().getItemInMainHand();
            if (is != null && is.hasItemMeta() && is.getItemMeta().hasDisplayName() && is.getItemMeta().getDisplayName().equals("Vortex Manipulator")) {
                String uuid = player.getUniqueId().toString();
                if (args.length == 0) {
                    // list saves
                    TVMResultSetSaves rss = new TVMResultSetSaves(plugin, uuid, 0, 10);
                    if (rss.resultSet()) {
                        TVMUtils.sendSaveList(player, rss, 1);
                    }
                }
                if (args.length < 1) {
                    TARDISMessage.send(player, MODULE.VORTEX_MANIPULATOR, "VM_PAGE_NUM");
                    return true;
                }
                try {
                    int page = Integer.parseInt(args[0]);
                    if (page <= 0) {
                        TARDISMessage.send(player, MODULE.VORTEX_MANIPULATOR, "VM_MSG_INVALID");
                        return true;
                    }
                    int start = (page * 10) - 10;
                    int limit = page * 10;
                    TVMResultSetSaves rss = new TVMResultSetSaves(plugin, uuid, start, limit);
                    if (rss.resultSet()) {
                        TVMUtils.sendSaveList(player, rss, page);
                    }
                } catch (NumberFormatException e) {
                    plugin.debug("Wasn't a page number...");
                    // check for existing save
                    TVMResultSetWarpByName rs = new TVMResultSetWarpByName(plugin, uuid, args[0]);
                    if (rs.resultSet()) {
                        TARDISMessage.send(player, MODULE.VORTEX_MANIPULATOR, "VM_SAVE_EXISTS");
                        return true;
                    }
                    Location l = player.getLocation();
                    HashMap<String, Object> set = new HashMap<>();
                    set.put("uuid", uuid);
                    set.put("save_name", args[0]);
                    set.put("world", l.getWorld().getName());
                    set.put("x", l.getX());
                    set.put("y", l.getY());
                    set.put("z", l.getZ());
                    set.put("yaw", l.getYaw());
                    set.put("pitch", l.getPitch());
                    plugin.getQueryFactory().doInsert("saves", set);
                    TARDISMessage.send(sender, MODULE.VORTEX_MANIPULATOR, "VM_SAVE_ADDED", args[0]);
                    return true;
                }
            } else {
                TARDISMessage.send(player, MODULE.VORTEX_MANIPULATOR, "VM_HAND");
                return true;
            }
        }
        return false;
    }
}
