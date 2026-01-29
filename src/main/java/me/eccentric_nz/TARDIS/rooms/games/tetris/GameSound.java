package me.eccentric_nz.TARDIS.rooms.games.tetris;

import org.bukkit.Sound;

public enum GameSound {
	MOVE(Sound.BLOCK_NOTE_BLOCK_XYLOPHONE),
	DROP(Sound.BLOCK_NOTE_BLOCK_BASS),
	ROTATE(Sound.BLOCK_NOTE_BLOCK_SNARE),
	LINE_CLEAR(Sound.BLOCK_NOTE_BLOCK_XYLOPHONE),
	TETRIS(Sound.BLOCK_NOTE_BLOCK_XYLOPHONE),
	LEVEL_UP(Sound.BLOCK_NOTE_BLOCK_XYLOPHONE),
	GAME_OVER(Sound.ENTITY_BLAZE_DEATH),
	PADDLE(Sound.BLOCK_NOTE_BLOCK_HAT),
	POINT(Sound.BLOCK_NOTE_BLOCK_BELL),
	BOUNCE(Sound.BLOCK_NOTE_BLOCK_BASS);

	private final Sound sound;

	GameSound(Sound sound) {
		this.sound = sound;
	}

	public Sound getSound() {
		return sound;
	}
}
