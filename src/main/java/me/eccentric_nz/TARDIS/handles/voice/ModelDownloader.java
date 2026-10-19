package me.eccentric_nz.TARDIS.handles.voice;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.concurrent.CompletableFuture;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ModelDownloader {

    public CompletableFuture<Boolean> get() {
        File voiceDir = new File(TARDIS.plugin.getDataFolder(), "voice_chat");
        if (!voiceDir.exists()) {
            boolean result = voiceDir.mkdirs();
            if (result && voiceDir.setWritable(true) && voiceDir.setExecutable(true)) {
                TARDIS.plugin.getMessenger().message(TARDIS.plugin.getConsole(), TardisModule.HANDLES, "Created voice_chat directory.");
            }
        }
        File targetModelDir = new File(voiceDir, "vosk-model");
        if (targetModelDir.exists() && targetModelDir.isDirectory()) {
            return CompletableFuture.completedFuture(true);
        }
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    URL modelDownload = URI.create("https://alphacephei.com/vosk/models/vosk-model-small-en-us-0.15.zip").toURL();
                    // create a connection
                    HttpURLConnection con = (HttpURLConnection) modelDownload.openConnection();
                    con.setRequestProperty("User-Agent", "eccentric_nz/TARDIS");
                    File dest = new File(voiceDir, "vosk-model-small-en-us-0.15.zip");
                    // get the input stream
                    try (InputStream input = con.getInputStream()) {
                        Files.copy(input, dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    }
                    TARDIS.plugin.getMessenger().message(TARDIS.plugin.getConsole(), TardisModule.HANDLES, "Vosk zip download complete. Extracting model archive...");
                    try {
                        // 1. Unzip the file while on the background thread
                        unzip(dest, targetModelDir);
                        // 2. Delete the temporary zip archive to save disk space
                        if (dest.exists()) {
                            dest.delete();
                        }
                        TARDIS.plugin.getMessenger().message(TARDIS.plugin.getConsole(), TardisModule.HANDLES, "Extraction complete.");
                        Bukkit.getScheduler().runTask(TARDIS.plugin, () -> future.complete(true));
                    } catch (Exception e) {
                        TARDIS.plugin.getLogger().severe("An error occurred while expanding or initializing the voice model: " + e.getMessage());
                    }
                } catch (Exception e) {
                    TARDIS.plugin.getLogger().severe("Failed to initialize Vosk Model: " + e.getMessage());
                    Bukkit.getScheduler().runTask(TARDIS.plugin, () -> future.complete(false));
                }
            }
        }.runTaskAsynchronously(TARDIS.plugin);
        return future;
    }

    private static void unzip(File zipFile, File destDir) throws IOException {
        if (!destDir.exists()) {
            destDir.mkdirs();
        }
        try (InputStream fis = Files.newInputStream(zipFile.toPath());
             BufferedInputStream bis = new BufferedInputStream(fis);
             ZipInputStream zipIn = new ZipInputStream(bis)
        ) {
            ZipEntry entry = zipIn.getNextEntry();
            while (entry != null) {
                String entryName = entry.getName();
                // Strip the top-level parent folder name inside the zip archive
                // (Transforms "vosk-model-small-en-us-0.15/am/final.mdl" into "am/final.mdl")
                if (entryName.contains("/")) {
                    entryName = entryName.substring(entryName.indexOf("/") + 1);
                }
                // Skip directory placeholders or the root folder marker
                if (entryName.isEmpty()) {
                    zipIn.closeEntry();
                    entry = zipIn.getNextEntry();
                    continue;
                }
                File filePath = new File(destDir, entryName);
                // Security: Prevent Zip Slip path traversal vulnerabilities
                if (!filePath.getCanonicalPath().startsWith(destDir.getCanonicalPath())) {
                    throw new IOException("Aborting: Malicious zip entry detected path traversal outside root boundaries: " + entry.getName());
                }
                if (!entry.isDirectory()) {
                    // Generate nested child folder layouts safely if missing
                    File parent = filePath.getParentFile();
                    if (!parent.exists()) {
                        parent.mkdirs();
                    }
                    // Write the file contents out to the hard disk
                    try (FileOutputStream fos = new FileOutputStream(filePath)) {
                        byte[] buffer = new byte[4096];
                        int len;
                        while ((len = zipIn.read(buffer)) > 0) {
                            fos.write(buffer, 0, len);
                        }
                    }
                } else {
                    filePath.mkdirs();
                }
                zipIn.closeEntry();
                entry = zipIn.getNextEntry();
            }
        }
    }
}
