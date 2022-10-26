package me.eccentric_nz.TARDIS.commands.handles;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.commands.tardis.TARDISHandbrakeCommand;
import me.eccentric_nz.TARDIS.utility.TARDISNumberParsers;
import org.bukkit.entity.Player;

public class TARDISHandlesBrakeCommand {

    private final TARDIS plugin;

    public TARDISHandlesBrakeCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean park(Player player, String[] args) {
        // handles brake on uuid id
        int id = TARDISNumberParsers.parseInt(args[3]);
        return new TARDISHandbrakeCommand(plugin).toggle(player, id, args, false);
    }
}
