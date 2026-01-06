/*
 * Copyright (C) 2026 eccentric_nz
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.TARDIS.console.telepathic;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.advanced.CircuitChecker;
import me.eccentric_nz.TARDIS.custommodels.keys.SwitchVariant;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTravellers;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.listeners.TARDISMenuListener;
import me.eccentric_nz.TARDIS.upgrades.SystemTree;
import me.eccentric_nz.TARDIS.upgrades.SystemUpgradeChecker;
import me.eccentric_nz.TARDIS.utility.ComponentUtils;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;

public class TelepathicGUIListener extends TARDISMenuListener {

    private final TARDIS plugin;

    public TelepathicGUIListener(TARDIS plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onTelepathicMenuClick(InventoryClickEvent event) {
        if (!(event.getInventory().getHolder(false) instanceof TelepathicInventory)) {
            return;
        }
        Player player = (Player) event.getWhoClicked();
        String uuid = player.getUniqueId().toString();
        // get id of TARDIS player is in
        HashMap<String, Object> where = new HashMap<>();
        where.put("uuid", uuid);
        ResultSetTravellers rs = new ResultSetTravellers(plugin, where, false);
        if (rs.resultSet()) {
            // check for telepathic circuit
            CircuitChecker tcc = null;
            if (plugin.getConfig().getBoolean("difficulty.circuits") && !plugin.getUtils().inGracePeriod(player, true)) {
                tcc = new CircuitChecker(plugin, rs.getTardis_id());
                tcc.getCircuits();
            }
            if (tcc != null && !tcc.hasTelepathic()) {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "NO_TELEPATHIC_CIRCUIT");
                event.setCancelled(true);
                return;
            }
            int slot = event.getRawSlot();
            if (slot < 0 || slot > 53) {
                ClickType click = event.getClick();
                if (click.equals(ClickType.SHIFT_RIGHT) || click.equals(ClickType.SHIFT_LEFT) || click.equals(ClickType.DOUBLE_CLICK)) {
                    plugin.debug("TelepathicGUIListener");
                    event.setCancelled(true);
                }
                return;
            }
            event.setCancelled(true);
            ItemStack choice = event.getView().getItem(slot);
            if (slot > 0 && slot < 8 && plugin.getConfig().getBoolean("difficulty.system_upgrades") && !new SystemUpgradeChecker(plugin).has(uuid, SystemTree.TELEPATHIC_CIRCUIT)) {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "SYS_NEED", "Telepathic Circuit");
                return;
            }
            switch (slot) {
                // toggle telepathy on/off
                case 0 -> {
                    ItemMeta im = choice.getItemMeta();
                    int b = (im.hasLore() && ComponentUtils.endsWith(im.lore().getFirst(), "ON")) ? 0 : 1;
                    // update database
                    HashMap<String, Object> set = new HashMap<>();
                    HashMap<String, Object> whereu = new HashMap<>();
                    whereu.put("uuid", player.getUniqueId().toString());
                    set.put("telepathy_on", b);
                    plugin.getQueryFactory().doUpdate("player_prefs", set, whereu);
                    // set item model
                    NamespacedKey model = im.getItemModel();
                    im.setItemModel((model == SwitchVariant.TELEPATHIC_CIRCUIT_OFF.getKey()) ? SwitchVariant.TELEPATHIC_CIRCUIT_ON.getKey() : SwitchVariant.TELEPATHIC_CIRCUIT_OFF.getKey());
                    choice.setItemMeta(im);
                    plugin.getMessenger().announceRepeater(player, "Telepathic Circuit " + (b == 1 ? "ON" : "OFF"));
                    close(player);
                }
                // cave finder
                case 2 -> {
                    if (choice != null) {
                        player.performCommand("tardistravel cave");
                        close(player);
                    }
                }
                // structure finder
                case 4 -> {
                    if (choice != null) {
                        player.openInventory(new TelepathicStructure(plugin).getInventory());
                    }
                }
                // biome finder
                case 6 -> {
                    if (choice != null) {
                        player.openInventory(new TelepathicBiome(plugin, rs.getTardis_id()).getInventory());
                    }
                }
                // close
                case 8 -> close(player);
                // do nothing
                default -> {
                }
            }
        }
    }
}
