package me.eccentric_nz.TARDIS.brigadier;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.BlockPositionResolver;
import io.papermc.paper.command.brigadier.argument.resolvers.PlayerProfileListResolver;
import io.papermc.paper.math.BlockPosition;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.brigadier.arguments.UpdateableArgumentType;
import me.eccentric_nz.TARDIS.commands.preferences.IsomorphicCommand;
import me.eccentric_nz.TARDIS.commands.remote.BackCommand;
import me.eccentric_nz.TARDIS.commands.remote.ComehereCommand;
import me.eccentric_nz.TARDIS.commands.remote.HideCommand;
import me.eccentric_nz.TARDIS.commands.remote.RebuildCommand;
import me.eccentric_nz.TARDIS.commands.sudo.*;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardisID;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.Collection;

public class SudoCommandNode {

    private final TARDIS plugin;

    public SudoCommandNode(TARDIS plugin) {
        this.plugin = plugin;
    }

    LiteralCommandNode<CommandSourceStack> build() {
        LiteralArgumentBuilder<CommandSourceStack> command = Commands.literal("tardissudo")
                .requires(ctx -> ctx.getSender().hasPermission("tardis.admin"))
                .then(Commands.argument("profile", ArgumentTypes.playerProfiles())
                        .then(Commands.literal("ars"))
                        .then(Commands.literal("assemble").executes(ctx -> {
                            PlayerProfileListResolver profilesResolver = ctx.getArgument("profile", PlayerProfileListResolver.class);
                            Collection<PlayerProfile> foundProfiles = profilesResolver.resolve(ctx.getSource());
                            for (PlayerProfile profile : foundProfiles) {
                                ResultSetTardisID rs = new ResultSetTardisID(plugin);
                                if (!rs.fromUUID(profile.getId().toString())) {
                                    plugin.getMessenger().send(ctx.getSource().getSender(), TardisModule.TARDIS, "PLAYER_NO_TARDIS");
                                    return Command.SINGLE_SUCCESS;
                                }
                                OfflinePlayer offlinePlayer = plugin.getServer().getOfflinePlayer(profile.getId());
                                new SudoAssemble(plugin).restore(ctx.getSource().getSender(), profile.getId(), offlinePlayer.getName());
                            }
                            return Command.SINGLE_SUCCESS;
                        }))
                        .then(Commands.literal("back")
                                .executes(ctx -> {
                                    PlayerProfileListResolver profilesResolver = ctx.getArgument("profile", PlayerProfileListResolver.class);
                                    Collection<PlayerProfile> foundProfiles = profilesResolver.resolve(ctx.getSource());
                                    for (PlayerProfile profile : foundProfiles) {
                                        ResultSetTardisID rs = new ResultSetTardisID(plugin);
                                        if (!rs.fromUUID(profile.getId().toString())) {
                                            plugin.getMessenger().send(ctx.getSource().getSender(), TardisModule.TARDIS, "PLAYER_NO_TARDIS");
                                            return Command.SINGLE_SUCCESS;
                                        }
                                        OfflinePlayer offlinePlayer = plugin.getServer().getOfflinePlayer(profile.getId());
                                        new BackCommand(plugin).sendBack(ctx.getSource().getSender(), rs.getTardisId(), offlinePlayer);
                                    }
                                    return Command.SINGLE_SUCCESS;
                                }))
                        .then(Commands.literal("chameleon"))
                        .then(Commands.literal("clean")
                                .executes(ctx -> {
                                    PlayerProfileListResolver profilesResolver = ctx.getArgument("profile", PlayerProfileListResolver.class);
                                    Collection<PlayerProfile> foundProfiles = profilesResolver.resolve(ctx.getSource());
                                    for (PlayerProfile profile : foundProfiles) {
                                        ResultSetTardisID rs = new ResultSetTardisID(plugin);
                                        if (!rs.fromUUID(profile.getId().toString())) {
                                            plugin.getMessenger().send(ctx.getSource().getSender(), TardisModule.TARDIS, "PLAYER_NO_TARDIS");
                                            return Command.SINGLE_SUCCESS;
                                        }
                                        new SudoRepair(plugin, profile.getId(), true).repair();
                                    }
                                    return Command.SINGLE_SUCCESS;
                                }))
                        .then(Commands.literal("comehere")
                                .executes(ctx -> {
                                    if (ctx.getSource().getSender() instanceof ConsoleCommandSender) {
                                        plugin.getMessenger().send(ctx.getSource().getSender(), TardisModule.TARDIS, "CMD_NO_CONSOLE");
                                        return Command.SINGLE_SUCCESS;
                                    }
                                    PlayerProfileListResolver profilesResolver = ctx.getArgument("profile", PlayerProfileListResolver.class);
                                    Collection<PlayerProfile> foundProfiles = profilesResolver.resolve(ctx.getSource());
                                    for (PlayerProfile profile : foundProfiles) {
                                        ResultSetTardisID rs = new ResultSetTardisID(plugin);
                                        if (!rs.fromUUID(profile.getId().toString())) {
                                            plugin.getMessenger().send(ctx.getSource().getSender(), TardisModule.TARDIS, "PLAYER_NO_TARDIS");
                                            return Command.SINGLE_SUCCESS;
                                        }
                                        new ComehereCommand(plugin).doRemoteComeHere((Player) ctx.getSource().getSender(), profile.getId());
                                    }
                                    return Command.SINGLE_SUCCESS;
                                }))
                        .then(Commands.literal("deadlock")
                                .executes(ctx -> {
                                    PlayerProfileListResolver profilesResolver = ctx.getArgument("profile", PlayerProfileListResolver.class);
                                    Collection<PlayerProfile> foundProfiles = profilesResolver.resolve(ctx.getSource());
                                    for (PlayerProfile profile : foundProfiles) {
                                        ResultSetTardisID rs = new ResultSetTardisID(plugin);
                                        if (!rs.fromUUID(profile.getId().toString())) {
                                            plugin.getMessenger().send(ctx.getSource().getSender(), TardisModule.TARDIS, "PLAYER_NO_TARDIS");
                                            return Command.SINGLE_SUCCESS;
                                        }
                                        new SudoDeadlock(plugin).toggleDeadlock(profile.getId(), ctx.getSource().getSender());
                                    }
                                    return Command.SINGLE_SUCCESS;
                                }))
                        .then(Commands.literal("desiege")
                                .executes(ctx -> {
                                    PlayerProfileListResolver profilesResolver = ctx.getArgument("profile", PlayerProfileListResolver.class);
                                    Collection<PlayerProfile> foundProfiles = profilesResolver.resolve(ctx.getSource());
                                    for (PlayerProfile profile : foundProfiles) {
                                        ResultSetTardisID rs = new ResultSetTardisID(plugin);
                                        if (!rs.fromUUID(profile.getId().toString())) {
                                            plugin.getMessenger().send(ctx.getSource().getSender(), TardisModule.TARDIS, "PLAYER_NO_TARDIS");
                                            return Command.SINGLE_SUCCESS;
                                        }
                                        OfflinePlayer offlinePlayer = plugin.getServer().getOfflinePlayer(profile.getId());
                                        if (offlinePlayer.isOnline()) {
                                            new SudoDesiege(plugin).restore(ctx.getSource().getSender(), profile.getId(), rs.getTardisId());
                                        } else {
                                            plugin.getMessenger().send(ctx.getSource().getSender(), TardisModule.TARDIS, "NOT_ONLINE");
                                        }
                                    }
                                    return Command.SINGLE_SUCCESS;
                                }))
                        .then(Commands.literal("handbrake"))
                        .then(Commands.literal("hide")
                                .executes(ctx -> {
                                    PlayerProfileListResolver profilesResolver = ctx.getArgument("profile", PlayerProfileListResolver.class);
                                    Collection<PlayerProfile> foundProfiles = profilesResolver.resolve(ctx.getSource());
                                    for (PlayerProfile profile : foundProfiles) {
                                        ResultSetTardisID rs = new ResultSetTardisID(plugin);
                                        if (!rs.fromUUID(profile.getId().toString())) {
                                            plugin.getMessenger().send(ctx.getSource().getSender(), TardisModule.TARDIS, "PLAYER_NO_TARDIS");
                                            return Command.SINGLE_SUCCESS;
                                        }
                                        new HideCommand(plugin).doRemoteHide(ctx.getSource().getSender(), rs.getTardisId());
                                    }
                                    return Command.SINGLE_SUCCESS;
                                }))
                        .then(Commands.literal("isomorphic")
                                .executes(ctx -> {
                                    PlayerProfileListResolver profilesResolver = ctx.getArgument("profile", PlayerProfileListResolver.class);
                                    Collection<PlayerProfile> foundProfiles = profilesResolver.resolve(ctx.getSource());
                                    for (PlayerProfile profile : foundProfiles) {
                                        ResultSetTardisID rs = new ResultSetTardisID(plugin);
                                        if (!rs.fromUUID(profile.getId().toString())) {
                                            plugin.getMessenger().send(ctx.getSource().getSender(), TardisModule.TARDIS, "PLAYER_NO_TARDIS");
                                            return Command.SINGLE_SUCCESS;
                                        }
                                        new IsomorphicCommand(plugin).toggleIsomorphicControls(profile.getId(), ctx.getSource().getSender());
                                    }
                                    return Command.SINGLE_SUCCESS;
                                }))
                        .then(Commands.literal("rebuild")
                                .executes(ctx -> {
                                    PlayerProfileListResolver profilesResolver = ctx.getArgument("profile", PlayerProfileListResolver.class);
                                    Collection<PlayerProfile> foundProfiles = profilesResolver.resolve(ctx.getSource());
                                    for (PlayerProfile profile : foundProfiles) {
                                        ResultSetTardisID rs = new ResultSetTardisID(plugin);
                                        if (!rs.fromUUID(profile.getId().toString())) {
                                            plugin.getMessenger().send(ctx.getSource().getSender(), TardisModule.TARDIS, "PLAYER_NO_TARDIS");
                                            return Command.SINGLE_SUCCESS;
                                        }
                                        OfflinePlayer offlinePlayer = plugin.getServer().getOfflinePlayer(profile.getId());
                                        new RebuildCommand(plugin).doRemoteRebuild(ctx.getSource().getSender(), rs.getTardisId(), offlinePlayer, true);
                                    }
                                    return Command.SINGLE_SUCCESS;
                                }))
                        .then(Commands.literal("repair")
                                .executes(ctx -> {
                                    PlayerProfileListResolver profilesResolver = ctx.getArgument("profile", PlayerProfileListResolver.class);
                                    Collection<PlayerProfile> foundProfiles = profilesResolver.resolve(ctx.getSource());
                                    for (PlayerProfile profile : foundProfiles) {
                                        ResultSetTardisID rs = new ResultSetTardisID(plugin);
                                        if (!rs.fromUUID(profile.getId().toString())) {
                                            plugin.getMessenger().send(ctx.getSource().getSender(), TardisModule.TARDIS, "PLAYER_NO_TARDIS");
                                            return Command.SINGLE_SUCCESS;
                                        }
                                        new SudoRepair(plugin, profile.getId(), false).repair();
                                    }
                                    return Command.SINGLE_SUCCESS;
                                }))
                        .then(Commands.literal("travel")
                                .then(Commands.argument("world", ArgumentTypes.world())
                                        .then(Commands.argument("coords", ArgumentTypes.blockPosition())
                                                .executes(ctx -> {
                                                    PlayerProfileListResolver profilesResolver = ctx.getArgument("profile", PlayerProfileListResolver.class);
                                                    Collection<PlayerProfile> foundProfiles = profilesResolver.resolve(ctx.getSource());
                                                    for (PlayerProfile profile : foundProfiles) {
                                                        ResultSetTardisID rs = new ResultSetTardisID(plugin);
                                                        if (!rs.fromUUID(profile.getId().toString())) {
                                                            plugin.getMessenger().send(ctx.getSource().getSender(), TardisModule.TARDIS, "PLAYER_NO_TARDIS");
                                                            return Command.SINGLE_SUCCESS;
                                                        }
                                                        OfflinePlayer offlinePlayer = plugin.getServer().getOfflinePlayer(profile.getId());
                                                        World world = ctx.getArgument("world", World.class);
                                                        BlockPositionResolver resolver = ctx.getArgument("coords", BlockPositionResolver.class);
                                                        BlockPosition pos = resolver.resolve(ctx.getSource());
                                                        plugin.getServer().dispatchCommand(plugin.getConsole(), "tardisremote " + offlinePlayer.getName() + " travel " + world.getName() + " " + pos.blockX() + " " + pos.blockY() + " " + pos.blockZ());
                                                    }
                                                    return Command.SINGLE_SUCCESS;
                                                }))))
                        .then(Commands.literal("update")
                                .then(Commands.argument("what", new UpdateableArgumentType())
                                        .executes(ctx -> {
                                            if (ctx.getSource().getSender() instanceof ConsoleCommandSender) {
                                                plugin.getMessenger().send(ctx.getSource().getSender(), TardisModule.TARDIS, "CMD_NO_CONSOLE");
                                                return Command.SINGLE_SUCCESS;
                                            }
                                            PlayerProfileListResolver profilesResolver = ctx.getArgument("profile", PlayerProfileListResolver.class);
                                            Collection<PlayerProfile> foundProfiles = profilesResolver.resolve(ctx.getSource());
                                            for (PlayerProfile profile : foundProfiles) {
                                                ResultSetTardisID rs = new ResultSetTardisID(plugin);
                                                if (!rs.fromUUID(profile.getId().toString())) {
                                                    plugin.getMessenger().send(ctx.getSource().getSender(), TardisModule.TARDIS, "PLAYER_NO_TARDIS");
                                                    return Command.SINGLE_SUCCESS;
                                                }
                                                // tsudo player update updatable lock|unlock|LEFT|RIGHT
                                                String u = ctx.getArgument("what", String.class);
                                                new SudoUpdate(plugin).initiate((Player) ctx.getSource().getSender(), u, "", rs.getTardisId(), profile.getId());
                                            }
                                            return Command.SINGLE_SUCCESS;
                                        })
                                        .then(Commands.argument("extra", StringArgumentType.word()).suggests((ctx, builder) -> {
                                            if (ctx.getArgument("what", String.class).equals("HINGE")) {
                                                builder.suggest("LEFT");
                                                builder.suggest("RIGHT");
                                            } else {
                                                builder.suggest("lock");
                                                builder.suggest("unlock");
                                            }
                                            return builder.buildFuture();
                                        }).executes(ctx -> {
                                            if (ctx.getSource().getSender() instanceof ConsoleCommandSender) {
                                                plugin.getMessenger().send(ctx.getSource().getSender(), TardisModule.TARDIS, "CMD_NO_CONSOLE");
                                                return Command.SINGLE_SUCCESS;
                                            }
                                            PlayerProfileListResolver profilesResolver = ctx.getArgument("profile", PlayerProfileListResolver.class);
                                            Collection<PlayerProfile> foundProfiles = profilesResolver.resolve(ctx.getSource());
                                            for (PlayerProfile profile : foundProfiles) {
                                                ResultSetTardisID rs = new ResultSetTardisID(plugin);
                                                if (!rs.fromUUID(profile.getId().toString())) {
                                                    plugin.getMessenger().send(ctx.getSource().getSender(), TardisModule.TARDIS, "PLAYER_NO_TARDIS");
                                                    return Command.SINGLE_SUCCESS;
                                                }
                                                String u = ctx.getArgument("what", String.class);
                                                String e = StringArgumentType.getString(ctx, "extra");
                                                new SudoUpdate(plugin).initiate((Player) ctx.getSource().getSender(), u, e, rs.getTardisId(), profile.getId());
                                            }
                                            return Command.SINGLE_SUCCESS;
                                        })))));
        return command.build();
    }
}
