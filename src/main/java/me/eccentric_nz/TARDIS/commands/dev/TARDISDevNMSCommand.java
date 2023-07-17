package me.eccentric_nz.TARDIS.commands.dev;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.tardisweepingangels.nms.MonsterSpawner;
import me.eccentric_nz.tardisweepingangels.utils.Monster;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TARDISDevNMSCommand {

    private final TARDIS plugin;

    public TARDISDevNMSCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean spawn(CommandSender sender, String[] args) {
        if (sender instanceof Player player) {
            try {
                Monster monster = Monster.valueOf(args[1].toUpperCase());
                Location location = player.getTargetBlock(plugin.getGeneralKeeper().getTransparent(), 16).getLocation();
                new MonsterSpawner().create(location, monster);
            } catch (IllegalArgumentException e) {
            }
        }
        return true;
    }
}
