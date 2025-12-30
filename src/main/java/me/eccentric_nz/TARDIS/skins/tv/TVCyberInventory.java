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
package me.eccentric_nz.TARDIS.skins.tv;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.ResolvableProfile;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.skins.CyberSkins;
import me.eccentric_nz.TARDIS.skins.Skin;
import me.eccentric_nz.TARDIS.skins.SkinUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class TVCyberInventory extends TVGUI {

    public TVCyberInventory(TARDIS plugin) {
        this.inventory = plugin.getServer().createInventory(this, 36, Component.text("Cyberman Skins", NamedTextColor.DARK_RED));
        this.inventory.setContents(getItemStack());
    }

    /**
     * Constructs an inventory for the Monster Skins GUI.
     *
     * @return an Array of item stacks (an inventory)
     */
    private ItemStack[] getItemStack() {
        ItemStack[] stack = new ItemStack[36];
        int i = 0;
        if (PlayerHeadCache.CYBERS.isEmpty()) {
            for (Skin variant : CyberSkins.VARIANTS) {
                ItemStack is = ItemStack.of(Material.PLAYER_HEAD, 1);
                SkullMeta im = (SkullMeta) is.getItemMeta();
                SkinUtils.getHeadProfile(variant).thenAccept(playerProfile -> {
                    is.setData(DataComponentTypes.PROFILE, ResolvableProfile.resolvableProfile(playerProfile));
                    im.setPlayerProfile(playerProfile);
                    im.displayName(Component.text(variant.name()));
                    is.setItemMeta(im);
                    // cache the item stack
                    PlayerHeadCache.CYBERS.add(is);
                });
                stack[i] = is;
                i++;
            }
        } else {
            for (ItemStack is : PlayerHeadCache.CYBERS) {
                stack[i] = is;
                i++;
            }
        }
        addDefaults(stack);
        return stack;
    }
}
