/*
 * Copyright (C) 2019 eccentric_nz
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
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Collections;
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

    private final ItemStack[] terminal;
    private final TARDIS plugin;
    private final List<Material> disguises = new ArrayList<>();

    TARDISLazarusInventory(TARDIS plugin) {
        this.plugin = plugin;
        // maximum number of eggs is 45
        disguises.add(Material.BAT_SPAWN_EGG);
        disguises.add(Material.BLAZE_SPAWN_EGG);
        disguises.add(Material.CAT_SPAWN_EGG);
        disguises.add(Material.CAVE_SPIDER_SPAWN_EGG);
        disguises.add(Material.CHICKEN_SPAWN_EGG);
        disguises.add(Material.COW_SPAWN_EGG);
        disguises.add(Material.CREEPER_SPAWN_EGG);
        disguises.add(Material.DOLPHIN_SPAWN_EGG);
        disguises.add(Material.DONKEY_SPAWN_EGG);
        disguises.add(Material.DROWNED_SPAWN_EGG);
        disguises.add(Material.ELDER_GUARDIAN_SPAWN_EGG);
        disguises.add(Material.ENDERMAN_SPAWN_EGG);
        disguises.add(Material.ENDERMITE_SPAWN_EGG);
        disguises.add(Material.EVOKER_SPAWN_EGG);
        disguises.add(Material.FOX_SPAWN_EGG);
        disguises.add(Material.GUARDIAN_SPAWN_EGG);
        disguises.add(Material.HORSE_SPAWN_EGG);
        disguises.add(Material.HUSK_SPAWN_EGG);
        disguises.add(Material.LLAMA_SPAWN_EGG);
        disguises.add(Material.MAGMA_CUBE_SPAWN_EGG);
        disguises.add(Material.MOOSHROOM_SPAWN_EGG);
        disguises.add(Material.MULE_SPAWN_EGG);
        disguises.add(Material.OCELOT_SPAWN_EGG);
        disguises.add(Material.PANDA_SPAWN_EGG);
        disguises.add(Material.PARROT_SPAWN_EGG);
        disguises.add(Material.PIG_SPAWN_EGG);
        disguises.add(Material.POLAR_BEAR_SPAWN_EGG);
        disguises.add(Material.RABBIT_SPAWN_EGG);
        disguises.add(Material.SHEEP_SPAWN_EGG);
        disguises.add(Material.SHULKER_SPAWN_EGG);
        disguises.add(Material.SILVERFISH_SPAWN_EGG);
        disguises.add(Material.SKELETON_SPAWN_EGG);
        disguises.add(Material.SLIME_SPAWN_EGG);
        disguises.add(Material.SPIDER_SPAWN_EGG);
        disguises.add(Material.SQUID_SPAWN_EGG);
        disguises.add(Material.STRAY_SPAWN_EGG);
        disguises.add(Material.TURTLE_SPAWN_EGG);
        disguises.add(Material.VEX_SPAWN_EGG);
        disguises.add(Material.VILLAGER_SPAWN_EGG);
        disguises.add(Material.WITCH_SPAWN_EGG);
        disguises.add(Material.WITHER_SKELETON_SPAWN_EGG);
        disguises.add(Material.WOLF_SPAWN_EGG);
        disguises.add(Material.ZOMBIE_PIGMAN_SPAWN_EGG);
        disguises.add(Material.ZOMBIE_SPAWN_EGG);
        disguises.add(Material.ZOMBIE_VILLAGER_SPAWN_EGG);
        terminal = getItemStack();
    }

    /**
     * Constructs an inventory for the Temporal Locator GUI.
     *
     * @return an Array of itemStacks (an inventory)
     */
    private ItemStack[] getItemStack() {
        ItemStack[] eggs = new ItemStack[54];
        int i = 0;
        for (Material m : disguises) {
            ItemStack egg = new ItemStack(m, 1);
            ItemMeta me = egg.getItemMeta();
            switch (m) {
                case MOOSHROOM_SPAWN_EGG:
                    me.setDisplayName("MUSHROOM_COW");
                    break;
                case ZOMBIE_PIGMAN_SPAWN_EGG:
                    me.setDisplayName("PIG_ZOMBIE");
                    break;
                default:
                    me.setDisplayName(m.toString().replace("_SPAWN_EGG", ""));
                    break;
            }
            egg.setItemMeta(me);
            eggs[i] = egg;
            i++;
        }
        // add options
        ItemStack the = new ItemStack(Material.COMPARATOR, 1);
        ItemMeta master = the.getItemMeta();
        master.setDisplayName(plugin.getLanguage().getString("BUTTON_MASTER"));
        master.setLore(Collections.singletonList(plugin.getLanguage().getString("SET_OFF")));
        the.setItemMeta(master);
        eggs[45] = the;
        ItemStack adult = new ItemStack(Material.HOPPER, 1);
        ItemMeta baby = adult.getItemMeta();
        baby.setDisplayName(plugin.getLanguage().getString("BUTTON_AGE"));
        baby.setLore(Collections.singletonList("ADULT"));
        adult.setItemMeta(baby);
        eggs[47] = adult;
        ItemStack typ = new ItemStack(Material.CYAN_DYE, 1);
        ItemMeta col = typ.getItemMeta();
        col.setDisplayName(plugin.getLanguage().getString("BUTTON_TYPE"));
        col.setLore(Collections.singletonList("WHITE"));
        typ.setItemMeta(col);
        eggs[48] = typ;
        ItemStack tamed = new ItemStack(Material.LEAD, 1);
        ItemMeta tf = tamed.getItemMeta();
        tf.setDisplayName(plugin.getLanguage().getString("BUTTON_OPTS"));
        List<String> opts = new ArrayList<>();
        for (String o : plugin.getLanguage().getString("BUTTON_OPTS_LIST").split("/")) {
            opts.add(ChatColor.ITALIC + o + ChatColor.RESET);
        }
        opts.add(ChatColor.RED + "FALSE");
        tf.setLore(opts);
        tamed.setItemMeta(tf);
        eggs[49] = tamed;
        // add buttons
        ItemStack rem = new ItemStack(Material.APPLE, 1);
        ItemMeta ove = rem.getItemMeta();
        ove.setDisplayName(plugin.getLanguage().getString("BUTTON_RESTORE"));
        rem.setItemMeta(ove);
        eggs[51] = rem;
        // set
        ItemStack s = new ItemStack(Material.WRITABLE_BOOK, 1);
        ItemMeta sim = s.getItemMeta();
        sim.setDisplayName(plugin.getLanguage().getString("BUTTON_DNA"));
        s.setItemMeta(sim);
        eggs[52] = s;
        ItemStack can = new ItemStack(Material.BOWL, 1);
        ItemMeta cel = can.getItemMeta();
        cel.setDisplayName(plugin.getLanguage().getString("BUTTON_CANCEL"));
        can.setItemMeta(cel);
        eggs[53] = can;

        return eggs;
    }

    public ItemStack[] getTerminal() {
        return terminal;
    }
}
