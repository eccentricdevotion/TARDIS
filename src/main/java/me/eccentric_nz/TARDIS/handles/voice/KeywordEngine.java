package me.eccentric_nz.TARDIS.handles.voice;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.handles.HandlesPattern;
import me.eccentric_nz.TARDIS.handles.HandlesRequest;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.vosk.Model;
import org.vosk.Recognizer;

import java.io.File;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class KeywordEngine {

    private static Model model;
    private static ExecutorService voicePool;
    private static final ConcurrentHashMap<UUID, Recognizer> recognizers = new ConcurrentHashMap<>();
    private static final Pattern prefix = HandlesPattern.getPattern("prefix", false);

    public static void init() {
        voicePool = Executors.newSingleThreadExecutor(r -> {
            Thread t = new Thread(r, "TARDIS-Voice-Worker");
            t.setDaemon(true);
            return t;
        });
        File voiceDir = new File(TARDIS.plugin.getDataFolder(), "voice_chat");
        File targetModelDir = new File(voiceDir, "vosk-model");
        try {
            model = new Model(targetModelDir.getAbsolutePath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        TARDIS.plugin.getMessenger().message(TARDIS.plugin.getConsole(), TardisModule.HANDLES, "Voice processing system successfully initialized!");
    }

    public static void processAudioAsync(UUID playerUUID, byte[] pcm16kAudio) {
        if (model == null || voicePool.isShutdown()) {
            return;
        }
        voicePool.submit(() -> {
            try {
                try (Recognizer recognizer = recognizers.computeIfAbsent(playerUUID, uuid -> {
                    try {
                        // configured to expect a 16000Hz downsampled target audio
                        return new Recognizer(model, 16000.0f);
                    } catch (Exception e) {
                        return null;
                    }
                })) {
                    if (recognizer == null) {
                        return;
                    }
                    // acceptWaveForm pushes chunk data to the parser pipeline
                    if (recognizer.acceptWaveForm(pcm16kAudio, pcm16kAudio.length)) {
                        handleJsonResult(playerUUID, recognizer.getResult());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private static void handleJsonResult(UUID playerUUID, String json) {
        // make sure we have a "text" token
        if (!json.contains("\"text\" :")) {
            return;
        }
        String cleanText = json.substring(json.indexOf("\"text\" :") + 8);
        cleanText = cleanText.substring(cleanText.indexOf("\"") + 1, cleanText.lastIndexOf("\"")).trim();
        if (cleanText.isEmpty()) {
            return;
        }
        // parsed by your config patterns
        routeSpeech(playerUUID, cleanText);
    }

    private static void routeSpeech(UUID playerUUID, String transcript) {
        Matcher prefixMatcher = prefix.matcher(transcript);
        // verify the voice chat begins with the prefix (e.g. "hey handles")
        if (!prefixMatcher.find()) {
            return;
        }
        // handoff to Paper's main thread
        Bukkit.getScheduler().runTask(TARDIS.plugin, () -> {
            Player player = Bukkit.getPlayer(playerUUID);
            if (player == null) {
                return;
            }
            new HandlesRequest(TARDIS.plugin).process(playerUUID, transcript);
        });
    }

    public static void removePlayer(UUID playerUUID) {
        Recognizer r = recognizers.remove(playerUUID);
        if (r != null) {
            r.close(); // Clean native pointer mappings
        }
    }

    public static void shutdown() {
        if (voicePool != null) {
            voicePool.shutdown();
        }
        recognizers.forEach((uuid, r) -> r.close());
        recognizers.clear();
        if (model != null) {
            model.close();
        }
    }
}
