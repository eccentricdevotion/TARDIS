/*
 * Copyright (C) 2021 eccentric_nz
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
package me.eccentric_nz.tardis.advancement;

import org.apache.commons.lang.Validate;
import org.bukkit.entity.Player;

import java.lang.ref.WeakReference;
import java.util.Arrays;

/**
 * The Doctor Who Experience was one of the largest and most ambitious Doctor Who exhibitions ever staged. Featuring
 * props and costumes from throughout the franchise's forty-seven year history, it also featured one new and different
 * element; an interactive story in which people could take part and become part of the adventure.
 *
 * @author desht
 * <p>
 * Adapted from ExperienceUtils code originally in ScrollingMenuSign.
 * <p>
 * Credit to nisovin (http://forums.bukkit.org/threads/experienceutils-make-giving-taking-exp-a-bit-more-intuitive.54450/#post-1067480)
 * for an implementation that avoids the problems of getTotalExperience(), which doesn't work properly after a player
 * has enchanted something.
 * <p>
 * Credit to comphenix for further contributions: See http://forums.bukkit.org/threads/experiencemanager-was-experienceutils-make-giving-taking-exp-a-bit-more-intuitive.54450/page-3#post-1273622
 */
class TardisExperienceRewarder {

    private static final int HARD_MAX_LEVEL = 100000;
    private static int[] experienceTotalToReachLevel;

    static {
        // 25 is an arbitrary value for the initial table size - the actual
        // value isn't critically important since the table is resized as needed.
        initializeLookupTables(25);
    }

    private final WeakReference<Player> player;
    private final String playerName;

    /**
     * Creates a new TardisExperienceRewarder for the given player.
     *
     * @param player the player for this TardisExperienceRewarder object
     * @throws IllegalArgumentException if the player is null
     */
    TardisExperienceRewarder(Player player) {
        Validate.notNull(player, "Player cannot be null");
        this.player = new WeakReference<>(player);
        playerName = player.getName();
    }

    /**
     * Initializes the experience lookup table. See http://minecraft.gamepedia.com/Experience
     *
     * @param maxLevel The highest level handled by the lookup tables
     */
    private static void initializeLookupTables(int maxLevel) {
        experienceTotalToReachLevel = new int[maxLevel];

        for (int i = 0; i < experienceTotalToReachLevel.length; i++) {
            experienceTotalToReachLevel[i] = i >= 30 ? (int) (3.5 * i * i - 151.5 * i + 2220) : i >= 16 ? (int) (1.5 * i * i - 29.5 * i + 360) : 17 * i;
        }
    }

    /**
     * Calculates the level that the given experience quantity corresponds to, without using the lookup tables. This is needed if
     * getLevelForExperience() is called with an experience quantity beyond the range of the existing lookup tables.
     *
     * @param experience the amount of experience
     * @return the experience level
     */
    private static int calculateLevelForExperience(int experience) {
        int level = 0;
        int currentExperience = 7; // level 1
        int increment = 10;

        while (currentExperience <= experience) {
            currentExperience += increment;
            level++;
            increment += (level % 2 == 0) ? 3 : 4;
        }
        return level;
    }

    /**
     * Get the Player associated with this TardisExperienceRewarder.
     *
     * @return the Player object
     * @throws IllegalStateException if the player is no longer online
     */
    private Player getPlayer() {
        Player player = this.player.get();
        if (player == null) {
            throw new IllegalStateException("Player " + playerName + " is not online");
        }
        return player;
    }

    /**
     * Adjust the player's XP by the given amount in an intelligent fashion. Works around some of the non-intuitive
     * behaviour of the basic Bukkit player.giveExp() method.
     *
     * @param amount Amount of experience, may be negative
     */
    void changeExperience(int amount) {
        changeExperience((double) amount);
    }

    /**
     * Adjust the player's XP by the given amount in an intelligent fashion. Works around some of the non-intuitive
     * behaviour of the basic Bukkit player.giveExp() method.
     *
     * @param amount Amount of XP, may be negative
     */
    private void changeExperience(double amount) {
        setExperience(getCurrentFractionalExp(), amount);
    }

    private void setExperience(double base, double amount) {
        int xp = (int) Math.max(base + amount, 0);

        Player player = getPlayer();
        int currentLevel = player.getLevel();
        int newLevel = getLevelForExp(xp);

        // Increment level
        if (currentLevel != newLevel) {
            player.setLevel(newLevel);
        }
        // Increment total experience - this should force the server to send an update packet
        if (xp > base) {
            player.setTotalExperience(player.getTotalExperience() + xp - (int) base);
        }

        // TODO Figure out what "pct" is.
        double pct = (base - getXpForLevel(newLevel) + amount) / (double) (getXpNeededToLevelUp(newLevel));
        player.setExp((float) pct);
    }

    /**
     * Get the player's current fractional XP.
     *
     * @return The player's total XP with fractions.
     */
    private double getCurrentFractionalExp() {
        Player player = getPlayer();

        int lvl = player.getLevel();
        return getXpForLevel(lvl) + (double) (getXpNeededToLevelUp(lvl) * player.getExp());
    }

    /**
     * Get the level that the given amount of XP falls within.
     *
     * @param exp the amount to check for
     * @return the level that a player with this amount total XP would be
     * @throws IllegalArgumentException if the given XP is less than 0
     */
    private int getLevelForExp(int exp) {
        if (exp <= 0) {
            return 0;
        }
        if (exp > experienceTotalToReachLevel[experienceTotalToReachLevel.length - 1]) {
            // need to extend the lookup tables
            int newMax = calculateLevelForExperience(exp) * 2;
            Validate.isTrue(newMax <= HARD_MAX_LEVEL, "Level for exp " + exp + " > hard max level " + HARD_MAX_LEVEL);
            initializeLookupTables(newMax);
        }
        int pos = Arrays.binarySearch(experienceTotalToReachLevel, exp);
        return pos < 0 ? -pos - 2 : pos;
    }

    /**
     * Retrieves the amount of experience the experience bar can hold at the given level.
     *
     * @param level the level to check
     * @return the amount of experience at this level in the level bar
     * @throws IllegalArgumentException if the level is less than 0
     */
    private int getXpNeededToLevelUp(int level) {
        Validate.isTrue(level >= 0, "Level may not be negative.");
        return level > 30 ? 62 + (level - 30) * 7 : level >= 16 ? 17 + (level - 15) * 3 : 17;
    }

    /**
     * Return the total XP needed to be the given level.
     *
     * @param level The level to check for.
     * @return The amount of XP needed for the level.
     * @throws IllegalArgumentException if the level is less than 0 or greater than the current hard maximum
     */
    private int getXpForLevel(int level) {
        Validate.isTrue(level >= 0 && level <= HARD_MAX_LEVEL, "Invalid level " + level + "(must be in range 0.." + HARD_MAX_LEVEL + ")");
        if (level >= experienceTotalToReachLevel.length) {
            initializeLookupTables(level * 2);
        }
        return experienceTotalToReachLevel[level];
    }
}
