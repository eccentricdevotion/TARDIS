package me.eccentric_nz.TARDIS.chemistry.constructor;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class ConstructCommand implements CommandExecutor {

    private final TARDIS plugin;

    public ConstructCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("construct")) {
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
            ItemStack[] menu = new ConstructorInventory().getMenu();
            Inventory products = plugin.getServer().createInventory(player, 27, ChatColor.DARK_RED + "Element constructor");
            products.setContents(menu);
            player.openInventory(products);
            return true;
        }
        return false;
    }
}
