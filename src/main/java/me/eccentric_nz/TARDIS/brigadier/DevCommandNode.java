package me.eccentric_nz.TARDIS.brigadier;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.dialog.Dialog;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.achievement.TARDISAchievementFactory;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.brigadier.arguments.*;
import me.eccentric_nz.TARDIS.brigadier.suggestions.BlockSuggestions;
import me.eccentric_nz.TARDIS.commands.TARDISCommandHelper;
import me.eccentric_nz.TARDIS.commands.dev.*;
import me.eccentric_nz.TARDIS.commands.dev.lists.*;
import me.eccentric_nz.TARDIS.commands.dev.wiki.WikiRecipeCommand;
import me.eccentric_nz.TARDIS.commands.tardis.update.UpdateBlocksCommand;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.monitor.MonitorSnapshot;
import me.eccentric_nz.TARDIS.playerprefs.PreferencesDialog;
import me.eccentric_nz.TARDIS.rooms.games.pong.GameDisplay;
import me.eccentric_nz.TARDIS.rooms.games.rockpaperscissors.Letters;
import me.eccentric_nz.TARDIS.utility.Pluraliser;
import me.eccentric_nz.TARDIS.utility.TARDISStaticUtils;
import me.eccentric_nz.tardisregeneration.Regenerator;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;

import java.util.Locale;

public class DevCommandNode {

    private final TARDIS plugin;

    public DevCommandNode(TARDIS plugin) {
        this.plugin = plugin;
    }

    LiteralCommandNode<CommandSourceStack> build() {
        LiteralArgumentBuilder<CommandSourceStack> command = Commands.literal("tardisdev")
                .requires(ctx -> ctx.getSender() instanceof ConsoleCommandSender || TARDISPermission.hasPermission(ctx.getSender(), "tardis.admin"))
                .executes(ctx -> {
                    new TARDISCommandHelper(plugin).getCommand("tardisdev", ctx.getSource().getSender());
                    return Command.SINGLE_SUCCESS;
                })
                .then(Commands.literal("add_regions")
                        .executes(ctx -> {
                            new AddRegionsCommand(plugin).doCheck(ctx.getSource().getSender());
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("advancements")
                        .then(Commands.argument("which", StringArgumentType.word()))
                        .executes(ctx -> {
                            String a = StringArgumentType.getString(ctx, "which");
                            TARDISAchievementFactory.checkAdvancement(a);
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("armour")
                        .then(Commands.argument("monster", new MonsterArgumentType())
                                .then(Commands.argument("slot", StringArgumentType.word())
                                        .suggests((ctx, builder) -> {
                                            builder.suggest("CHEST");
                                            builder.suggest("LEGS");
                                            return builder.buildFuture();
                                        })
                                        .executes(ctx -> {
                                            if (ctx.getSource().getSender() instanceof Player player) {
                                                String m = ctx.getArgument("monster", String.class);
                                                String s = StringArgumentType.getString(ctx, "slot");
                                                DevelopmentUtility.setArmour(player, m, s);
                                            }
                                            return Command.SINGLE_SUCCESS;
                                        }))))
                .then(Commands.literal("banner")
                        .executes(ctx -> {
                            if (ctx.getSource().getSender() instanceof Player player) {
                                Letters.giveAll(player);
                            }
                            return Command.SINGLE_SUCCESS;
                        })
                        .then(Commands.literal("make")
                                .executes(ctx -> {
                                    if (ctx.getSource().getSender() instanceof Player player) {
                                        Letters.makeCode(player);
                                    }
                                    return Command.SINGLE_SUCCESS;
                                })))
                .then(Commands.literal("biome")
                        .executes(ctx -> {
                            new BiomeCommand().reset(ctx.getSource().getSender());
                            return Command.SINGLE_SUCCESS;
                        })
                        .then(Commands.literal("name")
                                .executes(ctx -> {
                                    new BiomeCommand().getName(ctx.getSource().getSender());
                                    return Command.SINGLE_SUCCESS;
                                })))
                .then(Commands.literal("bleach")
                        .executes(ctx -> {
                            if (ctx.getSource().getSender() instanceof Player player) {
                                new BleachCommand(plugin).setDisplay(player);
                            }
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("blueprint")
                        .executes(ctx -> {
                            if (ctx.getSource().getSender() instanceof Player player) {
                                DevelopmentUtility.giveBlueprints(plugin, player);
                            }
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("box")
                        .then(Commands.argument("preset", new PresetArgumentType(0)))
                        .executes(ctx -> {
                            String preset = ctx.getArgument("preset", String.class);
                            new BoxCommand(plugin).setPreset(ctx.getSource().getSender(), preset, "");
                            return Command.SINGLE_SUCCESS;
                        })
                        .then(Commands.argument("state", StringArgumentType.word())
                                .suggests((ctx, builder) -> {
                                    builder.suggest("closed");
                                    builder.suggest("open");
                                    builder.suggest("stained");
                                    builder.suggest("glass");
                                    builder.suggest("fly");
                                    return builder.buildFuture();
                                })
                                .executes(ctx -> {
                                    String preset = ctx.getArgument("preset", String.class);
                                    String state = StringArgumentType.getString(ctx, "preset");
                                    new BoxCommand(plugin).setPreset(ctx.getSource().getSender(), preset, state);
                                    return Command.SINGLE_SUCCESS;
                                })))
                .then(Commands.literal("brushable")
                        .executes(ctx -> {
                            if (ctx.getSource().getSender() instanceof Player player) {
                                DevelopmentUtility.brush(player);
                            }
                            return Command.SINGLE_SUCCESS;
                        })
                        .then(Commands.literal("target")
                                .executes(ctx -> {
                                    if (ctx.getSource().getSender() instanceof Player player) {
                                        // get target block
                                        Block block = player.getTargetBlock(null, 8);
                                        player.sendMessage(block.getState().toString());
                                    }
                                    return Command.SINGLE_SUCCESS;
                                })))
                .then(Commands.literal("chain")
                        .executes(ctx -> {
                            new ChainCommand(plugin).checkSchematics();
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("chunks")
                        .executes(ctx -> {
                            new ChunksCommand(plugin).list(ctx.getSource().getSender());
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("chunky")
                        .then(Commands.argument("world", ArgumentTypes.world())
                                .executes(ctx -> {
                                    World world = ctx.getArgument("world", World.class);
                                    DevelopmentUtility.chunky(plugin, world.getName(), "250");
                                    return Command.SINGLE_SUCCESS;
                                })
                                .then(Commands.argument("radius", IntegerArgumentType.integer(1))
                                        .executes(ctx -> {
                                            World world = ctx.getArgument("world", World.class);
                                            String r = Integer.toString(IntegerArgumentType.getInteger(ctx, "radius"));
                                            DevelopmentUtility.chunky(plugin, world.getName(), r);
                                            return Command.SINGLE_SUCCESS;
                                        }))))
                .then(Commands.literal("circuit")
                        .executes(ctx -> {
                            new CircuitCommand(plugin).give(ctx.getSource().getSender());
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("component")
                        .then(Commands.literal("ars")
                                .executes(ctx -> {
                                    new ComponentCommand(plugin).writeARS();
                                    return Command.SINGLE_SUCCESS;
                                }))
                        .then(Commands.literal("repeater")
                                .executes(ctx -> {
                                    new ComponentCommand(plugin).writeRepeater();
                                    return Command.SINGLE_SUCCESS;
                                }))
                        .then(Commands.literal("key")
                                .executes(ctx -> {
                                    new ComponentCommand(plugin).writeKey();
                                    return Command.SINGLE_SUCCESS;
                                }))
                        .then(Commands.literal("pack")
                                .executes(ctx -> {
                                    new ResourcePackConverterCommand(plugin).process(ctx.getSource().getSender(), "");
                                    return Command.SINGLE_SUCCESS;
                                })
                                .then(Commands.literal("folder")
                                        .executes(ctx -> {
                                            new ResourcePackConverterCommand(plugin).process(ctx.getSource().getSender(), "folder");
                                            return Command.SINGLE_SUCCESS;
                                        })))
                        .then(Commands.literal("extra")
                                .executes(ctx -> {
                                    new ComponentCommand(plugin).writeExtra(ctx.getSource().getSender());
                                    return Command.SINGLE_SUCCESS;
                                })))
                .then(Commands.literal("dalek")
                        .executes(ctx -> {
                            if (ctx.getSource().getSender() instanceof Player player) {
                                DevelopmentUtility.dalek(plugin, player);
                            }
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("debug")
                        .then(Commands.argument("option", StringArgumentType.word())
                                .suggests((ctx, builder) -> {
                                    builder.suggest("enter");
                                    builder.suggest("exit");
                                    builder.suggest("create");
                                    builder.suggest("update");
                                    return builder.buildFuture();
                                })
                                .executes(ctx -> {
                                    String o = StringArgumentType.getString(ctx, "option");
                                    new DebugCommand(plugin).process(ctx.getSource().getSender(), o);
                                    return Command.SINGLE_SUCCESS;
                                })))
                .then(Commands.literal("dialog")
                        .executes(ctx -> {
                            if (ctx.getSource().getSender() instanceof Player player) {
                                Dialog dialog = new PreferencesDialog(plugin, player.getUniqueId()).create();
                                Audience.audience(player).showDialog(dialog);
                            }
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("dismount")
                        .executes(ctx -> {
                            if (ctx.getSource().getSender() instanceof Player player) {
                                if (player.getVehicle() != null) {
                                    player.getVehicle().eject();
                                }
                            }
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("display_item")
                        // "add", "shop", "animate", "remove", "place", "break", "convert", "chunk", "console", "door"
                        .then(Commands.literal("add")
                                .then(Commands.argument("item", ArgumentTypes.itemStack())
                                        .executes(ctx -> {
                                            if (ctx.getSource().getSender() instanceof Player player) {
                                                String item = ctx.getArgument("item", ItemStack.class).getType().toString();
                                                DisplayItemUtility.add(player, item, "");
                                            }
                                            return Command.SINGLE_SUCCESS;
                                        })
                                        .then(Commands.argument("transform", new TransformArgumentType())
                                                .executes(ctx -> {
                                                    if (ctx.getSource().getSender() instanceof Player player) {
                                                        String item = ctx.getArgument("item", ItemStack.class).getType().toString();
                                                        String transform = ctx.getArgument("transform", String.class);
                                                        DisplayItemUtility.add(player, item, transform);
                                                    }
                                                    return Command.SINGLE_SUCCESS;
                                                }))))
                        .then(Commands.literal("shop")
                                .then(Commands.argument("item", StringArgumentType.word())
                                        .then(Commands.argument("label", BoolArgumentType.bool())
                                                .executes(ctx -> {
                                                    if (ctx.getSource().getSender() instanceof Player player) {
                                                        String s = StringArgumentType.getString(ctx, "item");
                                                        boolean b = BoolArgumentType.getBool(ctx, "label");
                                                        DisplayItemUtility.shop(player, s, b);
                                                    }
                                                    return Command.SINGLE_SUCCESS;
                                                }))))
                        .then(Commands.literal("animate")
                                .executes(ctx -> {
                                    if (ctx.getSource().getSender() instanceof Player player) {
                                        DisplayItemUtility.animate(plugin, player, false);
                                    }
                                    return Command.SINGLE_SUCCESS;
                                })
                                .then(Commands.argument("bee", BoolArgumentType.bool())
                                        .executes(ctx -> {
                                            if (ctx.getSource().getSender() instanceof Player player) {
                                                boolean b = BoolArgumentType.getBool(ctx, "bee");
                                                DisplayItemUtility.animate(plugin, player, b);
                                            }
                                            return Command.SINGLE_SUCCESS;
                                        })))
                        .then(Commands.literal("place")
                                .then(Commands.argument("tdi", new DisplayItemArgumentType())
                                        .executes(ctx -> {
                                            if (ctx.getSource().getSender() instanceof Player player) {
                                                String tdi = ctx.getArgument("tdi", String.class);
                                                DisplayItemUtility.place(plugin, player, tdi);
                                            }
                                            return Command.SINGLE_SUCCESS;
                                        })))
                        .then(Commands.literal("remove")
                                .executes(ctx -> {
                                    if (ctx.getSource().getSender() instanceof Player player) {
                                        DisplayItemUtility.remove(player);
                                    }
                                    return Command.SINGLE_SUCCESS;
                                }))
                        .then(Commands.literal("break")
                                .executes(ctx -> {
                                    if (ctx.getSource().getSender() instanceof Player player) {
                                        DisplayItemUtility.breakDisplay(player);
                                    }
                                    return Command.SINGLE_SUCCESS;
                                }))
                        .then(Commands.literal("convert")
                                .executes(ctx -> {
                                    if (ctx.getSource().getSender() instanceof Player player) {
                                        new UpdateBlocksCommand(plugin).convert(player);
                                    }
                                    return Command.SINGLE_SUCCESS;
                                }))
                        .then(Commands.literal("chunk")
                                .executes(ctx -> {
                                    if (ctx.getSource().getSender() instanceof Player player) {
                                        Chunk chunk = player.getLocation().getChunk();
                                        for (Entity entity : chunk.getEntities()) {
                                            if (entity instanceof ItemDisplay || entity instanceof Interaction || entity instanceof TextDisplay) {
                                                entity.remove();
                                            }
                                        }
                                    }
                                    return Command.SINGLE_SUCCESS;
                                }))
                        .then(Commands.literal("console")
                                .then(Commands.argument("colour", new ConsoleColourArgumentType())
                                        .executes(ctx -> {
                                            if (ctx.getSource().getSender() instanceof Player player) {
                                                String c = ctx.getArgument("colour", String.class);
                                                DisplayItemUtility.console(plugin, player, c);
                                            }
                                            return Command.SINGLE_SUCCESS;
                                        })))
                        .then(Commands.literal("door")
                                .executes(ctx -> {
                                    if (ctx.getSource().getSender() instanceof Player player) {
                                        DisplayItemUtility.door(plugin, player);
                                    }
                                    return Command.SINGLE_SUCCESS;
                                })))
                .then(Commands.literal("effect")
                        .executes(ctx -> {
                            if (ctx.getSource().getSender() instanceof Player player) {
                                new EffectCommand(plugin).show(player, "", "", 0, 0, "", "");
                            }
                            return Command.SINGLE_SUCCESS;
                        })
                        .then(Commands.literal("sphere")
                                .then(Commands.argument("size", new CapacitorArgumentType())
                                        .executes(ctx -> {
                                            if (ctx.getSource().getSender() instanceof Player player) {
                                                String s = ctx.getArgument("shape", String.class);
                                                String e = ctx.getArgument("size", String.class);
                                                new EffectCommand(plugin).show(player, s, e, 0, 0, "", "");
                                            }
                                            return Command.SINGLE_SUCCESS;
                                        })))
                        .then(Commands.argument("shape", new ParticleShapeArgumentType())
                                .then(Commands.argument("effect", new ParticleEffectArgumentType())
                                        .executes(ctx -> {
                                            if (ctx.getSource().getSender() instanceof Player player) {
                                                String s = ctx.getArgument("shape", String.class);
                                                String e = ctx.getArgument("effect", String.class);
                                                new EffectCommand(plugin).show(player, s, e, 0, 0, "", "");
                                            }
                                            return Command.SINGLE_SUCCESS;
                                        })
                                        .then(Commands.argument("density", IntegerArgumentType.integer(1))
                                                .executes(ctx -> {
                                                    if (ctx.getSource().getSender() instanceof Player player) {
                                                        String s = ctx.getArgument("shape", String.class);
                                                        String e = ctx.getArgument("effect", String.class);
                                                        int d = IntegerArgumentType.getInteger(ctx, "density");
                                                        new EffectCommand(plugin).show(player, s, e, d, 0, "", "");
                                                    }
                                                    return Command.SINGLE_SUCCESS;
                                                })
                                                .then(Commands.argument("speed", DoubleArgumentType.doubleArg(0))
                                                        .executes(ctx -> {
                                                            if (ctx.getSource().getSender() instanceof Player player) {
                                                                String s = ctx.getArgument("shape", String.class);
                                                                String e = ctx.getArgument("effect", String.class);
                                                                int d = IntegerArgumentType.getInteger(ctx, "density");
                                                                double v = DoubleArgumentType.getDouble(ctx, "speed");
                                                                new EffectCommand(plugin).show(player, s, e, d, v, "", "");
                                                            }
                                                            return Command.SINGLE_SUCCESS;
                                                        })
                                                        .then(Commands.argument("colour", StringArgumentType.word())
                                                                .executes(ctx -> {
                                                                    if (ctx.getSource().getSender() instanceof Player player) {
                                                                        String s = ctx.getArgument("shape", String.class);
                                                                        String e = ctx.getArgument("effect", String.class);
                                                                        int d = IntegerArgumentType.getInteger(ctx, "density");
                                                                        double v = DoubleArgumentType.getDouble(ctx, "speed");
                                                                        String c = StringArgumentType.getString(ctx, "colour");
                                                                        new EffectCommand(plugin).show(player, s, e, d, v, c, "");
                                                                    }
                                                                    return Command.SINGLE_SUCCESS;
                                                                })
                                                                .then(Commands.argument("block", StringArgumentType.word())
                                                                        .suggests(BlockSuggestions::get)
                                                                        .executes(ctx -> {
                                                                            if (ctx.getSource().getSender() instanceof Player player) {
                                                                                String s = ctx.getArgument("shape", String.class);
                                                                                String e = ctx.getArgument("effect", String.class);
                                                                                int d = IntegerArgumentType.getInteger(ctx, "density");
                                                                                double v = DoubleArgumentType.getDouble(ctx, "speed");
                                                                                String c = StringArgumentType.getString(ctx, "colour");
                                                                                String b = StringArgumentType.getString(ctx, "block");
                                                                                new EffectCommand(plugin).show(player, s, e, d, v, c, b);
                                                                            }
                                                                            return Command.SINGLE_SUCCESS;
                                                                        }))))))))
                .then(Commands.literal("empty")
                        .executes(ctx -> {
                            new FixStorageCommand(plugin).convertStacks();
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("frame")
                        .then(Commands.argument("action", StringArgumentType.word())
                                .suggests((ctx, builder) -> {
                                    builder.suggest("lock");
                                    builder.suggest("unlock");
                                    return builder.buildFuture();
                                })
                                .executes(ctx -> {
                                    if (ctx.getSource().getSender() instanceof Player player) {
                                        String a = StringArgumentType.getString(ctx, "action");
                                        new FrameCommand(plugin).toggle(player, a.equalsIgnoreCase("lock"), false);
                                    } else {
                                        plugin.getMessenger().send(ctx.getSource().getSender(), TardisModule.TARDIS, "CMD_PLAYER");
                                    }
                                    return Command.SINGLE_SUCCESS;
                                })
                                .then(Commands.literal("rotor").executes(ctx -> {
                                    if (ctx.getSource().getSender() instanceof Player player) {
                                        String a = StringArgumentType.getString(ctx, "action");
                                        new FrameCommand(plugin).toggle(player, a.equalsIgnoreCase("lock"), true);
                                    } else {
                                        plugin.getMessenger().send(ctx.getSource().getSender(), TardisModule.TARDIS, "CMD_PLAYER");
                                    }
                                    return Command.SINGLE_SUCCESS;
                                }))))
                .then(Commands.literal("furnace")
                        .executes(ctx -> {
                            new FurnaceCommand(plugin).list();
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("gravity")
                        .executes(ctx -> {
                            if (ctx.getSource().getSender() instanceof Player player) {
                                player.setGravity(!player.hasGravity());
                            }
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("give")
                        .executes(ctx -> {
                            if (ctx.getSource().getSender() instanceof Player player) {
                                new StorageContents(plugin).give(player);
                            }
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("happy")
                        .executes(ctx -> {
                            if (ctx.getSource().getSender() instanceof Player player) {
                                new HappyCommand().leash(player);
                            }
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("head")
                        .executes(ctx -> {
                            if (ctx.getSource().getSender() instanceof Player player) {
                                new HeadCommand(plugin).giveAPIHead(player);
                            }
                            return Command.SINGLE_SUCCESS;
                        })
                        .then(Commands.literal("props")
                                .executes(ctx -> {
                                    if (ctx.getSource().getSender() instanceof Player player) {
                                        new HeadCommand(plugin).getHeadProperties(player);
                                    }
                                    return Command.SINGLE_SUCCESS;
                                })))
                .then(Commands.literal("interaction")
                        .executes(ctx -> {
                            if (ctx.getSource().getSender() instanceof Player player) {
                                new InteractionCommand(plugin).process(player.getUniqueId());
                            }
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("label")
                        .executes(ctx -> {
                            new LabelCommand(plugin).catalog(ctx.getSource().getSender());
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("leather")
                        .executes(ctx -> {
                            if (ctx.getSource().getSender() instanceof Player player) {
                                DevelopmentUtility.leather(player);
                            }
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("list")
                        .then(Commands.literal("perms")
                                .executes(ctx -> {
                                    new PermissionLister(plugin).listPerms(ctx.getSource().getSender());
                                    return Command.SINGLE_SUCCESS;
                                })
                                .then(Commands.literal("html").executes(ctx -> {
                                    new PermissionLister(plugin).listPermsHtml(ctx.getSource().getSender());
                                    return Command.SINGLE_SUCCESS;
                                })))
                        .then(Commands.literal("commands")
                                .executes(ctx -> {
                                    new CommandsLister(plugin).listTARDISCommands(ctx.getSource().getSender());
                                    return Command.SINGLE_SUCCESS;
                                })
                                .then(Commands.literal("other").executes(ctx -> {
                                    new CommandsLister(plugin).listOtherTARDISCommands(ctx.getSource().getSender());
                                    return Command.SINGLE_SUCCESS;
                                })))
                        .then(Commands.literal("consoles")
                                .executes(ctx -> {
                                    new SchematicLister(plugin).list();
                                    return Command.SINGLE_SUCCESS;
                                })
                                .then(Commands.literal("cost").executes(ctx -> {
                                    new ConsoleCostLister(plugin).actualArtron();
                                    return Command.SINGLE_SUCCESS;
                                })))
                        .then(Commands.literal("recipes")
                                .executes(ctx -> {
                                    new RecipesLister(plugin).listRecipes(ctx.getSource().getSender(), "");
                                    return Command.SINGLE_SUCCESS;
                                })
                                .then(Commands.argument("category", new RecipeCategoryArgumentType())
                                        .executes(ctx -> {
                                            String c = ctx.getArgument("category", String.class);
                                            new RecipesLister(plugin).listRecipes(ctx.getSource().getSender(), c);
                                            return Command.SINGLE_SUCCESS;
                                        })))
                        .then(Commands.argument("what", new DevListArgumentType())
                                .executes(ctx -> {
                                    String w = ctx.getArgument("what", String.class);
                                    new ListCommand(plugin).listStuff(ctx.getSource().getSender(), w, "");
                                    return Command.SINGLE_SUCCESS;
                                })))
                .then(Commands.literal("mannequin")
                        .then(Commands.argument("type", new MannequinEquipArgumentType())
                                .executes(ctx -> {
                                    if (ctx.getSource().getSender() instanceof Player player) {
                                        String t = ctx.getArgument("type", String.class);
                                        new MannequinCommand().equip(player, t);
                                    }
                                    return Command.SINGLE_SUCCESS;
                                })))
                .then(Commands.literal("monster")
                        .executes(ctx -> {
                            if (ctx.getSource().getSender() instanceof Player player) {
                                DevelopmentUtility.listPortals(plugin, player);
                            }
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("mount")
                        .executes(ctx -> {
                            if (ctx.getSource().getSender() instanceof Player player) {
                                new MountCommand(plugin).test(player, true);
                            }
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("ntc")
                        .executes(ctx -> {
                            TARDISStaticUtils.getColor(Component.text("test", NamedTextColor.AQUA));
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("nms")
                        .then(Commands.argument("monster", new MonsterArgumentType())
                                .executes(ctx -> {
                                    String m = ctx.getArgument("monster", String.class);
                                    new NMSCommand(plugin).spawn(ctx.getSource().getSender(), m, false);
                                    return Command.SINGLE_SUCCESS;
                                })
                                .then(Commands.literal("unclaimed")
                                        .executes(ctx -> {
                                            String m = ctx.getArgument("monster", String.class);
                                            new NMSCommand(plugin).spawn(ctx.getSource().getSender(), m, true);
                                            return Command.SINGLE_SUCCESS;
                                        }))))
                .then(Commands.literal("painting")
                        .executes(ctx -> {
                            if (ctx.getSource().getSender() instanceof Player player) {
                                new PaintingCommand(plugin).getLocation(player);
                            }
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("plurals")
                        .executes(ctx -> {
                            for (Material m : Material.values()) {
                                String str = m.toString().toLowerCase(Locale.ROOT).replace("_", " ");
                                plugin.getMessenger().message(plugin.getConsole(), TardisModule.TARDIS, str + " --> " + Pluraliser.pluralise(str));
                            }
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("pong")
                        .executes(ctx -> {
                            if (ctx.getSource().getSender() instanceof Player player) {
                                Block targetBlock = player.getTargetBlock(plugin.getGeneralKeeper().getTransparent(), 16);
                                String uuids = GameDisplay.create(targetBlock.getLocation().add(0.5d, 2d, 0.5d));
                                plugin.debug(uuids);
                            }
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("recipe")
                        .then(Commands.argument("which", new WikiRecipeArgumentType())
                                .executes(ctx -> {
                                    String w = ctx.getArgument("which", String.class);
                                    new WikiRecipeCommand(plugin).write(ctx.getSource().getSender(), w);
                                    return Command.SINGLE_SUCCESS;
                                })))
                .then(Commands.literal("regen")
                        .executes(ctx -> {
                            if (ctx.getSource().getSender() instanceof Player player) {
                                new Regenerator().dev(plugin, player, -1);
                            }
                            return Command.SINGLE_SUCCESS;
                        })
                        .then(Commands.argument("index", IntegerArgumentType.integer(0))
                                .executes(ctx -> {
                                    if (ctx.getSource().getSender() instanceof Player player) {
                                        int i = IntegerArgumentType.getInteger(ctx, "index");
                                        new Regenerator().dev(plugin, player, i);
                                    }
                                    return Command.SINGLE_SUCCESS;
                                })))
                .then(Commands.literal("registry")
                        .executes(ctx -> {
                            DevelopmentUtility.listPaintings(plugin);
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("roman")
                        .then(Commands.argument("type", new MannequinEquipArgumentType())
                                .executes(ctx -> {
                                    if (ctx.getSource().getSender() instanceof Player player) {
                                        String t = ctx.getArgument("type", String.class);
                                        new RomanCommand().equip(player, t);
                                    }
                                    return Command.SINGLE_SUCCESS;
                                })))
                .then(Commands.literal("rooms")
                        .then(Commands.argument("rooms", StringArgumentType.greedyString())
                                .executes(ctx -> {
                                    String[] rooms = StringArgumentType.getString(ctx, "rooms").split(" ");
                                    new RoomsCommand(plugin).build(ctx.getSource().getSender(), rooms);
                                    return Command.SINGLE_SUCCESS;
                                })))
                .then(Commands.literal("screen")
                        .then(Commands.argument("direction", new ScreenMoveArgumentType())
                                .executes(ctx -> {
                                    if (ctx.getSource().getSender() instanceof Player player) {
                                        String d = ctx.getArgument("direction", String.class);
                                        new ConsoleTextCommand(plugin).move(player, d);
                                    }
                                    return Command.SINGLE_SUCCESS;
                                })))
                .then(Commands.literal("shelf")
                        .executes(ctx -> {
                            if (ctx.getSource().getSender() instanceof Player player) {
                                new ShelfCommand(plugin).putItems(player);
                            }
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("siege")
                        .executes(ctx -> {
                            if (ctx.getSource().getSender() instanceof Player player) {
                                DevelopmentUtility.siege(plugin, player);
                            }
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("skin")
                        .executes(ctx -> {
                            if (ctx.getSource().getSender() instanceof Player player) {
                                plugin.getSkinChanger().remove(player);
                            }
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("snapshot")
                        .then(Commands.literal("clear")
                                .executes(ctx -> {
                                    if (ctx.getSource().getSender() instanceof Player player) {
                                        player.performCommand("minecraft:clear @s minecraft:filled_map");
                                    }
                                    return Command.SINGLE_SUCCESS;
                                }))
                        .then(Commands.literal("in")
                                .executes(ctx -> {
                                    if (ctx.getSource().getSender() instanceof Player player) {
                                        new MonitorSnapshot(plugin).get(true, player);
                                    }
                                    return Command.SINGLE_SUCCESS;
                                }))
                        .then(Commands.literal("out")
                                .executes(ctx -> {
                                    if (ctx.getSource().getSender() instanceof Player player) {
                                        new MonitorSnapshot(plugin).get(false, player);
                                    }
                                    return Command.SINGLE_SUCCESS;
                                })))
                .then(Commands.literal("staircase")
                        .executes(ctx -> {
                            if (ctx.getSource().getSender() instanceof Player player) {
                                new StaircaseCommand().spiral(player);
                            }
                            return Command.SINGLE_SUCCESS;
                        })
                        .then(Commands.literal("mark")
                                .executes(ctx -> {
                                    if (ctx.getSource().getSender() instanceof Player player) {
                                        new StaircaseCommand().mark(player);
                                    }
                                    return Command.SINGLE_SUCCESS;
                                }))
                        .then(Commands.literal("scan")
                                .executes(ctx -> {
                                    if (ctx.getSource().getSender() instanceof Player player) {
                                        new StaircaseCommand().scan(player);
                                    }
                                    return Command.SINGLE_SUCCESS;
                                }))
                        .then(Commands.literal("smaller")
                                .executes(ctx -> {
                                    if (ctx.getSource().getSender() instanceof Player player) {
                                        new StaircaseCommand().smaller(player);
                                    }
                                    return Command.SINGLE_SUCCESS;
                                })))
                .then(Commands.literal("stats")
                        .executes(ctx -> {
                            DevelopmentUtility.listStats(plugin);
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("system_tree")
                        .executes(ctx -> {
                            if (ctx.getSource().getSender() instanceof Player player) {
                                new SystemTreeCommand(plugin).open(player);
                            }
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("text")
                        .executes(ctx -> {
                            if (ctx.getSource().getSender() instanceof Player player) {
                                DevelopmentUtility.pong(plugin, player);
                            }
                            return Command.SINGLE_SUCCESS;
                        })
                        .then(Commands.literal("tp")
                                .executes(ctx -> {
                                    if (ctx.getSource().getSender() instanceof Player player) {
                                        DevelopmentUtility.pingPong(plugin, player);
                                    }
                                    return Command.SINGLE_SUCCESS;
                                })
                                .then(Commands.argument("amount", DoubleArgumentType.doubleArg())
                                        .executes(ctx -> {
                                            if (ctx.getSource().getSender() instanceof Player player) {
                                                double d = DoubleArgumentType.getDouble(ctx, "amount");
                                                DevelopmentUtility.movePongDisplay(plugin, player, d);
                                            }
                                            return Command.SINGLE_SUCCESS;
                                        }))))
                .then(Commands.literal("tis")
                        .executes(ctx -> {
                            new InfoCommand(plugin).test(ctx.getSource().getSender());
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("tips")
                        .executes(ctx -> {
                            new TIPSPreviewSlotInfo(plugin).display();
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("tree")
                        .executes(ctx -> {
                            if (ctx.getSource().getSender() instanceof Player) {
                                new TreeCommand(plugin).grow(ctx.getSource().getSender(), "", "", "", "");
                            }
                            return Command.SINGLE_SUCCESS;
                        })
                        .then(Commands.literal("delta")
                                .then(Commands.argument("which", IntegerArgumentType.integer(0))
                                        .executes(ctx -> {
                                            if (ctx.getSource().getSender() instanceof Player) {
                                                String s = Integer.toString(IntegerArgumentType.getInteger(ctx, "which"));
                                                new TreeCommand(plugin).grow(ctx.getSource().getSender(), "delta", s, "", "");
                                            }
                                            return Command.SINGLE_SUCCESS;
                                        })))
                        .then(Commands.literal("worldgen")
                                .executes(ctx -> {
                                    if (ctx.getSource().getSender() instanceof Player) {
                                        new TreeCommand(plugin).grow(ctx.getSource().getSender(), "worldgen", "", "", "");
                                    }
                                    return Command.SINGLE_SUCCESS;
                                }))
                        .then(Commands.literal("with")
                                .then(Commands.argument("stem", new TreeBlockArgumentType())
                                        .then(Commands.argument("hat", new TreeBlockArgumentType())
                                                .then(Commands.argument("decor", new TreeBlockArgumentType())
                                                        .executes(ctx -> {
                                                            String s = ctx.getArgument("stem", String.class);
                                                            String h = ctx.getArgument("hat", String.class);
                                                            String d = ctx.getArgument("decor", String.class);
                                                            new TreeCommand(plugin).grow(ctx.getSource().getSender(), "with", s, h, d);
                                                            return Command.SINGLE_SUCCESS;
                                                        }))))))
                .then(Commands.literal("unmount")
                        .executes(ctx -> {
                            if (ctx.getSource().getSender() instanceof Player player) {
                                new MountCommand(plugin).test(player, false);
                            }
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("sprite")
                        .then(Commands.argument("minecraft", BoolArgumentType.bool())
                                .executes(ctx -> {
                                    if (ctx.getSource().getSender() instanceof Player player) {
                                        boolean b = BoolArgumentType.getBool(ctx, "minecraft");
                                        new SpriteCommand().send(player, b);
                                    }
                                    return Command.SINGLE_SUCCESS;
                                })))
                .then(Commands.literal("update")
                        .executes(ctx -> {
                            if (ctx.getSource().getSender() instanceof Player player) {
                                new UpdateBlockStateCommand(plugin).refresh(player);
                            }
                            return Command.SINGLE_SUCCESS;
                        }));
        return command.build();
    }
}
