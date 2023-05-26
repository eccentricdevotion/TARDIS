/*
 * Copyright (C) 2023 eccentric_nz
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.TARDIS.move;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.api.event.TARDISEnterEvent;
import me.eccentric_nz.TARDIS.api.event.TARDISExitEvent;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetDoors;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetPlayerPrefs;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import me.eccentric_nz.TARDIS.messaging.TARDISMessage;
import me.eccentric_nz.TARDIS.mobfarming.TARDISPet;
import me.eccentric_nz.TARDIS.travel.TARDISDoorLocation;
import me.eccentric_nz.TARDIS.utility.TARDISItemRenamer;
import me.eccentric_nz.TARDIS.utility.TARDISSounds;
import me.eccentric_nz.TARDIS.utility.TARDISStaticLocationGetters;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

/**
 * @author eccentric_nz 
 */
public class TARDISDoorListener {

    public final float[][] adjustYaw = new float[4][4];
    final TARDIS plugin;
    private final int player_artron;

    public TARDISDoorListener(TARDIS plugin) {
        this.plugin = plugin;
        player_artron = this.plugin.getArtronConfig().getInt("player");
        // yaw adjustments if inner and outer door directions are different
        adjustYaw[0][0] = 0;
        adjustYaw[0][1] = 90;
        adjustYaw[0][2] = 180;
        adjustYaw[0][3] = -90;
        adjustYaw[1][0] = -90;
        adjustYaw[1][1] = 0;
        adjustYaw[1][2] = 90;
        adjustYaw[1][3] = 180;
        adjustYaw[2][0] = 180;
        adjustYaw[2][1] = -90;
        adjustYaw[2][2] = 0;
        adjustYaw[2][3] = 90;
        adjustYaw[3][0] = 90;
        adjustYaw[3][1] = 180;
        adjustYaw[3][2] = -90;
        adjustYaw[3][3] = 0;
    }

    /**
     * Get door location data for teleport entry and exit of the TARDIS.
     *
     * @param doortype a reference to the door_type field in the doors table
     * @param id       the unique TARDIS identifier i the database
     * @return an instance of the TARDISDoorLocation data class
     */
    public static TARDISDoorLocation getDoor(int doortype, int id) {
        TARDISDoorLocation tdl = new TARDISDoorLocation();
        // get door location
        HashMap<String, Object> wherei = new HashMap<>();
        wherei.put("door_type", doortype);
        wherei.put("tardis_id", id);
        ResultSetDoors rsd = new ResultSetDoors(TARDIS.plugin, wherei, false);
        if (rsd.resultSet()) {
            COMPASS d = rsd.getDoor_direction();
            tdl.setD(d);
            String doorLocStr = rsd.getDoor_location();
            World cw = TARDISStaticLocationGetters.getWorldFromSplitString(doorLocStr);
            tdl.setW(cw);
            Location tmp_loc = TARDISStaticLocationGetters.getLocationFromDB(doorLocStr);
            int getx = tmp_loc.getBlockX();
            int getz = tmp_loc.getBlockZ();
            switch (d) {
                case NORTH -> {
                    // z -ve
                    tmp_loc.setX(getx + 0.5);
                    tmp_loc.setZ(getz - 0.5);
                }
                case EAST -> {
                    // x +ve
                    tmp_loc.setX(getx + 1.5);
                    tmp_loc.setZ(getz + 0.5);
                }
                case SOUTH -> {
                    // z +ve
                    tmp_loc.setX(getx + 0.5);
                    tmp_loc.setZ(getz + 1.5);
                }
                case WEST -> {
                    // x -ve
                    tmp_loc.setX(getx - 0.5);
                    tmp_loc.setZ(getz + 0.5);
                }
            }
            tdl.setL(tmp_loc);
        }
        return tdl;
    }

    /**
     * A method to teleport the player into and out of the TARDIS.
     *
     * @param player   the player to teleport
     * @param location the location to teleport to
     * @param exit     whether the player is entering or exiting the TARDIS, if true
     *                 they are exiting
     * @param from     the world they are teleporting from
     * @param quotes   whether the player will receive a TARDIS quote message
     * @param sound    an integer representing the sound to play
     * @param minecart whether to play the resource pack sound
     * @param instant  whether to teleport the player out in this tick
     */
    public void movePlayer(Player player, Location location, boolean exit, World from, boolean quotes, int sound, boolean minecart, boolean instant) {
        // teleport player on this tick if instant is true
        if (instant) {
            playDoorSound(player, sound, location, minecart);
            doPlayerMove(player, location, exit, from, quotes);
        } else {
            // play the door sound 5 ticks (1/4s) later
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> playDoorSound(player, sound, location, minecart), 5L);
            // actually teleport the player 10 ticks (1/2s) later
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> doPlayerMove(player, location, exit, from, quotes), 10L);
        }
    }

    private void doPlayerMove(Player player, Location location, boolean exit, World from, boolean quotes) {
        int i = TARDISConstants.RANDOM.nextInt(plugin.getGeneralKeeper().getQuotes().size());
        World to = location.getWorld();
        boolean allowFlight = player.getAllowFlight();
        boolean crossWorlds = (from != to);
        boolean isSurvival = checkSurvival(to);
        player.teleport(location);
        if (player.getGameMode() == GameMode.CREATIVE || (allowFlight && crossWorlds && !isSurvival)) {
            player.setAllowFlight(true);
        }
        if (quotes) {
            if (TARDISConstants.RANDOM.nextInt(100) < 3) {
                TextComponent tcg = new TextComponent("[TARDIS] ");
                tcg.setColor(ChatColor.GOLD);
                TextComponent tcl = new TextComponent("Look at these eyebrows. These are attack eyebrows! They could take off bottle caps!");
                tcl.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Click me!")));
                tcl.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tardis egg"));
                tcg.addExtra(tcl);
                player.spigot().sendMessage(tcg);
            } else {
                player.sendMessage(plugin.getPluginName() + plugin.getGeneralKeeper().getQuotes().get(i));
            }
        }
        if (exit) {
            plugin.getPM().callEvent(new TARDISExitEvent(player, to));
            // give some artron energy to player
            HashMap<String, Object> where = new HashMap<>();
            UUID uuid = player.getUniqueId();
            where.put("uuid", uuid.toString());
            if (plugin.getTrackerKeeper().getHasTravelled().contains(uuid)) {
                plugin.getQueryFactory().alterEnergyLevel("player_prefs", player_artron, where, player);
                plugin.getTrackerKeeper().getHasTravelled().remove(uuid);
            }
            // set player time when exiting TARDIS
            if (plugin.getTrackerKeeper().getSetTime().containsKey(uuid)) {
                // player should be temporally relocated (time passes relative to world time)
                setTemporalLocation(player, plugin.getTrackerKeeper().getSetTime().get(uuid), true);
                plugin.getTrackerKeeper().getSetTime().remove(uuid);
            } else {
                // player time is reset to world time
                setTemporalLocation(player, -1, true);
            }
            plugin.getTrackerKeeper().getEjecting().remove(uuid);
        } else {
            plugin.getPM().callEvent(new TARDISEnterEvent(player, from));
            if (plugin.getConfig().getBoolean("creation.keep_night")) {
                // set the player's time to midnight
                setTemporalLocation(player, 18000, false);
            }
            TARDISSounds.playTARDISHum(player);
        }
        // give a key
        giveKey(player);
    }

    /**
     * Checks if the world the player is teleporting to is a SURVIVAL world.
     *
     * @param world the world to check
     * @return true if the world is a SURVIVAL world, otherwise false
     */
    public boolean checkSurvival(World world) {
        boolean bool = false;
        switch (plugin.getWorldManager()) {
            case MULTIVERSE -> bool = plugin.getMVHelper().isWorldSurvival(world);
            case NONE -> bool = plugin.getPlanetsConfig().getString("planets." + world.getName() + ".gamemode").equalsIgnoreCase("SURVIVAL");
        }
        return bool;
    }

    /**
     * A method to transport player pets (tamed mobs) into and out of the
     * TARDIS.
     *
     * @param pets      a list of the player's pets found nearby
     * @param location  the location to teleport pets to
     * @param player    the player who owns the pets
     * @param direction the direction of the police box
     * @param enter     whether the pets are entering (true) or exiting (false)
     */
    public void movePets(List<TARDISPet> pets, Location location, Player player, COMPASS direction, boolean enter) {
        Location pl = location.clone();
        World w = location.getWorld();
        // will need to adjust this depending on direction Police Box is facing
        if (enter) {
            pl.setZ(location.getZ() + 1);
        } else {
            switch (direction) {
                case NORTH -> {
                    pl.setX(location.getX() + 1);
                    pl.setZ(location.getZ() + 1);
                }
                case WEST -> {
                    pl.setX(location.getX() + 1);
                    pl.setZ(location.getZ() - 1);
                }
                case SOUTH -> {
                    pl.setX(location.getX() - 1);
                    pl.setZ(location.getZ() - 1);
                }
                default -> {
                    pl.setX(location.getX() - 1);
                    pl.setZ(location.getZ() + 1);
                }
            }
        }
        for (TARDISPet pet : pets) {
            plugin.setTardisSpawn(true);
            LivingEntity ent = (LivingEntity) w.spawnEntity(pl, pet.getType());
            if (ent.isDead()) {
                ent.remove();
                plugin.debug("Entity is dead! Spawning again...");
                ent = (LivingEntity) w.spawnEntity(pl, pet.getType());
            }
            String pet_name = pet.getName();
            if (pet_name != null && !pet_name.isEmpty()) {
                ent.setCustomName(pet.getName());
            }
            ent.setHealth(pet.getHealth());
            ((Tameable) ent).setTamed(true);
            ((Tameable) ent).setOwner(player);
            switch (pet.getType()) {
                case WOLF -> {
                    Wolf wolf = (Wolf) ent;
                    wolf.setCollarColor(pet.getColour());
                    wolf.setSitting(pet.getSitting());
                    wolf.setAge(pet.getAge());
                    if (pet.isBaby()) {
                        wolf.setBaby();
                    }
                }
                case CAT -> {
                    Cat cat = (Cat) ent;
                    cat.setCollarColor(pet.getColour());
                    cat.setCatType(pet.getCatType());
                    cat.setSitting(pet.getSitting());
                    cat.setAge(pet.getAge());
                    if (pet.isBaby()) {
                        cat.setBaby();
                    }
                }
                case PARROT -> {
                    Parrot parrot = (Parrot) ent;
                    parrot.setSitting(pet.getSitting());
                    parrot.setAge(pet.getAge());
                    if (pet.isBaby()) {
                        parrot.setBaby();
                    }
                    parrot.setVariant(pet.getVariant());
                    if (pet.isOnLeftShoulder()) {
                        player.setShoulderEntityLeft(parrot);
                    }
                    if (pet.isOnRightShoulder()) {
                        player.setShoulderEntityRight(parrot);
                    }
                }
                default -> {
                }
            }
        }
        pets.clear();
    }

    /**
     * A method to give the TARDIS key to a player if the server is using a
     * multi-inventory plugin.
     *
     * @param player the player to give the key to
     */
    private void giveKey(Player player) {
        if (plugin.getConfig().getBoolean("travel.give_key")) {
            String key;
            ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, player.getUniqueId().toString());
            if (rsp.resultSet()) {
                key = (!rsp.getKey().isEmpty()) ? rsp.getKey() : plugin.getConfig().getString("preferences.key");
            } else {
                key = plugin.getConfig().getString("preferences.key");
            }
            if (!key.equals("AIR")) {
                PlayerInventory inv = player.getInventory();
                Material m = Material.valueOf(key);
                ItemStack oh = inv.getItemInOffHand();
                if (!inv.contains(m) && (oh != null && !oh.getType().equals(m))) {
                    ItemStack is = new ItemStack(m, 1);
                    TARDISItemRenamer ir = new TARDISItemRenamer(plugin, player, is);
                    ir.setName("TARDIS Key", true);
                    inv.addItem(is);
                    player.updateInventory();
                    TARDISMessage.send(player, "KEY_REMIND");
                }
            }
        }
    }

    /**
     * Adjusts the direction the player is facing after a teleport.
     *
     * @param d1 the direction the first door is facing
     * @param d2 the direction the second door is facing
     * @return the angle needed to correct the yaw
     */
    public float adjustYaw(COMPASS d1, COMPASS d2) {
        return switch (d1) {
            case EAST -> adjustYaw[0][d2.ordinal() / 2];
            case SOUTH -> adjustYaw[1][d2.ordinal() / 2];
            case WEST -> adjustYaw[2][d2.ordinal() / 2];
            default -> adjustYaw[3][d2.ordinal() / 2];
        };
    }

    /**
     * Plays a door sound when the TARDIS door is clicked.
     *
     * @param player   a player to play the sound for
     * @param sound    the sound to play
     * @param location a location to play the sound at
     * @param minecart whether to play the TARDIS sound or a Minecraft substitute
     */
    private void playDoorSound(Player player, int sound, Location location, boolean minecart) {
        switch (sound) {
            case 1 -> {
                if (!minecart) {
                    TARDISSounds.playTARDISSound(location, "tardis_door_open");
                } else {
                    player.playSound(player.getLocation(), Sound.BLOCK_IRON_DOOR_OPEN, 1.0F, 1.0F);
                }
            }
            case 2 -> {
                if (!minecart) {
                    TARDISSounds.playTARDISSound(location, "tardis_door_close");
                } else {
                    player.playSound(player.getLocation(), Sound.BLOCK_IRON_DOOR_OPEN, 1.0F, 1.0F);
                }
            }
            case 3 -> {
                if (!minecart) {
                    TARDISSounds.playTARDISSound(location, "tardis_enter");
                } else {
                    player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0F, 1.0F);
                }
            }
            default -> {
            }
        }
    }

    /**
     * Set a player's time relative to the server time. Based on Essentials
     * /ptime command.
     *
     * @param player the player to set the time for
     * @param ticks  the ticks to set the time to
     */
    private void setTemporalLocation(Player player, long ticks, boolean relative) {
        if (player.isOnline()) {
            if (ticks != -1) {
                if (relative) {
                    player.resetPlayerTime();
                    long time = player.getPlayerTime();
                    time -= time % 24000L;
                    time += 24000L + ticks;
                    long calculatedtime = time - player.getWorld().getTime();
                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                        player.setPlayerTime(calculatedtime, true);
                        if (plugin.getConfig().getBoolean("allow.perception_filter")) {
                            plugin.getFilter().addPerceptionFilter(player);
                        }
                        plugin.getTrackerKeeper().getTemporallyLocated().add(player.getUniqueId());
                    }, 10L);
                } else {
                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> player.setPlayerTime(18000, false), 10L);
                }
            } else {
                player.resetPlayerTime();
                boolean remove = true;
                Material m = Material.valueOf(plugin.getRecipesConfig().getString("shaped.Perception Filter.result"));
                for (ItemStack is : player.getInventory().getArmorContents()) {
                    if (is != null && is.getType().equals(m)) {
                        remove = false;
                    }
                }
                if (remove && plugin.getTrackerKeeper().getTemporallyLocated().contains(player.getUniqueId())) {
                    if (plugin.getConfig().getBoolean("allow.perception_filter")) {
                        plugin.getFilter().removePerceptionFilter(player);
                    }
                    plugin.getTrackerKeeper().getTemporallyLocated().remove(player.getUniqueId());
                }
            }
        }
    }

    /**
     * Remove player from the travellers table.
     *
     * @param uuid the UUID of the player to remove
     */
    void removeTraveller(UUID uuid) {
        HashMap<String, Object> where = new HashMap<>();
        where.put("uuid", uuid.toString());
        plugin.getQueryFactory().doSyncDelete("travellers", where);
    }
}
