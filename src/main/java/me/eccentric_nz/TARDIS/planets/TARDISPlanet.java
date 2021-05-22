package me.eccentric_nz.tardis.planets;

import org.bukkit.World;

public class TARDISPlanet {

	private String alias;
	private String name;
	private World world;

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public World getWorld() {
		return world;
	}

	public void setWorld(World world) {
		this.world = world;
	}
}
