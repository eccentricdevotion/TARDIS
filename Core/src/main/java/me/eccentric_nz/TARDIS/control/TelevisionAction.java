package me.eccentric_nz.TARDIS.control;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.skins.tv.TVInventory;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class TelevisionAction {

    private final TARDIS plugin;

    public TelevisionAction(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void openGUI(Player player) {
        ItemStack[] items = new TVInventory().getMenu();
        Inventory tvinv = plugin.getServer().createInventory(player, 36, ChatColor.DARK_RED + "TARDIS Television");
        tvinv.setContents(items);
        player.openInventory(tvinv);
    }
}
