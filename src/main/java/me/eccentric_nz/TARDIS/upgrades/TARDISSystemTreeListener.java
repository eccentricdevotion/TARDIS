package me.eccentric_nz.TARDIS.upgrades;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.custommodeldata.GUISystemTree;
import me.eccentric_nz.TARDIS.database.data.SystemUpgrade;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetPlayerArtronLevel;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTravellers;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.listeners.TARDISMenuListener;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.List;

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
            case 9 -> clicked = GUISystemTree.ARCHITECTURE;
            case 13 -> clicked = GUISystemTree.NAVIGATION;
            case 16 -> clicked = GUISystemTree.TOOLS;
            case 19 -> clicked = GUISystemTree.CHAMELEON_CIRCUIT;
            case 23 -> clicked = GUISystemTree.SAVES;
            case 26 -> clicked = GUISystemTree.TARDIS_LOCATOR;
            case 28 -> clicked = GUISystemTree.ROOM_GROWING;
            case 32 -> clicked = GUISystemTree.DISTANCE_1;
            case 35 -> clicked = GUISystemTree.BIOME_READER;
            case 37 -> clicked = GUISystemTree.DESKTOP_THEME;
            case 41 -> clicked = GUISystemTree.DISTANCE_2;
            case 44 -> clicked = GUISystemTree.FORCE_FIELD;
            case 48 -> clicked = GUISystemTree.INTER_DIMENSIONAL_TRAVEL;
            case 50 -> clicked = GUISystemTree.DISTANCE_3;
            case 53 -> clicked = GUISystemTree.STATTENHEIM_REMOTE;
            default -> clicked = GUISystemTree.UPGRADE_TREE;
        }
        try {
            GUISystemTree required = GUISystemTree.valueOf(clicked.getRequired());
            if (!current.getUpgrades().get(required)) {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "SYS_REQUIRED", required.getName());
                player.playSound(player.getLocation(), Sound.ENTITY_WARDEN_TENDRIL_CLICKS, 1.0f, 1.0f);
            } else {
                // check if they have upgrade already
                if (current.getUpgrades().get(clicked)) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "SYS_HAS", clicked.getName());
                    player.playSound(player.getLocation(), Sound.ENTITY_WARDEN_STEP, 1.0f, 1.0f);
                    return;
                }
                // check artron
                int cost;
                if (clicked.getBranch().equals("branch")) {
                    cost = plugin.getSystemUpgradesConfig().getInt("branch");
                } else {
                    cost = plugin.getSystemUpgradesConfig().getInt(clicked.getBranch() + "." + clicked.toString().toLowerCase());
                }
                if (cost > current.getArtronLevel()) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "SYS_COST", clicked.getName());
                    player.playSound(player.getLocation(), Sound.ENTITY_CAT_EAT, 1.0f, 1.0f);
                    return;
                }
                // debit
                HashMap<String, Object> wheretl = new HashMap<>();
                wheretl.put("uuid", uuid);
                plugin.getQueryFactory().alterEnergyLevel("player_prefs", -cost, wheretl, player);
                // play sound
                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
                // message
                plugin.getMessenger().send(player, TardisModule.TARDIS, "SYS_SUCCESS", clicked.getName());
                // update system upgrade record
                new SystemUpgradeUpdate(plugin).set(uuid, id, clicked);
                // set custom model data for clicked upgrade
                ItemMeta im = is.getItemMeta();
                im.setCustomModelData(im.getCustomModelData() + 1000);
                List<String> lore = im.getLore();
                lore.set(lore.size() - 1, ChatColor.GREEN + "" + org.bukkit.ChatColor.ITALIC + "Unlocked");
                im.setLore(lore);
                is.setItemMeta(im);
                // set artron level remaining for item in system tree slot
                int remaining = current.getArtronLevel() - cost;
                ItemStack st = view.getItem(GUISystemTree.UPGRADE_TREE.getSlot());
                ItemMeta stim = st.getItemMeta();
                List<String> stlore = stim.getLore();
                stlore.set(3, ChatColor.AQUA + "" + ChatColor.ITALIC + "Artron Level: " + remaining);
                stim.setLore(stlore);
                st.setItemMeta(stim);
            }
        } catch (IllegalArgumentException e) {
            // clicked upgrade tree
            plugin.debug("IllegalArgumentException for " + clicked.getRequired());
            close(player);
        }
    }
}
