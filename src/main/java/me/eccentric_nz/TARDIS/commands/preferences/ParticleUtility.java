package me.eccentric_nz.TARDIS.commands.preferences;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetParticlePrefs;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.particles.ParticleEffect;
import me.eccentric_nz.TARDIS.particles.ParticleShape;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Locale;

public class ParticleUtility {

    public static void setEffect(TARDIS plugin, Player player, String e) {
        try {
            ParticleEffect effect = ParticleEffect.valueOf(e.toUpperCase(Locale.ROOT));
            save(plugin, player, "effect", effect.toString());
        } catch (IllegalArgumentException exception) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "ARG_PARTICLE", e, "effect");
        }
    }

    public static void setShape(TARDIS plugin, Player player, String s) {
        try {
            ParticleShape shape = ParticleShape.valueOf(s.toUpperCase(Locale.ROOT));
            save(plugin, player, "shape", shape.toString());
        } catch (IllegalArgumentException exception) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "ARG_PARTICLE", s, "shape");
        }
    }

    public static void setSpeed(TARDIS plugin, Player player, double speed) {
        save(plugin, player, "speed", speed);
    }

    private static void save(TARDIS plugin, Player player, String which, Object value) {
        String uuid = player.getUniqueId().toString();
        HashMap<String, Object> set = new HashMap<>();
        set.put(which, value);
        // do they have a particle_prefs record?
        ResultSetParticlePrefs rss = new ResultSetParticlePrefs(plugin);
        if (rss.fromUUID(uuid)) {
            HashMap<String, Object> wherea = new HashMap<>();
            wherea.put("uuid", uuid);
            plugin.getQueryFactory().doUpdate("particle_prefs", set, wherea);
        } else {
            set.put("uuid", uuid);
            plugin.getQueryFactory().doInsert("particle_prefs", set);
        }
        plugin.getMessenger().send(player, TardisModule.TARDIS, "PARTICLE_SAVED");
    }
}
