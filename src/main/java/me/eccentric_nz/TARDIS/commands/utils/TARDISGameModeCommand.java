package me.eccentric_nz.TARDIS.commands.utils;

import com.google.common.collect.ImmutableList;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.commands.TARDISCompleter;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import org.bukkit.GameMode;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Locale;

public class TARDISGameModeCommand extends TARDISCompleter implements CommandExecutor, TabCompleter {

    private final TARDIS plugin;
    private final ImmutableList<String> ROOT_SUBS = ImmutableList.of("SURVIVAL", "s", "CREATIVE", "c", "ADVENTURE", "a", "SPECTATOR", "sp");

    public TARDISGameModeCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("tardisgamemode")) {
            if (label.equalsIgnoreCase("tgms")) {
                // set survival
                return setPlayerGameMode(sender, args, GameMode.SURVIVAL);
            }
            if (label.equalsIgnoreCase("tgmc")) {
                // set creative
                return setPlayerGameMode(sender, args, GameMode.CREATIVE);
            }
            if (label.equalsIgnoreCase("tgma")) {
                // set adventure
                return setPlayerGameMode(sender, args, GameMode.ADVENTURE);
            }
            if (label.equalsIgnoreCase("tgmsp")) {
                // set spectator
                return setPlayerGameMode(sender, args, GameMode.SPECTATOR);
            }
            return setPlayerGameMode(sender, args, null);
        }
        return false;
    }

    private boolean setPlayerGameMode(CommandSender sender, String[] args, GameMode gm) {
        Player player = null;
        boolean thirdperson = false;
        if (sender instanceof ConsoleCommandSender) {
            // must specify a player name
            if ((gm == null && args.length < 2) || ((gm != null && args.length < 1))) {
                TARDISMessage.send(sender, "COULD_NOT_FIND_NAME");
                return true;
            }
            player = plugin.getServer().getPlayer(gm == null ? args[1] : args[0]);
            if (player == null) {
                TARDISMessage.send(sender, "NOT_ONLINE");
                return true;
            }
            thirdperson = true;
        }
        if (sender instanceof Player) {
            if (gm == null && args.length == 2) {
                player = plugin.getServer().getPlayer(args[1]);
                if (player == null) {
                    TARDISMessage.send(sender, "NOT_ONLINE");
                    return true;
                }
            } else if (gm != null && args.length == 1) {
                if (ROOT_SUBS.contains(args[0].toUpperCase(Locale.ENGLISH))) {
                    player = (Player) sender;
                } else {
                    player = plugin.getServer().getPlayer(args[0]);
                    thirdperson = true;
                    if (player == null) {
                        TARDISMessage.send(sender, "NOT_ONLINE");
                        return true;
                    }
                }
            } else {
                player = (Player) sender;
            }
        }
        if (gm == null) {
            // get GameMode from first argument
            if (args[0].length() <= 2) {
                switch (args[0].toLowerCase(Locale.ENGLISH)) {
                    case "s":
                        gm = GameMode.SURVIVAL;
                        break;
                    case "c":
                        gm = GameMode.CREATIVE;
                        break;
                    case "a":
                        gm = GameMode.ADVENTURE;
                        break;
                    case "sp":
                        gm = GameMode.SPECTATOR;
                        break;
                    default:
                        TARDISMessage.send(sender, "ARG_GAMEMODE");
                        return false;
                }
            } else {
                try {
                    gm = GameMode.valueOf(args[0].toUpperCase(Locale.ENGLISH));
                } catch (IllegalArgumentException e) {
                    TARDISMessage.send(sender, "ARG_GAMEMODE");
                    return false;
                }
            }
        }
        player.setGameMode(gm);
        if (thirdperson) {
            TARDISMessage.send(sender, "CMD_GAMEMODE_CONSOLE", player.getName(), gm.toString());
            TARDISMessage.send(player, "CMD_GAMEMODE_PLAYER", gm.toString());
        } else {
            TARDISMessage.send(player, "CMD_GAMEMODE_PLAYER", gm.toString());
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length <= 1) {
            return partial(args[0], ROOT_SUBS);
        }
        if (args.length == 2) {
            return null;
        }
        return ImmutableList.of();
    }
}
