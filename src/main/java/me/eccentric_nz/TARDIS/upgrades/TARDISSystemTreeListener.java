package me.eccentric_nz.TARDIS.upgrades;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.custommodeldata.GUISystemTree;
import me.eccentric_nz.TARDIS.database.data.SystemUpgrade;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetPlayerArtronLevel;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTravellers;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.listeners.TARDISMenuListener;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class TARDISSystemTreeListener extends TARDISMenuListener {

    private final TARDIS plugin;

    public TARDISSystemTreeListener(TARDIS plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onSystemInventoryClick(InventoryClickEvent event) {
        InventoryView view = event.getView();
        if (!view.getTitle().equals(ChatColor.DARK_RED + "TARDIS System Upgrades")) {
            return;
        }
        event.setCancelled(true);
        int slot = event.getRawSlot();
        if (slot < 0 || slot >= 54) {
            return;
        }
        ItemStack is = view.getItem(slot);
        if (is == null) {
            return;
        }
        if (is.getType() != Material.LIME_GLAZED_TERRACOTTA && slot != 8) {
            return;
        }
        Player player = (Player) event.getWhoClicked();
        String uuid = player.getUniqueId().toString();
        // get TARDIS player is in
        HashMap<String, Object> where = new HashMap<>();
        where.put("uuid", uuid);
        ResultSetTravellers rst = new ResultSetTravellers(plugin, where, false);
        if (!rst.resultSet()) {
            return;
        }
        int id = rst.getTardis_id();
        // must be the owner of the TARDIS
        HashMap<String, Object> wheret = new HashMap<>();
        wheret.put("tardis_id", id);
        wheret.put("uuid", uuid);
        ResultSetTardis rs = new ResultSetTardis(plugin, wheret, "", false, 2);
        if (!rs.resultSet()) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "NOT_OWNER");
            close(player);
            return;
        }
        // get player's artron energy level
        ResultSetPlayerArtronLevel rsp = new ResultSetPlayerArtronLevel(plugin, id, uuid);
        if (!rsp.resultset()) {
            close(player);
            plugin.getMessenger().send(player, TardisModule.TARDIS, "SYS_TRAVEL_FIRST");
            return;
        }
        if (slot == 8) {
            close(player);
            return;
        }
        SystemUpgrade current = rsp.getSystemUpgrade();
        GUISystemTree clicked;
        switch (slot) {
            case 9 -> {
                // architecture
                clicked = GUISystemTree.ARCHITECTURE;
            }
            case 13 -> {
                // navigation
                clicked = GUISystemTree.NAVIGATION;
            }
            case 16 -> {
                // tools
                clicked = GUISystemTree.TOOLS;
            }
            case 19 -> {
                // chameleon
                clicked = GUISystemTree.CHAMELEON_CIRCUIT;
            }
            case 28 -> {
                // rooms
                clicked = GUISystemTree.ROOM_GROWING;
            }
            case 37 -> {
                // desktop
                clicked = GUISystemTree.DESKTOP_THEME;
            }
            case 23 -> {
                // saves
                clicked = GUISystemTree.SAVES;
            }
            case 32 -> {
                // distance 1
                clicked = GUISystemTree.DISTANCE_1;
            }
            case 41 -> {
                // distance 2
                clicked = GUISystemTree.DISTANCE_2;
            }
            case 48 -> {
                // inter dimension
                clicked = GUISystemTree.INTER_DIMENSIONAL_TRAVEL;
            }
            case 50 -> {
                // distance 3
                clicked = GUISystemTree.DISTANCE_3;
            }
            case 26 -> {
                // locator
                clicked = GUISystemTree.TARDIS_LOCATOR;
            }
            case 35 -> {
                // biome reader
                clicked = GUISystemTree.BIOME_READER;
            }
            case 44 -> {
                // force field
                clicked = GUISystemTree.FORCE_FIELD;
            }
            case 53 -> {
                // stattenheim remote
                clicked = GUISystemTree.STATTENHEIM_REMOTE;
            }
            default -> {
                clicked = GUISystemTree.UPGRADE_TREE;
            }
        }
        try {
            GUISystemTree required = GUISystemTree.valueOf(clicked.getRequired());
            if (!current.getUpgrades().get(required)) {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "SYS_REQUIRED", required.getName());
            } else {
                // check artron
                int cost;
                if (clicked.getBranch().equals("branch")) {
                    cost = plugin.getSystemUpgradesConfig().getInt("branch");
                } else {
                    cost = plugin.getSystemUpgradesConfig().getInt(clicked.getBranch() + "." + clicked.toString().toLowerCase());
                }
                if (cost > current.getArtronLevel()) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "SYS_COST", clicked.getName());
                    return;
                }
                // close and debit
                close(player);
                plugin.getQueryFactory().alterEnergyLevel("player_prefs", -cost, where, player);
                plugin.getMessenger().send(player, TardisModule.TARDIS, "SYS_SUCCESS", clicked.getName());
            }
        } catch (IllegalArgumentException e) {
            // clicked upgrade tree
            close(player);
        }
    }
}
