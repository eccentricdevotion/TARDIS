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
import me.eccentric_nz.TARDIS.enumeration.HADS;
import net.kyori.adventure.text.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class HADSArgumentType implements CustomArgumentType<String, String> {

    private static final SimpleCommandExceptionType ERROR_INVALID_MODE = new SimpleCommandExceptionType(
            MessageComponentSerializer.message().serialize(Component.text("Invalid HADS mode specified!"))
    );
    private final List<String> MODES = new ArrayList<>();

    public HADSArgumentType() {
        for (HADS h : HADS.values()) {
            MODES.add(h.toString());
        }
    }

    @Override
    public String parse(StringReader reader) {
        return "";
    }

    @Override
    public <S> String parse(StringReader reader, S source) throws CommandSyntaxException {
        String input = reader.readUnquotedString();
        if (!MODES.contains(input)) {
            throw ERROR_INVALID_MODE.create();
        }
        return input;
    }

    @Override
    public ArgumentType<String> getNativeType() {
        return StringArgumentType.word();
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        for (String d : MODES) {
            builder.suggest(d);
        }
        return builder.buildFuture();
    }
}
