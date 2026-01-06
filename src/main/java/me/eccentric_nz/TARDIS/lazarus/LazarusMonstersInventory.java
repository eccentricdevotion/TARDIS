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
package me.eccentric_nz.TARDIS.lazarus;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.ResolvableProfile;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.skins.*;
import me.eccentric_nz.TARDIS.skins.tv.PlayerHeadCache;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class LazarusMonstersInventory extends LazarusItems implements InventoryHolder, LazarusGUI {

    private final TARDIS plugin;
    private final Inventory inventory;
    private int maxSlot;

    public LazarusMonstersInventory(TARDIS plugin) {
        this.plugin = plugin;
        this.inventory = plugin.getServer().createInventory(this, 54, Component.text("Genetic Manipulator", NamedTextColor.DARK_RED));
        this.inventory.setContents(getItemStack());
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    /**
     * Constructs an inventory for the Genetic Manipulator GUI.
     *
     * @return an Array of itemStacks (an inventory)
     */
    private ItemStack[] getItemStack() {
        ItemStack[] stacks = new ItemStack[54];
        int i = 0;
        // 27 monsters
        if (PlayerHeadCache.MONSTERS.isEmpty()) {
            for (Skin monster : MonsterSkins.MONSTERS) {
                ItemStack is = ItemStack.of(Material.PLAYER_HEAD, 1);
                SkullMeta im = (SkullMeta) is.getItemMeta();
                SkinUtils.getHeadProfile(monster).thenAccept(playerProfile -> {
                    is.setData(DataComponentTypes.PROFILE, ResolvableProfile.resolvableProfile(playerProfile));
                    im.setPlayerProfile(playerProfile);
                    im.displayName(Component.text(monster.name()));
                    is.setItemMeta(im);
                    // cache the item stack
                    PlayerHeadCache.MONSTERS.add(is);
                });
                stacks[i] = is;
                i++;
            }
        } else {
            for (ItemStack is : PlayerHeadCache.MONSTERS) {
                stacks[i] = is;
                i++;
            }
        }
        // add cyberman and cybershade
        for (Skin monster : CyberSkins.LAZARUS_CYBERS) {
            ItemStack is = ItemStack.of(Material.PLAYER_HEAD, 1);
            SkullMeta im = (SkullMeta) is.getItemMeta();
            SkinUtils.getHeadProfile(monster).thenAccept(playerProfile -> {
                is.setData(DataComponentTypes.PROFILE, ResolvableProfile.resolvableProfile(playerProfile));
                im.setPlayerProfile(playerProfile);
                im.displayName(Component.text(monster.name()));
                is.setItemMeta(im);
            });
            stacks[i] = is;
            i++;
        }
        // add adjacent characters
        for (Skin monster : CharacterSkins.LAZARUS_MONSTERS) {
            ItemStack is = ItemStack.of(Material.PLAYER_HEAD, 1);
            SkullMeta im = (SkullMeta) is.getItemMeta();
            SkinUtils.getHeadProfile(monster).thenAccept(playerProfile -> {
                is.setData(DataComponentTypes.PROFILE, ResolvableProfile.resolvableProfile(playerProfile));
                im.setPlayerProfile(playerProfile);
                im.displayName(Component.text(monster.name()));
                is.setItemMeta(im);
            });
            stacks[i] = is;
            i++;
        }
        // add standard buttons
        addItems(plugin, stacks, 8);

        return stacks;
    }

    @Override
    public int getMaxSlot() {
        return MonsterSkins.MONSTERS.size() + CyberSkins.LAZARUS_CYBERS.size() + CharacterSkins.LAZARUS_MONSTERS.size();
    }
}
