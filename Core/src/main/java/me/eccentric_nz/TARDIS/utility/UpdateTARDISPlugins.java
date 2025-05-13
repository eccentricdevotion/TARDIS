/*
 * Copyright (C) 2025 eccentric_nz
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
package me.eccentric_nz.TARDIS.utility;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.concurrent.atomic.AtomicBoolean;

public class UpdateTARDISPlugins {

    private final TARDIS plugin;
    private final AtomicBoolean updateInProgress = new AtomicBoolean(false);

    public UpdateTARDISPlugins(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean fetchFromJenkins(CommandSender sender) {
        if (updateInProgress.get()) {
            plugin.getMessenger().sendWithColour(sender, TardisModule.TARDIS, "An update is already in progress!", "#FF5555");
            return true;
        }
        plugin.getMessenger().sendWithColour(sender, TardisModule.TARDIS,  "Downloading TARDIS...", "#55FFFF");
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    // check update folder exists
                    File update = new File("plugins/update");
                    if (!update.exists()) {
                        if (update.mkdir()) {
                            update.setWritable(true);
                            update.setExecutable(true);
                        }
                    }
                    File dest = new File("plugins/update/TARDIS.jar");
                    // connect to tardis jenkins
                    URL url = URI.create("http://tardisjenkins.duckdns.org:8080/job/TARDIS/lastSuccessfulBuild/artifact/target/TARDIS.jar").toURL();
                    // create a connection
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setRequestProperty("User-Agent", "eccentric_nz/TARDIS");
                    // get the input stream
                    try (InputStream input = con.getInputStream()) {
                        Files.copy(input, dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    }
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            plugin.getMessenger().sendWithColour(sender, TardisModule.TARDIS, "Update success! Restart the server to finish the update.", "#55FFFF");
                        }
                    }.runTask(plugin);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            plugin.getMessenger().sendWithColour(sender, TardisModule.TARDIS,  "Update failed, " + ex.getMessage(), "#FF5555");
                        }
                    }.runTask(plugin);
                } finally {
                    updateInProgress.set(false);
                }
            }
        }.runTaskAsynchronously(plugin);
        return true;
    }
}
