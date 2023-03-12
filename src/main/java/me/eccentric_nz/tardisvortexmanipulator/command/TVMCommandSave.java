package me.eccentric_nz.tardisvortexmanipulator.command;

import me.eccentric_nz.tardisvortexmanipulator.TARDISVortexManipulator;
import me.eccentric_nz.tardisvortexmanipulator.TVMUtils;
import me.eccentric_nz.tardisvortexmanipulator.database.TVMQueryFactory;
import me.eccentric_nz.tardisvortexmanipulator.database.TVMResultSetSaves;
import me.eccentric_nz.tardisvortexmanipulator.database.TVMResultSetWarpByName;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class TVMCommandSave implements CommandExecutor {

    private final TARDISVortexManipulator plugin;

    public TVMCommandSave(TARDISVortexManipulator plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("vms")) {
            Player p = null;
            if (sender instanceof Player) {
                p = (Player) sender;
            }
            if (p == null) {
                sender.sendMessage(plugin.getPluginName() + "That command cannot be used from the console!");
                return true;
            }
            if (!p.hasPermission("vm.teleport")) {
                p.sendMessage(plugin.getPluginName() + "You don't have permission to use that command!");
                return true;
            }
            ItemStack is = p.getInventory().getItemInMainHand();
            if (is != null && is.hasItemMeta() && is.getItemMeta().hasDisplayName() && is.getItemMeta().getDisplayName().equals("Vortex Manipulator")) {
                String uuid = p.getUniqueId().toString();
                if (args.length == 0) {
                    // list saves
                    TVMResultSetSaves rss = new TVMResultSetSaves(plugin, uuid, 0, 10);
                    if (rss.resultSet()) {
                        TVMUtils.sendSaveList(p, rss, 1);
                    }
                }
                if (args.length < 1) {
                    p.sendMessage(plugin.getPluginName() + "You need to specify a save name or page number!");
                    return true;
                }
                try {
                    int page = Integer.parseInt(args[0]);
                    if (page <= 0) {
                        p.sendMessage(plugin.getPluginName() + "Invalid page number!");
                        return true;
                    }
                    int start = (page * 10) - 10;
                    int limit = page * 10;
                    TVMResultSetSaves rss = new TVMResultSetSaves(plugin, uuid, start, limit);
                    if (rss.resultSet()) {
                        TVMUtils.sendSaveList(p, rss, page);
                    }
                } catch (NumberFormatException e) {
                    plugin.debug("Wasn't a page number...");
                    // check for existing save
                    TVMResultSetWarpByName rs = new TVMResultSetWarpByName(plugin, uuid, args[0]);
                    if (rs.resultSet()) {
                        p.sendMessage(plugin.getPluginName() + "You already have a save with that name!");
                        return true;
                    }
                    Location l = p.getLocation();
                    HashMap<String, Object> set = new HashMap<>();
                    set.put("uuid", uuid);
                    set.put("save_name", args[0]);
                    set.put("world", l.getWorld().getName());
                    set.put("x", l.getX());
                    set.put("y", l.getY());
                    set.put("z", l.getZ());
                    set.put("yaw", l.getYaw());
                    set.put("pitch", l.getPitch());
                    new TVMQueryFactory(plugin).doInsert("saves", set);
                    sender.sendMessage(plugin.getPluginName() + "Vortex Manipulator location (" + args[0] + ") saved!");
                    return true;
                }
            } else {
                p.sendMessage(plugin.getPluginName() + "You don't have a Vortex Manipulator in your hand!");
                return true;
            }
        }
        return false;
    }
}
