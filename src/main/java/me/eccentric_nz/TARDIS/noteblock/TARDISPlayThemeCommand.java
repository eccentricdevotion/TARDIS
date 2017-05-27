package me.eccentric_nz.TARDIS.noteblock;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.entity.Player;

public class TARDISPlayThemeCommand {

    private final TARDIS plugin;

    public TARDISPlayThemeCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean playTheme(final Player p) {
        if (plugin.getTrackerKeeper().getEggs().contains(p.getUniqueId())) {
            return true;
        }
        plugin.getTrackerKeeper().getEggs().add(p.getUniqueId());
        Song s = NBSDecoder.parse(plugin.getResource("theme.nbs"));
        SongPlayer sp = new SongPlayer(s);
        sp.addPlayer(p);
        sp.setPlaying(true);
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            plugin.getTrackerKeeper().getEggs().remove(p.getUniqueId());
        }, 2200L);
        return true;
    }
}
