package me.eccentric_nz.TARDIS.brigadier;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.brigadier.arguments.OnOffArgumentType;
import me.eccentric_nz.tardisregeneration.RegenerationUtility;
import org.bukkit.entity.Player;

public class RegenerationCommandNode {

    private final TARDIS plugin;

    public RegenerationCommandNode(TARDIS plugin) {
        this.plugin = plugin;
    }

    public LiteralCommandNode<CommandSourceStack> build() {
        LiteralArgumentBuilder<CommandSourceStack> command = Commands.literal("tardisregeneration")
                .then(Commands.literal("add")
                        .then(Commands.argument("player", ArgumentTypes.player())
                                .then(Commands.argument("amount", IntegerArgumentType.integer(1))
                                        .executes(ctx -> {
                                            PlayerSelectorArgumentResolver targetResolver = ctx.getArgument("player", PlayerSelectorArgumentResolver.class);
                                            Player player = targetResolver.resolve(ctx.getSource()).getFirst();
                                            int amount = IntegerArgumentType.getInteger(ctx, "amount");
                                            RegenerationUtility.set(plugin, ctx.getSource().getSender(), player, amount, true);
                                            return Command.SINGLE_SUCCESS;
                                        }))))
                .then(Commands.literal("remove")
                        .then(Commands.argument("player", ArgumentTypes.player())
                                .then(Commands.argument("amount", IntegerArgumentType.integer(1))
                                        .executes(ctx -> {
                                            PlayerSelectorArgumentResolver targetResolver = ctx.getArgument("player", PlayerSelectorArgumentResolver.class);
                                            Player player = targetResolver.resolve(ctx.getSource()).getFirst();
                                            int amount = IntegerArgumentType.getInteger(ctx, "amount");
                                            RegenerationUtility.set(plugin, ctx.getSource().getSender(), player, amount, false);
                                            return Command.SINGLE_SUCCESS;
                                        }))))
                .then(Commands.literal("block")
                        .then(Commands.argument("toggle", new OnOffArgumentType())
                                .executes(ctx -> {
                                    Player player = (Player) ctx.getSource().getExecutor();
                                    String o = ctx.getArgument("toggle", String.class);
                                    RegenerationUtility.block(plugin, player, o);
                                    return Command.SINGLE_SUCCESS;
                                })));
        return command.build();
    }
}
