package me.eccentric_nz.TARDIS.info;

import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import org.bukkit.Material;

public enum TISCategory {

    CONSOLE_BLOCKS(Material.SHELTER_POTTERY_SHERD, "Functional blocks found~in the TARDIS console", true),
    UPDATEABLE_BLOCKS(Material.ANGLER_POTTERY_SHERD, "Updateable blocks for~room spawns", true),
    COMPONENTS(Material.BLADE_POTTERY_SHERD, "TARDIS circuits needed for~TARDIS features and crafting"),
    ITEMS(Material.BREWER_POTTERY_SHERD, "Things you can use~with your TARDIS"),
    SONIC_UPGRADES(Material.HOWL_POTTERY_SHERD, "Upgrades for your~Sonic Screwdriver"),
    SONIC_COMPONENTS(Material.EXPLORER_POTTERY_SHERD, "Circuits to extend the~Sonic Screwdriver's functions"),
    DISKS(Material.PLENTY_POTTERY_SHERD, "Save locations, biomes,~players and areas~for time travel"),
    CONSOLES(Material.FRIEND_POTTERY_SHERD, "All the TARDIS~desktop themes", true),
    ROOMS(Material.PRIZE_POTTERY_SHERD, "All the TARDIS rooms", true),
    FOOD(Material.MINER_POTTERY_SHERD, "The Doctor's~favourite foods"),
    ACCESSORIES(Material.SKULL_POTTERY_SHERD, "Functional and~cosmetic items"),
    TIME_TRAVEL(Material.BURN_POTTERY_SHERD, "How to fly~the TARDIS"),
    PLANETS(Material.DANGER_POTTERY_SHERD, "Visit planets from~the Whoniverse");

    private final String name;
    private final Material icon;
    private final int customModelData;
    private final String lore;
    private final boolean firstLevel;

    TISCategory(Material icon, String lore) {
        this.name = TARDISStringUtils.capitalise(toString());
        this.icon = icon;
        this.customModelData = 1;
        this.lore = lore;
        this.firstLevel = false;
    }

    TISCategory(Material icon, String lore, boolean firstLevel) {
        this.name = TARDISStringUtils.capitalise(toString());
        this.icon = icon;
        this.customModelData = 1;
        this.lore = lore;
        this.firstLevel = firstLevel;
    }

    public String getName() {
        return name;
    }

    public Material getIcon() {
        return icon;
    }

    public int getCustomModelData() {
        return customModelData;
    }

    public String getLore() {
        return lore;
    }

    public boolean isFirstLevel() {
        return firstLevel;
    }
}
