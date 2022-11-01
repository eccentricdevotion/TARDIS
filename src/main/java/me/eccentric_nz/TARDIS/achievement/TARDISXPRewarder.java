/*
 * Copyright (C) 2022 eccentric_nz
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
package me.eccentric_nz.TARDIS.achievement;

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
 * Credit to nisovin (<a href="http://forums.bukkit.org/threads/experienceutils-make-giving-taking-exp-a-bit-more-intuitive.54450/#post-1067480">...</a>)
 * for an implementation that avoids the problems of getTotalExperience(), which doesn't work properly after a player
 * has enchanted something.
 * <p>
 * Credit to comphenix for further contributions: See <a href="http://forums.bukkit.org/threads/experiencemanager-was-experienceutils-make-giving-taking-exp-a-bit-more-intuitive.54450/page-3#post-1273622">...</a>
 */
class TARDISXPRewarder {

    private static final int hardMaxLevel = 100000;
    private static int[] xpTotalToReachLevel;

    static {
        // 25 is an arbitrary value for the initial table size - the actual
        // value isn't critically important since the table is resized as needed.
        initLookupTables(25);
    }

    private final WeakReference<Player> player;
    private final String playerName;

    /**
     * Create a new XPKCalculator for the given player.
     *
     * @param player the player for this XPKCalculator object
     * @throws IllegalArgumentException if the player is null
     */
    TARDISXPRewarder(Player player) {
        if (player == null) {
            throw new IllegalArgumentException("Player cannot be null");
        }
        this.player = new WeakReference<>(player);
        playerName = player.getName();
    }

    /**
     * Initialize the XP lookup table. See <a href="http://minecraft.gamepedia.com/Experience">...</a>
     *
     * @param maxLevel The highest level handled by the lookup tables
     */
    private static void initLookupTables(int maxLevel) {
        xpTotalToReachLevel = new int[maxLevel];

        for (int i = 0; i < xpTotalToReachLevel.length; i++) {
            xpTotalToReachLevel[i] = i >= 30 ? (int) (3.5 * i * i - 151.5 * i + 2220) : i >= 16 ? (int) (1.5 * i * i - 29.5 * i + 360) : 17 * i;
        }
    }

    /**
     * Calculate the level that the given XP quantity corresponds to, without using the lookup tables. This is needed if
     * getLevelForExp() is called with an XP quantity beyond the range of the existing lookup tables.
     *
     * @param exp the amount of experience
     * @return the experience level
     */
    private static int calculateLevelForExp(int exp) {
        int level = 0;
        int curExp = 7; // level 1
        int incr = 10;

        while (curExp <= exp) {
            curExp += incr;
            level++;
            incr += (level % 2 == 0) ? 3 : 4;
        }
        return level;
    }

    /**
     * Get the Player associated with this XPKCalculator.
     *
     * @return the Player object
     * @throws IllegalStateException if the player is no longer online
     */
    private Player getPlayer() {
        Player p = player.get();
        if (p == null) {
            throw new IllegalStateException("Player " + playerName + " is not online");
        }
        return p;
    }

    /**
     * Adjust the player's XP by the given amount in an intelligent fashion. Works around some of the non-intuitive
     * behaviour of the basic Bukkit player.giveExp() method.
     *
     * @param amt Amount of XP, may be negative
     */
    void changeExp(int amt) {
        changeExp((double) amt);
    }

    /**
     * Adjust the player's XP by the given amount in an intelligent fashion. Works around some of the non-intuitive
     * behaviour of the basic Bukkit player.giveExp() method.
     *
     * @param amt Amount of XP, may be negative
     */
    private void changeExp(double amt) {
        setExp(getCurrentFractionalXP(), amt);
    }

    private void setExp(double base, double amt) {
        int xp = (int) Math.max(base + amt, 0);

        Player p = getPlayer();
        int curLvl = p.getLevel();
        int newLvl = getLevelForExp(xp);

        // Increment level
        if (curLvl != newLvl) {
            p.setLevel(newLvl);
        }
        // Increment total experience - this should force the server to send an update packet
        if (xp > base) {
            p.setTotalExperience(p.getTotalExperience() + xp - (int) base);
        }

        double pct = (base - getXpForLevel(newLvl) + amt) / (double) (getXpNeededToLevelUp(newLvl));
        p.setExp((float) pct);
    }

    /**
     * Get the player's current fractional XP.
     *
     * @return The player's total XP with fractions.
     */
    private double getCurrentFractionalXP() {
        Player p = getPlayer();

        int lvl = p.getLevel();
        return getXpForLevel(lvl) + (double) (getXpNeededToLevelUp(lvl) * p.getExp());
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
        if (exp > xpTotalToReachLevel[xpTotalToReachLevel.length - 1]) {
            // need to extend the lookup tables
            int newMax = calculateLevelForExp(exp) * 2;
            if (newMax > hardMaxLevel) {
                throw new IllegalArgumentException("Level for exp " + exp + " > hard max level " + hardMaxLevel);
            }
            initLookupTables(newMax);
        }
        int pos = Arrays.binarySearch(xpTotalToReachLevel, exp);
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
        if (level < 0) {
            throw new IllegalArgumentException("Level may not be negative.");
        }
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
        if (level < 0 || level > hardMaxLevel) {
            throw new IllegalArgumentException("Invalid level " + level + "(must be in range 0.." + hardMaxLevel + ")");
        }
        if (level >= xpTotalToReachLevel.length) {
            initLookupTables(level * 2);
        }
        return xpTotalToReachLevel[level];
    }
}
