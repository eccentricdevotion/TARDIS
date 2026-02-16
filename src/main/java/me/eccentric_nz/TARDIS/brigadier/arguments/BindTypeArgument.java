package me.eccentric_nz.TARDIS.brigadier.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.papermc.paper.command.brigadier.MessageComponentSerializer;
import io.papermc.paper.command.brigadier.argument.CustomArgumentType;
import me.eccentric_nz.TARDIS.enumeration.Bind;
import net.kyori.adventure.text.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class BindTypeArgument implements CustomArgumentType<String, String> {

    private static final SimpleCommandExceptionType ERROR_INVALID_BIND = new SimpleCommandExceptionType(
            MessageComponentSerializer.message().serialize(Component.text("Invalid bind type specified!"))
    );
    private final List<String> BIND_SUBS = new ArrayList<>();

    public BindTypeArgument(boolean restrict) {
        for (Bind b : Bind.values()) {
            if (!restrict || b.getArgs() == 2) {
                BIND_SUBS.add(b.toString());
            }
        }
    }

    @Override
    public String parse(StringReader reader) throws CommandSyntaxException {
        return "";
    }

    @Override
    public <S> String parse(StringReader reader, S source) throws CommandSyntaxException {
        String input = reader.readUnquotedString();
        if (!BIND_SUBS.contains(input)) {
            throw ERROR_INVALID_BIND.create();
        }
        return input;
    }

    @Override
    public ArgumentType<String> getNativeType() {
        return StringArgumentType.word();
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        for (String b : BIND_SUBS) {
            builder.suggest(b);
        }
        return builder.buildFuture();
    }
}
