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
package me.eccentric_nz.TARDIS.companionGUI;

import me.eccentric_nz.TARDIS.TARDIS;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.List;

public class EveryoneCompanionInventory implements InventoryHolder {

    private final TARDIS plugin;
    private final Player player;
    private final Inventory inventory;

    public EveryoneCompanionInventory(TARDIS plugin, Player player) {
        this.plugin = plugin;
        this.player = player;
        this.inventory = plugin.getServer().createInventory(this, 54, Component.text("Companions", NamedTextColor.DARK_RED));
        this.inventory.setContents(getItemStack());
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    private ItemStack[] getItemStack() {
        ItemStack[] heads = new ItemStack[54];
        int i = 0;
        for (Player c : plugin.getServer().getOnlinePlayers()) {
            if (i < 45) {
                if (VanishChecker.canSee(player, c)) {
                    ItemStack head = ItemStack.of(Material.PLAYER_HEAD, 1);
                    SkullMeta skull = (SkullMeta) head.getItemMeta();
                    skull.setOwningPlayer(c);
                    skull.displayName(Component.text(c.getName()));
                    skull.lore(List.of(Component.text(c.getUniqueId().toString())));
                    head.setItemMeta(skull);
                    heads[i] = head;
                    i++;
                }
            }
        }
        // add buttons
        ItemStack info = ItemStack.of(Material.BOOK, 1);
        ItemMeta ii = info.getItemMeta();
        ii.displayName(Component.text("Info"));
        ii.lore(List.of(
                Component.text("To REMOVE a companion"),
                Component.text("select a player head"),
                Component.text("then click the Remove"),
                Component.text("button (bucket)."),
                Component.text("To ADD a companion"),
                Component.text("click the Add button"),
                Component.text("(nether star).")
        ));
        info.setItemMeta(ii);
        heads[45] = info;
        ItemStack add = ItemStack.of(Material.NETHER_STAR, 1);
        ItemMeta aa = add.getItemMeta();
        aa.displayName(Component.text("Add"));
        add.setItemMeta(aa);
        heads[48] = add;
        ItemStack del = ItemStack.of(Material.BUCKET, 1);
        ItemMeta dd = add.getItemMeta();
        dd.displayName(Component.text("Remove"));
        del.setItemMeta(dd);
        heads[51] = del;
        // Cancel / close
        ItemStack close = ItemStack.of(Material.BOWL, 1);
        ItemMeta can = close.getItemMeta();
        can.displayName(Component.text(plugin.getLanguage().getString("BUTTON_CLOSE", "Close")));
        close.setItemMeta(can);
        heads[53] = close;

        return heads;
    }
}
