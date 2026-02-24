package me.eccentric_nz.TARDIS.brigadier;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.commands.TARDISCallRequestCommand;
import me.eccentric_nz.TARDIS.commands.TARDISCommandHelper;
import me.eccentric_nz.TARDIS.commands.tardis.HelpCommand;
import me.eccentric_nz.TARDIS.commands.tardis.StopSoundCommand;
import me.eccentric_nz.TARDIS.commands.tardis.TardisUtility;
import me.eccentric_nz.TARDIS.commands.tardis.VersionCommand;
import me.eccentric_nz.TARDIS.commands.utils.RescueAcceptor;
import me.eccentric_nz.TARDIS.enumeration.TardisCommand;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.travel.ComehereAction;
import me.eccentric_nz.TARDIS.travel.ComehereRequest;
import org.bukkit.entity.Player;

import java.util.UUID;

public class TardisCommandNode {

    private final TARDIS plugin;

    public TardisCommandNode(TARDIS plugin) {
        this.plugin = plugin;
    }

    LiteralCommandNode<CommandSourceStack> build() {
        LiteralArgumentBuilder<CommandSourceStack> command = Commands.literal("tardis")
                .executes(ctx -> {
                    new TARDISCommandHelper(plugin).getCommand("", ctx.getSource().getSender());
                    return Command.SINGLE_SUCCESS;
                })
                .then(Commands.literal("call")
                        .executes(ctx -> {
                            Player player = (Player) ctx.getSource().getExecutor();
                            UUID chatter = player.getUniqueId();
                            if (plugin.getTrackerKeeper().getComehereRequests().containsKey(chatter)) {
                                ComehereRequest request = plugin.getTrackerKeeper().getComehereRequests().get(chatter);
                                new ComehereAction(plugin).doTravel(request);
                                plugin.getTrackerKeeper().getComehereRequests().remove(chatter);
                            } else {
                                plugin.getMessenger().send(player, TardisModule.TARDIS, "REQUEST_TIMEOUT");
                            }
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("request")
                        .executes(ctx -> {
                            Player player = (Player) ctx.getSource().getExecutor();
                            new RescueAcceptor(plugin).doRequest(player, true);
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("abandon")
                        .executes(ctx -> {
                            Player player = TardisUtility.check(plugin, ctx.getSource().getSender(), TardisCommand.abandon);
                            if (player != null) {

                            }
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("abort")
                        .executes(ctx -> {
                            Player player = TardisUtility.check(plugin, ctx.getSource().getSender(), TardisCommand.abandon);
                            if (player != null) {

                            }
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("add")
                        .executes(ctx -> {
                            Player player = TardisUtility.check(plugin, ctx.getSource().getSender(), TardisCommand.abandon);
                            if (player != null) {

                            }
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("arch_time")
                        .executes(ctx -> {
                            Player player = TardisUtility.check(plugin, ctx.getSource().getSender(), TardisCommand.abandon);
                            if (player != null) {

                            }
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("archive")
                        .executes(ctx -> {
                            Player player = TardisUtility.check(plugin, ctx.getSource().getSender(), TardisCommand.abandon);
                            if (player != null) {

                            }
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("arsremove")
                        .executes(ctx -> {
                            Player player = TardisUtility.check(plugin, ctx.getSource().getSender(), TardisCommand.abandon);
                            if (player != null) {

                            }
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("bell")
                        .executes(ctx -> {
                            Player player = TardisUtility.check(plugin, ctx.getSource().getSender(), TardisCommand.abandon);
                            if (player != null) {

                            }
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("check_loc")
                        .executes(ctx -> {
                            Player player = TardisUtility.check(plugin, ctx.getSource().getSender(), TardisCommand.abandon);
                            if (player != null) {

                            }
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("colorize")
                        .executes(ctx -> {
                            Player player = TardisUtility.check(plugin, ctx.getSource().getSender(), TardisCommand.abandon);
                            if (player != null) {

                            }
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("colourise")
                        .executes(ctx -> {
                            Player player = TardisUtility.check(plugin, ctx.getSource().getSender(), TardisCommand.abandon);
                            if (player != null) {

                            }
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("comehere")
                        .executes(ctx -> {
                            Player player = TardisUtility.check(plugin, ctx.getSource().getSender(), TardisCommand.abandon);
                            if (player != null) {

                            }
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("construct")
                        .executes(ctx -> {
                            Player player = TardisUtility.check(plugin, ctx.getSource().getSender(), TardisCommand.abandon);
                            if (player != null) {

                            }
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("cube")
                        .executes(ctx -> {
                            Player player = TardisUtility.check(plugin, ctx.getSource().getSender(), TardisCommand.abandon);
                            if (player != null) {

                            }
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("decommission")
                        .executes(ctx -> {
                            Player player = TardisUtility.check(plugin, ctx.getSource().getSender(), TardisCommand.abandon);
                            if (player != null) {

                            }
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("desktop")
                        .executes(ctx -> {
                            Player player = TardisUtility.check(plugin, ctx.getSource().getSender(), TardisCommand.abandon);
                            if (player != null) {

                            }
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("direction")
                        .executes(ctx -> {
                            Player player = TardisUtility.check(plugin, ctx.getSource().getSender(), TardisCommand.abandon);
                            if (player != null) {

                            }
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("door")
                        .executes(ctx -> {
                            Player player = TardisUtility.check(plugin, ctx.getSource().getSender(), TardisCommand.abandon);
                            if (player != null) {

                            }
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("egg")
                        .executes(ctx -> {
                            Player player = TardisUtility.check(plugin, ctx.getSource().getSender(), TardisCommand.abandon);
                            if (player != null) {

                            }
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("eject")
                        .executes(ctx -> {
                            Player player = TardisUtility.check(plugin, ctx.getSource().getSender(), TardisCommand.abandon);
                            if (player != null) {

                            }
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("ep1")
                        .executes(ctx -> {
                            Player player = TardisUtility.check(plugin, ctx.getSource().getSender(), TardisCommand.abandon);
                            if (player != null) {

                            }
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("erase")
                        .executes(ctx -> {
                            Player player = TardisUtility.check(plugin, ctx.getSource().getSender(), TardisCommand.abandon);
                            if (player != null) {

                            }
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("excite")
                        .executes(ctx -> {
                            Player player = TardisUtility.check(plugin, ctx.getSource().getSender(), TardisCommand.abandon);
                            if (player != null) {

                            }
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("exterminate")
                        .executes(ctx -> {
                            Player player = TardisUtility.check(plugin, ctx.getSource().getSender(), TardisCommand.abandon);
                            if (player != null) {

                            }
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("find")
                        .executes(ctx -> {
                            Player player = TardisUtility.check(plugin, ctx.getSource().getSender(), TardisCommand.abandon);
                            if (player != null) {

                            }
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("handbrake")
                        .executes(ctx -> {
                            Player player = TardisUtility.check(plugin, ctx.getSource().getSender(), TardisCommand.abandon);
                            if (player != null) {

                            }
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("help")
                        .executes(ctx -> {
                            new HelpCommand(plugin).showHelp(ctx.getSource().getSender(), "", "");
                            return Command.SINGLE_SUCCESS;
                        })
                        .then(Commands.argument("command", StringArgumentType.word())
                                .executes(ctx -> {
                                    String c = StringArgumentType.getString(ctx, "command");
                                    new HelpCommand(plugin).showHelp(ctx.getSource().getSender(), c, "");
                                    return Command.SINGLE_SUCCESS;
                                })
                                .then(Commands.argument("argument", StringArgumentType.word())
                                        .executes(ctx -> {
                                            String c = StringArgumentType.getString(ctx, "command");
                                            String s = StringArgumentType.getString(ctx, "argument");
                                            new HelpCommand(plugin).showHelp(ctx.getSource().getSender(), c, s);
                                            return Command.SINGLE_SUCCESS;
                                        }))))
                .then(Commands.literal("hide")
                        .executes(ctx -> {
                            Player player = TardisUtility.check(plugin, ctx.getSource().getSender(), TardisCommand.abandon);
                            if (player != null) {

                            }
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("inside")
                        .executes(ctx -> {
                            Player player = TardisUtility.check(plugin, ctx.getSource().getSender(), TardisCommand.abandon);
                            if (player != null) {

                            }
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("item")
                        .executes(ctx -> {
                            Player player = TardisUtility.check(plugin, ctx.getSource().getSender(), TardisCommand.abandon);
                            if (player != null) {

                            }
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("jettison")
                        .executes(ctx -> {
                            Player player = TardisUtility.check(plugin, ctx.getSource().getSender(), TardisCommand.abandon);
                            if (player != null) {

                            }
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("lamps")
                        .executes(ctx -> {
                            Player player = TardisUtility.check(plugin, ctx.getSource().getSender(), TardisCommand.abandon);
                            if (player != null) {

                            }
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("list")
                        .executes(ctx -> {
                            Player player = TardisUtility.check(plugin, ctx.getSource().getSender(), TardisCommand.abandon);
                            if (player != null) {

                            }
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("make_her_blue")
                        .executes(ctx -> {
                            Player player = TardisUtility.check(plugin, ctx.getSource().getSender(), TardisCommand.abandon);
                            if (player != null) {

                            }
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("monsters")
                        .executes(ctx -> {
                            Player player = TardisUtility.check(plugin, ctx.getSource().getSender(), TardisCommand.abandon);
                            if (player != null) {

                            }
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("namekey")
                        .executes(ctx -> {
                            Player player = TardisUtility.check(plugin, ctx.getSource().getSender(), TardisCommand.abandon);
                            if (player != null) {

                            }
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("occupy")
                        .executes(ctx -> {
                            Player player = TardisUtility.check(plugin, ctx.getSource().getSender(), TardisCommand.abandon);
                            if (player != null) {

                            }
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("rebuild")
                        .executes(ctx -> {
                            Player player = TardisUtility.check(plugin, ctx.getSource().getSender(), TardisCommand.abandon);
                            if (player != null) {

                            }
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("remove")
                        .executes(ctx -> {
                            Player player = TardisUtility.check(plugin, ctx.getSource().getSender(), TardisCommand.abandon);
                            if (player != null) {

                            }
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("removesave")
                        .executes(ctx -> {
                            Player player = TardisUtility.check(plugin, ctx.getSource().getSender(), TardisCommand.abandon);
                            if (player != null) {

                            }
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("renamesave")
                        .executes(ctx -> {
                            Player player = TardisUtility.check(plugin, ctx.getSource().getSender(), TardisCommand.abandon);
                            if (player != null) {

                            }
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("reordersave")
                        .executes(ctx -> {
                            Player player = TardisUtility.check(plugin, ctx.getSource().getSender(), TardisCommand.abandon);
                            if (player != null) {

                            }
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("rescue")
                        .executes(ctx -> {
                            Player player = TardisUtility.check(plugin, ctx.getSource().getSender(), TardisCommand.abandon);
                            if (player != null) {

                            }
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("room")
                        .executes(ctx -> {
                            Player player = TardisUtility.check(plugin, ctx.getSource().getSender(), TardisCommand.abandon);
                            if (player != null) {

                            }
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("save")
                        .executes(ctx -> {
                            Player player = TardisUtility.check(plugin, ctx.getSource().getSender(), TardisCommand.abandon);
                            if (player != null) {

                            }
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("save_player")
                        .executes(ctx -> {
                            Player player = TardisUtility.check(plugin, ctx.getSource().getSender(), TardisCommand.abandon);
                            if (player != null) {

                            }
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("saveicon")
                        .executes(ctx -> {
                            Player player = TardisUtility.check(plugin, ctx.getSource().getSender(), TardisCommand.abandon);
                            if (player != null) {

                            }
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("secondary")
                        .executes(ctx -> {
                            Player player = TardisUtility.check(plugin, ctx.getSource().getSender(), TardisCommand.abandon);
                            if (player != null) {

                            }
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("section")
                        .executes(ctx -> {
                            Player player = TardisUtility.check(plugin, ctx.getSource().getSender(), TardisCommand.abandon);
                            if (player != null) {

                            }
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("setdest")
                        .executes(ctx -> {
                            Player player = TardisUtility.check(plugin, ctx.getSource().getSender(), TardisCommand.abandon);
                            if (player != null) {

                            }
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("sethome")
                        .executes(ctx -> {
                            Player player = TardisUtility.check(plugin, ctx.getSource().getSender(), TardisCommand.abandon);
                            if (player != null) {

                            }
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("stop_sound")
                        .executes(ctx -> {
                            if (ctx.getSource().getSender() instanceof Player player) {
                                new StopSoundCommand(plugin).mute(player);
                            }
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("tagtheood")
                        .executes(ctx -> {
                            Player player = TardisUtility.check(plugin, ctx.getSource().getSender(), TardisCommand.abandon);
                            if (player != null) {

                            }
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("theme")
                        .executes(ctx -> {
                            Player player = TardisUtility.check(plugin, ctx.getSource().getSender(), TardisCommand.abandon);
                            if (player != null) {

                            }
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("transmat")
                        .executes(ctx -> {
                            Player player = TardisUtility.check(plugin, ctx.getSource().getSender(), TardisCommand.abandon);
                            if (player != null) {

                            }
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("update")
                        .executes(ctx -> {
                            Player player = TardisUtility.check(plugin, ctx.getSource().getSender(), TardisCommand.abandon);
                            if (player != null) {

                            }
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("upgrade")
                        .executes(ctx -> {
                            Player player = TardisUtility.check(plugin, ctx.getSource().getSender(), TardisCommand.abandon);
                            if (player != null) {

                            }
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("version")
                        .executes(ctx -> {
                            new VersionCommand(plugin).displayVersion(ctx.getSource().getSender());
                            return Command.SINGLE_SUCCESS;
                        })
                );
        return command.build();
    }
}
