package me.eccentric_nz.TARDIS.console.interaction;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.console.telepathic.TARDISTelepathicInventory;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetPlayerPrefs;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.upgrades.SystemTree;
import me.eccentric_nz.TARDIS.upgrades.SystemUpgradeChecker;
import org.bukkit.ChatColor;
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
        if (plugin.getConfig().getBoolean("difficulty.system_upgrades") && !new SystemUpgradeChecker(plugin).has(uuid.toString(), SystemTree.TELEPATHIC_CIRCUIT)) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "SYS_NEED", "Telepathic Circuit");
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
            Inventory telepathic = plugin.getServer().createInventory(player, 54, ChatColor.DARK_RED + "TARDIS Telepathic Circuit");
            telepathic.setContents(gui);
            player.openInventory(telepathic);
        }
    }
}
