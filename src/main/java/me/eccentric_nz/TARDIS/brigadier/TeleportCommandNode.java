package me.eccentric_nz.TARDIS.brigadier;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.BlockPositionResolver;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;
import io.papermc.paper.math.BlockPosition;
import me.eccentric_nz.TARDIS.commands.utils.TeleportUtility;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class TeleportCommandNode {

    LiteralCommandNode<CommandSourceStack> build() {
        LiteralArgumentBuilder<CommandSourceStack> command = Commands.literal("tardisteleport")
                .requires(ctx -> ctx.getExecutor().hasPermission("tardis.admin"))
                .then(Commands.argument("world", ArgumentTypes.world())
                        .executes(ctx -> {
                            if (ctx.getSource().getExecutor() instanceof Player player) {
                                World world = ctx.getArgument("world", World.class);
                                TeleportUtility.teleport(player, world);
                            }
                            return Command.SINGLE_SUCCESS;
                        })
                        .then(Commands.argument("coords", ArgumentTypes.blockPosition())
                                .then(Commands.argument("nfp", StringArgumentType.word())
                                        .executes(ctx -> {
                                            String nfp = StringArgumentType.getString(ctx, "nfp");
                                            if (nfp.equals("not_for_players")) {
                                                World world = ctx.getArgument("world", World.class);
                                                BlockPositionResolver resolver = ctx.getArgument("coords", BlockPositionResolver.class);
                                                BlockPosition pos = resolver.resolve(ctx.getSource());
                                                Player player = (Player) ctx.getSource().getExecutor();
                                                TeleportUtility.notForPlayers(player, world, pos);
                                            }
                                            return Command.SINGLE_SUCCESS;
                                        })))
                        .then(Commands.argument("player", ArgumentTypes.player())
                                .executes(ctx -> {
                                    PlayerSelectorArgumentResolver targetResolver = ctx.getArgument("player", PlayerSelectorArgumentResolver.class);
                                    Player player = targetResolver.resolve(ctx.getSource()).getFirst();
                                    World world = ctx.getArgument("world", World.class);
                                    TeleportUtility.teleport(player, world);
                                    return Command.SINGLE_SUCCESS;
                                })));
        return command.build();
    }
}
