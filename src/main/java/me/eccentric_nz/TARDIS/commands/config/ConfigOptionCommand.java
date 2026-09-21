package me.eccentric_nz.TARDIS.commands.config;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import org.bukkit.command.CommandSender;

public class ConfigOptionCommand {

    public static void show(TARDIS plugin, CommandSender sender, String first) {
        // get the option path
        boolean isMainConfig = true;
        String path = "";
        if (ConfigUtility.firstsStr.containsKey(first)) {
            path = ConfigUtility.firstsStr.get(first) + "." + first;
        } else if (ConfigUtility.firstsBool.containsKey(first)) {
            path = ConfigUtility.firstsBool.get(first) + "." + first;
        } else if (ConfigUtility.firstsInt.containsKey(first)) {
            path = ConfigUtility.firstsInt.get(first) + "." + first;
        } else if (ConfigUtility.firstsStrArtron.contains(first) || ConfigUtility.firstsIntArtron.contains(first)) {
            isMainConfig = false;
            path = first;
        }
        // show the value of the config option
        if (isMainConfig) {
            plugin.getMessenger().send(sender, TardisModule.TARDIS, "CONFIG_OPTION", path, plugin.getConfig().getString(path));
        } else {
            plugin.getMessenger().send(sender, TardisModule.TARDIS, "CONFIG_OPTION_ARTRON", first, plugin.getArtronConfig().getString(path));
        }
        plugin.getMessenger().send(sender, TardisModule.TARDIS, "CONFIG_OPTION_SET", first);
    }
}
