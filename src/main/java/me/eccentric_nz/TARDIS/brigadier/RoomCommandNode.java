package me.eccentric_nz.TARDIS.brigadier;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.brigadier.arguments.RoomArgumentType;
import me.eccentric_nz.TARDIS.brigadier.suggestions.BlockSuggestions;
import me.eccentric_nz.TARDIS.commands.RoomsUtility;
import me.eccentric_nz.TARDIS.commands.TARDISCommandHelper;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.rooms.RoomRequiredLister;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;

public class RoomCommandNode {

    private final TARDIS plugin;

    public RoomCommandNode(TARDIS plugin) {
        this.plugin = plugin;
    }

    LiteralCommandNode<CommandSourceStack> build() {
        LiteralArgumentBuilder<CommandSourceStack> command = Commands.literal("tardisroom")
                .executes(ctx -> {
                    new TARDISCommandHelper(plugin).getCommand("tardisroom", ctx.getSource().getSender());
                    return Command.SINGLE_SUCCESS;
                })
                .then(Commands.literal("add")
                        .then(Commands.argument("name", StringArgumentType.word())
                                .requires(ctx -> ctx.getSender().hasPermission("tardis.admin"))
                                .executes(ctx -> {
                                    String n = ctx.getArgument("name", String.class);
                                    RoomsUtility.add(plugin, ctx.getSource().getSender(), n);
                                    return Command.SINGLE_SUCCESS;
                                })))
                .then(Commands.literal("blocks")
                        .then(Commands.literal("save")
                                .requires(ctx -> ctx.getSender().hasPermission("tardis.admin"))
                                .executes(ctx -> {
                                    RoomsUtility.saveBlocks(plugin, ctx.getSource().getSender());
                                    return Command.SINGLE_SUCCESS;
                                }))
                        .then(Commands.argument("room", new RoomArgumentType(plugin)))
                        .executes(ctx -> {
                            String r = ctx.getArgument("room", String.class);
                            RoomsUtility.listBlocks(plugin, ctx.getSource().getSender(), r);
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("required")
                        .then(Commands.argument("room", new RoomArgumentType(plugin))
                                .requires(ctx -> ctx.getExecutor() instanceof Player)
                                .executes(ctx -> {
                                    Player player = (Player) ctx.getSource().getExecutor();
                                    String r = ctx.getArgument("room", String.class);
                                    RoomRequiredLister.listCondensables(plugin, r, player);
                                    return Command.SINGLE_SUCCESS;
                                })))
                .then(Commands.argument("room", new RoomArgumentType(plugin))
                        .then(Commands.argument("enable", BoolArgumentType.bool())
                                .requires(ctx -> ctx.getSender().hasPermission("tardis.admin"))
                                .executes(ctx -> {
                                    String r = ctx.getArgument("room", String.class);
                                    boolean b = BoolArgumentType.getBool(ctx, "enable");
                                    RoomsUtility.setRoomEnabled(plugin, ctx.getSource().getSender(), r, b);
                                    return Command.SINGLE_SUCCESS;
                                }))
                        .then(Commands.argument("offset", IntegerArgumentType.integer(-6, 0))
                                .requires(ctx -> ctx.getSender().hasPermission("tardis.admin"))
                                .executes(ctx -> {
                                    String r = ctx.getArgument("room", String.class);
                                    int o = IntegerArgumentType.getInteger(ctx, "offset");
                                    plugin.getRoomsConfig().set("rooms." + r + ".offset", o);
                                    try {
                                        plugin.getRoomsConfig().save(new File(plugin.getDataFolder(), "rooms.yml"));
                                        plugin.getMessenger().send(ctx.getSource().getSender(), TardisModule.TARDIS, "ROOM_OFFSET", r, String.format("%d", o));
                                    } catch (IOException io) {
                                        plugin.debug("Could not save rooms.yml, " + io);
                                    }
                                    return Command.SINGLE_SUCCESS;
                                }))
                        .then(Commands.argument("cost", IntegerArgumentType.integer(1))
                                .requires(ctx -> ctx.getSender().hasPermission("tardis.admin"))
                                .executes(ctx -> {
                                    String r = ctx.getArgument("room", String.class);
                                    int c = IntegerArgumentType.getInteger(ctx, "cost");
                                    plugin.getRoomsConfig().set("rooms." + r + ".cost", c);
                                    try {
                                        plugin.getRoomsConfig().save(new File(plugin.getDataFolder(), "rooms.yml"));
                                        plugin.getMessenger().send(ctx.getSource().getSender(), TardisModule.TARDIS, "ROOM_COST", r, String.format("%d", c));
                                    } catch (IOException io) {
                                        plugin.debug("Could not save rooms.yml, " + io);
                                    }
                                    return Command.SINGLE_SUCCESS;
                                }))
                        .then(Commands.argument("seed", StringArgumentType.word())
                                .requires(ctx -> ctx.getSender().hasPermission("tardis.admin"))
                                .suggests(BlockSuggestions::get)
                                .executes(ctx -> {
                                    String r = ctx.getArgument("room", String.class);
                                    String s = ctx.getArgument("seed", String.class);
                                    RoomsUtility.setRoomSeed(plugin, ctx.getSource().getSender(), r, s);
                                    return Command.SINGLE_SUCCESS;
                                }))
                );
        return command.build();
    }
}
