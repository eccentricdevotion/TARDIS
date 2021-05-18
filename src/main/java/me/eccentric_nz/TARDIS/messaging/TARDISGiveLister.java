/*
 * Copyright (C) 2020 eccentric_nz
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
package me.eccentric_nz.TARDIS.messaging;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.RecipeCategory;
import me.eccentric_nz.TARDIS.enumeration.RecipeItem;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * @author eccentric_nz
 */
public class TARDISGiveLister {

	private final TARDIS plugin;
	private final CommandSender sender;
	private final TableGenerator tg;

	public TARDISGiveLister(TARDIS plugin, CommandSender sender) {
		this.plugin = plugin;
		this.sender = sender;
		if (TableGenerator.getSenderPrefs(sender)) {
			tg = new TableGeneratorCustomFont(TableGenerator.Alignment.LEFT, TableGenerator.Alignment.LEFT);
		} else {
			tg = new TableGeneratorSmallChar(TableGenerator.Alignment.LEFT, TableGenerator.Alignment.LEFT);
		}
	}

	public void list() {
		sender.sendMessage(plugin.getPluginName() + "You can 'give' the following items:");
		for (String line : createGiveOptions()) {
			sender.sendMessage(line);
		}
	}

	private List<String> createGiveOptions() {
		tg.addRow(ChatColor.GRAY + "Command argument", ChatColor.DARK_GRAY + "Description");
		tg.addRow();
		tg.addRow("Admin & development", "");
		tg.addRow(ChatColor.YELLOW + "artron", ChatColor.GOLD + "Artron Energy");
		tg.addRow(ChatColor.YELLOW + "blueprint", ChatColor.GOLD + "TARDIS Blueprint Disk");
		tg.addRow(ChatColor.YELLOW + "kit", ChatColor.GOLD + "TARDIS Item Kit");
		tg.addRow(ChatColor.YELLOW + "recipes", ChatColor.GOLD + "Grant TARDIS recipes");
		tg.addRow(ChatColor.YELLOW + "seed", ChatColor.GOLD + "TARDIS Seed Block");
		tg.addRow(ChatColor.YELLOW + "mushroom", ChatColor.GOLD + "Textured mushroom blocks");
		tg.addRow();
		for (RecipeCategory category : RecipeCategory.values()) {
			if (category != RecipeCategory.UNUSED && category != RecipeCategory.UNCRAFTABLE) {
				tg.addRow(category.getName(), "");
				for (RecipeItem item : RecipeItem.values()) {
					if (item.getCategory() == category) {
						tg.addRow(category.getKeyColour() + item.toTabCompletionString(), category.getValueColour() + item.toRecipeString());
					}
				}
			}
			tg.addRow();
		}
		return tg.generate(sender instanceof Player ? TableGeneratorSmallChar.Receiver.CLIENT : TableGeneratorSmallChar.Receiver.CONSOLE, true, true);
	}
}
