package me.eccentric_nz.tardis.display;

import me.eccentric_nz.tardis.TardisPlugin;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.Objects;

public class TardisDisplayListener implements Listener {

    private final TardisPlugin plugin;

    public TardisDisplayListener(TardisPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (plugin.getTrackerKeeper().getDisplay().containsKey(player.getUniqueId())) {
            if (Objects.requireNonNull(event.getFrom().getWorld()).getName().contains("TARDIS")) {
                return;
            }
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(plugin.getUtils().actionBarFormat(player)));
        }
    }
}
