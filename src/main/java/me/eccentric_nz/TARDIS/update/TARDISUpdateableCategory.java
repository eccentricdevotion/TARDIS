package me.eccentric_nz.tardis.update;

import org.bukkit.ChatColor;

public enum TARDISUpdateableCategory {

	CONTROLS(ChatColor.GREEN, ChatColor.DARK_GREEN, "tardis Controls"), INTERFACES(ChatColor.RED, ChatColor.DARK_RED, "User Interfaces"), LOCATIONS(ChatColor.YELLOW, ChatColor.GOLD, "Spawn Locations"), OTHERS(ChatColor.LIGHT_PURPLE, ChatColor.DARK_PURPLE, "Others");

	private final ChatColor keyColour;
	private final ChatColor valueColour;
	private final String name;

	TARDISUpdateableCategory(ChatColor keyColour, ChatColor valueColour, String name) {
		this.keyColour = keyColour;
		this.valueColour = valueColour;
		this.name = name;
	}

	public ChatColor getKeyColour() {
		return keyColour;
	}

	public ChatColor getValueColour() {
		return valueColour;
	}

	public String getName() {
		return name;
	}
}
