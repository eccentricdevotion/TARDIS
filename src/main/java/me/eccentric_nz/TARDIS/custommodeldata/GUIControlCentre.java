package me.eccentric_nz.TARDIS.custommodeldata;

import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import org.bukkit.Material;

import java.util.Locale;

public enum GUIControlCentre {

    // CONTROL CENTRE
    RANDOM_LOCATION(10000002, 0, Material.BOWL),
    SAVED_LOCATIONS(10000003, 9, Material.BOWL),
    FAST_RETURN(10000004, 18, Material.BOWL),
    TARDIS_AREAS(10000005, 27, Material.BOWL),
    DESTINATION_TERMINAL(10000006, 36, Material.BOWL),
    ARCHITECTURAL_RECONFIGURATION_SYSTEM(10000007, 2, Material.BOWL),
    DESKTOP_THEME(10000008, 11, Material.BOWL),
    POWER_ON(10000016, 20, Material.REPEATER),
    POWER_OFF(20000016, 20, Material.REPEATER),
    LIGHT_SWITCH_ON(10000017, 29, Material.REPEATER),
    LIGHT_SWITCH_OFF(20000017, 29, Material.REPEATER),
    TOGGLE_BLOCKS_BEHIND_DOOR_ON(10000018, 38, Material.REPEATER),
    TOGGLE_BLOCKS_BEHIND_DOOR_OFF(20000018, 38, Material.REPEATER),
    TARDIS_MAP(10000009, 47, Material.BOWL),
    CHAMELEON_CIRCUIT(10000010, 4, Material.BOWL),
    SIEGE_MODE_ON(10000018, 13, Material.REPEATER),
    SIEGE_MODE_OFF(20000018, 13, Material.REPEATER),
    HIDE(10000011, 22, Material.BOWL),
    REBUILD(10000012, 31, Material.BOWL),
    DIRECTION(10000013, 40, Material.BOWL),
    TEMPORAL_LOCATOR(10000014, 49, Material.BOWL),
    ARTRON_ENERGY_LEVELS(10000015, 6, Material.BOWL),
    SCANNER(10000016, 15, Material.BOWL),
    TARDIS_INFORMATION_SYSTEM(10000017, 24, Material.BOWL),
    ZERO_ROOM_TRANSMAT(10000018, 8, Material.BOWL),
    PLAYER_PREFERENCES(10000019, 17, Material.BOWL),
    COMPANION_MENU(10000020, 26, Material.BOWL);

    private final int customModelData;
    private final int slot;
    private final Material material;
    private final String name;

    GUIControlCentre(int customModelData, int slot, Material material) {
        this.customModelData = customModelData;
        this.slot = slot;
        this.material = material;
        name = setName(this);
    }

    public static GUIControlCentre getByName(String name) {
        String processed = name.replace(" ", "_").toUpperCase(Locale.ENGLISH);
        return GUIControlCentre.valueOf(processed);
    }

    private String setName(GUIControlCentre item) {
        String s = item.toString();
        return TARDISStringUtils.sentenceCase(s);
    }

    public int getCustomModelData() {
        return customModelData;
    }

    public int getSlot() {
        return slot;
    }

    public Material getMaterial() {
        return material;
    }

    public String getName() {
        return name;
    }
}
