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
import me.eccentric_nz.tardisweepingangels.utils.Monster;
import net.kyori.adventure.text.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class MonsterArgumentType implements CustomArgumentType<String, String> {

    private static final SimpleCommandExceptionType ERROR_INVALID_MONSTER = new SimpleCommandExceptionType(
            MessageComponentSerializer.message().serialize(Component.text("Invalid use_clay option specified!"))
    );
    private final Set<String> MONSTERS = new HashSet<>();

    public MonsterArgumentType() {
        for (Monster m : Monster.values()) {
            MONSTERS.add(m.toString());
        }
    }

    @Override
    public String parse(StringReader reader) {
        return "";
    }

    @Override
    public <S> String parse(StringReader reader, S source) throws CommandSyntaxException {
        String input = reader.readUnquotedString();
        if (!MONSTERS.contains(input)) {
            throw ERROR_INVALID_MONSTER.create();
        }
        return input;
    }

    @Override
    public ArgumentType<String> getNativeType() {
        return StringArgumentType.word();
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        for (String d : MONSTERS) {
            builder.suggest(d);
        }
        return builder.buildFuture();
    }
}
