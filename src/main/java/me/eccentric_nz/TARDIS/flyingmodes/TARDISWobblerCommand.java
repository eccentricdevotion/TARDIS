package me.eccentric_nz.TARDIS.flyingmodes;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class TARDISWobblerCommand implements CommandExecutor {

    private final TARDIS plugin;

    public TARDISWobblerCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(final CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("wobbler")) {
            Player player = null;
            if (sender instanceof Player) {
                player = (Player) sender;
            }
            if (player == null) {
                sender.sendMessage(plugin.getPluginName() + "Command can only be used by a player!");
                return true;
            }
            // do stuff
            TARDISWobblerInventory wob = new TARDISWobblerInventory();
            ItemStack[] items = wob.getTerminal();
            Inventory inv = plugin.getServer().createInventory(player, 54, "Wobbler");
            inv.setContents(items);
            player.openInventory(inv);
            return true;
        }
        return false;
    }
}
