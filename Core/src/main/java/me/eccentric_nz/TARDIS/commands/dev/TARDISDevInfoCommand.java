package me.eccentric_nz.TARDIS.commands.dev;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.info.TARDISInfoMenu;
import org.bukkit.command.CommandSender;

public class TARDISDevInfoCommand {

    private final TARDIS plugin;
    public TARDISDevInfoCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean test(CommandSender sender) {
        for (TARDISInfoMenu tim : TARDISInfoMenu.values()) {
            sender.sendMessage("---");
            sender.sendMessage("[" + tim.getName() + "]");
            TARDISInfoMenu.getChildren(tim.toString()).forEach((key, value) -> {
                String[] split = key.split(value, 2);
                if (split.length > 1) {
                    String first = "> " + split[0];
                    plugin.getMessenger().sendInfo(sender, first, value, split[1]);
                } else {
                    plugin.debug(key + ", " + value);
                }
            });
        }
        return true;
    }
}
