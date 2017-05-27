/*
 * Copyright (C) 2016 eccentric_nz
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

import java.util.Arrays;
import java.util.LinkedHashMap;
import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * The Genetic Manipulation Device was invented by Professor Richard Lazarus.
 * The machine would turn anyone inside decades younger, but the process
 * contained one side effect: genes that evolution rejected and left dormant
 * would be unlocked, transforming the human into a giant skeletal scorpion-like
 * beast that fed off the lifeforce of living creatures.
 *
 * @author eccentric_nz
 */
public class TARDISLazarusTWAInventory {

    private final ItemStack[] terminal;
    private final TARDIS plugin;
    LinkedHashMap<String, EntityType> disguises = new LinkedHashMap<>();

    public TARDISLazarusTWAInventory(TARDIS plugin) {
        this.plugin = plugin;
        this.terminal = getItemStack();
    }

    /**
     * Constructs an inventory for the Temporal Locator GUI.
     *
     * @return an Array of itemStacks (an inventory)
     */
    private ItemStack[] getItemStack() {
        ItemStack[] eggs = new ItemStack[54];
        int i = 0;
        // if TARDISWeepingAngels is enabled angels, cybermen and ice warriors will be available
        if (plugin.checkTWA()) {
            ItemStack weep = new ItemStack(Material.CLAY_BRICK, 1);
            ItemMeta ing = weep.getItemMeta();
            ing.setDisplayName("WEEPING ANGEL");
            weep.setItemMeta(ing);
            eggs[i] = weep;
            i++;
            ItemStack cyber = new ItemStack(Material.IRON_INGOT, 1);
            ItemMeta men = cyber.getItemMeta();
            men.setDisplayName("CYBERMAN");
            cyber.setItemMeta(men);
            eggs[i] = cyber;
            i++;
            ItemStack ice = new ItemStack(Material.SNOW_BALL, 1);
            ItemMeta war = ice.getItemMeta();
            war.setDisplayName("ICE WARRIOR");
            ice.setItemMeta(war);
            eggs[i] = ice;
            i++;
            ItemStack emp = new ItemStack(Material.SUGAR, 1);
            ItemMeta tyc = emp.getItemMeta();
            tyc.setDisplayName("EMPTY CHILD");
            emp.setItemMeta(tyc);
            eggs[i] = emp;
            i++;
            ItemStack sil = new ItemStack(Material.FEATHER, 1);
            ItemMeta uri = sil.getItemMeta();
            uri.setDisplayName("SILURIAN");
            sil.setItemMeta(uri);
            eggs[i] = sil;
            i++;
            ItemStack son = new ItemStack(Material.POTATO_ITEM, 1);
            ItemMeta tar = son.getItemMeta();
            tar.setDisplayName("SONTARAN");
            son.setItemMeta(tar);
            eggs[i] = son;
            i++;
            ItemStack str = new ItemStack(Material.BAKED_POTATO, 1);
            ItemMeta axs = str.getItemMeta();
            axs.setDisplayName("STRAX");
            str.setItemMeta(axs);
            eggs[i] = str;
            i++;
            ItemStack vas = new ItemStack(Material.BOOK, 1);
            ItemMeta hta = vas.getItemMeta();
            hta.setDisplayName("VASHTA NERADA");
            vas.setItemMeta(hta);
            eggs[i] = vas;
            i++;
            ItemStack zyg = new ItemStack(Material.PAINTING, 1);
            ItemMeta onz = zyg.getItemMeta();
            onz.setDisplayName("ZYGON");
            zyg.setItemMeta(onz);
            eggs[i] = zyg;
        }
        // add options
        ItemStack the = new ItemStack(Material.REDSTONE_COMPARATOR, 1);
        ItemMeta master = the.getItemMeta();
        master.setDisplayName(plugin.getLanguage().getString("BUTTON_MASTER"));
        master.setLore(Arrays.asList(plugin.getLanguage().getString("SET_OFF")));
        the.setItemMeta(master);
        eggs[45] = the;
        ItemStack adult = new ItemStack(Material.HOPPER, 1);
        ItemMeta baby = adult.getItemMeta();
        baby.setDisplayName(plugin.getLanguage().getString("BUTTON_AGE"));
        baby.setLore(Arrays.asList("ADULT"));
        adult.setItemMeta(baby);
        eggs[47] = adult;
        ItemStack typ = new ItemStack(Material.INK_SACK, 1, (byte) 6);
        ItemMeta col = typ.getItemMeta();
        col.setDisplayName(plugin.getLanguage().getString("BUTTON_TYPE"));
        col.setLore(Arrays.asList("WHITE"));
        typ.setItemMeta(col);
        eggs[48] = typ;
        ItemStack tamed = new ItemStack(Material.LEASH, 1);
        ItemMeta tf = tamed.getItemMeta();
        tf.setDisplayName(plugin.getLanguage().getString("BUTTON_OPTS"));
        tf.setLore(Arrays.asList("FALSE"));
        tamed.setItemMeta(tf);
        eggs[49] = tamed;
        // add buttons
        ItemStack rem = new ItemStack(Material.APPLE, 1);
        ItemMeta ove = rem.getItemMeta();
        ove.setDisplayName(plugin.getLanguage().getString("BUTTON_RESTORE"));
        rem.setItemMeta(ove);
        eggs[51] = rem;
        // set
        ItemStack s = new ItemStack(Material.BOOK_AND_QUILL, 1);
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
