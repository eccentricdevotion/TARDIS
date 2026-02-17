package me.eccentric_nz.TARDIS.brigadier;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.RegistryArgumentExtractor;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.TypedKey;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.brigadier.arguments.*;
import me.eccentric_nz.TARDIS.commands.bind.*;
import me.eccentric_nz.TARDIS.enumeration.Bind;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;

public class GiveCommandNode {

    private final TARDIS plugin;

    public GiveCommandNode(TARDIS plugin) {
        this.plugin = plugin;
    }

    LiteralCommandNode<CommandSourceStack> build() {
        LiteralArgumentBuilder<CommandSourceStack> command = Commands.literal("tardisgive")
                // require a player to execute the command
                .requires(ctx -> ctx.getExecutor() instanceof Player && ctx.getSender().hasPermission("tardis.admin"))
                .then(Commands.argument("player", ArgumentTypes.player())
                        .then(Commands.literal("artron")
                                .then(Commands.argument("amount", IntegerArgumentType.integer(1))))
                        .then(Commands.literal("blueprint")
                                .then(Commands.argument("name", new BlueprintArgumentType())))
                        .then(Commands.literal("kit"))
                        .then(Commands.literal("recipes"))
                        .then(Commands.literal("seed"))
                        .then(Commands.literal("system-upgrade"))
                        .then(Commands.literal("tachyon")
                                .then(Commands.argument("amount", IntegerArgumentType.integer(1))))
//                        .then(Commands.literal("acid-bucket"))
//                        .then(Commands.literal("rust-bucket"))
                        .then(Commands.argument("item", new GiveArgumentType())
                                .then(Commands.argument("amount", IntegerArgumentType.integer(1)))));
        return command.build();
    }
}
