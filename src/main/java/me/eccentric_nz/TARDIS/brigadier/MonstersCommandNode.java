package me.eccentric_nz.TARDIS.brigadier;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.brigadier.arguments.MonsterArgumentType;
import me.eccentric_nz.TARDIS.brigadier.arguments.MonsterExtraArgumentType;
import me.eccentric_nz.TARDIS.brigadier.arguments.OnOffArgumentType;
import me.eccentric_nz.TARDIS.commands.TARDISCallRequestCommand;
import me.eccentric_nz.tardisweepingangels.commands.*;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.UUID;

public class MonstersCommandNode {

    private final TARDIS plugin;

    public MonstersCommandNode(TARDIS plugin) {
        this.plugin = plugin;
    }

    LiteralCommandNode<CommandSourceStack> build() {
        LiteralArgumentBuilder<CommandSourceStack> command = Commands.literal("twa")
                .requires(ctx -> ctx.getExecutor() instanceof Player)
                .then(Commands.literal("spawn")
                        .executes(ctx -> {
                            new SpawnCommand(plugin).spawn(ctx.getSource().getSender(), args);
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("equip")
                        .then(Commands.argument("monster", new MonsterArgumentType())
                                .executes(ctx -> {
                                    String m = ctx.getArgument("monster", String.class);
                                    new EquipCommand(plugin).equip(ctx.getSource().getSender(), m, "");
                                    return Command.SINGLE_SUCCESS;
                                })
                                .then(Commands.argument("extra", new MonsterExtraArgumentType())
                                        .executes(ctx -> {
                                            String m = ctx.getArgument("monster", String.class);
                                            String e = ctx.getArgument("extra", String.class);
                                            new EquipCommand(plugin).equip(ctx.getSource().getSender(), m, e);
                                            return Command.SINGLE_SUCCESS;
                                        })
                                )))
                .then(Commands.literal("disguise")
                        .then(Commands.argument("monster", new MonsterArgumentType())
                                .then(Commands.argument("toggle", new OnOffArgumentType())
                                        .executes(ctx -> {
                                            String m = ctx.getArgument("monster", String.class);
                                            String o = ctx.getArgument("toggle", String.class);
                                            new DisguiseCommand(plugin).disguise(ctx.getSource().getSender(), m, o, "");
                                            return Command.SINGLE_SUCCESS;
                                        })
                                        .then(Commands.argument("uuid", ArgumentTypes.uuid())
                                                .executes(ctx -> {
                                                    String m = ctx.getArgument("monster", String.class);
                                                    String o = ctx.getArgument("toggle", String.class);
                                                    String u = ctx.getArgument("uuid", UUID.class).toString();
                                                    new DisguiseCommand(plugin).disguise(ctx.getSource().getSender(), m, o, u);
                                                    return Command.SINGLE_SUCCESS;
                                                })))))
                .then(Commands.literal("kill")
                        .then(Commands.argument("monster", new MonsterArgumentType())
                                .then(Commands.argument("world", ArgumentTypes.world())
                                        .executes(ctx -> {
                                            String m = ctx.getArgument("monster", String.class);
                                            World w = ctx.getArgument("world", World.class);
                                            new KillCommand(plugin).kill(ctx.getSource().getSender(), m, w);
                                            return Command.SINGLE_SUCCESS;
                                        }))))
                .then(Commands.literal("count")
                        .then(Commands.argument("monster", new MonsterArgumentType())
                                .then(Commands.argument("world", ArgumentTypes.world())
                                        .executes(ctx -> {
                                            String m = ctx.getArgument("monster", String.class);
                                            World w = ctx.getArgument("world", World.class);
                                            new CountCommand(plugin).count(ctx.getSource().getSender(), m, w);
                                            return Command.SINGLE_SUCCESS;
                                        }))))
                .then(Commands.literal("follow")
                        .executes(ctx -> {
                            new FollowCommand(plugin).follow(ctx.getSource().getSender());
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("stay")
                        .executes(ctx -> {
                            new StayCommand(plugin).stay(ctx.getSource().getSender());
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("remove")
                        .executes(ctx -> {
                            new RemoveCommand(plugin).remove(ctx.getSource().getSender());
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("set")
                        .then(Commands.argument("monster", new MonsterArgumentType())
                                .then(Commands.argument("world", ArgumentTypes.world())
                                        .then(Commands.argument("amount", IntegerArgumentType.integer(0))
                                                .executes(ctx -> {
                                                    if (TARDISPermission.hasPermission(ctx.getSource().getSender(), "tardis.admin")) {
                                                        String m = ctx.getArgument("monster", String.class);
                                                        World w = ctx.getArgument("world", World.class);
                                                        int a = IntegerArgumentType.getInteger(ctx, "monster");
                                                        new AdminCommand(plugin).set(ctx.getSource().getSender(), m, w, a);
                                                    }
                                                    return Command.SINGLE_SUCCESS;
                                                })))))
                .then(Commands.literal("give")
                        .then(Commands.argument("player", ArgumentTypes.player())
                                .then(Commands.argument("monster", new MonsterArgumentType())
                                        .executes(ctx -> {
                                            PlayerSelectorArgumentResolver targetResolver = ctx.getArgument("player", PlayerSelectorArgumentResolver.class);
                                            Player p = targetResolver.resolve(ctx.getSource()).getFirst();
                                            String m = ctx.getArgument("monster", String.class);
                                            new GiveCommand(plugin).give(ctx.getSource().getSender(), p, m);
                                            return Command.SINGLE_SUCCESS;
                                        }))))
                .then(Commands.literal("team")
                        .executes(ctx -> {
                            new TeamCommand(plugin).join(ctx.getSource().getSender());
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("teleport")
                        .executes(ctx -> {
                            if (ctx.getSource().getSender() instanceof Player player) {
                                new TeleportCommand(plugin).add(player);
                            }
                            return Command.SINGLE_SUCCESS;
                        })
                        .then(Commands.literal("replace")
                                .executes(ctx -> {
                                    if (ctx.getSource().getSender() instanceof Player player) {
                                        new TeleportCommand(plugin).replace(player);
                                    }
                                    return Command.SINGLE_SUCCESS;
                                }))
                        .then(Commands.argument("toggle", BoolArgumentType.bool())
                                .executes(ctx -> {
                                    boolean b = BoolArgumentType.getBool(ctx, "toggle");
                                    new TeleportCommand(plugin).toggle(ctx.getSource().getSender(), b);
                                    return Command.SINGLE_SUCCESS;
                                })));
        return command.build();
    }
}
