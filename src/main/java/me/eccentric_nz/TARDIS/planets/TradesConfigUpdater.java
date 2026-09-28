package me.eccentric_nz.TARDIS.planets;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.blueprints.BlueprintConsole;
import me.eccentric_nz.TARDIS.blueprints.BlueprintRoom;
import me.eccentric_nz.TARDIS.enumeration.Desktops;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class TradesConfigUpdater {

    private final TARDIS plugin;
    private final FileConfiguration tradesConfig;
    private final FileConfiguration roomsConfig;
    private final FileConfiguration artronConfig;
    private int i = 0;

    public TradesConfigUpdater(TARDIS plugin, FileConfiguration tradesConfig, FileConfiguration artronConfig, FileConfiguration roomsConfig) {
        this.plugin = plugin;
        this.tradesConfig = tradesConfig;
        this.artronConfig = artronConfig;
        this.roomsConfig = roomsConfig;
    }

    public void checkTrades() {
        for (BlueprintConsole bpc : BlueprintConsole.values()) {
            if (bpc != BlueprintConsole.CUSTOM && !tradesConfig.contains("consoles." + bpc)) {
                tradesConfig.set("consoles." + bpc + ".material", Desktops.getBY_NAMES().get(bpc.toString()).getSeed());
                tradesConfig.set("consoles." + bpc + ".amount", artronConfig.getInt("upgrades." + bpc.toString().toLowerCase(Locale.ROOT)) / 250);
                i++;
            }
        }
        for (BlueprintRoom bpr : BlueprintRoom.values()) {
            if (bpr != BlueprintRoom.JETTISON && !tradesConfig.contains("rooms." + bpr)) {
                tradesConfig.set("rooms." + bpr + ".material", roomsConfig.get("rooms." + bpr + ".seed"));
                tradesConfig.set("rooms." + bpr + ".amount", Math.min(roomsConfig.getInt("rooms." + bpr + ".cost") / 20, 128));
                i++;
            }
        }
        if (tradesConfig.getString("rooms.APIARY.material").equals("BEE_HIVE") || tradesConfig.getString("rooms.APIARY.material").equals("BEE_NEST")) {
            tradesConfig.set("rooms.APIARY.material", "BEEHIVE");
            i++;
        }
        // maximum trade is 128 blocks
        if (tradesConfig.getInt("rooms.OBSERVATORY.amount") == 197) {
            tradesConfig.set("rooms.OBSERVATORY.amount", 128);
            i++;
        }
        if (!tradesConfig.contains("traders.no_spawn")) {
            tradesConfig.set("traders.no_spawn", List.of("the_end", "the_nether"));
            tradesConfig.setComments("traders.no_spawn", List.of("blacklist of dimensions time lord traders cannot spawn in"));
            tradesConfig.set("traders.despawn_after", 5);
            tradesConfig.setComments("traders.despawn_after", List.of("number of minutes after no interaction a trader will despawn"));
            tradesConfig.setComments("traders", List.of("time lord traders"));
            i++;
        }
        if (i > 0) {
            try {
                String tradesPath = plugin.getDataFolder() + File.separator + "trades.yml";
                tradesConfig.save(new File(tradesPath));
                plugin.getMessenger().message(plugin.getConsole(), TardisModule.TARDIS, "Added " + i + " entries to trades.yml");
            } catch (IOException io) {
                plugin.debug("Could not save trades.yml, " + io.getMessage());
            }
        }
    }
}
