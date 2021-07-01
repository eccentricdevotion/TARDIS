package me.eccentric_nz.TARDIS.display;

import com.google.common.collect.ImmutableList;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.commands.TARDISCompleter;
import me.eccentric_nz.TARDIS.messaging.TARDISMessage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TARDISDisplayCommand extends TARDISCompleter implements CommandExecutor, TabCompleter {

    private final TARDIS plugin;
    private final List<String> SUBS = new ArrayList<>();

    public TARDISDisplayCommand(TARDIS plugin) {
        this.plugin = plugin;
        for (TARDISDisplayType dt : TARDISDisplayType.values()) {
            SUBS.add(dt.toString());
        }
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        if (sender instanceof Player p) {
            UUID uuid = p.getUniqueId();
            if (args.length == 0) {
                if (p.hasPermission("tardis.display")) {
                    if (plugin.getTrackerKeeper().getDisplay().containsKey(uuid)) {
                        plugin.getTrackerKeeper().getDisplay().remove(uuid);
                        TARDISMessage.send(p, "DISPLAY_DISABLED");
                    } else {
                        plugin.getTrackerKeeper().getDisplay().put(uuid, TARDISDisplayType.ALL);
                        TARDISMessage.send(p, "DISPLAY_ENABLED");
                    }
                } else {
                    TARDISMessage.send(p, "DISPLAY_PERMISSION");
                }
            }
            if (args.length == 1) {
                try {
                    TARDISDisplayType displayType = TARDISDisplayType.valueOf(args[0].toUpperCase());
                    plugin.getTrackerKeeper().getDisplay().put(uuid, displayType);
                    TARDISMessage.send(p, "DISPLAY_ENABLED");
                } catch (IllegalArgumentException e) {
                    TARDISMessage.send(p, "DISPLAY_INVALID");
                }
            }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (args.length <= 1) {
            return partial(args[0], SUBS);
        }
        return ImmutableList.of();
    }
}
