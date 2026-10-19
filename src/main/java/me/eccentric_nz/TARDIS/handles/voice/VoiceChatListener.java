package me.eccentric_nz.TARDIS.handles.voice;

import de.maxhenkel.voicechat.api.VoicechatPlugin;
import de.maxhenkel.voicechat.api.VoicechatServerApi;
import de.maxhenkel.voicechat.api.events.EventRegistration;
import de.maxhenkel.voicechat.api.events.MicrophonePacketEvent;
import de.maxhenkel.voicechat.api.opus.OpusDecoder;
import de.maxhenkel.voicechat.api.packets.MicrophonePacket;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class VoiceChatListener implements VoicechatPlugin {

    // cache decoders per player to protect CPU/RAM and maintain Opus state tracking
    private static final ConcurrentHashMap<UUID, OpusDecoder> playerDecoders = new ConcurrentHashMap<>();

    @Override
    public String getPluginId() {
        return "tardis";
    }

    @Override
    public void registerEvents(EventRegistration registration) {
        registration.registerEvent(MicrophonePacketEvent.class, this::onPlayerAudio);
    }

    private void onPlayerAudio(MicrophonePacketEvent event) {
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
        // convert short[] down to a 16kHz byte[] stream for Vosk
        byte[] pcm16kBytes = downsample48ShortsTo16kBytes(pcm48kShorts);
        // process the audio
        KeywordEngine.processAudioAsync(playerUUID, pcm16kBytes);
    }

    /**
     * Simultaneously downsamples 48kHz audio to 16kHz (3:1) and converts 16-bit
     * shorts directly into small linear PCM bytes required by Vosk.
     */
    private byte[] downsample48ShortsTo16kBytes(short[] src) {
        // Every 3 short samples become 1 short sample (2 bytes)
        int destSamples = src.length / 3;
        byte[] destBytes = new byte[destSamples * 2];
        int byteIndex = 0;

        for (int i = 0; i < src.length; i += 3) {
            if (byteIndex + 1 < destBytes.length) {
                short sample = src[i];
                // Extract Little-Endian bytes out of the 16-bit audio short
                destBytes[byteIndex] = (byte) (sample & 0xFF);
                destBytes[byteIndex + 1] = (byte) ((sample >> 8) & 0xFF);
                byteIndex += 2;
            }
        }
        return destBytes;
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
}
