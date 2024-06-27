package me.eccentric_nz.TARDIS.commands.dev;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.data.ParticleData;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetCurrentFromId;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetParticlePrefs;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardisID;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetThrottle;
import me.eccentric_nz.TARDIS.enumeration.SpaceTimeThrottle;
import me.eccentric_nz.TARDIS.particles.Emitter;
import me.eccentric_nz.TARDIS.particles.ParticleEffect;
import me.eccentric_nz.TARDIS.particles.ParticleShape;
import me.eccentric_nz.TARDIS.utility.TARDISNumberParsers;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Locale;
import java.util.UUID;

public class TARDISDevEffectCommand {

    private final TARDIS plugin;

    public TARDISDevEffectCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean show(Player player, String[] args) {
        if (args.length == 1) {
            // use particle prefs
            UUID uuid = player.getUniqueId();
            // get players TARDIS id
            ResultSetTardisID rst = new ResultSetTardisID(plugin);
            if (rst.fromUUID(uuid.toString())) {
                // get TARDIS location
                ResultSetCurrentFromId rsc = new ResultSetCurrentFromId(plugin, rst.getTardisId());
                if (rsc.resultSet()) {
                    Location current = new Location(rsc.getWorld(), rsc.getX(), rsc.getY(), rsc.getZ()).add(0.5, 0, 0.5);
                    // get throttle setting
                    ResultSetThrottle rs = new ResultSetThrottle(plugin);
                    SpaceTimeThrottle throttle = rs.getSpeedAndParticles(uuid.toString()).getThrottle();
                    // get current settings
                    ResultSetParticlePrefs rspp = new ResultSetParticlePrefs(plugin);
                    if (rspp.fromUUID(uuid.toString())) {
                        ParticleData data = rspp.getData();
                        // display particles
                        Emitter emitter = new Emitter(plugin, uuid, current, data, throttle.getFlightTime());
                        int task = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, emitter, 0, data.getShape().getPeriod());
                        emitter.setTaskID(task);
                    }
                }
            }
        } else if (args.length > 2) {
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
