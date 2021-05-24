/*
 * Copyright (C) 2021 eccentric_nz
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
package me.eccentric_nz.tardis.listeners;

import me.eccentric_nz.tardis.TARDISConstants;
import me.eccentric_nz.tardis.TARDISPlugin;
import me.eccentric_nz.tardis.planets.TARDISAngelsAPI;
import me.eccentric_nz.tardis.planets.TARDISBiome;
import me.eccentric_nz.tardis.utility.TARDISStaticUtils;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Bee;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author eccentric_nz
 */
public class TARDISSpawnListener implements Listener {

	private final TARDISPlugin plugin;
	private final List<SpawnReason> good_spawns = new ArrayList<>();
	private final List<TARDISBiome> biomes = new ArrayList<>();

	public TARDISSpawnListener(TARDISPlugin plugin) {
		this.plugin = plugin;
		good_spawns.add(SpawnReason.BEEHIVE);
		good_spawns.add(SpawnReason.BREEDING);
		good_spawns.add(SpawnReason.BUILD_IRONGOLEM);
		good_spawns.add(SpawnReason.BUILD_SNOWMAN);
		good_spawns.add(SpawnReason.BUILD_WITHER);
		good_spawns.add(SpawnReason.CURED);
		good_spawns.add(SpawnReason.CUSTOM);
		good_spawns.add(SpawnReason.DISPENSE_EGG);
		good_spawns.add(SpawnReason.EGG);
		good_spawns.add(SpawnReason.ENDER_PEARL);
		good_spawns.add(SpawnReason.INFECTION);
		good_spawns.add(SpawnReason.JOCKEY);
		good_spawns.add(SpawnReason.LIGHTNING);
		good_spawns.add(SpawnReason.MOUNT);
		good_spawns.add(SpawnReason.NETHER_PORTAL);
		good_spawns.add(SpawnReason.OCELOT_BABY);
		good_spawns.add(SpawnReason.RAID);
		good_spawns.add(SpawnReason.REINFORCEMENTS);
		good_spawns.add(SpawnReason.SHEARED);
		good_spawns.add(SpawnReason.SHOULDER_ENTITY);
		good_spawns.add(SpawnReason.SILVERFISH_BLOCK);
		good_spawns.add(SpawnReason.SLIME_SPLIT);
		good_spawns.add(SpawnReason.SPAWNER_EGG);
		good_spawns.add(SpawnReason.VILLAGE_DEFENSE);
		good_spawns.add(SpawnReason.VILLAGE_INVASION);
		biomes.add(TARDISBiome.DEEP_OCEAN);
		biomes.add(TARDISBiome.END_BARRENS);
		biomes.add(TARDISBiome.END_HIGHLANDS);
		biomes.add(TARDISBiome.END_MIDLANDS);
		biomes.add(TARDISBiome.MUSHROOM_FIELD_SHORE);
		biomes.add(TARDISBiome.MUSHROOM_FIELDS);
		biomes.add(TARDISBiome.NETHER_WASTES);
		biomes.add(TARDISBiome.SOUL_SAND_VALLEY);
		biomes.add(TARDISBiome.CRIMSON_FOREST);
		biomes.add(TARDISBiome.WARPED_FOREST);
		biomes.add(TARDISBiome.BASALT_DELTAS);
		biomes.add(TARDISBiome.SMALL_END_ISLANDS);
		biomes.add(TARDISBiome.THE_END);
	}

	/**
	 * Listens for entity spawn events. If WorldGuard is enabled it blocks mob-spawning inside the tardis, so this
	 * checks to see if we are doing the spawning and un-cancels WorldGuard's setCancelled(true).
	 * <p>
	 * It also prevents natural mob spawning in the tardis DEEP_OCEAN biome.
	 *
	 * @param event A creature spawn event
	 */
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onEntitySpawn(CreatureSpawnEvent event) {
		SpawnReason spawnReason = event.getSpawnReason();
		Location l = event.getLocation();
		if (Objects.requireNonNull(l.getWorld()).getName().contains("tardis")) {
			if (event.getEntityType().equals(EntityType.ARMOR_STAND)) {
				return;
			}
			if (plugin.isTardisSpawn()) {
				plugin.setTardisSpawn(false);
				return;
			}
			if (spawnReason.equals(SpawnReason.BEEHIVE) || (spawnReason.equals(SpawnReason.DEFAULT) && event.getEntity() instanceof Bee)) {
				int random = TARDISConstants.RANDOM.nextInt(1200) + 1200;
				((Bee) event.getEntity()).setCannotEnterHiveTicks(random);
				return;
			}
			// if not an allowable tardis spawn reason, cancel
			if (!good_spawns.contains(spawnReason)) {
				event.setCancelled(true);
			}
			if (spawnReason.equals(SpawnReason.BUILD_SNOWMAN) && plugin.getPM().isPluginEnabled("TARDISWeepingAngels")) {
				if (TARDISConstants.RANDOM.nextInt(100) < 3) {
					// spawn a Dalek instead
					LivingEntity le = (LivingEntity) l.getWorld().spawnEntity(l, EntityType.SKELETON);
					TARDISAngelsAPI.getAPI(plugin).setDalekEquipment(le, false);
					plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> event.getEntity().remove(), 2L);
				}
			}
		} else {
			// only if configured
			if (!plugin.getConfig().getBoolean("police_box.set_biome")) {
				return;
			}
			if (!event.getEntityType().isAlive()) {
				return;
			}
			// only natural spawning
			if (!spawnReason.equals(SpawnReason.NATURAL)) {
				return;
			}
			// only in DEEP_OCEAN, MUSHROOM_ISLAND, NETHER & THE END
			if (!biomes.contains(TARDISStaticUtils.getBiomeAt(l))) {
				return;
			}
			// only monsters
			if (!TARDISConstants.MONSTER_TYPES.contains(event.getEntity().getType())) {
				return;
			}
			// always deny MUSHROOM, HELL and SKY biomes
			switch (TARDISStaticUtils.getBiomeAt(l).name()) {
				case "MUSHROOM_FIELDS":
				case "NETHER_WASTES":
				case "SOUL_SAND_VALLEY":
				case "CRIMSON_FOREST":
				case "WARPED_FOREST":
				case "BASALT_DELTAS":
					if (!event.getEntity().getType().equals(EntityType.SKELETON)) {
						event.setCancelled(true);
						return;
					}
					return;
				case "THE_END":
					if (!event.getEntity().getType().equals(EntityType.ENDERMAN)) {
						event.setCancelled(true);
						return;
					}
					break;
				case "MUSHROOM_FIELD_SHORE":
					if (!event.getEntity().getType().equals(EntityType.SQUID)) {
						event.setCancelled(true);
						return;
					}
					break;
				default:
					break;
			}
			// only tardis locations
			if (isTARDISBiome(l)) {
				event.setCancelled(true);
			}
		}
	}

	private boolean isTARDISBiome(Location l) {
		/*
		 * Looping through all the TARDISes on the server and checking
		 * a 3x3 area around their location is too expensive, instead
		 * we'll check the specific area around the location and if a 3x3
		 * DEEP_OCEAN biome is found then we'll deny the spawn.
		 */
		int found = 0, three_by_three = 0;
		int x = l.getBlockX();
		int z = l.getBlockZ();
		World w = l.getWorld();
		/*
		 * A 7x7 block area is sure to encapsulate a 3x3 one if the mob
		 * spawns anywhere in the 3x3 square. Caveat: This relies on the
		 * premise that there is at least 1 block between TARDISes.
		 */
		for (int col = -3; col < 4; col++) {
			for (int row = -3; row < 4; row++) {
				assert w != null;
				TARDISBiome b = TARDISStaticUtils.getBiomeAt(w.getBlockAt(x + col, 64, z + row).getLocation());
				if (b.equals(TARDISBiome.DEEP_OCEAN)) {
					found++;
				}
				if (found < 3 && !b.equals(TARDISBiome.DEEP_OCEAN)) {
					// reset count - not three in a row
					found = 0;
				}
				if (found == 3 && !b.equals(TARDISBiome.DEEP_OCEAN)) {
					// found 3 consecutive blocks in a row, increment 3x3 row count
					three_by_three++;
					// reset count
					found = 0;
					// skip the rest of the row
					break;
				}
			}
		}
		// check if location is in region
		return three_by_three == 3;
	}
}
