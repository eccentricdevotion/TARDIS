package me.eccentric_nz.TARDIS.display;

public enum TARDISDisplayType {

    ALL("&6X&7%X% &6Y&7%Y% &6Z&7%Z% &6F&7%FACING% (%FACING_XZ%) %TARGET_BLOCK%"),
    BIOME("&6B&7%BIOME%"),
    COORDS("&6X&7%X% &6Y&7%Y% &6Z&7%Z%"),
    DIRECTION("&6F&7%FACING% (%FACING_XZ%)"),
    TARGET_BLOCK("&6T&7%TARGET_BLOCK%");

    private final String format;

    TARDISDisplayType(String format) {
        this.format = format;
    }

    public String getFormat() {
        return format;
    }
}
