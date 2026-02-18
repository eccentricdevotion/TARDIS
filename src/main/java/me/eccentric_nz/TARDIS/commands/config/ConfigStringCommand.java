package me.eccentric_nz.TARDIS.commands.config;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import org.bukkit.command.CommandSender;

import java.util.Locale;

public class ConfigStringCommand {

    public void set(TARDIS plugin, CommandSender sender, String option, String value, String section) {
        String tolower = option.toLowerCase(Locale.ROOT);
        String first = (section.isEmpty()) ? tolower : section + "." + tolower;
        plugin.getConfig().set(first, value);
        plugin.saveConfig();
        plugin.getMessenger().send(sender, TardisModule.TARDIS, "CONFIG_UPDATED", first);
    }
}
