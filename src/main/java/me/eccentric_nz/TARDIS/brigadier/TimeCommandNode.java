package me.eccentric_nz.TARDIS.brigadier;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.brigadier.arguments.TimeArgumentType;
import me.eccentric_nz.TARDIS.commands.utils.TimeUtility;
import org.bukkit.entity.Player;

public class TimeCommandNode {

    private final TARDIS plugin;

    public TimeCommandNode(TARDIS plugin) {
        this.plugin = plugin;
    }

    LiteralCommandNode<CommandSourceStack> build() {
        LiteralArgumentBuilder<CommandSourceStack> command = Commands.literal("tardistime")
                .requires(ctx -> ctx.getExecutor() instanceof Player && ctx.getExecutor().hasPermission("tardis.admin"))
                .then(Commands.argument("time", ArgumentTypes.time())
                        .executes(ctx -> {
                            Player player = (Player) ctx.getSource().getExecutor();
                            int timeInTicks = IntegerArgumentType.getInteger(ctx, "time");
                            TimeUtility.setTime(plugin, timeInTicks, player);
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.argument("time_of_day", new TimeArgumentType())
                        .executes(ctx -> {
                            Player player = (Player) ctx.getSource().getExecutor();
                            String t = ctx.getArgument("time_of_day", String.class);
                            TimeUtility.setTime(plugin, t, player);
                            return Command.SINGLE_SUCCESS;
                        }));
        return command.build();
    }
}
