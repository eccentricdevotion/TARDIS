package me.eccentric_nz.TARDIS.handles.voice;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.handles.HandlesPattern;
import me.eccentric_nz.TARDIS.handles.HandlesRequest;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.vosk.Model;
import org.vosk.Recognizer;

import java.io.File;
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
    private static final ConcurrentHashMap<UUID, Long> lastSpeechTimestamps = new ConcurrentHashMap<>();
    private static final Pattern prefix = HandlesPattern.getPattern("prefix", false);
    private static String grammar;

    public static void init(TARDIS plugin) {
        Grammar g = new Grammar(plugin);
        grammar = g.getJSON();
        voicePool = Executors.newSingleThreadExecutor(r -> {
            Thread t = new Thread(r, "TARDIS-Voice-Worker");
            t.setDaemon(true);
            return t;
        });
        File voiceDir = new File(plugin.getDataFolder(), "voice_chat");
        File targetModelDir = new File(voiceDir, "vosk-model");
        try {
            model = new Model(targetModelDir.getPath());
            VoiceChatListener.setModelReady();
        } catch (Throwable t) {
            plugin.getMessenger().handlesMessage(plugin.getConsole(), "Could not set Vosk model from path!");
            plugin.getMessenger().handlesMessage(plugin.getConsole(), "!!! NATIVE ENGAGE FAILURE !!!");
            plugin.getMessenger().handlesMessage(plugin.getConsole(), t.getClass().getName() + ": " + t.getMessage());
            t.printStackTrace();
        }
        // start task to check if speech is finished
        Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, () -> {
            long now = System.currentTimeMillis();
            lastSpeechTimestamps.forEach((playerUUID, lastActive) -> {
                if (now - lastActive > 800) { // 800 milliseconds of no packets = sentence finished
                    // player has been silent for longer than the threshold, so stop tracking until they talk again
                    lastSpeechTimestamps.remove(playerUUID);
                    Recognizer recognizer = recognizers.get(playerUUID);
                    if (recognizer != null) {
                        // force Vosk to compile whatever it heard into a final string payload
                        String json = recognizer.getResult();
                        // Route this straight into your command processing logic!
                        if (json != null && !json.isEmpty()) {
                            handleJsonResult(playerUUID, json);
                        }
                    }
                }
            });
        }, 10L, 10L);
        plugin.getMessenger().handlesMessage(plugin.getConsole(), "Voice processing system successfully initialized!");
    }

    public static void processAudioAsync(UUID playerUUID, short[] incomingAudio) {
        if (model == null || voicePool.isShutdown()) {
            return;
        }
        lastSpeechTimestamps.put(playerUUID, System.currentTimeMillis());
        voicePool.submit(() -> {
            try {
                Recognizer recognizer = recognizers.computeIfAbsent(playerUUID, uuid -> {
                    try {
                        TARDIS.plugin.getMessenger().handlesMessage(TARDIS.plugin.getConsole(), "Creating new Recognizer for: " + uuid);
                        return new Recognizer(model, 48000.0f, grammar);
                    } catch (Exception e) {
                        TARDIS.plugin.getMessenger().handlesMessage(TARDIS.plugin.getConsole(), "Failed to create recognizer: " + e.getMessage());
                        return null;
                    }
                });
                if (recognizer == null) {
                    return;
                }
                // detect channels
                boolean isStereo = incomingAudio.length > 1000;
                int stepStride = isStereo ? 2 : 1; // Stereo strips right channel. Mono processes every sample.
                int targetLength = incomingAudio.length / stepStride;
                if (targetLength <= 0) {
                    return;
                }
                byte[] voskBytes = new byte[targetLength * 2];
                for (int i = 0; i < targetLength; i++) {
                    int sourceIndex = i * stepStride;
                    if (sourceIndex >= incomingAudio.length) {
                        break;
                    }
                    short sample = incomingAudio[sourceIndex];
                    // Little-Endian bit-packing
                    voskBytes[i * 2] = (byte) (sample & 0xFF);
                    voskBytes[i * 2 + 1] = (byte) ((sample >> 8) & 0xFF);
                }
                // acceptWaveForm pushes chunk data to the parser pipeline
                if (recognizer.acceptWaveForm(voskBytes, voskBytes.length)) {
                    handleJsonResult(playerUUID, recognizer.getResult());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private static void handleJsonResult(UUID playerUUID, String json) {
        TARDIS.plugin.debug("Handling JSON result");
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
        TARDIS.plugin.debug("Routing speech for transcript: " + transcript);
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
