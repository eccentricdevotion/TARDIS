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
import me.eccentric_nz.TARDIS.particles.ParticleShape;
import net.kyori.adventure.text.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ParticleShapeArgumentType implements CustomArgumentType<String, String> {

    private static final SimpleCommandExceptionType ERROR_INVALID_SHAPE = new SimpleCommandExceptionType(
            MessageComponentSerializer.message().serialize(Component.text("Invalid particle shape specified!"))
    );
    private final List<String> SHAPES = new ArrayList<>();

    public ParticleShapeArgumentType() {
        for (ParticleShape s : ParticleShape.values()) {
            SHAPES.add(s.toString());
        }
    }

    @Override
    public String parse(StringReader reader) {
        return "";
    }

    @Override
    public <S> String parse(StringReader reader, S source) throws CommandSyntaxException {
        String input = reader.readUnquotedString();
        if (!SHAPES.contains(input)) {
            throw ERROR_INVALID_SHAPE.create();
        }
        return input;
    }

    @Override
    public ArgumentType<String> getNativeType() {
        return StringArgumentType.word();
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        for (String d : SHAPES) {
            builder.suggest(d);
        }
        return builder.buildFuture();
    }
}
