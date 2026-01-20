package me.eccentric_nz.TARDIS.artron.costs;

import me.eccentric_nz.TARDIS.TARDIS;

public class Calculator {

    public static int getMapped(CostData data, Highest highest) {
        float scale = 0.85f;
        float max = TARDIS.plugin.getArtronConfig().getInt("full_charge");
        float mediumMax = 2.0f * max;
        float tallMax = 3.0f * max;
        float wideMax = 4.0f * max;
        float massiveMax = 5.0f * max;
        // (artron_cost / most_expensive_for_size) * size_cap -> remapped to between (full * size_scale / 2), (full * size_scale * scale)
        float formula_cost = switch (data.size()) {
            case SMALL -> map((((float) data.artron() / (float) highest.getSmall()) * max), max, max * 0.5f, max * scale);
            case MEDIUM -> map((((float) data.artron() / (float) highest.getMedium()) * mediumMax), mediumMax, mediumMax * 0.5f, mediumMax * scale);
            case TALL -> map((((float) data.artron() / (float) highest.getTall()) * tallMax), tallMax, tallMax * 0.5f, tallMax * scale);
            case WIDE -> map((((float) data.artron() / (float) highest.getWide()) * wideMax), wideMax, wideMax * 0.5f, wideMax * scale);
            default -> map((((float) data.artron() / (float) highest.getMassive()) * massiveMax), massiveMax, massiveMax * 0.5f, massiveMax * scale);
        };
        // round to the nearest 25
        return ((int) formula_cost / 25) * 25;
    }

    private static float map(float value, float maxStart, float minEnd, float maxEnd) {
        return minEnd + (maxEnd - minEnd) * (value / maxStart);
    }
}
