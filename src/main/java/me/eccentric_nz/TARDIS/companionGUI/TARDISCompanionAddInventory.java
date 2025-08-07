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
package me.eccentric_nz.TARDIS.companionGUI;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.custommodels.GUICompanion;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardisCompanions;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author eccentric_nz
 */
public class TARDISCompanionAddInventory implements InventoryHolder {

    private final Player player;
    private final TARDIS plugin;
    private final UUID uuid;
    private final Inventory inventory;

    public TARDISCompanionAddInventory(TARDIS plugin, Player player) {
        this.plugin = plugin;
        this.player = player;
        uuid = this.player.getUniqueId();
        this.inventory = plugin.getServer().createInventory(this, 54, Component.text("Add Companion", NamedTextColor.DARK_RED));
        this.inventory.setContents(getItemStack());
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    private ItemStack[] getItemStack() {
        ItemStack[] heads = new ItemStack[54];
        // get current companions
        List<String> comps;
        ResultSetTardisCompanions rs = new ResultSetTardisCompanions(plugin);
        if (rs.fromUUID(uuid.toString()) && rs.getCompanions() != null && !rs.getCompanions().isEmpty()) {
            comps = List.of(rs.getCompanions().split(":"));
        } else {
            comps = new ArrayList<>();
        }
        int i = 0;
        for (Player p : plugin.getServer().getOnlinePlayers()) {
            if (i < 45) {
                UUID puid = p.getUniqueId();
                if (puid != uuid && !comps.contains(puid.toString()) && VanishChecker.canSee(player, p)) {
                    ItemStack head = ItemStack.of(Material.PLAYER_HEAD, 1);
                    SkullMeta skull = (SkullMeta) head.getItemMeta();
                    skull.setOwningPlayer(p);
                    skull.displayName(Component.text(p.getName()));
                    skull.lore(List.of(Component.text(p.getUniqueId().toString())));
                    head.setItemMeta(skull);
                    heads[i] = head;
                    i++;
                }
            }
        }
        // add buttons
        ItemStack info = ItemStack.of(GUICompanion.INFO.material(), 1);
        ItemMeta ii = info.getItemMeta();
        ii.displayName(Component.text("Info"));
        ii.lore(List.of(
                Component.text("Click a player head to"),
                Component.text("add them as a companion.")
        ));
        info.setItemMeta(ii);
        heads[GUICompanion.INFO.slot()] = info;
        ItemStack list = ItemStack.of(GUICompanion.LIST_COMPANIONS.material(), 1);
        ItemMeta ll = list.getItemMeta();
        ll.displayName(Component.text("List companions"));
        list.setItemMeta(ll);
        heads[GUICompanion.LIST_COMPANIONS.slot()] = list;
        ItemStack every = ItemStack.of(GUICompanion.ALL_COMPANIONS.material(), 1);
        ItemMeta one = every.getItemMeta();
        one.displayName(Component.text("Add all online players"));
        every.setItemMeta(one);
        heads[GUICompanion.ALL_COMPANIONS.slot()] = every;
        // Cancel / close
        ItemStack close = ItemStack.of(GUICompanion.BUTTON_CLOSE.material(), 1);
        ItemMeta can = close.getItemMeta();
        can.displayName(Component.text(plugin.getLanguage().getString("BUTTON_CLOSE", "Close")));
        close.setItemMeta(can);
        heads[GUICompanion.BUTTON_CLOSE.slot()] = close;

        return heads;
    }
}
