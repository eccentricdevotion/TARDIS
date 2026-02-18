package me.eccentric_nz.TARDIS.brigadier;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.brigadier.arguments.DirectionArgumentType;
import me.eccentric_nz.TARDIS.commands.GravityCommand;
import me.eccentric_nz.TARDIS.commands.TARDISCommandHelper;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.tardischunkgenerator.worldgen.PlotGenerator;
import org.bukkit.entity.Player;

public class PlotCommandNode {

    private final TARDIS plugin;

    public PlotCommandNode(TARDIS plugin) {
        this.plugin = plugin;
    }

    LiteralCommandNode<CommandSourceStack> build() {
        LiteralArgumentBuilder<CommandSourceStack> command = Commands.literal("tardisplot")
                .requires(ctx -> ctx.getExecutor() instanceof Player && ctx.getSender().hasPermission("tardis.plot"))
                .then(Commands.literal("name")
                        .then(Commands.argument("string", StringArgumentType.greedyString())
                                .executes(ctx -> {
                                    Player player = (Player) ctx.getSource().getExecutor();
                                    // are they in a plot world?
                                    if (!(player.getWorld().getGenerator() instanceof PlotGenerator)) {
                                        plugin.getMessenger().send(player, TardisModule.TARDIS, "PLOT_WORLD");
                                        return Command.SINGLE_SUCCESS;
                                    }
                                    String name = StringArgumentType.getString(ctx, "string");
                                    if (name.length() < 2 || name.length() > 16) {
                                        plugin.getMessenger().send(player, TardisModule.TARDIS, "PLOT_BAD_NAME");
                                        return Command.SINGLE_SUCCESS;
                                    }
                                    plugin.getTrackerKeeper().getPlotters().put(player.getUniqueId(), name);
                                    plugin.getMessenger().send(player, TardisModule.TARDIS, "PLOT_CLICK");
                                    return Command.SINGLE_SUCCESS;
                                })));
        return command.build();
    }
}
