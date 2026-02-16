package me.eccentric_nz.TARDIS.brigadier;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.BlockPositionResolver;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;
import io.papermc.paper.math.BlockPosition;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.arch.TARDISArchCommand;
import me.eccentric_nz.TARDIS.brigadier.arguments.*;
import me.eccentric_nz.TARDIS.brigadier.suggestions.PermissionSuggestions;
import me.eccentric_nz.TARDIS.brigadier.suggestions.SeedSuggestions;
import me.eccentric_nz.TARDIS.commands.admin.*;
import me.eccentric_nz.TARDIS.commands.dev.AddRegionsCommand;
import me.eccentric_nz.TARDIS.database.tool.Converter;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.maze.MazeBuilder;
import me.eccentric_nz.TARDIS.maze.MazeGenerator;
import me.eccentric_nz.TARDIS.utility.update.UpdateTARDISPlugins;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class AdminCommandNode {

    LiteralCommandNode<CommandSourceStack> build() {
        LiteralArgumentBuilder<CommandSourceStack> command = Commands.literal("tardisadmin")
                .requires(ctx -> ctx.getSender().hasPermission("tardis.admin"))
                .then(Commands.literal("add_regions")
                        .executes(ctx -> {
                            new AddRegionsCommand(TARDIS.plugin).doCheck(ctx.getSource().getSender());
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("arch")
                        .then(Commands.literal("whois")
                                .then(Commands.argument("name", StringArgumentType.word())
                                        .executes(ctx -> {
                                            String name = StringArgumentType.getString(ctx, "name");
                                            new TARDISArchCommand(TARDIS.plugin).whois(ctx.getSource().getSender(), name);
                                            return Command.SINGLE_SUCCESS;
                                        })))
                        .then(Commands.literal("undisguise")
                                .then(Commands.argument("player", ArgumentTypes.player())
                                        .executes(ctx -> {
                                            PlayerSelectorArgumentResolver targetResolver = ctx.getArgument("player", PlayerSelectorArgumentResolver.class);
                                            Player player = targetResolver.resolve(ctx.getSource()).getFirst();
                                            new TARDISArchCommand(TARDIS.plugin).force(ctx.getSource().getSender(), player);
                                            return Command.SINGLE_SUCCESS;
                                        }))))
                .then(Commands.literal("assemble")
                        .then(Commands.argument("action", StringArgumentType.word())
                                .suggests((ctx, builder) -> {
                                    builder.suggest("clear");
                                    builder.suggest("list");
                                    return builder.buildFuture();
                                })
                                .executes(ctx -> {
                                    new DispersedCommand(TARDIS.plugin).assemble(ctx.getSource().getSender(), StringArgumentType.getString(ctx, "action"));
                                    return Command.SINGLE_SUCCESS;
                                })
                        ))
                .then(Commands.literal("clean")
                        .executes(ctx -> {
                            new CleanEntitiesCommand(TARDIS.plugin).checkAndRemove(ctx.getSource().getSender());
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("condenser")
                        .executes(ctx -> {
                            new CondenserCommand(TARDIS.plugin).set(ctx.getSource().getSender());
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("convert_database")
                        .executes(ctx -> {
                            try {
                                Bukkit.getServer().getScheduler().runTaskAsynchronously(TARDIS.plugin, new Converter(TARDIS.plugin, ctx.getSource().getSender()));
                            } catch (Exception e) {
                                TARDIS.plugin.getMessenger().message(ctx.getSource().getSender(), "Database conversion failed! " + e.getMessage());
                            }
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("create")
                        .then(Commands.argument("player", ArgumentTypes.player())
                                .then(Commands.argument("seed", new SeedArgumentType())
                                        .suggests(SeedSuggestions::get)
                                        .executes(ctx -> {
                                            PlayerSelectorArgumentResolver targetResolver = ctx.getArgument("player", PlayerSelectorArgumentResolver.class);
                                            Player target = targetResolver.resolve(ctx.getSource()).getFirst();
                                            String seed = StringArgumentType.getString(ctx, "seed");
                                            new CreateTARDISCommand(TARDIS.plugin, "ORANGE_WOOL", "LIGHT_GRAY_WOOL").buildTARDIS(ctx.getSource().getSender(), target, seed);
                                            return Command.SINGLE_SUCCESS;
                                        })
                                        .then(Commands.argument("wall", new WallFloorArgumentType())
                                                .then(Commands.argument("floor", new WallFloorArgumentType())
                                                        .executes(ctx -> {
                                                            PlayerSelectorArgumentResolver targetResolver = ctx.getArgument("player", PlayerSelectorArgumentResolver.class);
                                                            Player target = targetResolver.resolve(ctx.getSource()).getFirst();
                                                            String seed = StringArgumentType.getString(ctx, "seed");
                                                            String wall = StringArgumentType.getString(ctx, "wall");
                                                            String floor = StringArgumentType.getString(ctx, "floor");
                                                            new CreateTARDISCommand(TARDIS.plugin, wall, floor).buildTARDIS(ctx.getSource().getSender(), target, seed);
                                                            return Command.SINGLE_SUCCESS;
                                                        })
                                                )))))
                .then(Commands.literal("decharge")
                        .then(Commands.argument("name", StringArgumentType.word())
                                .executes(ctx -> {
                                    String n = StringArgumentType.getString(ctx, "name");
                                    new DechargeCommand(TARDIS.plugin).removeChargerStatus(ctx.getSource().getSender(), n);
                                    return Command.SINGLE_SUCCESS;
                                })))
                .then(Commands.literal("delete")
                        .then(Commands.argument("player", ArgumentTypes.player())
                                .executes(ctx -> {
                                    Entity executor = ctx.getSource().getExecutor();
                                    if (!(executor instanceof Player)) {
                                        ctx.getSource().getSender().sendPlainMessage("Only players can enter TARDISes!");
                                        return Command.SINGLE_SUCCESS;
                                    }
                                    PlayerSelectorArgumentResolver targetResolver = ctx.getArgument("player", PlayerSelectorArgumentResolver.class);
                                    Player player = targetResolver.resolve(ctx.getSource()).getFirst();
                                    new DeleteTARDISCommand(TARDIS.plugin).deleteTARDIS(ctx.getSource().getSender(), player, 0);
                                    return Command.SINGLE_SUCCESS;
                                })
                                .then(Commands.literal("abandoned")
                                        .executes(ctx -> {
                                            Entity executor = ctx.getSource().getExecutor();
                                            if (!(executor instanceof Player)) {
                                                ctx.getSource().getSender().sendPlainMessage("Only players can enter TARDISes!");
                                                return Command.SINGLE_SUCCESS;
                                            }
                                            PlayerSelectorArgumentResolver targetResolver = ctx.getArgument("player", PlayerSelectorArgumentResolver.class);
                                            Player player = targetResolver.resolve(ctx.getSource()).getFirst();
                                            new DeleteTARDISCommand(TARDIS.plugin).deleteTARDIS(ctx.getSource().getSender(), player, 1);
                                            return Command.SINGLE_SUCCESS;
                                        })))
                        .then(Commands.argument("id", IntegerArgumentType.integer(1))
                                .executes(ctx -> {
                                    Entity executor = ctx.getSource().getExecutor();
                                    if (!(executor instanceof Player)) {
                                        ctx.getSource().getSender().sendPlainMessage("Only players can enter TARDISes!");
                                        return Command.SINGLE_SUCCESS;
                                    }
                                    int id = IntegerArgumentType.getInteger(ctx, "id");
                                    new DeleteTARDISCommand(TARDIS.plugin).deleteTARDIS(ctx.getSource().getSender(), id, 0);
                                    return Command.SINGLE_SUCCESS;
                                })).then(Commands.literal("abandoned")).executes(ctx -> {
                            Entity executor = ctx.getSource().getExecutor();
                            if (!(executor instanceof Player)) {
                                ctx.getSource().getSender().sendPlainMessage("Only players can enter TARDISes!");
                                return Command.SINGLE_SUCCESS;
                            }
                            int id = IntegerArgumentType.getInteger(ctx, "id");
                            new DeleteTARDISCommand(TARDIS.plugin).deleteTARDIS(ctx.getSource().getSender(), id, 1);
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("disguise")
                        .then(Commands.argument("player", ArgumentTypes.player())
                                .then(Commands.argument("entity_type", new CreatureArgumentType())
                                        .executes(ctx -> {
                                            PlayerSelectorArgumentResolver targetResolver = ctx.getArgument("player", PlayerSelectorArgumentResolver.class);
                                            Player player = targetResolver.resolve(ctx.getSource()).getFirst();
                                            String type = ctx.getArgument("entity_type", String.class);
                                            new DisguiseCommand(TARDIS.plugin).disguise(ctx.getSource().getSender(), player, type);
                                            return Command.SINGLE_SUCCESS;
                                        }))))
                .then(Commands.literal("dispersed")
                        .then(Commands.argument("action", StringArgumentType.word())
                                .suggests((ctx, builder) -> {
                                    builder.suggest("clear");
                                    builder.suggest("list");
                                    return builder.buildFuture();
                                })
                                .executes(ctx -> {
                                    String action = StringArgumentType.getString(ctx, "action");
                                    new DispersedCommand(TARDIS.plugin).assemble(ctx.getSource().getSender(), action);
                                    return Command.SINGLE_SUCCESS;
                                })
                        ))
                .then(Commands.literal("enter")
                        .then(Commands.argument("player", ArgumentTypes.player())
                                .executes(ctx -> {
                                    Entity executor = ctx.getSource().getExecutor();
                                    if (!(executor instanceof Player)) {
                                        ctx.getSource().getSender().sendPlainMessage("Only players can enter TARDISes!");
                                        return Command.SINGLE_SUCCESS;
                                    }
                                    PlayerSelectorArgumentResolver targetResolver = ctx.getArgument("player", PlayerSelectorArgumentResolver.class);
                                    Player player = targetResolver.resolve(ctx.getSource()).getFirst();
                                    new EnterCommand(TARDIS.plugin).enterTARDIS(ctx.getSource().getSender(), player);
                                    return Command.SINGLE_SUCCESS;
                                }))
                        .then(Commands.argument("id", IntegerArgumentType.integer(1))
                                .executes(ctx -> {
                                    Entity executor = ctx.getSource().getExecutor();
                                    if (!(executor instanceof Player)) {
                                        ctx.getSource().getSender().sendPlainMessage("Only players can enter TARDISes!");
                                        return Command.SINGLE_SUCCESS;
                                    }
                                    int id = IntegerArgumentType.getInteger(ctx, "id");
                                    new EnterCommand(TARDIS.plugin).enterTARDIS(ctx.getSource().getSender(), id);
                                    return Command.SINGLE_SUCCESS;
                                })))
                .then(Commands.literal("find")
                        .executes(ctx -> {
                            new FindHiddenCommand().search(TARDIS.plugin, ctx.getSource().getSender(), 16);
                            return Command.SINGLE_SUCCESS;
                        })
                        .then(Commands.argument("radius", IntegerArgumentType.integer(1, 32))
                                .executes(ctx -> {
                                    int radius = IntegerArgumentType.getInteger(ctx, "radius");
                                    new FindHiddenCommand().search(TARDIS.plugin, ctx.getSource().getSender(), radius);
                                    return Command.SINGLE_SUCCESS;
                                }))
                )
                .then(Commands.literal("list")
                        .then(Commands.literal("blueprints")
                                .then(Commands.argument("blueprint", new BlueprintArgumentType())
                                        .executes(ctx -> {
                                            String b = ctx.getArgument("blueprint", String.class);
                                            new BlueprintLister(TARDIS.plugin).list(ctx.getSource().getSender(), b);
                                            return Command.SINGLE_SUCCESS;
                                        })))
                        .then(Commands.literal("tardises")
                                .executes(ctx -> {
                                    new TardisPagedLister(TARDIS.plugin, ctx.getSource().getSender()).send(1);
                                    return Command.SINGLE_SUCCESS;
                                })
                                .then(Commands.argument("page", IntegerArgumentType.integer(1))
                                        .executes(ctx -> {
                                            int page = IntegerArgumentType.getInteger(ctx, "page");
                                            new TardisPagedLister(TARDIS.plugin, ctx.getSource().getSender()).send(page);
                                            return Command.SINGLE_SUCCESS;
                                        })))
                        .then(Commands.argument("what", new AdminListArgumentType())
                                .executes(ctx -> {
                                    String what = ctx.getArgument("what", String.class);
                                    new ListCommand(TARDIS.plugin).listStuff(ctx.getSource().getSender(), what);
                                    return Command.SINGLE_SUCCESS;
                                })))
                .then(Commands.literal("make_preset")
                        .requires(ctx -> ctx.getSender() instanceof Player)
                        .then(Commands.argument("name", StringArgumentType.word())
                                .executes(ctx -> {
                                    String name = StringArgumentType.getString(ctx, "name");
                                    new MakePresetCommand(TARDIS.plugin).scanBlocks(ctx.getSource().getSender(), name);
                                    return Command.SINGLE_SUCCESS;
                                })))
                .then(Commands.literal("maze")
                        .executes(ctx -> {
                            Entity executor = ctx.getSource().getExecutor();
                            if (!(executor instanceof Player player)) {
                                ctx.getSource().getSender().sendPlainMessage("Only players can create mazes!");
                                return Command.SINGLE_SUCCESS;
                            }
                            Location l = player.getTargetBlock(TARDIS.plugin.getGeneralKeeper().getTransparent(), 16).getRelative(BlockFace.UP).getLocation();
                            MazeGenerator generator = new MazeGenerator();
                            generator.makeMaze();
                            MazeBuilder builder = new MazeBuilder(generator.getMaze(), l);
                            builder.build(false);
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("mv_import")
                        .executes(ctx -> {
                            if (!TARDIS.plugin.getServer().getPluginManager().isPluginEnabled("Multiverse-Core")) {
                                TARDIS.plugin.getMessenger().send(ctx.getSource().getSender(), TardisModule.TARDIS, "MULTIVERSE_ENABLED");
                            }
                            TARDIS.plugin.getMVHelper().importWorlds(ctx.getSource().getSender());
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("player_count")
                        .then(Commands.argument("player", ArgumentTypes.player())
                                .executes(ctx -> {
                                    PlayerSelectorArgumentResolver targetResolver = ctx.getArgument("player", PlayerSelectorArgumentResolver.class);
                                    Player player = targetResolver.resolve(ctx.getSource()).getFirst();
                                    new PlayerCountCommand(TARDIS.plugin).countPlayers(ctx.getSource().getSender(), player, -1);
                                    return Command.SINGLE_SUCCESS;
                                }).then(Commands.argument("count", IntegerArgumentType.integer(1))
                                        .executes(ctx -> {
                                            PlayerSelectorArgumentResolver targetResolver = ctx.getArgument("player", PlayerSelectorArgumentResolver.class);
                                            Player player = targetResolver.resolve(ctx.getSource()).getFirst();
                                            int count = IntegerArgumentType.getInteger(ctx, "count");
                                            new PlayerCountCommand(TARDIS.plugin).countPlayers(ctx.getSource().getSender(), player, count);
                                            return Command.SINGLE_SUCCESS;
                                        }))))
                .then(Commands.literal("prune")
                        .then(Commands.argument("days", IntegerArgumentType.integer(1))
                                .executes(ctx -> {
                                    int days = IntegerArgumentType.getInteger(ctx, "days");
                                    new PruneCommand(TARDIS.plugin).startPruning(ctx.getSource().getSender(), days);
                                    return Command.SINGLE_SUCCESS;
                                })))
                .then(Commands.literal("prune_list")
                        .then(Commands.argument("days", IntegerArgumentType.integer(1))
                                .executes(ctx -> {
                                    int days = IntegerArgumentType.getInteger(ctx, "days");
                                    new PruneCommand(TARDIS.plugin).listPrunes(ctx.getSource().getSender(), days);
                                    return Command.SINGLE_SUCCESS;
                                })))
                .then(Commands.literal("purge")
                        .then(Commands.literal("junk")
                                .executes(ctx -> {
                                    new PurgeCommand(TARDIS.plugin).clearAll(ctx.getSource().getSender(), "junk", "00000000-aaaa-bbbb-cccc-000000000000");
                                    return Command.SINGLE_SUCCESS;
                                }))
                        .then(Commands.argument("player", ArgumentTypes.player())
                                .executes(ctx -> {
                                    PlayerSelectorArgumentResolver targetResolver = ctx.getArgument("player", PlayerSelectorArgumentResolver.class);
                                    Player player = targetResolver.resolve(ctx.getSource()).getFirst();
                                    new PurgeCommand(TARDIS.plugin).clearAll(ctx.getSource().getSender(), player.getName(), player.getUniqueId().toString());
                                    return Command.SINGLE_SUCCESS;
                                })))
                .then(Commands.literal("purge_portals")
                        .executes(ctx -> {
                            new PortalCommand(TARDIS.plugin).clearAll(ctx.getSource().getSender());
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("recharger")
                        .then(Commands.argument("name", StringArgumentType.word())
                                .executes(ctx -> {
                                    String n = StringArgumentType.getString(ctx, "name");
                                    new RechargerCommand(TARDIS.plugin).setRecharger(ctx.getSource().getSender(), n);
                                    return Command.SINGLE_SUCCESS;
                                })))
                .then(Commands.literal("remove_protection")
                        .then(Commands.argument("block_id", IntegerArgumentType.integer(1))
                                .then(Commands.argument("location", StringArgumentType.string())
                                        .executes(ctx -> {
                                            int id = IntegerArgumentType.getInteger(ctx, "block_id");
                                            String location = StringArgumentType.getString(ctx, "location");
                                            new RemoveProtectionCommand(TARDIS.plugin).remove(id, location);
                                            return Command.SINGLE_SUCCESS;
                                        }))))
                .then(Commands.literal("repair")
                        .then(Commands.argument("player", ArgumentTypes.player())
                                .then(Commands.argument("count", IntegerArgumentType.integer(0))
                                        .executes(ctx -> {
                                            PlayerSelectorArgumentResolver targetResolver = ctx.getArgument("player", PlayerSelectorArgumentResolver.class);
                                            Player player = targetResolver.resolve(ctx.getSource()).getFirst();
                                            int count = IntegerArgumentType.getInteger(ctx, "count");
                                            new RepairCommand(TARDIS.plugin).setFreeCount(ctx.getSource().getSender(), player, count);
                                            return Command.SINGLE_SUCCESS;
                                        }))))
                .then(Commands.literal("revoke")
                        .then(Commands.argument("player", ArgumentTypes.player())
                                .then(Commands.argument("permission", StringArgumentType.word())
                                        .suggests(PermissionSuggestions::get)
                                        .executes(ctx -> {
                                            PlayerSelectorArgumentResolver targetResolver = ctx.getArgument("player", PlayerSelectorArgumentResolver.class);
                                            Player player = targetResolver.resolve(ctx.getSource()).getFirst();
                                            String perm = StringArgumentType.getString(ctx, "permission");
                                            new RevokeCommand(TARDIS.plugin).removePermission(ctx.getSource().getSender(), player, perm);
                                            return Command.SINGLE_SUCCESS;
                                        }))))
                .then(Commands.literal("set_size")
                        .then(Commands.argument("player", ArgumentTypes.player())
                                .then(Commands.argument("desktop", new SeedArgumentType())
                                        .executes(ctx -> {
                                            PlayerSelectorArgumentResolver targetResolver = ctx.getArgument("player", PlayerSelectorArgumentResolver.class);
                                            Player player = targetResolver.resolve(ctx.getSource()).getFirst();
                                            String desktop = ctx.getArgument("desktop", String.class);
                                            new SetSizeCommand(TARDIS.plugin).overwrite(ctx.getSource().getSender(), player, desktop);
                                            return Command.SINGLE_SUCCESS;
                                        }))))
                .then(Commands.literal("spawn_abandoned")
                        .then(Commands.argument("schematic", new SeedArgumentType())
                                .then(Commands.argument("preset", new PresetArgumentType())
                                        .then(Commands.argument("direction", new CompassArgumentType())
                                                .executes(ctx -> {
                                                    Entity executor = ctx.getSource().getExecutor();
                                                    if (!(executor instanceof Player player)) {
                                                        ctx.getSource().getSender().sendPlainMessage("Only players can create abandoned TARDISes!");
                                                        return Command.SINGLE_SUCCESS;
                                                    }
                                                    String s = ctx.getArgument("schematic", String.class);
                                                    String p = ctx.getArgument("preset", String.class);
                                                    String d = ctx.getArgument("direction", String.class);
                                                    Location location = player.getTargetBlock(TARDIS.plugin.getGeneralKeeper().getTransparent(), 16).getRelative(BlockFace.UP).getLocation();
                                                    new CreateAbandonedCommand(TARDIS.plugin).spawn(ctx.getSource().getSender(), s, p, d, location.getWorld(), location.blockX(), location.blockY(), location.blockZ());
                                                    return Command.SINGLE_SUCCESS;
                                                })
                                                .then(Commands.argument("world", ArgumentTypes.world())
                                                        .then(Commands.argument("position", ArgumentTypes.blockPosition())
                                                                .executes(ctx -> {
                                                                    String s = ctx.getArgument("schematic", String.class);
                                                                    String p = ctx.getArgument("preset", String.class);
                                                                    String d = ctx.getArgument("direction", String.class);
                                                                    World world = ctx.getArgument("world", World.class);
                                                                    BlockPositionResolver blockPositionResolver = ctx.getArgument("arg", BlockPositionResolver.class);
                                                                    BlockPosition pos = blockPositionResolver.resolve(ctx.getSource());
                                                                    new CreateAbandonedCommand(TARDIS.plugin).spawn(ctx.getSource().getSender(), s, p, d, world, pos.blockX(), pos.blockY(), pos.blockZ());
                                                                    return Command.SINGLE_SUCCESS;
                                                                })))))))
                .then(Commands.literal("undisguise").executes(ctx -> {
                            Entity executor = ctx.getSource().getExecutor();
                            if (!(executor instanceof Player player)) {
                                ctx.getSource().getSender().sendPlainMessage("Only players can undisguise!");
                                return Command.SINGLE_SUCCESS;
                            }
                            TARDIS.plugin.getTardisHelper().undisguise(player);
                            return Command.SINGLE_SUCCESS;
                        })
                        .then(Commands.argument("player", ArgumentTypes.player())
                                .executes(ctx -> {
                                    PlayerSelectorArgumentResolver targetResolver = ctx.getArgument("player", PlayerSelectorArgumentResolver.class);
                                    Player player = targetResolver.resolve(ctx.getSource()).getFirst();
                                    TARDIS.plugin.getTardisHelper().undisguise(player);
                                    return Command.SINGLE_SUCCESS;
                                })))
                .then(Commands.literal("update_plugin")
                        .executes(ctx -> {
                            if (!ctx.getSource().getSender().isOp()) {
                                TARDIS.plugin.getMessenger().message(ctx.getSource().getSender(), "You must be a server operator to run this command!");
                                return Command.SINGLE_SUCCESS;
                            }
                            new UpdateTARDISPlugins(TARDIS.plugin).fetchFromGitHub(ctx.getSource().getSender());
                            return Command.SINGLE_SUCCESS;
                        }));
        return command.build();
    }
}
