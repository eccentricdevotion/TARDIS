package me.eccentric_nz.TARDIS.brigadier;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.brigadier.arguments.DirectoryArgumentType;
import me.eccentric_nz.TARDIS.brigadier.suggestions.BlockSuggestions;
import me.eccentric_nz.TARDIS.brigadier.suggestions.LightSuggestions;
import me.eccentric_nz.TARDIS.brigadier.suggestions.SchematicLoadSuggestions;
import me.eccentric_nz.TARDIS.schematic.actions.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class SchematicCommandNode {

    private final TARDIS plugin;

    public SchematicCommandNode(TARDIS plugin) {
        this.plugin = plugin;
    }

    LiteralCommandNode<CommandSourceStack> build() {
        LiteralArgumentBuilder<CommandSourceStack> command = Commands.literal("tardisschematic")
                // require a player to execute the command
                .requires(ctx -> ctx.getExecutor() instanceof Player && ctx.getSender().hasPermission("tardis.admin"))
                .then(Commands.literal("load")
                        .then(Commands.argument("directory", new DirectoryArgumentType())
                                .then(Commands.argument("schematic", StringArgumentType.word())
                                        .suggests(SchematicLoadSuggestions::get)
                                        .executes(ctx -> {
                                            String d = StringArgumentType.getString(ctx, "directory");
                                            String s = StringArgumentType.getString(ctx, "schematic");
                                            Player player = (Player) ctx.getSource().getExecutor();
                                            new SchematicLoad().act(plugin, player, d, s);
                                            return Command.SINGLE_SUCCESS;
                                        }))
                        ))
                .then(Commands.literal("paste")
                        .executes(ctx -> {
                            Player player = (Player) ctx.getSource().getExecutor();
                            SchematicPaster paster = new SchematicPaster(plugin, player, false);
                            int task = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, paster, 1L, 3L);
                            paster.setTask(task);
                            return Command.SINGLE_SUCCESS;
                        })
                        .then(Commands.literal("no_air")
                                .executes(ctx -> {
                                    Player player = (Player) ctx.getSource().getExecutor();
                                    SchematicPaster paster = new SchematicPaster(plugin, player, true);
                                    int task = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, paster, 1L, 3L);
                                    paster.setTask(task);
                                    return Command.SINGLE_SUCCESS;
                                })))
                .then(Commands.literal("save")
                        .then(Commands.argument("filename", StringArgumentType.word())
                                .executes(ctx -> {
                                    Player player = (Player) ctx.getSource().getExecutor();
                                    String n = StringArgumentType.getString(ctx, "filename");
                                    new SchematicSave().act(plugin, player, n);
                                    return Command.SINGLE_SUCCESS;
                                })))
                .then(Commands.literal("clear").executes(ctx -> {
                    Player player = (Player) ctx.getSource().getExecutor();
                    new SchematicClear().act(plugin, player);
                    return Command.SINGLE_SUCCESS;
                }))
                .then(Commands.literal("replace")
                        .then(Commands.argument("from_block", StringArgumentType.word())
                                .suggests(BlockSuggestions::get)
                                .then(Commands.argument("to_block", StringArgumentType.word())
                                        .suggests(BlockSuggestions::get)
                                        .executes(ctx -> {
                                            Player player = (Player) ctx.getSource().getExecutor();
                                            String from = StringArgumentType.getString(ctx, "from_block");
                                            String to = StringArgumentType.getString(ctx, "to_block");
                                            new SchematicReplace().act(plugin, player, from, to);
                                            return Command.SINGLE_SUCCESS;
                                        }))
                        ))
                .then(Commands.literal("convert")
                        .then(Commands.argument("from_light", StringArgumentType.word())
                                .suggests(LightSuggestions::get)
                                .then(Commands.argument("to_block", StringArgumentType.word())
                                        .suggests(BlockSuggestions::get)
                                        .executes(ctx -> {
                                            Player player = (Player) ctx.getSource().getExecutor();
                                            String from = StringArgumentType.getString(ctx, "from_light");
                                            String to = StringArgumentType.getString(ctx, "to_block");
                                            new SchematicConvert().act(plugin, player, from, to);
                                            return Command.SINGLE_SUCCESS;
                                        })
                                )))
                .then(Commands.literal("remove").
                        then(Commands.argument("what", StringArgumentType.word())
                                .suggests((ctx, builder) -> {
                                    builder.suggest("lights");
                                    builder.suggest("models");
                                    return builder.buildFuture();
                                })
                                .executes(ctx -> {
                                    Player player = (Player) ctx.getSource().getExecutor();
                                    String what = StringArgumentType.getString(ctx, "what");
                                    new SchematicRemove().act(plugin, player, what);
                                    return Command.SINGLE_SUCCESS;
                                })
                        ))
                .then(Commands.literal("flowers").executes(ctx -> {
                    Player player = (Player) ctx.getSource().getExecutor();
                    new SchematicFlowers().act(plugin, player);
                    return Command.SINGLE_SUCCESS;
                }))
                .then(Commands.literal("fix_liquid")
                        .then(Commands.argument("type", StringArgumentType.word())
                                .suggests((ctx, builder) -> {
                                    builder.suggest("lava");
                                    builder.suggest("water");
                                    return builder.buildFuture();
                                })
                                .executes(ctx -> {
                                    Player player = (Player) ctx.getSource().getExecutor();
                                    boolean lava = StringArgumentType.getString(ctx, "type").equalsIgnoreCase("lava");
                                    new SchematicLavaAndWater().act(plugin, player, lava);
                                    return Command.SINGLE_SUCCESS;
                                })
                        ))
                .then(Commands.literal("position").executes(ctx -> {
                    Player player = (Player) ctx.getSource().getExecutor();
                    new SchematicPosition().teleport(plugin, player);
                    return Command.SINGLE_SUCCESS;
                }));
        return command.build();
    }
}
