package me.eccentric_nz.TARDIS.rooms.eye;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class EyeOfHarmonyAction {

    private final TARDIS plugin;
    private final int id;

    public EyeOfHarmonyAction(TARDIS plugin, int id) {
        this.plugin = plugin;
        this.id = id;
    }

    public void openGUI(int id, Player player) {
        ItemStack[] entries = new TARDISEyeStorage(plugin).getGUI(id);
        Inventory gui = plugin.getServer().createInventory(player, 9, ChatColor.DARK_RED + "Artron Capacitor Storage");
        gui.setContents(entries);
        player.openInventory(gui);
    }
}
