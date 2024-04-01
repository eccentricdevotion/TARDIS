package me.eccentric_nz.TARDIS.console;

import org.bukkit.util.Vector;

public enum Interaction {

    // section zero
    HANDBRAKE("Time Rotor Handbrake", new Vector(1, 1, 1), 0.25f, 0.25f),
    THROTTLE("Flight Speed", new Vector(1, 1, 1), 0.25f, 0.25f),
    RELATIVITY_DIFFERENTIATOR("Exterior Flight Switch", new Vector(1, 1, 1), 0.25f, 0.25f),

    // section one
    WORLD("Dimension Selector", new Vector(1, 1, 1), 0.25f, 0.25f),
    X("X Distance", new Vector(1, 1, 1), 0.25f, 0.25f),
    Z("Z Distance", new Vector(1, 1, 1), 0.25f, 0.25f),
    MULTIPLIER("Coordinate Increment Modifier", new Vector(1, 1, 1), 0.25f, 0.25f),
    HELMIC_REGULATOR("Dimension Selector", new Vector(1, 1, 1), 0.25f, 0.25f),

    // section two
    RANDOMISER("Random Location Finder", new Vector(1, 1, 1), 0.25f, 0.25f),
    WAYPOINT_SELECTOR("Saves", new Vector(1, 1, 1), 0.25f, 0.25f),
    FAST_RETURN("Back Button", new Vector(1,1,1), 0.25f, 0.25f),

    // section three
    SONIC_DOCK("Sonic Screwdriver Dock", new Vector(1,1,1), 0.25f, 0.25f),
    ARTRON("Artron Energy Button", new Vector(1, 1, 1), 0.25f, 0.25f),
    DIRECTION("Exterior Directional Control", new Vector(1,1,1), 0.25f, 0.25f),
    TELEPATHIC_CIRCUIT("Telepathic Circuit", new Vector(1,1,1), 0.25f, 0.25f),

    // section four
    LIGHT_SWITCH("Interior Light Switch", new Vector(1,1,1), 0.25f, 0.25f),
    INTERIOR_LIGHT_LEVEL_SWITCH("Exterior Directional Control", new Vector(1,1,1), 0.25f, 0.25f),
    EXTERIOR_LAMP_LEVEL_SWITCH("Exterior Directional Control", new Vector(1,1,1), 0.25f, 0.25f),
    DOOR_TOGGLE("Toggle Wool Switch", new Vector(1,1,1), 0.25f, 0.25f),

    // section five
    SCREEN("Information Display", new Vector(1, 1, 1), 0.25f, 0.25f),
    SCANNER("Exterior Environment Scanner", new Vector(1, 1, 1), 0.25f, 0.25f),
    REBUILD("Chameleon Circuit Re-initialiser", new Vector(1, 1, 1), 0.25f, 0.25f);

    private final String alternateName;
    private final Vector relativePosition;
    private final float width;
    private final float height;

    Interaction(String alternateName, Vector relativePosition, float width, float height) {
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
