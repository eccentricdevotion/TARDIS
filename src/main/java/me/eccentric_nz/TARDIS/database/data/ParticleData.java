package me.eccentric_nz.TARDIS.database.data;

import me.eccentric_nz.TARDIS.particles.ParticleBlock;
import me.eccentric_nz.TARDIS.particles.ParticleColour;
import me.eccentric_nz.TARDIS.particles.ParticleEffect;
import me.eccentric_nz.TARDIS.particles.ParticleShape;
import org.bukkit.Color;
import org.bukkit.block.data.BlockData;

public class ParticleData {

    private final ParticleEffect effect;
    private final ParticleShape shape;
    private final int density;
    private final double speed;
    private final Color colour;
    private final BlockData blockData;
    private final boolean on;

    public ParticleData(ParticleEffect effect, ParticleShape shape, int density, double speed, String colour, String block, boolean on) {
        this.effect = effect;
        this.shape = shape;
        this.density = density;
        this.speed = speed;
        this.colour = ParticleColour.fromDatabase(colour);
        this.blockData = ParticleBlock.fromString(block);
        this.on = on;
    }

    public ParticleEffect getEffect() {
        return effect;
    }

    public ParticleShape getShape() {
        return shape;
    }

    public int getDensity() {
        return density;
    }

    public double getSpeed() {
        return speed;
    }

    public Color getColour() {
        return colour;
    }

    public BlockData getBlockData() {
        return blockData;
    }

    public boolean isOn() {
        return on;
    }
}
