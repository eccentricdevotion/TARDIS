package me.eccentric_nz.TARDIS.planets;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.util.HashMap;

public class TARDISAliasResolver {

	private static final HashMap<String, TARDISPlanet> planets = new HashMap<>();

	public static String getWorldAlias(World world) {
		return getWorldAlias(world.getName());
	}

	public static String getWorldAlias(String world) {
		return TARDIS.plugin.getPlanetsConfig().getString("planets." + world + ".alias", world);
	}

	public static World getWorldFromAlias(String alias) {
		World world = Bukkit.getServer().getWorld(alias);
		if (world != null) {
			return world;
		} else {
			for (TARDISPlanet planet : planets.values()) {
				if (planet.getAlias().equalsIgnoreCase(alias)) {
					return planet.getWorld();
				}
			}
		}
		return null;
	}

	public static String getWorldNameFromAlias(String alias) {
		World world = Bukkit.getServer().getWorld(alias);
		if (world != null) {
			return alias;
		} else {
			for (TARDISPlanet planet : planets.values()) {
				if (planet.getAlias().equalsIgnoreCase(alias)) {
					return planet.getName();
				}
			}
		}
		return "";
	}

	public static void createAliasMap() {
		for (String s : TARDIS.plugin.getPlanetsConfig().getConfigurationSection("planets").getKeys(false)) {
			World world = Bukkit.getServer().getWorld(s);
			if (world != null) {
				String alias = TARDIS.plugin.getPlanetsConfig().getString("planets." + s + ".alias", s);
				TARDISPlanet tp = new TARDISPlanet();
				tp.setAlias(!alias.isEmpty() ? alias : s);
				tp.setName(s);
				tp.setWorld(world);
				if (!alias.isEmpty()) {
					planets.put(alias, tp);
				} else {
					planets.put(s, tp);
				}
			}
		}
	}

	public static HashMap<String, TARDISPlanet> getPlanets() {
		return planets;
	}
}
