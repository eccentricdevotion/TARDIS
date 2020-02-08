package me.eccentric_nz.TARDIS.enumeration;

import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import org.bukkit.Material;

public enum UPDATEABLE {

    // "artron", "back", "button", "chameleon", "control", "direction", "door", "forcefield", "handbrake", "save-sign", "scanner", "terminal", "world-repeater", "x-repeater", "y-repeater", "z-repeater"

    ADVANCED(false, true, Material.JUKEBOX),
    ARS(true, true, Material.ELYTRA),
    ARTRON(false, true, Material.ELYTRA),
    BACK(true, true, Material.ELYTRA),
    BACKDOOR(false, false, Material.IRON_DOOR),
    BEACON(false, false, Material.BEACON),
    BUTTON(true, true, Material.ELYTRA),
    CHAMELEON(true, true, Material.OAK_WALL_SIGN),
    CONDENSER(true, true, Material.CHEST),
    CONTROL(true, true, Material.ELYTRA),
    CREEPER(false, false, Material.ELYTRA),
    DIRECTION(false, true, Material.ELYTRA),
    DISPENSER(true, true, Material.DISPENSER),
    DOOR(false, true, Material.IRON_DOOR),
    EPS(false, false, Material.SPAWNER),
    FARM(false, false, Material.ELYTRA),
    FLIGHT(true, true, Material.ELYTRA),
    FORCEFIELD(true, true, Material.ELYTRA),
    FRAME(false, true, Material.ELYTRA),
    GENERATOR(false, true, Material.FLOWER_POT),
    HANDBRAKE(false, true, Material.LEVER),
    HINGE(false, false, Material.ELYTRA),
    INFO(true, true, Material.ELYTRA),
    KEYBOARD(false, false, Material.OAK_WALL_SIGN),
    LIGHT(true, true, Material.ELYTRA),
    RAIL(false, false, Material.OAK_FENCE),
    SAVE_SIGN(true, true, Material.OAK_WALL_SIGN),
    SCANNER(true, true, Material.ELYTRA),
    SIEGE(true, false, Material.ELYTRA),
    STABLE(false, false, Material.ELYTRA),
    STALL(false, false, Material.ELYTRA),
    STORAGE(true, false, Material.NOTE_BLOCK),
    TELEPATHIC(false, true, Material.DAYLIGHT_DETECTOR),
    TEMPORAL(true, true, Material.ELYTRA),
    TERMINAL(true, true, Material.ELYTRA),
    TOGGLE_WOOL(true, true, Material.ELYTRA),
    VAULT(false, false, Material.CHEST),
    VILLAGE(false, false, Material.ELYTRA),
    WORLD_REPEATER(false, true, Material.REPEATER),
    X_REPEATER(false, true, Material.REPEATER),
    Y_REPEATER(false, true, Material.REPEATER),
    Z_REPEATER(false, true, Material.REPEATER),
    ZERO(true, false, Material.ELYTRA);

    private final boolean control;
    private final boolean secondary;
    private final Material material;

    UPDATEABLE(boolean control, boolean secondary, Material material) {
        this.control = control;
        this.secondary = secondary;
        this.material = material;
    }

    public String getName() {
        return TARDISStringUtils.toDashedLowercase(toString());
    }

    public boolean isControl() {
        return control;
    }

    public boolean isSecondary() {
        return secondary;
    }

    public Material getMaterial() {
        return material;
    }
}
