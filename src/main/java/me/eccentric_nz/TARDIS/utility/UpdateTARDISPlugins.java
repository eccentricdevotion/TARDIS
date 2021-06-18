package me.eccentric_nz.tardis.utility;

import me.eccentric_nz.tardis.TardisPlugin;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class UpdateTardisPlugins {

    private final TardisPlugin plugin;
    private final List<String> FILES = new ArrayList<>();
    private final List<String> URLS = new ArrayList<>();
    private final AtomicBoolean updateInProgress = new AtomicBoolean(false);

    public UpdateTardisPlugins(TardisPlugin plugin) {
        this.plugin = plugin;
        FILES.add("tardis.jar");
        FILES.add("TARDISChunkGenerator.jar");
        URLS.add("http://tardisjenkins.duckdns.org:8080/job/TARDIS/lastSuccessfulBuild/artifact/target/TARDIS.jar");
        URLS.add("http://tardisjenkins.duckdns.org:8080/job/TARDISChunkGenerator/lastSuccessfulBuild/artifact/target/TARDISChunkGenerator.jar");
    }

    public boolean fetchFromJenkins(CommandSender sender) {
        if (updateInProgress.get()) {
            sender.sendMessage(plugin.getPluginName() + ChatColor.RED + "An update is already in progress!");
            return true;
        }
        sender.sendMessage(plugin.getPluginName() + ChatColor.AQUA + "Downloading tardis & TARDISChunkGenerator ...");
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
                    for (int i = 0; i < URLS.size(); i++) {
                        File dest = new File("plugins/update/" + FILES.get(i));
                        // connect to tardis jenkins
                        URL url = new URL(URLS.get(i));
                        // create a connection
                        HttpURLConnection con = (HttpURLConnection) url.openConnection();
                        con.setRequestProperty("User-Agent", "eccentric_nz/tardis");
                        // get the input stream
                        try (InputStream input = con.getInputStream()) {
                            Files.copy(input, dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
                        }
                    }
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            sender.sendMessage(plugin.getPluginName() + ChatColor.AQUA + "Update success! Restart the server to finish the update.");
                        }
                    }.runTask(plugin);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            sender.sendMessage(plugin.getPluginName() + ChatColor.RED + "Update failed, " + ex.getMessage());
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
