package me.eccentric_nz.TARDIS.lights;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.customblocks.TARDISDisplayItemUtils;
import me.eccentric_nz.TARDIS.customblocks.VariableLight;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetLamps;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetLightLevel;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetLightPrefs;
import me.eccentric_nz.TARDIS.enumeration.TardisLight;
import me.eccentric_nz.TARDIS.utility.TARDISNumberParsers;
import org.bukkit.block.Block;
import org.bukkit.block.data.Levelled;

import java.util.HashMap;
import java.util.UUID;

public class TARDISLightSequence {

    private final TARDIS plugin;
    private final int id;
    private final UUID uuid;

    public TARDISLightSequence(TARDIS plugin, int id, UUID uuid) {
        this.plugin = plugin;
        this.id = id;
        this.uuid = uuid;
    }

    public void play() {
        plugin.getTrackerKeeper().getLightChangers().add(uuid);
        // get current light level
        ResultSetLightLevel lightLevel = new ResultSetLightLevel(plugin);
        if (lightLevel.fromTypeAndID(50, id)) {
            int currentLevel = lightLevel.getLevel();
            // get light sequence + delays + levels , current light type
            ResultSetLightPrefs rs = new ResultSetLightPrefs(plugin);
            if (rs.fromID(id)) {
                TardisLight currentLight = rs.getLight();
                // get TARDIS lights
                HashMap<String, Object> whereLight = new HashMap<>();
                whereLight.put("tardis_id", id);
                ResultSetLamps rsl = new ResultSetLamps(plugin, whereLight, true);
                if (rsl.resultSet()) {
                    // get sequence / delay / light levels
                    String[] seq = rs.getSequence().split(":");
                    int[] del = makeIntArray(rs.getDelays().split(":"));
                    int[] lev = makeIntArray(rs.getLevels().split(":"));
                    long delay = 5;
                    for (int i = 0; i < seq.length; i++) {
                        if (seq[i].equals("BLACK")) {
                            continue;
                        }
                        int level = lev[i];
                        TardisLight tardisLight = getLight(seq[i]);
                        // schedule delayed task
                        int remove = i;
                        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                            for (Block block : rsl.getData()) {
                                if (block.getBlockData() instanceof Levelled) {
                                    // remove the current light only on first loop
                                    if (remove == 0) {
                                        TARDISDisplayItemUtils.remove(block);
                                        // set new light - delay as interaction may be removed by the above
                                        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> new VariableLight(rs.getMaterial(), block.getLocation().add(0.5, 0.5, 0.5)).set(tardisLight.getOn().getCustomModel(), level), 3L);
                                    } else {
                                        // just change the variable light display
                                        new VariableLight(block.getLocation().add(0.5, 0.5, 0.5)).change(tardisLight.getOn().getCustomModel(), level);
                                    }
                                }
                            }
                        }, delay);
                        // increase delay
                        delay += del[i];
                    }
                    // reset lights back to original light and level
                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                        for (Block block : rsl.getData()) {
                            // remove the current light
                            TARDISDisplayItemUtils.remove(block);
                            // set new light - delay as interaction may be removed by the above
                            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                                if (currentLight.getOn().isVariable()) {
                                    new VariableLight(rs.getMaterial(), block.getLocation().add(0.5, 0.5, 0.5)).set(currentLight.getOn().getCustomModel(), currentLevel);
                                } else {
                                    TARDISDisplayItemUtils.set(currentLight.getOn(), block, -1);
                                }
                            }, 3L);
                        }
                        plugin.getTrackerKeeper().getLightChangers().remove(uuid);
                    }, delay);
                }
            }
        }
    }

    private TardisLight getLight(String s) {
        String variable = "VARIABLE_" + s;
        try {
            return TardisLight.valueOf(variable);
        } catch (IllegalArgumentException e) {
            return TardisLight.VARIABLE;
        }
    }

    private int[] makeIntArray(String[] strings) {
        int[] longs = new int[strings.length];
        for (int d = 0; d < strings.length; d++) {
            longs[d] = TARDISNumberParsers.parseInt(strings[d]);
        }
        return longs;
    }
}
