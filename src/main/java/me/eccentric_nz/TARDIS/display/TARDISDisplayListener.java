package me.eccentric_nz.TARDIS.display;

import me.eccentric_nz.TARDIS.TARDIS;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class TARDISDisplayListener implements Listener {

    private final TARDIS plugin;

    public TARDISDisplayListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (plugin.getTrackerKeeper().getDisplay().containsKey(player.getUniqueId())) {
            if (event.getFrom().getWorld().getName().contains("TARDIS")) {
                return;
            }
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(plugin.getUtils().actionBarFormat(player)));
        }
    }
}
