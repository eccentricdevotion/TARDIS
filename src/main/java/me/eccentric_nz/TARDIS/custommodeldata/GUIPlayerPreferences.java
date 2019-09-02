package me.eccentric_nz.TARDIS.custommodeldata;

import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import org.bukkit.Material;

public enum GUIPlayerPreferences {

    // Player Prefs Menu
    AUTONOMOUS(20, 0, Material.REPEATER),
    AUTONOMOUS_SIEGE(21, 1, Material.REPEATER),
    AUTO_RESCUE(22, 2, Material.REPEATER),
    BEACON(23, 3, Material.REPEATER),
    DO_NOT_DISTURB(24, 4, Material.REPEATER),
    EMERGENCY_PROGRAM_ONE(25, 5, Material.REPEATER),
    HOSTILE_ACTION_DISPLACEMENT_SYSTEM(26, 6, Material.REPEATER),
    HADS_TYPE(27, 7, Material.REPEATER),
    WHO_QUOTES(28, 8, Material.REPEATER),
    EXTERIOR_RENDERING_ROOM(29, 9, Material.REPEATER),
    INTERIOR_SFX(30, 10, Material.REPEATER),
    SUBMARINE_MODE(31, 11, Material.REPEATER),
    RESOURCE_PACK_SWITCHING(32, 12, Material.REPEATER),
    COMPANION_BUILD(33, 13, Material.REPEATER),
    WOOL_FOR_LIGHTS_OFF(34, 14, Material.REPEATER),
    CONNECTED_TEXTURES(35, 15, Material.REPEATER),
    PRESET_SIGN(36, 16, Material.REPEATER),
    TRAVEL_BAR(37, 17, Material.REPEATER),
    POLICE_BOX_TEXTURES(38, 18, Material.REPEATER),
    MOB_FARMING(39, 19, Material.REPEATER),
    TELEPATHIC_CIRCUIT(40, 20, Material.REPEATER),
    JUNK_TARDIS(41, 21, Material.REPEATER),
    AUTO_POWER_UP(42, 22, Material.REPEATER),
    INTERIOR_HUM_SOUND(58, 23, Material.BOWL),
    HANDBRAKE(1, 24, Material.LEVER),
    TARDIS_MAP(3, 25, Material.MAP),
    ADMIN_MENU(1, 26, Material.NETHER_STAR);

    private final int customModelData;
    private final int slot;
    private final Material material;

    GUIPlayerPreferences(int customModelData, int slot, Material material) {
        this.customModelData = customModelData;
        this.slot = slot;
        this.material = material;
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
        String s = toString();
        if (s.startsWith("TARDIS")) {
            return "TARDIS Map";
        } else if (s.startsWith("HADS")) {
            return "HADS Type";
        } else if (s.endsWith("TARDIS")) {
            return "Junk TARDIS";
        } else if (s.endsWith("RESCUE")) {
            return "Auto-rescue";
        } else if (s.endsWith("SFX")) {
            return "Interior SFX";
        } else {
            return TARDISStringUtils.capitalise(s);
        }
    }
}
