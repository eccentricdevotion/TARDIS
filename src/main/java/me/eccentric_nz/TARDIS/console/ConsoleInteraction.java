package me.eccentric_nz.TARDIS.console;

import org.bukkit.util.Vector;

public enum ConsoleInteraction {

    // section zero
    HANDBRAKE("Time Rotor Handbrake", new Vector(0.0d, 1d, 2.25d), 0.5f, 0.4f),
    THROTTLE("Flight Speed", new Vector(0.9d, 1.0d, 2.375d), 0.5f, 0.33f),
    RELATIVITY_DIFFERENTIATOR("Flight Mode Selector", new Vector(0.575d, 1.0d, 1.75d), 0.5f, 0.65f),

    // section one
    WORLD("Dimension Selector", new Vector(-0.525d, 1.0d, 0.95d), 0.15f, 0.65f),
    MULTIPLIER("Coordinate Increment Modifier", new Vector(-0.4d, 1.0d, 1.2d), 0.15f, 0.65f),
    X("X Distance", new Vector(-0.75d, 1.0d, 1.1d), 0.15f, 0.4f),
    Z("Z Distance", new Vector(-0.6d, 1.0d, 1.3d), 0.15f, 0.4f),
    HELMIC_REGULATOR("Dimension Selector", new Vector(-0.85d, 1d, 1.8d), 0.5f, 0.6f),

    // section two
    RANDOMISER("Random Location Finder", new Vector(-0.85d, 1d, -0.8125d), 0.25f, 0.33f),
    WAYPOINT_SELECTOR("Saves", new Vector(-1.1d, 1d, -0.4d), 0.25f, 0.33f),
    FAST_RETURN("Back Button", new Vector(-1.35d, 1d, 0d), 0.25f, 0.33f),
    TELEPATHIC_CIRCUIT("Telepathic Circuit", new Vector(-0.55d, 1d, -0.15d), 0.5f, 0.65f),

    // section three
    SONIC_DOCK("Sonic Screwdriver Dock", new Vector(0.45d, 1d, -0.65d), 0.5f, 0.65f),
    DIRECTION("Exterior Directional Control", new Vector(0.125d, 1d, -1.2d), 0.625f, 0.5f),

    // section four
    LIGHT_SWITCH("Interior Light Switch", new Vector(2.475d, 1d, 0.1d), 0.25f, 0.33f),
    INTERIOR_LIGHT_LEVEL_SWITCH("Interior Light Level", new Vector(1.8d, 1d, 0.0d), 0.25f, 0.5f),
    EXTERIOR_LAMP_LEVEL_SWITCH("Exterior Lamp Level", new Vector(1.5d, 1d, -0.3d), 0.25f, 0.5f),
    DOOR_TOGGLE("Toggle Wool Switch", new Vector(1.825d, 1d, -1.0d), 0.25f, 0.33f),

    // section five
    SCREEN_RIGHT("Coordinates Display", new Vector(1.825d, 1d, 0.95d), 0.5f, 1.1f),
    SCREEN_LEFT("Information Display", new Vector(1.525d, 1d, 1.45d), 0.5f, 1.1f),
    SCANNER("Exterior Environment Scanner", new Vector(1.825d, 1d, 1.95d), 0.25f, 0.33f),
    ARTRON("Artron Energy Button", new Vector(2.125d, 1d, 1.475d), 0.25f, 0.33f),
    REBUILD("Chameleon Circuit Re-initialiser", new Vector(2.4d, 1d, 1.025d), 0.25f, 0.33f);

    private final String alternateName;
    private final Vector relativePosition;
    private final float width;
    private final float height;

    ConsoleInteraction(String alternateName, Vector relativePosition, float width, float height) {
        this.alternateName = alternateName;
        this.relativePosition = relativePosition;
        this.width = width;
        this.height = height;
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
}
