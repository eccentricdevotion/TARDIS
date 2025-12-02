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
package me.eccentric_nz.TARDIS.lazarus;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.ResolvableProfile;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.skins.CompanionSkins;
import me.eccentric_nz.TARDIS.skins.Skin;
import me.eccentric_nz.TARDIS.skins.SkinUtils;
import me.eccentric_nz.TARDIS.skins.tv.PlayerHeadCache;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class TARDISLazarusCompanionInventory extends LazarusItems implements InventoryHolder, LazarusGUI {

    private final TARDIS plugin;
    private final Inventory inventory;

    public TARDISLazarusCompanionInventory(TARDIS plugin) {
        this.plugin = plugin;
        this.inventory = plugin.getServer().createInventory(this, 54, Component.text("Genetic Skins", NamedTextColor.DARK_RED));
        this.inventory.setContents(getItemStack());
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    private ItemStack[] getItemStack() {
        ItemStack[] stacks = new ItemStack[54];
        int i = 0;
        // 19 companions
        if (PlayerHeadCache.COMPANIONS.isEmpty()) {
            for (Skin companion : CompanionSkins.COMPANIONS) {
                ItemStack is = ItemStack.of(Material.PLAYER_HEAD, 1);
                SkullMeta im = (SkullMeta) is.getItemMeta();
                SkinUtils.getHeadProfile(companion).thenAccept(playerProfile -> {
                    is.setData(DataComponentTypes.PROFILE, ResolvableProfile.resolvableProfile(playerProfile));
                    im.setPlayerProfile(playerProfile);
                    im.displayName(Component.text(companion.name()));
                    is.setItemMeta(im);
                    // cache the item stack
                    PlayerHeadCache.COMPANIONS.add(is);
                });
                stacks[i] = is;
                i++;
            }
        } else {
            for (ItemStack is : PlayerHeadCache.COMPANIONS) {
                stacks[i] = is;
                i++;
            }
        }
        // add standard buttons
        addItems(plugin, stacks, 6);
        return stacks;
    }

    @Override
    public int getMaxSlot() {
        return CompanionSkins.COMPANIONS.size();
    }
}
