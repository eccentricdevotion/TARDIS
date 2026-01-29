package me.eccentric_nz.TARDIS.artron.costs;

import me.eccentric_nz.TARDIS.TARDIS;

public class Calculator {

    public static int getMapped(CostData data, Highest highest) {
        double scale = TARDIS.plugin.getArtronConfig().getDouble("upgrades.cost_factor");
        double max = TARDIS.plugin.getArtronConfig().getDouble("full_charge");
        double mediumMax = 2.0f * max;
        double tallMax = 3.0f * max;
        double wideMax = 4.0f * max;
        double massiveMax = 5.0f * max;
        // (artron_cost / most_expensive_for_size) * size_cap -> remapped to between (full * size_scale / 2), (full * size_scale * scale)
        double formula_cost = switch (data.size()) {
            case SMALL -> map((((double) data.artron() / (double) highest.getSmall()) * max), max, max * 0.5f, max * scale);
            case MEDIUM -> map((((double) data.artron() / (double) highest.getMedium()) * mediumMax), mediumMax, mediumMax * 0.5f, mediumMax * scale);
            case TALL -> map((((double) data.artron() / (double) highest.getTall()) * tallMax), tallMax, tallMax * 0.5f, tallMax * scale);
            case WIDE -> map((((double) data.artron() / (double) highest.getWide()) * wideMax), wideMax, wideMax * 0.5f, wideMax * scale);
            default -> map((((double) data.artron() / (double) highest.getMassive()) * massiveMax), massiveMax, massiveMax * 0.5f, massiveMax * scale);
        };
        // round to the nearest 25
        return ((int) formula_cost / 25) * 25;
    }

    private static double map(double value, double maxStart, double minEnd, double maxEnd) {
        return minEnd + (maxEnd - minEnd) * (value / maxStart);
    }
}
