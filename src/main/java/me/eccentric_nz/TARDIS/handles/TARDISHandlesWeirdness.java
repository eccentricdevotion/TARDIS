/*
 * Copyright (C) 2026 eccentric_nz
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
 * along with plugin program. If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.TARDIS.handles;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import org.bukkit.entity.Player;

public class TARDISHandlesWeirdness {

    private static final String[] start = new String[70];
    private static final String[] end = new String[70];

    static {
        start[0] = "The sky";
        start[1] = "Everything and more";
        start[2] = "The clear star that is yesterday";
        start[3] = "Tomorrow";
        start[4] = "An old apple";
        start[5] = "Camouflage paint";
        start[6] = "A sound you heard";
        start[7] = "A setback of the heart";
        start[8] = "The body of mind";
        start[9] = "A classical composition";
        start[10] = "Another day";
        start[11] = "Chair number eleven";
        start[12] = "Nihilism";
        start[13] = "Tranquility";
        start[14] = "Wondrous awe";
        start[15] = "That memory we used to share";
        start[16] = "Nothing of importance";
        start[17] = "Clear water";
        start[18] = "Gasoline";
        start[19] = "Sixty-four";
        start[20] = "Nothingness";
        start[21] = "The flow of quizzes";
        start[22] = "An enigma";
        start[23] = "A wave loudly clashing against a long shoreline";
        start[24] = "Stupidity";
        start[25] = "Love";
        start[26] = "An idea";
        start[27] = "The last sentence you saw";
        start[28] = "The person you were before";
        start[29] = "A flailing monkey";
        start[30] = "Organisational culture";
        start[31] = "Trickery";
        start[32] = "A caring mother";
        start[33] = "A sickeningly prodigious profile";
        start[34] = "A fly";
        start[35] = "Two-finger John";
        start[36] = "Seven worm";
        start[37] = "Pinocchio";
        start[38] = "Lucky number seven";
        start[39] = "A shooting star";
        start[40] = "Whiskey on the table";
        start[41] = "A cranky old lady";
        start[42] = "Stew and rum";
        start[43] = "Spam";
        start[44] = "Lonely Henry";
        start[45] = "Style";
        start[46] = "Fashion";
        start[47] = "A principal idea";
        start[48] = "Too long a stick";
        start[49] = "A glittering gem";
        start[50] = "That way";
        start[51] = "Significant understanding";
        start[52] = "Passion or serendipity";
        start[53] = "A late night";
        start[54] = "A stumbling first step";
        start[55] = "That stolen figurine";
        start[56] = "A token of gratitude";
        start[57] = "A small mercy";
        start[58] = "Utter nonsense";
        start[59] = "Colorful clay";
        start[60] = "Insignificance";
        start[61] = "The light at the end of the tunnel";
        start[62] = "The other side";
        start[63] = "Abstraction";
        start[64] = "Rock music";
        start[65] = "A passionate evening";
        start[66] = "A great silence";
        start[67] = "A river a thousand paces wide";
        start[68] = "The legend of the raven's roar";
        start[69] = "Enqoyism";
        end[0] = "is like a painted flower; it never wilts.";
        end[1] = "runs through everything.";
        end[2] = "is ever present.";
        end[3] = "lies ahead, what with the future yet to come.";
        end[4] = "approaches at high velocity!";
        end[5] = "is nonsensical, much like me.";
        end[6] = "likes to take a walk in the park.";
        end[7] = "is still not very coherent.";
        end[8] = "loves to love.";
        end[9] = "would die for a grapefruit!";
        end[10] = "sickens me.";
        end[11] = "has your skin crawling.";
        end[12] = "makes people shiver.";
        end[13] = "is always a pleasure.";
        end[14] = "could please even the most demanding follower of Freud.";
        end[15] = "slips on a banana peel.";
        end[16] = "is nothing at all?";
        end[17] = "doesn't like paying taxes.";
        end[18] = "would kindly inquire something about you.";
        end[19] = "is not yet ready to die.";
        end[20] = "is omni-present, much like candy.";
        end[21] = "is good for you.";
        end[22] = "does not make any sense.";
        end[23] = "gambles with lives, happiness, and even destiny itself!";
        end[24] = "would scare any linguist away.";
        end[25] = "sees the sun.";
        end[26] = "is running away.";
        end[27] = "jumps both ways.";
        end[28] = "can get both high and low.";
        end[29] = "tests the thesis that your theorem would unleash.";
        end[30] = "comes asking for bread.";
        end[31] = "is interdependent on the relatedness of motivation, subcultures, and management.";
        end[32] = "says hello.";
        end[33] = "tenderly sees to her child.";
        end[34] = "wants to go to hell.";
        end[35] = "is often pregnant.";
        end[36] = "is often one floor above you.";
        end[37] = "likes to have a shower in the morning.";
        end[38] = "wants to set things right.";
        end[39] = "tells the tale of towers.";
        end[40] = "stole the goods.";
        end[41] = "woke the prime minister.";
        end[42] = "shot the sheriff.";
        end[43] = "lay down on the riverbed.";
        end[44] = "asked you a question?";
        end[45] = "sat down once more.";
        end[46] = "shoots pineapples with a machine gun.";
        end[47] = "will take you to places you never expected not to visit!";
        end[48] = "revels in authority.";
        end[49] = "stands upon somebody else's legs.";
        end[50] = "visits Japan in the winter.";
        end[51] = "says goodbye to the shooter.";
        end[52] = "welcomes spring!";
        end[53] = "loves a good joke!";
        end[54] = "is a storyteller without equal.";
        end[55] = "rains heavily.";
        end[56] = "is like a summer breeze.";
        end[57] = "wanted the TRUTH!";
        end[58] = "set a tree house on fire.";
        end[59] = "bathes in sunlight.";
        end[60] = "has its world rocked by trees (or rocks).";
        end[61] = "ever stuns the onlooker.";
        end[62] = "brings both pleasure and pain.";
        end[63] = "takes the world for granted.";
        end[64] = "is not enough.";
        end[65] = "was always the second best.";
        end[66] = "is not all that great.";
        end[67] = "shakes beliefs widely held.";
        end[68] = "always strikes for the heart.";
        end[69] = "is belief in the interrelatedness of all things.";
    }

    public static void say(Player player) {
        String sentence = start[TARDISConstants.RANDOM.nextInt(70)] + " " + end[TARDISConstants.RANDOM.nextInt(70)];
        TARDIS.plugin.getMessenger().handlesMessage(player, sentence);
    }
}
