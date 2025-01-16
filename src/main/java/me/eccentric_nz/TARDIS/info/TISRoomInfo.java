package me.eccentric_nz.TARDIS.info;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.entity.Player;

public class TISRoomInfo {

    private final TARDIS plugin;

    public TISRoomInfo(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void show(Player player, TARDISInfoMenu item) {
        player.sendMessage("---");
        player.sendMessage("[" + item.getName() + "]");
        plugin.getMessenger().messageWithColour(player, TARDISDescription.valueOf(item.toString()).getDesc(), "#FFAA00");
        String r = item.toString();
        plugin.getMessenger().messageWithColour(player, "Seed Block: " + plugin.getRoomsConfig().getString("rooms." + r + ".seed"), "#FFAA00");
        plugin.getMessenger().messageWithColour(player, "Offset: " + plugin.getRoomsConfig().getString("rooms." + r + ".offset"), "#FFAA00");
        plugin.getMessenger().messageWithColour(player, "Cost: " + plugin.getRoomsConfig().getString("rooms." + r + ".cost"), "#FFAA00");
        plugin.getMessenger().messageWithColour(player, "Enabled: " + plugin.getRoomsConfig().getString("rooms." + r + ".enabled"), "#FFAA00");
    }
}
