/*
 * Copyright (C) 2018  michidk && xxmicloxx
 * http://dev.bukkit.org/bukkit-plugins/noteblockapi/
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.TARDIS.noteblock;

import org.bukkit.Instrument;
import org.bukkit.Sound;

class NoteBlockInstrument {

    public static Sound getInstrument(byte instrument) {
        switch (instrument) {
            case 0:
                return Sound.BLOCK_NOTE_BLOCK_HARP;
            case 1:
                return Sound.BLOCK_NOTE_BLOCK_BASS;
            case 2:
                return Sound.BLOCK_NOTE_BLOCK_BASEDRUM;
            case 3:
                return Sound.BLOCK_NOTE_BLOCK_SNARE;
            case 4:
                return Sound.BLOCK_NOTE_BLOCK_HAT;
        }
        return Sound.BLOCK_NOTE_BLOCK_HARP;
    }

    public static Instrument getBukkitInstrument(byte instrument) {
        switch (instrument) {
            case 0:
                return Instrument.PIANO;
            case 1:
                return Instrument.BASS_GUITAR;
            case 2:
                return Instrument.BASS_DRUM;
            case 3:
                return Instrument.SNARE_DRUM;
            case 4:
                return Instrument.STICKS;
        }
        return org.bukkit.Instrument.PIANO;
    }
}
