package me.eccentric_nz.TARDIS.brigadier;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.brigadier.arguments.DisplayArgumentType;
import me.eccentric_nz.TARDIS.display.TARDISDisplayType;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import org.bukkit.entity.Player;

import java.util.Locale;
import java.util.UUID;

public class DisplayCommandNode {

    private final TARDIS plugin;

    public DisplayCommandNode(TARDIS plugin) {
        this.plugin = plugin;
    }

    LiteralCommandNode<CommandSourceStack> build() {
        LiteralArgumentBuilder<CommandSourceStack> command = Commands.literal("tardisdisplay")
                .requires(ctx -> ctx.getSender() instanceof Player p && TARDISPermission.hasPermission(p, "tardis.display"))
                .executes(ctx -> {
                    Player player = (Player) ctx.getSource().getSender();
                    UUID uuid = player.getUniqueId();
                    if (plugin.getTrackerKeeper().getDisplay().containsKey(uuid)) {
                        plugin.getTrackerKeeper().getDisplay().remove(uuid);
                        plugin.getMessenger().send(player, TardisModule.TARDIS, "DISPLAY_DISABLED");
                    } else {
                        plugin.getTrackerKeeper().getDisplay().put(uuid, TARDISDisplayType.ALL);
                        plugin.getMessenger().send(player, TardisModule.TARDIS, "DISPLAY_ENABLED");
                    }
                    return Command.SINGLE_SUCCESS;
                })
                .then(Commands.argument("hud", new DisplayArgumentType())
                        .executes(ctx -> {
                            Player player = (Player) ctx.getSource().getSender();
                            String d = ctx.getArgument("hud", String.class);
                            try {
                                TARDISDisplayType displayType = TARDISDisplayType.valueOf(d.toUpperCase(Locale.ROOT));
                                plugin.getTrackerKeeper().getDisplay().put(player.getUniqueId(), displayType);
                                plugin.getMessenger().send(player, TardisModule.TARDIS, "DISPLAY_ENABLED");
                            } catch (IllegalArgumentException e) {
                                plugin.getMessenger().send(player, TardisModule.TARDIS, "DISPLAY_INVALID");
                            }
                            return Command.SINGLE_SUCCESS;
                        }));
        return command.build();
    }
}
