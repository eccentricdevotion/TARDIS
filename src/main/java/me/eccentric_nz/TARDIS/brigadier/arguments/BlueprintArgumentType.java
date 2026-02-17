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
import me.eccentric_nz.TARDIS.blueprints.*;
import net.kyori.adventure.text.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class BlueprintArgumentType implements CustomArgumentType<String, String> {

    private static final SimpleCommandExceptionType ERROR_INVALID_BLUEPRINT = new SimpleCommandExceptionType(
            MessageComponentSerializer.message().serialize(Component.text("Invalid blueprint specified!"))
    );
    private final Set<String> BLUEPRINT_SUBS = new HashSet<>();

    public BlueprintArgumentType() {
        for (BlueprintBase base : BlueprintBase.values()) {
            BLUEPRINT_SUBS.add("BLUEPRINT_BASE_" + base.toString());
        }
        for (BlueprintConsole console : BlueprintConsole.values()) {
            BLUEPRINT_SUBS.add("BLUEPRINT_CONSOLE_" + console.toString());
        }
        for (BlueprintFeature feature : BlueprintFeature.values()) {
            BLUEPRINT_SUBS.add("BLUEPRINT_FEATURE_" + feature.toString());
        }
        for (BlueprintPreset preset : BlueprintPreset.values()) {
            BLUEPRINT_SUBS.add("BLUEPRINT_PRESET_" + preset.toString());
        }
        for (BlueprintRoom room : BlueprintRoom.values()) {
            BLUEPRINT_SUBS.add("BLUEPRINT_ROOM_" + room.toString());
        }
        for (BlueprintSonic sonic : BlueprintSonic.values()) {
            BLUEPRINT_SUBS.add("BLUEPRINT_SONIC_" + sonic.toString());
        }
        for (BlueprintTravel travel : BlueprintTravel.values()) {
            BLUEPRINT_SUBS.add("BLUEPRINT_TRAVEL_" + travel.toString());
        }
    }

    @Override
    public String parse(StringReader reader) throws CommandSyntaxException {
        return "";
    }

    @Override
    public <S> String parse(StringReader reader, S source) throws CommandSyntaxException {
        String input = reader.readUnquotedString();
        if (!BLUEPRINT_SUBS.contains(input)) {
            throw ERROR_INVALID_BLUEPRINT.create();
        }
        return input;
    }

    @Override
    public ArgumentType<String> getNativeType() {
        return StringArgumentType.word();
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        for (String d : BLUEPRINT_SUBS) {
            builder.suggest(d);
        }
        return builder.buildFuture();
    }
}
