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
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class SongPlayer {

    protected Song song;
    protected boolean playing = false;
    protected short tick = -1;
    protected ArrayList<UUID> playerList = new ArrayList<UUID>();
    protected boolean destroyed = false;
    protected Thread playerThread;

    public SongPlayer(Song song) {
        this.song = song;
        createThread();
    }

    private void createThread() {
        playerThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!destroyed) {
                    long startTime = System.currentTimeMillis();
                    synchronized (SongPlayer.this) {
                        if (playing) {
                            SongPlayer sp = SongPlayer.this;
                            sp.tick = ((short) (sp.tick + 1));
                            if (tick > song.getLength()) {
                                playing = false;
                                tick = -1;
                                SongEndEvent event = new SongEndEvent(SongPlayer.this);
                                Bukkit.getPluginManager().callEvent(event);
                                destroy();
                                return;
                            }
                            for (UUID uuid : playerList) {
                                Player p = Bukkit.getPlayer(uuid);
                                if (p != null) {
                                    playTick(p, tick);
                                }
                            }
                        }
                    }
                    long duration = System.currentTimeMillis() - startTime;
                    float delayMillis = song.getDelay() * 50;
                    if (duration < delayMillis) {
                        try {
                            Thread.sleep((long) delayMillis - duration);
                        } catch (InterruptedException e) {
                        }
                    }
                }
            }
        });
        playerThread.setPriority(10);
        playerThread.start();
    }

    public List<UUID> getPlayerList() {
        return Collections.unmodifiableList(playerList);
    }

    public void addPlayer(Player p) {
        synchronized (this) {
            if (!playerList.contains(p.getUniqueId())) {
                playerList.add(p.getUniqueId());
                List<SongPlayer> songs = NoteBlockPlayer.PLAYING_SONGS.get(p.getUniqueId());
                if (songs == null) {
                    songs = new ArrayList<SongPlayer>();
                }
                songs.add(this);
                NoteBlockPlayer.PLAYING_SONGS.put(p.getUniqueId(), songs);
            }
        }
    }

    public void playTick(Player p, int tick) {
        for (Layer l : song.getLayerHashMap().values()) {
            Note note = l.getNote(tick);
            if (note != null) {
                p.playSound(p.getEyeLocation(), NoteBlockInstrument.getInstrument(note.getInstrument()), l.getVolume() * 100, NotePitch.getPitch(note.getKey() - 33));
            }
        }
    }

    public void destroy() {
        synchronized (this) {
            SongDestroyingEvent event = new SongDestroyingEvent(this);
            Bukkit.getPluginManager().callEvent(event);
            if (event.isCancelled()) {
                return;
            }
            destroyed = true;
            playing = false;
            setTick((short) -1);
        }
    }

    public boolean isPlaying() {
        return playing;
    }

    public void setPlaying(boolean playing) {
        this.playing = playing;
        if (!playing) {
            SongStoppedEvent event = new SongStoppedEvent(this);
            Bukkit.getPluginManager().callEvent(event);
        }
    }

    public short getTick() {
        return tick;
    }

    public void setTick(short tick) {
        this.tick = tick;
    }

    public void removePlayer(Player p) {
        synchronized (this) {
            playerList.remove(p.getUniqueId());
            if (NoteBlockPlayer.PLAYING_SONGS.get(p.getUniqueId()) == null) {
                return;
            }
            List<SongPlayer> songs = new ArrayList<SongPlayer>(NoteBlockPlayer.PLAYING_SONGS.get(p.getUniqueId()));
            songs.remove(this);
            NoteBlockPlayer.PLAYING_SONGS.put(p.getUniqueId(), songs);
            if ((playerList.isEmpty())) {
                SongEndEvent event = new SongEndEvent(this);
                Bukkit.getPluginManager().callEvent(event);
                destroy();
            }
        }
    }
}
