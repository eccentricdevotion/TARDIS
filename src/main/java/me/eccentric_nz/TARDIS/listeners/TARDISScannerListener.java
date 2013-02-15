/*
 * Copyright (C) 2013 eccentric_nz
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
package me.eccentric_nz.TARDIS.listeners;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.thirdparty.Version;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.getspout.spoutapi.SpoutManager;

/**
 * The Scanner consists of a collection of thousands of instruments designed to
 * gather information about the environment outside a TARDIS. Chief among these
 * is the visual signal, which is displayed on the Scanner Screen found in any
 * of the Control Rooms.
 *
 * @author eccentric_nz
 */
public class TARDISScannerListener implements Listener {

    private final TARDIS plugin;
    List<Material> validBlocks = new ArrayList<Material>();
    List<EntityType> entities = new ArrayList<EntityType>();
    HashMap<EntityType, Integer> scannedentities = new HashMap<EntityType, Integer>();
    Version bukkitversion;
    Version prewoodbuttonversion = new Version("1.4.2");

    public TARDISScannerListener(TARDIS plugin) {
        this.plugin = plugin;
        String[] v = Bukkit.getServer().getBukkitVersion().split("-");
        bukkitversion = new Version(v[0]);
        if (bukkitversion.compareTo(prewoodbuttonversion) >= 0) {
            validBlocks.add(Material.WOOD_BUTTON);
        }
        validBlocks.add(Material.STONE_BUTTON);
        validBlocks.add(Material.LEVER);
        entities.add(EntityType.BAT);
        entities.add(EntityType.BLAZE);
        entities.add(EntityType.CAVE_SPIDER);
        entities.add(EntityType.CHICKEN);
        entities.add(EntityType.COW);
        entities.add(EntityType.CREEPER);
        entities.add(EntityType.ENDERMAN);
        entities.add(EntityType.GHAST);
        entities.add(EntityType.IRON_GOLEM);
        entities.add(EntityType.MAGMA_CUBE);
        entities.add(EntityType.MUSHROOM_COW);
        entities.add(EntityType.OCELOT);
        entities.add(EntityType.PIG);
        entities.add(EntityType.PIG_ZOMBIE);
        entities.add(EntityType.PLAYER);
        entities.add(EntityType.SHEEP);
        entities.add(EntityType.SILVERFISH);
        entities.add(EntityType.SKELETON);
        entities.add(EntityType.SLIME);
        entities.add(EntityType.SPIDER);
        entities.add(EntityType.SQUID);
        entities.add(EntityType.VILLAGER);
        entities.add(EntityType.WITCH);
        entities.add(EntityType.WOLF);
        entities.add(EntityType.ZOMBIE);
    }

    /**
     * Listens for player interaction with the handbrake (lever) on the TARDIS
     * console. If the button is right-clicked the handbrake is set off, if
     * right-clicked while sneaking it is set on.
     */
    @EventHandler(priority = EventPriority.MONITOR)
    public void onInteract(PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        Block block = event.getClickedBlock();
        if (block != null) {
            Material blockType = block.getType();
            if (validBlocks.contains(blockType)) {
                // get clicked block location
                Location b = block.getLocation();
                World w = b.getWorld();
                String bw = b.getWorld().getName();
                int bx = b.getBlockX();
                int by = b.getBlockY();
                int bz = b.getBlockZ();
                String scanner_loc = bw + ":" + bx + ":" + by + ":" + bz;
                // get tardis from saved handbrake location
                HashMap<String, Object> where = new HashMap<String, Object>();
                where.put("scanner", scanner_loc);
                ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
                if (rs.resultSet()) {
                    int id = rs.getTardis_id();
                    if (plugin.getServer().getPluginManager().getPlugin("Spout") != null) {
                        SpoutManager.getSoundManager().playGlobalCustomSoundEffect(plugin, "https://dl.dropbox.com/u/53758864/soundeffects/scanner.mp3", false, b, 20, 75);
                    } else {
                        w.playSound(b, Sound.FIZZ, 1, 0);
                    }
                    String policebox;
                    String whereisit;
                    if (plugin.tardisHasDestination.containsKey(Integer.valueOf(id))) {
                        policebox = rs.getSave();
                        whereisit = "next destination";
                    } else {
                        policebox = rs.getCurrent();
                        whereisit = "current location";
                    }
                    Location scan_loc = plugin.utils.getLocationFromDB(policebox, 0, 0);
                    World pbw = scan_loc.getWorld();
                    Entity e = pbw.spawnEntity(scan_loc, EntityType.EGG);
                    double scan_radius = plugin.getConfig().getDouble("scan_radius");
                    // record nearby entities
                    for (Entity k : e.getNearbyEntities(scan_radius, scan_radius, scan_radius)) {
                        EntityType et = k.getType();
                        if (entities.contains(et)) {
                            scannedentities.put(et, scannedentities.get(et) + 1);
                        }
                    }
                    e.remove();
                    long time = scan_loc.getWorld().getTime();
                    String daynight = "day";
                    if (time > 12000) {
                        daynight = "night";
                    }
                    // get weather
                    String weather = (scan_loc.getWorld().hasStorm()) ? "raining" : "not raining";
                    // message the player
                    player.sendMessage(plugin.pluginName + "Sane results for the TARDIS's " + whereisit);
                    player.sendMessage("World: " + scan_loc.getWorld().getName());
                    player.sendMessage("Co-ordinates: " + scan_loc.getBlockX() + ":" + scan_loc.getBlockY() + ":" + scan_loc.getBlockZ());
                    player.sendMessage("Biome type: " + scan_loc.getBlock().getBiome());
                    player.sendMessage("Time of day: " + daynight + " - " + time + " ticks");
                    player.sendMessage("Weather: " + weather);
                    player.sendMessage("Humidity: " + scan_loc.getBlock().getHumidity());
                    player.sendMessage("Temperature: " + scan_loc.getBlock().getTemperature());
                    player.sendMessage("Nearby entities:");
                    for (Map.Entry<EntityType, Integer> entry : scannedentities.entrySet()) {
                        player.sendMessage(entry.getKey().getName() + ": " + entry.getValue());
                    }
                }
            }
        }
    }
}
