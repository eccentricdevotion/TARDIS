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
import me.eccentric_nz.TARDIS.rooms.eye.Capacitor;
import net.kyori.adventure.text.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class CapacitorArgumentType implements CustomArgumentType<String, String> {

    private static final SimpleCommandExceptionType ERROR_INVALID_SIZE = new SimpleCommandExceptionType(
            MessageComponentSerializer.message().serialize(Component.text("Invalid eye size specified!"))
    );
    private final Set<String> SIZES = new HashSet<>();

    public CapacitorArgumentType() {
        for (Capacitor c : Capacitor.values()) {
            SIZES.add(c.toString());
        }
    }

    @Override
    public String parse(StringReader reader) {
        return "";
    }

    @Override
    public <S> String parse(StringReader reader, S source) throws CommandSyntaxException {
        String input = reader.readUnquotedString();
        if (!SIZES.contains(input)) {
            throw ERROR_INVALID_SIZE.create();
        }
        return input;
    }

    @Override
    public ArgumentType<String> getNativeType() {
        return StringArgumentType.word();
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        for (String d : SIZES) {
            builder.suggest(d);
        }
        return builder.buildFuture();
    }
}
