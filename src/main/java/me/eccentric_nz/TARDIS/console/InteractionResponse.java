package me.eccentric_nz.TARDIS.console;

import me.eccentric_nz.TARDIS.TARDIS;

import java.util.HashMap;
import java.util.List;

public class InteractionResponse {

    public static final HashMap<ConsoleInteraction, List<String>> randomRange = new HashMap<>();
    public static final List<String> environment = List.of("", "Current world", "Overworld", "The Nether", "The End");
    public static final List<Integer> levels = List.of(15, 13, 12, 11, 9, 7, 5, 3);
    private final int quarter;

    public InteractionResponse(TARDIS plugin) {
        int max = plugin.getConfig().getInt("travel.tp_radius");
        this.quarter = (max + 4 - 1) / 4;
    }

    public void init() {
        randomRange.put(ConsoleInteraction.MULTIPLIER, List.of("", "1x", "2x", "3x", "4x"));
        randomRange.put(ConsoleInteraction.X, List.of("", "r = " + (quarter), "r = " + (quarter * 2), "r = " + (quarter * 3), "r = " + (quarter * 4)));
        randomRange.put(ConsoleInteraction.Z, List.of("", "r = " + (quarter), "r = " + (quarter * 2), "r = " + (quarter * 3), "r = " + (quarter * 4)));
    }
}
