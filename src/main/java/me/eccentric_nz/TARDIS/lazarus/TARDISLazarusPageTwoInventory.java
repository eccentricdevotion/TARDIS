/*
 * Copyright (C) 2024 eccentric_nz
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
import me.eccentric_nz.TARDIS.custommodeldata.GUIChameleonPoliceBoxes;
import me.eccentric_nz.TARDIS.custommodeldata.GUIGeneticManipulator;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

/**
 * The Genetic Manipulation Device was invented by Professor Richard Lazarus. The machine would turn anyone inside
 * decades younger, but the process contained one side effect: genes that evolution rejected and left dormant would be
 * unlocked, transforming the human into a giant skeletal scorpion-like beast that fed off the lifeforce of living
 * creatures.
 *
 * @author eccentric_nz
 */
class TARDISLazarusPageTwoInventory {

    private final ItemStack[] extra;
    private final TARDIS plugin;
    private final List<Material> disguises = new ArrayList<>();

    TARDISLazarusPageTwoInventory(TARDIS plugin) {
        this.plugin = plugin;
        disguises.add(Material.POLAR_BEAR_SPAWN_EGG);
        disguises.add(Material.SPIDER_SPAWN_EGG);
        disguises.add(Material.TRADER_LLAMA_SPAWN_EGG);
        disguises.add(Material.WANDERING_TRADER_SPAWN_EGG);
        disguises.add(Material.WOLF_SPAWN_EGG);
        disguises.add(Material.ZOMBIFIED_PIGLIN_SPAWN_EGG);
        // hostile
        disguises.add(Material.BLAZE_SPAWN_EGG);
        disguises.add(Material.BOGGED_SPAWN_EGG);
        disguises.add(Material.BREEZE_SPAWN_EGG);
        disguises.add(Material.CREEPER_SPAWN_EGG);
        disguises.add(Material.DROWNED_SPAWN_EGG);
        disguises.add(Material.ELDER_GUARDIAN_SPAWN_EGG);
        disguises.add(Material.ENDERMITE_SPAWN_EGG);
        disguises.add(Material.EVOKER_SPAWN_EGG);
        disguises.add(Material.GHAST_SPAWN_EGG);
        disguises.add(Material.GUARDIAN_SPAWN_EGG);
        disguises.add(Material.HOGLIN_SPAWN_EGG);
        disguises.add(Material.HUSK_SPAWN_EGG);
        disguises.add(Material.MAGMA_CUBE_SPAWN_EGG);
        disguises.add(Material.PHANTOM_SPAWN_EGG);
        disguises.add(Material.PIGLIN_BRUTE_SPAWN_EGG);
        disguises.add(Material.PILLAGER_SPAWN_EGG);
        disguises.add(Material.RAVAGER_SPAWN_EGG);
        disguises.add(Material.SHULKER_SPAWN_EGG);
        disguises.add(Material.SILVERFISH_SPAWN_EGG);
        disguises.add(Material.SKELETON_SPAWN_EGG);
        disguises.add(Material.SLIME_SPAWN_EGG);
        disguises.add(Material.STRAY_SPAWN_EGG);
        disguises.add(Material.VEX_SPAWN_EGG);
        disguises.add(Material.VINDICATOR_SPAWN_EGG);
        disguises.add(Material.WARDEN_SPAWN_EGG);
        disguises.add(Material.WITCH_SPAWN_EGG);
        disguises.add(Material.WITHER_SKELETON_SPAWN_EGG);
        disguises.add(Material.ZOGLIN_SPAWN_EGG);
        disguises.add(Material.ZOMBIE_SPAWN_EGG);
        disguises.add(Material.ZOMBIE_VILLAGER_SPAWN_EGG);
        // boss
        disguises.add(Material.ENDER_DRAGON_SPAWN_EGG);
        disguises.add(Material.WITHER_SPAWN_EGG);
        // unused
        disguises.add(Material.ZOMBIE_HORSE_SPAWN_EGG);

        extra = getItemStack();
    }

    /**
     * Constructs an inventory for the Genetic Manipulator GUI.
     *
     * @return an Array of itemStacks (an inventory)
     */
    private ItemStack[] getItemStack() {
        ItemStack[] stacks = new ItemStack[54];
        int i = 0;
        // hostile & boss & unused
        for (Material m : disguises) {
            ItemStack egg = new ItemStack(m, 1);
            ItemMeta me = egg.getItemMeta();
            me.setDisplayName(m.toString().replace("_SPAWN_EGG", ""));
            egg.setItemMeta(me);
            stacks[i] = egg;
            i++;
        }
        // put illusioner
        ItemStack ill = new ItemStack(Material.BOW, 1);
        ItemMeta usi = ill.getItemMeta();
        usi.setDisplayName("ILLUSIONER");
        ill.setItemMeta(usi);
        stacks[i] = ill;
        i++;
        // put herobrine
        ItemStack hero = new ItemStack(Material.PLAYER_HEAD, 1);
        ItemMeta brine = hero.getItemMeta();
        brine.setDisplayName("HEROBRINE");
        hero.setItemMeta(brine);
        stacks[i] = hero;
        // page one
        ItemStack page = new ItemStack(GUIChameleonPoliceBoxes.GO_TO_PAGE_1.material(), 1);
        ItemMeta one = page.getItemMeta();
        one.setDisplayName(plugin.getLanguage().getString("BUTTON_PAGE_1"));
        one.setCustomModelData(GUIChameleonPoliceBoxes.GO_TO_PAGE_1.customModelData());
        page.setItemMeta(one);
        stacks[43] = page;
        // if TARDISWeepingAngels is enabled angels, cybermen and ice warriors will be available
        if (plugin.getConfig().getBoolean("modules.weeping_angels")) {
            ItemStack weep = new ItemStack(GUIGeneticManipulator.BUTTON_TWA.material(), 1);
            ItemMeta ing = weep.getItemMeta();
            ing.setDisplayName("TARDIS Monsters");
            ing.setCustomModelData(GUIGeneticManipulator.BUTTON_TWA.customModelData());
            weep.setItemMeta(ing);
            stacks[GUIGeneticManipulator.BUTTON_TWA.slot()] = weep;
        }
        // add options
        ItemStack the = new ItemStack(GUIGeneticManipulator.BUTTON_MASTER.material(), 1);
        ItemMeta master = the.getItemMeta();
        master.setDisplayName(plugin.getLanguage().getString("BUTTON_MASTER"));
        master.setLore(List.of(plugin.getLanguage().getString("SET_OFF")));
        master.setCustomModelData(GUIGeneticManipulator.BUTTON_MASTER.customModelData());
        the.setItemMeta(master);
        stacks[GUIGeneticManipulator.BUTTON_MASTER.slot()] = the;
        // adult
        ItemStack adult = new ItemStack(GUIGeneticManipulator.BUTTON_AGE.material(), 1);
        ItemMeta baby = adult.getItemMeta();
        baby.setDisplayName(plugin.getLanguage().getString("BUTTON_AGE"));
        baby.setLore(List.of("ADULT"));
        baby.setCustomModelData(GUIGeneticManipulator.BUTTON_AGE.customModelData());
        adult.setItemMeta(baby);
        stacks[GUIGeneticManipulator.BUTTON_AGE.slot()] = adult;
        // type
        ItemStack typ = new ItemStack(GUIGeneticManipulator.BUTTON_TYPE.material(), 1);
        ItemMeta col = typ.getItemMeta();
        col.setDisplayName(plugin.getLanguage().getString("BUTTON_TYPE"));
        col.setLore(List.of("WHITE"));
        col.setCustomModelData(GUIGeneticManipulator.BUTTON_TYPE.customModelData());
        typ.setItemMeta(col);
        stacks[GUIGeneticManipulator.BUTTON_TYPE.slot()] = typ;
        // tamed etc
        ItemStack tamed = new ItemStack(GUIGeneticManipulator.BUTTON_OPTS.material(), 1);
        ItemMeta tf = tamed.getItemMeta();
        tf.setDisplayName(plugin.getLanguage().getString("BUTTON_OPTS"));
        List<String> opts = new ArrayList<>();
        for (String o : plugin.getLanguage().getString("BUTTON_OPTS_LIST").split("/")) {
            opts.add(ChatColor.ITALIC + o + ChatColor.RESET);
        }
        opts.add(ChatColor.RED + "FALSE");
        tf.setLore(opts);
        tf.setCustomModelData(GUIGeneticManipulator.BUTTON_OPTS.customModelData());
        tamed.setItemMeta(tf);
        stacks[GUIGeneticManipulator.BUTTON_OPTS.slot()] = tamed;
        // add buttons
        ItemStack rem = new ItemStack(GUIGeneticManipulator.BUTTON_RESTORE.material(), 1);
        ItemMeta ove = rem.getItemMeta();
        ove.setDisplayName(plugin.getLanguage().getString("BUTTON_RESTORE"));
        ove.setCustomModelData(GUIGeneticManipulator.BUTTON_RESTORE.customModelData());
        rem.setItemMeta(ove);
        stacks[GUIGeneticManipulator.BUTTON_RESTORE.slot()] = rem;
        // set
        ItemStack s = new ItemStack(GUIGeneticManipulator.BUTTON_DNA.material(), 1);
        ItemMeta sim = s.getItemMeta();
        sim.setDisplayName(plugin.getLanguage().getString("BUTTON_DNA"));
        sim.setCustomModelData(GUIGeneticManipulator.BUTTON_DNA.customModelData());
        s.setItemMeta(sim);
        stacks[GUIGeneticManipulator.BUTTON_DNA.slot()] = s;
        // cancel
        ItemStack can = new ItemStack(GUIGeneticManipulator.BUTTON_CANCEL.material(), 1);
        ItemMeta cel = can.getItemMeta();
        cel.setDisplayName(plugin.getLanguage().getString("BUTTON_CANCEL"));
        cel.setCustomModelData(GUIGeneticManipulator.BUTTON_CANCEL.customModelData());
        can.setItemMeta(cel);
        stacks[GUIGeneticManipulator.BUTTON_CANCEL.slot()] = can;

        return stacks;
    }

    public ItemStack[] getPageTwo() {
        return extra;
    }
}
