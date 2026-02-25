package me.eccentric_nz.TARDIS.brigadier;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.brigadier.arguments.ShopItemArgumentType;
import me.eccentric_nz.TARDIS.commands.TARDISCallRequestCommand;
import me.eccentric_nz.tardisshop.ShopUtility;
import org.bukkit.entity.Player;

public class ShopCommandNode {

    private final TARDIS plugin;

    public ShopCommandNode(TARDIS plugin) {
        this.plugin = plugin;
    }

    LiteralCommandNode<CommandSourceStack> build() {
        LiteralArgumentBuilder<CommandSourceStack> command = Commands.literal("tardisshop")
                .requires(ctx -> ctx.getExecutor() instanceof Player)
                .then(Commands.literal("add")
                        .then(Commands.argument("item", new ShopItemArgumentType(plugin))
                                .executes(ctx -> {
                                    Player player = (Player) ctx.getSource().getExecutor();
                                    String i = ctx.getArgument("item", String.class);
                                    ShopUtility.add(plugin, player, i);
                                    return Command.SINGLE_SUCCESS;
                                })))
                .then(Commands.literal("remove")
                        .executes(ctx -> {
                            Player player = (Player) ctx.getSource().getExecutor();
                            ShopUtility.remove(plugin, player);
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("update")
                        .executes(ctx -> {
                            ShopUtility.update(plugin);
                            return Command.SINGLE_SUCCESS;
                        }));
        return command.build();
    }
}
