package me.eccentric_nz.TARDIS.brigadier;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

public class GamemodeCommandNode {

    LiteralCommandNode<CommandSourceStack> build() {
        LiteralArgumentBuilder<CommandSourceStack> command = Commands.literal("tardisgamemode")
                .requires(ctx -> TARDISPermission.hasPermission(ctx.getSender(), "tardis.admin"))
                .then(Commands.argument("mode", ArgumentTypes.gameMode())
                        .executes(ctx -> {
                            if (ctx.getSource().getSender() instanceof Player player) {
                                GameMode gamemode = ctx.getArgument("mode", GameMode.class);
                                player.setGameMode(gamemode);
                            }
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.argument("player", ArgumentTypes.player())
                        .then(Commands.argument("mode", ArgumentTypes.gameMode())
                                .executes(ctx -> {
                                    PlayerSelectorArgumentResolver targetResolver = ctx.getArgument("player", PlayerSelectorArgumentResolver.class);
                                    Player player = targetResolver.resolve(ctx.getSource()).getFirst();
                                    GameMode gamemode = ctx.getArgument("mode", GameMode.class);
                                    player.setGameMode(gamemode);
                                    return Command.SINGLE_SUCCESS;
                                })));
        return command.build();
    }
}
