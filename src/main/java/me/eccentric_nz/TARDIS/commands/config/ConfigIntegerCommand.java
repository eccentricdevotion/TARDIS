package me.eccentric_nz.TARDIS.commands.config;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.command.CommandSender;

public class ConfigIntegerCommand {

    public void set(TARDIS plugin, CommandSender sender, String o, int i) {
        if (ConfigUtility.firstsInt.containsKey(o)) {
            String section = ConfigUtility.firstsInt.get(o);
            if (section != null) {
                new SetIntegerCommand(plugin).setConfigInt(sender, o, i, section);
            }
        } else if (ConfigUtility.firstsIntArtron.contains(o)) {
            new SetIntegerCommand(plugin).setConfigInt(sender, o, i);
        }
    }
}
