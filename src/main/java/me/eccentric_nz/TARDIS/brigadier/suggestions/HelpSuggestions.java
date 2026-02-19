package me.eccentric_nz.TARDIS.brigadier.suggestions;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.Material;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class HelpSuggestions {

    private final Set<String> CMD_ARGS = new HashSet<>();

    public HelpSuggestions(TARDIS plugin, CommandContext<CommandSourceStack> ctx) {
        String sub = StringArgumentType.getString(ctx, "command");
        for (String command : plugin.getGeneralKeeper().getPluginYAML().getConfigurationSection("commands." + sub).getKeys(false)) {
            CMD_ARGS.add(command.toString());
        }
        // remove unwanted
        Set.of("aliases", "description", "usage", "permission", "permission-message").forEach(CMD_ARGS::remove);
    }

    public CompletableFuture<Suggestions> get(final CommandContext<CommandSourceStack> ctx, final SuggestionsBuilder builder) {
        for (String ca : CMD_ARGS) {
                builder.suggest(ca);
        }
        return builder.buildFuture();
    }
}
