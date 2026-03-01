package me.eccentric_nz.TARDIS.brigadier;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.brigadier.arguments.OnOffArgumentType;
import me.eccentric_nz.TARDIS.commands.handles.*;
import me.eccentric_nz.TARDIS.handles.HandlesWeirdness;
import me.eccentric_nz.TARDIS.handles.wiki.HandlesWiki;
import me.eccentric_nz.TARDIS.handles.wiki.SearchDialog;
import net.kyori.adventure.audience.Audience;
import org.bukkit.entity.Player;

import java.util.UUID;

public class HandlesCommandNode {

    private final TARDIS plugin;

    public HandlesCommandNode(TARDIS plugin) {
        this.plugin = plugin;
    }

    LiteralCommandNode<CommandSourceStack> build() {
        LiteralArgumentBuilder<CommandSourceStack> command = Commands.literal("handles")
                .then(Commands.literal("disk")
                        .then(Commands.argument("name", StringArgumentType.greedyString())
                                .executes(ctx -> {
                                    if (ctx.getSource().getSender() instanceof Player player) {
                                        String greedy = StringArgumentType.getString(ctx, "name");
                                        new DiskCommand(plugin).renameDisk(player, greedy);
                                    }
                                    return Command.SINGLE_SUCCESS;
                                })))
                .then(Commands.literal("remove")
                        .executes(ctx -> {
                            if (ctx.getSource().getSender() instanceof Player player) {
                                new RemoveCommand(plugin).purge(player);
                            }
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("takeoff")
                        .then(Commands.argument("uuid", ArgumentTypes.uuid())
                                .then(Commands.argument("id", IntegerArgumentType.integer(1))
                                        .executes(ctx -> {
                                            if (ctx.getSource().getSender().hasPermission("tardis.admin")) {
                                                UUID uuid = ctx.getArgument("uuid", UUID.class);
                                                Player player = plugin.getServer().getPlayer(uuid);
                                                int id = IntegerArgumentType.getInteger(ctx, "id");
                                                new TakeOffCommand(plugin).enterVortex(player, uuid.toString(), id);
                                            }
                                            return Command.SINGLE_SUCCESS;
                                        }))))
                .then(Commands.literal("land")
                        .then(Commands.argument("uuid", ArgumentTypes.uuid())
                                .then(Commands.argument("id", IntegerArgumentType.integer(1))
                                        .executes(ctx -> {
                                            if (ctx.getSource().getSender().hasPermission("tardis.admin")) {
                                                UUID uuid = ctx.getArgument("uuid", UUID.class);
                                                Player player = plugin.getServer().getPlayer(uuid);
                                                int id = IntegerArgumentType.getInteger(ctx, "id");
                                                new LandCommand(plugin).exitVortex(player, id, uuid.toString());
                                            }
                                            return Command.SINGLE_SUCCESS;
                                        }))))
                .then(Commands.literal("scan")
                        .then(Commands.argument("uuid", ArgumentTypes.uuid())
                                .then(Commands.argument("id", IntegerArgumentType.integer(1))
                                        .executes(ctx -> {
                                            if (ctx.getSource().getSender().hasPermission("tardis.admin")) {
                                                UUID uuid = ctx.getArgument("uuid", UUID.class);
                                                Player player = plugin.getServer().getPlayer(uuid);
                                                int id = IntegerArgumentType.getInteger(ctx, "id");
                                                new ScanCommand(plugin, player, id).sayScan();
                                            }
                                            return Command.SINGLE_SUCCESS;
                                        }))))
                .then(Commands.literal("lock")
                        .then(Commands.argument("uuid", ArgumentTypes.uuid())
                                .then(Commands.argument("id", IntegerArgumentType.integer(1))
                                        .executes(ctx -> {
                                            if (ctx.getSource().getSender().hasPermission("tardis.admin")) {
                                                UUID uuid = ctx.getArgument("uuid", UUID.class);
                                                Player player = plugin.getServer().getPlayer(uuid);
                                                int id = IntegerArgumentType.getInteger(ctx, "id");
                                                new LockUnlockCommand(plugin).toggleLock(player, id, true);
                                            }
                                            return Command.SINGLE_SUCCESS;
                                        }))))
                .then(Commands.literal("unlock")
                        .then(Commands.argument("uuid", ArgumentTypes.uuid())
                                .then(Commands.argument("id", IntegerArgumentType.integer(1))
                                        .executes(ctx -> {
                                            if (ctx.getSource().getSender().hasPermission("tardis.admin")) {
                                                UUID uuid = ctx.getArgument("uuid", UUID.class);
                                                Player player = plugin.getServer().getPlayer(uuid);
                                                int id = IntegerArgumentType.getInteger(ctx, "id");
                                                new LockUnlockCommand(plugin).toggleLock(player, id, false);
                                            }
                                            return Command.SINGLE_SUCCESS;
                                        }))))
                .then(Commands.literal("remind")
                        .then(Commands.argument("uuid", ArgumentTypes.uuid())
                                .then(Commands.argument("when", IntegerArgumentType.integer(1))
                                        .then(Commands.argument("message", StringArgumentType.greedyString())
                                                .executes(ctx -> {
                                                    if (ctx.getSource().getSender().hasPermission("tardis.admin")) {
                                                        UUID uuid = ctx.getArgument("uuid", UUID.class);
                                                        Player player = plugin.getServer().getPlayer(uuid);
                                                        int when = IntegerArgumentType.getInteger(ctx, "when");
                                                        String message = StringArgumentType.getString(ctx, "message");
                                                        new RemindCommand(plugin).doReminder(player, message, when);
                                                    }
                                                    return Command.SINGLE_SUCCESS;
                                                })))))
                .then(Commands.literal("say")
                        .then(Commands.argument("uuid", ArgumentTypes.uuid())
                                .then(Commands.argument("message", StringArgumentType.greedyString())
                                        .executes(ctx -> {
                                            if (ctx.getSource().getSender().hasPermission("tardis.admin")) {
                                                UUID uuid = ctx.getArgument("uuid", UUID.class);
                                                Player player = plugin.getServer().getPlayer(uuid);
                                                String message = StringArgumentType.getString(ctx, "message");
                                                new SayCommand(plugin).say(player, message);
                                            }
                                            return Command.SINGLE_SUCCESS;
                                        }))))
                .then(Commands.literal("name")
                        .then(Commands.argument("uuid", ArgumentTypes.uuid())
                                .executes(ctx -> {
                                    if (ctx.getSource().getSender().hasPermission("tardis.admin")) {
                                        UUID uuid = ctx.getArgument("uuid", UUID.class);
                                        Player player = plugin.getServer().getPlayer(uuid);
                                        plugin.getMessenger().handlesSend(player, "HANDLES_NAME", player.getName());
                                    }
                                    return Command.SINGLE_SUCCESS;
                                })))
                .then(Commands.literal("time")
                        .then(Commands.argument("uuid", ArgumentTypes.uuid())
                                .executes(ctx -> {
                                    if (ctx.getSource().getSender().hasPermission("tardis.admin")) {
                                        UUID uuid = ctx.getArgument("uuid", UUID.class);
                                        Player player = plugin.getServer().getPlayer(uuid);
                                        new TimeCommand(plugin).sayTime(player);
                                    }
                                    return Command.SINGLE_SUCCESS;
                                })))
                .then(Commands.literal("tell")
                        .then(Commands.argument("player", ArgumentTypes.player())
                                .then(Commands.argument("message", StringArgumentType.greedyString())
                                        .executes(ctx -> {
                                            if (ctx.getSource().getSender().hasPermission("tardis.admin")) {
                                                PlayerSelectorArgumentResolver targetResolver = ctx.getArgument("player", PlayerSelectorArgumentResolver.class);
                                                Player player = targetResolver.resolve(ctx.getSource()).getFirst();
                                                String message = StringArgumentType.getString(ctx, "message");
                                                new TellCommand(plugin).message(player, message);
                                            }
                                            return Command.SINGLE_SUCCESS;
                                        }))))
                .then(Commands.literal("weird")
                        .executes(ctx -> {
                            if (ctx.getSource().getSender() instanceof Player player) {
                                HandlesWeirdness.say(player);
                            }
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("wiki")
                        .executes(ctx -> {
                            if (ctx.getSource().getSender() instanceof Player player) {
                                Audience.audience(player).showDialog(new SearchDialog().create());
                            }
                            return Command.SINGLE_SUCCESS;
                        })
                        .then(Commands.argument("query", StringArgumentType.greedyString())
                                .executes(ctx -> {
                                    if (ctx.getSource().getSender() instanceof Player player) {
                                        String greedy = StringArgumentType.getString(ctx, "query");
                                        new HandlesWiki(plugin).getLinks(greedy, player);
                                    }
                                    return Command.SINGLE_SUCCESS;
                                })))
                .then(Commands.literal("brake")
                        .then(Commands.argument("toggle", new OnOffArgumentType())
                                .then(Commands.argument("uuid", ArgumentTypes.uuid())
                                        .then(Commands.argument("id", IntegerArgumentType.integer(1))
                                                .executes(ctx -> {
                                                    if (ctx.getSource().getSender().hasPermission("tardis.admin")) {
                                                        UUID uuid = ctx.getArgument("uuid", UUID.class);
                                                        Player player = plugin.getServer().getPlayer(uuid);
                                                        int id = IntegerArgumentType.getInteger(ctx, "id");
                                                        String toggle = ctx.getArgument("toggle", String.class);
                                                        new BrakeCommand(plugin).park(player, id, toggle);
                                                    }
                                                    return Command.SINGLE_SUCCESS;
                                                })))));
        return command.build();
    }
}
