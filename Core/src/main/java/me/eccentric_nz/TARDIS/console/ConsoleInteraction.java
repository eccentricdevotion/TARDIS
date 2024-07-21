package me.eccentric_nz.TARDIS.console;

import org.bukkit.Material;
import org.bukkit.util.Vector;

public enum ConsoleInteraction {

    // section zero
    SONIC_DOCK("Sonic Screwdriver Dock", new Vector(0.5d, 0.75d, 1.625d), 0.5f, 0.65f, 0, 0.0f, Material.FLOWER_POT, 2000),
    DIRECTION("Exterior Directional Control", new Vector(0.75d, 0.75d, 2.25d), 0.625f, 0.5f, 0, 0.0f, Material.RAIL, 10000),

    // section one
    LIGHT_SWITCH("Interior Light Switch", new Vector(-1.45d, 0.75d, 0.9d), 0.25f, 0.33f, 0, 60.0f, Material.BAMBOO_BUTTON, 1004),
    INTERIOR_LIGHT_LEVEL_SWITCH("Interior Light Level", new Vector(-0.85d, 0.75d, 1.05d), 0.25f, 0.5f, 0, 60.0f, Material.LEVER, 8000),
    EXTERIOR_LAMP_LEVEL_SWITCH("Exterior Lamp Level", new Vector(-0.65d, 0.75d, 1.45d), 0.25f, 0.5f, 0, 60.0f, Material.LEVER, 7000),
    DOOR_TOGGLE("Toggle Wool Switch", new Vector(-0.775d, 0.75d, 2.0d), 0.25f, 0.33f, 0, 60.0f, Material.BAMBOO_BUTTON, 1005),

    // section two
    SCREEN_RIGHT("Coordinates Display", new Vector(-0.85d, 0.75d, 0.1d), 0.5f, 1.1f, 0, 120.0f, Material.MAP, 1000),
    SCREEN_LEFT("Information Display", new Vector(-0.6d, 0.75d, -0.4d), 0.5f, 1.1f, 0, 120.0f, Material.MAP, 1000),
    SCANNER("Exterior Environment Scanner", new Vector(-0.875d, 0.75d, -0.875d), 0.25f, 0.33f, 0, 120.0f, Material.BAMBOO_BUTTON, 1008),
    ARTRON("Artron Energy Button", new Vector(-1.15d, 0.75d, -0.4d), 0.25f, 0.33f, 0, 120.0f, Material.BAMBOO_BUTTON, 1006),
    REBUILD("Chameleon Circuit Re-initialiser", new Vector(-1.45d, 0.75d, 0.05d), 0.25f, 0.33f, 0, 120.0f, Material.BAMBOO_BUTTON, 1007),

    // section three
    HANDBRAKE("Time Rotor Handbrake", new Vector(0.95d, 0.75d, -1.2d), 0.5f, 0.4f, 1, 180.0f, Material.LEVER, 5002),
    THROTTLE("Flight Speed", new Vector(-0.0d, 0.75d, -1.35d), 0.5f, 0.33f, 4, 180.0f, Material.REPEATER, 1004),
    RELATIVITY_DIFFERENTIATOR("Flight Mode Selector", new Vector(0.45d, 0.75d, -0.75d), 0.5f, 0.65f, 1, 180.0f, Material.LEVER, 6001),

    // section four
    RANDOMISER("Random Location Finder", new Vector(2.35d, 0.75d, 0.025d), 0.25f, 0.33f, 0, 240.0f, Material.BAMBOO_BUTTON, 1001),
    WAYPOINT_SELECTOR("Saves", new Vector(2.1d, 0.75d, -0.45d), 0.25f, 0.33f, 0, 240.0f, Material.BAMBOO_BUTTON, 1002),
    FAST_RETURN("Back Button", new Vector(1.8d, 0.75d, -0.9d), 0.25f, 0.33f, 0, 240.0f, Material.BAMBOO_BUTTON, 1003),
    TELEPATHIC_CIRCUIT("Telepathic Circuit", new Vector(1.575d, 0.75d, -0.05d), 0.5f, 0.65f, 0, 240.0f, Material.DAYLIGHT_DETECTOR, 1000),

    // section five

    WORLD("Environment Selector", new Vector(1.425d, 0.75d, 1.15d), 0.15f, 0.65f, 1, 300.0f, Material.BAMBOO_BUTTON, 1009),
    MULTIPLIER("Coordinate Increment Modifier", new Vector(1.55d, 0.75d, 0.925d), 0.15f, 0.65f, 1, 300.0f, Material.BAMBOO_BUTTON, 1009),
    X("X Distance", new Vector(1.625d, 0.75d, 1.275d), 0.15f, 0.4f, 1, 300.0f, Material.BAMBOO_BUTTON, 1009),
    Z("Z Distance", new Vector(1.75d, 0.75d, 1.05), 0.15f, 0.4f, 1, 300.0f, Material.BAMBOO_BUTTON, 1009),
    HELMIC_REGULATOR("Helmic Regulator", new Vector(2.25d, 0.75d, 1.075d), 0.5f, 0.6f, 0, 300.0f, Material.ARMADILLO_SCUTE, 2000),

    // manual flight (includes helmic regulator above)
    ASTROSEXTANT_RECTIFIER("Astrosextant Rectifier", new Vector(0.55d, 0.75d, -1.35d), 0.15f, 0.35f, 0, 180.0f, Material.ARMADILLO_SCUTE, 1000),
    GRAVITIC_ANOMALISER("Gravitic Anomaliser", new Vector(0.125d, 0.75d, 1.8d), 0.15f, 0.5f, 0, 0.0f, Material.ARMADILLO_SCUTE, 1001),
    ABSOLUTE_TESSERACTULATOR("Absolute Tesseractulator", new Vector(-1.15d, 0.75d, 0.825d), 0.15f, 0.5f, 0, 60.0f, Material.ARMADILLO_SCUTE, 1002);

    private final String alternateName;
    private final Vector relativePosition;
    private final float width;
    private final float height;
    private final int defaultState;
    private final float yaw;
    private final Material material;
    private final int customModelData;

    ConsoleInteraction(String alternateName, Vector relativePosition, float width, float height, int defaultState, float yaw, Material material, int customModelData) {
        this.alternateName = alternateName;
        this.relativePosition = relativePosition;
        this.width = width;
        this.height = height;
        this.defaultState = defaultState;
        this.yaw = yaw;
        this.material = material;
        this.customModelData = customModelData;
    }

    public String getAlternateName() {
        return alternateName;
    }

    public Vector getRelativePosition() {
        return relativePosition;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public int getDefaultState() {
        return defaultState;
    }

    public float getYaw() {
        return yaw;
    }

    public Material getMaterial() {
        return material;
    }

    public int getCustomModelData() {
        return customModelData;
    }
}
