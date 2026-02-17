package me.eccentric_nz.TARDIS.brigadier;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.brigadier.arguments.*;
import me.eccentric_nz.TARDIS.commands.give.actions.*;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import org.bukkit.entity.Player;

import java.util.Locale;

public class GiveCommandNode {

    private final TARDIS plugin;
    private final int full;

    public GiveCommandNode(TARDIS plugin) {
        this.plugin = plugin;
        full = plugin.getArtronConfig().getInt("full_charge");
    }

    LiteralCommandNode<CommandSourceStack> build() {
        LiteralArgumentBuilder<CommandSourceStack> command = Commands.literal("tardisgive")
                // require a player to execute the command
                .requires(ctx -> ctx.getSender().hasPermission("tardis.admin"))
                .then(Commands.argument("player", ArgumentTypes.player())
                        .then(Commands.literal("artron")
                                .then(Commands.literal("full")
                                        .executes(ctx -> {
                                            PlayerSelectorArgumentResolver targetResolver = ctx.getArgument("player", PlayerSelectorArgumentResolver.class);
                                            Player player = targetResolver.resolve(ctx.getSource()).getFirst();
                                            new Artron(plugin).give(ctx.getSource().getSender(), player, full, false);
                                            return Command.SINGLE_SUCCESS;
                                        }))
                                .then(Commands.argument("amount", IntegerArgumentType.integer(1, full))
                                        .executes(ctx -> {
                                            PlayerSelectorArgumentResolver targetResolver = ctx.getArgument("player", PlayerSelectorArgumentResolver.class);
                                            Player player = targetResolver.resolve(ctx.getSource()).getFirst();
                                            int amount = IntegerArgumentType.getInteger(ctx, "amount");
                                            new Artron(plugin).give(ctx.getSource().getSender(), player, amount, false);
                                            return Command.SINGLE_SUCCESS;
                                        })
                                        .then(Commands.literal("timelord")
                                                .executes(ctx -> {
                                                    PlayerSelectorArgumentResolver targetResolver = ctx.getArgument("player", PlayerSelectorArgumentResolver.class);
                                                    Player player = targetResolver.resolve(ctx.getSource()).getFirst();
                                                    int amount = IntegerArgumentType.getInteger(ctx, "amount");
                                                    new Artron(plugin).give(ctx.getSource().getSender(), player, amount, true);
                                                    return Command.SINGLE_SUCCESS;
                                                }))))
                        .then(Commands.literal("blueprint")
                                .then(Commands.argument("name", new BlueprintArgumentType())
                                        .executes(ctx -> {
                                            PlayerSelectorArgumentResolver targetResolver = ctx.getArgument("player", PlayerSelectorArgumentResolver.class);
                                            Player player = targetResolver.resolve(ctx.getSource()).getFirst();
                                            String n = ctx.getArgument("name", String.class);
                                            new TARDISBlueprint(plugin).give(ctx.getSource().getSender(), player, n);
                                            return Command.SINGLE_SUCCESS;
                                        })))
                        .then(Commands.literal("kit")
                                .then(Commands.argument("name", new KitArgumentType())
                                        .executes(ctx -> {
                                            PlayerSelectorArgumentResolver targetResolver = ctx.getArgument("player", PlayerSelectorArgumentResolver.class);
                                            Player player = targetResolver.resolve(ctx.getSource()).getFirst();
                                            String n = ctx.getArgument("name", String.class);
                                            plugin.getKitsConfig().getStringList("kits." + n).forEach((k) -> new Kit(plugin).give(k, player));
                                            plugin.getMessenger().send(player, TardisModule.TARDIS, "GIVE_KIT", ctx.getSource().getSender().getName(), n);
                                            return Command.SINGLE_SUCCESS;
                                        })))
                        .then(Commands.literal("recipes")
                                .then(Commands.literal("all")
                                        .executes(ctx -> {
                                            PlayerSelectorArgumentResolver targetResolver = ctx.getArgument("player", PlayerSelectorArgumentResolver.class);
                                            Player player = targetResolver.resolve(ctx.getSource()).getFirst();
                                            new TARDISRecipe(plugin).grantMultiple(ctx.getSource().getSender(), player);
                                            return Command.SINGLE_SUCCESS;
                                        })
                                        .then(Commands.argument("item", new GiveArgumentType())
                                                .executes(ctx -> {
                                                    PlayerSelectorArgumentResolver targetResolver = ctx.getArgument("player", PlayerSelectorArgumentResolver.class);
                                                    Player player = targetResolver.resolve(ctx.getSource()).getFirst();
                                                    String i = ctx.getArgument("item", String.class);
                                                    new TARDISRecipe(plugin).grant(ctx.getSource().getSender(), player, i);
                                                    return Command.SINGLE_SUCCESS;
                                                }))))
                        .then(Commands.literal("seed")
                                .then(Commands.argument("desktop", new SeedArgumentType())
                                        .executes(ctx -> {
                                            PlayerSelectorArgumentResolver targetResolver = ctx.getArgument("player", PlayerSelectorArgumentResolver.class);
                                            Player player = targetResolver.resolve(ctx.getSource()).getFirst();
                                            String t = ctx.getArgument("desktop", String.class);
                                            new Seed(plugin).give(ctx.getSource().getSender(), player, t, "ORANGE_WOOL", "LIGHT_GRAY_WOOL");
                                            return Command.SINGLE_SUCCESS;
                                        })
                                        .then(Commands.literal("knowledge")
                                                .executes(ctx -> {
                                                    PlayerSelectorArgumentResolver targetResolver = ctx.getArgument("player", PlayerSelectorArgumentResolver.class);
                                                    Player player = targetResolver.resolve(ctx.getSource()).getFirst();
                                                    String t = ctx.getArgument("desktop", String.class);
                                                    new Knowledge(plugin).give(ctx.getSource().getSender(), t.toLowerCase(Locale.ROOT) + "_seed", player);
                                                    return Command.SINGLE_SUCCESS;
                                                }))
                                        .then(Commands.argument("wall", new WallFloorArgumentType())
                                                .then(Commands.argument("floor", new WallFloorArgumentType())
                                                        .executes(ctx -> {
                                                            PlayerSelectorArgumentResolver targetResolver = ctx.getArgument("player", PlayerSelectorArgumentResolver.class);
                                                            Player player = targetResolver.resolve(ctx.getSource()).getFirst();
                                                            String t = ctx.getArgument("desktop", String.class);
                                                            String w = ctx.getArgument("wall", String.class);
                                                            String f = ctx.getArgument("floor", String.class);
                                                            new Seed(plugin).give(ctx.getSource().getSender(), player, t, w, f);
                                                            return Command.SINGLE_SUCCESS;
                                                        })))))
                        .then(Commands.literal("system-upgrade")
                                .then(Commands.argument("upgrade", new SystemUpgradeArgumentType())
                                        .executes(ctx -> {
                                            PlayerSelectorArgumentResolver targetResolver = ctx.getArgument("player", PlayerSelectorArgumentResolver.class);
                                            Player player = targetResolver.resolve(ctx.getSource()).getFirst();
                                            String u = ctx.getArgument("upgrade", String.class);
                                            new SystemUpgrades(plugin).give(ctx.getSource().getSender(), player, u);
                                            return Command.SINGLE_SUCCESS;
                                        })))
                        .then(Commands.literal("tachyon")
                                .then(Commands.argument("amount", IntegerArgumentType.integer(1, 1000))
                                        .executes(ctx -> {
                                            PlayerSelectorArgumentResolver targetResolver = ctx.getArgument("player", PlayerSelectorArgumentResolver.class);
                                            Player player = targetResolver.resolve(ctx.getSource()).getFirst();
                                            int amount = IntegerArgumentType.getInteger(ctx, "amount");
                                            new Tachyon(plugin).give(ctx.getSource().getSender(), player, amount);
                                            return Command.SINGLE_SUCCESS;
                                        })))
                        .then(Commands.literal("all")
                                .then(Commands.literal("knowledge")
                                        .executes(ctx -> {
                                            PlayerSelectorArgumentResolver targetResolver = ctx.getArgument("player", PlayerSelectorArgumentResolver.class);
                                            Player player = targetResolver.resolve(ctx.getSource()).getFirst();
                                            new Knowledge(plugin).giveAll(ctx.getSource().getSender(), player);
                                            return Command.SINGLE_SUCCESS;
                                        })))
                        .then(Commands.literal("artron-storage-cell")
                                .then(Commands.argument("amount", IntegerArgumentType.integer(1, 64))
                                        .executes(ctx->{
                                            PlayerSelectorArgumentResolver targetResolver = ctx.getArgument("player", PlayerSelectorArgumentResolver.class);
                                            Player player = targetResolver.resolve(ctx.getSource()).getFirst();
                                            int amount = IntegerArgumentType.getInteger(ctx, "amount");
                                            new TARDISItem(plugin).give(ctx.getSource().getSender(), "artron-storage-cell", amount, player);
                                            return Command.SINGLE_SUCCESS;
                                        })
                                        .then(Commands.literal("full")
                                                .executes(ctx -> {
                                                    PlayerSelectorArgumentResolver targetResolver = ctx.getArgument("player", PlayerSelectorArgumentResolver.class);
                                                    Player player = targetResolver.resolve(ctx.getSource()).getFirst();
                                                    int amount = IntegerArgumentType.getInteger(ctx, "amount");
                                                    new FuelCell(plugin).give(ctx.getSource().getSender(), amount, player);
                                                    return Command.SINGLE_SUCCESS;
                                                }))))
                        .then(Commands.argument("item", new GiveArgumentType())
                                .then(Commands.literal("knowledge")
                                        .executes(ctx -> {
                                            PlayerSelectorArgumentResolver targetResolver = ctx.getArgument("player", PlayerSelectorArgumentResolver.class);
                                            Player player = targetResolver.resolve(ctx.getSource()).getFirst();
                                            String item = ctx.getArgument("item", String.class);
                                            new Knowledge(plugin).give(ctx.getSource().getSender(), item, player);
                                            return Command.SINGLE_SUCCESS;
                                        }))
                                .then(Commands.argument("amount", IntegerArgumentType.integer(1, 64))
                                        .executes(ctx -> {
                                            PlayerSelectorArgumentResolver targetResolver = ctx.getArgument("player", PlayerSelectorArgumentResolver.class);
                                            Player player = targetResolver.resolve(ctx.getSource()).getFirst();
                                            String item = ctx.getArgument("item", String.class);
                                            int amount = IntegerArgumentType.getInteger(ctx, "amount");
                                            new TARDISItem(plugin).give(ctx.getSource().getSender(), item, amount, player);
                                            return Command.SINGLE_SUCCESS;
                                        })))
                );
        return command.build();
    }
}
