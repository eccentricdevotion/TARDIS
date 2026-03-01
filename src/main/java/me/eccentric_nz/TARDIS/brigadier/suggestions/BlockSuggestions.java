package me.eccentric_nz.TARDIS.brigadier.suggestions;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.Material;

import java.util.concurrent.CompletableFuture;

public class BlockSuggestions {

    public static CompletableFuture<Suggestions> get(final CommandContext<CommandSourceStack> ctx, final SuggestionsBuilder builder) {
        for (Material m : Material.values()) {
            if (m.isBlock()) {
                builder.suggest(m.toString());
            }
        }
        return builder.buildFuture();
    }
}
