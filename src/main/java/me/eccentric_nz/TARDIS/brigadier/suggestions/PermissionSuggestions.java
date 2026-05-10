package me.eccentric_nz.TARDIS.brigadier.suggestions;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import me.eccentric_nz.TARDIS.TARDIS;

import java.util.Locale;
import java.util.concurrent.CompletableFuture;

public class PermissionSuggestions {

    public static CompletableFuture<Suggestions> get(final CommandContext<CommandSourceStack> ctx, final SuggestionsBuilder builder) {
        TARDIS.plugin.getGeneralKeeper().getPluginYAML().getConfigurationSection("permissions").getKeys(true).stream()
                .filter(p -> p.toLowerCase(Locale.ROOT).startsWith(builder.getRemainingLowerCase()))
                .forEach(builder::suggest);
        return builder.buildFuture();
    }
}
