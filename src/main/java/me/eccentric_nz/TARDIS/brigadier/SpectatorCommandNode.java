package me.eccentric_nz.TARDIS.brigadier;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;
import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

public class SpectatorCommandNode {

    private final TARDIS plugin;

    public SpectatorCommandNode(TARDIS plugin) {
        this.plugin = plugin;
    }

    LiteralCommandNode<CommandSourceStack> build() {
        LiteralArgumentBuilder<CommandSourceStack> command = Commands.literal("tgmsp")
                .requires(ctx -> ctx.getSender().hasPermission("tardis.admin"))
                .executes(ctx -> {
                    if (ctx.getSource().getExecutor() instanceof Player player) {
                        player.setGameMode(GameMode.SPECTATOR);
                    }
                    return Command.SINGLE_SUCCESS;
                })
                .then(Commands.argument("player", ArgumentTypes.player())
                        .executes(ctx -> {
                            PlayerSelectorArgumentResolver targetResolver = ctx.getArgument("player", PlayerSelectorArgumentResolver.class);
                            Player player = targetResolver.resolve(ctx.getSource()).getFirst();
                            player.setGameMode(GameMode.SPECTATOR);
                            return Command.SINGLE_SUCCESS;
                        }));
        return command.build();
    }
}
