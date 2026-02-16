package me.eccentric_nz.TARDIS.brigadier;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.commands.artron.ArtronCombine;
import me.eccentric_nz.TARDIS.commands.artron.ArtronTardis;
import me.eccentric_nz.TARDIS.commands.artron.ArtronTimelord;
import org.bukkit.entity.Player;

public class ArtronStorageCommandNode {

    private final TARDIS plugin;

    public ArtronStorageCommandNode(TARDIS plugin) {
        this.plugin = plugin;
    }

    LiteralCommandNode<CommandSourceStack> build() {
        LiteralArgumentBuilder<CommandSourceStack> command = Commands.literal("tardisartron")
                // require a player to execute the command
                .requires(ctx -> ctx.getExecutor() instanceof Player && ctx.getSender().hasPermission("tardis.store"))
                .then(Commands.literal("tardis")
                        .then(Commands.argument("amount", IntegerArgumentType.integer(1))
                                .executes(ctx -> {
                                    Player player = (Player) ctx.getSource().getExecutor();
                                    int amount = IntegerArgumentType.getInteger(ctx, "amount");
                                    new ArtronTardis().transfer(plugin, player, amount);
                                    return Command.SINGLE_SUCCESS;
                                })))
                .then(Commands.literal("timelord")
                        .then(Commands.argument("amount", IntegerArgumentType.integer(1))
                                .executes(ctx -> {
                                    Player player = (Player) ctx.getSource().getExecutor();
                                    int amount = IntegerArgumentType.getInteger(ctx, "amount");
                                    new ArtronTimelord().transfer(plugin, player, amount);
                                    return Command.SINGLE_SUCCESS;
                                })))
                .then(Commands.literal("combine")
                        .executes(ctx -> {
                            Player player = (Player) ctx.getSource().getExecutor();
                            new ArtronCombine().meld(plugin, player);
                            return Command.SINGLE_SUCCESS;
                        }));
        return command.build();
    }
}
