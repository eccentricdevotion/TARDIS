package me.eccentric_nz.TARDIS.brigadier.arguments;

import com.google.common.collect.ImmutableList;
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
import org.bukkit.DyeColor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class MonsterExtraArgumentType implements CustomArgumentType<String, String> {

    private static final SimpleCommandExceptionType ERROR_INVALID_OPT = new SimpleCommandExceptionType(
            MessageComponentSerializer.message().serialize(Component.text("Invalid mannequin equipment specified!"))
    );
    private final Set<String> EXTRAS = new HashSet<>();

    public MonsterExtraArgumentType() {
        for (DyeColor dye : DyeColor.values()) {
            EXTRAS.add(dye.toString());
        }
        EXTRAS.add("alternate");
        EXTRAS.add("flying");
        EXTRAS.add("flaming");
        EXTRAS.add("CYBERMAN_RISE");
        EXTRAS.add("CYBER_LORD");
        EXTRAS.add("BLACK_CYBERMAN");
        EXTRAS.add("CYBERMAN_EARTHSHOCK");
        EXTRAS.add("CYBERMAN_INVASION");
        EXTRAS.add("CYBERMAN_MOONBASE");
        EXTRAS.add("CYBERMAN_TENTH_PLANET");
        EXTRAS.add("WOOD_CYBERMAN");
    }

    @Override
    public String parse(StringReader reader) throws CommandSyntaxException {
        return "";
    }

    @Override
    public <S> String parse(StringReader reader, S source) throws CommandSyntaxException {
        String input = reader.readUnquotedString();
        if (!EXTRAS.contains(input)) {
            throw ERROR_INVALID_OPT.create();
        }
        return input;
    }

    @Override
    public ArgumentType<String> getNativeType() {
        return StringArgumentType.word();
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        for (String d : EXTRAS) {
            builder.suggest(d);
        }
        return builder.buildFuture();
    }
}
