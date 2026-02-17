package me.eccentric_nz.TARDIS.brigadier;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.brigadier.arguments.*;
import me.eccentric_nz.TARDIS.commands.TARDISCommandHelper;
import me.eccentric_nz.TARDIS.commands.config.ReloadCommand;
import me.eccentric_nz.TARDIS.commands.give.actions.*;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import org.bukkit.entity.Player;

import java.util.Locale;

public class ConfigCommandNode {

    private final TARDIS plugin;

    public ConfigCommandNode(TARDIS plugin) {
        this.plugin = plugin;
    }

    LiteralCommandNode<CommandSourceStack> build() {
        LiteralArgumentBuilder<CommandSourceStack> command = Commands.literal("tardisconfig")
                // require a player to execute the command
                .requires(ctx -> ctx.getSender().hasPermission("tardis.admin"))
                .executes(ctx -> {
                    new TARDISCommandHelper(plugin).getCommand("tardisconfig", ctx.getSource().getSender());
                    return Command.SINGLE_SUCCESS;
                })
                .then(Commands.literal("reload")
                        .executes(ctx -> {
                            new ReloadCommand(plugin).reloadConfig(ctx.getSource().getSender());
                            return Command.SINGLE_SUCCESS;
                        })
                        .then(Commands.argument("module", new ConfigFileArgumentType())
                                .executes(ctx -> {
                                    String m = ctx.getArgument("module", String.class);
                                    new ReloadCommand(plugin).reloadOtherConfig(ctx.getSource().getSender(), m);
                                    return Command.SINGLE_SUCCESS;
                                })))
                // TODO add more literals
                .then(Commands.argument("option", new ConfigOptionArgumentType())
                        .then(Commands.argument("boolean", BoolArgumentType.bool())
                                .executes(ctx -> {
                                    String o = ctx.getArgument("option", String.class);

                                    return Command.SINGLE_SUCCESS;
                                }))
                        .then(Commands.argument("integer", IntegerArgumentType.integer())
                                .executes(ctx -> {
                                    String o = ctx.getArgument("option", String.class);

                                    return Command.SINGLE_SUCCESS;
                                }))
                        .then(Commands.argument("string", StringArgumentType.word())
                                .executes(ctx -> {
                                    String o = ctx.getArgument("option", String.class);
                                    // TODO sort out string arguments from literals that are just arguments for
                                    return Command.SINGLE_SUCCESS;
                                }))
                );
        return command.build();
    }
}
