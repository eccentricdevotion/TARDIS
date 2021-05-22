package me.eccentric_nz.tardis.commands.utils;

import me.eccentric_nz.tardis.TARDIS;
import me.eccentric_nz.tardis.messaging.TARDISMessage;
import me.eccentric_nz.tardis.planets.TARDISBiome;
import me.eccentric_nz.tardis.utility.TARDISStaticUtils;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class TARDISBiomeCommand implements CommandExecutor {

	private final TARDIS plugin;

	public TARDISBiomeCommand(TARDIS plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(@NotNull CommandSender sender, Command cmd, @NotNull String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("tardisbiome")) {
			Player player;
			if (sender instanceof Player) {
				player = (Player) sender;
			} else {
				TARDISMessage.send(sender, "CMD_PLAYER");
				return true;
			}
			// get location
			Location eyeLocation = player.getTargetBlock(plugin.getGeneralKeeper().getTransparent(), 20).getLocation();
			// get biome
			TARDISBiome biome = TARDISStaticUtils.getBiomeAt(eyeLocation);
			TARDISMessage.message(player, "The TARDISBiome is: " + biome.getKey());
			return true;
		}
		return false;
	}
}
