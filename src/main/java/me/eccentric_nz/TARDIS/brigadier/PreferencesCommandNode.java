package me.eccentric_nz.TARDIS.brigadier;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.brigadier.arguments.*;
import me.eccentric_nz.TARDIS.commands.preferences.*;
import me.eccentric_nz.TARDIS.playerprefs.TARDISKeyMenuInventory;
import me.eccentric_nz.TARDIS.playerprefs.TARDISSonicMenuInventory;
import me.eccentric_nz.TARDIS.utility.ComponentUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import java.util.List;

public class PreferencesCommandNode {

    private final TARDIS plugin;

    public PreferencesCommandNode(TARDIS plugin) {
        this.plugin = plugin;
    }

    LiteralCommandNode<CommandSourceStack> build() {
        LiteralArgumentBuilder<CommandSourceStack> command = Commands.literal("tardisprefs")
                .requires(ctx -> ctx.getSender() instanceof Player)
                .then(Commands.literal("sonic")
                        .executes(ctx -> {
                            Player player = (Player) ctx.getSource().getSender();
                            player.openInventory(new TARDISSonicMenuInventory(plugin).getInventory());
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("key_menu")
                        .executes(ctx -> {
                            Player player = (Player) ctx.getSource().getSender();
                            player.openInventory(new TARDISKeyMenuInventory(plugin).getInventory());
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("console_labels")
                        .then(Commands.argument("toggle", new OnOffArgumentType())
                                .executes(ctx -> {
                                    Player player = (Player) ctx.getSource().getSender();
                                    String on_off = ctx.getArgument("toggle", String.class);
                                    new LabelsCommand(plugin).toggle(player, on_off);
                                    return Command.SINGLE_SUCCESS;
                                })))
                .then(Commands.literal("eps_message")
                        .executes(ctx -> {
                            Player player = (Player) ctx.getSource().getSender();
                            ItemStack bq = player.getInventory().getItemInMainHand();
                            if (bq.getType().equals(Material.WRITABLE_BOOK) || bq.getType().equals(Material.WRITTEN_BOOK)) {
                                BookMeta bm = (BookMeta) bq.getItemMeta();
                                List<Component> pages = bm.pages();
                                StringBuilder sb = new StringBuilder();
                                pages.forEach((s) -> sb.append(ComponentUtils.stripColour(s)).append(" "));
                                new EPSMessageCommand(plugin).setMessage(player, sb.toString());
                            }
                            return Command.SINGLE_SUCCESS;
                        })
                        .then(Commands.argument("text", StringArgumentType.greedyString())
                                .executes(ctx -> {
                                    Player player = (Player) ctx.getSource().getSender();
                                    String t = StringArgumentType.getString(ctx, "text");
                                    new EPSMessageCommand(plugin).setMessage(player, t);
                                    return Command.SINGLE_SUCCESS;
                                })))
                .then(Commands.literal("flight")
                        .then(Commands.argument("mode", new FlightModeArgumentType())
                                .executes(ctx -> {
                                    Player player = (Player) ctx.getSource().getSender();
                                    String m = ctx.getArgument("mode", String.class);
                                    new SetFlightCommand(plugin).setMode(player, m);
                                    return Command.SINGLE_SUCCESS;
                                })))
                .then(Commands.literal("hads_type")
                        .then(Commands.argument("mode", new HADSArgumentType())
                                .executes(ctx -> {
                                    Player player = (Player) ctx.getSource().getSender();
                                    String m = ctx.getArgument("mode", String.class);
                                    new HadsTypeCommand(plugin).setHadsPref(player, m);
                                    return Command.SINGLE_SUCCESS;
                                })))
                .then(Commands.literal("hum")
                        .then(Commands.argument("sound", new HumArgumentType())
                                .executes(ctx -> {
                                    Player player = (Player) ctx.getSource().getSender();
                                    String h = ctx.getArgument("sound", String.class);
                                    new HumCommand(plugin).setHumPref(player, h);
                                    return Command.SINGLE_SUCCESS;
                                })))
                .then(Commands.literal("isomorphic")
                        .executes(ctx -> {
                            Player player = (Player) ctx.getSource().getSender();
                            new IsomorphicCommand(plugin).toggleIsomorphicControls(player);
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("key")
                        .then(Commands.argument("type", ArgumentTypes.itemStack())
                                .executes(ctx -> {
                                    Player player = (Player) ctx.getSource().getSender();
                                    ItemStack m = ctx.getArgument("type", ItemStack.class);
                                    new SetKeyCommand(plugin).setKeyPref(player, m.getType().toString());
                                    return Command.SINGLE_SUCCESS;
                                })))
                .then(Commands.literal("language")
                        .then(Commands.argument("option", new TranslatableLanguageArgumentType()))
                        .executes(ctx -> {
                            Player player = (Player) ctx.getSource().getSender();
                            String l = ctx.getArgument("option", String.class);
                            LanguageUtility.set(plugin, player, l);
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("translate")
                        .then(Commands.literal("off")
                                .executes(ctx -> {
                                    Player player = (Player) ctx.getSource().getSender();
                                    LanguageUtility.translateOff(plugin, player);
                                    return Command.SINGLE_SUCCESS;
                                }))
                        .then(Commands.argument("from", new TranslatableLanguageArgumentType())
                                .then(Commands.argument("to", new TranslatableLanguageArgumentType())
                                        .then(Commands.argument("player", ArgumentTypes.player())
                                                .executes(ctx -> {
                                                    Player player = (Player) ctx.getSource().getSender();
                                                    PlayerSelectorArgumentResolver targetResolver = ctx.getArgument("player", PlayerSelectorArgumentResolver.class);
                                                    Player receiver = targetResolver.resolve(ctx.getSource()).getFirst();
                                                    String from = ctx.getArgument("from", String.class);
                                                    String to = ctx.getArgument("to", String.class);
                                                    LanguageUtility.translateOn(plugin, player, receiver, from, to);
                                                    return Command.SINGLE_SUCCESS;
                                                })))))
                .then(Commands.literal("lights")
                        .then(Commands.argument("type", new LightArgumentType())
                                .executes(ctx -> {
                                    Player player = (Player) ctx.getSource().getSender();
                                    String l = ctx.getArgument("type", String.class);
                                    new LightsCommand(plugin).setLightsPref(player, l);
                                    return Command.SINGLE_SUCCESS;
                                })))
                .then(Commands.literal("wall")
                        .then(Commands.argument("type", new WallFloorArgumentType())
                                .executes(ctx -> {
                                    Player player = (Player) ctx.getSource().getSender();
                                    String t = ctx.getArgument("type", String.class);
                                    new FloorCommand(plugin).setFloorOrWallBlock(player, "wall", t);
                                    return Command.SINGLE_SUCCESS;
                                })))
                .then(Commands.literal("floor")
                        .then(Commands.argument("type", new WallFloorArgumentType())
                                .executes(ctx -> {
                                    Player player = (Player) ctx.getSource().getSender();
                                    String t = ctx.getArgument("type", String.class);
                                    new FloorCommand(plugin).setFloorOrWallBlock(player, "floor", t);
                                    return Command.SINGLE_SUCCESS;
                                })))
                .then(Commands.literal("siege_wall")
                        .then(Commands.argument("type", new WallFloorArgumentType())
                                .executes(ctx -> {
                                    Player player = (Player) ctx.getSource().getSender();
                                    String t = ctx.getArgument("type", String.class);
                                    new FloorCommand(plugin).setFloorOrWallBlock(player, "siege_wall", t);
                                    return Command.SINGLE_SUCCESS;
                                })))
                .then(Commands.literal("siege_floor")
                        .then(Commands.argument("type", new WallFloorArgumentType())
                                .executes(ctx -> {
                                    Player player = (Player) ctx.getSource().getSender();
                                    String t = ctx.getArgument("type", String.class);
                                    new FloorCommand(plugin).setFloorOrWallBlock(player, "siege_floor", t);
                                    return Command.SINGLE_SUCCESS;
                                })))
                .then(Commands.literal("effect")
                        .then(Commands.argument("type", new ParticleEffectArgumentType()))
                        .executes(ctx -> {
                            Player player = (Player) ctx.getSource().getSender();
                            String e = ctx.getArgument("type", String.class);
                            ParticleUtility.setEffect(plugin, player, e);
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("shape")
                        .then(Commands.argument("type", new ParticleShapeArgumentType())
                                .executes(ctx -> {
                                    Player player = (Player) ctx.getSource().getSender();
                                    String s = ctx.getArgument("type", String.class);
                                    ParticleUtility.setShape(plugin, player, s);
                                    return Command.SINGLE_SUCCESS;
                                })))
                .then(Commands.literal("speed")
                        .then(Commands.argument("number", DoubleArgumentType.doubleArg(0))
                                .executes(ctx -> {
                                    Player player = (Player) ctx.getSource().getSender();
                                    double s = DoubleArgumentType.getDouble(ctx, "number");
                                    ParticleUtility.setSpeed(plugin, player, s);
                                    return Command.SINGLE_SUCCESS;
                                })))
                .then(Commands.literal("silence_mobs")
                        .then(Commands.argument("toggle", new OnOffArgumentType())
                                .executes(ctx -> {
                                    Player player = (Player) ctx.getSource().getSender();
                                    String o = ctx.getArgument("toggle", String.class);
                                    new SilenceMobsCommand(plugin).toggle(player, o);
                                    return Command.SINGLE_SUCCESS;
                                })))
                .then(Commands.literal("build")
                        .then(Commands.argument("toggle", new OnOffArgumentType())
                                .executes(ctx -> {
                                    Player player = (Player) ctx.getSource().getSender();
                                    String o = ctx.getArgument("toggle", String.class);
                                    new BuildCommand(plugin).toggleCompanionBuilding(player, o);
                                    return Command.SINGLE_SUCCESS;
                                })))
                .then(Commands.literal("junk")
                        .then(Commands.argument("toggle", new OnOffArgumentType())
                                .executes(ctx -> {
                                    Player player = (Player) ctx.getSource().getSender();
                                    String o = ctx.getArgument("toggle", String.class);
                                    new JunkPreference(plugin).toggle(player, o);
                                    return Command.SINGLE_SUCCESS;
                                })))
                .then(Commands.argument("preference", new PreferencesArgumentType())
                        .then(Commands.argument("toggle", StringArgumentType.word())
                                .executes(ctx -> {
                                    Player player = (Player) ctx.getSource().getSender();
                                    String p = ctx.getArgument("preference", String.class);
                                    String o = ctx.getArgument("toggle", String.class);
                                    new ToggleOnOffCommand(plugin).toggle(player, p, o);
                                    return Command.SINGLE_SUCCESS;
                                })));
        return command.build();
    }
}
