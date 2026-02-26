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
import me.eccentric_nz.tardischunkgenerator.custombiome.CubicMaterial;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class TreeBlockArgumentType implements CustomArgumentType<String, String> {

    private static final SimpleCommandExceptionType ERROR_INVALID_BLOCK = new SimpleCommandExceptionType(
            MessageComponentSerializer.message().serialize(Component.text("Invalid tree block specified!"))
    );
    private final Set<String> BLOCKS = new HashSet<>();

    public TreeBlockArgumentType() {
        for (Material m : CubicMaterial.cubes) {
            BLOCKS.add(m.toString());
        }
    }

    @Override
    public String parse(StringReader reader) {
        return "";
    }

    @Override
    public <S> String parse(StringReader reader, S source) throws CommandSyntaxException {
        String input = reader.readUnquotedString();
        if (!BLOCKS.contains(input)) {
            throw ERROR_INVALID_BLOCK.create();
        }
        return input;
    }

    @Override
    public ArgumentType<String> getNativeType() {
        return StringArgumentType.word();
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        for (String d : BLOCKS) {
            builder.suggest(d);
        }
        return builder.buildFuture();
    }
}
