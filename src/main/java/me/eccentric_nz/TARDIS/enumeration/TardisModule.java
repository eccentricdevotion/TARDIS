package me.eccentric_nz.TARDIS.enumeration;

public enum TardisModule {

    BLASTER("Sonic Blaster", "#00AA00"),
    BLUEPRINTS("TARDIS Blueprints", "#5555FF"),
    CHEMISTRY("TARDIS Chemistry", "#FF55FF"),
    DEBUG("TARDIS", "#FF55FF"),
    DYNMAP("TARDIS Dynmap", "#55FF55"),
    EMERGENCY_PROGRAM("Emergency Program One", "#C70039"),
    HANDLES("Handles", "#00AAAA"),
    HELPER("TARDIS Helper", "#55FFFF"),
    MONSTERS("Weeping Angels", "#FF5555"),
    SHOP("TARDIS Shop", "#FFFF55"),
    TARDIS("TARDIS", "#FFAA00"),
    TRANSLATOR("Universal Translator", "#0000AA"),
    VORTEX_MANIPULATOR("Vortex Manipulator", "#AA00AA"),
    WARNING("TARDIS WARNING", "#FF5555"),
    SEVERE("TARDIS ERROR", "#AA0000"),
    HELPER_WARNING("TARDIS Helper WARNING", "#FF5555"),
    HELPER_SEVERE("TARDIS Helper ERROR", "#AA0000");

    private final String name;
    private final String hex;

    TardisModule(String name, String hex) {
        this.name = name;
        this.hex = hex;
    }

    public String getName() {
        return name;
    }

    public String getHex() {
        return hex;
    }
}
