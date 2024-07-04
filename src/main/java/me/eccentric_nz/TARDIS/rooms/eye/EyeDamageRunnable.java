package me.eccentric_nz.TARDIS.rooms.eye;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.entity.Player;

import java.util.UUID;

public class EyeDamageRunnable implements Runnable {

    private final TARDIS plugin;

    public EyeDamageRunnable(TARDIS plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        for (UUID uuid : plugin.getTrackerKeeper().getEyeDamage()) {
            Player player = plugin.getServer().getPlayer(uuid);
            if (player != null && player.isOnline()) {
                player.damage(plugin.getConfig().getDouble("eye_of_harmony.damage_amount"));
            }
        }
    }
}
