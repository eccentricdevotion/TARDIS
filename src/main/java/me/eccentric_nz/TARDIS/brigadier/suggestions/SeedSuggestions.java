package me.eccentric_nz.TARDIS.brigadier.suggestions;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import me.eccentric_nz.TARDIS.enumeration.Desktops;

import java.util.concurrent.CompletableFuture;

public class SeedSuggestions {

    public static CompletableFuture<Suggestions> get(final CommandContext<CommandSourceStack> ctx, final SuggestionsBuilder builder) {
        for (String s : Desktops.getBY_NAMES().keySet()) {
            builder.suggest(s);
        }
        return builder.buildFuture();
    }
}
