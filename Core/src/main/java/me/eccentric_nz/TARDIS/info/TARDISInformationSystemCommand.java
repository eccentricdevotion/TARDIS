package me.eccentric_nz.TARDIS.info;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class TARDISInformationSystemCommand implements CommandExecutor {

    private final TARDIS plugin;

    public TARDISInformationSystemCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player p)) {
            return true;
        }
        UUID uuid = p.getUniqueId();
        if (plugin.getTrackerKeeper().getInfoMenu().containsKey(uuid)) {
            if (args[0].equalsIgnoreCase("E")) {
                TARDISInformationSystemListener.exit(p, plugin);
                return true;
            }
            if (args.length == 1) {
                TARDISInformationSystemListener.processInput(p, uuid, args[0], plugin);
            } else {
                plugin.getMessenger().send(p, TardisModule.TARDIS, "TIS_EXIT");
            }
        } else if (args.length > 0 && args[0].equalsIgnoreCase("test")) {
            // open TIS GUI
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                ItemStack[] cats = new TARDISIndexFileInventory(plugin).getMenu();
                Inventory gui = plugin.getServer().createInventory(p, 27, ChatColor.DARK_RED + "TARDIS Index File");
                gui.setContents(cats);
                p.openInventory(gui);
            }, 2L);
        }
        return true;
    }
}
