package me.eccentric_nz.TARDIS.commands.dev;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.data.ParticleData;
import me.eccentric_nz.TARDIS.enumeration.SpaceTimeThrottle;
import me.eccentric_nz.TARDIS.particles.Emitter;
import me.eccentric_nz.TARDIS.particles.ParticleEffect;
import me.eccentric_nz.TARDIS.particles.ParticleShape;
import me.eccentric_nz.TARDIS.utility.TARDISNumberParsers;
import org.bukkit.entity.Player;

import java.util.Locale;

public class TARDISDevEffectCommand {

    private final TARDIS plugin;

    public TARDISDevEffectCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean show(Player player, String[] args) {
        if (args.length > 2) {
            ParticleShape shape;
            ParticleEffect particle;
            try {
                shape = ParticleShape.valueOf(args[1].toUpperCase(Locale.ROOT));
            } catch (IllegalArgumentException e) {
                shape = ParticleShape.RANDOM;
            }
            try {
                particle = ParticleEffect.valueOf(args[2].toUpperCase(Locale.ROOT));
            } catch (IllegalArgumentException e) {
                particle = ParticleEffect.EFFECT;
            }
            // get density and speed
            int density = 16;
            double speed = 0;
            if (args.length > 3) {
                density = TARDISNumberParsers.parseInt(args[3]);
            }
            if (args.length > 4) {
                speed = TARDISNumberParsers.parseDouble(args[4]);
            }
            Emitter emitter = new Emitter(plugin, player.getUniqueId(), player.getLocation().add(3, 0, 3), new ParticleData(particle, shape, density, speed, true), SpaceTimeThrottle.RAPID.getFlightTime());
            int task = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, emitter, 0, shape.getPeriod());
            emitter.setTaskID(task);
        }
        return true;
    }
}
