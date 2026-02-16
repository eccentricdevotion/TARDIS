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
import org.bukkit.entity.Creature;
import org.bukkit.entity.EntityType;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class CreatureArgumentType implements CustomArgumentType<String, String> {

    private static final SimpleCommandExceptionType ERROR_INVALID_ENTITY = new SimpleCommandExceptionType(
            MessageComponentSerializer.message().serialize(Component.text("Invalid entity type specified!"))
    );
    private static final List<String> CREATURE_SUBS = new ArrayList<>();

    static {
        for (EntityType e : EntityType.values()) {
            if (e.getEntityClass() != null && Creature.class.isAssignableFrom(e.getEntityClass())) {
                CREATURE_SUBS.add(e.toString());
            }
        }
    }

    @Override
    public String parse(StringReader reader) throws CommandSyntaxException {
        return "";
    }

    @Override
    public <S> String parse(StringReader reader, S source) throws CommandSyntaxException {
        String input = reader.readUnquotedString();
        if (!CREATURE_SUBS.contains(input)) {
            throw ERROR_INVALID_ENTITY.create();
        }
        return input;
    }

    @Override
    public ArgumentType<String> getNativeType() {
        return StringArgumentType.word();
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        for (String d : CREATURE_SUBS) {
            builder.suggest(d);
        }
        return builder.buildFuture();
    }
}
