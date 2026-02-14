package me.eccentric_nz.TARDIS.brigadier;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.brigadier.arguments.DirectoryArgumentType;
import me.eccentric_nz.TARDIS.brigadier.suggestions.SchematicLoadSuggestions;
import me.eccentric_nz.TARDIS.schematic.actions.SchematicLoad;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class SchematicCommandNode {

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
                                            TARDIS.plugin.debug(d);
                                            String s = StringArgumentType.getString(ctx, "schematic");
                                            TARDIS.plugin.debug(s);
                                            Entity executor = ctx.getSource().getExecutor();
                                            if (!(executor instanceof Player player)) {
                                                // If a non-player tried to set their own flight speed
                                                ctx.getSource().getSender().sendPlainMessage("Only players can load schematics!");
                                                return Command.SINGLE_SUCCESS;
                                            }
                                            new SchematicLoad().act(TARDIS.plugin, player, d, s);
                                            return Command.SINGLE_SUCCESS;
                                        }))
                        ))
                .then(Commands.literal("paste")
                        .then(Commands.literal("no_air")))
                .then(Commands.literal("save")
                        .then(Commands.argument("directory", StringArgumentType.word())))
                .then(Commands.literal("clear"))
                .then(Commands.literal("replace")
                        .then(Commands.argument("from", StringArgumentType.word())
                                .then(Commands.argument("to", StringArgumentType.word()))
                        ))
                .then(Commands.literal("convert")
                        .then(Commands.argument("type", StringArgumentType.word())))
                .then(Commands.literal("remove").
                        then(Commands.argument("what", StringArgumentType.word())))
                .then(Commands.literal("flowers"))
                .then(Commands.literal("fixliquid")
                        .then(Commands.argument("type", StringArgumentType.word())))
                .then(Commands.literal("position"));
        return command.build();
    }
}
