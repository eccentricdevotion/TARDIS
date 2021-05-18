package me.eccentric_nz.TARDIS.blueprints;

public enum BlueprintConsole {

	ARS("tardis.ars"),
	BIGGER("tardis.bigger"),
	CORAL("tardis.coral"),
	CUSTOM("tardis.custom"),
	DELUXE("tardis.deluxe"),
	ELEVENTH("tardis.eleventh"),
	ENDER("tardis.ender"),
	LEGACY_BIGGER("tardis.legacy_bigger"),
	LEGACY_BUDGET("tardis.legacy_budget"),
	LEGACY_DELUXE("tardis.legacy_deluxe"),
	LEGACY_ELEVENTH("tardis.legacy_eleventh"),
	LEGACY_REDSTONE("tardis.legacy_redstone"),
	MASTER("tardis.master"),
	PLANK("tardis.plank"),
	PYRAMID("tardis.pyramid"),
	REDSTONE("tardis.redstone"),
	ROTOR("tardis.rotor"),
	STEAMPUNK("tardis.steampunk"),
	TOM("tardis.tom"),
	TWELFTH("tardis.twelfth"),
	WAR("tardis.war");

	private final String permission;

	BlueprintConsole(String permission) {
		this.permission = permission;
	}

	public String getPermission() {
		return permission;
	}
}
