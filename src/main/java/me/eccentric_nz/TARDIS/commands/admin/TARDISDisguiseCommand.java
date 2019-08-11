package me.eccentric_nz.TARDIS.commands.admin;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class TARDISDisguiseCommand {

    private final TARDIS plugin;

    public TARDISDisguiseCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean disguise(CommandSender sender, String[] args) {
        if (args.length < 2) {
            TARDISMessage.message(sender, "Two few arguments!");
            return true;
        }
        if (args[0].equalsIgnoreCase("disguise")) {
            Player player = null;
            if (args.length == 3) {
                player = plugin.getServer().getPlayer(args[2]);
            } else if (sender instanceof Player) {
                player = (Player) sender;
            }
            if (player == null) {
                TARDISMessage.message(sender, "You need to specify a player!");
                return true;
            }
            EntityType entityType;
            try {
                entityType = EntityType.valueOf(args[1]);
            } catch (IllegalArgumentException e) {
                TARDISMessage.message(sender, "You need to specify a valid living entity type!");
                return true;
            }
            plugin.getTardisHelper().disguise(entityType, player);
        }
        if (args[0].equalsIgnoreCase("undisguise")) {
            Player player = null;
            if (args.length == 2) {
                player = plugin.getServer().getPlayer(args[1]);
            } else if (sender instanceof Player) {
                player = (Player) sender;
            }
            if (player == null) {
                TARDISMessage.message(sender, "You need to specify a player!");
                return true;
            }
            plugin.getTardisHelper().undisguise(player);
        }
        return true;
    }
}
