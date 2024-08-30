package me.eccentric_nz.TARDIS.floodgate;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.data.Archive;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardisSize;
import me.eccentric_nz.TARDIS.desktop.TARDISRandomArchiveName;
import me.eccentric_nz.TARDIS.desktop.TARDISThemeProcessor;
import me.eccentric_nz.TARDIS.desktop.TARDISUpgradeData;
import me.eccentric_nz.TARDIS.enumeration.ConsoleSize;
import me.eccentric_nz.TARDIS.enumeration.Consoles;
import me.eccentric_nz.TARDIS.enumeration.Schematic;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.schematic.ArchiveUpdate;
import me.eccentric_nz.TARDIS.schematic.ResultSetArchive;
import me.eccentric_nz.TARDIS.schematic.ResultSetArchiveButtons;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.geysermc.cumulus.form.SimpleForm;
import org.geysermc.cumulus.response.SimpleFormResponse;
import org.geysermc.cumulus.util.FormImage;
import org.geysermc.floodgate.api.FloodgateApi;
import org.geysermc.floodgate.api.player.FloodgatePlayer;

import java.util.HashMap;
import java.util.Locale;
import java.util.UUID;

public class FloodgateDesktopArchiveForm {

    private final TARDIS plugin;
    private final UUID uuid;
    private final String path = "textures/blocks/%s.png";

    public FloodgateDesktopArchiveForm(TARDIS plugin, UUID uuid) {
        this.plugin = plugin;
        this.uuid = uuid;
    }

    public void send() {
        SimpleForm.Builder builder = SimpleForm.builder();
        builder.button("Scan console", FormImage.Type.URL, "https://github.com/eccentricdevotion/TARDIS-Resource-Pack/raw/master/assets/tardis/textures/item/gui/archive/scan_button.png");
        builder.button("Archive current console", FormImage.Type.URL, "https://github.com/eccentricdevotion/TARDIS-Resource-Pack/raw/master/assets/tardis/textures/item/gui/archive/archive_button.png");
        // get archived consoles
        ResultSetArchiveButtons rs = new ResultSetArchiveButtons(plugin, uuid.toString());
        if (rs.resultSet()) {
            int i = 48; // FloodgateColouredBlocks -> terracotta starts at index 48
            builder.title("TARDIS Archive");
            for (ItemStack is : rs.getButtons()) {
                ItemMeta im = is.getItemMeta();
                builder.button(im.getDisplayName(), FormImage.Type.PATH, String.format(path, FloodgateColouredBlocks.IMAGES.get(i)));
                i++;
            }
        }
        for (ConsoleSize c : ConsoleSize.values()) {
            if (!c.equals(ConsoleSize.MASSIVE)) {
                String url = "https://raw.githubusercontent.com/eccentricdevotion/TARDIS-Resource-Pack/master/assets/tardis/textures/item/gui/archive/" + c.toString().toLowerCase(Locale.ROOT) + ".png";
                builder.button(c.toString(), FormImage.Type.URL, url);
            }
        }
        builder.validResultHandler(this::handleResponse);
        SimpleForm form = builder.build();
        FloodgatePlayer player = FloodgateApi.getInstance().getPlayer(uuid);
        player.sendForm(form);
    }

    private void handleResponse(SimpleFormResponse response) {
        Player player = plugin.getServer().getPlayer(uuid);
        String label = response.clickedButton().text();
        switch (label) {
            case "Scan console" -> {
                // get current console size
                ResultSetTardisSize rss = new ResultSetTardisSize(plugin);
                if (rss.fromUUID(uuid.toString())) {
                    player.performCommand("tardis archive scan " + rss.getConsoleSize().toString());
                }
            }
            case "Archive current console" -> {
                ResultSetTardisSize rss = new ResultSetTardisSize(plugin);
                if (rss.fromUUID(uuid.toString())) {
                    // generate random name
                    String name = TARDISRandomArchiveName.getRandomName();
                    player.performCommand("tardis archive add " + name + " " + rss.getConsoleSize().toString());
                }
            }
            case "SMALL", "MEDIUM", "TALL" -> {
                TARDISUpgradeData tud = plugin.getTrackerKeeper().getUpgrades().get(uuid);
                String size = label.toLowerCase(Locale.ENGLISH);
                int upgrade = plugin.getArtronConfig().getInt("upgrades.template." + size);
                if (tud.getLevel() >= upgrade) {
                    new ArchiveUpdate(plugin, uuid.toString(), label).setInUse();
                    tud.setSchematic(Consoles.schematicFor(size));
                    tud.setWall("ORANGE_WOOL");
                    tud.setFloor("LIGHT_GRAY_WOOL");
                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                        plugin.getTrackerKeeper().getUpgrades().put(uuid, tud);
                        // process upgrade
                        new TARDISThemeProcessor(plugin, uuid).changeDesktop();
                    }, 10L);
                }
            }
            default -> {
                // saved archive
                // remember the upgrade choice
                Schematic schm = Consoles.schematicFor("archive");
                TARDISUpgradeData tud = plugin.getTrackerKeeper().getUpgrades().get(uuid);
                HashMap<String, Object> where = new HashMap<>();
                where.put("uuid", uuid.toString());
                where.put("name", label);
                ResultSetArchive rsa = new ResultSetArchive(plugin, where);
                if (rsa.resultSet()) {
                    Archive a = rsa.getArchive();
                    if (a.getUse() == 1) {
                        plugin.getMessenger().send(player, TardisModule.TARDIS, "ARCHIVE_NOT_CURRENT");
                        return;
                    }
                    int upgrade = plugin.getArtronConfig().getInt("upgrades.archive." + a.getConsoleSize().toString().toLowerCase(Locale.ROOT));
                    if (tud.getLevel() >= upgrade) {
                        new ArchiveUpdate(plugin, uuid.toString(), label).setInUse();
                        tud.setSchematic(schm);
                        tud.setWall("ORANGE_WOOL");
                        tud.setFloor("LIGHT_GRAY_WOOL");
                        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                            plugin.getTrackerKeeper().getUpgrades().put(uuid, tud);
                            // process upgrade
                            new TARDISThemeProcessor(plugin, uuid).changeDesktop();
                        }, 10L);
                    }
                }
            }
        }
    }
}
