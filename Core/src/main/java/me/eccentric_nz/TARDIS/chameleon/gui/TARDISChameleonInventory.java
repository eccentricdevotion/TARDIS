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
package me.eccentric_nz.TARDIS.chameleon.gui;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.custommodels.GUIChameleon;
import me.eccentric_nz.TARDIS.enumeration.Adaption;
import me.eccentric_nz.TARDIS.enumeration.ChameleonPreset;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

/**
 * A TARDIS with a functioning chameleon circuit can appear as almost anything
 * desired. The owner can program the circuit to make it assume a specific
 * shape. If no appearance is specified, the TARDIS automatically choses its own
 * shape. When a TARDIS materialises in a new location, within the first
 * nanosecond of landing, its chameleon circuit analyses the surrounding area,
 * calculates a twelve-dimensional data map of all objects within a
 * thousand-mile radius and then determines which outer shell will best blend in
 * with the environment. According to the Eleventh Doctor, the TARDIS would
 * perform these functions, but then disguise itself as a 1960s era police box
 * anyway.
 *
 * @author eccentric_nz
 */
public class TARDISChameleonInventory {

    private final ItemStack[] terminal;
    private final TARDIS plugin;
    private final Adaption adapt;
    private final ChameleonPreset preset;
    private final String model;
    private final ItemStack on;
    private final ItemStack off;

    public TARDISChameleonInventory(TARDIS plugin, Adaption adapt, ChameleonPreset preset, String model) {
        this.plugin = plugin;
        this.adapt = adapt;
        this.preset = preset;
        this.model = model;
        on = new ItemStack(Material.LIME_WOOL, 1);
        off = new ItemStack(Material.LIGHT_GRAY_CARPET, 1);
        terminal = getItemStack();
    }

    /**
     * Constructs an inventory for the Chameleon Circuit GUI.
     *
     * @return an Array of itemStacks (an inventory)
     */
    private ItemStack[] getItemStack() {

        // Apply now
        ItemStack apply = new ItemStack(GUIChameleon.BUTTON_APPLY.material(), 1);
        ItemMeta now = apply.getItemMeta();
        now.setDisplayName(plugin.getChameleonGuis().getString("APPLY"));
        now.setLore(plugin.getChameleonGuis().getStringList("APPLY_LORE"));
        apply.setItemMeta(now);
        // Disabled
        ItemStack dis = new ItemStack(GUIChameleon.BUTTON_CHAMELEON.material(), 1);
        ItemMeta abled = dis.getItemMeta();
        abled.setDisplayName("Chameleon Circuit");
        abled.setLore(plugin.getChameleonGuis().getStringList("DISABLED_LORE"));
        dis.setItemMeta(abled);
        // Adaptive
        ItemStack adap = new ItemStack(GUIChameleon.BUTTON_ADAPT.material(), 1);
        ItemMeta tive = adap.getItemMeta();
        tive.setDisplayName(plugin.getChameleonGuis().getString("ADAPT"));
        tive.setLore(plugin.getChameleonGuis().getStringList("ADAPT_LORE"));
        adap.setItemMeta(tive);
        // Invisible
        ItemStack invis;
        if (plugin.getConfig().getBoolean("allow.invisibility")) {
            invis = new ItemStack(GUIChameleon.BUTTON_INVISIBLE.material(), 1);
            ItemMeta ible = invis.getItemMeta();
            ible.setDisplayName(plugin.getChameleonGuis().getString("INVISIBLE"));
            List<String> ilore = plugin.getChameleonGuis().getStringList("INVISIBLE_LORE");
            if (plugin.getConfig().getBoolean("circuits.damage")) {
                ilore.add(plugin.getLanguage().getString("INVISIBILITY_LORE_1"));
                ilore.add(plugin.getLanguage().getString("INVISIBILITY_LORE_2"));
            }
            ible.setLore(ilore);
            invis.setItemMeta(ible);
        } else {
            invis = null;
        }
        // Shorted out
        ItemStack shor = new ItemStack(GUIChameleon.BUTTON_SHORT.material(), 1);
        ItemMeta tout = shor.getItemMeta();
        tout.setDisplayName(plugin.getChameleonGuis().getString("SHORT"));
        tout.setLore(plugin.getChameleonGuis().getStringList("SHORT_LORE"));
        shor.setItemMeta(tout);
        // construction GUI
        ItemStack cons = new ItemStack(GUIChameleon.BUTTON_CONSTRUCT.material(), 1);
        ItemMeta truct = cons.getItemMeta();
        truct.setDisplayName(plugin.getChameleonGuis().getString("CONSTRUCT"));
        truct.setLore(plugin.getChameleonGuis().getStringList("CONSTRUCT_LORE"));
        cons.setItemMeta(truct);
        // lock current preset
        ItemStack lock = null;
        if (adapt.equals(Adaption.BIOME)) {
            lock = new ItemStack(GUIChameleon.BUTTON_LOCK.material(), 1);
            ItemMeta circuit = lock.getItemMeta();
            circuit.setDisplayName(plugin.getChameleonGuis().getString("LOCK"));
            circuit.setLore(plugin.getChameleonGuis().getStringList("LOCK_LORE"));
            lock.setItemMeta(circuit);
        }
        // Disabled radio button
        boolean isFactoryOff = preset.equals(ChameleonPreset.FACTORY) && adapt.equals(Adaption.OFF);
        ItemStack fac = isFactoryOff ? on.clone() : off.clone();
        ItemMeta tory = fac.getItemMeta();
        String donoff = isFactoryOff ? ChatColor.RED + plugin.getLanguage().getString("DISABLED") : ChatColor.GREEN + plugin.getLanguage().getString("SET_ON");
        tory.setDisplayName(donoff);
        fac.setItemMeta(tory);
        // Adaptive radio button
        ItemStack biome = (adapt.equals(Adaption.OFF)) ? off.clone() : on.clone();
        ItemMeta block = biome.getItemMeta();
        block.setDisplayName(adapt.getColour() + adapt.toString());
        biome.setItemMeta(block);
        // Invisible radio button
        ItemStack not;
        if (plugin.getConfig().getBoolean("allow.invisibility")) {
            not = (preset.equals(ChameleonPreset.INVISIBLE)) ? on.clone() : off.clone();
            ItemMeta blue = not.getItemMeta();
            String ionoff = (preset.equals(ChameleonPreset.INVISIBLE)) ? ChatColor.GREEN + plugin.getLanguage().getString("SET_ON") : ChatColor.RED + plugin.getLanguage().getString("SET_OFF");
            blue.setDisplayName(ionoff);
            not.setItemMeta(blue);
        } else {
            not = null;
        }
        // Shorted out radio button
        boolean isNotFactoryInvisibleOrConstruct = !preset.equals(ChameleonPreset.INVISIBLE) && !preset.equals(ChameleonPreset.FACTORY) && !preset.equals(ChameleonPreset.CONSTRUCT);
        ItemStack pre = isNotFactoryInvisibleOrConstruct ? on.clone() : off.clone();
        ItemMeta set = pre.getItemMeta();
        String shorted;
        if (isNotFactoryInvisibleOrConstruct) {
            shorted = ChatColor.GREEN + ((preset == ChameleonPreset.ITEM) ? model : preset.toString());
        } else {
            shorted = ChatColor.RED + plugin.getLanguage().getString("SET_OFF");
        }
        set.setDisplayName(shorted);
        pre.setItemMeta(set);
        // Construct radio button
        ItemStack bui = (preset.equals(ChameleonPreset.CONSTRUCT)) ? on.clone() : off.clone();
        ItemMeta lder = bui.getItemMeta();
        String conoff = (preset.equals(ChameleonPreset.CONSTRUCT)) ? ChatColor.GREEN + plugin.getLanguage().getString("SET_ON") : ChatColor.RED + plugin.getLanguage().getString("SET_OFF");
        lder.setDisplayName(conoff);
        bui.setItemMeta(lder);
        // Cancel / close
        ItemStack close = new ItemStack(GUIChameleon.BUTTON_CLOSE.material(), 1);
        ItemMeta can = close.getItemMeta();
        can.setDisplayName(plugin.getLanguage().getString("BUTTON_CLOSE"));
        close.setItemMeta(can);

        return new ItemStack[]{apply, null, null, lock, null, null, null, null, null, null, null, dis, adap, invis, shor, cons, null, null, null, null, fac, biome, not, pre, bui, null, close};
    }

    public ItemStack[] getMenu() {
        return terminal;
    }
}
