package me.eccentric_nz.TARDIS.commands;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.commands.tardis.TARDISComehereRequestCommand;
import me.eccentric_nz.TARDIS.messaging.TARDISMessage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class TARDISCallCommand implements CommandExecutor, TabCompleter {

    private final TARDIS plugin;

    public TARDISCallCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (cmd.getName().equalsIgnoreCase("tardiscall")) {
            if (!(sender instanceof Player player)) {
                TARDISMessage.send(sender, "CMD_PLAYER");
                return true;
            }
            if (args.length != 1) {
                TARDISMessage.send(player, "TOO_FEW_ARGS");
                return true;
            }
            // get the requested player
            Player requested = plugin.getServer().getPlayer(args[0]);
            if (requested == null) {
                TARDISMessage.send(player, "PLAYER_NOT_FOUND");
                return true;
            }
            return new TARDISComehereRequestCommand(plugin).requestComeHere(player, requested);
        }
        return false;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        return null;
    }
}
