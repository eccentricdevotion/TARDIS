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

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.custommodels.GUIChameleonPoliceBoxes;
import me.eccentric_nz.TARDIS.custommodels.GUIChameleonPresets;
import me.eccentric_nz.TARDIS.custommodels.GUIGeneticManipulator;
import me.eccentric_nz.TARDIS.skins.*;
import me.eccentric_nz.TARDIS.skins.tv.PlayerHeadCache;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerProfile;

import java.util.List;

public class TARDISTelevisionInventory {

    private final TARDIS plugin;
    private final ItemStack[] skins;

    public TARDISTelevisionInventory(TARDIS plugin) {
        this.plugin = plugin;
        this.skins = getItemStack();
    }

    private ItemStack[] getItemStack() {
        ItemStack[] stacks = new ItemStack[54];
        int i = 0;
        // 16 doctors
        if (PlayerHeadCache.DOCTORS.isEmpty()) {
            for (Skin doctor : DoctorSkins.DOCTORS) {
                ItemStack is = new ItemStack(Material.PLAYER_HEAD, 1);
                SkullMeta im = (SkullMeta) is.getItemMeta();
                PlayerProfile profile = SkinUtils.getHeadProfile(doctor);
                im.setOwnerProfile(profile);
                String[] name = doctor.name().split(" - ");
                im.setDisplayName(name[0]);
                im.setLore(List.of(name[1]));
                is.setItemMeta(im);
                // cache the item stack
                PlayerHeadCache.DOCTORS.add(is);
                stacks[i] = is;
                i++;
            }
        } else {
            for (ItemStack is : PlayerHeadCache.DOCTORS) {
                stacks[i] = is;
                i++;
            }
        }
        // 15 companions
        if (PlayerHeadCache.COMPANIONS.isEmpty()) {
            for (Skin companion : CompanionSkins.COMPANIONS) {
                ItemStack is = new ItemStack(Material.PLAYER_HEAD, 1);
                SkullMeta im = (SkullMeta) is.getItemMeta();
                PlayerProfile profile = SkinUtils.getHeadProfile(companion);
                im.setOwnerProfile(profile);
                im.setDisplayName(companion.name());
                is.setItemMeta(im);
                // cache the item stack
                PlayerHeadCache.COMPANIONS.add(is);
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
                ItemStack is = new ItemStack(Material.PLAYER_HEAD, 1);
                SkullMeta im = (SkullMeta) is.getItemMeta();
                PlayerProfile profile = SkinUtils.getHeadProfile(character);
                im.setOwnerProfile(profile);
                im.setDisplayName(character.name());
                is.setItemMeta(im);
                // cache the item stack
                PlayerHeadCache.LAZARUS_CHARACTERS.add(is);
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
        ItemStack page1 = new ItemStack(GUIChameleonPoliceBoxes.GO_TO_PAGE_1.material(), 1);
        ItemMeta one = page1.getItemMeta();
        one.setDisplayName(plugin.getLanguage().getString("BUTTON_PAGE_1"));
//        one.setItemModel(GUIChameleonPoliceBoxes.GO_TO_PAGE_1.key());
        page1.setItemMeta(one);
        stacks[45] = page1;
        // page two
        ItemStack page2 = new ItemStack(GUIChameleonPresets.GO_TO_PAGE_2.material(), 1);
        ItemMeta two = page2.getItemMeta();
        two.setDisplayName(plugin.getLanguage().getString("BUTTON_PAGE_2"));
//        two.setItemModel(GUIChameleonPresets.GO_TO_PAGE_2.key());
        page2.setItemMeta(two);
        stacks[46] = page2;
        // TARDISWeepingAngels monsters
        ItemStack weep = new ItemStack(GUIGeneticManipulator.BUTTON_TWA.material(), 1);
        ItemMeta ing = weep.getItemMeta();
        ing.setDisplayName("TARDIS Monsters");
//        ing.setItemModel(GUIGeneticManipulator.BUTTON_TWA.key());
        weep.setItemMeta(ing);
        stacks[47] = weep;
        // add buttons
        ItemStack rem = new ItemStack(GUIGeneticManipulator.BUTTON_RESTORE.material(), 1);
        ItemMeta ove = rem.getItemMeta();
        ove.setDisplayName(plugin.getLanguage().getString("BUTTON_RESTORE"));
//        ove.setItemModel(GUIGeneticManipulator.BUTTON_RESTORE.key());
        rem.setItemMeta(ove);
        stacks[GUIGeneticManipulator.BUTTON_RESTORE.slot()] = rem;
        // set
        ItemStack s = new ItemStack(GUIGeneticManipulator.BUTTON_DNA.material(), 1);
        ItemMeta sim = s.getItemMeta();
        sim.setDisplayName(plugin.getLanguage().getString("BUTTON_DNA"));
//        sim.setItemModel(GUIGeneticManipulator.BUTTON_DNA.key());
        s.setItemMeta(sim);
        stacks[GUIGeneticManipulator.BUTTON_DNA.slot()] = s;
        // cancel
        ItemStack can = new ItemStack(GUIGeneticManipulator.BUTTON_CANCEL.material(), 1);
        ItemMeta cel = can.getItemMeta();
        cel.setDisplayName(plugin.getLanguage().getString("BUTTON_CANCEL"));
//        cel.setItemModel(GUIGeneticManipulator.BUTTON_CANCEL.key());
        can.setItemMeta(cel);
        stacks[GUIGeneticManipulator.BUTTON_CANCEL.slot()] = can;
        return stacks;
    }

    public ItemStack[] getSkins() {
        return skins;
    }
}
