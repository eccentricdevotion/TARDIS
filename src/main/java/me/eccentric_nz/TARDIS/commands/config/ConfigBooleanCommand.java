package me.eccentric_nz.TARDIS.commands.config;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.command.CommandSender;

public class ConfigBooleanCommand {

    public void set(TARDIS plugin, CommandSender sender, String o, boolean b) {
        if (o.equals("zero_room")) {
            new SetZeroRoomCommand(plugin).setConfigZero(sender, b);
        } else {
            new SetBooleanCommand(plugin).setConfigBool(sender, o, b, ConfigUtility.firstsBool.get(o));
        }
    }
}
