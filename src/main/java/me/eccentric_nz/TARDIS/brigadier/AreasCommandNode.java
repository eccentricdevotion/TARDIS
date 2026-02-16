package me.eccentric_nz.TARDIS.brigadier;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.brigadier.arguments.CompassArgumentType;
import me.eccentric_nz.TARDIS.brigadier.arguments.PresetArgumentType;
import me.eccentric_nz.TARDIS.brigadier.suggestions.BlockSuggestions;
import me.eccentric_nz.TARDIS.commands.TARDISCommandHelper;
import me.eccentric_nz.TARDIS.commands.areas.*;
import org.bukkit.entity.Player;

public class AreasCommandNode {

    private final TARDIS plugin;

    public AreasCommandNode(TARDIS plugin) {
        this.plugin = plugin;
    }

    LiteralCommandNode<CommandSourceStack> build() {
        LiteralArgumentBuilder<CommandSourceStack> command = Commands.literal("tardisarea")
                // require a player to execute the command
                .requires(ctx -> ctx.getExecutor() instanceof Player && ctx.getSender().hasPermission("tardis.admin"))
                .executes(ctx -> {
                    new TARDISCommandHelper(plugin).getCommand("tardisarea", ctx.getSource().getSender());
                    return Command.SINGLE_SUCCESS;
                })
                .then(Commands.literal("start")
                        .then(Commands.argument("name", StringArgumentType.word())
                                .executes(ctx -> {
                                    Player player = (Player) ctx.getSource().getExecutor();
                                    String name = StringArgumentType.getString(ctx, "name");
                                    new AreaStart().track(plugin, name, player);
                                    return Command.SINGLE_SUCCESS;
                                })))
                .then(Commands.literal("end")
                        .executes(ctx -> {
                            Player player = (Player) ctx.getSource().getExecutor();
                            new AreaEnd().track(plugin, player);
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("parking")
                        .then(Commands.argument("name", StringArgumentType.word())
                                .then(Commands.argument("distance", IntegerArgumentType.integer())
                                        .executes(ctx -> {
                                            Player player = (Player) ctx.getSource().getExecutor();
                                            String name = StringArgumentType.getString(ctx, "name");
                                            int dist = IntegerArgumentType.getInteger(ctx, "distance");
                                            new AreaParking().set(plugin, player, name, dist);
                                            return Command.SINGLE_SUCCESS;
                                        }))))
                .then(Commands.literal("remove")
                        .then(Commands.argument("name", StringArgumentType.word())
                                .executes(ctx -> {
                                    Player player = (Player) ctx.getSource().getExecutor();
                                    String name = StringArgumentType.getString(ctx, "name");
                                    new AreaRemove().delete(plugin, name, player);
                                    return Command.SINGLE_SUCCESS;
                                })))
                .then(Commands.literal("show")
                        .then(Commands.argument("name", StringArgumentType.word())
                                .executes(ctx -> {
                                    Player player = (Player) ctx.getSource().getExecutor();
                                    String name = StringArgumentType.getString(ctx, "name");
                                    new AreaShow().display(plugin, name, player);
                                    return Command.SINGLE_SUCCESS;
                                })))
                .then(Commands.literal("yard")
                        .then(Commands.argument("name", StringArgumentType.word())
                                .then(Commands.argument("fill_block", StringArgumentType.word())
                                        .suggests(BlockSuggestions::get)
                                        .then(Commands.argument("dock_block", StringArgumentType.word())
                                                .suggests(BlockSuggestions::get)
                                                .executes(ctx -> {
                                                    Player player = (Player) ctx.getSource().getExecutor();
                                                    String name = StringArgumentType.getString(ctx, "name");
                                                    String fill = StringArgumentType.getString(ctx, "fill_block");
                                                    String dock = StringArgumentType.getString(ctx, "dock_block");
                                                    new AreaYard().create(plugin, name, fill, dock, player);
                                                    return Command.SINGLE_SUCCESS;
                                                })))))
                .then(Commands.literal("invisibility")
                        .then(Commands.argument("name", StringArgumentType.word())
                                .then(Commands.argument("flag", new PresetArgumentType(1))
                                        .executes(ctx -> {
                                            Player player = (Player) ctx.getSource().getExecutor();
                                            String name = StringArgumentType.getString(ctx, "name");
                                            String flag = ctx.getArgument("flag", String.class);
                                            new AreaInvisibility().set(plugin, name, flag, player);
                                            return Command.SINGLE_SUCCESS;
                                        }))))
                .then(Commands.literal("direction")
                        .then(Commands.argument("name", StringArgumentType.word())
                                .then(Commands.argument("direction", new CompassArgumentType())
                                        .executes(ctx -> {
                                            Player player = (Player) ctx.getSource().getExecutor();
                                            String name = StringArgumentType.getString(ctx, "name");
                                            String direction = ctx.getArgument("direction", String.class);
                                            new AreaDirection().set(plugin, name, direction, player);
                                            return Command.SINGLE_SUCCESS;
                                        }))))
                .then(Commands.literal("create")
                        .then(Commands.argument("name", StringArgumentType.word())
                                .executes(ctx -> {
                                    Player player = (Player) ctx.getSource().getExecutor();
                                    String name = StringArgumentType.getString(ctx, "name");
                                    new AreaCreate().make(plugin, name, player);
                                    return Command.SINGLE_SUCCESS;
                                })))
                .then(Commands.literal("add")
                        .then(Commands.argument("name", StringArgumentType.word())
                                .executes(ctx -> {
                                    Player player = (Player) ctx.getSource().getExecutor();
                                    String name = StringArgumentType.getString(ctx, "name");
                                    new AreaAdd().setLocation(plugin, name, player);
                                    return Command.SINGLE_SUCCESS;
                                })))
                .then(Commands.literal("edit")
                        .then(Commands.argument("name", StringArgumentType.word())
                                .executes(ctx -> {
                                    Player player = (Player) ctx.getSource().getExecutor();
                                    String name = StringArgumentType.getString(ctx, "name");
                                    new AreaEdit().open(plugin, name, player);
                                    return Command.SINGLE_SUCCESS;
                                })));
        return command.build();
    }
}
