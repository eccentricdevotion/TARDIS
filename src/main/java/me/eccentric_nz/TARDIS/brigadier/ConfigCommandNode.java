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
import me.eccentric_nz.TARDIS.brigadier.suggestions.BlockSuggestions;
import me.eccentric_nz.TARDIS.commands.TARDISCommandHelper;
import me.eccentric_nz.TARDIS.commands.config.*;
import me.eccentric_nz.TARDIS.commands.give.actions.*;
import me.eccentric_nz.TARDIS.enumeration.ChameleonPreset;
import me.eccentric_nz.TARDIS.enumeration.CraftingDifficulty;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.enumeration.UseClay;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

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
                .then(Commands.literal("autonomous_area")
                        .then(Commands.argument("option", new AreasArgumentType())
                                .then(Commands.argument("action", StringArgumentType.word()).suggests((ctx, builder) -> {
                                            builder.suggest("add");
                                            builder.suggest("remove");
                                            return builder.buildFuture();
                                        })
                                        .executes(ctx -> {
                                            String o = ctx.getArgument("option", String.class);
                                            String a = ctx.getArgument("action", String.class);
                                            new AutonomousAreaCommand(plugin).processArea(ctx.getSource().getSender(), o, a);
                                            return Command.SINGLE_SUCCESS;
                                        }))))
                .then(Commands.literal("options")
                        .then(Commands.argument("section", new SectionArgumentType(plugin))
                                .executes(ctx -> {
                                    String o = ctx.getArgument("option", String.class);
                                    new OptionsCommand(plugin).showConfigOptions(ctx.getSource().getSender(), o);
                                    return Command.SINGLE_SUCCESS;
                                })))
                .then(Commands.literal("language")
                        .then(Commands.argument("option", new LanguageArgumentType())
                                .executes(ctx -> {
                                    String o = ctx.getArgument("option", String.class);
                                    new LanguageCommand(plugin).setLanguage(ctx.getSource().getSender(), o);
                                    return Command.SINGLE_SUCCESS;
                                })))
                .then(Commands.literal("power_down")
                        .then(Commands.argument("option", BoolArgumentType.bool())
                                .executes(ctx -> {
                                    boolean b = BoolArgumentType.getBool(ctx, "option");
                                    new PowerDownCommand(plugin).togglePowerDown(ctx.getSource().getSender(), b);
                                    return Command.SINGLE_SUCCESS;
                                })))
                .then(Commands.literal("database")
                        .then(Commands.argument("option", new DatabaseArgumentType())
                                .executes(ctx -> {
                                    String o = ctx.getArgument("option", String.class);
                                    String dbtype = o.toLowerCase(Locale.ROOT);
                                    plugin.getConfig().set("database", dbtype);
                                    plugin.saveConfig();
                                    return Command.SINGLE_SUCCESS;
                                })))
                .then(Commands.literal("include")
                        .then(Commands.argument("option", ArgumentTypes.world())
                                .executes(ctx -> {
                                    World world = ctx.getArgument("world", World.class);
                                    new SetWorldInclusionCommand(plugin).setWorldStatus(ctx.getSource().getSender(), "include", world);
                                    return Command.SINGLE_SUCCESS;
                                })))
                .then(Commands.literal("exclude")
                        .then(Commands.argument("option", ArgumentTypes.world())
                                .executes(ctx -> {
                                    World world = ctx.getArgument("world", World.class);
                                    new SetWorldInclusionCommand(plugin).setWorldStatus(ctx.getSource().getSender(), "exclude", world);
                                    return Command.SINGLE_SUCCESS;
                                })))
                .then(Commands.literal("siege")
                        .then(Commands.argument("option", new SiegeArgumentType())
                                .then(Commands.argument("bool", BoolArgumentType.bool())
                                        .executes(ctx -> {
                                            String o = ctx.getArgument("option", String.class);
                                            boolean b = BoolArgumentType.getBool(ctx, "bool");
                                            new SiegeCommand(plugin).setOption(ctx.getSource().getSender(), o, b);
                                            return Command.SINGLE_SUCCESS;
                                        }))
                                .then(Commands.argument("integer", IntegerArgumentType.integer(1))
                                        .executes(ctx -> {
                                            String o = ctx.getArgument("option", String.class);
                                            int i = IntegerArgumentType.getInteger(ctx, "integer");
                                            new SiegeCommand(plugin).setOption(ctx.getSource().getSender(), o, i);
                                            return Command.SINGLE_SUCCESS;
                                        }))))
                .then(Commands.literal("sign_colour")
                        .then(Commands.argument("option", ArgumentTypes.namedColor())
                                .executes(ctx -> {
                                    NamedTextColor colour = ctx.getArgument("color", NamedTextColor.class);
                                    // TODO SignColourCommand should accept named colour
                                    new SignColourCommand(plugin).setColour(ctx.getSource().getSender(), colour);
                                    return Command.SINGLE_SUCCESS;
                                })))
                .then(Commands.literal("key")
                        .then(Commands.argument("option", ArgumentTypes.itemStack())
                                .executes(ctx -> {
                                    ItemStack stack = ctx.getArgument("option", ItemStack.class);
                                    new SetMaterialCommand(plugin).setConfigMaterial(ctx.getSource().getSender(), stack.getType().toString(), "preferences");
                                    return Command.SINGLE_SUCCESS;
                                })))
                .then(Commands.literal("full_charge_item")
                        .then(Commands.argument("option", ArgumentTypes.itemStack())
                                .executes(ctx -> {
                                    ItemStack stack = ctx.getArgument("option", ItemStack.class);
                                    new SetMaterialCommand(plugin).setConfigMaterial(ctx.getSource().getSender(), "full_charge_item", stack.getType().toString());
                                    return Command.SINGLE_SUCCESS;
                                })))
                .then(Commands.literal("jettison_seed")
                        .then(Commands.argument("option", StringArgumentType.word())
                                .suggests(BlockSuggestions::get)
                                .executes(ctx -> {
                                    String o = ctx.getArgument("option", String.class);
                                    new SetMaterialCommand(plugin).setConfigMaterial(ctx.getSource().getSender(), "jettison_seed", o);
                                    return Command.SINGLE_SUCCESS;
                                })))
                .then(Commands.literal("default_key")
                        .then(Commands.argument("option", new KeyArgumentType())
                                .executes(ctx -> {
                                    String o = ctx.getArgument("option", String.class);
                                    new DefaultCommand(plugin).setDefaultItem(ctx.getSource().getSender(), "default_key", o);
                                    return Command.SINGLE_SUCCESS;
                                })))
                .then(Commands.literal("default_sonic")
                        .then(Commands.argument("option", new SonicArgumentType())
                                .executes(ctx -> {
                                    String o = ctx.getArgument("option", String.class);
                                    new DefaultCommand(plugin).setDefaultItem(ctx.getSource().getSender(), "default_model", o);
                                    return Command.SINGLE_SUCCESS;
                                })))
                .then(Commands.literal("default_world_name")
                        .then(Commands.argument("option", ArgumentTypes.world())
                                .executes(ctx -> {
                                    World world = ctx.getArgument("world", World.class);
                                    new DefaultWorldNameCommand(plugin).setName(ctx.getSource().getSender(), world);
                                    return Command.SINGLE_SUCCESS;
                                })))
                .then(Commands.literal("respect_towny")
                        .then(Commands.argument("option", new TownyArgumentType())
                                .executes(ctx -> {
                                    String o = ctx.getArgument("option", String.class);
                                    new SetRespectCommand(plugin).setRegion(ctx.getSource().getSender(), o);
                                    return Command.SINGLE_SUCCESS;
                                })))
                .then(Commands.literal("respect_worldguard")
                        .then(Commands.argument("option", new WorldGuardArgumentType(plugin))
                                .executes(ctx -> {
                                    String o = ctx.getArgument("option", String.class);
                                    new SetRespectCommand(plugin).setFlag(ctx.getSource().getSender(), o);
                                    return Command.SINGLE_SUCCESS;
                                })))
                .then(Commands.literal("crafting")
                        .then(Commands.argument("option", new DifficultyArgumentType())
                                .executes(ctx -> {
                                    String o = ctx.getArgument("option", String.class);
                                    plugin.getConfig().set("difficulty.crafting", o.toLowerCase(Locale.ROOT));
                                    plugin.setDifficulty(CraftingDifficulty.valueOf(o.toUpperCase(Locale.ROOT)));
                                    plugin.saveConfig();
                                    return Command.SINGLE_SUCCESS;
                                })))
                .then(Commands.literal("default_preset")
                        .then(Commands.argument("option", new PresetArgumentType(0))
                                .executes(ctx -> {
                                    String o = ctx.getArgument("option", String.class);
                                    if (plugin.getCustomModelConfig().getConfigurationSection("models").getKeys(false).contains(o)) {
                                        plugin.getConfig().set("police_box.default_preset", "ITEM:" + o);
                                    } else {
                                        plugin.getConfig().set("police_box.default_preset", o.toUpperCase(Locale.ROOT));
                                    }
                                    plugin.saveConfig();
                                    return Command.SINGLE_SUCCESS;
                                })))
                .then(Commands.literal("use_clay")
                        .then(Commands.argument("option", new UseClayArgumentType())
                                .executes(ctx -> {
                                    String o = ctx.getArgument("option", String.class).toUpperCase(Locale.ROOT);
                                    plugin.getConfig().set("creation.use_clay", o);
                                    return Command.SINGLE_SUCCESS;
                                })))
                .then(Commands.literal("vortex_fall")
                        .then(Commands.argument("option", new VortexFallArgumentType())
                                .executes(ctx -> {
                                    String o = ctx.getArgument("option", String.class);
                                    plugin.getConfig().set("preferences.vortex_fall", o);
                                    return Command.SINGLE_SUCCESS;
                                })))
                // TODO add literals for options with special tab completion
                // "provider" "region_flag" "tips_next" "tips_limit"
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
                                    // TODO sort out string arguments from literals that are just arguments for setConfigString()
                                    return Command.SINGLE_SUCCESS;
                                }))
                );
        return command.build();
    }
}
