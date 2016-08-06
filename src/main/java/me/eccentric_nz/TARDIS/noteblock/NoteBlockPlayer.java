/*
 * Copyright (C) 2014  michidk && xxmicloxx
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import org.bukkit.entity.Player;

public class NoteBlockPlayer {

    public static final HashMap<UUID, List<SongPlayer>> PLAYING_SONGS = new HashMap<UUID, List<SongPlayer>>();

    public boolean isReceivingSong(Player p) {
        return (PLAYING_SONGS.get(p.getUniqueId()) != null) && (!((ArrayList<SongPlayer>) PLAYING_SONGS.get(p.getUniqueId())).isEmpty());
    }

    public void stopPlaying(Player p) {
        if (PLAYING_SONGS.get(p.getUniqueId()) == null) {
            return;
        }
        for (SongPlayer s : PLAYING_SONGS.get(p.getUniqueId())) {
            s.removePlayer(p);
        }
    }
}
