package me.eccentric_nz.TARDIS.bStats;

import me.eccentric_nz.TARDIS.TARDIS;

public class TARDISStats {

    private final TARDIS plugin;
    private final int pluginId = 11698; // You can find the plugin id on https://bstats.org/what-is-my-plugin-id

    public TARDISStats(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void startMetrics() {
        // start bStats metrics
        Metrics metrics = new Metrics(plugin, pluginId);
        // console types
        metrics.addCustomChart(new Metrics.AdvancedPie("console_types", () -> new ConsoleTypes(plugin).getMap()));
        // chameleon presets
        metrics.addCustomChart(new Metrics.AdvancedPie("chameleon_presets", () -> new ChameleonPresets(plugin).getMap()));
        // how often each type of travel is used (saves, biome, cave, village, etc.)
        metrics.addCustomChart(new Metrics.AdvancedPie("travel_types", () -> new TravelTypes(plugin).getMap()));
        // TIPS or create_worlds
        metrics.addCustomChart(new Metrics.SimplePie("using_tips", () -> !plugin.getConfig().getBoolean("creation.create_worlds", false) ? "true" : "false"));
        // End / Nether travel enabled
        metrics.addCustomChart(new Metrics.SimplePie("end_travel", () -> plugin.getConfig().getString("travel.the_end", "true")));
        metrics.addCustomChart(new Metrics.SimplePie("nether_travel", () -> plugin.getConfig().getString("travel.nether", "true")));
        // number tardises
        metrics.addCustomChart(new Metrics.SingleLineChart("number_of_tardises", () -> new TardisCount(plugin).getCount(0)));
        // number abandoned tardises
        metrics.addCustomChart(new Metrics.SingleLineChart("abandoned_tardises", () -> new TardisCount(plugin).getCount(1)));
        // most condensed blocks
        metrics.addCustomChart(new Metrics.AdvancedPie("condensed_blocks", () -> new CondenserCounts(plugin).getCounts()));
        // most commonly grown rooms / median number of ARS rooms per TARDIS
        ARSRoomCounts arsRoomCounts = new ARSRoomCounts(plugin);
        metrics.addCustomChart(new Metrics.AdvancedPie("rooms", () -> arsRoomCounts.getRoomCounts()));
        metrics.addCustomChart(new Metrics.SimplePie("median_rooms_per_tardis", () -> arsRoomCounts.getMedian()));
        // how many servers/players use hard or easy mode
        metrics.addCustomChart(new Metrics.SimplePie("server_difficulty", () -> plugin.getConfig().getString("preferences.difficulty", "easy")));
        metrics.addCustomChart(new Metrics.AdvancedPie("player_difficulty", () -> new PlayerDifficulty(plugin).getModes()));
        // junk tardis enabled
        metrics.addCustomChart(new Metrics.SimplePie("junk_tardis", () -> plugin.getConfig().getString("junk.enabled", "true")));
    }
}
