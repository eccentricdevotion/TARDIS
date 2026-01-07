package me.eccentric_nz.TARDIS.rooms.architectural.tree;

public class Branch {

    private final Branch parent;
    private final TreeVector originalDirection;
    private final TreeVector position;
    public int count = 0;
    private TreeVector direction;

    public Branch(Branch parent, TreeVector direction, TreeVector position) {
        this.parent = parent;
        this.position = position;
        this.direction = direction;
        this.originalDirection = direction;
    }

    public Branch(Branch anotherBranch) {
        this.parent = anotherBranch;
        this.position = this.parent.next();
        this.direction = this.parent.direction;
        this.originalDirection = this.direction;
    }

    public TreeVector next() {
        TreeVector nextDir = TreeVector.mul(direction, 1);
        return TreeVector.add(position, nextDir);
    }

    public void reset() {
        count = 0;
        direction = originalDirection;
    }

    public Branch getParent() {
        return parent;
    }

    public TreeVector getDirection() {
        return direction;
    }

    public void setDirection(TreeVector d) {
        direction = d;
    }

    public TreeVector getPosition() {
        return position;
    }
}
