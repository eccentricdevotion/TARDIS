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
import me.eccentric_nz.TARDIS.TARDIS;
import net.kyori.adventure.text.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class RoomArgumentType implements CustomArgumentType<String, String> {

    private static final SimpleCommandExceptionType ERROR_INVALID_ROOM = new SimpleCommandExceptionType(
            MessageComponentSerializer.message().serialize(Component.text("Invalid room specified!"))
    );
    private final Set<String> ROOM_SUBS = new HashSet<>();

    public RoomArgumentType(TARDIS plugin) {
        plugin.getRoomsConfig().getConfigurationSection("rooms").getKeys(false).forEach((r) -> {
            if (plugin.getRoomsConfig().getBoolean("rooms." + r + ".enabled")) {
                ROOM_SUBS.add(r);
            }
        });
    }

    @Override
    public String parse(StringReader reader) throws CommandSyntaxException {
        return "";
    }

    @Override
    public <S> String parse(StringReader reader, S source) throws CommandSyntaxException {
        String input = reader.readUnquotedString();
        if (!ROOM_SUBS.contains(input)) {
            throw ERROR_INVALID_ROOM.create();
        }
        return input;
    }

    @Override
    public ArgumentType<String> getNativeType() {
        return StringArgumentType.word();
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        for (String d : ROOM_SUBS) {
            builder.suggest(d);
        }
        return builder.buildFuture();
    }
}
