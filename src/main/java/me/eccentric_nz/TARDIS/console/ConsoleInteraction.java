package me.eccentric_nz.TARDIS.console;

import org.bukkit.Material;
import org.bukkit.util.Vector;

public enum ConsoleInteraction {

    // section zero
    HANDBRAKE("Time Rotor Handbrake", new Vector(0.0d, 1d, 2.25d), 0.5f, 0.4f, 1, 0.0f, Material.LEVER, 5002),
    THROTTLE("Flight Speed", new Vector(0.9d, 1.0d, 2.375d), 0.5f, 0.33f, 4, 0.0f, Material.REPEATER, 1004),
    RELATIVITY_DIFFERENTIATOR("Flight Mode Selector", new Vector(0.575d, 1.0d, 1.75d), 0.5f, 0.65f, 1, 0.0f, Material.LEVER, 6001),

    // section one
    WORLD("Environment Selector", new Vector(-0.525d, 1.0d, 0.95d), 0.15f, 0.65f, 1, 60.0f, Material.BAMBOO_BUTTON, 1009),
    MULTIPLIER("Coordinate Increment Modifier", new Vector(-0.4d, 1.0d, 1.2d), 0.15f, 0.65f, 1, 60.0f, Material.BAMBOO_BUTTON, 1009),
    X("X Distance", new Vector(-0.75d, 1.0d, 1.1d), 0.15f, 0.4f, 1, 60.0f, Material.BAMBOO_BUTTON, 1009),
    Z("Z Distance", new Vector(-0.6d, 1.0d, 1.3d), 0.15f, 0.4f, 1, 60.0f, Material.BAMBOO_BUTTON, 1009),
    HELMIC_REGULATOR("Dimension Selector", new Vector(-0.85d, 1d, 1.8d), 0.5f, 0.6f, 0, 60.0f, Material.REPEATER, 2000),

    // section two
    RANDOMISER("Random Location Finder", new Vector(-0.85d, 1d, -0.8125d), 0.25f, 0.33f, 0, 120.0f, Material.BAMBOO_BUTTON, 1001),
    WAYPOINT_SELECTOR("Saves", new Vector(-1.1d, 1d, -0.4d), 0.25f, 0.33f, 0, 120.0f, Material.BAMBOO_BUTTON, 1002),
    FAST_RETURN("Back Button", new Vector(-1.35d, 1d, 0d), 0.25f, 0.33f, 0, 120.0f, Material.BAMBOO_BUTTON, 1003),
    TELEPATHIC_CIRCUIT("Telepathic Circuit", new Vector(-0.55d, 1d, -0.15d), 0.5f, 0.65f, 0, 120.0f, Material.DAYLIGHT_DETECTOR, 1000),

    // section three
    SONIC_DOCK("Sonic Screwdriver Dock", new Vector(0.45d, 1d, -0.65d), 0.5f, 0.65f, 0, 180.0f, Material.FLOWER_POT, 2000),
    DIRECTION("Exterior Directional Control", new Vector(0.125d, 1d, -1.2d), 0.625f, 0.5f, 0, 180.0f, Material.RAIL, 10000),

    // section four
    LIGHT_SWITCH("Interior Light Switch", new Vector(2.475d, 1d, 0.1d), 0.25f, 0.33f, 0, 240.0f, Material.BAMBOO_BUTTON, 1004),
    INTERIOR_LIGHT_LEVEL_SWITCH("Interior Light Level", new Vector(1.8d, 1d, 0.0d), 0.25f, 0.5f, 0, 240.0f, Material.LEVER, 8000),
    EXTERIOR_LAMP_LEVEL_SWITCH("Exterior Lamp Level", new Vector(1.5d, 1d, -0.3d), 0.25f, 0.5f, 0, 240.0f, Material.LEVER, 7000),
    DOOR_TOGGLE("Toggle Wool Switch", new Vector(1.825d, 1d, -1.0d), 0.25f, 0.33f, 0, 240.0f, Material.BAMBOO_BUTTON, 1005),

    // section five
    SCREEN_RIGHT("Coordinates Display", new Vector(1.825d, 1d, 0.95d), 0.5f, 1.1f, 0, 300.0f, Material.MAP, 1000),
    SCREEN_LEFT("Information Display", new Vector(1.525d, 1d, 1.45d), 0.5f, 1.1f, 0, 300.0f, Material.MAP, 1000),
    SCANNER("Exterior Environment Scanner", new Vector(1.825d, 1d, 1.95d), 0.25f, 0.33f, 0, 300.0f, Material.BAMBOO_BUTTON, 1008),
    ARTRON("Artron Energy Button", new Vector(2.125d, 1d, 1.475d), 0.25f, 0.33f, 0, 300.0f, Material.BAMBOO_BUTTON, 1006),
    REBUILD("Chameleon Circuit Re-initialiser", new Vector(2.4d, 1d, 1.025d), 0.25f, 0.33f, 0, 300.0f, Material.BAMBOO_BUTTON, 1007);

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
