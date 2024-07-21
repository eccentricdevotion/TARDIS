package me.eccentric_nz.TARDIS.info;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.entity.Player;

public class TISInfo {

    private final TARDIS plugin;

    public TISInfo(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void show(Player p, TARDISInfoMenu item) {
        p.sendMessage("---");
        p.sendMessage("[" + item.getName() + "]");
        plugin.getMessenger().messageWithColour(p, TARDISDescription.valueOf(item.toString()).getDesc(), "#FFAA00");
    }
}
