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
import me.eccentric_nz.TARDIS.custommodeldata.GUIChameleonPresets;
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
class TARDISLazarusInventory {

    private final ItemStack[] pageOne;
    private final TARDIS plugin;
    private final List<Material> disguises = new ArrayList<>();

    TARDISLazarusInventory(TARDIS plugin) {
        this.plugin = plugin;
        // maximum number of eggs is 45
        // passive
        disguises.add(Material.ALLAY_SPAWN_EGG);
        disguises.add(Material.ARMADILLO_SPAWN_EGG);
        disguises.add(Material.AXOLOTL_SPAWN_EGG);
        disguises.add(Material.BAT_SPAWN_EGG);
        disguises.add(Material.CAMEL_SPAWN_EGG);
        disguises.add(Material.CAT_SPAWN_EGG);
        disguises.add(Material.CHICKEN_SPAWN_EGG);
        disguises.add(Material.COD_SPAWN_EGG);
        disguises.add(Material.COW_SPAWN_EGG);
        disguises.add(Material.DONKEY_SPAWN_EGG);
        disguises.add(Material.FOX_SPAWN_EGG);
        disguises.add(Material.FROG_SPAWN_EGG);
        disguises.add(Material.GLOW_SQUID_SPAWN_EGG);
        disguises.add(Material.HORSE_SPAWN_EGG);
        disguises.add(Material.MOOSHROOM_SPAWN_EGG);
        disguises.add(Material.MULE_SPAWN_EGG);
        disguises.add(Material.OCELOT_SPAWN_EGG);
        disguises.add(Material.PARROT_SPAWN_EGG);
        disguises.add(Material.PIG_SPAWN_EGG);
        disguises.add(Material.PUFFERFISH_SPAWN_EGG);
        disguises.add(Material.RABBIT_SPAWN_EGG);
        disguises.add(Material.SALMON_SPAWN_EGG);
        disguises.add(Material.SHEEP_SPAWN_EGG);
        disguises.add(Material.SKELETON_HORSE_SPAWN_EGG);
        disguises.add(Material.SNIFFER_SPAWN_EGG);
        disguises.add(Material.SNOW_GOLEM_SPAWN_EGG);
        disguises.add(Material.SQUID_SPAWN_EGG);
        disguises.add(Material.STRIDER_SPAWN_EGG);
        disguises.add(Material.TADPOLE_SPAWN_EGG);
        disguises.add(Material.TROPICAL_FISH_SPAWN_EGG);
        disguises.add(Material.TURTLE_SPAWN_EGG);
        disguises.add(Material.VILLAGER_SPAWN_EGG);
        // neutral
        disguises.add(Material.BEE_SPAWN_EGG);
        disguises.add(Material.CAVE_SPIDER_SPAWN_EGG);
        disguises.add(Material.DOLPHIN_SPAWN_EGG);
        disguises.add(Material.ENDERMAN_SPAWN_EGG);
        disguises.add(Material.GOAT_SPAWN_EGG);
        disguises.add(Material.IRON_GOLEM_SPAWN_EGG);
        disguises.add(Material.LLAMA_SPAWN_EGG);
        disguises.add(Material.PANDA_SPAWN_EGG);
        disguises.add(Material.PIGLIN_SPAWN_EGG);

        pageOne = getItemStack();
    }

    /**
     * Constructs an inventory for the Genetic Manipulator GUI.
     *
     * @return an Array of itemStacks (an inventory)
     */
    private ItemStack[] getItemStack() {
        ItemStack[] stacks = new ItemStack[54];
        int i = 0;
        for (Material m : disguises) {
            ItemStack egg = new ItemStack(m, 1);
            ItemMeta me = egg.getItemMeta();
            if (m == Material.MOOSHROOM_SPAWN_EGG) {
                me.setDisplayName("MUSHROOM_COW");
            } else {
                me.setDisplayName(m.toString().replace("_SPAWN_EGG", ""));
            }
            egg.setItemMeta(me);
            stacks[i] = egg;
            i++;
        }
        // page two
        ItemStack page = new ItemStack(GUIChameleonPresets.GO_TO_PAGE_2.material(), 1);
        ItemMeta two = page.getItemMeta();
        two.setDisplayName(plugin.getLanguage().getString("BUTTON_PAGE_2"));
        two.setCustomModelData(GUIChameleonPresets.GO_TO_PAGE_2.customModelData());
        page.setItemMeta(two);
        stacks[42] = page;
        // add skins
        ItemStack down = new ItemStack(GUIGeneticManipulator.BUTTON_SKINS.material(), 1);
        ItemMeta load = down.getItemMeta();
        load.setDisplayName("TARDIS Television");
        load.setCustomModelData(GUIGeneticManipulator.BUTTON_SKINS.customModelData());
        down.setItemMeta(load);
        stacks[GUIGeneticManipulator.BUTTON_SKINS.slot()] = down;
        // TARDISWeepingAngels monsters
        ItemStack weep = new ItemStack(GUIGeneticManipulator.BUTTON_TWA.material(), 1);
        ItemMeta ing = weep.getItemMeta();
        ing.setDisplayName("TARDIS Monsters");
        ing.setCustomModelData(GUIGeneticManipulator.BUTTON_TWA.customModelData());
        weep.setItemMeta(ing);
        stacks[GUIGeneticManipulator.BUTTON_TWA.slot()] = weep;
        // add options
        // master
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
        // tamed
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

    public ItemStack[] getPageOne() {
        return pageOne;
    }
}
