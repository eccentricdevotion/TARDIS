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

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.ItemLore;
import io.papermc.paper.datacomponent.item.ResolvableProfile;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.custommodels.GUIItemFactory;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

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
                    head.setData(DataComponentTypes.PROFILE, ResolvableProfile.resolvableProfile(c.getPlayerProfile()));
                    head.setData(DataComponentTypes.CUSTOM_NAME, Component.text(c.getName()));
                    head.setData(DataComponentTypes.LORE, ItemLore.lore().addLine(Component.text(c.getUniqueId().toString())).build());
                    heads[i] = head;
                    i++;
                }
            }
        }
        // add buttons
        ItemStack info = ItemStack.of(Material.BOOK, 1);
        info.setData(DataComponentTypes.CUSTOM_NAME, Component.text("Info"));
        info.setData(DataComponentTypes.LORE, ItemLore.lore(List.of(
                Component.text("To REMOVE a companion"),
                Component.text("select a player head"),
                Component.text("then click the Remove"),
                Component.text("button (bucket)."),
                Component.text("To ADD a companion"),
                Component.text("click the Add button"),
                Component.text("(nether star).")
        )));
        heads[45] = info;
        ItemStack add = ItemStack.of(Material.NETHER_STAR, 1);
        add.setData(DataComponentTypes.CUSTOM_NAME, Component.text("Add"));
        heads[48] = add;
        ItemStack del = ItemStack.of(Material.BUCKET, 1);
        del.setData(DataComponentTypes.CUSTOM_NAME, Component.text("Remove"));
        heads[51] = del;
        // Cancel / close
        heads[53] = GUIItemFactory.close();

        return heads;
    }
}
