package me.eccentric_nz.tardis.enumeration;

import me.eccentric_nz.tardis.utility.TARDISStringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;

public enum RecipeCategory {

	ACCESSORIES(Material.LEATHER_HELMET, 10000037, 9, ChatColor.GREEN, ChatColor.DARK_GREEN), CONSOLE_CIRCUITS(Material.GLOWSTONE_DUST, 10001977, 11, ChatColor.LIGHT_PURPLE, ChatColor.DARK_PURPLE), FOOD(Material.MELON_SLICE, 10000002, 13, ChatColor.GRAY, ChatColor.DARK_GRAY), ITEM_CIRCUITS(Material.GLOWSTONE_DUST, 10001967, 15, ChatColor.RED, ChatColor.DARK_RED), ITEMS(Material.GOLD_NUGGET, 12, 17, ChatColor.GREEN, ChatColor.DARK_GREEN), ROTORS(Material.LIGHT_GRAY_DYE, 10000002, 18, ChatColor.GOLD, ChatColor.YELLOW), SONIC_CIRCUITS(Material.GLOWSTONE_DUST, 10001971, 20, ChatColor.BLUE, ChatColor.DARK_BLUE), SONIC_UPGRADES(Material.BLAZE_ROD, 10000009, 22, ChatColor.LIGHT_PURPLE, ChatColor.DARK_PURPLE), STORAGE_DISKS(Material.MUSIC_DISC_STRAD, 10000001, 24, ChatColor.AQUA, ChatColor.DARK_AQUA), MISC(Material.WATER_BUCKET, 1, 26, ChatColor.GRAY, ChatColor.DARK_GRAY), UNCRAFTABLE(Material.CRAFTING_TABLE, 1, -1, ChatColor.GRAY, ChatColor.DARK_GRAY), UNUSED(Material.STONE, 1, -1, ChatColor.RED, ChatColor.DARK_RED);

	private final Material material;
	private final int customModelData;
	private final int slot;
	private final ChatColor keyColour;
	private final ChatColor valueColour;

	RecipeCategory(Material material, int customModelData, int slot, ChatColor keyColour, ChatColor valueColour) {
		this.material = material;
		this.customModelData = customModelData;
		this.slot = slot;
		this.keyColour = keyColour;
		this.valueColour = valueColour;
	}

	public String getName() {
		String s = toString();
		return TARDISStringUtils.sentenceCase(s);
	}

	public Material getMaterial() {
		return material;
	}

	public int getCustomModelData() {
		return customModelData;
	}

	public int getSlot() {
		return slot;
	}

	public ChatColor getKeyColour() {
		return keyColour;
	}

	public ChatColor getValueColour() {
		return valueColour;
	}
}
