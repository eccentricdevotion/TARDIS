package me.eccentric_nz.TARDIS.commands.dev;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.blueprints.trader.TimeLordTraderSpawner;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TradeCommand {

    private final TARDIS plugin;

    public TradeCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void spawn(CommandSender sender) {
        Player player = (Player) sender;
        Location location = player.getLocation().add(2.5d, 0, 2.5d);
        location.setPitch(0.0f);
        new TimeLordTraderSpawner(plugin).spawn(location);
    }
}
