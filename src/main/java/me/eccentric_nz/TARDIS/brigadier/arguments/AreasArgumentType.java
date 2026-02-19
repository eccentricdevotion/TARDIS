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
import me.eccentric_nz.TARDIS.database.data.Area;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetAreas;
import net.kyori.adventure.text.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class AreasArgumentType implements CustomArgumentType<String, String> {

    private static final SimpleCommandExceptionType ERROR_INVALID_AREA = new SimpleCommandExceptionType(
            MessageComponentSerializer.message().serialize(Component.text("Invalid TARDIS area specified!"))
    );
    private final List<String> AREA_SUBS = new ArrayList<>();

    public AreasArgumentType() {
        ResultSetAreas rsa = new ResultSetAreas(TARDIS.plugin, null, true, false);
        if (rsa.resultSet()) {
            // cycle through areas
            for (Area a : rsa.getData()) {
                AREA_SUBS.add(a.areaName());
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
        if (!AREA_SUBS.contains(input)) {
            throw ERROR_INVALID_AREA.create();
        }
        return input;
    }

    @Override
    public ArgumentType<String> getNativeType() {
        return StringArgumentType.word();
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        for (String a : AREA_SUBS) {
            builder.suggest(a);
        }
        return builder.buildFuture();
    }
}
