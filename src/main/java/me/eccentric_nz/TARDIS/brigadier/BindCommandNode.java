package me.eccentric_nz.TARDIS.brigadier;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.RegistryArgumentExtractor;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.TypedKey;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.brigadier.arguments.BindTypeArgument;
import me.eccentric_nz.TARDIS.brigadier.arguments.PresetArgumentType;
import me.eccentric_nz.TARDIS.commands.bind.BindRemove;
import me.eccentric_nz.TARDIS.enumeration.Bind;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;

public class BindCommandNode {

    private final TARDIS plugin;

    public BindCommandNode(TARDIS plugin) {
        this.plugin = plugin;
    }

    LiteralCommandNode<CommandSourceStack> build() {
        LiteralArgumentBuilder<CommandSourceStack> command = Commands.literal("tardisbind")
                // require a player to execute the command
                .requires(ctx -> ctx.getExecutor() instanceof Player && ctx.getSender().hasPermission("tardis.update"))
                .then(Commands.literal("add")
                        .then(Commands.literal("SAVE"))
                        .then(Commands.literal("AREA"))
                        .then(Commands.literal("BIOME")
                                .then(Commands.argument("biome", ArgumentTypes.resourceKey(RegistryKey.BIOME))
                                        .executes(ctx -> {
                                            TypedKey<Biome> key = RegistryArgumentExtractor.getTypedKey(ctx, RegistryKey.BIOME, "biome");
                                            Biome biome = RegistryAccess.registryAccess().getRegistry(key.registryKey()).get(key.key());

                                            return Command.SINGLE_SUCCESS;
                                        })))
                        .then(Commands.literal("CHAMELEON")
                                .then(Commands.argument("preset", new PresetArgumentType(2))
                                        .executes(ctx -> {
                                            String p = ctx.getArgument("preset", String.class);

                                            return Command.SINGLE_SUCCESS;
                                        })))
                        .then(Commands.literal("PLAYER"))
                        .then(Commands.literal("TRANSMAT"))
                        .then(Commands.argument("bind_type", new BindTypeArgument(true))
                                .executes(ctx -> {
                                    
                                    return Command.SINGLE_SUCCESS;
                                })))
                .then(Commands.literal("remove")
                        .then(Commands.argument("bind_type", new BindTypeArgument(false))
                                .executes(ctx -> {
                                    Bind bind = Bind.valueOf(StringArgumentType.getString(ctx, "bind_type"));
                                    Player player = (Player) ctx.getSource().getExecutor();
                                    new BindRemove(plugin).setClick(bind, player);
                                    return Command.SINGLE_SUCCESS;
                                })));
        return command.build();
    }
}
