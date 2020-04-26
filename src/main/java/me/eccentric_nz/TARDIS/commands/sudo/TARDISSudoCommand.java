package me.eccentric_nz.TARDIS.commands.sudo;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.ResultSetTardisID;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class TARDISSudoCommand implements CommandExecutor {

    private final TARDIS plugin;

    public TARDISSudoCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("tardissudo")) {
            if (sender instanceof ConsoleCommandSender || sender.hasPermission("tardis.admin")) {
                Player player;
                UUID uuid;
                if (sender instanceof Player) {
                    player = (Player) sender;
                    uuid = player.getUniqueId();
                } else {
                    uuid = TARDISSudoTracker.CONSOLE_UUID;
                }
                String first = args[0];
                if (first.equalsIgnoreCase("off")) {
                    TARDISSudoTracker.SUDOERS.remove(uuid);
                    TARDISMessage.send(sender, "SUDO_OFF");
                } else {
                    // must be a player name
                    OfflinePlayer offlinePlayer = plugin.getServer().getOfflinePlayer(args[0]);
                    if (offlinePlayer == null) {
                        TARDISMessage.send(sender, "COULD_NOT_FIND_NAME");
                        return true;
                    }
                    ResultSetTardisID rs = new ResultSetTardisID(plugin);
                    if (!rs.fromUUID(offlinePlayer.getUniqueId().toString())) {
                        TARDISMessage.send(sender, "PLAYER_NO_TARDIS");
                        return true;
                    }
                    TARDISSudoTracker.SUDOERS.put(uuid, offlinePlayer.getUniqueId());
                    TARDISMessage.send(sender, "SUDO_ON", offlinePlayer.getName());
                }
            } else {
                TARDISMessage.send(sender, "CMD_ADMIN");
            }
            return true;
        }
        return false;
    }
}
