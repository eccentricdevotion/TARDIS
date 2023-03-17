package me.eccentric_nz.tardisvortexmanipulator.command;

import java.util.HashMap;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.MODULE;
import me.eccentric_nz.TARDIS.messaging.TARDISMessage;
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
                if (args.length < 1) {
                    TARDISMessage.send(player, MODULE.VORTEX_MANIPULATOR, "VM_SAVE_NAME");
                    return true;
                }
                String uuid = player.getUniqueId().toString();
                // check for existing save
                TVMResultSetWarpByName rs = new TVMResultSetWarpByName(plugin, uuid, args[0]);
                if (rs.resultSet()) {
                    TARDISMessage.send(player, MODULE.VORTEX_MANIPULATOR, "VM_SAVE_NONE");
                    return true;
                }
                HashMap<String, Object> where = new HashMap<>();
                where.put("uuid", uuid);
                where.put("save_name", args[0]);
                plugin.getQueryFactory().doDelete("saves", where);
                TARDISMessage.send(sender, MODULE.VORTEX_MANIPULATOR, "VM_SAVE_REMOVED", args[0]);
                return true;
            } else {
                TARDISMessage.send(player, MODULE.VORTEX_MANIPULATOR, "VM_HAND");
                return true;
            }
        }
        return false;
    }
}
