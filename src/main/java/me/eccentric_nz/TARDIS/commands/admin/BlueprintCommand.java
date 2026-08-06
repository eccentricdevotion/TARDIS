package me.eccentric_nz.TARDIS.commands.admin;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BlueprintCommand {
    private final TARDIS plugin;

    public BlueprintCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void hasPermission(CommandSender sender, Player player, String perm) {
        if (TARDISPermission.hasPermission(player, perm)) {
            plugin.getMessenger().message(sender, TardisModule.TARDIS, player.getName() + "has " + perm);
        } else {
            plugin.getMessenger().message(sender, TardisModule.TARDIS, player.getName() + "does NOT have " + perm);
        }
    }
}
