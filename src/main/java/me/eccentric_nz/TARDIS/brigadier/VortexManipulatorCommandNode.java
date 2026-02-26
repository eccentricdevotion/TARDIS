package me.eccentric_nz.TARDIS.brigadier;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.BlockPositionResolver;
import io.papermc.paper.command.brigadier.argument.resolvers.PlayerProfileListResolver;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;
import io.papermc.paper.math.BlockPosition;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.brigadier.arguments.VMHelpArgumentType;
import me.eccentric_nz.tardisvortexmanipulator.command.*;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.Collection;

public class VortexManipulatorCommandNode {

    private final TARDIS plugin;

    public VortexManipulatorCommandNode(TARDIS plugin) {
        this.plugin = plugin;
    }

    public LiteralCommandNode<CommandSourceStack> build() {
        LiteralArgumentBuilder<CommandSourceStack> command = Commands.literal("vm")
                .executes(ctx -> {
                    new TVMCommandHelp(plugin).display(ctx.getSource().getSender(), "");
                    return Command.SINGLE_SUCCESS;
                })
                .then(Commands.literal("go")
                        .executes(ctx -> {
                            if (ctx.getSource().getExecutor() instanceof Player player && VortexManipulatorUtility.checkPlayer(plugin, player)) {
                                new TVMCommandCoords(plugin).execute(player, null, null);
                            }
                            return Command.SINGLE_SUCCESS;
                        })
                        .then(Commands.argument("save", StringArgumentType.string())
                                .executes(ctx -> {
                                    if (ctx.getSource().getExecutor() instanceof Player player && VortexManipulatorUtility.checkPlayer(plugin, player)) {
                                        String s = StringArgumentType.getString(ctx, "save");
                                        new TVMCommandGo(plugin).execute(player, s);
                                    }
                                    return Command.SINGLE_SUCCESS;
                                }))
                        .then(Commands.argument("world", ArgumentTypes.world())
                                .executes(ctx -> {
                                    if (ctx.getSource().getExecutor() instanceof Player player && VortexManipulatorUtility.checkPlayer(plugin, player)) {
                                        World world = ctx.getArgument("world", World.class);
                                        new TVMCommandCoords(plugin).execute(player, world, null);
                                    }
                                    return Command.SINGLE_SUCCESS;
                                })
                                .then(Commands.argument("coords", ArgumentTypes.blockPosition())
                                        .executes(ctx -> {
                                            if (ctx.getSource().getExecutor() instanceof Player player && VortexManipulatorUtility.checkPlayer(plugin, player)) {
                                                World world = ctx.getArgument("world", World.class);
                                                BlockPositionResolver resolver = ctx.getArgument("coords", BlockPositionResolver.class);
                                                BlockPosition pos = resolver.resolve(ctx.getSource());
                                                new TVMCommandCoords(plugin).execute(player, world, pos);
                                            }
                                            return Command.SINGLE_SUCCESS;
                                        }))))
                .then(Commands.literal("gui")
                        .executes(ctx -> {
                            if (ctx.getSource().getExecutor() instanceof Player player && VortexManipulatorUtility.checkPlayer(plugin, player)) {
                                new TVMCommandGUI(plugin).open(player);
                            }
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("help")
                        .executes(ctx -> {
                            new TVMCommandHelp(plugin).display(ctx.getSource().getSender(), "");
                            return Command.SINGLE_SUCCESS;
                        })
                        .then(Commands.argument("with", new VMHelpArgumentType())
                                .executes(ctx -> {
                                    String w = ctx.getArgument("with", String.class);
                                    new TVMCommandHelp(plugin).display(ctx.getSource().getSender(), w);
                                    return Command.SINGLE_SUCCESS;
                                })))
                .then(Commands.literal("message")
                        .then(Commands.literal("msg")
                                .then(Commands.argument("player", ArgumentTypes.playerProfiles())
                                        .then(Commands.argument("text", StringArgumentType.greedyString())
                                                .executes(ctx -> {
                                                    if (ctx.getSource().getExecutor() instanceof Player player && VortexManipulatorUtility.checkPlayer(plugin, player)) {
                                                        PlayerProfileListResolver profilesResolver = ctx.getArgument("profile", PlayerProfileListResolver.class);
                                                        Collection<PlayerProfile> foundProfiles = profilesResolver.resolve(ctx.getSource());
                                                        for (PlayerProfile profile : foundProfiles) {
                                                            String msg = StringArgumentType.getString(ctx, "text");
                                                            VortexManipulatorUtility.message(plugin, player, plugin.getServer().getOfflinePlayer(profile.getId()), msg);
                                                        }
                                                    }
                                                    return Command.SINGLE_SUCCESS;
                                                }))))
                        .then(Commands.literal("list")
                                .then(Commands.argument("box", StringArgumentType.greedyString())
                                        .suggests((ctx, builder) -> {
                                            builder.suggest("in");
                                            builder.suggest("out");
                                            return builder.buildFuture();
                                        })
                                        .executes(ctx -> {
                                            if (ctx.getSource().getExecutor() instanceof Player player && VortexManipulatorUtility.checkPlayer(plugin, player)) {
                                                String b = StringArgumentType.getString(ctx, "box");
                                                VortexManipulatorUtility.basicList(plugin, player, b);
                                            }
                                            return Command.SINGLE_SUCCESS;
                                        })
                                        .then(Commands.argument("page", IntegerArgumentType.integer(1))
                                                .executes(ctx -> {
                                                    if (ctx.getSource().getExecutor() instanceof Player player && VortexManipulatorUtility.checkPlayer(plugin, player)) {
                                                        String b = StringArgumentType.getString(ctx, "box");
                                                        int p = IntegerArgumentType.getInteger(ctx, "page");
                                                        VortexManipulatorUtility.pagedList(plugin, player, b, p);
                                                    }
                                                    return Command.SINGLE_SUCCESS;
                                                }))))
                        .then(Commands.literal("read")
                                .then(Commands.argument("id", IntegerArgumentType.integer(1))
                                        .executes(ctx -> {
                                            if (ctx.getSource().getExecutor() instanceof Player player && VortexManipulatorUtility.checkPlayer(plugin, player)) {
                                                int r = IntegerArgumentType.getInteger(ctx, "id");
                                                VortexManipulatorUtility.read(plugin, player, r);
                                            }
                                            return Command.SINGLE_SUCCESS;
                                        })))
                        .then(Commands.literal("delete")
                                .then(Commands.argument("id", IntegerArgumentType.integer(1))
                                        .executes(ctx -> {
                                            if (ctx.getSource().getExecutor() instanceof Player player && VortexManipulatorUtility.checkPlayer(plugin, player)) {
                                                int d = IntegerArgumentType.getInteger(ctx, "id");
                                                VortexManipulatorUtility.delete(plugin, player, d);
                                            }
                                            return Command.SINGLE_SUCCESS;
                                        })))
                        .then(Commands.literal("clear")
                                .then(Commands.argument("box", StringArgumentType.greedyString())
                                        .suggests((ctx, builder) -> {
                                            builder.suggest("in");
                                            builder.suggest("out");
                                            return builder.buildFuture();
                                        })
                                        .executes(ctx -> {
                                            if (ctx.getSource().getExecutor() instanceof Player player && VortexManipulatorUtility.checkPlayer(plugin, player)) {
                                                String b = StringArgumentType.getString(ctx, "box");
                                                VortexManipulatorUtility.basicList(plugin, player, b);
                                            }
                                            return Command.SINGLE_SUCCESS;
                                        }))))
                .then(Commands.literal("save")
                        .executes(ctx -> {
                            if (ctx.getSource().getExecutor() instanceof Player player && VortexManipulatorUtility.checkPlayer(plugin, player)) {
                                new TVMCommandSave(plugin).send(player, 0);
                            }
                            return Command.SINGLE_SUCCESS;
                        })
                        .then(Commands.argument("page", IntegerArgumentType.integer(1))
                                .executes(ctx -> {
                                    int p = IntegerArgumentType.getInteger(ctx, "page");
                                    if (ctx.getSource().getExecutor() instanceof Player player && VortexManipulatorUtility.checkPlayer(plugin, player)) {
                                        new TVMCommandSave(plugin).send(player, p);
                                    }
                                    return Command.SINGLE_SUCCESS;
                                }))
                        .then(Commands.argument("name", StringArgumentType.word())
                                .executes(ctx -> {
                                    String s = StringArgumentType.getString(ctx, "name");
                                    if (ctx.getSource().getExecutor() instanceof Player player && VortexManipulatorUtility.checkPlayer(plugin, player)) {
                                        new TVMCommandSave(plugin).save(player, s);
                                    }
                                    return Command.SINGLE_SUCCESS;
                                })))
                .then(Commands.literal("remove")
                        .then(Commands.argument("save", StringArgumentType.word())
                                .executes(ctx -> {
                                    if (ctx.getSource().getExecutor() instanceof Player player && VortexManipulatorUtility.checkPlayer(plugin, player)) {
                                        String s = StringArgumentType.getString(ctx, "save");
                                        new TVMCommandRemove(plugin).process(player, s);
                                    }
                                    return Command.SINGLE_SUCCESS;
                                })))
                .then(Commands.literal("lifesigns")
                        .executes(ctx -> {
                            if (ctx.getSource().getExecutor() instanceof Player player && VortexManipulatorUtility.checkPlayer(plugin, player)) {
                                new TVMCommandLifesigns(plugin).scan(player, null);
                            }
                            return Command.SINGLE_SUCCESS;
                        })
                        .then(Commands.argument("player", ArgumentTypes.player())
                                .executes(ctx -> {
                                    if (ctx.getSource().getExecutor() instanceof Player player && VortexManipulatorUtility.checkPlayer(plugin, player)) {
                                        PlayerSelectorArgumentResolver targetResolver = ctx.getArgument("player", PlayerSelectorArgumentResolver.class);
                                        Player target = targetResolver.resolve(ctx.getSource()).getFirst();
                                        new TVMCommandLifesigns(plugin).scan(player, target);
                                    }
                                    return Command.SINGLE_SUCCESS;
                                })))
                .then(Commands.literal("beacon")
                        .executes(ctx -> {
                            if (ctx.getSource().getExecutor() instanceof Player player && VortexManipulatorUtility.checkPlayer(plugin, player)) {
                                new TVMCommandBeacon(plugin).process(player);
                            }
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("activate")
                        .then(Commands.argument("player", ArgumentTypes.player())
                                .executes(ctx -> {
                                    PlayerSelectorArgumentResolver targetResolver = ctx.getArgument("player", PlayerSelectorArgumentResolver.class);
                                    Player p = targetResolver.resolve(ctx.getSource()).getFirst();
                                    new TVMCommandActivate(plugin).process(ctx.getSource().getSender(), p);
                                    return Command.SINGLE_SUCCESS;
                                })))
                .then(Commands.literal("give")
                        .then(Commands.argument("player", ArgumentTypes.player())
                                .then(Commands.literal("full")
                                        .executes(ctx -> {
                                            PlayerSelectorArgumentResolver targetResolver = ctx.getArgument("player", PlayerSelectorArgumentResolver.class);
                                            Player p = targetResolver.resolve(ctx.getSource()).getFirst();
                                            new TVMCommandGive(plugin).process(ctx.getSource().getSender(), p, plugin.getVortexConfig().getInt("tachyon_use.max"));
                                            return Command.SINGLE_SUCCESS;
                                        }))
                                .then(Commands.literal("empty")
                                        .executes(ctx -> {
                                            PlayerSelectorArgumentResolver targetResolver = ctx.getArgument("player", PlayerSelectorArgumentResolver.class);
                                            Player p = targetResolver.resolve(ctx.getSource()).getFirst();
                                            new TVMCommandGive(plugin).process(ctx.getSource().getSender(), p, 0);
                                            return Command.SINGLE_SUCCESS;
                                        }))
                                .then(Commands.argument("amount", IntegerArgumentType.integer(0))
                                        .executes(ctx -> {
                                            PlayerSelectorArgumentResolver targetResolver = ctx.getArgument("player", PlayerSelectorArgumentResolver.class);
                                            Player p = targetResolver.resolve(ctx.getSource()).getFirst();
                                            int a = IntegerArgumentType.getInteger(ctx, "amount");
                                            new TVMCommandGive(plugin).process(ctx.getSource().getSender(), p, a);
                                            return Command.SINGLE_SUCCESS;
                                        }))));
        return command.build();
    }
}
