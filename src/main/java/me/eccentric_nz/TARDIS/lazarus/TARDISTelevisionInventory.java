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
import me.eccentric_nz.TARDIS.custommodels.GUIChameleonPoliceBoxes;
import me.eccentric_nz.TARDIS.custommodels.GUIChameleonPresets;
import me.eccentric_nz.TARDIS.custommodels.GUIGeneticManipulator;
import me.eccentric_nz.TARDIS.skins.*;
import me.eccentric_nz.TARDIS.skins.tv.PlayerHeadCache;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.List;

public class TARDISTelevisionInventory implements InventoryHolder {

    private final TARDIS plugin;
    private final Inventory inventory;

    public TARDISTelevisionInventory(TARDIS plugin) {
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
        // 16 doctors
        if (PlayerHeadCache.DOCTORS.isEmpty()) {
            for (Skin doctor : DoctorSkins.DOCTORS) {
                ItemStack is = ItemStack.of(Material.PLAYER_HEAD, 1);
                ItemMeta im = is.getItemMeta();
                SkinUtils.getHeadProfile(doctor).thenAccept(playerProfile -> {
                    is.setData(DataComponentTypes.PROFILE, ResolvableProfile.resolvableProfile(playerProfile));
                    String[] name = doctor.name().split(" - ");
                    im.displayName(Component.text(name[0]));
                    im.lore(List.of(Component.text(name[1])));
                    is.setItemMeta(im);
                    // cache the item stack
                    PlayerHeadCache.DOCTORS.add(is);
                });
                stacks[i] = is;
                i++;
            }
        } else {
            for (ItemStack is : PlayerHeadCache.DOCTORS) {
                stacks[i] = is;
                i++;
            }
        }
        // 19 companions
        if (PlayerHeadCache.COMPANIONS.isEmpty()) {
            for (Skin companion : CompanionSkins.COMPANIONS) {
                ItemStack is = ItemStack.of(Material.PLAYER_HEAD, 1);
                SkullMeta im = (SkullMeta) is.getItemMeta();
                SkinUtils.getHeadProfile(companion).thenAccept(playerProfile -> {
                    is.setData(DataComponentTypes.PROFILE, ResolvableProfile.resolvableProfile(playerProfile));
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
        // 13 characters
        if (PlayerHeadCache.LAZARUS_CHARACTERS.isEmpty()) {
            for (Skin character : CharacterSkins.LAZARUS_CHARACTERS) {
                ItemStack is = ItemStack.of(Material.PLAYER_HEAD, 1);
                SkullMeta im = (SkullMeta) is.getItemMeta();
                SkinUtils.getHeadProfile(character).thenAccept(playerProfile -> {
                    is.setData(DataComponentTypes.PROFILE, ResolvableProfile.resolvableProfile(playerProfile));
                    im.displayName(Component.text(character.name()));
                    is.setItemMeta(im);
                    // cache the item stack
                    PlayerHeadCache.LAZARUS_CHARACTERS.add(is);
                });
                stacks[i] = is;
                i++;
            }
        } else {
            for (ItemStack is : PlayerHeadCache.LAZARUS_CHARACTERS) {
                stacks[i] = is;
                i++;
            }
        }
        // page one
        ItemStack page1 = ItemStack.of(GUIChameleonPoliceBoxes.GO_TO_PAGE_1.material(), 1);
        ItemMeta one = page1.getItemMeta();
        one.displayName(Component.text(plugin.getLanguage().getString("BUTTON_PAGE_1", "Go to page 1")));
        page1.setItemMeta(one);
        stacks[48] = page1;
        // page two
        ItemStack page2 = ItemStack.of(GUIChameleonPresets.GO_TO_PAGE_2.material(), 1);
        ItemMeta two = page2.getItemMeta();
        two.displayName(Component.text(plugin.getLanguage().getString("BUTTON_PAGE_2", "Go to page 2")));
        page2.setItemMeta(two);
        stacks[49] = page2;
        // TARDISWeepingAngels monsters
        ItemStack weep = ItemStack.of(GUIGeneticManipulator.BUTTON_TWA.material(), 1);
        ItemMeta ing = weep.getItemMeta();
        ing.displayName(Component.text("TARDIS Monsters"));
        weep.setItemMeta(ing);
        stacks[50] = weep;
        // add buttons
        ItemStack rem = ItemStack.of(GUIGeneticManipulator.BUTTON_RESTORE.material(), 1);
        ItemMeta ove = rem.getItemMeta();
        ove.displayName(Component.text(plugin.getLanguage().getString("BUTTON_RESTORE", "Restore my original genetic material")));
        rem.setItemMeta(ove);
        stacks[GUIGeneticManipulator.BUTTON_RESTORE.slot()] = rem;
        // set
        ItemStack s = ItemStack.of(GUIGeneticManipulator.BUTTON_DNA.material(), 1);
        ItemMeta sim = s.getItemMeta();
        sim.displayName(Component.text(plugin.getLanguage().getString("BUTTON_DNA", "Modify my genetic material")));
        s.setItemMeta(sim);
        stacks[GUIGeneticManipulator.BUTTON_DNA.slot()] = s;
        // cancel
        ItemStack can = ItemStack.of(GUIGeneticManipulator.BUTTON_CANCEL.material(), 1);
        ItemMeta cel = can.getItemMeta();
        cel.displayName(Component.text(plugin.getLanguage().getString("BUTTON_CANCEL", "Cancel")));
        can.setItemMeta(cel);
        stacks[GUIGeneticManipulator.BUTTON_CANCEL.slot()] = can;
        return stacks;
    }
}
