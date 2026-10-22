package me.eccentric_nz.TARDIS.handles.voice;

import de.maxhenkel.voicechat.api.VoicechatApi;
import de.maxhenkel.voicechat.api.VoicechatPlugin;
import de.maxhenkel.voicechat.api.VoicechatServerApi;
import de.maxhenkel.voicechat.api.events.EventRegistration;
import de.maxhenkel.voicechat.api.events.MicrophonePacketEvent;
import de.maxhenkel.voicechat.api.opus.OpusDecoder;
import de.maxhenkel.voicechat.api.packets.MicrophonePacket;
import me.eccentric_nz.TARDIS.TARDIS;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class VoiceChatListener implements VoicechatPlugin {

    // cache decoders per player to protect CPU/RAM and maintain Opus state tracking
    private static final ConcurrentHashMap<UUID, OpusDecoder> playerDecoders = new ConcurrentHashMap<>();
    private static boolean modelReady = false;
    private final TARDIS plugin;

    public VoiceChatListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getPluginId() {
        return "tardis";
    }

    @Override
    public void initialize(VoicechatApi api) {
        new ModelDownloader(plugin).get().thenAccept(success -> {
            // runs only after the file is either verified or downloaded + unzipped
            if (!success) {
                plugin.getMessenger().handlesMessage(plugin.getConsole(), "Voice processing disabled because the Vosk model failed to load.");
            } else {
                // only do the following if the model is present
                KeywordEngine.init(plugin);
                modelReady = true;
                plugin.setVoiceChatEnabled();
                plugin.getMessenger().handlesMessage(plugin.getConsole(), "Vosk model loaded successfully.");
            }
        });
    }

    @Override
    public void registerEvents(EventRegistration registration) {
        registration.registerEvent(MicrophonePacketEvent.class, this::onPlayerAudio);
    }

    private void onPlayerAudio(MicrophonePacketEvent event) {
        if (!modelReady) {
            return;
        }
        if (!(event.getPacket() instanceof MicrophonePacket microphonePacket)) {
            return;
        }
        if (event.getSenderConnection() == null || event.getSenderConnection().getPlayer() == null) {
            return;
        }
        UUID playerUUID = event.getSenderConnection().getPlayer().getUuid();
        VoicechatServerApi api = event.getVoicechat();
        if (api == null) {
            return;
        }
        // fetch or create a dedicated OpusDecoder
        OpusDecoder decoder = playerDecoders.computeIfAbsent(playerUUID, uuid -> api.createDecoder());
        if (decoder == null || decoder.isClosed()) {
            return;
        }
        byte[] opusAudio = microphonePacket.getOpusEncodedData();
        short[] pcm48kShorts = decoder.decode(opusAudio);
        if (pcm48kShorts == null || pcm48kShorts.length == 0) {
            return;
        }
        // process the audio
        KeywordEngine.processAudioAsync(playerUUID, pcm48kShorts);
    }

    /**
     * Clean up native memory mapping pointers immediately upon disconnects.
     * Call this from your main JavaPlugin PlayerQuitEvent!
     */
    public static void clearPlayerDecoder(UUID playerUUID) {
        OpusDecoder decoder = playerDecoders.remove(playerUUID);
        if (decoder != null && !decoder.isClosed()) {
            decoder.close(); // Crucial step to stop native heap memory leaks!
        }
    }

    public static void setModelReady() {
        modelReady = true;
    }
}
