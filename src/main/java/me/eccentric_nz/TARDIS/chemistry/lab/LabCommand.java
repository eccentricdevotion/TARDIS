package me.eccentric_nz.TARDIS.chemistry.lab;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class LabCommand implements CommandExecutor {

    private final TARDIS plugin;

    public LabCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("lab")) {
            Player player = null;
            if (sender instanceof Player) {
                player = (Player) sender;
            }
            if (player == null) {
                sender.sendMessage(plugin.getPluginName() + "Command can only be used by a player!");
                return true;
            }
            if (!player.hasPermission("chemistry.command")) {
                sender.sendMessage(plugin.getPluginName() + "You don't have permission to open Chemistry GUIs by command!");
                return true;
            }
            // do stuff
            ItemStack[] menu = new LabInventory().getMenu();
            Inventory products = plugin.getServer().createInventory(player, 27, ChatColor.DARK_RED + "Lab table");
            products.setContents(menu);
            player.openInventory(products);
            return true;
        }
        return false;
    }
}
