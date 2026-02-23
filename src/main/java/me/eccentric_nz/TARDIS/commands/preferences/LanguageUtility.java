package me.eccentric_nz.TARDIS.commands.preferences;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.universaltranslator.Language;
import me.eccentric_nz.TARDIS.universaltranslator.TranslateData;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class LanguageUtility {

    public static void set(TARDIS plugin, Player player, String language) {
        try {
            Language.valueOf(language);
        } catch (IllegalArgumentException e) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "LANG_NOT_VALID");
            return;
        }
        HashMap<String, Object> setl = new HashMap<>();
        setl.put("language", language);
        HashMap<String, Object> where = new HashMap<>();
        where.put("uuid", player.getUniqueId().toString());
        plugin.getQueryFactory().doUpdate("player_prefs", setl, where);
        plugin.getMessenger().send(player, TardisModule.TARDIS, "PREF_SET", "Language");
    }

    public static void translateOff(TARDIS plugin, Player player) {
        plugin.getTrackerKeeper().getTranslators().remove(player.getUniqueId());
        plugin.getMessenger().send(player, TardisModule.TARDIS, "TRANSLATE_OFF");
    }

    public static void translateOn(TARDIS plugin, Player player, Player receiver, String from, String to) {
        Language first;
        Language second;
        try {
            first = Language.valueOf(from);
            second = Language.valueOf(to);
        } catch (IllegalArgumentException e) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "LANG_NOT_VALID");
            return;
        }
        String name = receiver.getName();
        TranslateData data = new TranslateData(first, second, name);
        plugin.getTrackerKeeper().getTranslators().put(player.getUniqueId(), data);
        plugin.getMessenger().send(player, TardisModule.TARDIS, "TRANSLATE_ON", name);
    }
}
