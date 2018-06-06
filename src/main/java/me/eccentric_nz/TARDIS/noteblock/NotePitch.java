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

public enum NotePitch {

    NOTE_0(0, 0.5F),
    NOTE_1(1, 0.53F),
    NOTE_2(2, 0.56F),
    NOTE_3(3, 0.6F),
    NOTE_4(4, 0.63F),
    NOTE_5(5, 0.67F),
    NOTE_6(6, 0.7F),
    NOTE_7(7, 0.76F),
    NOTE_8(8, 0.8F),
    NOTE_9(9, 0.84F),
    NOTE_10(10, 0.9F),
    NOTE_11(11, 0.94F),
    NOTE_12(12, 1.0F),
    NOTE_13(13, 1.06F),
    NOTE_14(14, 1.12F),
    NOTE_15(15, 1.18F),
    NOTE_16(16, 1.26F),
    NOTE_17(17, 1.34F),
    NOTE_18(18, 1.42F),
    NOTE_19(19, 1.5F),
    NOTE_20(20, 1.6F),
    NOTE_21(21, 1.68F),
    NOTE_22(22, 1.78F),
    NOTE_23(23, 1.88F),
    NOTE_24(24, 2.0F);

    public int note;
    public float pitch;

    NotePitch(int note, float pitch) {
        this.note = note;
        this.pitch = pitch;
    }

    public static float getPitch(int note) {
        for (NotePitch notePitch : values()) {
            if (notePitch.note == note) {
                return notePitch.pitch;
            }
        }
        return 0.0F;
    }
}
