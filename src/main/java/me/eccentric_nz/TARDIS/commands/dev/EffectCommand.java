/*
 * Copyright (C) 2026 eccentric_nz
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
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
import me.eccentric_nz.TARDIS.particles.Sphere;
import me.eccentric_nz.TARDIS.rooms.eye.Capacitor;
import me.eccentric_nz.TARDIS.utility.TARDISNumberParsers;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Locale;
import java.util.UUID;

public class EffectCommand {

    private final TARDIS plugin;

    public EffectCommand(TARDIS plugin) {
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
                    // get throttle setting
                    ResultSetThrottle rs = new ResultSetThrottle(plugin);
                    SpaceTimeThrottle throttle = rs.getSpeedAndParticles(uuid.toString()).throttle();
                    // get current settings
                    ResultSetParticlePrefs rspp = new ResultSetParticlePrefs(plugin);
                    if (rspp.fromUUID(uuid.toString())) {
                        ParticleData data = rspp.getData();
                        // display particles
                        Emitter emitter = new Emitter(plugin, uuid, rsc.getCurrent().location().clone().add(0.5, 0, 0.5), data, throttle.getFlightTime());
                        int task = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, emitter, 0, data.getShape().getPeriod());
                        emitter.setTaskID(task);
                    }
                }
            }
        } else if (args.length > 2) {
            if (args[1].equalsIgnoreCase("sphere")) {
                Location s = player.getLocation().add(3.5d, 3.5d, 3.5d);
                Capacitor capacitor = Capacitor.valueOf(args[2].toUpperCase(Locale.ROOT));
                Sphere sphere = new Sphere(plugin, player.getUniqueId(), s, capacitor);
                int task = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, sphere, 0, 10);
                sphere.setTaskID(task);
            } else {
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
                String colour = "White";
                String block = "STONE";
                if (args.length > 3) {
                    density = TARDISNumberParsers.parseInt(args[3]);
                }
                if (args.length > 4) {
                    speed = TARDISNumberParsers.parseDouble(args[4]);
                }
                if (args.length > 5) {
                    colour = args[5];
                }
                if (args.length > 6) {
                    block = args[6];
                }
                Emitter emitter = new Emitter(plugin, player.getUniqueId(), player.getLocation().add(3, 0, 3), new ParticleData(particle, shape, density, speed, colour, block, true), SpaceTimeThrottle.RAPID.getFlightTime());
                int task = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, emitter, 0, shape.getPeriod());
                emitter.setTaskID(task);
            }
        }
        return true;
    }
}
