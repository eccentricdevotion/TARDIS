package me.eccentric_nz.TARDIS.handles.voice;

import de.maxhenkel.voicechat.api.BukkitVoicechatService;
import me.eccentric_nz.TARDIS.TARDIS;

public class Voice {

    public void enable(TARDIS plugin) {
        BukkitVoicechatService service = plugin.getServer().getServicesManager().load(BukkitVoicechatService.class);
        if (service != null) {
            service.registerPlugin(new VoiceChatListener(plugin));
            plugin.getMessenger().handlesMessage(plugin.getConsole(), "Successfully connected TARDIS to Simple Voice Chat!");
        } else {
            plugin.getMessenger().handlesMessage(plugin.getConsole(), "Failed to get Voice Chat Service!");
        }
    }

    public void disable() {
        KeywordEngine.shutdown();
    }
}
