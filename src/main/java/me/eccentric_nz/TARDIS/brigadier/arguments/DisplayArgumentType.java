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
import me.eccentric_nz.TARDIS.display.TARDISDisplayType;
import net.kyori.adventure.text.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class DisplayArgumentType implements CustomArgumentType<String, String> {

    private static final SimpleCommandExceptionType ERROR_INVALID_HUD = new SimpleCommandExceptionType(
            MessageComponentSerializer.message().serialize(Component.text("Invalid HUD specified!"))
    );
    private final Set<String> HUDS = new HashSet<>();

    public DisplayArgumentType() {
        for (TARDISDisplayType dt : TARDISDisplayType.values()) {
            HUDS.add(dt.toString());
        }
    }

    @Override
    public String parse(StringReader reader) {
        return "";
    }

    @Override
    public <S> String parse(StringReader reader, S source) throws CommandSyntaxException {
        String input = reader.readUnquotedString();
        if (!HUDS.contains(input)) {
            throw ERROR_INVALID_HUD.create();
        }
        return input;
    }

    @Override
    public ArgumentType<String> getNativeType() {
        return StringArgumentType.word();
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        for (String h : HUDS) {
            builder.suggest(h);
        }
        return builder.buildFuture();
    }
}
