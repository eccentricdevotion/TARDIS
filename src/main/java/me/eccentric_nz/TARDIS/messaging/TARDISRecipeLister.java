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

import java.util.List;

/**
 * @author eccentric_nz
 */
public class TARDISRecipeLister {

	private final TARDIS plugin;
	private final CommandSender sender;
	private final TableGenerator tg;

	public TARDISRecipeLister(TARDIS plugin, CommandSender sender) {
		this.plugin = plugin;
		this.sender = sender;
		if (TableGenerator.getSenderPrefs(sender)) {
			tg = new TableGeneratorCustomFont(TableGenerator.Alignment.LEFT, TableGenerator.Alignment.LEFT);
		} else {
			tg = new TableGeneratorSmallChar(TableGenerator.Alignment.LEFT, TableGenerator.Alignment.LEFT);
		}
	}

	public void list() {
		TARDISMessage.send(sender, "RECIPE_VIEW");
		for (String line : createRecipeOptions()) {
			sender.sendMessage(line);
		}
	}

	private List<String> createRecipeOptions() {
		tg.addRow(ChatColor.GRAY + "" + ChatColor.UNDERLINE + "Command argument", ChatColor.DARK_GRAY + "" + ChatColor.UNDERLINE + "Recipe Result");
		tg.addRow();
		for (RecipeCategory category : RecipeCategory.values()) {
			if (category != RecipeCategory.UNUSED && category != RecipeCategory.UNCRAFTABLE) {
				tg.addRow(category.getName(), "");
				for (RecipeItem item : RecipeItem.values()) {
					if (item.getCategory() == category) {
						tg.addRow(category.getKeyColour() + item.toTabCompletionString(), category.getValueColour() + item.toRecipeString());
					}
				}
				tg.addRow();
			}
		}
		return tg.generate(TableGeneratorSmallChar.Receiver.CLIENT, true, true);
	}
}
