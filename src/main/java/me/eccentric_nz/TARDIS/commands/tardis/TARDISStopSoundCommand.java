package me.eccentric_nz.TARDIS.commands.tardis;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;

public class TARDISStopSoundCommand {
    private final TARDIS plugin;

    public TARDISStopSoundCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean mute(Player player) {
        if (player.getPersistentDataContainer().has(plugin.getLoopKey(), PersistentDataType.INTEGER)) {
            int task = player.getPersistentDataContainer().get(plugin.getLoopKey(), PersistentDataType.INTEGER);
            plugin.getServer().getScheduler().cancelTask(task);
            player.getPersistentDataContainer().remove(plugin.getLoopKey());
        }
        return true;
    }
}
