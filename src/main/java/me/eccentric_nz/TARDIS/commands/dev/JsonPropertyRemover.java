package me.eccentric_nz.TARDIS.commands.dev;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.command.CommandSender;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JsonPropertyRemover {

    private final TARDIS plugin;
    private final CommandSender sender;
    private final String property;

    public JsonPropertyRemover(TARDIS plugin, CommandSender sender, String property) {
        this.plugin = plugin;
        this.sender = sender;
        this.property = property;
    }

    public List<Path> listAllFiles(String startDirectory) throws IOException {
        List<Path> result;
        try (Stream<Path> walkStream = Files.walk(Paths.get(startDirectory))) {
            result = walkStream
                    .filter(Files::isRegularFile) // Filter out directories
                    .collect(Collectors.toList());
        }
        return result;
    }

    public boolean processFiles() {
        try {
            // get folder
            List<Path> allFiles = listAllFiles(plugin.getDataFolder() + File.separator + "models" + File.separator + "item");
            for (Path path : allFiles) {
                File child = path.toFile();
                // do something with child
                JsonReader reader = new JsonReader(new FileReader(child));
                JsonObject root = JsonParser.parseReader(reader).getAsJsonObject();
                if (root.has(property)) {
                    root.remove(property);
                    plugin.debug("Removed " + property + " from " + child.getName());
                }
                // write root object back to file
                try (Writer writer = new FileWriter(child.getPath())) {
//                    new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create().toJson(root, writer);
                    new GsonBuilder().disableHtmlEscaping().create().toJson(root, writer);
                }
                reader.close();
            }
        } catch (IOException e) {
            plugin.debug("Error reading json file!" + e.getMessage());
        }
        return true;
    }
}
