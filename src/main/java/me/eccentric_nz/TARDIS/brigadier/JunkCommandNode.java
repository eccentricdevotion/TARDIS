package me.eccentric_nz.TARDIS.brigadier;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.brigadier.suggestions.BlockSuggestions;
import me.eccentric_nz.TARDIS.junk.*;
import org.bukkit.entity.Player;

public class JunkCommandNode {

    private final TARDIS plugin;

    public JunkCommandNode(TARDIS plugin) {
        this.plugin = plugin;
    }

    LiteralCommandNode<CommandSourceStack> build() {
        LiteralArgumentBuilder<CommandSourceStack> command = Commands.literal("tardisjunk")
                .executes(ctx -> {
                    new JunkFind(plugin).find(ctx.getSource().getSender());
                    return Command.SINGLE_SUCCESS;
                })
                .then(Commands.literal("create")
                        .executes(ctx -> {
                            if (ctx.getSource().getSender() instanceof Player player) {
                                new JunkCreator(plugin, player).createJunkTARDIS();
                            }
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("delete")
                        .executes(ctx -> {
                            new JunkDelete(plugin).delete(ctx.getSource().getSender());
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("find")
                        .executes(ctx -> {
                            new JunkFind(plugin).find(ctx.getSource().getSender());
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("floor")
                        .then(Commands.argument("block", StringArgumentType.word())
                                .suggests(BlockSuggestions::get)
                                .executes(ctx -> {
                                    String m = StringArgumentType.getString(ctx, "block");
                                    new JunkFloorWall(plugin).setJunkWallOrFloor(ctx.getSource().getSender(), "floor", m);
                                    return Command.SINGLE_SUCCESS;
                                })))
                .then(Commands.literal("return")
                        .executes(ctx -> {
                            new JunkReturn(plugin).recall(ctx.getSource().getSender(), false);
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("time")
                        .executes(ctx -> {
                            new JunkTime(plugin).elapsed(ctx.getSource().getSender());
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("wall")
                        .then(Commands.argument("block", StringArgumentType.word())
                                .suggests(BlockSuggestions::get)
                                .executes(ctx -> {
                                    String m = StringArgumentType.getString(ctx, "block");
                                    new JunkFloorWall(plugin).setJunkWallOrFloor(ctx.getSource().getSender(), "wall", m);
                                    return Command.SINGLE_SUCCESS;
                                })));
        return command.build();
    }
}
