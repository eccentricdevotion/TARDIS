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
package me.eccentric_nz.TARDIS.commands.admin;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.messaging.TARDISMessage;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;

/**
 * @author eccentric_nz
 */
class TARDISCondenserCommand {

	private final TARDIS plugin;

	TARDISCondenserCommand(TARDIS plugin) {
		this.plugin = plugin;
	}

	public boolean set(CommandSender sender) {
		if (!(sender instanceof Player)) {
			TARDISMessage.send(sender, "CMD_PLAYER");
			return true;
		}
		Player player = (Player) sender;
		Block b = player.getTargetBlock(plugin.getGeneralKeeper().getTransparent(), 10);
		Material chest = b.getType();
		if (!chest.equals(Material.CHEST)) {
			TARDISMessage.send(sender, "UPDATE_CONDENSER");
			return true;
		}
		String loc = b.getLocation().toString();
		// update the artron config
		plugin.getArtronConfig().set("condenser", loc);
		try {
			plugin.getArtronConfig().save(new File(plugin.getDataFolder(), "artron.yml"));
		} catch (IOException io) {
			plugin.debug("Could not save artron.yml, " + io);
		}
		TARDISMessage.send(sender, "CONFIG_UPDATED");
		return true;
	}
}
