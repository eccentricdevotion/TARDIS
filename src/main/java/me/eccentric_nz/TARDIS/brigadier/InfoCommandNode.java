package me.eccentric_nz.TARDIS.brigadier;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.info.TARDISIndexFileInventory;
import me.eccentric_nz.TARDIS.info.TARDISInformationSystemListener;
import org.bukkit.entity.Player;

public class InfoCommandNode {

    private final TARDIS plugin;

    public InfoCommandNode(TARDIS plugin) {
        this.plugin = plugin;
    }

    LiteralCommandNode<CommandSourceStack> build() {
        LiteralArgumentBuilder<CommandSourceStack> command = Commands.literal("tardisinfo")
                .requires(ctx -> ctx.getExecutor() instanceof Player)
                .executes(ctx -> {
                    Player player = (Player) ctx.getSource().getExecutor();
                    if (plugin.getTrackerKeeper().getInfoMenu().containsKey(player.getUniqueId())) {
                        plugin.getMessenger().send(player, TardisModule.TARDIS, "TIS_EXIT");
                    }
                    return Command.SINGLE_SUCCESS;
                })
                .then(Commands.literal("e")
                        .executes(ctx -> {
                            Player player = (Player) ctx.getSource().getExecutor();
                            if (plugin.getTrackerKeeper().getInfoMenu().containsKey(player.getUniqueId())) {
                                TARDISInformationSystemListener.exit(player, plugin);
                            }
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("E")
                        .executes(ctx -> {
                            Player player = (Player) ctx.getSource().getExecutor();
                            if (plugin.getTrackerKeeper().getInfoMenu().containsKey(player.getUniqueId())) {
                                TARDISInformationSystemListener.exit(player, plugin);
                            }
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("gui")
                        .executes(ctx -> {
                            Player player = (Player) ctx.getSource().getExecutor();
                            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> player.openInventory(new TARDISIndexFileInventory(plugin).getInventory()), 2L);
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.argument("i", StringArgumentType.word())
                        .executes(ctx -> {
                            Player player = (Player) ctx.getSource().getExecutor();
                            if (plugin.getTrackerKeeper().getInfoMenu().containsKey(player.getUniqueId())) {
                                String i = StringArgumentType.getString(ctx, "i");
                                TARDISInformationSystemListener.processInput(player, i, plugin);
                            }
                            return Command.SINGLE_SUCCESS;
                        }));
        return command.build();
    }
}
