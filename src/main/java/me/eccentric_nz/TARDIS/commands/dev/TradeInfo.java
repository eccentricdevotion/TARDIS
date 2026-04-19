package me.eccentric_nz.TARDIS.commands.dev;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.blueprints.trader.TimeLordTraderSpawner;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TradeInfo {

    private final TARDIS plugin;

    public TradeInfo(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void list(CommandSender sender) {
        Player player = (Player) sender;
        String list = new TimeLordTraderSpawner(plugin).getTrades(player.getLocation());
        plugin.getMessenger().message(sender, list);
    }
}
