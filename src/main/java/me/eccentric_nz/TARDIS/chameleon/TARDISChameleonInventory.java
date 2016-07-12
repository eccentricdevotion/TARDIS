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
package me.eccentric_nz.TARDIS.chameleon;

import java.util.List;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.ADAPTION;
import me.eccentric_nz.TARDIS.enumeration.PRESET;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

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
    private final ADAPTION adapt;
    private final PRESET preset;
    private final ItemStack on;
    private final ItemStack off;

    public TARDISChameleonInventory(TARDIS plugin, ADAPTION adapt, PRESET preset) {
        this.plugin = plugin;
        this.adapt = adapt;
        this.preset = preset;
        this.on = new ItemStack(Material.WOOL, 1, (byte) 5);
        this.off = new ItemStack(Material.CARPET, 1, (byte) 8);
        this.terminal = getItemStack();
    }

    /**
     * Constructs an inventory for the Chameleon Circuit GUI.
     *
     * @return an Array of itemStacks (an inventory)
     */
    private ItemStack[] getItemStack() {

        // Apply now
        ItemStack apply = new ItemStack(Material.REDSTONE_COMPARATOR, 1);
        ItemMeta now = apply.getItemMeta();
        now.setDisplayName(plugin.getChameleonGuis().getString("APPLY"));
        now.setLore(plugin.getChameleonGuis().getStringList("APPLY_LORE"));
        apply.setItemMeta(now);
        // Disabled
        ItemStack dis = new ItemStack(Material.BOWL, 1);
        ItemMeta abled = dis.getItemMeta();
        abled.setDisplayName("Chameleon Circuit");
        abled.setLore(plugin.getChameleonGuis().getStringList("DISABLED_LORE"));
        dis.setItemMeta(abled);
        // Adaptive
        ItemStack adap = new ItemStack(Material.BOWL, 1);
        ItemMeta tive = adap.getItemMeta();
        tive.setDisplayName(plugin.getChameleonGuis().getString("ADAPT"));
        tive.setLore(plugin.getChameleonGuis().getStringList("ADAPT_LORE"));
        adap.setItemMeta(tive);
        // Invisible
        ItemStack invis;
        if (plugin.getConfig().getBoolean("allow.invisibility")) {
            invis = new ItemStack(Material.BOWL, 1);
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
        ItemStack shor = new ItemStack(Material.BOWL, 1);
        ItemMeta tout = shor.getItemMeta();
        tout.setDisplayName(plugin.getChameleonGuis().getString("SHORT"));
        tout.setLore(plugin.getChameleonGuis().getStringList("SHORT_LORE"));
        shor.setItemMeta(tout);
        // construction GUI
        ItemStack cons = new ItemStack(Material.BOWL, 1);
        ItemMeta truct = cons.getItemMeta();
        truct.setDisplayName(plugin.getChameleonGuis().getString("CONSTRUCT"));
        truct.setLore(plugin.getChameleonGuis().getStringList("CONSTRUCT_LORE"));
        cons.setItemMeta(truct);
        // Disabled radio button
        ItemStack fac = (preset.equals(PRESET.FACTORY) && adapt.equals(ADAPTION.OFF)) ? on : off;
        ItemMeta tory = fac.getItemMeta();
        String donoff = (preset.equals(PRESET.FACTORY) && adapt.equals(ADAPTION.OFF)) ? ChatColor.RED + plugin.getChameleonGuis().getString("DISABLED") : ChatColor.GREEN + plugin.getLanguage().getString("SET_ON");
        tory.setDisplayName(donoff);
        fac.setItemMeta(tory);
        // Adaptive radio button
        ItemStack biome = (adapt.equals(ADAPTION.OFF)) ? off : on;
        ItemMeta block = biome.getItemMeta();
        block.setDisplayName(adapt.getColour() + adapt.toString());
        biome.setItemMeta(block);
        // Invisible radio button
        ItemStack not;
        if (plugin.getConfig().getBoolean("allow.invisibility")) {
            not = (preset.equals(PRESET.INVISIBLE)) ? on : off;
            ItemMeta blue = not.getItemMeta();
            String ionoff = (preset.equals(PRESET.INVISIBLE)) ? ChatColor.GREEN + plugin.getLanguage().getString("SET_ON") : ChatColor.RED + plugin.getLanguage().getString("SET_OFF");
            blue.setDisplayName(ionoff);
            not.setItemMeta(blue);
        } else {
            not = null;
        }
        // Shorted out radio button
        ItemStack pre = (!preset.equals(PRESET.INVISIBLE) && !preset.equals(PRESET.FACTORY) && !preset.equals(PRESET.CONSTRUCT)) ? on : off;
        ItemMeta set = pre.getItemMeta();
        String sonoff = (!preset.equals(PRESET.INVISIBLE) && !preset.equals(PRESET.FACTORY) && !preset.equals(PRESET.CONSTRUCT)) ? ChatColor.GREEN + preset.toString() + " " + plugin.getLanguage().getString("SET_ON") : ChatColor.RED + plugin.getLanguage().getString("SET_OFF");
        set.setDisplayName(sonoff);
        pre.setItemMeta(set);
        // Construct radio button
        ItemStack bui = (preset.equals(PRESET.CONSTRUCT)) ? on : off;
        ItemMeta lder = bui.getItemMeta();
        String conoff = (preset.equals(PRESET.CONSTRUCT)) ? ChatColor.GREEN + plugin.getLanguage().getString("SET_ON") : ChatColor.RED + plugin.getLanguage().getString("SET_OFF");
        lder.setDisplayName(conoff);
        bui.setItemMeta(lder);
        // Cancel / close
        ItemStack close = new ItemStack(Material.BOWL, 1);
        ItemMeta can = close.getItemMeta();
        can.setDisplayName(plugin.getLanguage().getString("BUTTON_CLOSE"));
        close.setItemMeta(can);

        ItemStack[] is = {
            apply, null, null, null, null, null, null, null, null,
            null, null, dis, adap, invis, shor, cons, null, null,
            null, null, fac, biome, not, pre, bui, null, close
        };
        return is;
    }

    public ItemStack[] getMenu() {
        return terminal;
    }
}
