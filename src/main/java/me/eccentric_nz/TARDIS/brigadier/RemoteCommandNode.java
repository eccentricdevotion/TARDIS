package me.eccentric_nz.TARDIS.brigadier;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.mojang.datafixers.util.Pair;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.BlockPositionResolver;
import io.papermc.paper.command.brigadier.argument.resolvers.PlayerProfileListResolver;
import io.papermc.paper.math.BlockPosition;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.brigadier.arguments.AreasArgumentType;
import me.eccentric_nz.TARDIS.commands.TARDISCommandHelper;
import me.eccentric_nz.TARDIS.commands.remote.BackCommand;
import me.eccentric_nz.TARDIS.commands.remote.ComehereCommand;
import me.eccentric_nz.TARDIS.commands.remote.RemoteUtility;
import me.eccentric_nz.TARDIS.commands.tardis.HideCommand;
import me.eccentric_nz.TARDIS.commands.tardis.RebuildCommand;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardis;
import me.eccentric_nz.TARDIS.enumeration.ChameleonPreset;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.HashMap;

public class RemoteCommandNode {

    private final TARDIS plugin;

    public RemoteCommandNode(TARDIS plugin) {
        this.plugin = plugin;
    }

    LiteralCommandNode<CommandSourceStack> build() {
        LiteralArgumentBuilder<CommandSourceStack> command = Commands.literal("tardisremote")
                .requires(ctx -> TARDISPermission.hasPermission(ctx.getSender(), "tardis.remote"))
                .executes(ctx -> {
                    new TARDISCommandHelper(plugin).getCommand("tardisremote", ctx.getSource().getSender());
                    return Command.SINGLE_SUCCESS;
                })
                .then(Commands.argument("player", ArgumentTypes.playerProfiles())
                        .then(Commands.literal("travel")
                                .then(Commands.literal("home")
                                        .executes(ctx -> {
                                            Pair<Tardis, OfflinePlayer> required = getRequired(ctx);
                                            if (required == null) {
                                                return Command.SINGLE_SUCCESS;
                                            }
                                            if (RemoteUtility.travelCheck(plugin, ctx.getSource().getSender(), required.getFirst().isHandbrakeOn(), required.getFirst().getArtronLevel())) {
                                                return Command.SINGLE_SUCCESS;
                                            }
                                            if (ctx.getSource().getSender() instanceof BlockCommandSender && required.getSecond().getPlayer() == null) {
                                                return Command.SINGLE_SUCCESS;
                                            }
                                            RemoteUtility.home(plugin, required.getFirst().getTardisId(), ctx.getSource().getSender(), required.getSecond().getUniqueId());
                                            return Command.SINGLE_SUCCESS;
                                        }))

                                .then(Commands.literal("area")
                                        .then(Commands.argument("name", new AreasArgumentType())
                                                .executes(ctx -> {
                                                    Pair<Tardis, OfflinePlayer> required = getRequired(ctx);
                                                    if (required == null) {
                                                        return Command.SINGLE_SUCCESS;
                                                    }
                                                    if (RemoteUtility.travelCheck(plugin, ctx.getSource().getSender(), required.getFirst().isHandbrakeOn(), required.getFirst().getArtronLevel())) {
                                                        return Command.SINGLE_SUCCESS;
                                                    }
                                                    if (ctx.getSource().getSender() instanceof BlockCommandSender && required.getSecond().getPlayer() == null) {
                                                        return Command.SINGLE_SUCCESS;
                                                    }
                                                    String a = ctx.getArgument("name", String.class);
                                                    RemoteUtility.area(plugin, ctx.getSource().getSender(), a, required.getSecond(), required.getFirst().getTardisId(), required.getFirst().getPreset().equals(ChameleonPreset.INVISIBLE));
                                                    return Command.SINGLE_SUCCESS;
                                                })))
                                .then(Commands.argument("world", ArgumentTypes.world())
                                        .then(Commands.argument("coords", ArgumentTypes.blockPosition())
                                                .executes(ctx -> {
                                                    Pair<Tardis, OfflinePlayer> required = getRequired(ctx);
                                                    if (required == null) {
                                                        return Command.SINGLE_SUCCESS;
                                                    }
                                                    if (RemoteUtility.travelCheck(plugin, ctx.getSource().getSender(), required.getFirst().isHandbrakeOn(), required.getFirst().getArtronLevel())) {
                                                        return Command.SINGLE_SUCCESS;
                                                    }
                                                    if (ctx.getSource().getSender() instanceof BlockCommandSender && required.getSecond().getPlayer() == null) {
                                                        return Command.SINGLE_SUCCESS;
                                                    }
                                                    World world = ctx.getArgument("world", World.class);
                                                    BlockPositionResolver resolver = ctx.getArgument("coords", BlockPositionResolver.class);
                                                    BlockPosition pos = resolver.resolve(ctx.getSource());
                                                    RemoteUtility.coordinates(plugin, ctx.getSource().getSender(), world, pos, required.getSecond(), required.getFirst().getTardisId());
                                                    return Command.SINGLE_SUCCESS;
                                                }))))
                        .then(Commands.literal("come_here")
                                .executes(ctx -> {
                                    Pair<Tardis, OfflinePlayer> required = getRequired(ctx);
                                    if (required == null) {
                                        return Command.SINGLE_SUCCESS;
                                    }
                                    if (ctx.getSource().getSender() instanceof Player player && ctx.getSource().getSender().hasPermission("tardis.admin")) {
                                        new ComehereCommand(plugin).doRemoteComeHere(player, required.getSecond().getUniqueId());
                                    } else {
                                        plugin.getMessenger().send(ctx.getSource().getSender(), TardisModule.TARDIS, "NO_PERMS");
                                    }
                                    return Command.SINGLE_SUCCESS;
                                }))
                        .then(Commands.literal("hide")
                                .executes(ctx -> {
                                    Pair<Tardis, OfflinePlayer> required = getRequired(ctx);
                                    if (required == null) {
                                        return Command.SINGLE_SUCCESS;
                                    }
                                    if ((ctx.getSource().getSender() instanceof Player && !ctx.getSource().getSender().hasPermission("tardis.admin")) || ctx.getSource().getSender() instanceof BlockCommandSender) {
                                        new HideCommand(plugin).hide(required.getSecond());
                                    } else {
                                        new me.eccentric_nz.TARDIS.commands.remote.HideCommand(plugin).doRemoteHide(ctx.getSource().getSender(), required.getFirst().getTardisId());
                                    }
                                    return Command.SINGLE_SUCCESS;
                                }))
                        .then(Commands.literal("rebuild")
                                .executes(ctx -> {
                                    Pair<Tardis, OfflinePlayer> required = getRequired(ctx);
                                    if (required == null) {
                                        return Command.SINGLE_SUCCESS;
                                    }
                                    if ((ctx.getSource().getSender() instanceof Player && !ctx.getSource().getSender().hasPermission("tardis.admin")) || ctx.getSource().getSender() instanceof BlockCommandSender) {
                                        new RebuildCommand(plugin).rebuildPreset(required.getSecond());
                                    } else {
                                        new me.eccentric_nz.TARDIS.commands.remote.RebuildCommand(plugin).doRemoteRebuild(ctx.getSource().getSender(), required.getFirst().getTardisId(), required.getSecond(), required.getFirst().isHidden());
                                    }
                                    return Command.SINGLE_SUCCESS;
                                }))
                        .then(Commands.literal("back")
                                .executes(ctx -> {
                                    Pair<Tardis, OfflinePlayer> required = getRequired(ctx);
                                    if (required == null) {
                                        return Command.SINGLE_SUCCESS;
                                    }
                                    if ((ctx.getSource().getSender() instanceof Player && ctx.getSource().getSender().hasPermission("tardis.admin")) || ctx.getSource().getSender() instanceof ConsoleCommandSender) {
                                        if (!required.getFirst().isHandbrakeOn()) {
                                            plugin.getMessenger().send(ctx.getSource().getSender(), TardisModule.TARDIS, "NOT_WHILE_TRAVELLING");
                                            return Command.SINGLE_SUCCESS;
                                        }
                                        new BackCommand(plugin).sendBack(ctx.getSource().getSender(), required.getFirst().getTardisId(), required.getSecond());
                                    } else {
                                        plugin.getMessenger().send(ctx.getSource().getSender(), TardisModule.TARDIS, "NO_PERMS");
                                    }
                                    return Command.SINGLE_SUCCESS;
                                })));
        return command.build();
    }

    private Pair<Tardis, OfflinePlayer> getRequired(CommandContext<CommandSourceStack> ctx) {
        try {
            PlayerProfileListResolver profilesResolver = ctx.getArgument("profile", PlayerProfileListResolver.class);
            Collection<PlayerProfile> foundProfiles = profilesResolver.resolve(ctx.getSource());
            for (PlayerProfile profile : foundProfiles) {
                HashMap<String, Object> where = new HashMap<>();
                where.put("uuid", profile.getId().toString());
                ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
                if (!rs.resultSet()) {
                    plugin.getMessenger().send(ctx.getSource().getSender(), TardisModule.TARDIS, "PLAYER_NO_TARDIS");
                    return null;
                }
                Tardis t = rs.getTardis();
                if (RemoteUtility.check(plugin, ctx.getSource().getSender(), t.getTardisId())) {
                    return null;
                }
                OfflinePlayer o = plugin.getServer().getOfflinePlayer(profile.getId());
                return new Pair<>(t, o);
            }
        } catch (CommandSyntaxException ignored) {
        }
        return null;
    }
}
