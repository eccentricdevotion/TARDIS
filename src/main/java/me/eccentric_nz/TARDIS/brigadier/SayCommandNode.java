package me.eccentric_nz.TARDIS.brigadier;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.brigadier.arguments.TranslatableLanguageArgumentType;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetPlayerPrefs;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.universaltranslator.Language;
import me.eccentric_nz.TARDIS.universaltranslator.LingvaTranslate;
import org.bukkit.command.CommandException;
import org.bukkit.entity.Player;

public class SayCommandNode {

    private final TARDIS plugin;

    public SayCommandNode(TARDIS plugin) {
        this.plugin = plugin;
    }

    LiteralCommandNode<CommandSourceStack> build() {
        LiteralArgumentBuilder<CommandSourceStack> command = Commands.literal("tardissay")
                .requires(ctx -> TARDISPermission.hasPermission(ctx.getSender(), "tardis.translate"))
                .then(Commands.argument("language", new TranslatableLanguageArgumentType())
                        .then(Commands.argument("message", StringArgumentType.greedyString())
                                .executes(ctx -> {
                                    String preferred = getPreferredLanguage(ctx);
                                    String lang = ctx.getArgument("language", String.class);
                                    String whatToTranslate = StringArgumentType.getString(ctx, "message");
                                    try {
                                        Language from = Language.valueOf(preferred);
                                        Language to = Language.valueOf(lang);
                                        try {
                                            LingvaTranslate translate = new LingvaTranslate(plugin, from.getCode(), to.getCode(), whatToTranslate);
                                            translate.fetchAsync((hasResult, translated) -> {
                                                if (hasResult) {
                                                    plugin.getServer().dispatchCommand(ctx.getSource().getSender(), "say [" + TardisModule.TRANSLATOR.getName() + "] " + translated.getTranslated());
                                                }
                                            });
                                            return Command.SINGLE_SUCCESS;
                                        } catch (CommandException ex) {
                                            plugin.debug("Could not get translation! " + ex.getMessage());
                                            plugin.getMessenger().send(ctx.getSource().getSender(), TardisModule.TRANSLATOR, "YT_UNAVAILABLE");
                                        }
                                    } catch (IllegalArgumentException e) {
                                        plugin.getMessenger().send(ctx.getSource().getSender(), TardisModule.TRANSLATOR, "LANG_NOT_VALID");
                                    }
                                    return Command.SINGLE_SUCCESS;
                                })));
        return command.build();
    }

    private String getPreferredLanguage(CommandContext<CommandSourceStack> ctx) {
        String preferedLang = "ENGLISH";
        if (ctx.getSource().getSender() instanceof Player player) {
            ResultSetPlayerPrefs rs = new ResultSetPlayerPrefs(plugin, player.getUniqueId().toString());
            if (rs.resultSet() && !rs.getLanguage().isEmpty()) {
                if (!rs.getLanguage().equalsIgnoreCase("AUTO_DETECT")) {
                    preferedLang = rs.getLanguage();
                }
            }
        }
        return preferedLang;
    }
}
