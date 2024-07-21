package me.eccentric_nz.TARDIS.info;

import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;

public enum TISCategory {

    CONSOLE_BLOCKS("Functional blocks found~in the TARDIS console", true),
    UPDATEABLE_BLOCKS("Updateable blocks for~room spawns", true),
    COMPONENTS("TARDIS circuits needed for~TARDIS features and crafting"),
    ITEMS("Things you can use~with your TARDIS"),
    SONIC_UPGRADES("Upgrades for your~Sonic Screwdriver"),
    SONIC_COMPONENTS("Circuits to extend the~Sonic Screwdriver's functions"),
    DISKS("Save locations, biomes,~players and areas~for time travel"),
    CONSOLES("All the TARDIS~desktop themes", true),
    ROOMS("All the TARDIS rooms", true),
    FOOD("The Doctor's~favourite foods"),
    ACCESSORIES("Functional and~cosmetic items"),
    TIME_TRAVEL("How to fly~the TARDIS", true),
    PLANETS("Visit planets from~the Whoniverse"),
    MONSTERS("Monsters from~the Whoniverse");

    private final String name;
    private final String lore;
    private final boolean firstLevel;

    TISCategory(String lore) {
        this.name = TARDISStringUtils.capitalise(toString());
        this.lore = lore;
        this.firstLevel = false;
    }

    TISCategory(String lore, boolean firstLevel) {
        this.name = TARDISStringUtils.capitalise(toString());
        this.lore = lore;
        this.firstLevel = firstLevel;
    }

    public String getName() {
        return name;
    }

    public String getLore() {
        return lore;
    }
    public boolean isFirstLevel() {
        return firstLevel;
    }
}
