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
import me.eccentric_nz.tardischemistry.compound.Compound;
import me.eccentric_nz.tardischemistry.lab.Lab;
import me.eccentric_nz.tardischemistry.product.Product;
import net.kyori.adventure.text.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class FormulaArgumentType implements CustomArgumentType<String, String> {

    private static final SimpleCommandExceptionType ERROR_INVALID_FORMULA = new SimpleCommandExceptionType(
            MessageComponentSerializer.message().serialize(Component.text("Invalid formula specified!"))
    );
    private final Set<String> FORMULAS = new HashSet<>();

    public FormulaArgumentType() {
        for (Compound compound : Compound.values()) {
            FORMULAS.add(compound.toString());
        }
        for (Product product : Product.values()) {
            FORMULAS.add(product.toString());
        }
        for (Lab lab : Lab.values()) {
            FORMULAS.add(lab.toString());
        }
    }

    @Override
    public String parse(StringReader reader) {
        return "";
    }

    @Override
    public <S> String parse(StringReader reader, S source) throws CommandSyntaxException {
        String input = reader.readUnquotedString();
        if (!FORMULAS.contains(input)) {
            throw ERROR_INVALID_FORMULA.create();
        }
        return input;
    }

    @Override
    public ArgumentType<String> getNativeType() {
        return StringArgumentType.word();
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        for (String h : FORMULAS) {
            builder.suggest(h);
        }
        return builder.buildFuture();
    }
}
