package me.eccentric_nz.TARDIS.brigadier.suggestions;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.Desktops;
import me.eccentric_nz.tardischunkgenerator.worldgen.utils.GallifeyStructureUtility;
import me.eccentric_nz.tardischunkgenerator.worldgen.utils.SiluriaStructureUtility;
import me.eccentric_nz.tardischunkgenerator.worldgen.utils.SkaroStructureUtility;

import java.io.File;
import java.util.Arrays;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;

public class SchematicLoadSuggestions {

    public static CompletableFuture<Suggestions> get(final CommandContext<CommandSourceStack> ctx, final SuggestionsBuilder builder) {
        String dir = StringArgumentType.getString(ctx, "directory");
        // suggest
        switch (dir) {
            case "console" -> {
                Desktops.getBY_PERMS().keySet().stream()
                        .filter(d -> d.toLowerCase(Locale.ROOT).startsWith(builder.getRemainingLowerCase()))
                        .forEach(builder::suggest);
            }
            case "room" -> {
                TARDIS.plugin.getRoomsConfig().getConfigurationSection("rooms").getKeys(false).stream()
                        .filter(r -> r.toLowerCase(Locale.ROOT).startsWith(builder.getRemainingLowerCase()))
                        .forEach(r -> builder.suggest(r.toLowerCase(Locale.ROOT)));

            }
            case "structure" -> {
                for (String g : GallifeyStructureUtility.structures) {
                    builder.suggest("gallifrey_" + g);
                }
                for (String s : SiluriaStructureUtility.structures) {
                    builder.suggest("siluria_" + s);
                }
                for (String d : SkaroStructureUtility.structures) {
                    builder.suggest("dalek_" + d);
                }
            }
            // user
            default -> {
                File userDir = new File(TARDIS.plugin.getDataFolder() + File.separator + "user_schematics");
                if (userDir.exists()) {
                    Arrays.stream(userDir.list())
                            .filter(f -> f.endsWith(".tschm"))
                            .filter(f -> f.toLowerCase(Locale.ROOT).startsWith(builder.getRemainingLowerCase()))
                            .forEach(f -> builder.suggest(f.substring(0, f.length() - 6)));
                }
            }
        }
        return builder.buildFuture();
    }
}
