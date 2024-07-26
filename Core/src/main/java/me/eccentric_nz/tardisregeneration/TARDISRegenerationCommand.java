package me.eccentric_nz.tardisregeneration;

import com.google.common.collect.ImmutableList;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.commands.TARDISCompleter;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.List;

public class TARDISRegenerationCommand extends TARDISCompleter implements CommandExecutor, TabCompleter {

    private final TARDIS plugin;
    private final ImmutableList<String> ROOT_SUBS = ImmutableList.of("add", "remove");

    public TARDISRegenerationCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("tardisregeneration")) {
            // need at least 2 arguments
            if (args.length < 2) {
                plugin.getMessenger().send(sender, TardisModule.REGENERATION, "TOO_FEW_ARGS");
                return true;
            }
            if (args[0].equalsIgnoreCase("remove")) {
                return true;
            }
            if (args[0].equalsIgnoreCase("add")) {
                return true;
            }
            return true;
        } else {
            plugin.getMessenger().send(sender, TardisModule.REGENERATION, "CMD_NO_CONSOLE");
            return true;
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        String lastArg = args[args.length - 1];
        if (args.length <= 1) {
            return partial(args[0], ROOT_SUBS);
        }
//        else if (args.length == 2) {
//            String sub = args[0];
//            if (sub.equalsIgnoreCase("add")) {
//                return partial(lastArg, ITEM_SUBS);
//            }
//        }
        return ImmutableList.of();
    }
}
