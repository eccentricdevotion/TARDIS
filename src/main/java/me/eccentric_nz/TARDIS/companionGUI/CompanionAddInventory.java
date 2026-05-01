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
import me.eccentric_nz.TARDIS.custommodels.GUICompanion;
import me.eccentric_nz.TARDIS.custommodels.GUIItemFactory;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardisCompanions;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author eccentric_nz
 */
public class CompanionAddInventory implements InventoryHolder {

    private final Player player;
    private final TARDIS plugin;
    private final UUID uuid;
    private final Inventory inventory;

    public CompanionAddInventory(TARDIS plugin, Player player) {
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
                    // TODO check this
                    head.setData(DataComponentTypes.PROFILE, ResolvableProfile.resolvableProfile(p.getPlayerProfile()));
                    head.setData(DataComponentTypes.CUSTOM_NAME, Component.text(p.getName()));
                    head.setData(DataComponentTypes.LORE, ItemLore.lore().addLine(Component.text(p.getUniqueId().toString())));
                    heads[i] = head;
                    i++;
                }
            }
        }
        // add buttons
        ItemStack info = ItemStack.of(GUICompanion.INFO.material(), 1);
        info.setData(DataComponentTypes.CUSTOM_NAME, Component.text("Info"));
        info.setData(DataComponentTypes.LORE, ItemLore.lore(List.of(
                Component.text("Click a player head to"),
                Component.text("add them as a companion.")
        )));
        heads[GUICompanion.INFO.slot()] = info;
        ItemStack list = ItemStack.of(GUICompanion.LIST_COMPANIONS.material(), 1);
        list.setData(DataComponentTypes.CUSTOM_NAME, Component.text("List companions"));
        heads[GUICompanion.LIST_COMPANIONS.slot()] = list;
        ItemStack everyone = ItemStack.of(GUICompanion.ALL_COMPANIONS.material(), 1);
        everyone.setData(DataComponentTypes.CUSTOM_NAME, Component.text("Add all online players"));
        heads[GUICompanion.ALL_COMPANIONS.slot()] = everyone;
        // Cancel / close
        heads[GUICompanion.BUTTON_CLOSE.slot()] = GUIItemFactory.close();

        return heads;
    }
}
