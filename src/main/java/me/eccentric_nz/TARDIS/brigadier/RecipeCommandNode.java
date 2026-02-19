package me.eccentric_nz.TARDIS.brigadier;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.brigadier.arguments.RecipeArgumentType;
import me.eccentric_nz.TARDIS.brigadier.arguments.SeedArgumentType;
import me.eccentric_nz.TARDIS.commands.RecipeUtility;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.messaging.TARDISRecipeLister;
import me.eccentric_nz.TARDIS.recipes.TARDISRecipeCategoryInventory;
import me.eccentric_nz.tardischunkgenerator.worldgen.PlotGenerator;
import org.bukkit.entity.Player;

import java.util.Locale;

public class RecipeCommandNode {

    private final TARDIS plugin;

    public RecipeCommandNode(TARDIS plugin) {
        this.plugin = plugin;
    }

    LiteralCommandNode<CommandSourceStack> build() {
        LiteralArgumentBuilder<CommandSourceStack> command = Commands.literal("tardisrecipe")
                .requires(ctx -> ctx.getSender().hasPermission("tardis.help"))
                .executes(ctx -> {
                    if (ctx.getSource().getSender() instanceof Player player) {
                        player.openInventory(new TARDISRecipeCategoryInventory(plugin).getInventory());
                    } else {
                        new TARDISRecipeLister(plugin, ctx.getSource().getSender()).list();
                    }
                    return Command.SINGLE_SUCCESS;
                })
                .requires(ctx -> ctx.getExecutor() instanceof Player)
                .then(Commands.literal("seed")
                        .then(Commands.argument("type", new SeedArgumentType())
                                .executes(ctx -> {
                                    Player player = (Player) ctx.getSource().getExecutor();
                                    String t = ctx.getArgument("type", String.class);
                                    RecipeUtility.showTARDISRecipe(plugin, player, t);
                                    return Command.SINGLE_SUCCESS;
                                })))
                .then(Commands.literal("tardis")
                        .then(Commands.argument("type", new SeedArgumentType())
                                .executes(ctx -> {
                                    Player player = (Player) ctx.getSource().getExecutor();
                                    String t = ctx.getArgument("type", String.class);
                                    RecipeUtility.showTARDISRecipe(plugin, player, t);
                                    return Command.SINGLE_SUCCESS;
                                })))
                .then(Commands.argument("item", new RecipeArgumentType(plugin))
                        .executes(ctx -> {
                            Player player = (Player) ctx.getSource().getExecutor();
                            String item = ctx.getArgument("item", String.class);
                            RecipeUtility.showItemRecipe(plugin, player, item);
                            return Command.SINGLE_SUCCESS;
                        }));
        return command.build();
    }
}
