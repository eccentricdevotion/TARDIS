package me.eccentric_nz.TARDIS.brigadier;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.brigadier.arguments.DirectionArgumentType;
import me.eccentric_nz.TARDIS.commands.GravityCommand;
import me.eccentric_nz.TARDIS.commands.TARDISCallRequestCommand;
import me.eccentric_nz.TARDIS.commands.TARDISCommandHelper;
import org.bukkit.entity.Player;

public class GravityCommandNode {

    private final TARDIS plugin;

    public GravityCommandNode(TARDIS plugin) {
        this.plugin = plugin;
    }

    LiteralCommandNode<CommandSourceStack> build() {
        LiteralArgumentBuilder<CommandSourceStack> command = Commands.literal("tardisgravity")
                .requires(ctx -> ctx.getExecutor() instanceof Player && ctx.getSender().hasPermission("tardis.gravity"))
                .executes(ctx -> {
                    new TARDISCommandHelper(plugin).getCommand("tardisgravity", ctx.getSource().getSender());
                    return Command.SINGLE_SUCCESS;
                })
                .then(Commands.literal("remove")
                        .executes(ctx->{
                            Player player = (Player) ctx.getSource().getExecutor();
                            new GravityCommand(plugin).remove(player);
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("down")
                        .executes(ctx->{
                            Player player = (Player) ctx.getSource().getExecutor();
                            new GravityCommand(plugin).make(player, "down", 0d, 0.5d);
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.argument("direction", new DirectionArgumentType())
                        .then(Commands.argument("distance", IntegerArgumentType.integer(0))
                                .executes(ctx -> {
                                    Player player = (Player) ctx.getSource().getExecutor();
                                    String direction = ctx.getArgument("direction", String.class);
                                    double distance = DoubleArgumentType.getDouble(ctx, "distance");
                                    new GravityCommand(plugin).make(player, direction, distance, 0.5d);
                                    return Command.SINGLE_SUCCESS;
                                })
                                .then(Commands.argument("velocity", DoubleArgumentType.doubleArg(0))
                                        .executes(ctx->{
                                            Player player = (Player) ctx.getSource().getExecutor();
                                            String direction = ctx.getArgument("direction", String.class);
                                            double distance = DoubleArgumentType.getDouble(ctx, "distance");
                                            double velocity = DoubleArgumentType.getDouble(ctx, "velocity");
                                            new GravityCommand(plugin).make(player, direction, distance, velocity);
                                            return Command.SINGLE_SUCCESS;
                                }))));
        return command.build();
    }
}
