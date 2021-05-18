package me.eccentric_nz.TARDIS.recipes;

import me.eccentric_nz.TARDIS.custommodeldata.GUIChemistry;
import me.eccentric_nz.TARDIS.enumeration.RecipeCategory;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class TARDISRecipeCategoryInventory {

	private final ItemStack[] menu;

	public TARDISRecipeCategoryInventory() {
		menu = getItemStack();
	}

	private ItemStack[] getItemStack() {
		ItemStack[] stack = new ItemStack[27];
		// info
		ItemStack info = new ItemStack(Material.BOWL, 1);
		ItemMeta info_im = info.getItemMeta();
		info_im.setDisplayName("Info");
		info_im.setLore(Arrays.asList("Click a button below", "to see the items", "in that recipe category"));
		info_im.setCustomModelData(GUIChemistry.INFO.getCustomModelData());
		info.setItemMeta(info_im);
		stack[4] = info;
		for (RecipeCategory category : RecipeCategory.values()) {
			if (!category.equals(RecipeCategory.UNUSED) && category != RecipeCategory.UNCRAFTABLE) {
				ItemStack cat = new ItemStack(category.getMaterial(), 1);
				ItemMeta egory = cat.getItemMeta();
				egory.setDisplayName(category.getName());
				egory.setCustomModelData(category.getCustomModelData());
				egory.addItemFlags(ItemFlag.values());
				cat.setItemMeta(egory);
				stack[category.getSlot()] = cat;
			}
		}
		return stack;
	}

	public ItemStack[] getMenu() {
		return menu;
	}
}
