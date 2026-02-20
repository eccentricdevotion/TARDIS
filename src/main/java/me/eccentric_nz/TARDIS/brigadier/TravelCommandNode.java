package me.eccentric_nz.TARDIS.brigadier;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.mojang.datafixers.util.Pair;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.RegistryArgumentExtractor;
import io.papermc.paper.command.brigadier.argument.resolvers.BlockPositionResolver;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;
import io.papermc.paper.math.BlockPosition;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.TypedKey;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.brigadier.arguments.AreasArgumentType;
import me.eccentric_nz.TARDIS.commands.TARDISCommandHelper;
import me.eccentric_nz.TARDIS.commands.travel.*;
import me.eccentric_nz.TARDIS.enumeration.ChameleonPreset;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.generator.structure.Structure;

public class TravelCommandNode {

    private final TARDIS plugin;

    public TravelCommandNode(TARDIS plugin) {
        this.plugin = plugin;
    }

    LiteralCommandNode<CommandSourceStack> build() {
        LiteralArgumentBuilder<CommandSourceStack> command = Commands.literal("tardiscall")
                .requires(ctx -> ctx.getExecutor() instanceof Player player && player.hasPermission("tardis.travel"))
                .executes(ctx -> {
                    new TARDISCommandHelper(plugin).getCommand("tardistravel", ctx.getSource().getSender());
                    return Command.SINGLE_SUCCESS;
                })
                .then(Commands.literal("home")
                        .executes(ctx -> {
                            Player player = (Player) ctx.getSource().getExecutor();
                            int id = TravelUtilities.getId(plugin, player);
                            if (id > 0) {
                                new HomeCommand(plugin).action(player, id);
                            }
                            return Command.SINGLE_SUCCESS;
                        })
                        .then(Commands.argument("communicator", StringArgumentType.word())
                                .executes(ctx -> {
                                    if (StringArgumentType.getString(ctx, "communicator").equals("kzsbtr1h2")) {
                                        Player player = (Player) ctx.getSource().getExecutor();
                                        int id = TravelUtilities.getId(plugin, player);
                                        if (id > 0) {
                                            new HomeCommand(plugin).action(player, id);
                                        }
                                    }
                                    return Command.SINGLE_SUCCESS;
                                })))
                .then(Commands.literal("biome")
                        .then(Commands.literal("list").executes(ctx -> {
                            Player player = (Player) ctx.getSource().getExecutor();
                            TravelUtilities.listBiomes(plugin, player);
                            return Command.SINGLE_SUCCESS;
                        }))
                        .then(Commands.argument("biome", ArgumentTypes.resourceKey(RegistryKey.BIOME))
                                .executes(ctx -> {
                                    Player player = (Player) ctx.getSource().getExecutor();
                                    int id = TravelUtilities.getId(plugin, player);
                                    if (id > 0) {
                                        TypedKey<Biome> key = RegistryArgumentExtractor.getTypedKey(ctx, RegistryKey.BIOME, "biome");
                                        Biome biome = RegistryAccess.registryAccess().getRegistry(key.registryKey()).get(key.key());
                                        new BiomeCommand(plugin).action(player, biome, null, id);
                                    }
                                    return Command.SINGLE_SUCCESS;
                                })
                                .then(Commands.argument("communicator", StringArgumentType.word())
                                        .executes(ctx -> {
                                            if (StringArgumentType.getString(ctx, "communicator").equals("kzsbtr1h2")) {
                                                Player player = (Player) ctx.getSource().getExecutor();
                                                int id = TravelUtilities.getId(plugin, player);
                                                if (id > 0) {
                                                    TypedKey<Biome> key = RegistryArgumentExtractor.getTypedKey(ctx, RegistryKey.BIOME, "biome");
                                                    Biome biome = RegistryAccess.registryAccess().getRegistry(key.registryKey()).get(key.key());
                                                    new BiomeCommand(plugin).action(player, biome, null, id);
                                                }
                                            }
                                            return Command.SINGLE_SUCCESS;
                                        }))
                                .then(Commands.argument("world", ArgumentTypes.world())
                                        .executes(ctx -> {
                                            Player player = (Player) ctx.getSource().getExecutor();
                                            int id = TravelUtilities.getId(plugin, player);
                                            if (id > 0) {
                                                TypedKey<Biome> key = RegistryArgumentExtractor.getTypedKey(ctx, RegistryKey.BIOME, "biome");
                                                Biome biome = RegistryAccess.registryAccess().getRegistry(key.registryKey()).get(key.key());
                                                World world = ctx.getArgument("world", World.class);
                                                new BiomeCommand(plugin).action(player, biome, world, id);
                                            }
                                            return Command.SINGLE_SUCCESS;
                                        }))))
                .then(Commands.literal("save")
                        .then(Commands.argument("name", StringArgumentType.string())
                                .executes(ctx -> {
                                    Player player = (Player) ctx.getSource().getExecutor();
                                    Pair<Integer, ChameleonPreset> data = TravelUtilities.getIdAndPreset(plugin, player);
                                    if (data.getFirst() > 0) {
                                        String n = ctx.getArgument("name", String.class);
                                        new SaveCommand(plugin).action(player, n, data.getFirst(), data.getSecond());
                                    }
                                    return Command.SINGLE_SUCCESS;
                                })))
                .then(Commands.literal("dest")
                        .then(Commands.argument("name", StringArgumentType.string())
                                .executes(ctx -> {
                                    Player player = (Player) ctx.getSource().getExecutor();
                                    Pair<Integer, ChameleonPreset> data = TravelUtilities.getIdAndPreset(plugin, player);
                                    if (data.getFirst() > 0) {
                                        String n = ctx.getArgument("name", String.class);
                                        new SaveCommand(plugin).action(player, n, data.getFirst(), data.getSecond());
                                    }
                                    return Command.SINGLE_SUCCESS;
                                })
                                .then(Commands.argument("communicator", StringArgumentType.word())
                                        .executes(ctx -> {
                                            if (StringArgumentType.getString(ctx, "communicator").equals("kzsbtr1h2")) {
                                                Player player = (Player) ctx.getSource().getExecutor();
                                                Pair<Integer, ChameleonPreset> data = TravelUtilities.getIdAndPreset(plugin, player);
                                                if (data.getFirst() > 0) {
                                                    String n = ctx.getArgument("name", String.class);
                                                    new SaveCommand(plugin).action(player, n, data.getFirst(), data.getSecond());
                                                }
                                            }
                                            return Command.SINGLE_SUCCESS;
                                        }))))
                .then(Commands.literal("area")
                        .then(Commands.argument("name", new AreasArgumentType())
                                .executes(ctx -> {
                                    Player player = (Player) ctx.getSource().getExecutor();
                                    Pair<Integer, ChameleonPreset> data = TravelUtilities.getIdAndPreset(plugin, player);
                                    if (data.getFirst() > 0) {
                                        String n = ctx.getArgument("name", String.class);
                                        new AreaCommand(plugin).action(player, n, data.getFirst(), data.getSecond());
                                    }
                                    return Command.SINGLE_SUCCESS;
                                })))
                .then(Commands.literal("back")
                        .executes(ctx -> {
                            Player player = (Player) ctx.getSource().getExecutor();
                            int id = TravelUtilities.getId(plugin, player);
                            if (id > 0) {
                                new BackCommand(plugin).action(player, id);
                            }
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("player")
                        .then(Commands.argument("target", ArgumentTypes.player())
                                .executes(ctx -> {
                                    Player player = (Player) ctx.getSource().getExecutor();
                                    int id = TravelUtilities.getId(plugin, player);
                                    if (id > 0) {
                                        PlayerSelectorArgumentResolver targetResolver = ctx.getArgument("target", PlayerSelectorArgumentResolver.class);
                                        Player target = targetResolver.resolve(ctx.getSource()).getFirst();
                                        new PlayerCommand(plugin).action(player, target, id);
                                    }
                                    return Command.SINGLE_SUCCESS;
                                })
                                .then(Commands.argument("communicator", StringArgumentType.word())
                                        .executes(ctx -> {
                                            if (StringArgumentType.getString(ctx, "communicator").equals("kzsbtr1h2")) {
                                                Player player = (Player) ctx.getSource().getExecutor();
                                                int id = TravelUtilities.getId(plugin, player);
                                                if (id > 0) {
                                                    PlayerSelectorArgumentResolver targetResolver = ctx.getArgument("target", PlayerSelectorArgumentResolver.class);
                                                    Player target = targetResolver.resolve(ctx.getSource()).getFirst();
                                                    new PlayerCommand(plugin).action(player, target, id);
                                                }
                                            }
                                            return Command.SINGLE_SUCCESS;
                                        }))))
                .then(Commands.literal("cave")
                        .executes(ctx -> {
                            Player player = (Player) ctx.getSource().getExecutor();
                            int id = TravelUtilities.getId(plugin, player);
                            if (id > 0) {
                                new CaveCommand(plugin).action(player, id);
                            }
                            return Command.SINGLE_SUCCESS;
                        })
                        .then(Commands.argument("communicator", StringArgumentType.word())
                                .executes(ctx -> {
                                    if (StringArgumentType.getString(ctx, "communicator").equals("kzsbtr1h2")) {
                                        Player player = (Player) ctx.getSource().getExecutor();
                                        int id = TravelUtilities.getId(plugin, player);
                                        if (id > 0) {
                                            new CaveCommand(plugin).action(player, id);
                                        }
                                    }
                                    return Command.SINGLE_SUCCESS;
                                })))
                .then(Commands.literal("village")
                        .executes(ctx -> {
                            Player player = (Player) ctx.getSource().getExecutor();
                            int id = TravelUtilities.getId(plugin, player);
                            if (id > 0) {
                                // get random village in current world
                                StructureUtilities.randomVillage(plugin, player, id);
                            }
                            return Command.SINGLE_SUCCESS;
                        })
                        .then(Commands.argument("type", ArgumentTypes.resourceKey(RegistryKey.STRUCTURE))
                                .executes(ctx -> {
                                    Player player = (Player) ctx.getSource().getExecutor();
                                    int id = TravelUtilities.getId(plugin, player);
                                    if (id > 0) {
                                        Structure type = ctx.getArgument("type", Structure.class);
                                        StructureUtilities.search(plugin, player, type, id);
                                    }
                                    return Command.SINGLE_SUCCESS;
                                }))
                        .then(Commands.argument("communicator", StringArgumentType.word())
                                .executes(ctx -> {
                                    if (StringArgumentType.getString(ctx, "communicator").equals("kzsbtr1h2")) {
                                        Player player = (Player) ctx.getSource().getExecutor();
                                        int id = TravelUtilities.getId(plugin, player);
                                        if (id > 0) {
                                            new TARDISTravelGUI(plugin).open(player, id, "village");
                                        }
                                    }
                                    return Command.SINGLE_SUCCESS;
                                })))
                .then(Commands.literal("structure")
                        .executes(ctx -> {
                            Player player = (Player) ctx.getSource().getExecutor();
                            int id = TravelUtilities.getId(plugin, player);
                            if (id > 0) {
                                // get random structure in current world
                                StructureUtilities.randomStructure(plugin, player, id);
                            }
                            return Command.SINGLE_SUCCESS;
                        })
                        .then(Commands.argument("type", ArgumentTypes.resourceKey(RegistryKey.STRUCTURE))
                                .executes(ctx -> {
                                    Player player = (Player) ctx.getSource().getExecutor();
                                    int id = TravelUtilities.getId(plugin, player);
                                    if (id > 0) {
                                        Structure type = ctx.getArgument("type", Structure.class);
                                        StructureUtilities.search(plugin, player, type, id);
                                    }
                                    return Command.SINGLE_SUCCESS;
                                })))
                .then(Commands.literal("cancel")
                        .executes(ctx -> {
                            Player player = (Player) ctx.getSource().getExecutor();
                            int id = TravelUtilities.getId(plugin, player);
                            if (id > 0) {
                                new CancelCommand(plugin).action(player, id);
                            }
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("costs")
                        .executes(ctx -> {
                            Player player = (Player) ctx.getSource().getExecutor();
                            int id = TravelUtilities.getId(plugin, player);
                            if (id > 0) {
                                new StopCommand(plugin).action(player, id);
                            }
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("stop")
                        .executes(ctx -> {
                            Player player = (Player) ctx.getSource().getExecutor();
                            int id = TravelUtilities.getId(plugin, player);
                            if (id > 0) {
                                new StopCommand(plugin).action(player, id);
                            }
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("random")
                        .then(Commands.argument("world", ArgumentTypes.world())
                                .executes(ctx -> {
                                    Player player = (Player) ctx.getSource().getExecutor();
                                    int id = TravelUtilities.getId(plugin, player);
                                    if (id > 0) {
                                        World world = ctx.getArgument("world", World.class);
                                        TravelUtilities.random(plugin, player, world, id);
                                    }
                                    return Command.SINGLE_SUCCESS;
                                })))
                .then(Commands.argument("world", ArgumentTypes.world())
                        .then(Commands.argument("coords", ArgumentTypes.blockPosition())
                                .executes(ctx -> {
                                    Player player = (Player) ctx.getSource().getExecutor();
                                    int id = TravelUtilities.getId(plugin, player);
                                    if (id > 0) {
                                        World world = ctx.getArgument("world", World.class);
                                        BlockPositionResolver resolver = ctx.getArgument("coords", BlockPositionResolver.class);
                                        BlockPosition pos = resolver.resolve(ctx.getSource());
                                        TravelUtilities.coords(plugin, player, world, pos, id);
                                    }
                                    return Command.SINGLE_SUCCESS;
                                })))
                .then(Commands.argument("coords", ArgumentTypes.blockPosition())
                        .executes(ctx -> {
                            Player player = (Player) ctx.getSource().getExecutor();
                            int id = TravelUtilities.getId(plugin, player);
                            if (id > 0) {
                                BlockPositionResolver resolver = ctx.getArgument("coords", BlockPositionResolver.class);
                                BlockPosition pos = resolver.resolve(ctx.getSource());
                                TravelUtilities.coords(plugin, player, null, pos, id);
                            }
                            return Command.SINGLE_SUCCESS;
                        }));
        return command.build();
    }
}
