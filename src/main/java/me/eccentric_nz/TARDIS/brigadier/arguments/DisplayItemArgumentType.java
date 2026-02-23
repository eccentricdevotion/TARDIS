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
import me.eccentric_nz.TARDIS.customblocks.TARDISDisplayItemRegistry;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.ItemDisplay;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class DisplayItemArgumentType implements CustomArgumentType<String, String> {

    private static final SimpleCommandExceptionType ERROR_INVALID_TDI = new SimpleCommandExceptionType(
            MessageComponentSerializer.message().serialize(Component.text("Invalid TARDIS item display specified!"))
    );
    private final Set<String> TDIS = new HashSet<>();

    public DisplayItemArgumentType() {
        for (String t : TARDISDisplayItemRegistry.getBY_NAME().keySet()) {
            TDIS.add(t.toString());
        }
    }

    @Override
    public String parse(StringReader reader) throws CommandSyntaxException {
        return "";
    }

    @Override
    public <S> String parse(StringReader reader, S source) throws CommandSyntaxException {
        String input = reader.readUnquotedString();
        if (!TDIS.contains(input)) {
            throw ERROR_INVALID_TDI.create();
        }
        return input;
    }

    @Override
    public ArgumentType<String> getNativeType() {
        return StringArgumentType.word();
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        for (String d : TDIS) {
            builder.suggest(d);
        }
        return builder.buildFuture();
    }
}
