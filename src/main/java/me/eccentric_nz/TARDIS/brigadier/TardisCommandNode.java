package me.eccentric_nz.TARDIS.brigadier;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.mojang.datafixers.util.Pair;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.advanced.DiskWriterCommand;
import me.eccentric_nz.TARDIS.arch.TARDISArchCommand;
import me.eccentric_nz.TARDIS.brigadier.arguments.*;
import me.eccentric_nz.TARDIS.brigadier.suggestions.BlockSuggestions;
import me.eccentric_nz.TARDIS.commands.TARDISCommandHelper;
import me.eccentric_nz.TARDIS.commands.tardis.*;
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
                        .then(Commands.argument("room", new RoomArgumentType(plugin))
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
                                    if (c != null) {
                                        new LampsCommand(plugin).zip(c.getFirst(), "set");
                                    }
                                    return Command.SINGLE_SUCCESS;
                                }))
                        .then(Commands.literal("list")
                                .executes(ctx -> {
                                    Pair<Player, Integer> c = TardisUtility.check(plugin, ctx.getSource().getSender(), TardisCommand.lamps);
                                    if (c != null) {
                                        new LampsCommand(plugin).zip(c.getFirst(), "list");
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
// TODO
                                                        new LampsCommand(plugin).zip(c.getFirst(), "");
                                                    }
                                                    return Command.SINGLE_SUCCESS;
                                                })))))
                .then(Commands.literal("list")
                        .executes(ctx -> {
                            Pair<Player, Integer> c = TardisUtility.check(plugin, ctx.getSource().getSender(), TardisCommand.list);
                            if (c != null) {

                            }
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("make_her_blue")
                        .executes(ctx -> {
                            Pair<Player, Integer> c = TardisUtility.check(plugin, ctx.getSource().getSender(), TardisCommand.make_her_blue);
                            if (c != null) {

                            }
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("monsters")
                        .executes(ctx -> {
                            Pair<Player, Integer> c = TardisUtility.check(plugin, ctx.getSource().getSender(), TardisCommand.monsters);
                            if (c != null) {

                            }
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("namekey")
                        .executes(ctx -> {
                            Pair<Player, Integer> c = TardisUtility.check(plugin, ctx.getSource().getSender(), TardisCommand.namekey);
                            if (c != null) {

                            }
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("occupy")
                        .executes(ctx -> {
                            Pair<Player, Integer> c = TardisUtility.check(plugin, ctx.getSource().getSender(), TardisCommand.occupy);
                            if (c != null) {

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
                        .executes(ctx -> {
                            Pair<Player, Integer> c = TardisUtility.check(plugin, ctx.getSource().getSender(), TardisCommand.remove);
                            if (c != null) {

                            }
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("removesave")
                        .executes(ctx -> {
                            Pair<Player, Integer> c = TardisUtility.check(plugin, ctx.getSource().getSender(), TardisCommand.removesave);
                            if (c != null) {

                            }
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("renamesave")
                        .executes(ctx -> {
                            Pair<Player, Integer> c = TardisUtility.check(plugin, ctx.getSource().getSender(), TardisCommand.renamesave);
                            if (c != null) {

                            }
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("reordersave")
                        .executes(ctx -> {
                            Pair<Player, Integer> c = TardisUtility.check(plugin, ctx.getSource().getSender(), TardisCommand.reordersave);
                            if (c != null) {

                            }
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("rescue")
                        .executes(ctx -> {
                            Pair<Player, Integer> c = TardisUtility.check(plugin, ctx.getSource().getSender(), TardisCommand.rescue);
                            if (c != null) {

                            }
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("room")
                        .executes(ctx -> {
                            Pair<Player, Integer> c = TardisUtility.check(plugin, ctx.getSource().getSender(), TardisCommand.room);
                            if (c != null) {

                            }
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("save")
                        .executes(ctx -> {
                            Pair<Player, Integer> c = TardisUtility.check(plugin, ctx.getSource().getSender(), TardisCommand.save);
                            if (c != null) {

                            }
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("save_player")
                        .executes(ctx -> {
                            Pair<Player, Integer> c = TardisUtility.check(plugin, ctx.getSource().getSender(), TardisCommand.save_player);
                            if (c != null) {

                            }
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("saveicon")
                        .executes(ctx -> {
                            Pair<Player, Integer> c = TardisUtility.check(plugin, ctx.getSource().getSender(), TardisCommand.saveicon);
                            if (c != null) {

                            }
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("secondary")
                        .executes(ctx -> {
                            Pair<Player, Integer> c = TardisUtility.check(plugin, ctx.getSource().getSender(), TardisCommand.secondary);
                            if (c != null) {

                            }
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("section")
                        .executes(ctx -> {
                            Pair<Player, Integer> c = TardisUtility.check(plugin, ctx.getSource().getSender(), TardisCommand.section);
                            if (c != null) {

                            }
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("setdest")
                        .executes(ctx -> {
                            Pair<Player, Integer> c = TardisUtility.check(plugin, ctx.getSource().getSender(), TardisCommand.setdest);
                            if (c != null) {

                            }
                            return Command.SINGLE_SUCCESS;
                        }))
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
                        .executes(ctx -> {
                            Pair<Player, Integer> c = TardisUtility.check(plugin, ctx.getSource().getSender(), TardisCommand.transmat);
                            if (c != null) {

                            }
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("update")
                        .executes(ctx -> {
                            Pair<Player, Integer> c = TardisUtility.check(plugin, ctx.getSource().getSender(), TardisCommand.update);
                            if (c != null) {

                            }
                            return Command.SINGLE_SUCCESS;
                        }))
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
