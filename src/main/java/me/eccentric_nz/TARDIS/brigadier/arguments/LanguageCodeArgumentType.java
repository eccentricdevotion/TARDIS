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

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class LanguageCodeArgumentType implements CustomArgumentType<String, String> {

    private static final SimpleCommandExceptionType ERROR_INVALID_LANG = new SimpleCommandExceptionType(
            MessageComponentSerializer.message().serialize(Component.text("Invalid config section specified!"))
    );
    private final Set<String> LANG_SUBS = Set.of(
            "ar", "bg", "ca", "zh", "cs", "da", "nl", "en",
            "et", "fi", "fr", "de", "el", "ht", "he", "hi",
            "mww", "hu", "id", "it", "ja", "ko", "lv", "lt",
            "ms", "no", "fa", "pl", "pt", "ro", "ru", "sk",
            "sl", "es", "sv", "th", "tr", "uk", "ur", "vi"
    );

    @Override
    public String parse(StringReader reader) {
        return "";
    }

    @Override
    public <S> String parse(StringReader reader, S source) throws CommandSyntaxException {
        String input = reader.readUnquotedString();
        if (!LANG_SUBS.contains(input)) {
            throw ERROR_INVALID_LANG.create();
        }
        return input;
    }

    @Override
    public ArgumentType<String> getNativeType() {
        return StringArgumentType.word();
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        for (String d : LANG_SUBS) {
            builder.suggest(d);
        }
        return builder.buildFuture();
    }
}
