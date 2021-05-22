package me.eccentric_nz.tardis.blueprints;

public enum BlueprintBase {

	ADD("tardis.add"), BOOK("tardis.book"), CREATE("tardis.create"), DELETE("tardis.delete"), // don't really need this - just use "tardis.exterminate"?
	ENTER("tardis.enter"), EXTERMINATE("tardis.exterminate"), // don't really need this - just use "tardis.delete"?
	FIND("tardis.find"), HOME("tardis.home"), LIST("tardis.list"), REBUILD("tardis.rebuild"), SAVE("tardis.save"), TAG("tardis.tag"), TIMETRAVEL("tardis.timetravel"), UPDATE("tardis.update"), USE("tardis.use");

	private final String permission;

	BlueprintBase(String permission) {
		this.permission = permission;
	}

	public String getPermission() {
		return permission;
	}
}
