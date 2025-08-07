/*
 * Copyright (C) 2025 eccentric_nz
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
package me.eccentric_nz.TARDIS.commands.config;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.playerprefs.TARDISPrefsMenuInventory;
import me.eccentric_nz.TARDIS.custommodels.GUIConfiguration;
import me.eccentric_nz.TARDIS.utility.ComponentUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.components.CustomModelDataComponent;

import java.util.List;
import java.util.Locale;

/**
 * The architectural reconfiguration system is a component of the Doctor's TARDIS in the shape of a tree that, according
 * to the Eleventh Doctor, "reconstructs the particles according to your needs." It is basically "a machine that makes
 * machines," perhaps somewhat like a 3D printer. It is, according to Gregor Van Baalen's scanner, "more valuable than
 * the total sum of any currency.
 *
 * @author eccentric_nz
 */
public class TARDISConfigMenuListener implements Listener {

    private final TARDIS plugin;

    public TARDISConfigMenuListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onAdminMenuClick(InventoryClickEvent event) {
        if (!(event.getInventory().getHolder(false) instanceof TARDISConfigMenuInventory) && !(event.getInventory().getHolder(false) instanceof TARDISConfigPageTwoInventory)) {
            return;
        }
        event.setCancelled(true);
        int slot = event.getRawSlot();
        if (slot < 0 || slot > 53) {
            return;
        }
        InventoryView view = event.getView();
        String option = getDisplay(view, slot);
        if (slot == 52) {
            Player p = (Player) event.getWhoClicked();
            // close this gui and load the previous / next page
            if (option.equals("Previous page")) {
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () ->
                        p.openInventory(new TARDISConfigMenuInventory(plugin).getInventory()), 1L);
            } else {
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () ->
                        p.openInventory(new TARDISConfigPageTwoInventory(plugin).getInventory()), 1L);
            }
            return;
        }
        if (slot == 53 && option.equals("Player Preferences")) {
            Player p = (Player) event.getWhoClicked();
            // close this gui and load the Player Prefs Menu
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () ->
                    p.openInventory(new TARDISPrefsMenuInventory(plugin, p.getUniqueId()).getInventory()), 1L);
            return;
        }
        if (!option.isEmpty()) {
            boolean bool = plugin.getConfig().getBoolean(option);
            if (option.equals("abandon.enabled") && !bool && (plugin.getConfig().getBoolean("creation.create_worlds") || plugin.getConfig().getBoolean("creation.create_worlds_with_perms"))) {
                Player p = (Player) event.getWhoClicked();
                plugin.getMessenger().messageWithColour(p, "Abandoned TARDISes cannot be enabled as TARDISes are not stored in a TIPS world!", "#FF5555");
                return;
            }
            plugin.getConfig().set(option, !bool);
            String lore = (bool) ? "false" : "true";
            setLore(view, slot, lore, option);
            plugin.saveConfig();
        }
    }

    private String getDisplay(InventoryView view, int slot) {
        ItemStack is = view.getItem(slot);
        if (is != null) {
            ItemMeta im = is.getItemMeta();
            return ComponentUtils.stripColour(im.displayName());
        } else {
            return "";
        }
    }

    private void setLore(InventoryView view, int slot, String str, String option) {
        List<Component> lore = (str != null) ? List.of(Component.text(str)) : null;
        ItemStack is = view.getItem(slot);
        ItemMeta im = is.getItemMeta();
        im.lore(lore);
        GUIConfiguration gui = GUIConfiguration.valueOf(option.split("\\.")[0].toUpperCase(Locale.ROOT));
        CustomModelDataComponent component = im.getCustomModelDataComponent();
        component.setFloats("false".equals(str) ? gui.getOffFloats() : gui.getOnFloats());
        im.setCustomModelDataComponent(component);
        is.setItemMeta(im);
    }
}
