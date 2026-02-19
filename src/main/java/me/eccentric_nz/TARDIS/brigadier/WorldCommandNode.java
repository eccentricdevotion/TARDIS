package me.eccentric_nz.TARDIS.brigadier;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.brigadier.arguments.EnvironmentArgument;
import me.eccentric_nz.TARDIS.brigadier.arguments.PlanetArgumentType;
import me.eccentric_nz.TARDIS.brigadier.arguments.WorldTypeArgument;
import me.eccentric_nz.TARDIS.brigadier.suggestions.BlockSuggestions;
import me.eccentric_nz.TARDIS.commands.tardis.SaveIconCommand;
import me.eccentric_nz.TARDIS.commands.utils.WorldUtility;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class WorldCommandNode {

    private final TARDIS plugin;

    public WorldCommandNode(TARDIS plugin) {
        this.plugin = plugin;
    }

    LiteralCommandNode<CommandSourceStack> build() {
        LiteralArgumentBuilder<CommandSourceStack> command = Commands.literal("tardisworld")
                .requires(ctx -> ctx.getExecutor().hasPermission("tardis.admin"))
                .then(Commands.literal("load")
                        // load [world] <WorldType> <Environment> <generator>
                        .then(Commands.argument("world", StringArgumentType.word())
                                .executes(ctx -> {
                                    String world = StringArgumentType.getString(ctx, "world");
                                    if (plugin.getServer().getWorld(world) != null) {
                                        plugin.getMessenger().send(ctx.getSource().getSender(), TardisModule.TARDIS, "WORLD_LOADED", world);
                                    } else {
                                        WorldUtility.load(plugin, ctx.getSource().getSender(), world, "", "", "");
                                    }
                                    return Command.SINGLE_SUCCESS;
                                }).then(Commands.argument("world_type", new WorldTypeArgument())
                                        .executes(ctx -> {
                                            String world = StringArgumentType.getString(ctx, "world");
                                            String t = ctx.getArgument("world_type", String.class);
                                            if (plugin.getServer().getWorld(world) != null) {
                                                plugin.getMessenger().send(ctx.getSource().getSender(), TardisModule.TARDIS, "WORLD_LOADED", world);
                                            } else {
                                                WorldUtility.load(plugin, ctx.getSource().getSender(), world, t, "", "");
                                            }
                                            return Command.SINGLE_SUCCESS;
                                        })
                                        .then(Commands.argument("environment", new EnvironmentArgument())
                                                .executes(ctx -> {
                                                    String world = StringArgumentType.getString(ctx, "world");
                                                    String t = ctx.getArgument("world_type", String.class);
                                                    String e = ctx.getArgument("environment", String.class);
                                                    if (plugin.getServer().getWorld(world) != null) {
                                                        plugin.getMessenger().send(ctx.getSource().getSender(), TardisModule.TARDIS, "WORLD_LOADED", world);
                                                    } else {
                                                        WorldUtility.load(plugin, ctx.getSource().getSender(), world, t, e, "");
                                                    }
                                                    return Command.SINGLE_SUCCESS;
                                                }))
                                        .then(Commands.argument("generator", StringArgumentType.greedyString())
                                                .executes(ctx -> {
                                                    String world = StringArgumentType.getString(ctx, "world");
                                                    String t = ctx.getArgument("world_type", String.class);
                                                    String e = ctx.getArgument("environment", String.class);
                                                    String g = StringArgumentType.getString(ctx, "environment");
                                                    if (plugin.getServer().getWorld(world) != null) {
                                                        plugin.getMessenger().send(ctx.getSource().getSender(), TardisModule.TARDIS, "WORLD_LOADED", world);
                                                    } else {
                                                        WorldUtility.load(plugin, ctx.getSource().getSender(), world, t, e, g);
                                                    }
                                                    return Command.SINGLE_SUCCESS;
                                                })))))
                .then(Commands.literal("unload")
                        .then(Commands.argument("world", ArgumentTypes.world())
                                .executes(ctx -> {
                                    World world = ctx.getArgument("world", World.class);
                                    WorldUtility.unload(plugin, ctx.getSource().getSender(), world);
                                    return Command.SINGLE_SUCCESS;
                                })))
                .then(Commands.literal("enable")
                        .then(Commands.argument("planet", new PlanetArgumentType())
                                .executes(ctx -> {
                                    String world = ctx.getArgument("planet", String.class);
                                    WorldUtility.enable(plugin, ctx.getSource().getSender(), world);
                                    return Command.SINGLE_SUCCESS;
                                })))
                .then(Commands.literal("disable")
                        .then(Commands.argument("planet", new PlanetArgumentType())
                                .executes(ctx -> {
                                    String p = ctx.getArgument("planet", String.class);
                                    World world = plugin.getServer().getWorld(p);
                                    if (world != null) {
                                        WorldUtility.disable(plugin, ctx.getSource().getSender(), world);
                                    }
                                    return Command.SINGLE_SUCCESS;
                                })))
                .then(Commands.literal("gm")
                        .then(Commands.argument("world", ArgumentTypes.world())
                                .then(Commands.argument("gamemode", ArgumentTypes.gameMode())
                                        .executes(ctx -> {
                                            String w = ctx.getArgument("world", World.class).getName();
                                            GameMode gamemode = ctx.getArgument("gamemode", GameMode.class);
                                            WorldUtility.setGameMode(plugin, ctx.getSource().getSender(), w, gamemode);
                                            return Command.SINGLE_SUCCESS;
                                        }))))
                .then(Commands.literal("rename")
                        .then(Commands.argument("world", ArgumentTypes.world())
                                .then(Commands.argument("new_name", StringArgumentType.word())
                                        .executes(ctx -> {
                                            World world = ctx.getArgument("world", World.class);
                                            String n = StringArgumentType.getString(ctx, "new_name");
                                            WorldUtility.rename(plugin, ctx.getSource().getSender(), world, n);
                                            return Command.SINGLE_SUCCESS;
                                        }))))
                .then(Commands.literal("info")
                        .executes(ctx -> {
                            if (ctx.getSource().getSender() instanceof Player player) {
                                WorldUtility.sendInfo(plugin, player);
                            }
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("dimension_icon")
                        .then(Commands.argument("world", StringArgumentType.word())
                                .then(Commands.argument("material", StringArgumentType.word())
                                        .suggests(BlockSuggestions::get)
                                        .executes(ctx -> {
                                            String w = StringArgumentType.getString(ctx, "world");
                                            String m = StringArgumentType.getString(ctx, "material");
                                            new SaveIconCommand(plugin).changeIcon(ctx.getSource().getSender(), w, m, true);
                                            return Command.SINGLE_SUCCESS;
                                        }))));
        return command.build();
    }
}
