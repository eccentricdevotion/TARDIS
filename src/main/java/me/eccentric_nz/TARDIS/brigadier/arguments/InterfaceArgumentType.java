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
import me.eccentric_nz.TARDIS.update.TARDISUpdateableCategory;
import net.kyori.adventure.text.Component;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class InterfaceArgumentType implements CustomArgumentType<String, String> {

    private static final SimpleCommandExceptionType ERROR_INVALID_SECTION = new SimpleCommandExceptionType(
            MessageComponentSerializer.message().serialize(Component.text("Invalid section specified!"))
    );
    private final Set<String> SECTIONS = new HashSet<>();

    public InterfaceArgumentType() {
        for (TARDISUpdateableCategory c : TARDISUpdateableCategory.values()) {
            SECTIONS.add(c.getName().toLowerCase(Locale.ROOT));
        }
    }

    @Override
    public String parse(StringReader reader) {
        return "";
    }

    @Override
    public <S> String parse(StringReader reader, S source) throws CommandSyntaxException {
        String input = reader.readUnquotedString();
        if (!SECTIONS.contains(input)) {
            throw ERROR_INVALID_SECTION.create();
        }
        return input;
    }

    @Override
    public ArgumentType<String> getNativeType() {
        return StringArgumentType.word();
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        for (String s : SECTIONS) {
            builder.suggest(s);
        }
        return builder.buildFuture();
    }
}
