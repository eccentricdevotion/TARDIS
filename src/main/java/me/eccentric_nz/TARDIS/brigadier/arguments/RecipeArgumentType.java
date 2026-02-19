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
import me.eccentric_nz.TARDIS.enumeration.RecipeCategory;
import me.eccentric_nz.TARDIS.enumeration.RecipeItem;
import net.kyori.adventure.text.Component;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class RecipeArgumentType  implements CustomArgumentType<String, String> {

    private static final SimpleCommandExceptionType ERROR_INVALID_RECIPE = new SimpleCommandExceptionType(
            MessageComponentSerializer.message().serialize(Component.text("Invalid recipe specified!"))
    );
    private final Set<String> ROOT_SUBS = new HashSet<>();

    public RecipeArgumentType(TARDIS plugin) {
        ROOT_SUBS.add("seed");
        ROOT_SUBS.add("tardis");
        for (RecipeItem recipeItem : RecipeItem.values()) {
            if (recipeItem.getCategory() != RecipeCategory.UNCRAFTABLE && recipeItem.getCategory() != RecipeCategory.UNUSED && recipeItem.getCategory() != RecipeCategory.CHEMISTRY) {
                ROOT_SUBS.add(recipeItem.toTabCompletionString());
            }
        }
        for (String d : plugin.getCustomDoorsConfig().getKeys(false)) {
            ROOT_SUBS.add("door-" + d.toLowerCase(Locale.ROOT));
        }
        for (String r : plugin.getCustomRotorsConfig().getKeys(false)) {
            ROOT_SUBS.add("time-rotor-" + r.toLowerCase(Locale.ROOT));
        }
        for (String c : plugin.getCustomConsolesConfig().getConfigurationSection("consoles").getKeys(false)) {
            ROOT_SUBS.add("console-" + c.toLowerCase(Locale.ROOT));
        }
        // remove recipes form modules that are not enabled
        if (!plugin.getConfig().getBoolean("modules.vortex_manipulator")) {
            ROOT_SUBS.remove("vortex-manipulator");
        }
        if (!plugin.getConfig().getBoolean("modules.regeneration")) {
            ROOT_SUBS.remove("elixir-of-life");
        }
        if (!plugin.getConfig().getBoolean("modules.sonic_blaster")) {
            ROOT_SUBS.remove("sonic-blaster");
            ROOT_SUBS.remove("blaster-battery");
            ROOT_SUBS.remove("landing-pad");
        }
        if (!plugin.getConfig().getBoolean("modules.weeping_angels")) {
            ROOT_SUBS.remove("judoon-ammunition");
            ROOT_SUBS.remove("k9");
        }
    }

    @Override
    public String parse(StringReader reader) throws CommandSyntaxException {
        return "";
    }

    @Override
    public <S> String parse(StringReader reader, S source) throws CommandSyntaxException {
        String input = reader.readUnquotedString();
        if (!ROOT_SUBS.contains(input)) {
            throw ERROR_INVALID_RECIPE.create();
        }
        return input;
    }

    @Override
    public ArgumentType<String> getNativeType() {
        return StringArgumentType.word();
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        for (String d : ROOT_SUBS) {
            builder.suggest(d);
        }
        return builder.buildFuture();
    }
}
