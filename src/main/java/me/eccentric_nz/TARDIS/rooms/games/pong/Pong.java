package me.eccentric_nz.TARDIS.rooms.games.pong;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetGames;
import me.eccentric_nz.TARDIS.rooms.games.ArcadeData;
import me.eccentric_nz.TARDIS.rooms.games.ArcadeTracker;
import me.eccentric_nz.TARDIS.rooms.games.GameState;
import me.eccentric_nz.TARDIS.rooms.games.GameUtils;
import me.eccentric_nz.TARDIS.utility.TARDISStaticLocationGetters;
import net.kyori.adventure.text.Component;
import org.bukkit.Chunk;
import org.bukkit.Input;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.TextDisplay;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInputEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class Pong implements Listener {

    private final TARDIS plugin;
    private final Player player;
    private List<TextDisplay> displayList;
    private Location playerLocation;
    private int id = -1;
    private char[][] CANVAS;
    private GameState state = GameState.INITIALIZING;
    private int tickTask = -1;
    private int paddleY = 8;
    private int tardisY = 8;
    private Ball ball;

    public Pong(TARDIS plugin, Player player) {
        this.plugin = plugin;
        this.player = player;
        startGame();
    }

    public char[][] getCANVAS() {
        return CANVAS;
    }

    public void startGame() {
        // teleport to ARCADE room
        getRoom(player);
        ArcadeTracker.PLAYERS.put(player.getUniqueId(), new ArcadeData(GameUtils.centre(player.getLocation()), player.getAllowFlight(), this, id));
        // load the text displays chunk
        Chunk displayChunk = playerLocation.getWorld().getChunkAt(playerLocation.getBlock().getRelative(BlockFace.NORTH, 8));
        while (!displayChunk.isLoaded()) {
            displayChunk.load();
        }
        player.setFallDistance(0.0f);
        player.teleport(playerLocation);
        // get armour stand and put player in it
        for (Entity e : playerLocation.getChunk().getEntities()) {
            if (e instanceof ArmorStand stand) {
                stand.setRotation(180.0f, 0);
                stand.addPassenger(player);
            }
        }
        player.setFallDistance(0.0f);
        if (state == GameState.INITIALIZING) {
            // get the text displays
            load();
            // reset the canvas to new game
            reset();
            state = GameState.PLAYING;
        }
    }

    private void getRoom(Player player) {
        // get the id of the TARDIS the player is in
        id = plugin.getTardisAPI().getIdOfTARDISPlayerIsIn(player.getUniqueId());
        if (id != -1) {
            // get game record
            ResultSetGames rsg = new ResultSetGames(plugin);
            if (rsg.fromId(id)) {
                // get the player location
                String location = rsg.getPlayerLocation();
                playerLocation = TARDISStaticLocationGetters.getLocationFromBukkitString(location);
                playerLocation.setYaw(180.0f);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerInput(PlayerInputEvent event) {
        Player player = event.getPlayer();
        if (!ArcadeTracker.PLAYERS.containsKey(player.getUniqueId())) {
            return;
        }
        Entity entity = player.getVehicle();
        if (entity instanceof ArmorStand) {
            Input input = event.getInput();
            if (input.isSneak()) {
                abort();
            }
            if (input.isJump()) {
                if (tickTask == -1) {
                    startTick();
                } else {
                    pauseTick();
                }
            }
            if (input.isForward()) {
                paddleUp();
            }
            if (input.isBackward()) {
                paddleDown();
            }
        }
    }

    private void startTick() {
        tickTask = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
            draw();
            ball.move();
        }, 15L, 15L);
    }

    private void pauseTick() {
        plugin.getServer().getScheduler().cancelTask(tickTask);
        tickTask = -1;
    }

    private void paddleUp() {
        // upper bound = 1
        paddleY--;
        if (paddleY < 1) {
            paddleY = 1;
        }
        for (int p = 0; p < 16; p++) {
            CANVAS[p][1] = GameChar.space;
        }
        CANVAS[paddleY - 1][1] = GameChar.paddle;
        CANVAS[paddleY][1] = GameChar.paddle;
        CANVAS[paddleY + 1][1] = GameChar.paddle;
    }

    private void paddleDown() {
        // lower bound = 14
        paddleY++;
        if (paddleY > 14) {
            paddleY = 14;
        }
        for (int p = 0; p < 16; p++) {
            CANVAS[p][1] = GameChar.space;
        }
        CANVAS[paddleY + 1][1] = GameChar.paddle;
        CANVAS[paddleY][1] = GameChar.paddle;
        CANVAS[paddleY - 1][1] = GameChar.paddle;
    }

    private void tardisUp() {
        // upper bound = 1
        tardisY--;
        if (tardisY < 1) {
            tardisY = 1;
        }
        for (int p = 0; p < 16; p++) {
            CANVAS[p][27] = GameChar.space;
        }
        CANVAS[tardisY - 1][27] = GameChar.paddle;
        CANVAS[tardisY][27] = GameChar.paddle;
        CANVAS[tardisY + 1][27] = GameChar.paddle;
    }

    private void tardisDown() {
        // lower bound = 14
        tardisY++;
        if (tardisY > 14) {
            tardisY = 14;
        }
        for (int p = 0; p < 16; p++) {
            CANVAS[p][27] = GameChar.space;
        }
        CANVAS[tardisY + 1][27] = GameChar.paddle;
        CANVAS[tardisY][27] = GameChar.paddle;
        CANVAS[tardisY - 1][27] = GameChar.paddle;
    }

    private void draw() {
        if (state == GameState.PLAYING) {
            tardisPaddle();
        }
        for (int i = 0; i < 16; i++) {
            char[] chars = CANVAS[i];
            String line = new String(chars);
            if (state == GameState.INITIALIZING) {
                plugin.debug(line);
            }
            TextDisplay display = displayList.get(i);
            display.text(Component.text(line));
        }
    }

    private void tardisPaddle() {
        // move paddle towards ball
        if (tardisY < ball.by) {
            tardisDown();
        } else if (tardisY > ball.by) {
            tardisUp();
        }
    }

    protected void reset() {
        if (state != GameState.INITIALIZING) {
            pauseTick();
        }
        plugin.debug("reset");
        this.CANVAS = new char[16][29];
        for (int i = 0; i < 16; i++) {
            char[] line = Lines.CANVAS[i];
            this.CANVAS[i] = line;
        }
        int row = TARDISConstants.RANDOM.nextInt(2, 14);
        this.CANVAS[row][14] = GameChar.ball;
        paddleY = 8;
        tardisY = 8;
        if (state == GameState.INITIALIZING) {
            ball = new Ball(this, row);
        }
        ball.by = row;
        ball.bx = 14;
        ball.originY = row;
        ball.originX = 14;
        double tmp = TARDISConstants.RANDOM.nextDouble(-ball.limit, ball.limit);
        if (tmp < 0) {
            tmp = (Math.PI * 2) + tmp;
        }
        ball.angle = tmp;
        draw();
        if (state != GameState.INITIALIZING) {
            startTick();
        }
    }

    private void load() {
        // get the pong_ids
        ResultSetGames rsg = new ResultSetGames(plugin);
        if (rsg.fromId(id)) {
            displayList = new ArrayList<>(16);
            for (UUID uuid : rsg.getPongUUIDs()) {
                TextDisplay display = (TextDisplay) playerLocation.getWorld().getEntity(uuid);
                displayList.add(display);
                if (display == null) {
                    plugin.debug("Could not get text display -> " + uuid);
                    abort();
                    return;
                }
            }
        } else {
            // couldn't get text displays
            // return player to TARDIS
            abort();
        }
    }

    private void abort() {
        state = GameState.INITIALIZING;
        plugin.getServer().getScheduler().cancelTask(tickTask);
        reset();
        UUID uuid = player.getUniqueId();
        ArcadeData data = ArcadeTracker.PLAYERS.get(uuid);
        // teleport player back to games room
        player.setFallDistance(0.0f);
        player.teleport(data.backup());
        player.setFallDistance(0.0f);
        // reset flight status
        player.setAllowFlight(data.allowFlight());
        HandlerList.unregisterAll(this);
        int id = data.id();
        ArcadeTracker.PLAYERS.remove(uuid);
        // put player back in travellers table
        HashMap<String, Object> where = new HashMap<>();
        where.put("tardis_id", id);
        where.put("uuid", uuid.toString());
        plugin.getQueryFactory().doInsert("travellers", where);
    }
}
