/*
 * Copyright (C) 2026 eccentric_nz
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.TARDIS.floodgate;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.data.Archive;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardisSize;
import me.eccentric_nz.TARDIS.desktop.RandomArchiveName;
import me.eccentric_nz.TARDIS.desktop.DesktopThemeProcessor;
import me.eccentric_nz.TARDIS.desktop.UpgradeData;
import me.eccentric_nz.TARDIS.enumeration.ConsoleSize;
import me.eccentric_nz.TARDIS.enumeration.Desktops;
import me.eccentric_nz.TARDIS.enumeration.Schematic;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.schematic.archive.ArchiveUpdate;
import me.eccentric_nz.TARDIS.schematic.archive.ResultSetArchiveButtons;
import me.eccentric_nz.TARDIS.schematic.archive.ResultSetArchiveByName;
import me.eccentric_nz.TARDIS.utility.ComponentUtils;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.geysermc.cumulus.form.SimpleForm;
import org.geysermc.cumulus.response.SimpleFormResponse;
import org.geysermc.cumulus.util.FormImage;
import org.geysermc.floodgate.api.FloodgateApi;
import org.geysermc.floodgate.api.player.FloodgatePlayer;

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
                builder.button(ComponentUtils.stripColour(im.displayName()), FormImage.Type.PATH, String.format(path, FloodgateColouredBlocks.IMAGES.get(i)));
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
                    String name = RandomArchiveName.getRandomName();
                    player.performCommand("tardis archive add " + name + " " + rss.getConsoleSize().toString());
                }
            }
            case "SMALL", "MEDIUM", "TALL" -> {
                UpgradeData tud = plugin.getTrackerKeeper().getUpgrades().get(uuid);
                String size = label.toLowerCase(Locale.ROOT);
                int upgrade = plugin.getArtronConfig().getInt("upgrades.template." + size);
                if (tud.getLevel() >= upgrade) {
                    new ArchiveUpdate(plugin, uuid.toString(), label).setInUse();
                    tud.setSchematic(Desktops.schematicFor(size));
                    tud.setWall("ORANGE_WOOL");
                    tud.setFloor("LIGHT_GRAY_WOOL");
                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                        plugin.getTrackerKeeper().getUpgrades().put(uuid, tud);
                        // process upgrade
                        new DesktopThemeProcessor(plugin, uuid).changeDesktop();
                    }, 10L);
                }
            }
            default -> {
                // saved archive
                // remember the upgrade choice
                Schematic schm = Desktops.schematicFor("archive");
                UpgradeData tud = plugin.getTrackerKeeper().getUpgrades().get(uuid);
                ResultSetArchiveByName rsa = new ResultSetArchiveByName(plugin, uuid.toString(), label);
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
                            new DesktopThemeProcessor(plugin, uuid).changeDesktop();
                        }, 10L);
                    }
                }
            }
        }
    }
}
