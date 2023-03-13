package me.eccentric_nz.tardisvortexmanipulator.command;

import java.util.HashMap;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.tardisvortexmanipulator.database.TVMResultSetWarpByName;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class TVMCommandRemove implements CommandExecutor {

    private final TARDIS plugin;

    public TVMCommandRemove(TARDIS plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("vmr")) {
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
                if (args.length < 1) {
                    p.sendMessage(plugin.getPluginName() + "You need to specify a save name!");
                    return true;
                }
                String uuid = p.getUniqueId().toString();
                // check for existing save
                TVMResultSetWarpByName rs = new TVMResultSetWarpByName(plugin, uuid, args[0]);
                if (rs.resultSet()) {
                    p.sendMessage(plugin.getPluginName() + "No save with that name exists! Try using /vms to list saves.");
                    return true;
                }
                HashMap<String, Object> where = new HashMap<>();
                where.put("uuid", uuid);
                where.put("save_name", args[0]);
                plugin.getQueryFactory().doDelete("saves", where);
                sender.sendMessage(plugin.getPluginName() + "Vortex Manipulator location (" + args[0] + ") removed!");
                return true;
            } else {
                p.sendMessage(plugin.getPluginName() + "You don't have a Vortex Manipulator in your hand!");
                return true;
            }
        }
        return false;
    }
}
