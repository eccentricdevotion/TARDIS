package me.eccentric_nz.TARDIS.console;

import org.bukkit.util.Vector;

public enum ConsoleInteraction {

    // section zero
    HANDBRAKE("Time Rotor Handbrake", new Vector(0.0d, 1d, 2.25d), 0.5f, 0.4f, 1),
    THROTTLE("Flight Speed", new Vector(0.9d, 1.0d, 2.375d), 0.5f, 0.33f, 4),
    RELATIVITY_DIFFERENTIATOR("Flight Mode Selector", new Vector(0.575d, 1.0d, 1.75d), 0.5f, 0.65f, 1),

    // section one
    WORLD("Environment Selector", new Vector(-0.525d, 1.0d, 0.95d), 0.15f, 0.65f, 1),
    MULTIPLIER("Coordinate Increment Modifier", new Vector(-0.4d, 1.0d, 1.2d), 0.15f, 0.65f, 1),
    X("X Distance", new Vector(-0.75d, 1.0d, 1.1d), 0.15f, 0.4f, 1),
    Z("Z Distance", new Vector(-0.6d, 1.0d, 1.3d), 0.15f, 0.4f, 1),
    HELMIC_REGULATOR("Dimension Selector", new Vector(-0.85d, 1d, 1.8d), 0.5f, 0.6f, 0),

    // section two
    RANDOMISER("Random Location Finder", new Vector(-0.85d, 1d, -0.8125d), 0.25f, 0.33f, 0),
    WAYPOINT_SELECTOR("Saves", new Vector(-1.1d, 1d, -0.4d), 0.25f, 0.33f, 0),
    FAST_RETURN("Back Button", new Vector(-1.35d, 1d, 0d), 0.25f, 0.33f, 0),
    TELEPATHIC_CIRCUIT("Telepathic Circuit", new Vector(-0.55d, 1d, -0.15d), 0.5f, 0.65f, 0),

    // section three
    SONIC_DOCK("Sonic Screwdriver Dock", new Vector(0.45d, 1d, -0.65d), 0.5f, 0.65f, 0),
    DIRECTION("Exterior Directional Control", new Vector(0.125d, 1d, -1.2d), 0.625f, 0.5f, 0),

    // section four
    LIGHT_SWITCH("Interior Light Switch", new Vector(2.475d, 1d, 0.1d), 0.25f, 0.33f, 0),
    INTERIOR_LIGHT_LEVEL_SWITCH("Interior Light Level", new Vector(1.8d, 1d, 0.0d), 0.25f, 0.5f, 0),
    EXTERIOR_LAMP_LEVEL_SWITCH("Exterior Lamp Level", new Vector(1.5d, 1d, -0.3d), 0.25f, 0.5f, 0),
    DOOR_TOGGLE("Toggle Wool Switch", new Vector(1.825d, 1d, -1.0d), 0.25f, 0.33f, 0),

    // section five
    SCREEN_RIGHT("Coordinates Display", new Vector(1.825d, 1d, 0.95d), 0.5f, 1.1f, 0),
    SCREEN_LEFT("Information Display", new Vector(1.525d, 1d, 1.45d), 0.5f, 1.1f, 0),
    SCANNER("Exterior Environment Scanner", new Vector(1.825d, 1d, 1.95d), 0.25f, 0.33f, 0),
    ARTRON("Artron Energy Button", new Vector(2.125d, 1d, 1.475d), 0.25f, 0.33f, 0),
    REBUILD("Chameleon Circuit Re-initialiser", new Vector(2.4d, 1d, 1.025d), 0.25f, 0.33f, 0);

    private final String alternateName;
    private final Vector relativePosition;
    private final float width;
    private final float height;
    private final int defaultState;

    ConsoleInteraction(String alternateName, Vector relativePosition, float width, float height, int defaultState) {
        this.alternateName = alternateName;
        this.relativePosition = relativePosition;
        this.width = width;
        this.height = height;
        this.defaultState = defaultState;
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
}
