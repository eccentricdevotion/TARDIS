package me.eccentric_nz.TARDIS.control.actions;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.commands.utils.TARDISWeatherInventory;
import me.eccentric_nz.TARDIS.floodgate.FloodgateWeatherForm;
import me.eccentric_nz.TARDIS.floodgate.TARDISFloodgate;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class WeatherAction {

    private final TARDIS plugin;

    public WeatherAction(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void openGUI(Player player) {
        UUID playerUUID = player.getUniqueId();
        if (TARDISFloodgate.isFloodgateEnabled() && TARDISFloodgate.isBedrockPlayer(playerUUID)) {
            new FloodgateWeatherForm(plugin, playerUUID).send();
        } else {
            ItemStack[] weather = new TARDISWeatherInventory(plugin).getWeatherButtons();
            Inventory forecast = plugin.getServer().createInventory(player, 9, ChatColor.DARK_RED + "TARDIS Weather Menu");
            forecast.setContents(weather);
            player.openInventory(forecast);
        }
    }
}
