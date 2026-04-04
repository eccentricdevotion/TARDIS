package me.eccentric_nz.TARDIS.planets;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.blueprints.BlueprintConsole;
import me.eccentric_nz.TARDIS.blueprints.BlueprintRoom;
import me.eccentric_nz.TARDIS.enumeration.Desktops;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.io.IOException;
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
                tradesConfig.set("rooms." + bpr + ".amount", roomsConfig.getInt("rooms." + bpr + ".cost") / 20);
                i++;
            }
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
