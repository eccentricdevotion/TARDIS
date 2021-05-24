package me.eccentric_nz.tardis.database.data;

import me.eccentric_nz.tardis.enumeration.PRESET;

import java.util.UUID;

public class StandbyData {

	final int level;
	final UUID uuid;
	final boolean hidden;
	final boolean lightsOn;
	final PRESET preset;
	final boolean lanterns;

	public StandbyData(int level, UUID uuid, boolean hidden, boolean lightsOn, PRESET preset, boolean lanterns) {
		this.level = level;
		this.uuid = uuid;
		this.hidden = hidden;
		this.lightsOn = lightsOn;
		this.preset = preset;
		this.lanterns = lanterns;
	}

	public int getLevel() {
		return level;
	}

	public UUID getUuid() {
		return uuid;
	}

	public boolean isHidden() {
		return hidden;
	}

	public boolean isLightsOn() {
		return lightsOn;
	}

	public PRESET getPreset() {
		return preset;
	}

	public boolean isLanterns() {
		return lanterns;
	}
}
