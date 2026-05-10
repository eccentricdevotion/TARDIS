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
    private final Set<String> RECIPE_SUBS = new HashSet<>();

    public RecipeArgumentType(TARDIS plugin) {
        RECIPE_SUBS.add("seed");
        RECIPE_SUBS.add("tardis");
        for (RecipeItem recipeItem : RecipeItem.values()) {
            if (recipeItem.getCategory() != RecipeCategory.UNCRAFTABLE && recipeItem.getCategory() != RecipeCategory.UNUSED && recipeItem.getCategory() != RecipeCategory.CHEMISTRY) {
                RECIPE_SUBS.add(recipeItem.toTabCompletionString());
            }
        }
        for (String d : plugin.getCustomDoorsConfig().getKeys(false)) {
            RECIPE_SUBS.add("door-" + d.toLowerCase(Locale.ROOT));
        }
        for (String r : plugin.getCustomRotorsConfig().getKeys(false)) {
            RECIPE_SUBS.add("time-rotor-" + r.toLowerCase(Locale.ROOT));
        }
        for (String c : plugin.getCustomConsolesConfig().getConfigurationSection("consoles").getKeys(false)) {
            RECIPE_SUBS.add("console-" + c.toLowerCase(Locale.ROOT));
        }
        // remove recipes form modules that are not enabled
        if (!plugin.getConfig().getBoolean("modules.vortex_manipulator")) {
            RECIPE_SUBS.remove("vortex-manipulator");
        }
        if (!plugin.getConfig().getBoolean("modules.regeneration")) {
            RECIPE_SUBS.remove("elixir-of-life");
        }
        if (!plugin.getConfig().getBoolean("modules.sonic_blaster")) {
            RECIPE_SUBS.remove("sonic-blaster");
            RECIPE_SUBS.remove("blaster-battery");
            RECIPE_SUBS.remove("landing-pad");
        }
        if (!plugin.getConfig().getBoolean("modules.weeping_angels")) {
            RECIPE_SUBS.remove("judoon-ammunition");
            RECIPE_SUBS.remove("k9");
        }
    }

    @Override
    public String parse(StringReader reader) {
        return "";
    }

    @Override
    public <S> String parse(StringReader reader, S source) throws CommandSyntaxException {
        String input = reader.readUnquotedString();
        if (!RECIPE_SUBS.contains(input)) {
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
        RECIPE_SUBS.stream()
                .filter(r -> r.toLowerCase(Locale.ROOT).startsWith(builder.getRemainingLowerCase()))
                .forEach(builder::suggest);
        return builder.buildFuture();
    }
}
