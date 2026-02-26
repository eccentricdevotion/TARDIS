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

public class PreferencesArgumentType implements CustomArgumentType<String, String> {

    private static final SimpleCommandExceptionType ERROR_INVALID_PREF = new SimpleCommandExceptionType(
            MessageComponentSerializer.message().serialize(Component.text("Invalid preference specified!"))
    );
    private final Set<String> PREFERENCES = Set.of(
            "announce_repeaters", "auto", "auto_powerup", "auto_rescue", "auto_siege",
            "beacon",
            "close_gui",
            "dialogs", "dnd", "dynamic_lamps",
            "eps",
            "farm", "forcefield",
            "hads",
            "info",
            "lock_containers",
            "minecart",
            "open_display_door",
            "quotes",
            "regen_block", "renderer",
            "sfx", "sign", "sonic", "submarine",
            "telepathy", "travelbar"
    );

    @Override
    public String parse(StringReader reader) {
        return "";
    }

    @Override
    public <S> String parse(StringReader reader, S source) throws CommandSyntaxException {
        String input = reader.readUnquotedString();
        if (!PREFERENCES.contains(input)) {
            throw ERROR_INVALID_PREF.create();
        }
        return input;
    }

    @Override
    public ArgumentType<String> getNativeType() {
        return StringArgumentType.word();
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        for (String n : PREFERENCES) {
            builder.suggest(n);
        }
        return builder.buildFuture();
    }
}
