package me.eccentric_nz.tardis.sonic;

import me.eccentric_nz.tardis.custommodeldata.GUISonicConfigurator;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class TARDISSonicConfiguratorInventory {

	private final ItemStack[] configurator;

	public TARDISSonicConfiguratorInventory() {
		configurator = getItemStack();
	}

	/**
	 * Constructs an inventory for the Sonic Configurator Menu GUI.
	 *
	 * @return an Array of itemStacks (an inventory)
	 */
	private ItemStack[] getItemStack() {

		ItemStack[] stack = new ItemStack[27];
		for (GUISonicConfigurator gui : GUISonicConfigurator.values()) {
			if (gui.getSlot() != -1) {
				ItemStack is = new ItemStack(gui.getMaterial(), 1);
				ItemMeta im = is.getItemMeta();
				im.setDisplayName(gui.getName());
				if (!gui.getLore().isEmpty()) {
					im.setLore(Arrays.asList(gui.getLore().split("~")));
				}
				im.setCustomModelData(gui.getCustomModelData());
				is.setItemMeta(im);
				stack[gui.getSlot()] = is;
			}
		}
		ItemStack place = new ItemStack(GUISonicConfigurator.PLACE_SONIC.getMaterial(), 1);
		ItemMeta pim = place.getItemMeta();
		pim.setDisplayName(GUISonicConfigurator.PLACE_SONIC.getName());
		pim.setCustomModelData(GUISonicConfigurator.PLACE_SONIC.getCustomModelData());
		place.setItemMeta(pim);
		stack[9] = place;
		ItemStack wool = new ItemStack(GUISonicConfigurator.WAITING.getMaterial(), 1);
		ItemMeta wim = wool.getItemMeta();
		wim.setDisplayName(" ");
		wim.setCustomModelData(GUISonicConfigurator.WAITING.getCustomModelData());
		wool.setItemMeta(wim);
		for (int i = 10; i < 17; i++) {
			stack[i] = wool;
		}
		return stack;
	}

	public ItemStack[] getConfigurator() {
		return configurator;
	}
}
