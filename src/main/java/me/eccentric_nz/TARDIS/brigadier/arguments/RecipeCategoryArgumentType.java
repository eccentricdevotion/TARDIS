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
import me.eccentric_nz.TARDIS.enumeration.RecipeCategory;
import net.kyori.adventure.text.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class RecipeCategoryArgumentType implements CustomArgumentType<String, String> {

    private static final SimpleCommandExceptionType ERROR_INVALID_CAT = new SimpleCommandExceptionType(
            MessageComponentSerializer.message().serialize(Component.text("Invalid use_clay option specified!"))
    );
    private final Set<String> CATEGORIES = new HashSet<>();

    public RecipeCategoryArgumentType() {
        for (RecipeCategory r : RecipeCategory.values()) {
            CATEGORIES.add(r.toString());
        }
        CATEGORIES.remove("UNUSED");
        CATEGORIES.remove("UNCRAFTABLE");
    }

    @Override
    public String parse(StringReader reader) throws CommandSyntaxException {
        return "";
    }

    @Override
    public <S> String parse(StringReader reader, S source) throws CommandSyntaxException {
        String input = reader.readUnquotedString();
        if (!CATEGORIES.contains(input)) {
            throw ERROR_INVALID_CAT.create();
        }
        return input;
    }

    @Override
    public ArgumentType<String> getNativeType() {
        return StringArgumentType.word();
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        for (String d : CATEGORIES) {
            builder.suggest(d);
        }
        return builder.buildFuture();
    }
}
