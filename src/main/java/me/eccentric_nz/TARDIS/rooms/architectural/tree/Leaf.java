package me.eccentric_nz.TARDIS.rooms.architectural.tree;

public class Leaf {

    private final TreeVector position;
    public boolean reached = false;

    public Leaf(TreeVector position) {
        this.position = position;
    }

    public TreeVector getPosition() {
        return position;
    }

    public int getX() {
        return (int) position.x();
    }

    public int getY() {
        return (int) position.y();
    }

    public int getZ() {
        return (int) position.z();
    }
}
