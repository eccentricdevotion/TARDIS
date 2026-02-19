package me.eccentric_nz.TARDIS.brigadier;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.commands.TARDISCallRequestCommand;
import org.bukkit.entity.Player;

public class CallCommandNode {

    private final TARDIS plugin;

    public CallCommandNode(TARDIS plugin) {
        this.plugin = plugin;
    }

    LiteralCommandNode<CommandSourceStack> build() {
        LiteralArgumentBuilder<CommandSourceStack> command = Commands.literal("tardiscall")
                .requires(ctx -> ctx.getExecutor() instanceof Player)
                .then(Commands.argument("player", ArgumentTypes.player())
                        .executes(ctx -> {
                            Player player = (Player) ctx.getSource().getExecutor();
                            PlayerSelectorArgumentResolver targetResolver = ctx.getArgument("player", PlayerSelectorArgumentResolver.class);
                            Player requested = targetResolver.resolve(ctx.getSource()).getFirst();
                            new TARDISCallRequestCommand(plugin).requestComeHere(player, requested);
                            return Command.SINGLE_SUCCESS;
                        }));
        return command.build();
    }
}
