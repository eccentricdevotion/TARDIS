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
import net.kyori.adventure.text.Component;
import org.bukkit.WorldType;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class WorldTypeArgument  implements CustomArgumentType<String, String> {

    private static final SimpleCommandExceptionType ERROR_INVALID_TYPE = new SimpleCommandExceptionType(
            MessageComponentSerializer.message().serialize(Component.text("Invalid world type specified!"))
    );
    private final List<String> TYPE_SUBS = new ArrayList<>();

    public WorldTypeArgument() {
        for (WorldType wt : WorldType.values()) {
            TYPE_SUBS.add(wt.toString());
        }
    }

    @Override
    public String parse(StringReader reader) throws CommandSyntaxException {
        return "";
    }

    @Override
    public <S> String parse(StringReader reader, S source) throws CommandSyntaxException {
        String input = reader.readUnquotedString();
        if (!TYPE_SUBS.contains(input)) {
            throw ERROR_INVALID_TYPE.create();
        }
        return input;
    }

    @Override
    public ArgumentType<String> getNativeType() {
        return StringArgumentType.word();
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        for (String d : TYPE_SUBS) {
            builder.suggest(d);
        }
        return builder.buildFuture();
    }
}