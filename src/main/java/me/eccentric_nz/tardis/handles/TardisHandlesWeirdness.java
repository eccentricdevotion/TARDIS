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
 * along with plugin program. If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.tardis.handles;

import me.eccentric_nz.tardis.TardisConstants;
import me.eccentric_nz.tardis.messaging.TardisMessage;
import org.bukkit.entity.Player;

public class TardisHandlesWeirdness {

    private static final String[] START = new String[70];
    private static final String[] END = new String[70];

    static {
        START[0] = "The sky";
        START[1] = "Everything and more";
        START[2] = "The clear star that is yesterday";
        START[3] = "Tomorrow";
        START[4] = "An old apple";
        START[5] = "Camouflage paint";
        START[6] = "A sound you heard";
        START[7] = "A setback of the heart";
        START[8] = "The body of mind";
        START[9] = "A classical composition";
        START[10] = "Another day";
        START[11] = "Chair number eleven";
        START[12] = "Nihilism";
        START[13] = "Tranquility";
        START[14] = "Wondrous awe";
        START[15] = "That memory we used to share";
        START[16] = "Nothing of importance";
        START[17] = "Clear water";
        START[18] = "Gasoline";
        START[19] = "Sixty-four";
        START[20] = "Nothingness";
        START[21] = "The flow of quizzes";
        START[22] = "An enigma";
        START[23] = "A wave loudly clashing against a long shoreline";
        START[24] = "Stupidity";
        START[25] = "Love";
        START[26] = "An idea";
        START[27] = "The last sentence you saw";
        START[28] = "The person you were before";
        START[29] = "A flailing monkey";
        START[30] = "Organisational culture";
        START[31] = "Trickery";
        START[32] = "A caring mother";
        START[33] = "A sickeningly prodigious profile";
        START[34] = "A fly";
        START[35] = "Two-finger John";
        START[36] = "Seven worm";
        START[37] = "Pinocchio";
        START[38] = "Lucky number seven";
        START[39] = "A shooting star";
        START[40] = "Whiskey on the table";
        START[41] = "A cranky old lady";
        START[42] = "Stew and rum";
        START[43] = "Spam";
        START[44] = "Lonely Henry";
        START[45] = "Style";
        START[46] = "Fashion";
        START[47] = "A principal idea";
        START[48] = "Too long a stick";
        START[49] = "A glittering gem";
        START[50] = "That way";
        START[51] = "Significant understanding";
        START[52] = "Passion or serendipity";
        START[53] = "A late night";
        START[54] = "A stumbling first step";
        START[55] = "That stolen figurine";
        START[56] = "A token of gratitude";
        START[57] = "A small mercy";
        START[58] = "Utter nonsense";
        START[59] = "Colorful clay";
        START[60] = "Insignificance";
        START[61] = "The light at the end of the tunnel";
        START[62] = "The other side";
        START[63] = "Abstraction";
        START[64] = "Rock music";
        START[65] = "A passionate evening";
        START[66] = "A great silence";
        START[67] = "A river a thousand paces wide";
        START[68] = "The legend of the raven's roar";
        START[69] = "Enqoyism";
        END[0] = "is like a painted flower; it never wilts.";
        END[1] = "runs through everything.";
        END[2] = "is ever present.";
        END[3] = "lies ahead, what with the future yet to come.";
        END[4] = "approaches at high velocity!";
        END[5] = "is nonsensical, much like me.";
        END[6] = "likes to take a walk in the park.";
        END[7] = "is still not very coherent.";
        END[8] = "loves to love.";
        END[9] = "would die for a grapefruit!";
        END[10] = "sickens me.";
        END[11] = "has your skin crawling.";
        END[12] = "makes people shiver.";
        END[13] = "is always a pleasure.";
        END[14] = "could please even the most demanding follower of Freud.";
        END[15] = "slips on a banana peel.";
        END[16] = "is nothing at all?";
        END[17] = "doesn't like paying taxes.";
        END[18] = "would kindly inquire something about you.";
        END[19] = "is not yet ready to die.";
        END[20] = "is omni-present, much like candy.";
        END[21] = "is good for you.";
        END[22] = "does not make any sense.";
        END[23] = "gambles with lives, happiness, and even destiny itself!";
        END[24] = "would scare any linguist away.";
        END[25] = "sees the sun.";
        END[26] = "is running away.";
        END[27] = "jumps both ways.";
        END[28] = "can get both high and low.";
        END[29] = "tests the thesis that your theorem would unleash.";
        END[30] = "comes asking for bread.";
        END[31] = "is interdependent on the relatedness of motivation, subcultures, and management.";
        END[32] = "says hello.";
        END[33] = "tenderly sees to her child.";
        END[34] = "wants to go to hell.";
        END[35] = "is often pregnant.";
        END[36] = "is often one floor above you.";
        END[37] = "likes to have a shower in the morning.";
        END[38] = "wants to set things right.";
        END[39] = "tells the tale of towers.";
        END[40] = "stole the goods.";
        END[41] = "woke the prime minister.";
        END[42] = "shot the sheriff.";
        END[43] = "lay down on the riverbed.";
        END[44] = "asked you a question?";
        END[45] = "sat down once more.";
        END[46] = "shoots pineapples with a machine gun.";
        END[47] = "will take you to places you never expected not to visit!";
        END[48] = "revels in authority.";
        END[49] = "stands upon somebody else's legs.";
        END[50] = "visits Japan in the winter.";
        END[51] = "says goodbye to the shooter.";
        END[52] = "welcomes spring!";
        END[53] = "loves a good joke!";
        END[54] = "is a storyteller without equal.";
        END[55] = "rains heavily.";
        END[56] = "is like a summer breeze.";
        END[57] = "wanted the TRUTH!";
        END[58] = "set a tree house on fire.";
        END[59] = "bathes in sunlight.";
        END[60] = "has its world rocked by trees (or rocks).";
        END[61] = "ever stuns the onlooker.";
        END[62] = "brings both pleasure and pain.";
        END[63] = "takes the world for granted.";
        END[64] = "is not enough.";
        END[65] = "was always the second best.";
        END[66] = "is not all that great.";
        END[67] = "shakes beliefs widely held.";
        END[68] = "always strikes for the heart.";
        END[69] = "is belief in the interrelatedness of all things.";
    }

    public static void say(Player player) {
        String sentence = START[TardisConstants.RANDOM.nextInt(70)] + " " + END[TardisConstants.RANDOM.nextInt(70)];
        TardisMessage.handlesMessage(player, sentence);
    }
}
