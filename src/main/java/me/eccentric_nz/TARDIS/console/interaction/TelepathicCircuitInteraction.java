package me.eccentric_nz.TARDIS.console.interaction;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.console.telepathic.TARDISTelepathicInventory;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class TelepathicCircuitInteraction {

    private final TARDIS plugin;

    public TelepathicCircuitInteraction(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void openGUI(Player player) {
        // open GUI for
        // toggling telepathic circuit on/off
        // cave finder
        // structure finder
        // biome finder
        TARDISTelepathicInventory tti = new TARDISTelepathicInventory(plugin, player);
        ItemStack[] gui = tti.getButtons();
        Inventory telepathic = plugin.getServer().createInventory(player, 54, ChatColor.DARK_RED + "TARDIS Telepathic Circuit");
        telepathic.setContents(gui);
        player.openInventory(telepathic);
    }
}
