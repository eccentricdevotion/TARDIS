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

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

class SongPlayer {

    private final Song song;
    private final ArrayList<UUID> playerList = new ArrayList<>();
    private boolean playing = false;
    private short tick = -1;
    private boolean destroyed = false;

    SongPlayer(Song song) {
        this.song = song;
        createThread();
    }

    private void createThread() {
        Thread playerThread = new Thread(() -> {
            while (!destroyed) {
                long startTime = System.currentTimeMillis();
                synchronized (SongPlayer.this) {
                    if (playing) {
                        SongPlayer sp = SongPlayer.this;
                        sp.tick = ((short) (sp.tick + 1));
                        if (tick > song.getLength()) {
                            playing = false;
                            tick = -1;
                            destroy();
                            return;
                        }
                        playerList.forEach((uuid) -> {
                            Player p = Bukkit.getPlayer(uuid);
                            if (p != null) {
                                playTick(p, tick);
                            }
                        });
                    }
                }
                long duration = System.currentTimeMillis() - startTime;
                float delayMillis = song.getDelay() * 50;
                if (duration < delayMillis) {
                    try {
                        Thread.sleep((long) delayMillis - duration);
                    } catch (InterruptedException ignored) {
                    }
                }
            }
        });
        playerThread.setPriority(10);
        playerThread.start();
    }

    void addPlayer(Player p) {
        synchronized (this) {
            if (!playerList.contains(p.getUniqueId())) {
                playerList.add(p.getUniqueId());
                List<SongPlayer> songs = NoteBlockPlayer.PLAYING_SONGS.get(p.getUniqueId());
                if (songs == null) {
                    songs = new ArrayList<>();
                }
                songs.add(this);
                NoteBlockPlayer.PLAYING_SONGS.put(p.getUniqueId(), songs);
            }
        }
    }

    private void playTick(Player p, int tick) {
        song.getLayerHashMap().values().forEach((l) -> {
            Note note = l.getNote(tick);
            if (note != null) {
                p.playSound(p.getEyeLocation(), NoteBlockInstrument.getInstrument(note.getInstrument()), l.getVolume() * 100, NotePitch.getPitch(note.getKey() - 33));
            }
        });
    }

    private void destroy() {
        synchronized (this) {
            destroyed = true;
            playing = false;
            setTick((short) -1);
        }
    }

    void setPlaying(boolean playing) {
        this.playing = playing;
    }

    private void setTick(short tick) {
        this.tick = tick;
    }
}
