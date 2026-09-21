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
        if (!isPermission(perm)) {
            plugin.getMessenger().sendWithColours(sender, TardisModule.TARDIS, perm, "#aaff33", " is not a valid TARDIS permission node! ", "#ffffff");
            return;
        }
        if (TARDISPermission.hasPermission(player, perm)) {
            plugin.getMessenger().sendWithColours(sender, TardisModule.TARDIS, player.getName() + " has ", "#ffffff", perm, "#aaff33");
        } else {
            plugin.getMessenger().sendWithColours(sender, TardisModule.TARDIS, player.getName() + " does NOT have ", "#ffffff", perm, "#aaff33");
        }
    }

    private boolean isPermission(String perm) {
        return plugin.getGeneralKeeper().getPluginYAML().getConfigurationSection("permissions").getKeys(true).contains(perm);
    }
}
