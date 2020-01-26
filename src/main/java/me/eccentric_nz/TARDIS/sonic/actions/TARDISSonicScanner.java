package me.eccentric_nz.TARDIS.sonic.actions;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.enumeration.WORLD_MANAGER;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import me.eccentric_nz.TARDIS.utility.TARDISStaticUtils;
import org.bukkit.Location;
import org.bukkit.block.Biome;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static me.eccentric_nz.TARDIS.listeners.TARDISScannerListener.getNearbyEntities;

public class TARDISSonicScanner {

    public static void scan(TARDIS plugin, Location scan_loc, Player player) {
        // record nearby entities
        HashMap<EntityType, Integer> scannedentities = new HashMap<>();
        List<String> playernames = new ArrayList<>();
        getNearbyEntities(scan_loc, 16).forEach((k) -> {
            EntityType et = k.getType();
            if (TARDISConstants.ENTITY_TYPES.contains(et)) {
                Integer entity_count = scannedentities.getOrDefault(et, 0);
                boolean visible = true;
                if (et.equals(EntityType.PLAYER)) {
                    Player entPlayer = (Player) k;
                    if (player.canSee(entPlayer)) {
                        playernames.add(entPlayer.getName());
                    } else {
                        visible = false;
                    }
                }
                if (visible) {
                    scannedentities.put(et, entity_count + 1);
                }
            }
        });
        long time = scan_loc.getWorld().getTime();
        String daynight = TARDISStaticUtils.getTime(time);
        String worldname;
        if (plugin.getWorldManager().equals(WORLD_MANAGER.MULTIVERSE)) {
            worldname = plugin.getMVHelper().getAlias(scan_loc.getWorld());
        } else {
            worldname = scan_loc.getWorld().getName();
        }
        String wn = worldname;
        // message the player
        TARDISMessage.send(player, "SONIC_SCAN");
        BukkitScheduler bsched = plugin.getServer().getScheduler();
        bsched.scheduleSyncDelayedTask(plugin, () -> {
            TARDISMessage.send(player, "SCAN_WORLD", wn);
            TARDISMessage.send(player, "SONIC_COORDS", scan_loc.getBlockX() + ":" + scan_loc.getBlockY() + ":" + scan_loc.getBlockZ());
        }, 20L);
        // get biome
        Biome biome = scan_loc.getBlock().getBiome();
        bsched.scheduleSyncDelayedTask(plugin, () -> TARDISMessage.send(player, "BIOME_TYPE", biome.toString()), 40L);
        bsched.scheduleSyncDelayedTask(plugin, () -> TARDISMessage.send(player, "SCAN_TIME", daynight + " / " + time), 60L);
        // get weather
        String weather;
        switch (biome) {
            case BADLANDS:
            case BADLANDS_PLATEAU:
            case DESERT:
            case DESERT_HILLS:
            case DESERT_LAKES:
            case ERODED_BADLANDS:
            case MODIFIED_BADLANDS_PLATEAU:
            case MODIFIED_WOODED_BADLANDS_PLATEAU:
            case SAVANNA:
            case SAVANNA_PLATEAU:
            case SHATTERED_SAVANNA:
            case SHATTERED_SAVANNA_PLATEAU:
            case WOODED_BADLANDS_PLATEAU:
                weather = plugin.getLanguage().getString("WEATHER_DRY");
                break;
            case FROZEN_OCEAN:
            case FROZEN_RIVER:
            case ICE_SPIKES:
            case SNOWY_BEACH:
            case SNOWY_MOUNTAINS:
            case SNOWY_TAIGA:
            case SNOWY_TAIGA_HILLS:
            case SNOWY_TAIGA_MOUNTAINS:
            case SNOWY_TUNDRA:
                weather = (scan_loc.getWorld().hasStorm()) ? plugin.getLanguage().getString("WEATHER_SNOW") : plugin.getLanguage().getString("WEATHER_COLD");
                break;
            default:
                weather = (scan_loc.getWorld().hasStorm()) ? plugin.getLanguage().getString("WEATHER_RAIN") : plugin.getLanguage().getString("WEATHER_CLEAR");
                break;
        }
        bsched.scheduleSyncDelayedTask(plugin, () -> TARDISMessage.send(player, "SCAN_WEATHER", weather), 80L);
        bsched.scheduleSyncDelayedTask(plugin, () -> TARDISMessage.send(player, "SCAN_HUMIDITY", String.format("%.2f", scan_loc.getBlock().getHumidity())), 100L);
        bsched.scheduleSyncDelayedTask(plugin, () -> TARDISMessage.send(player, "SCAN_TEMP", String.format("%.2f", scan_loc.getBlock().getTemperature())), 120L);
        bsched.scheduleSyncDelayedTask(plugin, () -> {
            TARDISMessage.send(player, "SCAN_ENTS");
            if (scannedentities.size() > 0) {
                scannedentities.forEach((key, value) -> {
                    String message = "";
                    StringBuilder buf = new StringBuilder();
                    if (key.equals(EntityType.PLAYER) && playernames.size() > 0) {
                        playernames.forEach((p) -> buf.append(", ").append(p));
                        message = " (" + buf.toString().substring(2) + ")";
                    }
                    player.sendMessage("    " + key + ": " + value + message);
                });
                scannedentities.clear();
            } else {
                TARDISMessage.send(player, "SCAN_NONE");
            }
        }, 140L);
    }
}
