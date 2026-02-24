package me.eccentric_nz.TARDIS.brigadier;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.mojang.datafixers.util.Pair;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.BlockPositionResolver;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;
import io.papermc.paper.math.BlockPosition;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.advanced.DiskWriterCommand;
import me.eccentric_nz.TARDIS.arch.TARDISArchCommand;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.brigadier.arguments.*;
import me.eccentric_nz.TARDIS.brigadier.suggestions.BlockSuggestions;
import me.eccentric_nz.TARDIS.chatGUI.UpdateChatGUI;
import me.eccentric_nz.TARDIS.commands.TARDISCommandHelper;
import me.eccentric_nz.TARDIS.commands.tardis.*;
import me.eccentric_nz.TARDIS.commands.tardis.update.UpdateBlocksCommand;
import me.eccentric_nz.TARDIS.commands.tardis.update.UpdateCommand;
import me.eccentric_nz.TARDIS.commands.tardis.update.UpdateLungeCommand;
import me.eccentric_nz.TARDIS.commands.utils.RescueAcceptor;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.enumeration.TardisCommand;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.enumeration.Updateable;
import me.eccentric_nz.TARDIS.travel.ComehereAction;
import me.eccentric_nz.TARDIS.travel.ComehereRequest;
import me.eccentric_nz.TARDIS.update.TARDISUpdateBlocks;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Door;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

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
                            Pair<Player, Integer> c = TardisUtility.check(plugin, ctx.getSource().getSender(), TardisCommand.abandon);
                            if (c != null) {

                            }
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("abort")
                        .executes(ctx -> {
                            Pair<Player, Integer> c = TardisUtility.check(plugin, ctx.getSource().getSender(), TardisCommand.abort);
                            if (c != null) {

                            }
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("add")
                        .executes(ctx -> {
                            Pair<Player, Integer> c = TardisUtility.check(plugin, ctx.getSource().getSender(), TardisCommand.add);
                            if (c != null) {
                                new AddCompanionCommand(plugin).doAddGUI(c.getFirst());
                            }
                            return Command.SINGLE_SUCCESS;
                        })
                        .then(Commands.argument("player", StringArgumentType.word())
                                .executes(ctx -> {
                                    Pair<Player, Integer> c = TardisUtility.check(plugin, ctx.getSource().getSender(), TardisCommand.add);
                                    if (c != null) {
                                        String p = StringArgumentType.getString(ctx, "player");
                                        new AddCompanionCommand(plugin).doAdd(c.getFirst(), p);
                                    }
                                    return Command.SINGLE_SUCCESS;
                                })))
                .then(Commands.literal("arch_time")
                        .executes(ctx -> {
                            Pair<Player, Integer> c = TardisUtility.check(plugin, ctx.getSource().getSender(), TardisCommand.arch_time);
                            if (c != null) {
                                new TARDISArchCommand(plugin).getTime(c.getFirst());
                            }
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("archive")
                        .then(Commands.literal("add")
                                .then(Commands.argument("name", StringArgumentType.word())
                                        .then(Commands.argument("size", new ConsoleSizeArgumentType())
                                                .executes(ctx -> {
                                                    Pair<Player, Integer> c = TardisUtility.check(plugin, ctx.getSource().getSender(), TardisCommand.archive);
                                                    if (c != null) {
                                                        String n = StringArgumentType.getString(ctx, "name");
                                                        String s = ctx.getArgument("size", String.class);
                                                        ArchiveUtility.add(plugin, c.getFirst(), n, s);
                                                    }
                                                    return Command.SINGLE_SUCCESS;
                                                }))))
                        .then(Commands.literal("description")
                                .then(Commands.argument("name", StringArgumentType.word())
                                        .then(Commands.argument("text", StringArgumentType.greedyString())
                                                .executes(ctx -> {
                                                    Pair<Player, Integer> c = TardisUtility.check(plugin, ctx.getSource().getSender(), TardisCommand.archive);
                                                    if (c != null) {
                                                        String n = StringArgumentType.getString(ctx, "name");
                                                        String d = StringArgumentType.getString(ctx, "text");
                                                        ArchiveUtility.description(plugin, c.getFirst(), n, d);
                                                    }
                                                    return Command.SINGLE_SUCCESS;
                                                }))))
                        .then(Commands.literal("remove")
                                .then(Commands.argument("name", StringArgumentType.word())
                                        .executes(ctx -> {
                                            Pair<Player, Integer> c = TardisUtility.check(plugin, ctx.getSource().getSender(), TardisCommand.archive);
                                            if (c != null) {
                                                String n = StringArgumentType.getString(ctx, "name");
                                                ArchiveUtility.remove(plugin, c.getFirst(), n);
                                            }
                                            return Command.SINGLE_SUCCESS;
                                        })))
                        .then(Commands.literal("rename")
                                .then(Commands.argument("old_name", StringArgumentType.word())
                                        .then(Commands.argument("new_name", StringArgumentType.word())
                                                .executes(ctx -> {
                                                    Pair<Player, Integer> c = TardisUtility.check(plugin, ctx.getSource().getSender(), TardisCommand.archive);
                                                    if (c != null) {
                                                        String n = StringArgumentType.getString(ctx, "old_name");
                                                        String r = StringArgumentType.getString(ctx, "new_name");
                                                        ArchiveUtility.rename(plugin, c.getFirst(), n, r);
                                                    }
                                                    return Command.SINGLE_SUCCESS;
                                                }))))
                        .then(Commands.literal("scan")
                                .then(Commands.argument("size", new ConsoleSizeArgumentType())
                                        .executes(ctx -> {
                                            Pair<Player, Integer> c = TardisUtility.check(plugin, ctx.getSource().getSender(), TardisCommand.archive);
                                            if (c != null) {
                                                String s = ctx.getArgument("size", String.class);
                                                ArchiveUtility.scan(plugin, c.getFirst(), s);
                                            }
                                            return Command.SINGLE_SUCCESS;
                                        })))
                        .then(Commands.literal("update")
                                .then(Commands.argument("name", StringArgumentType.word())
                                        .then(Commands.argument("size", new ConsoleSizeArgumentType())
                                                .executes(ctx -> {
                                                    Pair<Player, Integer> c = TardisUtility.check(plugin, ctx.getSource().getSender(), TardisCommand.archive);
                                                    if (c != null) {
                                                        String n = StringArgumentType.getString(ctx, "name");
                                                        String s = ctx.getArgument("size", String.class);
                                                        ArchiveUtility.update(plugin, c.getFirst(), n, s);
                                                    }
                                                    return Command.SINGLE_SUCCESS;
                                                }))))
                        .then(Commands.literal("y")
                                .then(Commands.argument("name", StringArgumentType.word())
                                        .then(Commands.argument("y", IntegerArgumentType.integer(64, 65))
                                                .executes(ctx -> {
                                                    Pair<Player, Integer> c = TardisUtility.check(plugin, ctx.getSource().getSender(), TardisCommand.archive);
                                                    if (c != null) {
                                                        String n = StringArgumentType.getString(ctx, "name");
                                                        int y = IntegerArgumentType.getInteger(ctx, "y");
                                                        ArchiveUtility.y(plugin, c.getFirst(), n, y);
                                                    }
                                                    return Command.SINGLE_SUCCESS;
                                                })))))
                .then(Commands.literal("arsremove")
                        .executes(ctx -> {
                            Pair<Player, Integer> c = TardisUtility.check(plugin, ctx.getSource().getSender(), TardisCommand.arsremove);
                            if (c != null) {
                                new ARSRemoveCommand(plugin).resetARS(c.getFirst());
                            }
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("bell")
                        .executes(ctx -> {
                            Pair<Player, Integer> c = TardisUtility.check(plugin, ctx.getSource().getSender(), TardisCommand.bell);
                            if (c != null) {
                                new BellCommand(plugin).toggle(c.getSecond(), c.getFirst(), "");
                            }
                            return Command.SINGLE_SUCCESS;
                        })
                        .then(Commands.argument("toggle", new OnOffArgumentType())
                                .executes(ctx -> {
                                    Pair<Player, Integer> c = TardisUtility.check(plugin, ctx.getSource().getSender(), TardisCommand.bell);
                                    if (c != null) {
                                        String o = ctx.getArgument("toggle", String.class);
                                        new BellCommand(plugin).toggle(c.getSecond(), c.getFirst(), o);
                                    }
                                    return Command.SINGLE_SUCCESS;
                                })))
                .then(Commands.literal("check_loc")
                        .executes(ctx -> {
                            Pair<Player, Integer> c = TardisUtility.check(plugin, ctx.getSource().getSender(), TardisCommand.check_loc);
                            if (c != null) {
                                new CheckLocationCommand(plugin).doACheckLocation(c.getFirst());
                            }
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("colorize")
                        .executes(ctx -> {
                            Pair<Player, Integer> c = TardisUtility.check(plugin, ctx.getSource().getSender(), TardisCommand.colorize);
                            if (c != null) {
                                new ColouriseCommand(plugin).updateBeaconGlass(c.getFirst());
                            }
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("colourise")
                        .executes(ctx -> {
                            Pair<Player, Integer> c = TardisUtility.check(plugin, ctx.getSource().getSender(), TardisCommand.colourise);
                            if (c != null) {
                                new ColouriseCommand(plugin).updateBeaconGlass(c.getFirst());
                            }
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("comehere")
                        .executes(ctx -> {
                            Pair<Player, Integer> c = TardisUtility.check(plugin, ctx.getSource().getSender(), TardisCommand.comehere);
                            if (c != null) {
                                new ComehereCommand(plugin).doComeHere(c.getFirst());
                            }
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("construct")
                        .then(Commands.argument("line", IntegerArgumentType.integer(1, 4))
                                .then(Commands.argument("text", StringArgumentType.greedyString())
                                        .executes(ctx -> {
                                            Pair<Player, Integer> c = TardisUtility.check(plugin, ctx.getSource().getSender(), TardisCommand.construct);
                                            if (c != null) {
                                                int l = IntegerArgumentType.getInteger(ctx, "line");
                                                String t = StringArgumentType.getString(ctx, "text");
                                                new ConstructCommand(plugin).setLine(c.getFirst(), l, t);
                                            }
                                            return Command.SINGLE_SUCCESS;
                                        }))))
                .then(Commands.literal("cube")
                        .executes(ctx -> {
                            Pair<Player, Integer> c = TardisUtility.check(plugin, ctx.getSource().getSender(), TardisCommand.cube);
                            if (c != null) {
                                new SiegeCubeCommand(plugin).whoHasCube(c.getFirst());
                            }
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("decommission")
                        .then(Commands.argument("tardis_block", new UpdateableArgumentType())
                                .executes(ctx -> {
                                    Pair<Player, Integer> c = TardisUtility.check(plugin, ctx.getSource().getSender(), TardisCommand.decommission);
                                    if (c != null) {
                                        String b = ctx.getArgument("tardis_block", String.class);
                                        new DecommissionCommand(plugin).withdraw(c.getFirst(), b);
                                    }
                                    return Command.SINGLE_SUCCESS;
                                })))
                .then(Commands.literal("desktop")
                        .executes(ctx -> {
                            Pair<Player, Integer> c = TardisUtility.check(plugin, ctx.getSource().getSender(), TardisCommand.desktop);
                            if (c != null) {
                                new UpgradeCommand(plugin).openUpgradeGUI(c.getFirst());
                            }
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("direction")
                        .then(Commands.argument("facing", new ChameleonDirectionArgumentType())
                                .executes(ctx -> {
                                    Pair<Player, Integer> c = TardisUtility.check(plugin, ctx.getSource().getSender(), TardisCommand.direction);
                                    if (c != null) {
                                        String d = ctx.getArgument("facing", String.class);
                                        new DirectionCommand(plugin).changeDirection(c.getFirst(), d);
                                    }
                                    return Command.SINGLE_SUCCESS;
                                })))
                .then(Commands.literal("door")
                        .then(Commands.literal("close")
                                .executes(ctx -> {
                                    Pair<Player, Integer> c = TardisUtility.check(plugin, ctx.getSource().getSender(), TardisCommand.door);
                                    if (c != null) {
                                        new DoorCommand(plugin).toggleDoors(c.getFirst(), true);
                                    }
                                    return Command.SINGLE_SUCCESS;
                                }))
                        .then(Commands.literal("open")
                                .executes(ctx -> {
                                    Pair<Player, Integer> c = TardisUtility.check(plugin, ctx.getSource().getSender(), TardisCommand.door);
                                    if (c != null) {
                                        new DoorCommand(plugin).toggleDoors(c.getFirst(), false);
                                    }
                                    return Command.SINGLE_SUCCESS;
                                })))
                .then(Commands.literal("egg")
                        .executes(ctx -> {
                            Pair<Player, Integer> c = TardisUtility.check(plugin, ctx.getSource().getSender(), TardisCommand.egg);
                            if (c != null) {
                                new ThemeMusicCommand(plugin).play(c.getFirst(), "");
                            }
                            return Command.SINGLE_SUCCESS;
                        })
                        .then(Commands.argument("theme", new ThemeArgumentType())
                                .executes(ctx -> {
                                    Pair<Player, Integer> c = TardisUtility.check(plugin, ctx.getSource().getSender(), TardisCommand.egg);
                                    if (c != null) {
                                        String t = ctx.getArgument("theme", String.class);
                                        new ThemeMusicCommand(plugin).play(c.getFirst(), t);
                                    }
                                    return Command.SINGLE_SUCCESS;
                                })))
                .then(Commands.literal("eject")
                        .executes(ctx -> {
                            Pair<Player, Integer> c = TardisUtility.check(plugin, ctx.getSource().getSender(), TardisCommand.eject);
                            if (c != null) {
                                new EjectCommand(plugin).eject(c.getFirst());
                            }
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("ep1")
                        .executes(ctx -> {
                            Pair<Player, Integer> c = TardisUtility.check(plugin, ctx.getSource().getSender(), TardisCommand.ep1);
                            if (c != null) {
                                new EmergencyProgrammeOneCommand(plugin).showEP1(c.getFirst());
                            }
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("erase")
                        .executes(ctx -> {
                            Pair<Player, Integer> c = TardisUtility.check(plugin, ctx.getSource().getSender(), TardisCommand.erase);
                            if (c != null) {
                                new DiskWriterCommand(plugin).eraseDisk(c.getFirst());
                            }
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("excite")
                        .executes(ctx -> {
                            Pair<Player, Integer> c = TardisUtility.check(plugin, ctx.getSource().getSender(), TardisCommand.excite);
                            if (c != null) {
                                new ExciteCommand(plugin).excite(c.getFirst());
                            }
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("exterminate")
                        .executes(ctx -> {
                            Pair<Player, Integer> c = TardisUtility.check(plugin, ctx.getSource().getSender(), TardisCommand.exterminate);
                            if (c != null) {
                                new ExterminateCommand(plugin).doExterminate(c.getFirst(), true);
                            }
                            return Command.SINGLE_SUCCESS;
                        })
                        .then(Commands.argument("extra", StringArgumentType.greedyString())
                                .executes(ctx -> {
                                    Pair<Player, Integer> c = TardisUtility.check(plugin, ctx.getSource().getSender(), TardisCommand.exterminate);
                                    if (c != null) {
                                        new ExterminateCommand(plugin).doExterminate(c.getFirst(), false);
                                    }
                                    return Command.SINGLE_SUCCESS;
                                })))
                .then(Commands.literal("find")
                        .executes(ctx -> {
                            Pair<Player, Integer> c = TardisUtility.check(plugin, ctx.getSource().getSender(), TardisCommand.find);
                            if (c != null) {
                                new FindCommand(plugin).findTARDIS(c.getFirst());
                            }
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("handbrake")
                        .then(Commands.argument("toggle", new OnOffArgumentType())
                                .executes(ctx -> {
                                    Pair<Player, Integer> c = TardisUtility.check(plugin, ctx.getSource().getSender(), TardisCommand.handbrake);
                                    if (c != null) {
                                        String o = ctx.getArgument("toggle", String.class);
                                        new HandbrakeCommand(plugin).toggle(c.getFirst(), c.getSecond(), o, false);
                                    }
                                    return Command.SINGLE_SUCCESS;
                                })))
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
                            Pair<Player, Integer> c = TardisUtility.check(plugin, ctx.getSource().getSender(), TardisCommand.hide);
                            if (c != null) {
                                new HideCommand(plugin).hide(c.getFirst());
                            }
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("inside")
                        .executes(ctx -> {
                            Pair<Player, Integer> c = TardisUtility.check(plugin, ctx.getSource().getSender(), TardisCommand.inside);
                            if (c != null) {
                                new InsideCommand(plugin).whosInside(c.getFirst());
                            }
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("item")
                        .then(Commands.argument("which", StringArgumentType.word())
                                .suggests((ctx, builder) -> {
                                    builder.suggest("hand");
                                    builder.suggest("inventory");
                                    builder.suggest("cell");
                                    return builder.buildFuture();
                                })
                                .executes(ctx -> {
                                    Pair<Player, Integer> c = TardisUtility.check(plugin, ctx.getSource().getSender(), TardisCommand.item);
                                    if (c != null) {
                                        String w = StringArgumentType.getString(ctx, "which");
                                        new ItemCommand(plugin).update(c.getFirst(), w);
                                    }
                                    return Command.SINGLE_SUCCESS;
                                })))
                .then(Commands.literal("jettison")
                        .then(Commands.argument("room", new RoomArgumentType(plugin, false))
                                .executes(ctx -> {
                                    Pair<Player, Integer> c = TardisUtility.check(plugin, ctx.getSource().getSender(), TardisCommand.jettison);
                                    if (c != null) {
                                        String r = ctx.getArgument("room", String.class);
                                        new JettisonCommand(plugin).startJettison(c.getFirst(), r);
                                    }
                                    return Command.SINGLE_SUCCESS;
                                })))
                .then(Commands.literal("lamps")
                        .then(Commands.literal("auto")
                                .executes(ctx -> {
                                    Pair<Player, Integer> c = TardisUtility.check(plugin, ctx.getSource().getSender(), TardisCommand.lamps);
                                    if (c != null && TARDISPermission.hasPermission(c.getFirst(), "tardis.update")) {
                                        new LampsCommand(plugin).addLampBlocks(c.getFirst());
                                    }
                                    return Command.SINGLE_SUCCESS;
                                }))
                        .then(Commands.literal("list")
                                .executes(ctx -> {
                                    Pair<Player, Integer> c = TardisUtility.check(plugin, ctx.getSource().getSender(), TardisCommand.lamps);
                                    if (c != null) {
                                        new LampsCommand(plugin).listLampBlocks(c.getFirst());
                                    }
                                    return Command.SINGLE_SUCCESS;
                                }))
                        .then(Commands.literal("set")
                                .then(Commands.argument("coords", ArgumentTypes.blockPosition())
                                        .then(Commands.argument("material_on", StringArgumentType.word())
                                                .suggests(BlockSuggestions::get)
                                                .executes(ctx -> {
                                                    Pair<Player, Integer> c = TardisUtility.check(plugin, ctx.getSource().getSender(), TardisCommand.lamps);
                                                    if (c != null) {
                                                        BlockPositionResolver resolver = ctx.getArgument("coords", BlockPositionResolver.class);
                                                        BlockPosition pos = resolver.resolve(ctx.getSource());
                                                        String materialOn = StringArgumentType.getString(ctx, "material_on");
                                                        new LampsCommand(plugin).setLampBlock(c.getFirst(), pos.blockX(), pos.blockY(), pos.blockZ(), materialOn, "", -1f);
                                                    }
                                                    return Command.SINGLE_SUCCESS;
                                                })
                                                .then(Commands.argument("material_off", StringArgumentType.word())
                                                        .suggests(BlockSuggestions::get)
                                                        .executes(ctx -> {
                                                            Pair<Player, Integer> c = TardisUtility.check(plugin, ctx.getSource().getSender(), TardisCommand.lamps);
                                                            if (c != null) {
                                                                BlockPositionResolver resolver = ctx.getArgument("coords", BlockPositionResolver.class);
                                                                BlockPosition pos = resolver.resolve(ctx.getSource());
                                                                String materialOn = StringArgumentType.getString(ctx, "material_on");
                                                                String materialOff = StringArgumentType.getString(ctx, "material_off");
                                                                new LampsCommand(plugin).setLampBlock(c.getFirst(), pos.blockX(), pos.blockY(), pos.blockZ(), materialOn, materialOff, -1f);
                                                            }
                                                            return Command.SINGLE_SUCCESS;
                                                        })
                                                        .then(Commands.argument("percentage", FloatArgumentType.floatArg())
                                                                .executes(ctx -> {
                                                                    Pair<Player, Integer> c = TardisUtility.check(plugin, ctx.getSource().getSender(), TardisCommand.lamps);
                                                                    if (c != null) {
                                                                        BlockPositionResolver resolver = ctx.getArgument("coords", BlockPositionResolver.class);
                                                                        BlockPosition pos = resolver.resolve(ctx.getSource());
                                                                        String materialOn = StringArgumentType.getString(ctx, "material_on");
                                                                        String materialOff = StringArgumentType.getString(ctx, "material_off");
                                                                        float f = FloatArgumentType.getFloat(ctx, "percentage");
                                                                        new LampsCommand(plugin).setLampBlock(c.getFirst(), pos.blockX(), pos.blockY(), pos.blockZ(), materialOn, materialOff, f);
                                                                    }
                                                                    return Command.SINGLE_SUCCESS;
                                                                })))))))
                .then(Commands.literal("list")
                        .then(Commands.argument("what", StringArgumentType.word())
                                .suggests((ct, builder) -> {
                                    builder.suggest("saves");
                                    builder.suggest("companions");
                                    builder.suggest("areas");
                                    builder.suggest("rechargers");
                                    return builder.buildFuture();
                                })
                                .executes(ctx -> {
                                    Pair<Player, Integer> c = TardisUtility.check(plugin, ctx.getSource().getSender(), TardisCommand.list);
                                    if (c != null) {
                                        String l = StringArgumentType.getString(ctx, "what");
                                        new ListCommand(plugin).doList(c.getFirst(), l);
                                    }
                                    return Command.SINGLE_SUCCESS;
                                })))
                .then(Commands.literal("make_her_blue")
                        .executes(ctx -> {
                            Pair<Player, Integer> c = TardisUtility.check(plugin, ctx.getSource().getSender(), TardisCommand.make_her_blue);
                            if (c != null) {
                                new MakeHerBlueCommand(plugin).show(c.getFirst());
                            }
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("monsters")
                        .then(Commands.argument("action", StringArgumentType.word())
                                .suggests((ct, builder) -> {
                                    builder.suggest("kill");
                                    builder.suggest("reset");
                                    return builder.buildFuture();
                                })
                                .executes(ctx -> {
                                    Pair<Player, Integer> c = TardisUtility.check(plugin, ctx.getSource().getSender(), TardisCommand.monsters);
                                    if (c != null) {
                                        String a = StringArgumentType.getString(ctx, "action");
                                        new MonstersCommand(plugin).reset(c.getFirst(), c.getSecond(), a);
                                    }
                                    return Command.SINGLE_SUCCESS;
                                })))
                .then(Commands.literal("namekey")
                        .then(Commands.argument("name", StringArgumentType.greedyString()))
                        .executes(ctx -> {
                            Pair<Player, Integer> c = TardisUtility.check(plugin, ctx.getSource().getSender(), TardisCommand.namekey);
                            if (c != null) {
                                String n = StringArgumentType.getString(ctx, "name");
                                new NameKeyCommand(plugin).nameKey(c.getFirst(), n);
                            }
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("occupy")
                        .executes(ctx -> {
                            Pair<Player, Integer> c = TardisUtility.check(plugin, ctx.getSource().getSender(), TardisCommand.occupy);
                            if (c != null) {
                                new OccupyCommand(plugin).toggleOccupancy(c.getFirst());
                            }
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("rebuild")
                        .executes(ctx -> {
                            Pair<Player, Integer> c = TardisUtility.check(plugin, ctx.getSource().getSender(), TardisCommand.rebuild);
                            if (c != null) {
                                new RebuildCommand(plugin).rebuildPreset(c.getFirst());
                            }
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("remove")
                        .then(Commands.argument("player", StringArgumentType.word())
                                .executes(ctx -> {
                                    Pair<Player, Integer> c = TardisUtility.check(plugin, ctx.getSource().getSender(), TardisCommand.remove);
                                    if (c != null) {
                                        String p = StringArgumentType.getString(ctx, "player");
                                        new RemoveCompanionCommand(plugin).doRemoveCompanion(c.getFirst(), p);
                                    }
                                    return Command.SINGLE_SUCCESS;
                                })))
                .then(Commands.literal("removesave")
                        .then(Commands.argument("name", StringArgumentType.word())
                                .executes(ctx -> {
                                    Pair<Player, Integer> c = TardisUtility.check(plugin, ctx.getSource().getSender(), TardisCommand.removesave);
                                    if (c != null) {
                                        String n = StringArgumentType.getString(ctx, "name");
                                        new RemoveSavedLocationCommand(plugin).doRemoveSave(c.getFirst(), n);
                                    }
                                    return Command.SINGLE_SUCCESS;
                                })))
                .then(Commands.literal("renamesave")
                        .then(Commands.argument("old_name", StringArgumentType.word())
                                .then(Commands.argument("new_name", StringArgumentType.word())
                                        .executes(ctx -> {
                                            Pair<Player, Integer> c = TardisUtility.check(plugin, ctx.getSource().getSender(), TardisCommand.renamesave);
                                            if (c != null) {
                                                String o = StringArgumentType.getString(ctx, "old_name");
                                                String n = StringArgumentType.getString(ctx, "new_name");
                                                new RenameSavedLocationCommand(plugin).doRenameSave(c.getFirst(), o, n);
                                            }
                                            return Command.SINGLE_SUCCESS;
                                        }))))
                .then(Commands.literal("reordersave")
                        .then(Commands.argument("name", StringArgumentType.word())
                                .then(Commands.argument("slot", IntegerArgumentType.integer(1))
                                        .executes(ctx -> {
                                            Pair<Player, Integer> c = TardisUtility.check(plugin, ctx.getSource().getSender(), TardisCommand.reordersave);
                                            if (c != null) {
                                                String n = StringArgumentType.getString(ctx, "name");
                                                int s = IntegerArgumentType.getInteger(ctx, "slot");
                                                new ReorderSavedLocationCommand(plugin).doReorderSave(c.getFirst(), n, s);
                                            }
                                            return Command.SINGLE_SUCCESS;
                                        }))))
                .then(Commands.literal("rescue")
                        .then(Commands.literal("accept")
                                .executes(ctx -> {
                                    Pair<Player, Integer> c = TardisUtility.check(plugin, ctx.getSource().getSender(), TardisCommand.rescue);
                                    if (c != null) {
                                        new RescueAcceptor(plugin).doRequest(c.getFirst(), false);
                                    }
                                    return Command.SINGLE_SUCCESS;
                                })
                                .then(Commands.argument("player", ArgumentTypes.player())
                                        .executes(ctx -> {
                                            Pair<Player, Integer> c = TardisUtility.check(plugin, ctx.getSource().getSender(), TardisCommand.rescue);
                                            if (c != null) {
                                                PlayerSelectorArgumentResolver targetResolver = ctx.getArgument("player", PlayerSelectorArgumentResolver.class);
                                                Player player = targetResolver.resolve(ctx.getSource()).getFirst();
                                                new RescueCommand(plugin).startRescue(c.getFirst(), player);
                                            }
                                            return Command.SINGLE_SUCCESS;
                                        }))))
                .then(Commands.literal("room")
                        .then(Commands.argument("name", new RoomArgumentType(plugin, false))
                                .executes(ctx -> {
                                    Pair<Player, Integer> c = TardisUtility.check(plugin, ctx.getSource().getSender(), TardisCommand.room);
                                    if (c != null) {
                                        String room = StringArgumentType.getString(ctx, "name");
                                        new RoomCommand(plugin).startRoom(c.getFirst(), room);
                                    }
                                    return Command.SINGLE_SUCCESS;
                                })))
                .then(Commands.literal("save")
                        .then(Commands.argument("name", StringArgumentType.word())
                                .executes(ctx -> {
                                    Pair<Player, Integer> c = TardisUtility.check(plugin, ctx.getSource().getSender(), TardisCommand.save);
                                    if (c != null) {
                                        String n = StringArgumentType.getString(ctx, "name");
                                        TardisUtility.doSave(plugin, c.getFirst(), n, false);
                                    }
                                    return Command.SINGLE_SUCCESS;
                                })
                                .then(Commands.literal("true")
                                        .executes(ctx -> {
                                            Pair<Player, Integer> c = TardisUtility.check(plugin, ctx.getSource().getSender(), TardisCommand.save);
                                            if (c != null) {
                                                String n = StringArgumentType.getString(ctx, "name");
                                                TardisUtility.doSave(plugin, c.getFirst(), n, true);
                                            }
                                            return Command.SINGLE_SUCCESS;
                                        }))))
                .then(Commands.literal("save_player")
                        .then(Commands.argument("target_player", ArgumentTypes.player())
                                .executes(ctx -> {
                                    Pair<Player, Integer> c = TardisUtility.check(plugin, ctx.getSource().getSender(), TardisCommand.save_player);
                                    if (c != null) {
                                        ItemStack is = c.getFirst().getInventory().getItemInMainHand();
                                        if (TardisUtility.heldDiskIsWrong(is, "Player Storage Disk")) {
                                            plugin.getMessenger().send(c.getFirst(), TardisModule.TARDIS, "DISK_HAND_PLAYER");
                                            return Command.SINGLE_SUCCESS;
                                        }
                                        PlayerSelectorArgumentResolver targetResolver = ctx.getArgument("player", PlayerSelectorArgumentResolver.class);
                                        Player player = targetResolver.resolve(ctx.getSource()).getFirst();
                                        new DiskWriterCommand(plugin).writePlayer(c.getFirst(), player);
                                    }
                                    return Command.SINGLE_SUCCESS;
                                })))
                .then(Commands.literal("saveicon")
                        .then(Commands.argument("name", StringArgumentType.word())
                                .then(Commands.argument("icon", StringArgumentType.word())
                                        .suggests(BlockSuggestions::get)
                                        .executes(ctx -> {
                                            Pair<Player, Integer> c = TardisUtility.check(plugin, ctx.getSource().getSender(), TardisCommand.saveicon);
                                            if (c != null) {
                                                String n = StringArgumentType.getString(ctx, "name");
                                                String i = StringArgumentType.getString(ctx, "icon");
                                                new SaveIconCommand(plugin).changeIcon(c.getFirst(), n, i, false);
                                            }
                                            return Command.SINGLE_SUCCESS;
                                        }))))
                .then(Commands.literal("secondary")
                        .then(Commands.literal("remove")
                                .executes(ctx -> {
                                    Pair<Player, Integer> c = TardisUtility.check(plugin, ctx.getSource().getSender(), TardisCommand.secondary);
                                    if (c != null) {
                                        plugin.getTrackerKeeper().getSecondaryRemovers().put(c.getFirst().getUniqueId(), c.getSecond());
                                        plugin.getMessenger().send(c.getFirst(), TardisModule.TARDIS, "SEC_REMOVE_CLICK_BLOCK");
                                    }
                                    return Command.SINGLE_SUCCESS;
                                }))
                        .then(Commands.argument("which", new SecondaryArgumentType())
                                .executes(ctx -> {
                                    Pair<Player, Integer> c = TardisUtility.check(plugin, ctx.getSource().getSender(), TardisCommand.secondary);
                                    if (c != null) {
                                        String w = ctx.getArgument("which", String.class);
                                        new SecondaryCommand(plugin).startSecondary(c.getFirst(), w);
                                    }
                                    return Command.SINGLE_SUCCESS;
                                })))
                .then(Commands.literal("section")
                        .executes(ctx -> {
                            Pair<Player, Integer> c = TardisUtility.check(plugin, ctx.getSource().getSender(), TardisCommand.section);
                            if (c != null) {
                                new UpdateChatGUI(plugin).showInterface(c.getFirst(), "");
                            }
                            return Command.SINGLE_SUCCESS;
                        })
                        .then(Commands.argument("which", new InterfaceArgumentType())
                                .executes(ctx -> {
                                    Pair<Player, Integer> c = TardisUtility.check(plugin, ctx.getSource().getSender(), TardisCommand.section);
                                    if (c != null) {
                                        String s = ctx.getArgument("which", String.class);
                                        new UpdateChatGUI(plugin).showInterface(c.getFirst(), s);
                                    }
                                    return Command.SINGLE_SUCCESS;
                                })))
                .then(Commands.literal("setdest")
                        .then(Commands.argument("name", StringArgumentType.word())
                                .executes(ctx -> {
                                    Pair<Player, Integer> c = TardisUtility.check(plugin, ctx.getSource().getSender(), TardisCommand.setdest);
                                    if (c != null) {
                                        String n = StringArgumentType.getString(ctx, "name");
                                        new SetDestinationCommand(plugin).doSetDestination(c.getFirst(), n);
                                    }
                                    return Command.SINGLE_SUCCESS;
                                })))
                .then(Commands.literal("sethome")
                        .executes(ctx -> {
                            Pair<Player, Integer> c = TardisUtility.check(plugin, ctx.getSource().getSender(), TardisCommand.sethome);
                            if (c != null) {
                                new SetHomeCommand(plugin).setHome(c.getFirst(), "", "");
                            }
                            return Command.SINGLE_SUCCESS;
                        })
                        .then(Commands.literal("preset")
                                .then(Commands.argument("type", new PresetArgumentType(0))
                                        .executes(ctx -> {
                                            Pair<Player, Integer> c = TardisUtility.check(plugin, ctx.getSource().getSender(), TardisCommand.sethome);
                                            if (c != null) {
                                                String t = ctx.getArgument("type", String.class);
                                                new SetHomeCommand(plugin).setHome(c.getFirst(), "preset", t);
                                            }
                                            return Command.SINGLE_SUCCESS;
                                        }))))
                .then(Commands.literal("stop_sound")
                        .executes(ctx -> {
                            if (ctx.getSource().getSender() instanceof Player player) {
                                new StopSoundCommand(plugin).mute(player);
                            }
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("tagtheood")
                        .executes(ctx -> {
                            Pair<Player, Integer> c = TardisUtility.check(plugin, ctx.getSource().getSender(), TardisCommand.tagtheood);
                            if (c != null) {
                                new TagCommand(plugin).getStats(c.getFirst());
                            }
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("theme")
                        .executes(ctx -> {
                            Pair<Player, Integer> c = TardisUtility.check(plugin, ctx.getSource().getSender(), TardisCommand.theme);
                            if (c != null) {
                                new UpgradeCommand(plugin).openUpgradeGUI(c.getFirst());
                            }
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("transmat")
                        .then(Commands.literal("list")
                                .executes(ctx -> {
                                    Pair<Player, Integer> c = TardisUtility.check(plugin, ctx.getSource().getSender(), TardisCommand.transmat);
                                    if (c != null) {
                                        TransmatUtility.list(plugin, c.getFirst(), c.getSecond());
                                    }
                                    return Command.SINGLE_SUCCESS;
                                })
                                .then(Commands.literal("tp")
                                        .then(Commands.literal("Rooms")
                                                .executes(ctx -> {
                                                    Pair<Player, Integer> c = TardisUtility.check(plugin, ctx.getSource().getSender(), TardisCommand.transmat);
                                                    if (c != null) {
                                                        TransmatUtility.toRoomsWorld(plugin, c.getFirst(), c.getSecond());
                                                    }
                                                    return Command.SINGLE_SUCCESS;
                                                }))
                                        .then(Commands.argument("to", StringArgumentType.word())
                                                .executes(ctx -> {
                                                    Pair<Player, Integer> c = TardisUtility.check(plugin, ctx.getSource().getSender(), TardisCommand.transmat);
                                                    if (c != null) {
                                                        String w = StringArgumentType.getString(ctx, "to");
                                                        TransmatUtility.tp(plugin, c.getFirst(), w, c.getSecond());
                                                    }
                                                    return Command.SINGLE_SUCCESS;
                                                })))))
                .then(Commands.literal("update")
                        .executes(ctx -> {
                            Pair<Player, Integer> c = TardisUtility.check(plugin, ctx.getSource().getSender(), TardisCommand.update);
                            if (c != null) {
                                new UpdateChatGUI(plugin).showInterface(c.getFirst(), "");
                            }
                            return Command.SINGLE_SUCCESS;
                        })
                        .then(Commands.literal("blocks")
                                .executes(ctx -> {
                                    Pair<Player, Integer> c = TardisUtility.check(plugin, ctx.getSource().getSender(), TardisCommand.update);
                                    if (c != null) {
                                        new UpdateBlocksCommand(plugin).convert(c.getFirst());
                                    }
                                    return Command.SINGLE_SUCCESS;
                                }))
                        .then(Commands.literal("blocks")
                                .executes(ctx -> {
                                    Pair<Player, Integer> c = TardisUtility.check(plugin, ctx.getSource().getSender(), TardisCommand.update);
                                    if (c != null) {
                                        new UpdateBlocksCommand(plugin).convert(c.getFirst());
                                    }
                                    return Command.SINGLE_SUCCESS;
                                }))
                        .then(Commands.literal("lunge")
                                .executes(ctx -> {
                                    Pair<Player, Integer> c = TardisUtility.check(plugin, ctx.getSource().getSender(), TardisCommand.update);
                                    if (c != null) {
                                        new UpdateLungeCommand(plugin).addChiseledShelves(c.getFirst());
                                    }
                                    return Command.SINGLE_SUCCESS;
                                }))
                        .then(Commands.literal("remove_displays")
                                .executes(ctx -> {
                                    Pair<Player, Integer> c = TardisUtility.check(plugin, ctx.getSource().getSender(), TardisCommand.update);
                                    if (c != null) {
                                        new UpdateBlocksCommand(plugin).remove_displays(c.getFirst());
                                    }
                                    return Command.SINGLE_SUCCESS;
                                }))
                        .then(Commands.literal("handles")
                                .then(Commands.argument("toggle", StringArgumentType.word())
                                        .suggests((ctx, builder) -> {
                                            builder.suggest("lock");
                                            builder.suggest("unlock");
                                            return builder.buildFuture();
                                        })
                                        .executes(ctx -> {
                                            Pair<Player, Integer> c = TardisUtility.check(plugin, ctx.getSource().getSender(), TardisCommand.update);
                                            if (c != null) {
                                                String toggle = StringArgumentType.getString(ctx, "toggle");
                                                if (toggle.equalsIgnoreCase("lock")) {
                                                    plugin.getTrackerKeeper().getHandlesRotation().remove(c.getFirst().getUniqueId());
                                                    plugin.getMessenger().send(c.getFirst(), TardisModule.HANDLES, "HANDLES_LOCKED");
                                                } else {
                                                    plugin.getTrackerKeeper().getHandlesRotation().add(c.getFirst().getUniqueId());
                                                    plugin.getMessenger().send(c.getFirst(), TardisModule.HANDLES, "HANDLES_ROTATE");
                                                }
                                            }
                                            return Command.SINGLE_SUCCESS;
                                        })))
                        .then(Commands.literal("hinge")
                                .executes(ctx -> {
                                    Pair<Player, Integer> c = TardisUtility.check(plugin, ctx.getSource().getSender(), TardisCommand.update);
                                    if (c != null) {
                                        Block block = c.getFirst().getTargetBlock(plugin.getGeneralKeeper().getTransparent(), 10);
                                        Door door = (Door) block.getBlockData();
                                        Door.Hinge hinge = door.getHinge();
                                        if (hinge.equals(Door.Hinge.LEFT)) {
                                            door.setHinge(Door.Hinge.RIGHT);
                                        } else {
                                            door.setHinge(Door.Hinge.LEFT);
                                        }
                                        block.setBlockData(door);
                                    }
                                    return Command.SINGLE_SUCCESS;
                                }))
                        .then(Commands.argument("what", new UpdateableArgumentType())
                                .executes(ctx -> {
                                    Pair<Player, Tardis> c = TardisUtility.updateCheck(plugin, ctx.getSource().getSender(), TardisCommand.update);
                                    if (c != null) {
                                        String what = ctx.getArgument("what", String.class);
                                        Updateable updateable = Updateable.valueOf(what.toUpperCase());
                                        TardisUtility.update(plugin, c.getFirst(), updateable, c.getSecond());
                                    }
                                    return Command.SINGLE_SUCCESS;
                                })
                                .then(Commands.literal("blocks").executes(ctx -> {
                                    Pair<Player, Integer> c = TardisUtility.check(plugin, ctx.getSource().getSender(), TardisCommand.update);
                                    if (c != null) {
                                        String what = ctx.getArgument("what", String.class);
                                        Updateable updateable = Updateable.valueOf(what.toUpperCase());
                                        TARDISUpdateBlocks.showOptions(c.getFirst(), updateable);
                                    }
                                    return Command.SINGLE_SUCCESS;
                                }))
                                .then(Commands.literal("unlock").executes(ctx -> {
                                    Pair<Player, Tardis> c = TardisUtility.updateCheck(plugin, ctx.getSource().getSender(), TardisCommand.update);
                                    if (c != null) {
                                        String what = ctx.getArgument("what", String.class);
                                        Updateable updateable = Updateable.valueOf(what.toUpperCase());
                                        if (updateable.equals(Updateable.ROTOR) || updateable.equals(Updateable.MONITOR) || updateable.equals(Updateable.MONITOR_FRAME) || updateable.equals(Updateable.SONIC_DOCK)) {
                                            TardisUtility.unlock(plugin, c.getFirst(), updateable, c.getSecond());
                                        }
                                    }
                                    return Command.SINGLE_SUCCESS;
                                }))))
                .then(Commands.literal("upgrade")
                        .executes(ctx -> {
                            Pair<Player, Integer> c = TardisUtility.check(plugin, ctx.getSource().getSender(), TardisCommand.upgrade);
                            if (c != null) {
                                new UpgradeCommand(plugin).openUpgradeGUI(c.getFirst());
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
