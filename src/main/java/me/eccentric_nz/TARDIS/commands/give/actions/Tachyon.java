package me.eccentric_nz.TARDIS.commands.give.actions;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import java.util.UUID;

public class Tachyon {

    private final TARDIS plugin;

    public Tachyon(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void give(CommandSender sender, String player, String amount) {
        if (!plugin.getConfig().getBoolean("modules.vortex_manipulator")) {
            plugin.getMessenger().send(sender, TardisModule.TARDIS, "RECIPE_VORTEX");
            return;
        }
        if (Bukkit.getOfflinePlayer(player).getName() == null) {
            plugin.getMessenger().send(sender, TardisModule.TARDIS, "COULD_NOT_FIND_NAME");
            return;
        }
        // Look up this player's UUID
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(player);
        if (offlinePlayer.getName() != null) {
            UUID uuid = offlinePlayer.getUniqueId();
            plugin.getServer().dispatchCommand(sender, "vm give " + uuid + " " + amount);
        } else {
            plugin.getMessenger().send(sender, TardisModule.TARDIS, "PLAYER_NOT_FOUND");
        }
    }
}
