package me.eccentric_nz.TARDIS.brigadier.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.papermc.paper.command.brigadier.MessageComponentSerializer;
import io.papermc.paper.command.brigadier.argument.CustomArgumentType;
import net.kyori.adventure.text.Component;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class TipsLimitArgumentType implements CustomArgumentType<Integer, Integer> {

    private static final SimpleCommandExceptionType ERROR_INVALID_LIMIT = new SimpleCommandExceptionType(
            MessageComponentSerializer.message().serialize(Component.text("Invalid TIPS limit specified!"))
    );
    private final Set<Integer> LIMIT_SUBS = Set.of(400, 800, 1200, 1600);

    @Override
    public Integer parse(StringReader reader) throws CommandSyntaxException {
        return 0;
    }

    @Override
    public <S> Integer parse(StringReader reader, S source) throws CommandSyntaxException {
        int input = reader.readInt();
        if (!LIMIT_SUBS.contains(input)) {
            throw ERROR_INVALID_LIMIT.create();
        }
        return input;
    }

    @Override
    public ArgumentType<Integer> getNativeType() {
        return IntegerArgumentType.integer(400, 1600);
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        for (Integer i : LIMIT_SUBS) {
            builder.suggest(i);
        }
        return builder.buildFuture();
    }
}
