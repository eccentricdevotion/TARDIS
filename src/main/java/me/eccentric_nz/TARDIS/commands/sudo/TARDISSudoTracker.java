package me.eccentric_nz.TARDIS.commands.sudo;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class TARDISSudoTracker {

    public static HashMap<UUID, UUID> SUDOERS = new HashMap<>();
    public static UUID CONSOLE_UUID = UUID.randomUUID();

    public static boolean isSudo(CommandSender sender) {
        return SUDOERS.containsKey(getSudo(sender));
    }

    public static UUID getSudoPlayer(CommandSender sender) {
        return SUDOERS.get(getSudo(sender));
    }

    public static UUID getSudo(CommandSender sender) {
        UUID uuid;
        Player player;
        if (sender instanceof Player) {
            player = (Player) sender;
            uuid = player.getUniqueId();
        } else {
            // console
            uuid = CONSOLE_UUID;
        }
        return uuid;
    }
}
