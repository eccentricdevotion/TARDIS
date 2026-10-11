package me.eccentric_nz.TARDIS.geyser;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import org.bukkit.command.CommandSender;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public class KeyMappingEditor {

    public static void updateType(TARDIS plugin, Path path, String replacement, CommandSender sender) {
        try {
            // read all lines from the file into memory
            List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
            // process each line: check for the target string and modify it
            String target = "gold_nugget";
            List<String> modifiedLines = lines.stream()
                    .map(line -> {
                        // check if the line contains your specific string
                        if (line.contains(target)) {
                            // edit the string (e.g., replace it or modify the whole line)
                            return line.replace(target, replacement);
                        }
                        return line; // return unchanged line if it doesn't match
                    })
                    .collect(Collectors.toList());
            // write the modified lines back to the file
            Files.write(path, modifiedLines, StandardCharsets.UTF_8);
            plugin.getMessenger().message(sender, TardisModule.TARDIS, String.format("Key mappings file updated successfully for %s.", replacement));
        } catch (IOException e) {
            plugin.getMessenger().message(sender, TardisModule.TARDIS, "An error occurred while editing the key_mappings file. " + e.getMessage());
        }
    }
}
