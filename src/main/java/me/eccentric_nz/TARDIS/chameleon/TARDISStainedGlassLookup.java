/*
 * Copyright (C) 2014 eccentric_nz
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
package me.eccentric_nz.TARDIS.chameleon;

import java.util.HashMap;

/**
 * Lookup table for Police Box materialisation. This maps block IDs to Stained
 * glass colours. For example a BRICK block would map to RED stained glass.
 *
 * @author eccentric_nz
 */
public class TARDISStainedGlassLookup {

    private final HashMap<Integer, Byte> stain = new HashMap<Integer, Byte>();

    public TARDISStainedGlassLookup() {
        stain.put(1, (byte) 8);
        stain.put(2, (byte) 13);
        stain.put(3, (byte) 12);
        stain.put(4, (byte) 8);
        stain.put(5, (byte) 12);
        stain.put(6, (byte) 13);
        stain.put(7, (byte) 15);
        stain.put(12, (byte) 4);
        stain.put(13, (byte) 8);
        stain.put(14, (byte) 8);
        stain.put(15, (byte) 8);
        stain.put(16, (byte) 8);
        stain.put(17, (byte) 12);
        stain.put(18, (byte) 13);
        stain.put(19, (byte) 4);
        stain.put(20, (byte) 0);
        stain.put(21, (byte) 8);
        stain.put(22, (byte) 11);
        stain.put(23, (byte) 8);
        stain.put(24, (byte) 4);
        stain.put(25, (byte) 12);
        stain.put(27, (byte) 14);
        stain.put(28, (byte) 8);
        stain.put(29, (byte) 8);
        stain.put(30, (byte) 0);
        stain.put(33, (byte) 8);
        stain.put(35, (byte) -1);
        stain.put(41, (byte) 1);
        stain.put(42, (byte) 7);
        stain.put(43, (byte) 8);
        stain.put(44, (byte) 8);
        stain.put(45, (byte) 14);
        stain.put(46, (byte) 14);
        stain.put(47, (byte) 14);
        stain.put(48, (byte) 8);
        stain.put(49, (byte) 15);
        stain.put(52, (byte) 0);
        stain.put(53, (byte) 12);
        stain.put(54, (byte) 12);
        stain.put(56, (byte) 8);
        stain.put(57, (byte) 9);
        stain.put(58, (byte) 12);
        stain.put(59, (byte) 5);
        stain.put(60, (byte) 12);
        stain.put(61, (byte) 8);
        stain.put(62, (byte) 1);
        stain.put(65, (byte) 12);
        stain.put(66, (byte) 8);
        stain.put(67, (byte) 8);
        stain.put(69, (byte) 8);
        stain.put(70, (byte) 8);
        stain.put(72, (byte) 12);
        stain.put(73, (byte) 8);
        stain.put(74, (byte) 8);
        stain.put(77, (byte) 8);
        stain.put(78, (byte) 0);
        stain.put(79, (byte) 3);
        stain.put(80, (byte) 0);
        stain.put(81, (byte) 13);
        stain.put(82, (byte) 8);
        stain.put(83, (byte) 5);
        stain.put(84, (byte) 12);
        stain.put(85, (byte) 12);
        stain.put(86, (byte) 1);
        stain.put(87, (byte) 14);
        stain.put(88, (byte) 8);
        stain.put(89, (byte) 4);
        stain.put(90, (byte) 10);
        stain.put(91, (byte) 1);
        stain.put(92, (byte) 12);
        stain.put(93, (byte) 14);
        stain.put(94, (byte) 14);
        stain.put(95, (byte) -1);
        stain.put(97, (byte) 8);
        stain.put(98, (byte) 8);
        stain.put(99, (byte) 12);
        stain.put(100, (byte) 12);
        stain.put(101, (byte) 8);
        stain.put(102, (byte) 0);
        stain.put(103, (byte) 5);
        stain.put(107, (byte) 12);
        stain.put(108, (byte) 14);
        stain.put(109, (byte) 8);
        stain.put(110, (byte) 6);
        stain.put(112, (byte) 14);
        stain.put(113, (byte) 14);
        stain.put(114, (byte) 14);
        stain.put(115, (byte) 14);
        stain.put(116, (byte) 15);
        stain.put(117, (byte) 1);
        stain.put(118, (byte) 7);
        stain.put(120, (byte) 0);
        stain.put(121, (byte) 0);
        stain.put(123, (byte) 4);
        stain.put(124, (byte) 4);
        stain.put(125, (byte) 12);
        stain.put(126, (byte) 12);
        stain.put(128, (byte) 4);
        stain.put(129, (byte) 8);
        stain.put(130, (byte) 13);
        stain.put(133, (byte) 5);
        stain.put(134, (byte) 12);
        stain.put(135, (byte) 12);
        stain.put(136, (byte) 12);
        stain.put(137, (byte) 12);
        stain.put(138, (byte) 9);
        stain.put(139, (byte) 8);
        stain.put(141, (byte) 13);
        stain.put(142, (byte) 13);
        stain.put(143, (byte) 12);
        stain.put(144, (byte) 8);
        stain.put(145, (byte) 7);
        stain.put(146, (byte) 12);
        stain.put(147, (byte) 1);
        stain.put(148, (byte) 8);
        stain.put(149, (byte) 14);
        stain.put(150, (byte) 14);
        stain.put(151, (byte) 12);
        stain.put(152, (byte) 14);
        stain.put(153, (byte) 14);
        stain.put(154, (byte) 7);
        stain.put(155, (byte) 0);
        stain.put(156, (byte) 0);
        stain.put(157, (byte) 14);
        stain.put(158, (byte) 8);
        stain.put(159, (byte) -1);
        stain.put(160, (byte) -1);
        stain.put(161, (byte) 13);
        stain.put(162, (byte) 12);
        stain.put(163, (byte) 12);
        stain.put(164, (byte) 12);
        stain.put(165, (byte) 5);
        stain.put(166, (byte) 0);
        stain.put(168, (byte) 13);
        stain.put(169, (byte) 0);
        stain.put(170, (byte) 4);
        stain.put(171, (byte) -1);
        stain.put(172, (byte) 12);
        stain.put(173, (byte) 15);
        stain.put(174, (byte) 3);
        stain.put(175, (byte) 5);
        stain.put(179, (byte) 1);
    }

    public HashMap<Integer, Byte> getStain() {
        return stain;
    }
}
