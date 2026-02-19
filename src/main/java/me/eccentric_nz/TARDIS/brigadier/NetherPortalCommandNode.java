package me.eccentric_nz.TARDIS.brigadier;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.BlockPositionResolver;
import io.papermc.paper.math.BlockPosition;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.commands.utils.NetherPortalUtility;
import org.bukkit.entity.Player;

public class NetherPortalCommandNode {

    private final TARDIS plugin;

    public NetherPortalCommandNode(TARDIS plugin) {
        this.plugin = plugin;
    }

    LiteralCommandNode<CommandSourceStack> build() {
        LiteralArgumentBuilder<CommandSourceStack> command = Commands.literal("tardisnetherportal")
                .requires(ctx -> ctx.getSender().hasPermission("tardis.help"))
                .executes(ctx -> {
                    if (ctx.getSource().getExecutor() instanceof Player player) {
                        NetherPortalUtility.o2n(plugin, player);
                    }
                    return Command.SINGLE_SUCCESS;
                })
                .then(Commands.argument("coords", ArgumentTypes.blockPosition())
                        .then(Commands.argument("environment", StringArgumentType.word())
                                .suggests((ctx, builder) -> {
                                    builder.suggest("overworld");
                                    builder.suggest("nether");
                                    return builder.buildFuture();
                                })
                                .executes(ctx -> {
                                    BlockPositionResolver resolver = ctx.getArgument("coords", BlockPositionResolver.class);
                                    BlockPosition pos = resolver.resolve(ctx.getSource());
                                    String e = StringArgumentType.getString(ctx, "environment");
                                    NetherPortalUtility.o2n(plugin, ctx.getSource().getSender(), pos.blockX(), pos.blockY(), pos.blockZ(), e.equalsIgnoreCase("overworld"));
                                    return Command.SINGLE_SUCCESS;
                                })));
        return command.build();
    }
}
