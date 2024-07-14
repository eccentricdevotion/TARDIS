package me.eccentric_nz.TARDIS.regeneration;

import io.papermc.lib.PaperLib;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.particles.Regeneration;
import me.eccentric_nz.TARDIS.particles.TARDISParticleRunnable;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class RegenerationEmitter extends TARDISParticleRunnable implements Runnable {

    private final TARDIS plugin;
    private final Player player;
    private final Location location;
    private final float yaw;

    public RegenerationEmitter(TARDIS plugin, Player player, Location location, float yaw) {
        super(plugin, player.getUniqueId());
        this.plugin = plugin;
        this.player = player;
        this.location = location;
        this.yaw = yaw;
    }

    @Override
    public void cancel() {
        plugin.getServer().getScheduler().cancelTask(taskID);
        taskID = 0;
        if (player.isOnline()) {
            // eject the player
            Entity vehicle = player.getVehicle();
            if (vehicle != null) {
                vehicle.eject();
                // remove the display entity
                vehicle.remove();
            }
            // reset player scale
            player.getAttribute(Attribute.GENERIC_SCALE).setBaseValue(1.0d);
            // remove invisibility
            player.removePotionEffect(PotionEffectType.INVISIBILITY);
            // show the player again
            for (Player p : plugin.getServer().getOnlinePlayers()) {
                if (p.getWorld() == player.getWorld()) {
                    p.showPlayer(plugin, player);
                }
            }
            // add potion effects
            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 180, 1));
            player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 180, 1));
            player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 180, 1));
            // reset skin
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                if (PaperLib.isPaper()) {
                    SkinChangerPaper.remove(player);
                } else {
                    SkinChangerSpigot.remove(player);
                }
                // remove potion effects
                player.removePotionEffect(PotionEffectType.SLOWNESS);
                player.removePotionEffect(PotionEffectType.WEAKNESS);
                player.removePotionEffect(PotionEffectType.REGENERATION);
            }, 180L);
        }
    }

    @Override
    public void run() {
        if (t < 20) {
            TARDISParticleRunnable runnable = new Regeneration(plugin, player, location, yaw);
            int task = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, runnable, 0, 1L);
            runnable.setTaskID(task);
            t++;
        } else {
            cancel();
        }
    }
}
