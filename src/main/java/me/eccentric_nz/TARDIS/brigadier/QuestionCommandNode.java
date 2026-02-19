package me.eccentric_nz.TARDIS.brigadier;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.brigadier.arguments.HelpCommandArgumentType;
import me.eccentric_nz.TARDIS.brigadier.suggestions.HelpSuggestions;
import me.eccentric_nz.TARDIS.commands.TARDISCommandHelper;

public class QuestionCommandNode {

    private final TARDIS plugin;

    public QuestionCommandNode(TARDIS plugin) {
        this.plugin = plugin;
    }

    LiteralCommandNode<CommandSourceStack> build() {
        LiteralArgumentBuilder<CommandSourceStack> command = Commands.literal("tardis?")
                .executes(ctx -> {
                    new TARDISCommandHelper(plugin).getCommand("", ctx.getSource().getSender());
                    return Command.SINGLE_SUCCESS;
                })
                .then(Commands.argument("command", new HelpCommandArgumentType())
                        .executes(ctx -> {
                            String s = StringArgumentType.getString(ctx, "command");
                            new TARDISCommandHelper(plugin).getCommand(s, ctx.getSource().getSender());
                            return Command.SINGLE_SUCCESS;
                        })
                        .then(Commands.argument("argument", StringArgumentType.word())
                                .suggests((ctx, builder) -> new HelpSuggestions(plugin, ctx).get(ctx, builder))
                                .executes(ctx -> {
                                    String s = StringArgumentType.getString(ctx, "command");
                                    String a = StringArgumentType.getString(ctx, "argument");
                                    new TARDISCommandHelper(plugin).getCommand(s + " " + a, ctx.getSource().getSender());
                                    return Command.SINGLE_SUCCESS;
                                })));
        return command.build();
    }
}
