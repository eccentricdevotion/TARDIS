package me.eccentric_nz.TARDIS.commands.preferences;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetParticlePrefs;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.particles.ParticleEffect;
import me.eccentric_nz.TARDIS.particles.ParticleShape;
import me.eccentric_nz.TARDIS.utility.TARDISNumberParsers;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Locale;

public class TARDISParticlePrefsCommand {

    private final TARDIS plugin;

    public TARDISParticlePrefsCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean setPartclePref(Player player, String[] args) {
        String which = args[0].toLowerCase();
        Object value;
        switch (which) {
            case "effect" -> {
                try {
                    ParticleEffect effect = ParticleEffect.valueOf(args[1].toUpperCase(Locale.ROOT));
                    value = effect.toString();
                } catch (IllegalArgumentException e) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "ARG_PARTICLE", args[1], "effect");
                    return true;
                }
            }
            case "shape" -> {
                try {
                    ParticleShape shape = ParticleShape.valueOf(args[1].toUpperCase(Locale.ROOT));
                    value = shape.toString();
                } catch (IllegalArgumentException e) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "ARG_PARTICLE", args[1], "shape");
                    return true;
                }
            }
            default -> {
                // speed
                value = TARDISNumberParsers.parseDouble(args[1]);
                if ((Double) value == -1) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "ARG_PARTICLE", args[1], "speed");
                    return true;
                }
            }
        }
        String uuid = player.getUniqueId().toString();
        HashMap<String, Object> set = new HashMap<>();
        set.put(which, value);
        // do they have a particle_prefs record?
        HashMap<String, Object> wheres = new HashMap<>();
        wheres.put("uuid", uuid);
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
        return true;
    }
}
