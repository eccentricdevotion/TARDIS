/*
 * Copyright (C) 2021 eccentric_nz
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
package me.eccentric_nz.tardis.chameleon;

import me.eccentric_nz.tardis.TARDISPlugin;
import me.eccentric_nz.tardis.custommodeldata.GUIChameleon;
import me.eccentric_nz.tardis.enumeration.Adaption;
import me.eccentric_nz.tardis.enumeration.PRESET;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

/**
 * A tardis with a functioning chameleon circuit can appear as almost anything desired. The owner can program the
 * circuit to make it assume a specific shape. If no appearance is specified, the tardis automatically choses its own
 * shape. When a tardis materialises in a new location, within the first nanosecond of landing, its chameleon circuit
 * analyses the surrounding area, calculates a twelve-dimensional data map of all objects within a thousand-mile radius
 * and then determines which outer shell will best blend in with the environment. According to the Eleventh Doctor, the
 * tardis would perform these functions, but then disguise itself as a 1960s era police box anyway.
 *
 * @author eccentric_nz
 */
public class TARDISChameleonInventory {

	private final ItemStack[] terminal;
	private final TARDISPlugin plugin;
	private final Adaption adapt;
	private final PRESET preset;
	private final ItemStack on;
	private final ItemStack off;

	public TARDISChameleonInventory(TARDISPlugin plugin, Adaption adapt, PRESET preset) {
		this.plugin = plugin;
		this.adapt = adapt;
		this.preset = preset;
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
		ItemStack apply = new ItemStack(Material.COMPARATOR, 1);
		ItemMeta now = apply.getItemMeta();
		assert now != null;
		now.setDisplayName(plugin.getChameleonGuis().getString("APPLY"));
		now.setLore(plugin.getChameleonGuis().getStringList("APPLY_LORE"));
		now.setCustomModelData(GUIChameleon.BUTTON_APPLY.getCustomModelData());
		apply.setItemMeta(now);
		// Disabled
		ItemStack dis = new ItemStack(Material.BOWL, 1);
		ItemMeta abled = dis.getItemMeta();
		assert abled != null;
		abled.setDisplayName("Chameleon Circuit");
		abled.setLore(plugin.getChameleonGuis().getStringList("DISABLED_LORE"));
		abled.setCustomModelData(GUIChameleon.BUTTON_CHAMELEON.getCustomModelData());
		dis.setItemMeta(abled);
		// Adaptive
		ItemStack adap = new ItemStack(Material.BOWL, 1);
		ItemMeta tive = adap.getItemMeta();
		assert tive != null;
		tive.setDisplayName(plugin.getChameleonGuis().getString("ADAPT"));
		tive.setLore(plugin.getChameleonGuis().getStringList("ADAPT_LORE"));
		tive.setCustomModelData(GUIChameleon.BUTTON_ADAPT.getCustomModelData());
		adap.setItemMeta(tive);
		// Invisible
		ItemStack invis;
		if (plugin.getConfig().getBoolean("allow.invisibility")) {
			invis = new ItemStack(Material.BOWL, 1);
			ItemMeta ible = invis.getItemMeta();
			assert ible != null;
			ible.setDisplayName(plugin.getChameleonGuis().getString("INVISIBLE"));
			List<String> ilore = plugin.getChameleonGuis().getStringList("INVISIBLE_LORE");
			if (plugin.getConfig().getBoolean("circuits.damage")) {
				ilore.add(plugin.getLanguage().getString("INVISIBILITY_LORE_1"));
				ilore.add(plugin.getLanguage().getString("INVISIBILITY_LORE_2"));
			}
			ible.setLore(ilore);
			ible.setCustomModelData(GUIChameleon.BUTTON_INVISIBLE.getCustomModelData());
			invis.setItemMeta(ible);
		} else {
			invis = null;
		}
		// Shorted out
		ItemStack shor = new ItemStack(Material.BOWL, 1);
		ItemMeta tout = shor.getItemMeta();
		assert tout != null;
		tout.setDisplayName(plugin.getChameleonGuis().getString("SHORT"));
		tout.setLore(plugin.getChameleonGuis().getStringList("SHORT_LORE"));
		tout.setCustomModelData(GUIChameleon.BUTTON_SHORT.getCustomModelData());
		shor.setItemMeta(tout);
		// construction GUI
		ItemStack cons = new ItemStack(Material.BOWL, 1);
		ItemMeta truct = cons.getItemMeta();
		assert truct != null;
		truct.setDisplayName(plugin.getChameleonGuis().getString("CONSTRUCT"));
		truct.setLore(plugin.getChameleonGuis().getStringList("CONSTRUCT_LORE"));
		truct.setCustomModelData(GUIChameleon.BUTTON_CONSTRUCT.getCustomModelData());
		cons.setItemMeta(truct);
		// Disabled radio button
		boolean isFactoryOff = preset.equals(PRESET.FACTORY) && adapt.equals(Adaption.OFF);
		ItemStack fac = isFactoryOff ? on.clone() : off.clone();
		ItemMeta tory = fac.getItemMeta();
		String donoff = isFactoryOff ? ChatColor.RED + plugin.getLanguage().getString("DISABLED") : ChatColor.GREEN + plugin.getLanguage().getString("SET_ON");
		assert tory != null;
		tory.setDisplayName(donoff);
		fac.setItemMeta(tory);
		// Adaptive radio button
		ItemStack biome = (adapt.equals(Adaption.OFF)) ? off.clone() : on.clone();
		ItemMeta block = biome.getItemMeta();
		assert block != null;
		block.setDisplayName(adapt.getColour() + adapt.toString());
		biome.setItemMeta(block);
		// Invisible radio button
		ItemStack not;
		if (plugin.getConfig().getBoolean("allow.invisibility")) {
			not = (preset.equals(PRESET.INVISIBLE)) ? on.clone() : off.clone();
			ItemMeta blue = not.getItemMeta();
			String ionoff = (preset.equals(PRESET.INVISIBLE)) ? ChatColor.GREEN + plugin.getLanguage().getString("SET_ON") : ChatColor.RED + plugin.getLanguage().getString("SET_OFF");
			assert blue != null;
			blue.setDisplayName(ionoff);
			not.setItemMeta(blue);
		} else {
			not = null;
		}
		// Shorted out radio button
		boolean isNotFactoryInvisibleOrConstruct = !preset.equals(PRESET.INVISIBLE) && !preset.equals(PRESET.FACTORY) && !preset.equals(PRESET.CONSTRUCT);
		ItemStack pre = isNotFactoryInvisibleOrConstruct ? on.clone() : off.clone();
		ItemMeta set = pre.getItemMeta();
		String sonoff = isNotFactoryInvisibleOrConstruct ? ChatColor.GREEN + preset.toString() : ChatColor.RED + plugin.getLanguage().getString("SET_OFF");
		assert set != null;
		set.setDisplayName(sonoff);
		pre.setItemMeta(set);
		// Construct radio button
		ItemStack bui = (preset.equals(PRESET.CONSTRUCT)) ? on.clone() : off.clone();
		ItemMeta lder = bui.getItemMeta();
		String conoff = (preset.equals(PRESET.CONSTRUCT)) ? ChatColor.GREEN + plugin.getLanguage().getString("SET_ON") : ChatColor.RED + plugin.getLanguage().getString("SET_OFF");
		assert lder != null;
		lder.setDisplayName(conoff);
		bui.setItemMeta(lder);
		// Cancel / close
		ItemStack close = new ItemStack(Material.BOWL, 1);
		ItemMeta can = close.getItemMeta();
		assert can != null;
		can.setDisplayName(plugin.getLanguage().getString("BUTTON_CLOSE"));
		can.setCustomModelData(GUIChameleon.BUTTON_CLOSE.getCustomModelData());
		close.setItemMeta(can);

		return new ItemStack[]{apply, null, null, null, null, null, null, null, null, null, null, dis, adap, invis, shor, cons, null, null, null, null, fac, biome, not, pre, bui, null, close};
	}

	public ItemStack[] getMenu() {
		return terminal;
	}
}
