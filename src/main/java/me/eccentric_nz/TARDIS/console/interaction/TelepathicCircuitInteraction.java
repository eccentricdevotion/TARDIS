package me.eccentric_nz.TARDIS.console.interaction;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.console.telepathic.TARDISTelepathicInventory;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetPlayerPrefs;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.UUID;

public class TelepathicCircuitInteraction {

    private final TARDIS plugin;

    public TelepathicCircuitInteraction(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void process(Player player) {
        UUID uuid = player.getUniqueId();
        if (plugin.getTrackerKeeper().getFlight().containsKey(uuid)) {
            return;
        }
        if (player.isSneaking()) {
            // get current telepathic setting
            ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, uuid.toString());
            int b = (rsp.resultSet() && rsp.isTelepathyOn()) ? 0 : 1;
            // update database
            HashMap<String, Object> set = new HashMap<>();
            HashMap<String, Object> whereu = new HashMap<>();
            whereu.put("uuid", uuid.toString());
            set.put("telepathy_on", b);
            plugin.getQueryFactory().doUpdate("player_prefs", set, whereu);
            plugin.getMessenger().announceRepeater(player, "Telepathic Circuit " + (b == 1 ? "ON" : "OFF"));
        } else {
            // open GUI for
            // toggling telepathic circuit on/off
            // cave finder
            // structure finder
            // biome finder
            TARDISTelepathicInventory tti = new TARDISTelepathicInventory(plugin, player);
            ItemStack[] gui = tti.getButtons();
            Inventory telepathic = plugin.getServer().createInventory(player, 54, NamedTextColor.DARK_RED + "TARDIS Telepathic Circuit");
            telepathic.setContents(gui);
            player.openInventory(telepathic);
        }
    }
}
