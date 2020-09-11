package me.eccentric_nz.TARDIS.commands.utils;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.messaging.TARDISMessage;
import me.eccentric_nz.TARDIS.planets.TARDISBiome;
import me.eccentric_nz.TARDIS.utility.TARDISStaticUtils;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TARDISBiomeCommand implements CommandExecutor {

    private final TARDIS plugin;

    public TARDISBiomeCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
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
            TARDISMessage.message(player, "The TARDISBiome is: " + biome.getKey().toString());
            return true;
        }
        return false;
    }
}
