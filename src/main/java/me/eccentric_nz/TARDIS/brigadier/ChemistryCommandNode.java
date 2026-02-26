package me.eccentric_nz.TARDIS.brigadier;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.brigadier.arguments.ChemistryCreativeArgumentType;
import me.eccentric_nz.TARDIS.brigadier.arguments.ChemistryGuiArgumentType;
import me.eccentric_nz.TARDIS.brigadier.arguments.FormulaArgumentType;
import me.eccentric_nz.tardischemistry.ChemistryUtility;
import me.eccentric_nz.tardischemistry.formula.FormulaCommand;
import org.bukkit.entity.Player;

public class ChemistryCommandNode {

    private final TARDIS plugin;

    public ChemistryCommandNode(TARDIS plugin) {
        this.plugin = plugin;
    }

    public LiteralCommandNode<CommandSourceStack> build() {
        LiteralArgumentBuilder<CommandSourceStack> command = Commands.literal("tardischemistry")
                .requires(ctx -> ctx.getExecutor() instanceof Player)
                .then(Commands.literal("gui")
                        .then(Commands.literal("creative")
                                .then(Commands.argument("which", new ChemistryCreativeArgumentType())
                                        .executes(ctx -> {
                                            Player player = (Player) ctx.getSource().getExecutor();
                                            String w = ctx.getArgument("which", String.class);
                                            ChemistryUtility.creative(plugin, player, w);
                                            return Command.SINGLE_SUCCESS;
                                        })))
                        .then(Commands.argument("which", new ChemistryGuiArgumentType())
                                .executes(ctx -> {
                                    Player player = (Player) ctx.getSource().getExecutor();
                                    String g = ctx.getArgument("which", String.class);
                                    ChemistryUtility.open(plugin, player, g);
                                    return Command.SINGLE_SUCCESS;
                                })))
                .then(Commands.literal("formula")
                        .then(Commands.argument("item", new FormulaArgumentType()))
                        .executes(ctx -> {
                            Player player = (Player) ctx.getSource().getExecutor();
                            if (TARDISPermission.hasPermission(player, "tardis.formula.show")) {
                                String w = ctx.getArgument("item", String.class);
                                new FormulaCommand(plugin).show(player, w);
                            }
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("recipe")
                        .then(Commands.argument("block", new ChemistryGuiArgumentType())
                                .executes(ctx -> {
                                    Player player = (Player) ctx.getSource().getExecutor();
                                    String b = ctx.getArgument("block", String.class);
                                    ChemistryUtility.recipe(plugin, player, b);
                                    return Command.SINGLE_SUCCESS;
                                })));
        return command.build();
    }
}
